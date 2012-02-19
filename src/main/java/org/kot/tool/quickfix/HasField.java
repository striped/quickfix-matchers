package org.kot.tool.quickfix;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNull;
import quickfix.FieldMap;
import quickfix.FieldNotFound;
import quickfix.Message;

/**
 * Checks conditions against field of QuickFIX/J message.
 * <p/>
 * Allows check whether certain field exists in specified message and test if its value matches to provided conditions
 * (by other {@link Matcher matchers}).
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 01/12/2011 10:05 $
 * @created 01/12/2011 10:05 by striped
 */
public class HasField extends DiagnosingMatcher<FieldMap> {

    private final int field;

    private final Matcher<String> matcher;

    /**
     * Constructor with field number and matcher.
     * @param field The field number (tag) which value need to check against.
     * @param matcher The textual matcher.
     */
    public HasField(final int field, final Matcher<String> matcher) {
        this.field = field;
        this.matcher = matcher;
    }

    /**
     * Description of this according to {@link Matcher#describeTo(org.hamcrest.Description) Matcher contract}
     * @param description The description to be append to.
     */
    public void describeTo(final Description description) {
        description.appendText("has field ").appendValue(field).appendText(" that ");
        matcher.describeTo(description);
    }

    /**
     * Field presence check.
     * @param field The field number (i.e. tag).
     * @return Matcher that checks field presence.
     */
    @Factory
    public static Matcher<FieldMap> hasField(final int field) {
        return hasFieldThat(field, IsNull.notNullValue(String.class));
    }

    /**
     * Field value check with provided matcher.
     * @param field The field number (i.e. tag).
     * @param matcher The matcher to check against.
     * @return Matcher that checks field value against provided matcher.
     */
    @Factory
    public static Matcher<FieldMap> hasFieldThat(final int field, final Matcher<String> matcher) {
        return new HasField(field, matcher);
    }

    @Override
    protected boolean matches(final Object object, final Description mismatchDescription) {
        if (!(object instanceof FieldMap)) {
            mismatchDescription.appendText("there is nothing to check");
            return false;
        }
        final FieldMap message = (FieldMap) object;
        final String value = getFieldValue(message);
        if (!matcher.matches(value)) {
            mismatchDescription.appendText("it ");
            matcher.describeMismatch(value, mismatchDescription);
            return false;
        }
        return true;
    }

    protected String getFieldValue(final FieldMap fields) {
        try {
            return fields.getString(field);
        } catch (FieldNotFound e) {
            if (fields instanceof Message) {
                final Message message = (Message) fields;
                final String value = getFieldValue(message.getHeader());
                if (null != value) {
                    return value;
                }
                return getFieldValue(message.getTrailer());
            }
            return null;
        }
    }
}
