package org.logan.protocol;

import java.util.logging.Logger;

import org.logan.core.Connector;

public class Http11NioProtocol {

	private static final Logger logger = Logger.getLogger("Http11NioProtocol");
	
	private final Connector connector;
	
	private final NioEndpoint endpoint;
	
	public Http11NioProtocol(Connector connector) {
		this.connector = connector;
		endpoint = new NioEndpoint(this);
	}
	
	public void init() {
		logger.info("Http11NioProtocol init starting...");
		endpoint.init();
	}
	
	public void start() {
		endpoint.start();
	}
	
	public void closeServerSocketGracefully() throws Exception {
		endpoint.closeServerSocketGracefully();
	}
	
	public Connector getConnector() {
		return connector;
	}
	
	public void stop() {
		try {
			endpoint.closeServerSocketGracefully();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
}
