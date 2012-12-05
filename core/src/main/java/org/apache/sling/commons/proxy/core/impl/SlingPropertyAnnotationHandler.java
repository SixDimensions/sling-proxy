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
package org.apache.sling.commons.proxy.core.impl;

import java.lang.reflect.Method;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.proxy.api.ProxyAnnotationHandler;
import org.apache.sling.commons.proxy.api.ProxyAnnotationHandlerManager;
import org.apache.sling.commons.proxy.api.SlingProxy;
import org.apache.sling.commons.proxy.api.annotations.SlingProperty;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for handling Proxy calls for the SlingProperty annotation.
 * 
 * @author dklco
 */
@Component(label = "SlingProperty Annotation Service", name = "org.apache.sling.commons.proxy.impl.SlingPropertyAnnotationHandler", metatype = true, immediate = true)
@Service(value = ProxyAnnotationHandler.class)
@Properties(value = {
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "service.description", value = "Apache Sling SlingProperty Annotation Handler") })
public class SlingPropertyAnnotationHandler implements ProxyAnnotationHandler {

	/**
	 * Reference to the ProxyAnnotationHandlerManager, used to register and
	 * unregister this service.
	 */
	@Reference
	private ProxyAnnotationHandlerManager proxyAnnotationHandlerManager;

	/**
	 * The SLF4J Logger.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(SlingPropertyAnnotationHandler.class);

	/**
	 * Called by OSGi when this Service is activated.
	 * 
	 * @param context
	 *            the service context
	 */
	protected void activate(final ComponentContext context) {
		log.info("activate");
		proxyAnnotationHandlerManager.registerProxyAnnotationHandler(
				SlingProperty.class, this);
		log.info("Activation successful");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.commons.proxy.ProxyAnnotationHandler#invoke(org.apache
	 * .sling.api.resource.Resource, org.apache.sling.commons.proxy.SlingProxy,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Resource resource, SlingProxy proxy, Method m,
			Object[] args) throws Throwable {
		log.trace("invoke");

		String methodName = m.getName();
		log.debug("Handling method: {}", methodName);

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
		if (slingProperty.path().trim().length() != 0) {
			String path = slingProperty.path();
			Resource pathResource = null;
			ResourceResolver resolver = resource.getResourceResolver();
			if (path.startsWith("/")) {
				log.debug("Retrieving properties from absolute path: {}", path);
				pathResource = resolver.getResource(path);
			} else {
				log.debug("Retrieving properties from relative path: {}", path);
				pathResource = resolver.getResource(resource, path);
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
			if (slingProperty.name().trim().length() != 0) {
				name = slingProperty.name();
			} else if (methodName.startsWith("get")) {
				name = DefaultProxyHandler.findMatchingKey(properties,
						methodName.substring(methodName.indexOf("get") + 3));
			} else if (methodName.startsWith("is")) {
				name = DefaultProxyHandler.findMatchingKey(properties,
						methodName.substring(methodName.indexOf("is") + 2));
			} else {
				name = DefaultProxyHandler.findMatchingKey(properties,
						methodName);
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
		log.info("deactivate");
		proxyAnnotationHandlerManager
				.unregisterProxyAnnotationHandler(SlingProperty.class);
		log.info("Deactivation successful");
	}

}
