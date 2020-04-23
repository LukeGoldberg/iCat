package org.logan.core.container;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.logan.core.Pipeline;
import org.logan.core.event.LifecycleEvent;
import org.logan.core.lifecycle.LifecycleState;
import org.logan.core.listener.ContainerListener;
import org.logan.core.listener.LifecycleListener;
import org.logan.protocol.ResponseInfo;

import io.netty.handler.codec.http.HttpRequest;

public abstract class BaseContainer implements Container {
	
	protected final List<Container> children = new CopyOnWriteArrayList<>();
	
	private final List<ContainerListener> listeners = new CopyOnWriteArrayList<>();
	
	private String name;
	
	protected Container parent;
	
	private Pipeline pipeline = new Pipeline();
	
    private final CopyOnWriteArrayList<LifecycleListener> lifecycleListeners = new CopyOnWriteArrayList<>();
	
	private volatile LifecycleState state = LifecycleState.NEW;
	
	private ConcurrentHashMap<String, Container> uriChildMap = new ConcurrentHashMap<>();
	
	// ---------------------- Container ----------------------
	
	@Override
	public void addChild(Container child) {
		if (child == null) {
			return;
		}
		child.setParent(this);
		children.add(child);
		uriChildMap.put(child.getName(), child);
	}
	
	@Override
	public void parseRequest(String uri, HttpRequest request, ResponseInfo response) {
		String path = uri.contains("/") ? uri.substring(0, uri.indexOf("/")) : uri;
		Container child = uriChildMap.get(path);
		if (child != null) {
			child.getPipeline().invoke(request, response);
			child.parseRequest(uri.substring(uri.indexOf("/")+1), request, response);
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void setParent(Container parent) {
		this.parent = parent;
	}
	
	@Override
	public Container getParent() {
		return parent;
	}
	
	@Override
	public List<Container> getChildren() {
		return children;
	}
	
	@Override
	public Pipeline getPipeline() {
		return pipeline;
	}
	
	// ---------------------- Lifecycle ----------------------
	
	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		lifecycleListeners.add(listener);
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		
	}

	@Override
	public void init() {
		if (state != LifecycleState.NEW) {
			throw new IllegalArgumentException("invalidate state before init server");
		}
        setStateInternal(LifecycleState.INITIALIZING, null);
        initInternal();
        setStateInternal(LifecycleState.INITIALIZED, null);
	}

	@Override
	public void start() {
		if (state != LifecycleState.INITIALIZED) {
			throw new IllegalArgumentException("invalidate state before start server");
		}
		setStateInternal(LifecycleState.STARTING, this);
		startInternal();
		setStateInternal(LifecycleState.STARTED, this);
	}

	@Override
	public void stop() {
		setStateInternal(LifecycleState.STOPPING, this);
		stopInternal();
		setStateInternal(LifecycleState.STOPPED, this);
	}

	@Override
	public void destroy() {
		
	}
	
	@Override
	public LifecycleState getState() {
		return state;
	}
	
	// ---------------------- private method ----------------------
	
	private synchronized void setStateInternal(LifecycleState state, Object data) {
		this.state = state;
        if (state != null) {
            fireLifecycleEvent(state, data);
        }
	}
	
	private void fireLifecycleEvent(LifecycleState state, Object data) {
		LifecycleEvent event = new LifecycleEvent(state, data);
        for (LifecycleListener listener : lifecycleListeners) {
            listener.fireEvent(event);
        }
    }
	
	protected abstract void initInternal();
	
	protected abstract void startInternal();
	
	protected abstract void stopInternal();

}
