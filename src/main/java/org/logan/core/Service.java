package org.logan.core;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.logan.core.container.Engine;
import org.logan.core.lifecycle.BaseLifecycle;

public class Service extends BaseLifecycle {
	
	private static final Logger logger = Logger.getLogger("Service");

	private Server server;
	
	private Engine engine;
	
	private List<Connector> connectors = new ArrayList<>();
	private final Object connectorsLock = new Object();
	
	public void addConnector(Connector connector) {
		connectors.add(connector);
	}

	@Override
	protected void initInternal() {
		logger.info("Service init starting...");
		if (engine != null) {
            engine.init();
        }

//        // Initialize any Executors
//        for (Executor executor : findExecutors()) {
//            if (executor instanceof JmxEnabled) {
//                ((JmxEnabled) executor).setDomain(getDomain());
//            }
//            executor.init();
//        }
//
//        // Initialize mapper listener
//        mapperListener.init();

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

//        synchronized (executors) {
//            for (Executor executor: executors) {
//                executor.start();
//            }
//        }
//
//        mapperListener.start();

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
//                if (!LifecycleState.STARTED.equals(connector.getState())) {
                    // Connectors only need stopping if they are currently
                    // started. They may have failed to start or may have been
                    // stopped (e.g. via a JMX call)
//                    continue;
//                }
                connector.stop();
            }
        }

        // If the Server failed to start, the mapperListener won't have been
        // started
//        if (mapperListener.getState() != LifecycleState.INITIALIZED) {
//            mapperListener.stop();
//        }

//        synchronized (executors) {
//            for (Executor executor: executors) {
//                executor.stop();
//            }
//        }
        
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
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
