// Generated source.
package org.kot.tool.quickfix;

public class Matchers {

  public static <T> org.hamcrest.Matcher<T> is(org.hamcrest.Matcher<T> p0) {
    return org.hamcrest.core.Is.<T>is(p0);
  }

  public static <T> org.hamcrest.Matcher<T> is(T param1) {
    return org.hamcrest.core.Is.<T>is(param1);
  }

  public static <T> org.hamcrest.Matcher<T> is(java.lang.Class<T> p0) {
    return org.hamcrest.core.Is.<T>is(p0);
  }

  public static <T> org.hamcrest.Matcher<T> isA(java.lang.Class<T> p0) {
    return org.hamcrest.core.Is.<T>isA(p0);
  }

  public static org.hamcrest.Matcher<java.lang.String> isEmptyString() {
    return org.hamcrest.text.IsEmptyString.isEmptyString();
  }

  public static org.hamcrest.Matcher<java.lang.String> isEmptyOrNullString() {
    return org.hamcrest.text.IsEmptyString.isEmptyOrNullString();
  }

  public static org.hamcrest.Matcher<java.lang.String> equalToIgnoringCase(java.lang.String p0) {
    return org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase(p0);
  }

  public static org.hamcrest.Matcher<java.lang.String> equalToIgnoringWhiteSpace(java.lang.String p0) {
    return org.hamcrest.text.IsEqualIgnoringWhiteSpace.equalToIgnoringWhiteSpace(p0);
  }

  public static org.hamcrest.Matcher<java.lang.String> stringContainsInOrder(java.lang.Iterable<java.lang.String> p0) {
    return org.hamcrest.text.StringContainsInOrder.stringContainsInOrder(p0);
  }

  /**
   * Checks string matches specified regular expression.
   * 
   * @param pattern The regular expression
   * @return the matcher instance.
   */
  public static org.hamcrest.Matcher<java.lang.String> matches(java.lang.String pattern) {
    return org.kot.tool.quickfix.MatchRegExp.matches(pattern);
  }

  /**
   * Field presence check.
   * 
   * @param field The field number (i.e. tag).
   * @return Matcher that checks field presence.
   */
  public static org.hamcrest.Matcher<quickfix.FieldMap> hasField(int field) {
    return org.kot.tool.quickfix.HasField.hasField(field);
  }

  /**
   * Field value check with provided matcher.
   * 
   * @param field The field number (i.e. tag).
   * @param matcher The matcher to check against.
   * @return Matcher that checks field value against provided matcher.
   */
  public static org.hamcrest.Matcher<quickfix.FieldMap> hasFieldThat(int field, org.hamcrest.Matcher<java.lang.String> matcher) {
    return org.kot.tool.quickfix.HasField.hasFieldThat(field, matcher);
  }

  /**
   * Group presence check.
   * 
   * @param field The field group number (i.e. tag).
   * @return Matcher that checks field group presence.
   */
  public static org.hamcrest.Matcher<quickfix.FieldMap> hasGroup(int field) {
    return org.kot.tool.quickfix.HasGroup.hasGroup(field);
  }

  /**
   * <var>idx</var>th group presence check.
   * 
   * @param field The field group number (i.e. tag) and
   * @param idx its index (0, 1, 2 and so on) in list.
   * @return Matcher that checks field group presence.
   */
  public static org.hamcrest.Matcher<quickfix.FieldMap> hasGroup(int field, int idx) {
    return org.kot.tool.quickfix.HasGroup.hasGroup(field, idx);
  }

  /**
   * Group check with provided matcher.
   * 
   * @param field The field group number (i.e. tag) and
   * @param matcher The matcher against which to be checked.
   * @return Matcher that checks field group against provided matcher.
   */
  public static org.hamcrest.Matcher<quickfix.FieldMap> hasGroupThat(int field, org.hamcrest.Matcher<quickfix.FieldMap> matcher) {
    return org.kot.tool.quickfix.HasGroup.hasGroupThat(field, matcher);
  }

  /**
   * <var>idx</var>th group check against provided matcher.
   * 
   * @param field The field group number (i.e. tag) and
   * @param idx its index (0, 1, 2 and so on) in list.
   * @param matcher The matcher against which to be checked.
   * @return Matcher that checks field group against provided matcher.
   */
  public static org.hamcrest.Matcher<quickfix.FieldMap> hasGroupThat(int field, int idx, org.hamcrest.Matcher<quickfix.FieldMap> matcher) {
    return org.kot.tool.quickfix.HasGroup.hasGroupThat(field, idx, matcher);
  }

}
