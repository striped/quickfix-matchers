package org.kot.tool.quickfix;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import quickfix.FieldNotFound;
import quickfix.InvalidMessage;
import quickfix.Message;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.kot.tool.quickfix.HasField.*;

/**
 * Tests different scenarios of {@link HasField field matcher}.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 01/12/2011 11:32 $
 * @created 01/12/2011 11:32 by striped
 */
@FIXDictionary(path = "FIX44.xml")
public class HasFieldTest {

    @Test
    public void testNull() {
        assertThat(null, not(hasFieldThat(8, is("FIX.4.4"))));
    }

    @Test
    public void testAbsentField() throws InvalidMessage, ClassNotFoundException, IOException {
        final Message message = new MessageMocker("0") {}.createMessage();
        assertThat(message, not(hasField(112)));
    }

    @Test
    public void testUnexpectedFieldValue() throws FieldNotFound, InvalidMessage, ClassNotFoundException, IOException {
        final Message message = new MessageMocker("0") {{
            redefine("8=FIX.4.2");
        }}.createMessage();

        assertThat(message, not(hasFieldThat(8, is("FIX.4.4"))));
    }

    @Test
    public void testExpectedFieldValue() throws FieldNotFound, InvalidMessage, ClassNotFoundException, IOException {
        final Message message = new MessageMocker("0") {{
            imply("112=request1");
        }}.createMessage();

        assertThat(message, hasFieldThat(8, is("FIX.4.4")));
    }

    @Test
    public void testDescriptionOnNotNull() {
        final Matcher object = hasField(8);

        Description description = new StringDescription();
        description.appendDescriptionOf(object);
        assertThat(description.toString(), is("has field <8> that not null"));

        description = new StringDescription();
        object.describeMismatch(null, description);
        assertThat(description.toString(), is("there is nothing to check"));
    }

    @Test
    public void testDescriptionOnExpectedValue() throws FieldNotFound, InvalidMessage, ClassNotFoundException, IOException {
        final Matcher object = HasField.hasFieldThat(8, is("FIX.4.4"));

        Description description = new StringDescription();
        description.appendDescriptionOf(object);
        assertThat(description.toString(), is("has field <8> that is \"FIX.4.4\""));

        description = new StringDescription();
        object.describeMismatch(null, description);
        assertThat(description.toString(), is("there is nothing to check"));

        final Message message = new MessageMocker("0") {{
            redefine("8=FIX.4.2");
        }}.createMessage();

        description = new StringDescription();
        object.describeMismatch(message, description);
        assertThat(description.toString(), is("it was \"FIX.4.2\""));
    }
}
