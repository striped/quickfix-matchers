package org.kot.tool.quickfix;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.field.converter.UtcTimestampConverter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * QuickFIX/J message mocker.
 * <p/>
 * Provides facility for mocking any {@link Message QuickFIX/J message} in convenient way. Client class supposed to be
 * {@link FIXDictionary annotated} with provided path to the QuickFIX/J message dictionary. {@link DataDictionary This
 * dictionary} will be used for {@link #createMessage() instantiation of relevant message} that allows maximally approach
 * a "real life" requirements.
 * <p/>
 * The "mocking" lies in construction of a real QuickFIX/J message but in more convenient way (unlike suggested by
 * design). To do so there are following approach:
 * <p/>
 * <ul>
 *     <li>{@link #imply(String...) imply} - just append FIX field (i.e. &lt;tag&gt;=&lt;value&gt;),</li>
 *     <li>{@link #implyIfNone(String) optionally imply} - append FIX field but only if it was not there and</li>
 *     <li>{@link #redefine(String) redefine} - append FIX field if it was not there else - replace by new.</li>
 * </ul>
 * <p/>
 * All these manipulation can be done at any time and will affect the next QuickFIX/J message instantiation.
 * <p/>
 * Typical usage could be:
 * <pre>
 *     final Message message = new MessageMocker("0") {{
 *         imply("112=request1");
 *     }}.createMessage();
 * </pre>
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 01/12/2011 22:38 $
 * @created 01/12/2011 22:38 by striped
 * @see FIXDictionary
 */
public class MessageMocker {

    static final Map<String, DataDictionary> dictionaries = new HashMap<String, DataDictionary>();

    static final Map<String, MessageFactory> factories = new HashMap<String, MessageFactory>();

    static final char SOH = '\001';

    static final String SOH_STRING = "" + SOH;

    private final DataDictionary dictionary;

    private final StringBuilder text;

    private final Message message;

    /**
     * Create message instance from implied fields.
     * @return The QuickFIX/J message instance.
     * @throws InvalidMessage If message can't be constructed from implied fields.
     */
    public Message createMessage() throws InvalidMessage {
        final Message clone = (Message) message.clone();
        implyIfNone("10=");
        clone.fromString(text.substring(1), dictionary, false);
        return clone;
    }

    /**
     * Constructor with provided MsgType (35) and {@code Source} and {@code Target} as SenderCompID (49) and
     * TargetCompID (56) correspondingly.
     * @param msgType The message type, i.e. MsgType (35)
     * @throws IOException If {@link DataDictionary message dictionary} can't be loaded.
     * @throws ClassNotFoundException If {@link MessageFactory message factory} can't be loaded.
     * @see #MessageMocker(String, String, String)
     */
    protected MessageMocker(final String msgType) throws IOException, ClassNotFoundException {
        this(msgType, "Sender", "Target");
    }

    /**
     * Constructor with provided MsgType (35), SenderCompID (49) and TargetCompID (56).
     * @param msgType The message type, i.e. MsgType (35)
     * @param sender The SenderCompID (49).
     * @param target The TargetCompID (56).
     * @throws IOException If {@link DataDictionary message dictionary} can't be loaded.
     * @throws ClassNotFoundException If {@link MessageFactory message factory} can't be loaded.
     */
    protected MessageMocker(final String msgType, final String sender, final String target)
            throws IOException, ClassNotFoundException {
        this.text = new StringBuilder();
        this.dictionary = getDictionary(getClass());
        final MessageFactory factory = factories.get(dictionary.getVersion());
        this.message = factory.create(dictionary.getVersion(), msgType);
        text.append(SOH);
        imply("8=" + dictionary.getVersion(), "9=", "35=" + msgType);
        imply("49=" + sender, "56=" + target);
        imply("52=" + UtcTimestampConverter.convert(new Date(), false));
    }

    /**
     * Add provided fields that will be implied on next {@link #createMessage()} invocation.
     * @param fields Fields in form &lt;tag&gt;=&lt;value&gt;.
     */
    protected void imply(final String... fields) {
        for (String pair : fields) {
            append(pair);
        }
    }

    /**
     * Same as {@link #imply(String...)} but field added only if it was not added before.
     * @param field Field in form &lt;tag&gt;=&lt;value&gt;.
     */
    protected void implyIfNone(final String field) {
        final int pos = field.indexOf('=');
        if (0 <= pos && 0 > text.indexOf(SOH_STRING + field.substring(0, pos + 1))) {
            append(field);
        }
    }

    /**
     * Same as {@link #imply(String...)} but field may override previous one if it was added before.
     * @param field Field in form &lt;tag&gt;=&lt;value&gt;.
     */
    protected void redefine(final String field) {
        final int pos = field.indexOf('=');
        if (0 > pos) {
            return;
        }
        int start = text.indexOf(SOH_STRING + field.substring(0, pos));
        if (0 > start) {
            append(field);
        } else {
            start++;
            final int end = text.indexOf(SOH_STRING, start);
            text.replace(start, end, field);
        }
    }

    private void append(final String field) {
        text.append(field).append(SOH);
    }

    static DataDictionary getDictionary(final Class<?> clazz) throws IOException, ClassNotFoundException {
        FIXDictionary annotation = clazz.getEnclosingMethod().getAnnotation(FIXDictionary.class);
        if (null == annotation) {
            annotation = clazz.getEnclosingClass().getAnnotation(FIXDictionary.class);
        }
        assert null != annotation : clazz + " should be annotated by " + FIXDictionary.class
                                    + " on method or class level";
        final String path = annotation.path();
        if (dictionaries.containsKey(path)) {
            return dictionaries.get(path);
        }
        try {
            final DataDictionary dictionary = new DataDictionary(path);
            dictionaries.put(path, dictionary);
            final String version = dictionary.getVersion();
            if (!factories.containsKey(version)) {
                factories.put(version, (MessageFactory) Class.forName(toClassName(version)).newInstance());
            }
            return dictionary;
        } catch (ConfigError e) {
            throw new IOException(e.getMessage(), e.getCause());
        } catch (InstantiationException e) {
            throw new ClassNotFoundException("Failed to create a message factory", e);
        } catch (IllegalAccessException e) {
            throw new ClassNotFoundException("Insufficient permission to create a message factory", e);
        }
    }

    static String toClassName(final CharSequence chars) {
        assert null != chars : "Nothing to convert: is NULL";

        final StringBuilder result = new StringBuilder("quickfix.");
        for (int i = 0; i < chars.length(); i++) {
            final char c = chars.charAt(i);
            if ('.' != c) {
                result.append(Character.toLowerCase(c));
            }
        }
        result.append(".MessageFactory");
        return result.toString();
    }
}
