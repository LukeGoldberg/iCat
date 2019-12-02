package org.logan.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.logan.core.valve.Valve;
import org.logan.protocol.ResponseInfo;

import io.netty.handler.codec.http.HttpRequest;

public class Pipeline {
	
	private Set<Valve> valves = Collections.synchronizedSet(new HashSet<>());
	
	public void addValve(Valve valve) {
		valves.add(valve);
	}
	
	public void invoke(HttpRequest request, ResponseInfo response) {
		valves.forEach(valve -> valve.invoke(request, response));
	}

}
