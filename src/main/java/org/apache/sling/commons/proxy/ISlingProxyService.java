package org.apache.sling.commons.proxy;

import org.apache.sling.api.resource.Resource;

/**
 * Service for retrieving ISlingProxy instances from Sling Resources.
 */
public interface ISlingProxyService {

	/**
	 * Creates new ISlingProxy instances of the provided type <code>type</code>
	 * using the <code>resource</code> as the backing Sling Resource for the
	 * JavaBean accessor methods on <code>type</code>
	 * 
	 * The following criteria must be met when invoking this method:
	 * <ol>
	 * <li><code>resource</code> must not be null</li>
	 * <li><code>type</code> must not be null</li>
	 * <li><code>type</code> must be an Interface</li>
	 * <li>Interface <code>type</code> must have at least one
	 * <code>@SlingProperty</code> Annotation</li>
	 * <li></li>
	 * <ol>
	 * 
	 * @param <AdapterType>
	 *            extends SlingProxy
	 * @param resource
	 *            Resource - the backing Resource
	 * @param type
	 *            Class - the interface that extends SlingProxy that is the
	 *            Interface we are to create a new Proxy instance of
	 * @return the new proxy instance of type <code>type</code>
	 */
	<AdapterType> AdapterType getProxy(Resource resource,
			Class<AdapterType> type);
}
