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
import java.lang.reflect.Proxy;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.proxy.ISlingProxyService;
import org.apache.sling.commons.proxy.annotations.SlingProperty;
import org.apache.sling.commons.proxy.impl.reflection.Annotations;

/**
 * Default implementation of the {@link ISlingProxyService#}
 */
@Service(value = ISlingProxyService.class)
@Component(description = "Creates ISlingProxy instances", immediate = true)
public final class DefaultSlingProxyServiceImpl implements ISlingProxyService {

	/**
	 * Checks to see if an instance of the specified <code>type</code> can be
	 * instantiated from the <code>resource</code>.
	 * 
	 * @param resource
	 *            the resource to check
	 * @param type
	 *            the type to check
	 */
	private <T> void validateIsInstantiable(Resource resource,
			Class<T> type) {
		if (resource == null) {
			String msg = "The backing Resource cannot be NULL.";
			throw new NullPointerException(msg);
		}
		if (type == null) {
			String msg = "The provided ISlingProxy Interface cannot be NULL.";
			throw new NullPointerException(msg);
		}
		if (!type.isInterface()) {
			String msg = "Proxy class " + type.getName() + " must be an Interface.";
			throw new UnsupportedOperationException(msg);
		}
		if (!Annotations.hasMethodAnnotation(type, SlingProperty.class)) {
			String msg = "Proxy interface " + type.getName() + " must have at least "
					+ "one Method with a @SlingProperty annotation.";
			throw new UnsupportedOperationException(msg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.sling.commons.proxy.ISlingProxyService#getProxy(org.apache
	 * .sling.api.resource.Resource, java.lang.Class)
	 */
	public <AdapterType> AdapterType getProxy(Resource resource,
			Class<AdapterType> type) {
		validateIsInstantiable(resource, type);

		InvocationHandler ih = new ResourceInvocationHandler(resource);
		AdapterType rtn = type.cast(Proxy.newProxyInstance(
				type.getClassLoader(), new Class[] { type }, ih));
		return rtn;
	}

}
