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
import static org.kot.tool.quickfix.HasGroup.*;

/**
 * Tests different scenarios of {@link HasGroup field group matcher}.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 01/12/2011 15:07 $
 * @created 01/12/2011 15:07 by striped
 */
@FIXDictionary(path = "FIX44.xml")
public class HasGroupTest {

    @Test
    public void testNullMessage() {
        assertThat(null, not(hasGroup(268)));
    }

    @Test
    public void testAbsentGroup() throws InvalidMessage, ClassNotFoundException, IOException {
        final Message message = new MessageMocker("W") {{
            imply("55=EUR/USD");
        }}.createMessage();

        assertThat(message, not(hasGroup(268)));
    }

    @Test
    public void testAbsentFieldValue() throws FieldNotFound, InvalidMessage, ClassNotFoundException, IOException {
        final Message message = new MessageMocker("W") {{
            imply("55=EUR/USD", "268=1", "269=1");
        }}.createMessage();

        assertThat(message, not(hasGroupThat(268, hasField(270))));
    }

    @Test
    public void testUnexpectedFieldValue() throws FieldNotFound, InvalidMessage, ClassNotFoundException, IOException {
        final Message message = new MessageMocker("W") {{
            imply("55=EUR/USD", "268=1", "269=1");
        }}.createMessage();

        assertThat(message, not(hasGroupThat(268, hasFieldThat(270, is("0")))));
    }

    @Test
    public void testExpectedFieldValue() throws FieldNotFound, InvalidMessage, ClassNotFoundException, IOException {
        final Message message = new MessageMocker("W") {{
            imply("55=EUR/USD", "268=2");
            imply("269=1", "270=1.31", "271=1000");
            imply("269=2", "270=1.29", "271=1000");
        }}.createMessage();

        assertThat(message, hasGroupThat(268, hasFieldThat(269, is("1"))));
        assertThat(message, hasGroupThat(268, 1, hasFieldThat(269, is("2"))));
    }

    @Test
    public void testDescriptionOnNotNull() {
        final Matcher object = hasGroup(268, 0);

        Description description = new StringDescription();
        description.appendDescriptionOf(object);
        assertThat(description.toString(), is("has group <268> that not null"));

        description = new StringDescription();
        object.describeMismatch(null, description);
        assertThat(description.toString(), is("there is nothing to check"));
    }

    @Test
    public void testDescriptionOnExpectedValueInFirstGroup() throws FieldNotFound, InvalidMessage, ClassNotFoundException, IOException {
        Matcher object = hasGroupThat(268, hasFieldThat(269, is("1")));

        Description description = new StringDescription();
        description.appendDescriptionOf(object);
        assertThat(description.toString(), is("has group <268> that has field <269> that is \"1\""));

        description = new StringDescription();
        object.describeMismatch(null, description);
        assertThat(description.toString(), is("there is nothing to check"));

        Message message = new MessageMocker("W") {{
            imply("55=EUR/USD", "268=0");
        }}.createMessage();

        description = new StringDescription();
        object.describeMismatch(message, description);
        assertThat(description.toString(), is("there is no group <268>"));

        message = new MessageMocker("W") {{
            imply("55=EUR/USD", "268=1");
            imply("269=2", "270=1.31", "271=1000");
        }}.createMessage();

        description = new StringDescription();
        object.describeMismatch(message, description);
        assertThat(description.toString(), is("it was \"2\""));
    }

    @Test
    public void testDescriptionOnExpectedValueInOtherGroup() throws FieldNotFound, InvalidMessage, ClassNotFoundException, IOException {
        Matcher object = hasGroupThat(268, 2, hasFieldThat(269, is("1")));

        Description description = new StringDescription();
        description.appendDescriptionOf(object);
        assertThat(description.toString(), is("has <3> instance of group <268> that has field <269> that is \"1\""));

        description = new StringDescription();
        object.describeMismatch(null, description);
        assertThat(description.toString(), is("there is nothing to check"));

        Message message = new MessageMocker("W") {{
            imply("55=EUR/USD", "268=0");
        }}.createMessage();

        description = new StringDescription();
        object.describeMismatch(message, description);
        assertThat(description.toString(), is("there is no group <268>"));

        message = new MessageMocker("W") {{
            imply("55=EUR/USD", "268=1");
            imply("269=2", "270=1.31", "271=1000");
        }}.createMessage();

        description = new StringDescription();
        object.describeMismatch(message, description);
        assertThat(description.toString(), is("there is only <1> instance(s) of group <268>"));

        message = new MessageMocker("W") {{
            imply("55=EUR/USD", "268=3");
            imply("269=1", "270=1.31", "271=1000");
            imply("269=2", "270=1.29", "271=1000");
            imply("269=3", "270=1.29", "271=1000");
        }}.createMessage();

        description = new StringDescription();
        object.describeMismatch(message, description);
        assertThat(description.toString(), is("it was \"3\""));
    }
}
