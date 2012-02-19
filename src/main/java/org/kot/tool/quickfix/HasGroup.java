package org.kot.tool.quickfix;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsNull;
import quickfix.FieldMap;
import quickfix.Group;

import java.util.List;

/**
 * Checks conditions against field group of QuickFIX/J message.
 * <p/>
 * Allows check whether certain field group exists in specified message and test if its value matches to provided
 * conditions (by other {@link Matcher matchers}).
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 01/12/2011 10:05 $
 * @created 01/12/2011 10:05 by striped
 */
public class HasGroup extends DiagnosingMatcher<FieldMap> {

    private int group;

    private int idx;

    private Matcher<FieldMap> matcher;

    /**
     * Constructs matcher with field tag, its index and condition to check against it.
     * @param group The group tag and.
     * @param idx The index of group to be checked.
     * @param matcher The matcher.
     */
    public HasGroup(final int group, final int idx, final Matcher<FieldMap> matcher) {
        this.group = group;
        this.idx = idx;
        this.matcher = matcher;
    }

    /**
     * Description of this according to {@link Matcher#describeTo(org.hamcrest.Description) Matcher contract}
     * @param description The description to be append to.
     */
    public void describeTo(final Description description) {
        description.appendText("has ");
        if (0 < idx) {
            description.appendValue(idx + 1).appendText(" instance of ");
        }
        description.appendText("group ").appendValue(group).appendText(" that ");
        matcher.describeTo(description);
    }

    /**
     * Group presence check.
     * @param field The field group number (i.e. tag).
     * @return Matcher that checks field group presence.
     */
    @Factory
    public static Matcher<FieldMap> hasGroup(final int field) {
        return hasGroup(field, 0);
    }

    /**
     * <var>idx</var>th group presence check.
     * @param field The field group number (i.e. tag) and
     * @param idx its index (0, 1, 2 and so on) in list.
     * @return Matcher that checks field group presence.
     */
    @Factory
    public static Matcher<FieldMap> hasGroup(final int field, final int idx) {
        return new HasGroup(field, idx, IsNull.notNullValue(FieldMap.class));
    }

    /**
     * Group check with provided matcher.
     * @param field The field group number (i.e. tag) and
     * @param matcher The matcher against which to be checked.
     * @return Matcher that checks field group against provided matcher.
     */
    @Factory
    public static Matcher<FieldMap> hasGroupThat(final int field, final Matcher<FieldMap> matcher) {
        return hasGroupThat(field, 0, matcher);
    }

    /**
     * <var>idx</var>th group check against provided matcher.
     * @param field The field group number (i.e. tag) and
     * @param idx its index (0, 1, 2 and so on) in list.
     * @param matcher The matcher against which to be checked.
     * @return Matcher that checks field group against provided matcher.
     */
    @Factory
    public static Matcher<FieldMap> hasGroupThat(final int field, final int idx, final Matcher<FieldMap> matcher) {
        return new HasGroup(field, idx, matcher);
    }

    @Override
    protected boolean matches(final Object object, final Description mismatchDescription) {
        if (!(object instanceof FieldMap)) {
            mismatchDescription.appendText("there is nothing to check");
            return false;
        }
        FieldMap message = (FieldMap) object;
        final List<Group> groups = message.getGroups(group);
        if (idx >= groups.size()) {
            mismatchDescription.appendText("there is ");
            if (0 < groups.size()) {
                mismatchDescription.appendText("only ").appendValue(groups.size()).appendText(" instance(s) of ");
            } else {
                mismatchDescription.appendText("no ");
            }
            mismatchDescription.appendText("group ").appendValue(group);
            return false;
        }
        final Group value = groups.get(idx);
        if (!matcher.matches(value)) {
            matcher.describeMismatch(value, mismatchDescription);
            return false;
        }
        return true;
    }
}
