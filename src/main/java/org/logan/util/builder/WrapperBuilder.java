package org.logan.util.builder;

import org.logan.core.container.Wrapper;

public class WrapperBuilder {

	private Wrapper wrapper = new Wrapper();
	
	public WrapperBuilder() {
		
	}
	
	public WrapperBuilder name(String name) {
		wrapper.setName(name);
		return this;
	}
	
	public WrapperBuilder initParameter(String name, String value) {
		wrapper.addInitParameter(name, value);
		return this;
	}
	
	public WrapperBuilder servletContent(String content) {
		wrapper.setServletContent(content);
		return this;
	}
	
	public Wrapper build() {
		return wrapper;
	}
	
}
