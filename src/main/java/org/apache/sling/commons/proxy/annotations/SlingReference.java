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
 * Annotation used to mark a Method as referencing another resource. The path is
 * required paths starting with '/' are absolute references, and do not have to
 * be contained beneath the current Resource. Paths not starting with '/' are
 * assumed to be relative to the current Resource.
 * 
 * The Resource at the path will be returned depending on the return type of the
 * method, according to the following rules (in order):
 * 
 * <ol>
 * <li>If the return type is an instance of a Resource, the resource at the path
 * will be returned</li>
 * <li>If adapting the resource into the return class returns a non-null, this
 * object will be returned</li>
 * <li>If using the ProxyService to retrieve a proxy instance does not throw and
 * error and does not return null, the resulting object will returned</li>
 * <li>Otherwise, null will be returned</li>
 * </ol> 
 * 
 * Here are 3 examples, showing valid return types:
 * 
 * <code><br/>
 * &#64;SlingReference(path = "/content/page")<br/>
 * Resource getPage();<br/><br/>
 * 
 * &#64;SlingReference(path = "/content/page")<br/>
 * ValueMap getPageProperties();<br/><br/>
 * 
 * &#64;SlingReference(path = "/content/page")
 * IPageProxy getPageProxy();
 * </code>
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface SlingReference {

	/**
	 * The path to the resource being referenced, if it begins with '/' it will
	 * be treated as an absolute path, otherwise, it will be treated as a
	 * relative path.
	 * 
	 * @return the path to the resource being referenced
	 */
	String path();
}
