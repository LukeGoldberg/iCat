package org.logan.util.builder;

import org.logan.Cat;
import org.logan.core.Server;
import org.logan.core.Service;

public final class ServerBuilder {
	
	private Server server = new Server();
	
	public ServerBuilder() {
	}
	
	public ServerBuilder address(String address) {
		server.address = address;
		return this;
	}
	
	public ServerBuilder addService(Service service) {
		server.addService(service);
		return this;
	}
	
    public ServerBuilder application(Cat application) {
    	server.setApplication(application);
    	return this;
    }
    
    public Server build() {
    	return server;
    }
	
}
