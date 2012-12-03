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
package org.apache.sling.commons.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import org.apache.sling.api.resource.Resource;

/**
 * Interfaces for Services which will be called when a matching annotation is
 * found on a Sling Proxy interface. The invoke method should handle the method
 * invocation on the proxy.
 * 
 * @author dklco
 */
public interface ProxyAnnotationService {

	/**
	 * Handle the invocation of a method annotated with the correct annotation
	 * for this Proxy Annotation Service. Processes a method invocation on a
	 * proxy instance and returns the result.
	 * 
	 * @param resource
	 *            the Sling Resource this proxy was adapted from
	 * @param proxy
	 *            the proxy instance that the method was invoked on
	 * 
	 * @param method
	 *            the <code>Method</code> instance corresponding to the
	 *            interface method invoked on the proxy instance. The declaring
	 *            class of the <code>Method</code> object will be the interface
	 *            that the method was declared in, which may be a superinterface
	 *            of the proxy interface that the proxy class inherits the
	 *            method through.
	 * 
	 * @param args
	 *            an array of objects containing the values of the arguments
	 *            passed in the method invocation on the proxy instance, or
	 *            <code>null</code> if interface method takes no arguments.
	 *            Arguments of primitive types are wrapped in instances of the
	 *            appropriate primitive wrapper class, such as
	 *            <code>java.lang.Integer</code> or
	 *            <code>java.lang.Boolean</code>.
	 * 
	 * @return the value to return from the method invocation on the proxy
	 *         instance. If the declared return type of the interface method is
	 *         a primitive type, then the value returned by this method must be
	 *         an instance of the corresponding primitive wrapper class;
	 *         otherwise, it must be a type assignable to the declared return
	 *         type. If the value returned by this method is <code>null</code>
	 *         and the interface method's return type is primitive, then a
	 *         <code>NullPointerException</code> will be thrown by the method
	 *         invocation on the proxy instance. If the value returned by this
	 *         method is otherwise not compatible with the interface method's
	 *         declared return type as described above, a
	 *         <code>ClassCastException</code> will be thrown by the method
	 *         invocation on the proxy instance.
	 * 
	 * @throws Throwable
	 *             the exception to throw from the method invocation on the
	 *             proxy instance. The exception's type must be assignable
	 *             either to any of the exception types declared in the
	 *             <code>throws</code> clause of the interface method or to the
	 *             unchecked exception types
	 *             <code>java.lang.RuntimeException</code> or
	 *             <code>java.lang.Error</code>. If a checked exception is
	 *             thrown by this method that is not assignable to any of the
	 *             exception types declared in the <code>throws</code> clause of
	 *             the interface method, then an
	 *             {@link UndeclaredThrowableException} containing the exception
	 *             that was thrown by this method will be thrown by the method
	 *             invocation on the proxy instance.
	 * 
	 * @see java.lang.reflect.InvocationHandler
	 */
	public Object invoke(Resource resource, SlingProxy proxy, Method m,
			Object[] args) throws Throwable;
}
