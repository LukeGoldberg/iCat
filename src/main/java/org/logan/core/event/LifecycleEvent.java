package org.logan.core.event;

import org.logan.core.lifecycle.LifecycleState;

public class LifecycleEvent implements Event {

	/**
     * The event data associated with this event.
     */
    public final Object data;

    /**
     * The event type this instance represents.
     */
    public final LifecycleState state;
    
    public LifecycleEvent(LifecycleState state, Object data) {
    	this.state = state;
    	this.data = data;
    }
	
}
