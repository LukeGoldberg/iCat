package org.logan.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import org.logan.Cat;
import org.logan.core.lifecycle.BaseLifecycle;

public class Server extends BaseLifecycle {
	
	private static final Logger logger = Logger.getLogger("Server");

	public String address = "localhost";
	
	private Cat application;
	
	private final Object servicesLock = new Object();
	
	private CopyOnWriteArrayList<Service> services = new CopyOnWriteArrayList<>();
	
	public void setApplication(Cat application) {
		this.application = application;
	}
	
	@Override
	protected void initInternal() {
		logger.info("Server init starting...");
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
