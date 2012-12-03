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
import org.apache.sling.commons.proxy.ProxyAnnotationService;
import org.apache.sling.commons.proxy.ProxyAnnotationServiceManager;

/**
 * Default implementation of the Proxy Annotation Service Manager.
 * 
 * @author dklco
 */
@Component(label = "Proxy Annotation Service Manager", name = "org.apache.sling.commons.proxy.impl.ProxyAnnotationServiceManagerImpl", metatype = true, immediate = true)
@Service(value = ProxyAnnotationServiceManager.class)
@Properties(value = {
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "service.description", value = "Apache Sling Proxy Annotation Service Manager Service") })
public class ProxyAnnotationServiceManagerImpl implements
		ProxyAnnotationServiceManager {

	/**
	 * Cache of the registered Proxy Annotation Service instances.
	 */
	private Map<Class<?>, ProxyAnnotationService> annotationServiceCache = Collections
			.synchronizedMap(new HashMap<Class<?>, ProxyAnnotationService>());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.ProxyAnnotationServiceManager#
	 * getProxyAnnotationService(java.lang.Class)
	 */
	public ProxyAnnotationService getProxyAnnotationService(
			final Class<?> annotation) {
		return this.annotationServiceCache.get(annotation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.ProxyAnnotationServiceManager#
	 * registerProxyAnnotationService(java.lang.Class,
	 * org.apache.sling.commons.proxy.ProxyAnnotationService)
	 */
	public void registerProxyAnnotationService(final Class<?> annotationClass,
			final ProxyAnnotationService service) {
		this.annotationServiceCache.put(annotationClass, service);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.sling.commons.proxy.ProxyAnnotationServiceManager#
	 * unregisterProxyAnnotationService(java.lang.Class)
	 */
	public void unregisterProxyAnnotationService(final Class<?> annotationClass) {
		this.annotationServiceCache.remove(annotationClass);
	}
}
