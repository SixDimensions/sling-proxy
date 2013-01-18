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

package org.apache.sling.commons.proxy.api.annotations;


import java.lang.annotation.*;

/**
 * @author MJKelleher - Jan 17, 2013 4:47:30 PM
 *
 * Apache Sling Proxy API
 *
 *
 * org.apache.sling.commons.proxy.api.annotations.InjectMember
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
@Documented
public @interface InjectField {
	/**
	 * Whether or not this instance of 'Inject' is enabled.  Probably used
         * during testing to mark a Field as injectable, but disable it for 
         * specific test cases.
	 * 
	 * @return boolean TRUE - injecting of this Field is allowed otherwise
         * injecting of this field is not allowed.
	 */
	boolean enabled() default true;
} 