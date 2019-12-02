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
		Wrapper wrapper1 = wrapperBuilder.build();
		wrapper1.setName("welcome");
		wrapper1.setServletContent("<html><h1>welcome</h1></html>");
		
		// Wrapper2
		wrapperBuilder = new WrapperBuilder();
		Wrapper wrapper2 = wrapperBuilder.build();
		wrapper2.setName("index");
		wrapper2.setServletContent("<html>index</html>");
		
		// Context
		ContextBuilder contextBuilder = new ContextBuilder();
		Context context1 = contextBuilder.build();
		context1.setName("app");
		
		// Host
		HostBuilder hostBuilder = new HostBuilder();
		Host host1 = hostBuilder.name("logan")
				.addLifecycleListener(new HostConfigListener())
				.build();
		
		Engine engine1 = new Engine();
		engine1.setName("engine1");
		
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
				throw new IllegalArgumentException("container muse has a name");
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
				throw new IllegalArgumentException("container muse has a name");
			}
			checkChildren(c.getChildren());
		}
	}

}
