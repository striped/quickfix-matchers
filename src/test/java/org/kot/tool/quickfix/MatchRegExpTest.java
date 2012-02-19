package org.kot.tool.quickfix;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.not;
import static org.kot.tool.quickfix.MatchRegExp.*;

/**
 * Tests different scenarios of {@link MatchRegExp regular expression matcher}
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 04/12/2011 14:38 $
 * @created 04/12/2011 14:38 by striped
 * @todo add JavaDoc
 */
public class MatchRegExpTest {

    @Test
    public void testExpected() {
        assertThat("something", matches(".+"));
    }

    @Test
    public void testNotExpected() {
        assertThat("something", not(matches(".{6}")));
    }

    @Test
    public void testDescriptionOnNotNull() {
        final Matcher object = matches(".+");

        Description description = new StringDescription();
        description.appendDescriptionOf(object);
        assertThat(description.toString(), is("matches <.+> pattern"));

        description = new StringDescription();
        object.describeMismatch("", description);
        assertThat(description.toString(), is("was \"\""));
    }
}
