package org.logan.core.listener;

import org.logan.core.event.LifecycleEvent;

public interface LifecycleListener {
	void fireEvent(LifecycleEvent event);
}
