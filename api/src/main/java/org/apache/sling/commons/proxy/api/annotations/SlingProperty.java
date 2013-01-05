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
package org.apache.sling.commons.proxy.api.annotations;

import java.lang.annotation.*;

/**
 * Annotation used to mark a Method as being a JCR backed property.  All JCR
 * backed methods must use JavaBean style naming to identify them as either
 * a 'getter' (getSomeValue, isSomevalue) or a JavaBean 'setter' 'setSomeValue'.
 * 
 * 'name' and 'path' are both optional.  If both are missing and/or empty, then
 * the method name will be used to determine the corresponding JCR property
 * name.  In this case, namespaced property names are handled this way:
 * 
 * @SlingProperty
 * Date getCq_lastReplicated();
 * 
 * this will correspond to a JCR property name of:  'cq:lastReplicated'
 * 
 * The ":" in the property name must be represented as a "_" underscore in the
 * Property name.
 * 
 * This style of property name identification will limit properties to being
 * located immediately within the associated Resource Object.
 * 
 * It is recommended to use 'name' and 'path' values as this enables the use of
 * extravagant property names located anywhere within the JCR, not simply 
 * within the current Resource.
 * 
 * Property names starting with '/' are absolute references, and do not have to 
 * be contained beneath the current Resource.  Property names not starting with
 * '/' are assumed to be relative to the current Resource.
 * 
 * Here are 3 examples, one of each style:
 * 
 *   @SlingProperty(name = "cq:lastReplicated")
 *   Date getLastReplicated();
 * 
 *   @SlingProperty(path = "par/image", name = "fileReference")
 *   String getImagePath(); 
 * 
 *   @SlingProperty(path="/content/dam/geometrixx/documents/GeoCube_Datasheet.pdf/jcr:content/renditions/original/jcr:content", name="jcr:data")
 *   InputStream getGeoCubePDF();
 * 
 * @author dklco
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface SlingProperty {

	/**
	 * The name of the property to retrieve from Sling.
	 * 
	 * @return the name of the property to retrieve
	 */
	String name() default "";

	/**
	 * The path to the property to retrieve, if it begins with '/' it will 
	 * be treated as an absolute path, otherwise, it will be treated as a 
	 * relative path.
	 * 
	 * @return the path to the property to retrieve
	 */
	String path() default "";
}
