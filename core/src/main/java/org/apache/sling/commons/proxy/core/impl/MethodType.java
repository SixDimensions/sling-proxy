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
package org.apache.sling.commons.proxy.core.impl;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author MJKelleher - Dec 23, 2012 11:37:12 PM
 *
 * proxy-poc
 * 
 * Types of Method's determined solely by the Method name.
 *
 * org.apache.sling.commons.proxy.poc.jdp.MethodType
 */
enum MethodType {

    ToString("^(toString)$"), 
    HashCode("^(hashCode)$"), 
    Equals("^(equals)$"), 
    JavaBeanGet("^get([A-Z])(\\w+)$"), 
    JavaBeanIs("^is([A-Z])(\\w+)$"), 
    JavaBeanSet("^is([A-Z])(\\w+)$"), 
    Unknown("^$");
    
    private final String regex;
    MethodType(String type) {
        regex = type;
    }
    
    public Pattern getMethodPattern() {
        return Pattern.compile(regex);
    }
    public boolean isType(Method m) {
        return Pattern.matches(regex, m.getName());
    }
    
    /***************************************************************************
     * 
     * Static convenience Methods
     */
    
    /**
     * Determine if the given MethodType enum is contained within the Array of
     * MethodType's
     * @param mta MethodType[] - the MethodType Array to search in
     * @param mt MethodType - the MethodType to search for
     * @return TRUE if 'mt' is found within 'mta' 
     * FALSE otherwise
     */
    public static final boolean contains(MethodType[] mta, MethodType mt) {
        for (MethodType _mt : mta) {
            if (mt == _mt) return true;
        }
        return false;
    }
    /**
     * Get the JavaBean name of the corresponding Method
     * @param mt MethodType - The Method type of Method 'm'
     * @param m Method 
     * @return String - If the method is of the given the JavaBean name for 
     * the provided Method, if it is of type 'mt'
     */
    public static final String getBeanName(MethodType mt, Method m) {
        Matcher match = mt.getMethodPattern().matcher(m.getName());
        String name = null;
        if (match.find()) {
            if (match.groupCount() == 1) {
                name = match.group(1);
            } else {
                String pfx = match.group(1).toLowerCase();
                name = pfx + match.group(2);
            }
        }
        return name;
    }
    
    public static final MethodType getMethodType(Method m) {
        for (MethodType mt : values()) {
            if (mt.isType(m)) {
                return mt;
            }
        }
        return Unknown;
    }
}
