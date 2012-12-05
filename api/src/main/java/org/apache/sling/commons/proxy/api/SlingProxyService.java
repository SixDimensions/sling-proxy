package org.apache.sling.commons.proxy.api;

import org.apache.sling.api.resource.Resource;

/**
 * Service for retrieving Sling Dynamic Proxy instances from Sling Resources.
 * 
 * @author dklco
 */
public interface SlingProxyService {

	/**
	 * Get the Java Dynamic Proxy instance for the specified adapter type and
	 * Sling Resource.
	 * 
	 * @param resource
	 *            the resource to back the proxy with
	 * @param type
	 *            the proxy type to create
	 * @return the proxy instance
	 */
	<AdapterType> AdapterType getProxy(Resource resource,
			Class<AdapterType> type);
}
