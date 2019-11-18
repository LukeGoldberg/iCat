package org.logan;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.logan.util.CatProperties;
import org.logan.util.ClassLoaderFactory;

/**
 * Hello world!
 *
 */
public class CatApplication {
	
	private static final Logger logger = Logger.getLogger("CatApplication");
	
	private static final Object daemonLock = new Object();
	private static volatile CatApplication daemon = null;
	
	ClassLoader commonLoader = null;
    ClassLoader catalinaLoader = null;
    ClassLoader sharedLoader = null;
	
	private Object worker = null;
	
	private void init() {
		
		initClassLoaders();
		
	}
	
	private void load() {
		
	}
	
	private void start() {
		
	}
	
	private void stop() {
		
	}
	
	private void initClassLoaders() {
		try {
			commonLoader = createClassLoader("common", null);
			if (commonLoader == null) {
	            commonLoader = this.getClass().getClassLoader();
	        }
	        catalinaLoader = createClassLoader("server", commonLoader);
	        sharedLoader = createClassLoader("shared", commonLoader);
		} catch(Throwable t) {
			logger.severe("Class loader creation threw exception : " + t);
            System.exit(1);
		}
	}
	
	private ClassLoader createClassLoader(String name, ClassLoader parent)
	    throws Exception {
	    String value = CatProperties.getProperty(name + ".loader");
	    if ((value == null) || (value.equals(""))) {
	        return parent;
	    }
	    ArrayList<String> repositories = new ArrayList<>();    
        return ClassLoaderFactory.createClassLoader(repositories, parent);
    }
	
    public static void main(String[] args) {
        synchronized(daemonLock) {
        	CatApplication application = new CatApplication();
        	application.init();
        	daemon = application;
        }
        String command = "start";
        if (args.length > 0) {
        	command = args[args.length - 1];
        }
        if (StringUtils.equals(command, "start")) {
        	daemon.load();
        	daemon.start();
        } else if (StringUtils.equals(command, "stop")) {
        	daemon.stop();
        }
    }
    
}
