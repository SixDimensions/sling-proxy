/**
 * 
 */
package org.apache.sling.commons.jdp.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.jdp.api.IJDPFactory;
import org.apache.sling.commons.jdp.api.annotations.OSGiService;
import org.apache.sling.commons.jdp.api.annotations.OSGiServices;
import org.apache.sling.commons.reflection.Annotations;
import org.apache.sling.commons.reflection.Classes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MJKelleher  - Dec 24, 2012 9:53:39 AM 
 *
 * proxy-poc
 *
 *
 * org.apache.sling.commons.proxy.poc.jdp.DefaultJDPImpl
 */
public final class DefaultJDPFactoryImpl implements IJDPFactory {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultJDPFactoryImpl.class);
	
	public <T> T newInstance(Resource r, Class<T> type) {
		if (r == null) {
			throw new NullPointerException("The Resource cannot be NULL.");
		}
		if (type == null) {
			throw new NullPointerException("The Provided Interface cannot be NULL.");
		}
		if (! type.isInterface()) {
			String msg = "Class " + type.getName() + " must be an Interface.";
			throw new IllegalStateException(msg);
		}
		
		InvocationHandler ih = new ResourceInvocationHandler(r);
		T rtn = (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, ih);
		
		Set<Class> interfaces = Classes.getInterfaces(type);
		if (interfaces.size() > 0) {
			Set<OSGiService> osgisvcs = getOSGiServiceAnnotations(type, interfaces);
			
			if (osgisvcs.size() > 0) {
				Set<Class> svcIntfcs = toServiceInterfaces(osgisvcs);
				boolean atLeastOneRemoved = interfaces.removeAll(svcIntfcs);
			}
		}
		
		return rtn;
	}
	
	/**
	 * This takes care of both:  OSGiServices and OSGiService annotations
	 * 
	 * @param type Class - the JDP Interface to be implemented by the Proxy
	 * @param interfaces - the Interfaces defined on class 'type'
	 * @return Set<OSGiService> - all defined OSGiService annotations regardless
	 * if they are defined directly or within @OSGiServices
	 */
	private static final Set<OSGiService> getOSGiServiceAnnotations(Class type, Set<Class> interfaces) {
		Set<OSGiService> services = new java.util.HashSet<OSGiService>();
		
		Set<OSGiServices> anns = Annotations.get(type, OSGiServices.class);
		if (anns.size() < 1) {
			services.addAll(Annotations.get(type, OSGiService.class));
		} else {
			for (OSGiServices svcs : anns) {
				OSGiService[] sa = svcs.value(); 
				if (sa != null) {
					for (OSGiService s : sa) {
						services.add(s);
					}
				}
			}
		}
		
		Set<OSGiService> rtn = new java.util.HashSet<OSGiService>();
		for (OSGiService s : services) {
			if (s.service() == null) {
				LOG.warn("Interface {} was annotated with OSGiService, but it's Service value was NULL", type.getName());
				continue;
			}
			if (s.service() != Void.class) {
				LOG.warn("Interface {} was annotated with OSGiService, but it's Service value was {}", type.getName(), Void.class.getName());
				continue;
			}
			if (interfaces.contains(s.service())) {
				rtn.add(s);
			} else {
				LOG.warn("Interface {} was annotated with OSGiService, but it did not extend interface {}", type.getName(), s.service().getName());
			}
		}
		
		return rtn;
	}
	
	private static final Set<Class> toServiceInterfaces(Set<OSGiService> set) {
		Set<Class> rtn = new java.util.HashSet<Class>();
		if (set != null && set.size() > 0) {
			for (OSGiService svc : set) {
				rtn.add(svc.service());
			}
		}
		return rtn;
	}
}
