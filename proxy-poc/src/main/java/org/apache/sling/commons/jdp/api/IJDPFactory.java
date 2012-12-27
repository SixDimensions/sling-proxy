package org.apache.sling.commons.jdp.api;

import org.apache.sling.api.resource.Resource;

public interface IJDPFactory {
	
	/**
	 * Create a new Java Dynamic Proxy instance with Primary interface 'type'
	 * 
	 * @param r Resource - the Sling resource that backs the new JDP instance
	 * @param type Class<T> - The primary interface/type for the new JDP 
	 * @return T - the new Java Dynamic Proxy instance
	 */
	<T> T newInstance(Resource r, Class<T> type);
}
