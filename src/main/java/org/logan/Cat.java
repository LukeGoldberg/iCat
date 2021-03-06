package org.logan;

import java.util.logging.Logger;

import org.logan.core.Server;
import org.logan.util.ServerConfigurationUtil;

public class Cat {
	
	private static final Logger logger = Logger.getLogger("Cat");
	
    protected ClassLoader parentClassLoader =
        Cat.class.getClassLoader();
    
    /**
     * Prevent duplicate loads.
     */
    protected boolean loaded = false;
	
	/**
     * The server component we are starting or stopping.
     */
	private Server server;
	
	public void load() {
		if (loaded) {
			return;
		}
		loaded = true;
		server = ServerConfigurationUtil.configServer();
		server.init();
	}
	
	public void start() {
        if (server == null) {
            load();
        }
        if (server == null) {
            logger.severe("catalina.noServer");
            return;
        }
        long t1 = System.nanoTime();
        server.start();
        logger.info("catalina.startup" + Long.valueOf((System.nanoTime() - t1) / 1000000));
	}
	
	public void stop() {
		server.stop();
	}
	
	public ClassLoader getParentClassLoader() {
		return parentClassLoader;
	}
	
	public void setParentClassLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }

}
