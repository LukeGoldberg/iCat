package org.logan.core.event;

public class ContainerEvent implements Event {

	/**
     * The event data associated with this event.
     */
    private final Object data;


    /**
     * The event type this instance represents.
     */
    private final String type;
    
    public ContainerEvent(String type, Object data) {
    	this.type = type;
    	this.data = data;
    }
	
}
