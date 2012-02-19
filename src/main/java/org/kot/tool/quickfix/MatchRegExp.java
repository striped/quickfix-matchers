package org.kot.tool.quickfix;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.regex.Pattern;

/**
 * Checks string matches provided regular expression.
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 04/12/2011 14:21 $
 * @created 04/12/2011 14:21 by striped
 */
public class MatchRegExp  extends TypeSafeMatcher<String> {

    private final Pattern pattern;

    /**
     * Constructor with regular expression.
     * @param regexp The regular expression.
     */
    public MatchRegExp(final String regexp) {
        pattern = Pattern.compile(regexp);
    }

    /**
     * Checks string matches specified regular expression.
     * @param pattern The regular expression
     * @return the matcher instance.
     */
    @Factory
    public static Matcher<String> matches(final String pattern) {
        return new MatchRegExp(pattern);
    }

    /**
     * Description of this according to {@link Matcher#describeTo(org.hamcrest.Description) Matcher contract}
     * @param description The description to be append to.
     */
    public void describeTo(final Description description) {
        description.appendText("matches ").appendValue(pattern).appendText(" pattern");
    }

    @Override
    protected boolean matchesSafely(final String item) {
        return pattern.matcher(item).matches();
    }
}
