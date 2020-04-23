package org.logan.core;

import org.logan.core.lifecycle.BaseLifecycle;
import org.logan.protocol.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connector extends BaseLifecycle {
	
	private static final Logger logger = LoggerFactory.getLogger(Connector.class);

	protected final Http11NioProtocol protocolHandler = new Http11NioProtocol(this);
	
	protected Service service = null;
	
	@Override
	public void initInternal() {
		try {
			protocolHandler.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startInternal() {
		protocolHandler.start();
	}
	
	public Http11NioProtocol getProtocolHandler() {
		return protocolHandler;
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
	public Service getService() {
		return service;
	}

	@Override
	protected void stopInternal() {
		protocolHandler.stop();
	}
	
}
