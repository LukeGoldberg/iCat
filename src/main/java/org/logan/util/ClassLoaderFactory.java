package org.logan.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClassLoaderFactory {
	
	private static final Logger log = LoggerFactory.getLogger(ClassLoaderFactory.class);
	
	private ClassLoaderFactory() {
		
	}

	public static ClassLoader createClassLoader(List<String> repositories, ClassLoader parent) throws IOException {
		if (CollectionUtils.isEmpty(repositories)) {
			return parent;
		}
		HashSet<URL> set = new HashSet<>();
		for (String repository : repositories) {
			File directory=new File(repository);
	        directory = directory.getCanonicalFile();
	        log.info("Including global directory : "
	                + directory.getAbsolutePath());
	        String filenames[] = directory.list();
	        if (filenames == null) {
	            continue;
	        }
	        for (int j = 0; j < filenames.length; j++) {
	            String filename = filenames[j].toLowerCase(Locale.ENGLISH);
	            if (!filename.endsWith(".jar"))
	                continue;
	            File file = new File(directory, filenames[j]);
	            file = file.getCanonicalFile();
                log.info("Including glob jar file : "
	                    + file.getAbsolutePath());
	            URL url = buildClassLoaderUrl(file);
	            set.add(url);
	        }
		}
		
		if (parent == null) {
			return new URLClassLoader(set.toArray(new URL[repositories.size()]));
		}
		return new URLClassLoader(set.toArray(new URL[repositories.size()]), parent);
	}
	
    private static URL buildClassLoaderUrl(File file) throws MalformedURLException {
        // Could be a directory or a file
        String fileUrlString = file.toURI().toString();
    	// URLs passed to class loaders may point to directories that contain
        // JARs. If these URLs are used to construct URLs for resources in a JAR
        // the URL will be used as is. It is therefore necessary to ensure that
        // the sequence "!/" is not present in a class loader URL.
        fileUrlString = fileUrlString.replaceAll("!/", "%21/");
        return new URL(fileUrlString);
    }
    
}
