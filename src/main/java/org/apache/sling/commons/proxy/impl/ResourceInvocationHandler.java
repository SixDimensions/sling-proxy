/*
 * Copyright 2012 Six Dimensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.commons.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.commons.proxy.impl.lang.GetMethodToStringImpl;
import org.apache.sling.commons.proxy.impl.lang.JDPEqualsImpl;
import org.apache.sling.commons.proxy.impl.lang.JDPHashCodeImpl;
import org.apache.sling.commons.proxy.impl.lang.MethodType;
import org.apache.sling.commons.proxy.impl.lang.PrimeNumbers;

/**
 * Invocation handler for proxies backed by a Sling Resource.
 */
public class ResourceInvocationHandler implements InvocationHandler {

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
	 * Create a new ResourceInvocationHandler allowing invocation of all Methods
	 * that this InvocationHandler represents
	 * 
	 * @param r
	 *            Resource - the
	 */
	ResourceInvocationHandler(Resource r) {
		this.r = r;
		this.cache = new java.util.HashMap<String, Object>();
	}

	/*
	 * (non-Javadoc) @see
	 * java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		InvokedTO to = InvokedTO.newInstance(proxy, method, args);
		if (to.isGetter()) {
			return (handleGet(to));
		} else if (to.isType(MethodType.JavaBeanSet)) {
			throw new UnsupportedOperationException(
					"Method not yet implemented.");
		} else if (to.isType(MethodType.ToString)) {
			return new GetMethodToStringImpl().toString(proxy);
		} else if (to.isType(MethodType.HashCode)) {
			return new JDPHashCodeImpl().hashCode(proxy);
		} else if (to.isType(MethodType.Equals)) {
			if (args == null || args.length != 1) {
				String msg = "Method 'equals' requires exactly 1 argument.";
				throw new IllegalArgumentException(msg);
			}
			return new JDPEqualsImpl().equals(proxy, args[0]);
		} else if (to.isType(MethodType.BackingResource)) {
			return r;
		}
		throw new NoSuchMethodException("Method " + method.getName() + " DNE");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hashCode = ResourceInvocationHandler.class.hashCode()
				* PrimeNumbers.getInstance().get(2) + r.getPath().hashCode();
		return hashCode;
	}

	/**
	 * Get the path of the resource backing this invocation handler.
	 * 
	 * @return the resource path
	 */
	public final String getResourcePath() {
		return r.getPath();
	}

	/**
	 * Handles get requests against a proxy to a Sling Resource.
	 * 
	 * @param to
	 *            the DTO for the invocation
	 * @return the result of the get access
	 * @throws Throwable
	 */
	private Object handleGet(InvokedTO to) throws Throwable {
		Object objReturn;
		if (cache.containsKey(to.propertyName)) {
			objReturn = cache.get(to.propertyName);
		} else {
			// TODO: refactor to also cache the ValueMap for a given path maybe?
			ValueMap vm;
			if (to.path == null) {
				vm = r.adaptTo(ValueMap.class);
			} else {
				Resource rsrc;
				if (to.isAbsolute()) {
					rsrc = r.getResourceResolver().getResource(to.path);
				} else {
					rsrc = r.getResourceResolver().getResource(r, to.path);
				}
				vm = rsrc.adaptTo(ValueMap.class);
			}
			objReturn = vm.get(to.name, to.method.getReturnType());
			cache.put(to.propertyName, objReturn);
		}
		return objReturn;
	}
}
