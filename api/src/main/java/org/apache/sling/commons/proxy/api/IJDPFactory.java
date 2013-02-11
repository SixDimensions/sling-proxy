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
package org.apache.sling.commons.proxy.api;

import org.apache.sling.api.resource.Resource;

public interface IJDPFactory {

    /**
     * Create a new Java Dynamic Proxy instance with Primary interface 'type'
     *
     * @param r Resource - the Sling resource that backs the new JDP instance
     * @param type Class<T> - The primary interface/type for the new JDP
     * @return T extends SlingProxy - the new Java Dynamic Proxy instance type
     *  which must extend SlingProxy
     */
    <T extends SlingProxy> T newInstance(Resource r, Class<T> type);
}
