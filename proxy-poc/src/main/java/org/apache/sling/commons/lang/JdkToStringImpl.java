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
package org.apache.sling.commons.lang;

import org.apache.sling.commons.lang.IToString;
import java.lang.reflect.Method;

/**
 * @author MJKelleher - Dec 25, 2012 3:12:05 PM
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.api.reflection.JdkToStringImpl
 */
public final class JdkToStringImpl implements IToString {

    public String toString(Object obj) {
        StringBuilder sb = new StringBuilder(256);

        if (obj != null) {
            if (!supportsToString(obj, sb)) {
                handleByType(obj, sb);
            }
        } else {
            sb.append("null");
        }

        return sb.toString();
    }

    private boolean supportsToString(Object obj, StringBuilder sb) {
        try {
            Method m = obj.getClass().getDeclaredMethod("toString", (Class[]) null);
            try {
                Object result = m.invoke(obj, (Object[]) null);
                sb.append(result);
            } catch (Exception ex) {
                sb.append("caused - ").append(ex.getClass().getName()).append(" - Message = ").append(ex.getMessage());
            }
            return true;
        } catch (SecurityException ex) {
        } catch (NoSuchMethodException ex) {
        }

        return false;
    }

    private void handleByType(Object obj, StringBuilder sb) {
        if (obj.getClass().isArray()) {
            Object[] oa = (Object[]) obj;
            sb.append("[ ");
            for (int ndx = 0, max = oa.length; ndx < max; ndx++) {
                sb.append(toString(oa[ndx]));
                if (ndx + 1 < max) {
                    sb.append(" , ");
                }
            }
            sb.append(" ]");
        } else {
            sb.append(obj);
        }
    }
}
