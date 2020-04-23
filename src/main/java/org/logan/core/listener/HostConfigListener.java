package org.logan.core.listener;

import org.logan.core.container.Host;
import org.logan.core.event.LifecycleEvent;
import org.logan.core.lifecycle.LifecycleState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HostConfigListener implements LifecycleListener {
	
	private static final Logger logger = LoggerFactory.getLogger("HostConfig");

	private Host host;
	
	@Override
	public void fireEvent(LifecycleEvent event) {
		if (event == null) {
			return;
		}
		if (event.data == null || !(event.data instanceof Host)) {
			return;
		}
		host = (Host) event.data;
		if (event.state != null && event.state == LifecycleState.STARTED) {
			deployApps();
		}
	}
	
	/**
     * Deploy applications for any directories or WAR files that are found
     * in our "application root" directory.
     */
    private void deployApps() {
        logger.info("deploying apps");    
        deployWARs("");
    }
    
    private void deployWARs(String appAbsolutePath) {
    	// TODO read web.xml, build a Context, and add it to host as a child.
    }

}
