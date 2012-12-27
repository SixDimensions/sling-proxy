package org.apache.sling.commons.jdp.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface OSGiService {
	/**
	 * @return Class<?> - the corresponding Interface that we implement - when
	 * searching for instances of the OSGi service, this will allow us to 
	 * correlate the implemented interface to an OSGi Service Filter or a 
	 * specific pid.
	 */
	Class<?> service() default Void.class;
	/**
	 * The pid to use in the Search for an implementation for the related 
	 * interface in 'service()'.  This take precedence over 'filter()'
	 * @return String - the OSGi Service pid for the implementation
	 */
	String pid() default "";
	/**
	 * The OSGi Service Filter to use when searching for an implementation for 
	 * the related interface in 'service()'.  'pid()' takes precedence over this
	 * value.
	 * @return String - The OSGi search Filter for the implementation
	 */
	String filter() default "";
	/**
	 * The specific implementation to use.  'pid()' and 'filter()' take 
	 * precendence over this value.  This is primarily for testing or usage 
	 * when we are not running within an OSGi container.  If we are not, we
	 * obviously cannot perform an OSGi search for a Service. 
	 * @return Class<?> the implementation Class
	 */
	Class<?> implementation() default Void.class;
}
