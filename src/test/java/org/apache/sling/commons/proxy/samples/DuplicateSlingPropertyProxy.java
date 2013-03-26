package org.apache.sling.commons.proxy.samples;

import org.apache.sling.commons.proxy.ISlingProxy;
import org.apache.sling.commons.proxy.annotations.SlingProperty;

public interface DuplicateSlingPropertyProxy extends ISlingProxy {

	@SlingProperty(name = "jcr:title", path = "jcr:content")
	public String getTitle();

	@SlingProperty(name="sling:resourceType")
	public String getSlingResourceType();

	@SlingProperty(path = "jcr:content", name="active")
	public Boolean isActive();

	@SlingProperty(path = "jcr:content")
	public String getNonExistentProperty();
}
