/**
 * 
 */
package org.apache.sling.commons.reflection;

import java.lang.reflect.Method;
import java.util.List;


/**
 * @author MJKelleher  - Dec 25, 2012 2:54:53 PM 
 *
 * proxy-poc
 *
 * Generates a String representation based on the return values for all of the 
 * 'get' methods on the provided Object. 
 *
 * org.apache.sling.commons.proxy.poc.jdp.GetMethodToStringImpl
 */
public final class GetMethodToStringImpl implements IToString {
	private final IToString tostring = new JdkToStringImpl();
	
	public String toString(Object obj) {
		StringBuilder sb = new StringBuilder();
		
		if (obj != null) {
			addInterfaceInfo(obj, sb);
			addMethodReturnValues(obj, Methods.getterMethods(obj), sb);
		} else {
			sb.append("null");
		}
		
		return sb.toString();
	}
	
	private void addMethodReturnValues(Object obj, List<Method> list, StringBuilder sb) {
		sb.append("Get Method Values:");
		
		if (list != null && list.size() > 0) {
			for (Method m : list) {
				sb.append("\n\t").append(m.getName()).append(" = ");
				try {
					Object rtnObj = m.invoke(obj, (Object[]) null);
					sb.append("{").append(tostring.toString(rtnObj)).append("}");
				} catch (Exception ex) {
					sb.append("caused ").append(ex.getClass().getName())
					  .append(" - Message = ").append(ex.getMessage());
				}
			}
		}
		
		sb.append("\n\n");
	}
	
	@SuppressWarnings("rawtypes")
	private void addInterfaceInfo(Object obj, StringBuilder sb) {
		Class[] cla = obj.getClass().getInterfaces();
		sb.append("Implements: [");
		
		int max = (cla != null ? cla.length : -1);
		for (int ndx=0 ; ndx < max ; ndx++) {
			sb.append(cla[ndx].getName());
			if (ndx + 1 < max) sb.append(" , ");
		}
		
		sb.append("]\n");
	}
}
