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
package org.apache.sling.commons.proxy.impl.to;

import java.lang.reflect.Method;

import org.apache.sling.commons.proxy.impl.lang.MethodType;

/**
 * InvokedPropertyTO - Transfer Object Meant as a convenient way to maintain and
 * pass a method invocation around The members are not private because the class
 * is package protected and they are final.
 */
public final class InvokedPropertyTO extends BaseInvokedTO {

	/**
	 * The property name specified in the annotation
	 */
	private final String name;

	/**
	 * The full property name, calculated from the path and name from the
	 * annotation parameters
	 */
	private final String propertyName;

	/**
	 * Constructs a new Invoked Transfer Object.
	 * 
	 * @param proxy
	 *            the proxy instance on which the method was invoked
	 * @param method
	 *            the invoked method
	 * @param args
	 *            the method arguments
	 * @param path
	 *            the path annotation value
	 * @param name
	 *            the name annotation value
	 * @param mt
	 *            the type of method invoked
	 */
	protected InvokedPropertyTO(final Method method, final Object[] args,
			final String path, final String name, final MethodType mt) {
		super(method, path, mt);
		this.name = name;
		this.propertyName = (path != null ? path + "/" : "") + name;
	}

	/**
	 * Get the name annotation value.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the property name to retrieve, this is calculated from the annotation
	 * name and path.
	 * 
	 * @return the property name
	 */
	public String getPropertyName() {
		return this.propertyName;
	}
}
