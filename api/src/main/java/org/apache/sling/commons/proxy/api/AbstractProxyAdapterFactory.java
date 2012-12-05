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
package org.apache.sling.commons.proxy.api;

import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.classloader.DynamicClassLoaderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Adaptor Factory for adapting Sling Resources to Sling Dynamic
 * Proxies. This adaptor utilizes Java Dynamic Proxies to allow adapting
 * resources into objects which proxy calls against interface methods into the
 * resource properties.
 * 
 * Implementors of the SlingProxy interface should register an AdapterFactory
 * Service with the services
 * 
 * @author dklco
 */
public class AbstractProxyAdapterFactory implements AdapterFactory {

	/**
	 * The SLF4J Logger.
	 */
	private static final Logger log = LoggerFactory
			.getLogger(AbstractProxyAdapterFactory.class);

	/**
	 * Reference to the Sling Proxy Service.
	 */
	@Reference
	private SlingProxyService slingProxyService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.api.adapter.AdapterFactory#getAdapter(java.lang.Object,
	 * java.lang.Class)
	 */
	public <AdapterType> AdapterType getAdapter(Object adaptable,
			Class<AdapterType> type) {
		log.trace("getAdapter");

		if (adaptable instanceof Resource) {
			Resource resource = ((Resource) adaptable);
			return slingProxyService.getProxy(resource, type);
		} else {
			log.warn("Unable to adapt object of type: {}", adaptable.getClass()
					.getName());
		}
		return null;
	}

}
