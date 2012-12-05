package org.apache.sling.commons.proxy.test;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.adapter.AdapterFactory;
import org.apache.sling.commons.proxy.api.AbstractProxyAdapterFactory;

@Component(label = "File Proxy Adapter Factory", name = "org.apache.sling.commons.proxy.test.FileProxyAdapterFactory", metatype = true, immediate = true)
@Service(value = AdapterFactory.class)
@Properties(value = {
		@Property(name = AdapterFactory.ADAPTABLE_CLASSES, value = "org.apache.sling.api.resource.Resource"),
		@Property(name = AdapterFactory.ADAPTER_CLASSES, value = "org.apache.sling.commons.proxy.test.FileProxy"),
		@Property(name = "service.vendor", value = "The Apache Software Foundation"),
		@Property(name = "service.description", value = "File Proxy Adapter Factory") })
public class FileProxyAdapterFactory extends AbstractProxyAdapterFactory {

}
