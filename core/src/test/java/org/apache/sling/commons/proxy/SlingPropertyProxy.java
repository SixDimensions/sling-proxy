package org.apache.sling.commons.proxy;

import org.apache.sling.commons.proxy.api.SlingProxy;
import org.apache.sling.commons.proxy.api.annotations.SlingProperty;

public interface SlingPropertyProxy extends SlingProxy {

	@SlingProperty(name = "jcr:title", path = "jcr:content")
	public String getTitle();

	public String getSlingResourceType();

	@SlingProperty(path = "jcr:content")
	public Boolean isActive();

	@SlingProperty(path = "jcr:content")
	public String getNonExistentProperty();
}
