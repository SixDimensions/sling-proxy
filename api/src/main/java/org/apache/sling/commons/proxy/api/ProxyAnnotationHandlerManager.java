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

/**
 * The ProxyAnnotationHandlerManager manages the available
 * ProxyAnnotationHandler instances.
 * 
 * @author dklco
 */
public interface ProxyAnnotationHandlerManager {

	/**
	 * Get the ProxyAnnotationHandler instance associated with the specified
	 * annotation class.
	 * 
	 * @param annotation
	 *            the Java class for the annotation associated with the
	 *            ProxyAnnotationHandler
	 * @return the ProxyAnnotationHandler associated with the annotation class
	 *         or null
	 */
	public ProxyAnnotationHandler getProxyAnnotationHandler(Class<?> annotationClass);

	/**
	 * Registers a new ProxyAnnotationHandler instance associated with the
	 * specified annotation class. Will replace any existing
	 * ProxyAnnotationHandler for the specified annotation class.
	 * 
	 * @param annotationClass
	 *            the Java class for the annotation to associate with the
	 *            ProxyAnnotationHandler
	 * @param handler
	 *            the ProxyAnnotationHandler instance
	 */
	public void registerProxyAnnotationHandler(Class<?> annotationClass,
			ProxyAnnotationHandler handler);

	/**
	 * Unregisters a new ProxyAnnotationHandler instance associated with the
	 * specified annotation class.
	 * 
	 * @param annotationClass
	 *            the Java class for the annotation associated with the
	 *            ProxyAnnotationHandler to remove
	 */
	public void unregisterProxyAnnotationHandler(Class<?> annotationClass);
}