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
 * InvokedPropertyTO - Transfer Object Meant as a convenient way to maintain and pass a
 * method invocation around The members are not private because the class is
 * package protected and they are final.
 */
final class InvokedPropertyTO extends BaseInvokedTO {

	final String name;
	final String propertyName;
	
	/**
	 * Constructs a new Invoked Transfer Object.
	 * 
	 * @param proxy
	 * @param method
	 * @param args
	 * @param path
	 * @param name
	 * @param mt
	 */
	protected InvokedPropertyTO(Object proxy, Method method, Object[] args,
			String path, String name, MethodType mt) {
		super(proxy, method, args, path, mt);
		this.name = name;
		this.propertyName = (path != null ? path + "/" : "") + name;
	}
}
