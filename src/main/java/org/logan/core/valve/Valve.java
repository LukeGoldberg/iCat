package org.logan.core.valve;

import org.logan.protocol.ResponseInfo;

import io.netty.handler.codec.http.HttpRequest;

public interface Valve {

	void invoke(HttpRequest request, ResponseInfo response);
	
}
