package org.logan.util.builder;

import org.logan.core.container.Host;
import org.logan.core.listener.ContainerListener;
import org.logan.core.listener.LifecycleListener;

public final class HostBuilder {

	private Host host = new Host();
	
	public HostBuilder() {
	}
	
	public HostBuilder workDir(String workDir) {
		host.setWorkDir(workDir);
		return this;
	}
	
	public HostBuilder appBase(String appBase) {
		host.setAppBase(appBase);
		return this;
	}
	
	public HostBuilder addLifecycleListener(LifecycleListener lifecycleListener) {
		host.addLifecycleListener(lifecycleListener);
		return this;
	}
	
	public HostBuilder name(String name) {
		host.setName(name);
		return this;
	}
	
	public Host build() {
		return host;
	}
	
}
