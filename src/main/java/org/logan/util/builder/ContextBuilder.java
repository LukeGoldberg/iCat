package org.logan.util.builder;

import org.logan.core.container.Context;

public class ContextBuilder {

	private Context context = new Context();
	
	public ContextBuilder() {
	}
	
	public Context build() {
		return context;
	}
	
}
