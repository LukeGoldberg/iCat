package org.logan.core.lifecycle;

import org.logan.core.listener.LifecycleListener;

public interface Lifecycle {

	void addLifecycleListener(LifecycleListener listener);
	
	void removeLifecycleListener(LifecycleListener listener);
	
	void init();
	
	void start();
	
	void stop();
	
	void destroy();
	
	LifecycleState getState();
	
}
