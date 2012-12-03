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

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.proxy.ProxyAnnotationService;
import org.apache.sling.commons.proxy.ProxyAnnotationServiceManager;
import org.apache.sling.commons.proxy.SlingProxy;
import org.apache.sling.commons.proxy.annotations.SlingProperty;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author dklco
 */
@Component(label = "Proxy Annotation Service Manager", name = "org.apache.sling.commons.proxy.impl.ProxyAnnotationServiceManagerImpl", metatype = true, immediate = true)
@Service(value = ProxyAnnotationServiceManager.class)
@Properties(value = {
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "service.description", value = "Apache Sling Proxy Annotation Service Manager Service") })
public class SlingPropertyAnnotationService implements ProxyAnnotationService {

	/**
	 * Reference to the ProxyAnnotationServiceManager, used to register and
	 * unregister this service.
	 */
	@Reference
	private ProxyAnnotationServiceManager proxyAnnotationServiceManager;

	/**
	 * The SLF4J Logger.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(SlingPropertyAnnotationService.class);

	/**
	 * Called by OSGi when this Service is activated.
	 * 
	 * @param context
	 *            the service context
	 */
	protected void activate(final ComponentContext context) {
		proxyAnnotationServiceManager.registerProxyAnnotationService(
				SlingProperty.class, this);
	}

	public Object invoke(Resource resource, SlingProxy proxy, Method m,
			Object[] args) throws Throwable {
		log.trace("invoke");

		String methodName = m.getName();
		log.debug("Handling method: {}" + methodName);

		return retrieveProperty(resource, m);
	}

	/**
	 * Retrieve the property, taking into account the values set in the
	 * SlingProperty annotation.
	 * 
	 * @param resource
	 *            the backing resource
	 * @param m
	 *            the invoked method
	 * @return the property value
	 */
	private Object retrieveProperty(Resource resource, Method m) {
		log.trace("retrieveProperty");

		String methodName = m.getName();
		SlingProperty slingProperty = m.getAnnotation(SlingProperty.class);

		ValueMap properties = null;
		if (slingProperty.path() != null) {
			String path = slingProperty.path();
			Resource pathResource = null;
			if (path.startsWith("/")) {
				log.debug("Retrieving properties from absolute path: {}", path);
				pathResource = resource.getResourceResolver().getResource(path);
			} else {
				log.debug("Retrieving properties from relative path: {}", path);
				pathResource = resource.getChild(path);
			}

			if (pathResource != null) {
				properties = pathResource.adaptTo(ValueMap.class);
			} else {
				log.warn("Resource not found at path: {}", path);
			}
		} else {
			properties = resource.adaptTo(ValueMap.class);
		}

		if (properties != null) {
			String name = null;
			if (slingProperty.name() != null) {
				name = slingProperty.name();
			} else if (methodName.startsWith("get")) {
				name = DefaultProxyHandler.findMatchingKey(properties,
						methodName.substring(methodName.indexOf("get") + 3));
			} else if (methodName.startsWith("is")) {
				name = DefaultProxyHandler.findMatchingKey(properties,
						methodName.substring(methodName.indexOf("is") + 2));
			}
			return properties.get(name, m.getReturnType());
		}

		return null;
	}

	/**
	 * Called by OSGi when this Service is deactivated.
	 * 
	 * @param context
	 *            the service context
	 */
	protected void deactivate(final ComponentContext context) {
		proxyAnnotationServiceManager
				.unregisterProxyAnnotationService(SlingProperty.class);
	}

}
