package org.logan.core.container;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.logan.core.servlet.DefaultServletConfig;
import org.logan.core.servlet.StandardWrapperFacade;
import org.logan.protocol.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.codec.http.HttpRequest;

public class Wrapper extends BaseContainer implements ServletConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(Wrapper.class);

	/**
     * The (single) possibly uninitialized instance of this servlet.
     */
    protected volatile Servlet instance = null;
    
    protected String servletName;
    
    protected String servletContent;
    
    /**
     * The initialization parameters for this servlet, keyed by
     * parameter name.
     */
    protected HashMap<String, String> parameters = new HashMap<>();
    
    private final ReentrantReadWriteLock parametersLock =
            new ReentrantReadWriteLock();
	
	private Stack<Servlet> instancePool = new Stack<>();
	
	/**
     * Flag that indicates if this instance has been initialized
     */
    protected volatile boolean instanceInitialized = false;
	
    /**
     * Does this servlet implement the SingleThreadModel interface?
     */
    protected volatile boolean singleThreadModel = false;
    
    /**
     * Are we unloading our servlet instance at the moment?
     */
    protected volatile boolean unloading = false;
    
    /**
     * The count of allocations that are currently active (even if they
     * are for the same instance, as will be true on a non-STM servlet).
     */
    protected final AtomicInteger countAllocated = new AtomicInteger(0);
    
    /**
     * Number of instances currently loaded for a STM servlet.
     */
    protected int nInstances = 0;
    
    /**
     * Maximum number of STM instances.
     */
    protected int maxInstances = 20;
    
	public void load() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException {
		logger.info("loading Servlets...");
		instance = loadServlet();
		
	}
	
	public synchronized Servlet loadServlet() throws ServletException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
		if (!singleThreadModel && (instance != null))
            return instance;
		Servlet servlet = (Servlet) this.getClass().getClassLoader()
				.loadClass(servletName)
				.getConstructor()
				.newInstance();
		initServlet(servlet);
		return servlet;
	}
	
	private synchronized void initServlet(Servlet servlet)
            throws ServletException {
		DefaultServletConfig defaultServletConfig = new DefaultServletConfig();
	    final StandardWrapperFacade facade = new StandardWrapperFacade(defaultServletConfig);
		servlet.init(facade);
		instanceInitialized = true;
	}
	
	public Servlet allocate() throws ServletException {
        if (unloading) {
            throw new ServletException("standardWrapper.unloading");
        }
        boolean newInstance = false;
        if (!singleThreadModel) {
            if (instance == null || !instanceInitialized) {
                synchronized (this) {
                    if (instance == null) {
                        try {
                            instance = loadServlet();
                            newInstance = true;
                            if (!singleThreadModel) {
                                countAllocated.incrementAndGet();
                            }
                        } catch (ServletException e) {
                            throw e;
                        } catch (Throwable e) {
                            throw new ServletException("standardWrapper.allocate");
                        }
                    }
                    if (!instanceInitialized) {
                        initServlet(instance);
                    }
                }
            }
            if (singleThreadModel) {
                if (newInstance) {
                    synchronized (instancePool) {
                        instancePool.push(instance);
                        nInstances++;
                    }
                }
            } else {
                if (!newInstance) {
                    countAllocated.incrementAndGet();
                }
                return instance;
            }
        }
        synchronized (instancePool) {
            while (countAllocated.get() >= nInstances) {
                if (nInstances < maxInstances) {
                    try {
                        instancePool.push(loadServlet());
                        nInstances++;
                    } catch (ServletException e) {
                        throw e;
                    } catch (Throwable e) {
                        throw new ServletException("standardWrapper.allocate");
                    }
                } else {
                    try {
                        instancePool.wait();
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                }
            }
            countAllocated.incrementAndGet();
            return instancePool.pop();
        }
	}
	
	@Override
	public void initInternal() {
		logger.info("Wrapper init starting...");
	}
	
	@Override
	public void startInternal() {
		logger.info("Wrapper starting...");
	}
	
	@Override
	public void stopInternal() {
        try {
            unload();
        } catch (ServletException e) {
            logger.info("standardWrapper.unloadException" + e);
        }
	}

	@Override
	public void pause() {
		
	}
	
	@Override
	public void parseRequest(String uri, HttpRequest request, ResponseInfo response) {
		response.content = servletContent;
	}
	
    public synchronized void unload() throws ServletException {

//        // Nothing to do if we have never loaded the instance
//        if (!singleThreadModel && (instance == null))
//            return;
//        unloading = true;
//
//        if (instanceInitialized) instance.destroy();
//
//
//        // Deregister the destroyed instance
//        instance = null;
//        instanceInitialized = false;
//
//
//        if (singleThreadModel && (instancePool != null)) {
//            try {
//                while (!instancePool.isEmpty()) {
//                    Servlet s = instancePool.pop();
//                    if (Globals.IS_SECURITY_ENABLED) {
//                        try {
//                            SecurityUtil.doAsPrivilege("destroy", s);
//                        } finally {
//                            SecurityUtil.remove(s);
//                        }
//                    } else {
//                        s.destroy();
//                    }
//                    // Annotation processing
//                    if (!((Context) getParent()).getIgnoreAnnotations()) {
//                       ((StandardContext)getParent()).getInstanceManager().destroyInstance(s);
//                    }
//                }
//            } catch (Throwable t) {
//                t = ExceptionUtils.unwrapInvocationTargetException(t);
//                ExceptionUtils.handleThrowable(t);
//                instancePool = null;
//                nInstances = 0;
//                unloading = false;
//                fireContainerEvent("unload", this);
//                throw new ServletException
//                    (sm.getString("standardWrapper.destroyException",
//                                  getName()), t);
//            }
//            instancePool = null;
//            nInstances = 0;
//        }
//
//        singleThreadModel = false;
//
//        unloading = false;
//        fireContainerEvent("unload", this);

    }
    
    public String getServletContent() {
		return servletContent;
	}

	public void setServletContent(String servletContent) {
		this.servletContent = servletContent;
	}
	
	/**
     * Add a new servlet initialization parameter for this servlet.
     *
     * @param name Name of this initialization parameter to add
     * @param value Value of this initialization parameter to add
     */
    public void addInitParameter(String name, String value) {
        parametersLock.writeLock().lock();
        try {
            parameters.put(name, value);
        } finally {
            parametersLock.writeLock().unlock();
        }
    }

	@Override
	public String getInitParameter(String name) {
		parametersLock.readLock().lock();
        try {
            return parameters.get(name);
        } finally {
            parametersLock.readLock().unlock();
        }
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		parametersLock.readLock().lock();
        try {
            return Collections.enumeration(parameters.keySet());
        } finally {
            parametersLock.readLock().unlock();
        }
    }

	@Override
	public ServletContext getServletContext() {
		if (parent == null)
            return null;
        else if (!(parent instanceof Context))
            return null;
        else
            return ((Context) parent).getServletContext();
	}

	@Override
	public String getServletName() {
		return servletName;
	}
	
}
