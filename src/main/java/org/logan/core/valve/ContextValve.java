package org.logan.core.valve;

import org.logan.protocol.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpRequest;

public class ContextValve implements Valve {

	private static final Logger logger = LoggerFactory.getLogger(ContextValve.class);

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
