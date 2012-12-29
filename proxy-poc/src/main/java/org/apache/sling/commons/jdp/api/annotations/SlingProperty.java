package org.apache.sling.commons.jdp.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author kellehmj - Dec 29, 2012 5:01:32 PM 
 *
 * proxy-poc
 *
 * Used to annotate Method names with the property name or relative path plus 
 * property name of a JCR Property.  This means that the JCR Property can be
 * located within the corresponding org.apache.sling.api.resource.Resource 
 * directly, or some arbitrary path beneath the 
 * org.apache.sling.api.resource.Resource.
 *
 * org.apache.sling.commons.jdp.api.annotations.SlingProperty
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SlingProperty {
	/**
	 * The property name or relative path plus property name
	 * @return String - the property name and optionally the relative path.
	 */
	String path() default "";
}
