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

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.ISlingProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An iterator which defers the loading of the elements from a backing of
 * Iterator of resources.
 * 
 * @param <E>
 */
public class DeferredIterator<E> implements Iterator<E> {

	private static final Logger log = LoggerFactory
			.getLogger(DeferredIterator.class);
	private Iterator<Resource> backingResources;
	private Class<E> returnType;
	private ISlingProxyService slingProxyService;

	/**
	 * Instantiates a Deferred Iterator.
	 * 
	 * @param backingResources
	 *            the Iterator of resources with which to back this iterator
	 * @param returnType
	 *            the class which should be returned by next
	 * @param slingProxyService
	 *            a reference to the Sling Proxy service
	 */
	public DeferredIterator(Iterator<Resource> backingResources,
			Class<E> returnType, ISlingProxyService slingProxyService) {
		this.backingResources = backingResources;
		this.returnType = returnType;
		this.slingProxyService = slingProxyService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.backingResources.hasNext();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public E next() {
		log.trace("next");
		Resource resource = backingResources.next();

		Object toReturn = null;
		if (resource != null) {
			if (Resource.class.equals(returnType)) {
				log.debug("Returning resource as child");
				toReturn = resource;
			}

			Object adapted = resource.adaptTo(returnType);
			if (adapted != null) {
				log.debug("Returning adapted object as child");
				toReturn = adapted;
			}

			try {
				Object proxy = slingProxyService.getProxy(resource, returnType);
				log.debug("Returning proxy as reference");
				toReturn = proxy;
			} catch (Exception e) {
				log.warn("Exception getting proxy, null reference will be returned");
			}
		} else {
			log.debug("Referenced resource is null");
		}
		return returnType.cast(toReturn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		this.backingResources.remove();
	}

}
