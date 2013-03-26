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

import org.apache.sling.api.resource.Resource;

/**
 * Service for retrieving ISlingProxy instances from Sling Resources.
 */
public interface ISlingProxyService {

	/**
	 * Creates new ISlingProxy instances of the provided type <code>type</code>
	 * using the <code>resource</code> as the backing Sling Resource for the
	 * JavaBean accessor methods on <code>type</code>
	 * 
	 * The following criteria must be met when invoking this method:
	 * <ol>
	 * <li><code>resource</code> must not be null</li>
	 * <li><code>type</code> must not be null</li>
	 * <li><code>type</code> must be an Interface</li>
	 * <li>Interface <code>type</code> must have at least one
	 * <code>@SlingProperty</code>, <code>@SlingReference</code> or <code>@SlingChildren</code> Annotation</li>
	 * </ol>
	 * 
	 * @param <AdapterType>
	 *            extends SlingProxy
	 * @param resource
	 *            Resource - the backing Resource
	 * @param type
	 *            Class - the interface that extends SlingProxy that is the
	 *            Interface we are to create a new Proxy instance of
	 * @return the new proxy instance of type <code>type</code>
	 */
	<AdapterType> AdapterType getProxy(Resource resource,
			Class<AdapterType> type);
}
