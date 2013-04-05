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
import org.apache.sling.commons.proxy.SlingProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An iterator which defers the loading of the elements from a backing of
 * Iterator of resources.
 * 
 * @param <E>
 *            the object type for the interator contents, set in the
 *            SlingChildren annotation
 */
final class DeferredIterator<E> implements Iterator<E> {

	/**
	 * The SLF4j Logger
	 */
	private static final Logger log = LoggerFactory
			.getLogger(DeferredIterator.class);

	/**
	 * The iterator of Resources which backs this deferred iterator
	 */
	private final Iterator<Resource> backingResources;

	/**
	 * They type to be returned.
	 */
	private final Class<E> returnType;

	/**
	 * A reference to the Sling Proxy service, used to load items which are
	 * SlingProxies
	 */
	private final SlingProxyService slingProxyService;

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
	public DeferredIterator(final Iterator<Resource> backingResources,
			final Class<E> returnType,
			final SlingProxyService slingProxyService) {
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
		final Resource resource = this.backingResources.next();

		Object toReturn = null;
		if (resource != null) {
			if (Resource.class.equals(this.returnType)) {
				log.debug("Returning resource as child");
				toReturn = resource;
			}

			final Object adapted = resource.adaptTo(this.returnType);
			if (adapted != null) {
				log.debug("Returning adapted object as child");
				toReturn = adapted;
			}

			try {
				final Object proxy = this.slingProxyService.getProxy(resource,
						this.returnType);
				log.debug("Returning proxy as reference");
				toReturn = proxy;
			} catch (final Exception e) {
				log.warn("Exception getting proxy, null reference will be returned");
			}
		} else {
			log.debug("Referenced resource is null");
		}
		return this.returnType.cast(toReturn);
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
