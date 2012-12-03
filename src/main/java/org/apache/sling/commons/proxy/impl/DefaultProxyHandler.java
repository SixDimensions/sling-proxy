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

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.proxy.ProxyAnnotationService;
import org.apache.sling.commons.proxy.SlingProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Default Proxy Handler, this class will attempt to retrieve a property
 * from the resource with a key matching the method name according to the
 * findMatchingKey method.
 * 
 * @author dklco
 */
public class DefaultProxyHandler implements ProxyAnnotationService {

	private static final String GET_RESOURCE_METHOD_NAME = "getBackingResource";
	private static final Logger log = LoggerFactory
			.getLogger(DefaultProxyHandler.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.commons.proxy.ProxyAnnotationService#invoke(org.apache
	 * .sling.api.resource.Resource, org.apache.sling.commons.proxy.SlingProxy,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Resource resource, SlingProxy proxy, Method m,
			Object[] args) throws Throwable {
		log.trace("invoke");

		String methodName = m.getName();
		log.debug("Handling method: {}" + methodName);

		ValueMap properties = resource.adaptTo(ValueMap.class);

		if (GET_RESOURCE_METHOD_NAME.equals(methodName)) {
			return resource;
		} else if (methodName.startsWith("get")) {
			String name = methodName.substring(methodName.indexOf("get") + 3);
			return properties.get(findMatchingKey(properties, name),
					m.getReturnType());
		} else if (methodName.startsWith("is")) {
			String name = methodName.substring(methodName.indexOf("is") + 2);
			return properties.get(findMatchingKey(properties, name),
					m.getReturnType());
		}
		return null;
	}

	/**
	 * Finds the key matching the specified name based on the following rules:
	 * <ul>
	 * <li>First checks to see if a property with the name exists</li>
	 * <li>Attempt to find a rule with a similar name by removing any colons
	 * from the keys and doing a case insensitive comparison
	 * <li>
	 * <li>If no key is found, will return the provided name</li>
	 * </ul>
	 * 
	 * @param properties
	 *            the current ValueMap of properties to check
	 * @param name
	 *            the name of the property to attempt to retrieve
	 * @return
	 */
	protected String findMatchingKey(ValueMap properties, String name) {
		log.trace("findMatchingKey");
		if (properties.containsKey(name)) {
			return name;
		} else {
			for (String key : properties.keySet()) {
				if (key.replace(":", "").equalsIgnoreCase(name)) {
					log.debug("Found key {} matching name {}", key, name);
					return key;
				}
			}
		}
		return name;
	}
}
