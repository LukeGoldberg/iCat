package org.logan.core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.logan.core.valve.Valve;
import org.logan.protocol.ResponseInfo;

import io.netty.handler.codec.http.HttpRequest;

public class Pipeline {
	
	private List<Valve> valves = new CopyOnWriteArrayList<>();
	
	public void addValve(Valve valve) {
		valves.add(valve);
	}
	
	public void invoke(HttpRequest request, ResponseInfo response) {
		valves.forEach(valve -> valve.invoke(request, response));
	}

}
