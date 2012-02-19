package org.kot.tool.quickfix;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * QickFIX/J message dictionary describer.
 * <p/>
 * Provides location information about QuickFIX/J message dictionary:
 * <pre>
 *     &#64;FIXDictionary(path = "FIX44.xml")
 *     public class Test {
 *
 *         &#64;Test
 *         &#64;FIXDictionary(path = "FIX41.xml")
 *         public void test() {
 *             ...
 *         }
 *     }
 * </pre>
 * @author <a href="mailto:striped@gmail.com">Val</a>
 * @version $Revision: 1.0 $ $Date: 02/12/2011 21:14 $
 * @created 02/12/2011 21:14 by striped
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface FIXDictionary {
    String path();
}
