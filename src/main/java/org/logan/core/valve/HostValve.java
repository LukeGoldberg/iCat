package org.logan.core.valve;

import org.logan.protocol.ResponseInfo;

import io.netty.handler.codec.http.HttpRequest;

public class HostValve implements Valve {

	@Override
	public void invoke(HttpRequest request, ResponseInfo response) {
		response.addHeader("content-type", "text/html;charset=utf-8");
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
