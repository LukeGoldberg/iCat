package org.logan.core.container;

import java.util.Set;

import org.logan.core.Pipeline;
import org.logan.core.event.ContainerEvent;
import org.logan.core.listener.ContainerListener;

public interface Container {
    
	void addChild(Container child);
	
	Pipeline getPipeline();
	
	void setParent(Container parent);
	
	Container getParent();
	
	Set<Container> getChildren();
	
	String getName();

	void addContainerListener(ContainerListener listener);
	
	void removeContainerListener(ContainerListener listener);
	
	void fireContainerEvent(ContainerEvent event);
	
}
