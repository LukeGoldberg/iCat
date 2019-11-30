package org.logan.util.builder;

import org.logan.core.Connector;
import org.logan.core.Service;
import org.logan.core.container.Engine;

public final class ServiceBuilder {

	private Service service = new Service();
	
	public ServiceBuilder() {
	}
	
	public ServiceBuilder addConnector(Connector connector) {
		service.addConnector(connector);
		return this;
	}
	
	public ServiceBuilder addEngine(Engine engine) {
		service.setEngine(engine);
		return this;
	}
	
	public Service build() {
		return service;
	}
	
}
