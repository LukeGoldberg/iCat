package org.logan.protocol;

import org.logan.core.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11NioProtocol {

	private static final Logger logger = LoggerFactory.getLogger(Http11NioProtocol.class);
	
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
