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
package org.apache.sling.commons.proxy.impl;

import org.apache.sling.commons.proxy.impl.lang.MethodType;

/**
 * Represents a method invocation on the InvocationHandler.
 */
public interface InvokedTO {

	public static final InvokedTO UNKNOWN = new InvokedPropertyTO(null, null,
			null, null, null, MethodType.Unknown);

	/**
	 * Determines if the item represented by this method Invocation is an
	 * absolute path reference, starts with a /.
	 * 
	 * @return TRUE if the path starts with /, FALSE otherwise
	 */
	public boolean isAbsolute();

	/**
	 * Determines if the current Invocation is a 'Getter' method - that is a
	 * method that starts with 'get' or 'is', has no arguments, and returns a
	 * value.
	 * 
	 * @return TRUE if it is a 'get' or 'is' method, FALSE otherwise
	 */
	public boolean isGetter();

	/**
	 * Determines if the current Invocation is named in the JavaBeans style, '
	 * that is 'get', 'is' or 'set'.
	 * 
	 * @return
	 */
	public boolean isJavaBean();
	
	/**
	 * Determines if the item represented by this method Invocation is a
	 * relative path reference, that is a descendant of the backing Resource
	 * 
	 * @return TRUE if the path contains / but does not start with /, FALSE
	 *         otherwise
	 */
	public boolean isRelative();

	/**
	 * Determines if the current Invocation is of type <code>methodType</code>
	 * 
	 * @param methodType
	 *            MethodType
	 * @return TRUE if it is, FALSE otherwise
	 */
	public boolean isType(MethodType methodType);
}
