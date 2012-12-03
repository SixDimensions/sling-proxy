package org.apache.sling.commons.proxy;

public interface SimpleSlingProxy extends SlingProxy {

	public String getJcrTitle();

	public String getSlingResourceType();

	public String jcrTitle();

	public Boolean isActive();

	public String getNonExistentProperty();

}
