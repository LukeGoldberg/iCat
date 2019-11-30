package org.logan.protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseInfo {

	private final ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
	
	public String content = "";
	
	public void addHeader(String name, String value) {
		headers.put(name, value);
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}
	
}
