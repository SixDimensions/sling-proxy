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
 * Interface for filtering resources to be returned by the @SlingChildren
 * annotation.
 * 
 * @see org.apache.sling.commons.proxy.annotations.SlingChildren
 */
public interface ChildFilter {

	/**
	 * Child filter which excludes the jcr:content node.
	 */
	public static final ChildFilter EXCLUDE_CONTENT_NODE = new ChildFilter() {
		public boolean filter(Resource resource) {
			if (resource.getPath().endsWith("jcr:content")) {
				return false;
			}
			return true;
		}
	};

	/**
	 * Filters the child resource, if true the child resource should be
	 * included, if false, the child resource should be excluded.
	 * 
	 * @param resource
	 *            the child resource
	 * @return true if the child resource should be included, false if the child
	 *         resource should be excluded.
	 */
	public boolean filter(Resource resource);
}
