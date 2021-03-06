/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.sling.commons.proxy.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a Method as being a JCR backed property. All JCR
 * backed methods must use JavaBean style naming to identify them as either a
 * 'getter' (getSomeValue, isSomevalue) or a JavaBean 'setter' 'setSomeValue'.
 * 
 * 'name' and 'path' are both optional. If both are missing and/or empty, then
 * the method name will be used to determine the corresponding JCR property
 * name. In this case, namespaced property names are handled this way:
 * 
 * &#64;SlingProperty<br/>
 * Date getCq_lastReplicated();
 * 
 * this will correspond to a JCR property name of: 'cq:lastReplicated'
 * 
 * The ":" in the property name must be represented as a "_" underscore in the
 * Property name.
 * 
 * This style of property name identification will limit properties to being
 * located immediately within the associated Resource Object.
 * 
 * It is recommended to use 'name' and 'path' values as this enables the use of
 * extravagant property names located anywhere within the JCR, not simply within
 * the current Resource.
 * 
 * Property names starting with '/' are absolute references, and do not have to
 * be contained beneath the current Resource. Property names not starting with
 * '/' are assumed to be relative to the current Resource.
 * 
 * Here are 3 examples, one of each style:
 * 
 * <code><br/>
 *   &#64;SlingProperty(name = "cq:lastReplicated")<br/>
 *   Date getLastReplicated();
 * <br/><br/>
 *   &#64;SlingProperty(path = "par/image", name = "fileReference")<br/>
 *   String getImagePath(); 
 * <br/><br/>
 *   &#64;SlingProperty(path="/content/dam/geometrixx/documents/GeoCube_Datasheet.pdf/jcr:content/renditions/original/jcr:content", name="jcr:data")<br/>
 *   InputStream getGeoCubePDF();<br/>
 * </code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface SlingProperty {

	/**
	 * The default boolean, used to provide a default for booleans.
	 * 
	 * @return the default boolean
	 */
	boolean defaultBoolean() default false;

	/**
	 * The default byte array, used to provide a default when the method returns
	 * an InputStream or a byte array.
	 * 
	 * @return the default bytes
	 */
	byte[] defaultBytes() default {};

	/**
	 * The default date, used to set the default value for Calendars, Dates &
	 * Long Timestamps.
	 * 
	 * @return the default date
	 */
	long defaultDate() default -1;

	/**
	 * The default double, used to set the default values for doubles.
	 * 
	 * @return the default double
	 */
	double defaultDouble() default 0.0;

	/**
	 * The default long, used to set the default values for long.
	 * 
	 * @return the default long
	 */
	long defaultLong() default 0L;

	/**
	 * The default String, used to set the default String value.
	 * 
	 * @return the default String
	 */
	String defaultString() default "";

	/**
	 * The default String array, used to set the default String array value.
	 * 
	 * @return the default String array
	 */
	String[] defaultStrings() default {};

	/**
	 * The name of the property to retrieve from Sling.
	 * 
	 * @return the name of the property to retrieve
	 */
	String name() default "";

	/**
	 * The path to the property to retrieve, if it begins with '/' it will be
	 * treated as an absolute path, otherwise, it will be treated as a relative
	 * path.
	 * 
	 * @return the path to the property to retrieve
	 */
	String path() default "";

	/**
	 * Use a default value instead of just casting.
	 * 
	 * @return the use default flag
	 */
	boolean useDefault() default false;
}
