package org.apache.sling.commons.proxy.test;

import java.io.InputStream;
import java.util.Date;

import org.apache.sling.commons.proxy.api.SlingProxy;
import org.apache.sling.commons.proxy.api.annotations.SlingProperty;

public interface FileProxy extends SlingProxy {

	@SlingProperty(name = "jcr:data", path = "jcr:content")
	public InputStream getContents();

	@SlingProperty(name = "jcr:createdBy", path = "jcr:content")
	public String getCreatedBy();

	@SlingProperty(name = "jcr:created", path = "jcr:content")
	public Date getCreationDate();

	@SlingProperty(name = "jcr:lastModifiedBy", path = "jcr:content")
	public String getLastModifiedBy();

	@SlingProperty(name = "jcr:lastModified", path = "jcr:content")
	public Date getLastModifiedDate();

	@SlingProperty(name = "jcr:mimeType", path = "jcr:content")
	public String getMimeType();

	@SlingProperty(name = "jcr:primaryType")
	public String getPrimaryType();

	@SlingProperty(name = "sling:resourceType", path = "jcr:content")
	public String getResourceType();

	@SlingProperty(name = "jcr:title", path = "jcr:content")
	public String getTitle();

}
