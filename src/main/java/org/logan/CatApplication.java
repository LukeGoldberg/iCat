package org.logan;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.logan.util.CatProperties;
import org.logan.util.ClassLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class CatApplication {
	
	private static final Logger logger = LoggerFactory.getLogger("CatApplication");
	
	private static final Object daemonLock = new Object();
	private static volatile CatApplication daemon = null;
	private static final CatProperties catProperties = new CatProperties();
    private static final Pattern PATH_PATTERN = Pattern.compile("(\".*?\")|(([^,])*)");
	
	
	ClassLoader commonLoader = null;
    ClassLoader catalinaLoader = null;
    ClassLoader sharedLoader = null;
    
    /**
     * Daemon reference.
     */
    private Object catalinaDaemon = null;
	
	private void init() throws Exception {
		
		initClassLoaders();
		
		// use the class loaders.
		Thread.currentThread().setContextClassLoader(catalinaLoader);
		
		// use catalinaLoader load Cat Class
		Class<?> startupClass = catalinaLoader.loadClass("org.logan.Cat");
        Object startupInstance = startupClass.getConstructor().newInstance();
		
        String methodName = "setParentClassLoader";
        Class<?> paramTypes[] = new Class[1];
        paramTypes[0] = Class.forName("java.lang.ClassLoader");
        Object paramValues[] = new Object[1];
        paramValues[0] = sharedLoader;
        Method method =
            startupInstance.getClass().getMethod(methodName, paramTypes);
        method.invoke(startupInstance, paramValues);
        
        catalinaDaemon = startupInstance;
	}
	
	private void load() throws Exception {
        String methodName = "load";
        Object param[] = null;
        Class<?> paramTypes[] = null;
        Method method =
            catalinaDaemon.getClass().getMethod(methodName, paramTypes);
        method.invoke(catalinaDaemon, param);
	}
	
	private void start() throws Exception {
		if (catalinaDaemon == null) {
            init();
        }
        Method method = catalinaDaemon.getClass().getMethod("start", (Class [])null);
        method.invoke(catalinaDaemon, (Object [])null);
	}
	
	private void stop() throws Exception {
		Method method = catalinaDaemon.getClass().getMethod("stop", (Class []) null);
        method.invoke(catalinaDaemon, (Object []) null);
	}
	
	private void initClassLoaders() {
		// after Tomcat-5.5, only common class loader has actual meaning.
		try {
			commonLoader = createClassLoader("common", null);
			if (commonLoader == null) {
	            commonLoader = this.getClass().getClassLoader();
	        }
	        catalinaLoader = createClassLoader("server", commonLoader);
	        sharedLoader = createClassLoader("shared", commonLoader);
		} catch(Throwable t) {
			logger.error("Class loader creation throw exception : " + t);
            System.exit(1);
		}
		// if there is no configuration in Catalina.properties,
		// commonLoader is sun.misc.AppClassLoder,
		// else is java.net.URLClassLoader.
		logger.info("comonLoader is : " + commonLoader
				+ "\r\ncatalinaLoader is : " + catalinaLoader
				+ "\r\nsharedLoader is : " + sharedLoader);
	}
	
	private ClassLoader createClassLoader(String name, ClassLoader parent) throws Exception {
		String value = catProperties.getProperty(name + ".loader");
	    if ((value == null) || (value.equals(""))) {
	        return parent;
	    }
	    List<String> repositoryPaths = getPaths(value);
        return ClassLoaderFactory.createClassLoader(repositoryPaths, parent);
    }
	
    public static void main(String[] args) {
        synchronized(daemonLock) {
        	CatApplication application = new CatApplication();
        	try {
				application.init();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
        	daemon = application;
        }
        try {
        	String command = "start";
            if (args.length > 0) {
            	command = args[args.length - 1];
            }
            if (StringUtils.equals(command, "start")) {
            	daemon.load();
            	daemon.start();
            } else if (StringUtils.equals(command, "stop")) {
            	daemon.stop();
            }
        } catch (Throwable t) {
            if (t instanceof InvocationTargetException &&
                    t.getCause() != null) {
                t = t.getCause();
            }
            t.printStackTrace();
            System.exit(1);
        }
    }
    
    protected static List<String> getPaths(String value) {
        ArrayList<String> result = new ArrayList<>();
        Matcher matcher = PATH_PATTERN.matcher(value);
        while (matcher.find()) {
            String path = value.substring(matcher.start(), matcher.end());
            path = path.trim();
            if (path.length() == 0) {
                continue;
            }
            char first = path.charAt(0);
            char last = path.charAt(path.length() - 1);
            if (first == '"' && last == '"' && path.length() > 1) {
                path = path.substring(1, path.length() - 1);
                path = path.trim();
                if (path.length() == 0) {
                    continue;
                }
            } else if (path.contains("\"")) {
                // Unbalanced quotes
                // Too early to use standard i18n support. The class path hasn't
                // been configured.
                throw new IllegalArgumentException(
                        "The double quote [\"] character only be used to quote paths. It must " +
                        "not appear in a path. This loader path is not valid: [" + value + "]");
            } else {
                // Not quoted - NO-OP
            }
            result.add(path);
        }
        return result;
    }
    
}
