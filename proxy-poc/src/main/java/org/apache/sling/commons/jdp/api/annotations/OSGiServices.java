/**
 * 
 */
package org.apache.sling.commons.jdp.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kellehmj - Dec 29, 2012 4:54:09 PM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.jdp.api.annotations.OSGiServices
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OSGiServices {
	/**
	 * List of {@link OSGiService} annotations
	 * @return OSGiService[] - an array of OSGiService annotations 
	 */
	OSGiService[] value();
}
