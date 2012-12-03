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

/**
 * The ProxyAnnotationServiceManager manages the available
 * ProxyAnnotationService instances.
 * 
 * @author dklco
 */
public interface ProxyAnnotationServiceManager {

	/**
	 * Get the ProxyAnnotationService instance associated with the specified
	 * annotation class.
	 * 
	 * @param annotation
	 *            the Java class for the annotation associated with the
	 *            ProxyAnnotationService
	 * @return the ProxyAnnotationService associated with the annotation class
	 *         or null
	 */
	public ProxyAnnotationService getProxyAnnotationService(Class<?> annotation);

	/**
	 * Registers a new ProxyAnnotationService instance associated with the
	 * specified annotation class. Will replace any existing
	 * ProxyAnnotationServices for the specified annotation class.
	 * 
	 * @param annotationClass
	 *            the Java class for the annotation to associate with the
	 *            ProxyAnnotationService
	 * @param service
	 *            the ProxyAnnotationService instance
	 */
	public void registerProxyAnnotationService(Class<?> annotationClass,
			ProxyAnnotationService service);

	/**
	 * Unregisters a new ProxyAnnotationService instance associated with the
	 * specified annotation class.
	 * 
	 * @param annotationClass
	 *            the Java class for the annotation associated with the
	 *            ProxyAnnotationService to remove
	 */
	public void unregisterProxyAnnotationService(Class<?> annotationClass);
}