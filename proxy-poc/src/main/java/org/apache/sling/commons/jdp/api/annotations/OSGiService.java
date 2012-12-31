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
 * @author MJKelleher - Dec 29, 2012 4:54:09 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.jdp.api.annotations.OSGiService
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OSGiService {

    /**
     * Every JDP will define a single interface that it will implement. This
     * interface may extend other interfaces that represent "Services" - that
     * is, an interface that represents methods that are not get/set type
     * methods. These "Service" interfaces in themselves should not be annotated
     * to identify where the implementations come from. This would restrict the
     * what kind of implementations can be used.
     *
     * In order to provide implementations for these "Service" interfaces, there
     * must be some way of identifying where the implementations will come from.
     *
     * This annotation on the JDP Interface identifies which "Service" interface
     * it relates to. This 'service()' value provides that relationship.
     *
     * The default value for this is 'Void.class' - if this has not been set to
     * a non 'Void.class' value, then this instance of the @OSGiService
     * annotation will be ignored.
     *
     * @return Class<?> - the corresponding Interface that we implement - when
     * searching for instances of the OSGi service, this will allow us to
     * correlate the implemented interface to an OSGi Service Filter or a
     * specific pid.
     */
    Class<?> service() default Void.class;

    /**
     * The pid to use in the Search for an implementation for the related
     * interface in 'service()'. This take precedence over 'filter()'
     *
     * @return String - the OSGi Service pid for the implementation
     */
    String pid() default "";

    /**
     * The OSGi Service Filter to use when searching for an implementation for
     * the related interface in 'service()'. 'pid()' takes precedence over this
     * value.
     *
     * @return String - The OSGi search Filter for the implementation
     */
    String filter() default "";

    /**
     * The specific implementation to use. 'pid()' and 'filter()' take
     * precendence over this value. This is primarily for testing or usage when
     * we are not running within an OSGi container. If we are not, we obviously
     * cannot perform an OSGi search for a Service. The default Value for this
     * is 'Void.class' which indicates that this values is NOT to be used to
     * find an Implementation
     *
     * @return Class<?> the implementation Class
     */
    Class<?> implementation() default Void.class;
}
