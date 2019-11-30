package org.logan.core.container;

import java.util.List;

import org.logan.core.Pipeline;
import org.logan.core.lifecycle.Lifecycle;
import org.logan.protocol.ResponseInfo;

import io.netty.handler.codec.http.HttpRequest;

public interface Container extends Lifecycle {
    
	void addChild(Container child);
	
	Pipeline getPipeline();
	
	void setParent(Container parent);
	
	Container getParent();
	
	List<Container> getChildren();
	
	String getName();
	
	void setName(String name);
	
	void parseRequest(String uri, HttpRequest request, ResponseInfo response);

//	void addContainerListener(ContainerListener listener);
//	
//	void removeContainerListener(ContainerListener listener);
//	
//	void fireContainerEvent(ContainerEvent event);
	
}
