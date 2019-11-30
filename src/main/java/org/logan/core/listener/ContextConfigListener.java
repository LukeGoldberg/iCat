package org.logan.core.listener;

import java.util.logging.Logger;

import org.logan.core.event.LifecycleEvent;

public class ContextConfigListener implements LifecycleListener {

	private static final Logger logger = Logger.getLogger("ContextConfigListener");
	
	@Override
	public void fireEvent(LifecycleEvent event) {
		logger.info("event : " + event);
		// TODO Auto-generated method stub
		
	}

}
