package org.logan.core;

import java.util.ArrayList;
import java.util.List;

import org.logan.core.container.Engine;
import org.logan.core.lifecycle.BaseLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Service extends BaseLifecycle {
	
	private static final Logger logger = LoggerFactory.getLogger(Service.class);

	private Server server;
	
	private Engine engine;
	
	private List<Connector> connectors = new ArrayList<>();
	private final Object connectorsLock = new Object();
	
	public void addConnector(Connector connector) {
		connectors.add(connector);
	}

	@Override
	protected void initInternal() {
		logger.info("Service initing...");
		if (engine != null) {
            engine.init();
        }
        // Initialize our defined Connectors
        synchronized (connectorsLock) {
            for (Connector connector : connectors) {
                connector.init();
            }
        }
	}
	
	@Override
	protected void startInternal() {
		logger.info("Service starting...");
		// Start our defined Container first
        if (engine != null) {
            synchronized (engine) {
                engine.start();
            }
        }
        // Start our defined Connectors second
        synchronized (connectorsLock) {
            for (Connector connector: connectors) {
                connector.start();
            }
        }
	}
	
	@Override
	public void stopInternal() {
		// Pause connectors first
        synchronized (connectorsLock) {
            for (Connector connector: connectors) {
                connector.pause();
                // Close server socket if bound on start
                // Note: test is in AbstractEndpoint
                try {
					connector.getProtocolHandler().closeServerSocketGracefully();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
            }
        }
        // Stop our defined Container second
        if (engine != null) {
            synchronized (engine) {
                engine.stop();
            }
        }
        // Now stop the connectors
        synchronized (connectorsLock) {
            for (Connector connector: connectors) {
                connector.stop();
            }
        }
	}

	@Override
	public void pause() {
	}
	
	public void setServer(Server server) {
		this.server = server;
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	public Engine getEngine() {
		return engine;
	}
	
}
