package org.kot.tool.quickfix;

import org.junit.Test;
import quickfix.InvalidMessage;
import quickfix.Message;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.kot.tool.quickfix.HasField.*;
import static org.kot.tool.quickfix.MatchRegExp.matches;

/**
 * Tests different scenarios of {@link MessageMocker messge mocker} usage.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 06/12/2011 23:38 $
 * @created 06/12/2011 23:38 by striped
 */
@FIXDictionary(path = "FIX44.xml")
public class MessageMockerTest {

    @FIXDictionary(path = "FIX44.xml")
    @Test
    public void testInitialState() throws ClassNotFoundException, IOException, InvalidMessage {
        final MessageMocker object = new MessageMocker("0") {};
        final Message message = object.createMessage();
        assertThat(message, hasFieldThat(8, is("FIX.4.4")));
        assertThat(message, hasField(9));
        assertThat(message, hasFieldThat(35, is("0")));
        assertThat(message, hasFieldThat(49, is("Sender")));
        assertThat(message, hasField(52));
        assertThat(message, hasFieldThat(56, is("Target")));
        assertThat(message, hasField(10));
    }

    @Test
    public void testInitialState1() throws ClassNotFoundException, IOException, InvalidMessage {
        final MessageMocker object = new MessageMocker("0", "Src", "Dst") {};
        final Message message = object.createMessage();
        assertThat(message, hasFieldThat(8, is("FIX.4.4")));
        assertThat(message, hasField(9));
        assertThat(message, hasFieldThat(35, is("0")));
        assertThat(message, hasFieldThat(49, is("Src")));
        assertThat(message, hasField(52));
        assertThat(message, hasFieldThat(56, is("Dst")));
        assertThat(message, hasField(10));
    }

    @Test
    public void testImplyIfNone() throws ClassNotFoundException, IOException, InvalidMessage {
        final MessageMocker object = new MessageMocker("0") {{
            implyIfNone("112=request1");
            implyIfNone("112=request2");
            implyIfNone("113");  // wrong data, should be ignored
        }};
        final Message message = object.createMessage();
        assertThat(message, hasFieldThat(112, is("request1")));
        assertThat(message, not(hasField(113)));
    }

    @Test
    public void testRedefine() throws ClassNotFoundException, IOException, InvalidMessage {
        final MessageMocker object = new MessageMocker("0") {{
            redefine("8=FIX.4.2");
            redefine("52=20000101:00:00:00");
            redefine("112=request1");
            redefine("113");  // wrong data, should be ignored
        }};
        final Message message = object.createMessage();
        assertThat(message, hasFieldThat(8, is("FIX.4.2")));
        assertThat(message, hasFieldThat(52, matches("20000101:00:00:00")));
        assertThat(message, hasFieldThat(112, is("request1")));
        assertThat(message, not(hasField(113)));
    }
}
