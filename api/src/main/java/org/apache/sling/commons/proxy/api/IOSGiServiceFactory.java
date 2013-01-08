/*
 * Copyright 2013 Six Dimensions.
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

/**
* @author MJKelleher - Jan 7, 2013 11:39:32 PM
 *
 * Apache Sling Proxy API
 *
 *
 * org.apache.sling.commons.proxy.api.IOSGiServiceFactory
 */
public interface IOSGiServiceFactory<T> {
    /**
     * Creates new instances of type T.  T must contain a public no-args 
     * constructor.  If the new instance has a member that is the Type of the
     * SlingProxy being, will have the SlingProxy instance Injected.  The member
     * does not have to be public or even mutable.
     * @return 
     */
    T create();
}
