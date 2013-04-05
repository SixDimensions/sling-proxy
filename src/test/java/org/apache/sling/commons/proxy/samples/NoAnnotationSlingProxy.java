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
package org.apache.sling.commons.proxy.samples;

import org.apache.sling.commons.proxy.SlingProxy;

/**
 * A simple proxy interface for testing the SlingProxy API. This proxy interface
 * only utilizes the default property retrieval mechanism.
 */
public interface NoAnnotationSlingProxy extends SlingProxy {
    
	/**
	 * Gets the jcr:title attribute
	 * 
	 * @return
	 */
	public String getJcrTitle();

	/**
	 * Gets the sling:resourceType attribute.
	 * 
	 * @return
	 */
	public String getSlingResourceType();

	/**
	 * Another form of retrieving the jcr:title.
	 * 
	 * @return
	 */
	public String jcrTitle();

	/**
	 * Gets the active attribute.
	 * 
	 * @return
	 */
	public Boolean isActive();

	/**
	 * Gets the active attribute as a string.
	 * 
	 * @return
	 */
	public String active();

	/**
	 * Attempts to retrieve a property which does not exist.
	 * 
	 * @return
	 */
	public String getNonExistentProperty();

}
