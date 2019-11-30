package org.logan.core.valve;

import org.logan.protocol.ResponseInfo;

import io.netty.handler.codec.http.HttpRequest;

public class HostValve implements Valve {

	@Override
	public void invoke(HttpRequest request, ResponseInfo response) {
		response.addHeader("content-type", "text/html");
	}

}
