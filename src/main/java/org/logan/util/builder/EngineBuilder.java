package org.logan.util.builder;

import org.logan.core.container.Engine;
import org.logan.core.container.Host;

public final class EngineBuilder {
	
	private Engine engine = new Engine();
	
	public EngineBuilder() {
	}
	
	public EngineBuilder hostAddress(String hostAddress) {
		engine.setHostAddress(hostAddress);
		return this;
	}
	
	public EngineBuilder addHost(Host host) {
		engine.addChild(host);
		return this;
	}
	
	public Engine build() {
		return engine;
	}

}
