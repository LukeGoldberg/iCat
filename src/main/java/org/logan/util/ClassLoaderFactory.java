package org.logan.util;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

public final class ClassLoaderFactory {
	
	private ClassLoaderFactory() {
		
	}

	public static ClassLoader createClassLoader(List<String> repositories, ClassLoader parent) {
		if (CollectionUtils.isEmpty(repositories)) {
			return parent;
		}
		if (parent == null) {
			return new URLClassLoader(repositories.toArray(new URL[repositories.size()]));
		}
		return new URLClassLoader(repositories.toArray(new URL[repositories.size()]), parent);
	}
	
}
