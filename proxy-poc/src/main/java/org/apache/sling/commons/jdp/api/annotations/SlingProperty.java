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
package org.apache.sling.commons.jdp.api.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author MJKelleher - Dec 29, 2012 5:01:32 PM
 *
 * proxy-poc
 *
 * Used to annotate Method names with the property name or relative path plus
 * property name of a JCR Property. This means that the JCR Property can be
 * located within the corresponding org.apache.sling.api.resource.Resource
 * directly, or some arbitrary path beneath the
 * org.apache.sling.api.resource.Resource.
 *
 * org.apache.sling.commons.jdp.api.annotations.SlingProperty
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SlingProperty {

    /**
     * The property name or relative path plus property name
     *
     * @return String - the property name and optionally the relative path.
     */
    String path() default "";
}
