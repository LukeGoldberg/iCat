package org.logan.core;

import java.util.logging.Logger;

import org.logan.core.lifecycle.BaseLifecycle;
import org.logan.protocol.Http11NioProtocol;

public class Connector extends BaseLifecycle {
	
	private static final Logger logger = Logger.getLogger("Connector");

	protected final Http11NioProtocol protocolHandler = new Http11NioProtocol(this);
	
	protected Service service = null;
	
	@Override
	public void initInternal() {
		logger.info("Connector init starting...");
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
