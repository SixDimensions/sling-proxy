package org.apache.sling.commons.proxy;

import org.apache.sling.commons.proxy.annotations.SlingProperty;

public interface SlingPropertyProxy extends ISlingProxy {

	@SlingProperty(name = "jcr:title", path = "jcr:content")
	public String getTitle();

	public String getSlingResourceType();

	@SlingProperty(path = "jcr:content")
	public Boolean isActive();

	@SlingProperty(path = "jcr:content")
	public String getNonExistentProperty();
}
