package org.logan.core.container;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.logan.core.valve.ContextValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context extends BaseContainer {
	
	private static final Logger logger = LoggerFactory.getLogger(Context.class);

	@Override
	public void initInternal() {
		getPipeline().addValve(new ContextValve());
	}
	
	@Override
	public void startInternal() {
		logger.info("Context starting...");
        for (Container child : getChildren()) {
            if (!child.getState().isAvailable()) {
                child.start();
            }
        }
        try {
			loadOnStartup(getChildren());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException | ServletException e) {
			e.printStackTrace();
			System.exit(1);
		}
        
	}
	
	@Override
	public void stopInternal() {
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
    	for (Container wrapper : getChildren()) {
    		((Wrapper) wrapper).load();
    	}
        return true;
    }

	@Override
	public void pause() {
	}
	
	public ServletContext getServletContext() {
        return null;
	}
	
}
