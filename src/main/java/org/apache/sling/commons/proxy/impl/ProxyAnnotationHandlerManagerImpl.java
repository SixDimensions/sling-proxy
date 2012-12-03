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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.proxy.ProxyAnnotationHandler;
import org.apache.sling.commons.proxy.ProxyAnnotationHandlerManager;

/**
 * Default implementation of the Proxy Annotation Handler Manager.
 * 
 * @author dklco
 */
@Component(label = "Proxy Annotation Handler Manager", name = "org.apache.sling.commons.proxy.impl.ProxyAnnotationHandlerManagerImpl", metatype = true, immediate = true)
@Service(value = ProxyAnnotationHandlerManager.class)
@Properties(value = {
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "service.description", value = "Apache Sling Proxy Annotation Handler Manager Service") })
public class ProxyAnnotationHandlerManagerImpl implements
		ProxyAnnotationHandlerManager {

	/**
	 * Cache of the registered Proxy Annotation Handler instances.
	 */
	private Map<Class<?>, ProxyAnnotationHandler> annotationHandlerCache = Collections
			.synchronizedMap(new HashMap<Class<?>, ProxyAnnotationHandler>());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.ProxyAnnotationHandlerManager#
	 * getProxyAnnotationHandler(java.lang.Class)
	 */
	public ProxyAnnotationHandler getProxyAnnotationHandler(
			final Class<?> annotation) {
		return this.annotationHandlerCache.get(annotation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.ProxyAnnotationHandlerManager#
	 * registerProxyAnnotationHandler(java.lang.Class,
	 * org.apache.sling.commons.proxy.ProxyAnnotationHandler)
	 */
	public void registerProxyAnnotationHandler(final Class<?> annotationClass,
			final ProxyAnnotationHandler service) {
		this.annotationHandlerCache.put(annotationClass, service);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.ProxyAnnotationHandlerManager#
	 * unregisterProxyAnnotationHandler(java.lang.Class)
	 */
	public void unregisterProxyAnnotationHandler(final Class<?> annotationClass) {
		this.annotationHandlerCache.remove(annotationClass);
	}
}
