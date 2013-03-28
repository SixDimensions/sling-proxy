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
 * BaseInvokedTO - Transfer Object Meant as a convenient way to maintain and
 * pass a method invocation around The members are not private because the class
 * is package protected and they are final.
 */
public class BaseInvokedTO implements InvokedTO {

	/**
	 * The method being invoked
	 */
	private final Method method;

	/**
	 * The path from the annotations
	 */
	private final String path;

	/**
	 * The type of method being invoked
	 */
	private final MethodType mt;

	/**
	 * Constructs a new Base Invoked Transfer Object.
	 * 
	 * @param method
	 *            the method being invoked
	 * @param path
	 *            the path from the annotations
	 * @param mt
	 *            the type of method being invoked
	 */
	protected BaseInvokedTO(final Method method, final String path,
			final MethodType mt) {
		this.method = method;
		this.path = path;
		this.mt = mt;
	}

	/**
	 * Gets the invoked method.
	 * 
	 * @return the method
	 */
	public final Method getMethod() {
		return this.method;
	}

	/**
	 * Gets the method type, derived from the method name.
	 * 
	 * @return the method type
	 */
	public final MethodType getMt() {
		return this.mt;
	}

	/**
	 * Gets the path specified in the annotation.
	 * 
	 * @return the path
	 */
	public final String getPath() {
		return this.path;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.impl.InvokedTO#isAbsolute()
	 */
	public boolean isAbsolute() {
		return (this.path != null) && this.path.startsWith("/");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.impl.InvokedTO#isGetter()
	 */
	public boolean isGetter() {
		return MethodType.contains(new MethodType[] { MethodType.JavaBeanIs,
				MethodType.JavaBeanGet }, this.mt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.impl.InvokedTO#isJavaBean()
	 */
	public boolean isJavaBean() {
		return MethodType.contains(new MethodType[] { MethodType.JavaBeanIs,
				MethodType.JavaBeanGet, MethodType.JavaBeanSet }, this.mt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.impl.InvokedTO#isRelative()
	 */
	public boolean isRelative() {
		return (this.path != null) && !this.isAbsolute()
				&& this.path.contains("/");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.commons.proxy.impl.InvokedTO#isType(org.apache.sling
	 * .commons.proxy.impl.lang.MethodType)
	 */
	public boolean isType(final MethodType _mt) {
		return this.mt == _mt;
	}
}
