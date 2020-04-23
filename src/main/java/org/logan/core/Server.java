package org.logan.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.logan.Cat;
import org.logan.core.lifecycle.BaseLifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a server, used to start/stop catalina.
 * a service is not one Container.
 * 
 * @author logan
 * @date Apr 23, 2020
 */
public class Server extends BaseLifecycle {
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	public String address = "localhost";
	
	private Cat application;
	
	private final Object servicesLock = new Object();
	
	private CopyOnWriteArrayList<Service> services = new CopyOnWriteArrayList<>();
	
	public void setApplication(Cat application) {
		this.application = application;
	}
	
	@Override
	protected void initInternal() {
        if (application != null) {
        	logger.info("Ignore ApplicationClassLoader, mount commonLoader on SystemClassLoader");
        	ClassLoader cl = application.getParentClassLoader();
        	logger.info("..." + cl);
        	while (cl != null && cl != ClassLoader.getSystemClassLoader()) {
        		cl = cl.getParent();
        		logger.info("..." + cl);
        	}
        }
		// Initialize our defined Services
		for (Service service : services) {
			service.init();
		}
	}
	
	@Override
	public void startInternal() {
		logger.info("Server starting...");
		// Start our defined Services
        synchronized (servicesLock) {
            for (Service service : services) {
                service.start();
            }
        }
	}
	
	@Override
	public void stopInternal() {
		// Stop our defined Services
        for (Service service : services) {
            service.stop();
        }
	}

	@Override
	public void pause() {
	}
	
	public List<Service> getServices() {
		return services;
	}
	
	public void addService(Service service) {
		services.add(service);
	}
	
}
