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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.ProxyAnnotationService;
import org.apache.sling.commons.proxy.ProxyAnnotationServiceManager;
import org.apache.sling.commons.proxy.SlingProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the invocation of methods on SlingProxy interfaces. Retrieves the
 * annotations for the method being invoked and attempts to find a
 * ProxyAnnotationService for the annotation and invoke that, if no suitable
 * ProxyAnnotationService is found, it will invoke the DefaultProxyHandler.
 * 
 * @author dklco
 */
public class SlingDynamicProxy implements InvocationHandler {

	/**
	 * The resource which has been adapted for this Dynamic Proxy.
	 */
	private final Resource resource;

	/**
	 * The ProxyAnnotationServiceManager, used to hold references to the Proxy
	 * Annotation Services.
	 */
	private final ProxyAnnotationServiceManager proxyAnnotationServiceManager;
	private static final Logger log = LoggerFactory
			.getLogger(SlingDynamicProxy.class);

	/**
	 * Construct a new Sling Dynamic Proxy instance.
	 * 
	 * @param resource
	 *            this resource will be used to back the proxy
	 * @param proxyAnnotationServiceManager
	 *            the proxy annotations service manager reference
	 */
	public SlingDynamicProxy(final Resource resource,
			final ProxyAnnotationServiceManager proxyAnnotationServiceManager) {
		this.resource = resource;
		this.proxyAnnotationServiceManager = proxyAnnotationServiceManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method m, Object[] args)
			throws Throwable {
		log.trace("invoke");

		ProxyAnnotationService proxyAnnotationService = new DefaultProxyHandler();
		for (Annotation annotation : m.getAnnotations()) {
			if (proxyAnnotationServiceManager
					.getProxyAnnotationService(annotation.getClass()) != null) {
				proxyAnnotationService = proxyAnnotationServiceManager
						.getProxyAnnotationService(annotation.getClass());
				log.debug(
						"Found Proxy Annotation Service {} for annotation {}",
						proxyAnnotationService.getClass().getName(), annotation
								.getClass().getName());
				break;
			}
		}

		return proxyAnnotationService.invoke(resource,
				SlingProxy.class.cast(proxy), m, args);
	}
}
