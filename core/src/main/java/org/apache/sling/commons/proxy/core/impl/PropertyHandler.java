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

import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.apache.sling.api.resource.Resource;

/**
 * @author MJKelleher - Dec 24, 2012 12:27:17 AM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.PropertyHandler
 */
public final class PropertyHandler {

    private PropertyHandler() {
    }

    public static Property getProperty(Resource r, String relPath)
            throws PathNotFoundException, RepositoryException {
        Node n = r.adaptTo(Node.class);
        return n.getProperty(relPath);
    }

    public static <T> T castProperty(Property p, Class<T> type)
            throws RepositoryException, ValueFormatException {
        return cast(p.getValue(), type);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] castPropertyArray(Property p, Class<T> type)
            throws RepositoryException, ValueFormatException {
        Value[] va = p.getValues();
        List<T> list = new java.util.ArrayList<T>(va.length);
        for (Value v : va) {
            list.add(cast(v, type));
        }
        T[] ta = (T[]) Array.newInstance(type, va.length);
        return list.toArray(ta);
    }

    /**
     * ************************************************************************
     *
     *
     */
    /**
     * The cases where the requested type may not match the Value type are:
     * Requested java.util.Date - value is java.util.Calendar Requested some I/O
     * type - value is Binary
     *
     * @param v
     * @param type
     * @return
     * @throws RepositoryException
     */
    @SuppressWarnings("unchecked")
    static <T> T cast(Value v, Class<T> type) throws RepositoryException {
        if (type == null) {
            String msg = "Attempt to cast Value Object from NULL Class<T> type";
            throw new NullPointerException(msg);
        }
        if (v == null) {
            return (T) null;
        }

        Object rtn = null;
        switch (toPropertyType(type)) {
            case PropertyType.BINARY:
                rtn = v.getStream();
                break;
            case PropertyType.BOOLEAN:
                rtn = v.getBoolean();
                break;
            case PropertyType.DATE:
                rtn = v.getDate();
                break;
            /*
            case PropertyType.DECIMAL:
                rtn = v.getDecimal();
                break;
            */
            case PropertyType.DOUBLE:
                rtn = v.getDouble();
                break;
            case PropertyType.LONG:
                rtn = v.getLong();
                break;
            case PropertyType.NAME:
                rtn = v.getString();
                break;
            case PropertyType.PATH:
                rtn = v.getString();
                break;
            case PropertyType.REFERENCE:
                rtn = v.getString();
                break;
            case PropertyType.STRING:
                rtn = v.getString();
                break;
            case PropertyType.UNDEFINED:
                rtn = v.getString();
                break;
            /*
            case PropertyType.URI:
                rtn = v.getString();
                break;
            case PropertyType.WEAKREFERENCE:
                rtn = v.getString();
                break;
            */
        }

        if (rtn == null) {
            return (T) null;
        }

        if (!type.isAssignableFrom(rtn.getClass())) {
            rtn = coerce(rtn, type);
        }

        return (T) rtn;
    }

    /**
     * ************************************************************************
     *
     *
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static int toPropertyType(Class type) {
        int pt = PropertyType.STRING;
        if (type.isAssignableFrom(Boolean.class)) {
            pt = PropertyType.BOOLEAN;
        } else if (type.isAssignableFrom(boolean.class)) {
            pt = PropertyType.BOOLEAN;
        } else if (type.isAssignableFrom(Date.class)) {
            pt = PropertyType.DATE;
        } else if (type.isAssignableFrom(Calendar.class)) {
            pt = PropertyType.DATE;
//        } else if (type.isAssignableFrom(Float.class)) {
//            pt = PropertyType.DECIMAL;
        } else if (type.isAssignableFrom(Double.class)) {
            pt = PropertyType.DOUBLE;
        } else if (type.isAssignableFrom(Long.class)) {
            pt = PropertyType.LONG;
        } else if (type.equals(InputStream.class)) {
            pt = PropertyType.BINARY;
        } else if (type.isAssignableFrom(FilterInputStream.class)) {
            pt = PropertyType.BINARY;
        } else if (type.isAssignableFrom(ObjectInputStream.class)) {
            pt = PropertyType.BINARY;
        }
        return pt;
    }

    /**
     * @TODO - turn 'coerce' into an Interface, and this class will potentially
     * contain many implementations of 'coerce' - and this will loop through
     * them all until it finds one that will coerce the source type into the
     * target type.
     *
     * @param source
     * @param type
     * @return
     * @throws RepositoryException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static Object coerce(Object source, Class type)
            throws RepositoryException {
        if (Calendar.class.isAssignableFrom(source.getClass())) {
            Calendar c = (Calendar) source;
            if (Date.class.isAssignableFrom(type)) {
                return c.getTime();
            } else if (String.class.isAssignableFrom(type)) {
                return c.getTime().toString();
            } else if (Long.class.isAssignableFrom(type)) {
                return Long.valueOf(c.getTime().getTime());
            }
        }
        /**
        if (source instanceof Binary) {
            Binary b = (Binary) source;
            if (type.equals(InputStream.class)) {
                return b.getStream();
            }
            try {
                Constructor c = type.getConstructor(InputStream.class);
                return c.newInstance(b.getStream());
            } catch (Exception e) {
                throw new ValueFormatException(e);
            }
        }
        **/
        String msg = "Could not coerce " + source + " to type " + type.getName() + ".";
        throw new ValueFormatException(msg);
    }
}
