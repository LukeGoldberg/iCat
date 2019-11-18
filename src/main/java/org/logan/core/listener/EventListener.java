package org.logan.core.listener;

import org.logan.core.event.Event;

public interface EventListener {

	void fireEvent(Event event);
	
}
