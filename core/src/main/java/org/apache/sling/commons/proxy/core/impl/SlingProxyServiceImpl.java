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

import java.lang.reflect.Proxy;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.apache.sling.commons.proxy.api.ProxyAnnotationHandlerManager;
import org.apache.sling.commons.proxy.api.SlingProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the SlingProxyService.
 * 
 * @author dklco
 */
@Component(label = "Sling Proxy Service", name = "org.apache.sling.commons.proxy.core.impl.SlingProxyServiceImpl", metatype = true, immediate = true)
@Service(value = SlingProxyService.class)
@Properties(value = {
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "service.description", value = "Apache Sling Proxy Service") })
public class SlingProxyServiceImpl implements SlingProxyService {

	/**
	 * Reference to the Sling Dynamic ClassLoader Manager.
	 */
	@Reference
	private DynamicClassLoaderManager classLoaderManager;

	/**
	 * Reference to the Sling Proxy Annotation Service Manager.
	 */
	@Reference
	private ProxyAnnotationHandlerManager proxyAnnotationServiceManager;

	private static final Logger log = LoggerFactory
			.getLogger(SlingProxyServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.commons.proxy.api.SlingProxyService#getProxy(org.apache
	 * .sling.api.resource.Resource, java.lang.Class)
	 */
	public <AdapterType> AdapterType getProxy(Resource resource,
			Class<AdapterType> type) {
		log.trace("getProxy");

		ClassLoader classLoader = classLoaderManager.getDynamicClassLoader();
		log.debug("Creating Dynamic Proxy of type: {}", type.getName());

		return type.cast(Proxy.newProxyInstance(classLoader,
				new Class[] { type }, new SlingDynamicProxy(resource,
						proxyAnnotationServiceManager)));
	}

}
