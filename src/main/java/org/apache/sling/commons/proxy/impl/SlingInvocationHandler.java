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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceNotFoundException;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.proxy.SlingProxyService;
import org.apache.sling.commons.proxy.annotations.SlingChildren;
import org.apache.sling.commons.proxy.annotations.SlingReference;
import org.apache.sling.commons.proxy.impl.lang.GetMethodToStringImpl;
import org.apache.sling.commons.proxy.impl.lang.JDPEqualsImpl;
import org.apache.sling.commons.proxy.impl.lang.JDPHashCodeImpl;
import org.apache.sling.commons.proxy.impl.lang.MethodType;
import org.apache.sling.commons.proxy.impl.lang.PrimeNumbers;
import org.apache.sling.commons.proxy.impl.reflection.Annotations;
import org.apache.sling.commons.proxy.impl.to.BaseInvokedTO;
import org.apache.sling.commons.proxy.impl.to.InvokedChildrenTO;
import org.apache.sling.commons.proxy.impl.to.InvokedPropertyTO;
import org.apache.sling.commons.proxy.impl.to.InvokedTO;
import org.apache.sling.commons.proxy.impl.to.InvokedTOFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invocation handler for proxies backed by a Sling Resource.
 */
public class SlingInvocationHandler implements InvocationHandler {

	/**
	 * SLF4J Logger
	 */
	private static final Logger log = LoggerFactory
			.getLogger(SlingInvocationHandler.class);

	/**
	 * The backing Sling Resource
	 */
	private final Resource r;

	/**
	 * This caches all 'get' or 'is' method's return values. Calling 'set' will
	 * clear that properties cached value.
	 */
	private final Map<String, Object> cache;

	/**
	 * The SlingProxyService instance, used to retrieve references and
	 * children.
	 */
	private final SlingProxyService slingProxyService;

	/**
	 * Create a new ResourceInvocationHandler allowing invocation of all Methods
	 * that this InvocationHandler represents
	 * 
	 * @param r
	 *            Resource - the
	 * @param defaultSlingProxyServiceImpl
	 */
	SlingInvocationHandler(final Resource r,
			final SlingProxyService slingProxyService) {
		this.r = r;
		this.slingProxyService = slingProxyService;
		this.cache = new java.util.HashMap<String, Object>();
	}

	/**
	 * Get the path of the resource backing this invocation handler.
	 * 
	 * @return the resource path
	 */
	public final String getResourcePath() {
		return this.r.getPath();
	}

	/**
	 * Handle get children invocations.
	 * 
	 * @param to
	 *            the method invocation transfer object
	 * @return the returned object
	 */
	@SuppressWarnings("unchecked")
	private Object handleGetChildren(final InvokedChildrenTO to) {
		log.trace("handleGetChildren");

		Resource resource = this.r;
		if (!StringUtils.isEmpty(to.getPath())) {
			log.debug("Loading child resources from: {}", to.getPath());
			if (to.isAbsolute()) {
				resource = this.r.getResourceResolver().getResource(
						to.getPath());
			} else {
				resource = this.r.getResourceResolver().getResource(this.r,
						to.getPath());
			}
		}

		Class<?> returnType = Resource.class;
		if (to.getReturnType() != null) {
			returnType = to.getReturnType();
		}

		Iterator<?> toReturn = Collections.EMPTY_LIST.iterator();
		if (resource != null) {
			toReturn = new DeferredIterator<Object>(resource
					.getResourceResolver().listChildren(resource),
					(Class<Object>) returnType, this.slingProxyService);
		}
		return toReturn;
	}

	/**
	 * Handles get requests against a proxy to a Sling Resource.
	 * 
	 * @param to
	 *            the DTO for the invocation
	 * @return the result of the get access
	 * @throws Throwable
	 */
	private Object handleGetProperty(final InvokedPropertyTO to)
			throws Throwable {
		Object objReturn;

		// handle properties
		if (this.cache.containsKey(to.getPropertyName())) {
			objReturn = this.cache.get(to.getPropertyName());
		} else {
			// TODO: refactor to also cache the ValueMap for a given path maybe?
			ValueMap vm;
			if (to.getPath() == null) {
				vm = this.r.adaptTo(ValueMap.class);
			} else {
				Resource rsrc;
				if (StringUtils.isEmpty(to.getPath())) {
					rsrc = this.r;
				} else if (to.isAbsolute()) {
					rsrc = this.r.getResourceResolver().getResource(
							to.getPath());
				} else {
					rsrc = this.r.getResourceResolver().getResource(this.r,
							to.getPath());
				}
				if (rsrc == null) {
					throw new ResourceNotFoundException(
							"Unable to load resource at path: " + to.getPath());
				}
				vm = rsrc.adaptTo(ValueMap.class);
			}
			objReturn = vm.get(to.getName(), to.getMethod().getReturnType());
			this.cache.put(to.getPropertyName(), objReturn);
		}
		return objReturn;
	}

	/**
	 * Handles a call to get a reference to another resource.
	 * 
	 * @param to
	 *            the method invocation transfer object
	 * @return the resulting object
	 */
	private Object handleGetReference(final BaseInvokedTO to) {
		log.trace("handleGetReference");

		log.debug("Referencing resource at path: {}", to.getPath());
		Resource reference = null;
		if (to.getPath().startsWith("/")) {
			reference = this.r.getResourceResolver().getResource(to.getPath());
		} else {
			reference = this.r.getResourceResolver().getResource(this.r,
					to.getPath());
		}
		log.debug("Loaded resource: {}", reference);

		if (reference != null) {
			if (Resource.class.equals(to.getMethod().getReturnType())) {
				log.debug("Returning resource as reference");
				return reference;
			}

			final Object adapted = reference.adaptTo(to.getMethod()
					.getReturnType());
			if (adapted != null) {
				log.debug("Returning adapted object as reference");
				return adapted;
			}

			try {
				final Object proxy = this.slingProxyService.getProxy(reference,
						to.getMethod().getReturnType());
				log.debug("Returning proxy as reference");
				return proxy;
			} catch (final Exception e) {
				log.warn("Exception getting proxy, null reference will be returned");
			}
		} else {
			log.debug("Referenced resource is null");
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int hashCode = (SlingInvocationHandler.class.hashCode() * PrimeNumbers
				.getInstance().get(2)) + this.r.getPath().hashCode();
		return hashCode;
	}

	/*
	 * (non-Javadoc) @see
	 * java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(final Object proxy, final Method method,
			final Object[] args) throws Throwable {

		final InvokedTO to = InvokedTOFactory.newInstance(proxy, method, args);
		if (to.isGetter()) {
			if (Annotations.methodHasAnnotation(method, SlingReference.class)) {
				return (this.handleGetReference((BaseInvokedTO) to));
			} else if (Annotations.methodHasAnnotation(method,
					SlingChildren.class)) {
				return (this.handleGetChildren((InvokedChildrenTO) to));
			} else {
				return (this.handleGetProperty((InvokedPropertyTO) to));
			}
		} else if (to.isType(MethodType.JavaBeanSet)) {
			throw new UnsupportedOperationException(
					"Setter methods not yet implemented.");
		} else if (to.isType(MethodType.ToString)) {
			return new GetMethodToStringImpl().toString(proxy);
		} else if (to.isType(MethodType.HashCode)) {
			return new JDPHashCodeImpl().hashCode(proxy);
		} else if (to.isType(MethodType.Equals)) {
			if ((args == null) || (args.length != 1)) {
				final String msg = "Method 'equals' requires exactly 1 argument.";
				throw new IllegalArgumentException(msg);
			}
			return new JDPEqualsImpl().equals(proxy, args[0]);
		} else if (to.isType(MethodType.BackingResource)) {
			return this.r;
		}
		throw new NoSuchMethodException("Method " + method.getName() + " DNE");
	}
}
