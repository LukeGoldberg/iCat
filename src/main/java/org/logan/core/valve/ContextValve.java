package org.logan.core.valve;

import java.util.logging.Logger;

import org.logan.protocol.ResponseInfo;

import io.netty.handler.codec.http.HttpRequest;

public class ContextValve implements Valve {

	private static final Logger logger = Logger.getLogger("ContextValve");

	@Override
	public void invoke(HttpRequest request, ResponseInfo response) {
		logger.info("receive uri : " + request.uri());
	}
	
	@Override
	public int hashCode() {
		return 1;
	}
	
	@Override
	public boolean equals(Object o) {
		return true;
	}
	
}
