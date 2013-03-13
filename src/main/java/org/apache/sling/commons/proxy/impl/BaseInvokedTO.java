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

import java.lang.reflect.Method;

import org.apache.sling.commons.proxy.impl.lang.MethodType;

/**
 * BaseInvokedTO - Transfer Object Meant as a convenient way to maintain and pass a
 * method invocation around The members are not private because the class is
 * package protected and they are final.
 */
public class BaseInvokedTO implements InvokedTO {

	final Object proxy;
	final Method method;
	final Object[] args;
	final String path;
	final MethodType mt;

	/**
	 * Constructs a new Base Invoked Transfer Object.
	 * 
	 * @param proxy
	 * @param method
	 * @param args
	 * @param path
	 * @param name
	 * @param mt
	 */
	protected BaseInvokedTO(Object proxy, Method method, Object[] args,
			String path, MethodType mt) {
		this.proxy = proxy;
		this.method = method;
		this.args = args;
		this.path = path;
		this.mt = mt;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.sling.commons.proxy.impl.InvokedTO#isAbsolute()
	 */
	public boolean isAbsolute() {
		return path != null && path.startsWith("/");
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.sling.commons.proxy.impl.InvokedTO#isRelative()
	 */
	public boolean isRelative() {
		return path != null && !isAbsolute() && path.contains("/");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.commons.proxy.impl.InvokedTO#isType(org.apache.sling
	 * .commons.proxy.impl.lang.MethodType)
	 */
	public boolean isType(MethodType _mt) {
		return mt == _mt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.impl.InvokedTO#isGetter()
	 */
	public boolean isGetter() {
		return MethodType.contains(new MethodType[] { MethodType.JavaBeanIs,
				MethodType.JavaBeanGet }, mt);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.sling.commons.proxy.impl.InvokedTO#isJavaBean()
	 */
	public boolean isJavaBean() {
		return MethodType.contains(new MethodType[] { MethodType.JavaBeanIs,
				MethodType.JavaBeanGet, MethodType.JavaBeanSet }, mt);
	}
}
