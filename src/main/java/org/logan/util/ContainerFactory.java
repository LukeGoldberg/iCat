package org.logan.util;

import org.logan.core.container.Context;
import org.logan.core.container.Engine;
import org.logan.core.container.Host;
import org.logan.core.container.Wrapper;

public final class ContainerFactory {
	
	private ContainerFactory() {
		
	}
	
	public static Engine getStandardEngine() {
		return new Engine();
	}
	
	public static Host getStandardHost() {
		return new Host();
	}
	
	public static Context getStandardContext() {
		return new Context();
	}
	
	public static Wrapper getStandardWrapper() {
		return new Wrapper();
	}
	
}
