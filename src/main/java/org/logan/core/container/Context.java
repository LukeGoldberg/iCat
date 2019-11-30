package org.logan.core.container;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.logan.core.valve.ContextValve;

public class Context extends BaseContainer {
	
	private static final Logger logger = Logger.getLogger("Context");

	private List<String> welcomeFiles;
	
	private String workDir;
	
	private URL configFile;
	
	private ConcurrentHashMap<String, String> parameters = new ConcurrentHashMap<>();
	
	private String requestEncoding;
	
	private String responseEncoding;
	
	@Override
	public void initInternal() {
		getPipeline().addValve(new ContextValve());
	}
	
	@Override
	public void startInternal() {
		logger.info("Context starting...");
		// configure every property
		
		
		
		// Notify our interested LifecycleListeners
//        fireLifecycleEvent(Lifecycle.CONFIGURE_START_EVENT, null);

        // Start our child containers, if not already started
        for (Container child : getChildren()) {
            if (!child.getState().isAvailable()) {
                child.start();
            }
        }
        
        try {
			loadOnStartup(getChildren());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException | ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
        
	}
	
	@Override
	public void stopInternal() {
		
		// set every property
		
//		setState(LifecycleState.STOPPING);
		
		for (Container wrapper : getChildren()) {
            wrapper.stop();
        }
		
	}
	
	/**
     * Load and initialize all servlets marked "load on startup" in the
     * web application deployment descriptor.
     *
     * @param children Array of wrappers for all currently defined
     *  servlets (including those not declared load on startup)
     * @return <code>true</code> if load on startup was considered successful
	 * @throws ServletException 
	 * @throws ClassNotFoundException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
     */
    private boolean loadOnStartup(List<Container> childre) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException {
    	
//    	// Collect "load on startup" servlets that need to be initialized
//        TreeMap<Integer, ArrayList<Wrapper>> map = new TreeMap<>();
//        for (int i = 0; i < children.length; i++) {
//            Wrapper wrapper = (Wrapper) children[i];
//            int loadOnStartup = wrapper.getLoadOnStartup();
//            if (loadOnStartup < 0)
//                continue;
//            Integer key = Integer.valueOf(loadOnStartup);
//            ArrayList<Wrapper> list = map.get(key);
//            if (list == null) {
//                list = new ArrayList<>();
//                map.put(key, list);
//            }
//            list.add(wrapper);
//        }
//
//        // Load the collected "load on startup" servlets
//        for (ArrayList<Wrapper> list : map.values()) {
//            for (Wrapper wrapper : list) {
//                try {
//                    wrapper.load();
//                } catch (ServletException e) {
//                    getLogger().error(sm.getString("standardContext.loadOnStartup.loadException",
//                          getName(), wrapper.getName()), StandardWrapper.getRootCause(e));
//                    // NOTE: load errors (including a servlet that throws
//                    // UnavailableException from the init() method) are NOT
//                    // fatal to application startup
//                    // unless failCtxIfServletStartFails="true" is specified
//                    if(getComputedFailCtxIfServletStartFails()) {
//                        return false;
//                    }
//                }
//            }
//        }
    	
    	for (Container wrapper : getChildren()) {
    		((Wrapper) wrapper).load();
    	}
    	
        return true;
    	
    }

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
}
