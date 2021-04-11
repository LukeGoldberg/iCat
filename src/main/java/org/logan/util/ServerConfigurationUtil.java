package org.logan.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.logan.core.Connector;
import org.logan.core.Server;
import org.logan.core.Service;
import org.logan.core.container.Container;
import org.logan.core.container.Context;
import org.logan.core.container.Engine;
import org.logan.core.container.Host;
import org.logan.core.container.Wrapper;
import org.logan.core.listener.HostConfigListener;
import org.logan.util.builder.ContextBuilder;
import org.logan.util.builder.EngineBuilder;
import org.logan.util.builder.HostBuilder;
import org.logan.util.builder.ServerBuilder;
import org.logan.util.builder.ServiceBuilder;
import org.logan.util.builder.WrapperBuilder;

/**
 * build one <code>Server</code> here.
 *
 * @author Logan
 */
public final class ServerConfigurationUtil {
	
	private ServerConfigurationUtil() {
		
	}
	
	public static Server configServer() {
		
		// Wrapper1
		WrapperBuilder wrapperBuilder = new WrapperBuilder();
		Wrapper wrapper1 = wrapperBuilder.name("welcome")
				.initParameter("servletName", "welcome")
				.servletContent("<html><h1>welcome</h1></html>")
				.build();
		
		// Wrapper2
		wrapperBuilder = new WrapperBuilder();
		Wrapper wrapper2 = wrapperBuilder.name("index")
				.initParameter("servletName", "index")
				.servletContent("<html>index<br/><a href='welcome'>go to welcome page.</a></html>")
				.build();
		
		// Context
		ContextBuilder contextBuilder = new ContextBuilder();
		Context context1 = contextBuilder.name("app1").build();
		
		// Host
		HostBuilder hostBuilder = new HostBuilder();
		Host host1 = hostBuilder.name("localhost")
				.addLifecycleListener(new HostConfigListener())
				.build();
		
		EngineBuilder engineBuilder = new EngineBuilder();
		Engine engine1 = engineBuilder.name("engine1").build();
		
		// Service
		ServiceBuilder serviceBuilder = new ServiceBuilder();
		Connector connector = new Connector();
		Service service1 = serviceBuilder.addConnector(connector)
				.addEngine(engine1)
				.build();
		connector.setService(service1);
		
		// Server
		ServerBuilder serverBuilder = new ServerBuilder();
		Server server = serverBuilder.address("localhost")
				.addService(service1)
				.build();
		
		// ...
		engine1.setService(service1);
		engine1.addChild(host1);
		host1.addChild(context1);
		context1.addChild(wrapper1);
		context1.addChild(wrapper2);
		service1.setServer(server);
		service1.setEngine(engine1);
		
		check(server);
		
		return server;
	}
	
	private static void check(Server server) {
		checkNames(server);
	}
	
	private static void checkNames(Server server) {
		for (Service service : server.getServices()) {
			Engine engine = service.getEngine();
			if (StringUtils.isBlank(engine.getName())) {
				throw new IllegalArgumentException("container must has a name");
			}
			checkChildren(engine.getChildren());
		}
	}
	
	private static void checkChildren(List<Container> children) {
		if (CollectionUtils.isEmpty(children)) {
			return;
		}
		for (Container c : children) {
			if (StringUtils.isBlank(c.getName())) {
				throw new IllegalArgumentException("container must has a name");
			}
			checkChildren(c.getChildren());
		}
	}

}
