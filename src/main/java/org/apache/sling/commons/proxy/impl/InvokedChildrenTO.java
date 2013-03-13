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
 * Transfer object for SlingChildren method invocations.
 */
public class InvokedChildrenTO extends BaseInvokedTO {

	final Class<?> returnType;

	/**
	 * Constructs a new Invoked Children Transfer Object.
	 * 
	 * @param proxy
	 *            the proxy object
	 * @param method
	 *            the invoked method
	 * @param args
	 *            the method arguments
	 * @param path
	 *            the path
	 * @param returnType
	 *            the return type
	 * @param mt
	 *            the method type
	 */
	protected InvokedChildrenTO(Object proxy, Method method, Object[] args,
			String path, Class<?> returnType, MethodType mt) {
		super(proxy, method, args, path, mt);
		this.returnType = returnType;
	}

}
