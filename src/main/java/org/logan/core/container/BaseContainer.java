package org.logan.core.container;

import java.util.List;
import java.util.Map.Entry;
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
	
	/*
	 * 不需要有 Container 的接口，不要为了抽象而抽象，考虑将 Container 接口删除掉。
	 */
	
	protected final List<Container> children = new CopyOnWriteArrayList<>();
	
	private final List<ContainerListener> listeners = new CopyOnWriteArrayList<>();
	
	private String name;
	
	private Container parent;
	
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
//		fireContainerEvent(new ContainerEvent(Const.ADD_CHILD_EVENT, child));
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
	
//	@Override
//	public void addContainerListener(ContainerListener listener) {
//		listeners.add(listener);
//	}
//	
//	@Override
//	public void removeContainerListener(ContainerListener listener) {
//		listeners.remove(listener);
//	}
//	
//	@Override
//	public void fireContainerEvent(final ContainerEvent event) {
//		if (CollectionUtils.isEmpty(listeners)) {
//			return;
//		}
//		listeners.forEach(listener -> listener.fireEvent(event));
//	}
	
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
		// TODO Auto-generated method stub
		lifecycleListeners.add(listener);
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
//		if (state != LifecycleState.NEW) {
//			throw new IllegalArgumentException("invalidate state before init server");
//		}
        setStateInternal(LifecycleState.INITIALIZING, null);
        initInternal();
        setStateInternal(LifecycleState.INITIALIZED, null);
	}

	@Override
	public void start() {
//		if (state != LifecycleState.INITIALIZED) {
//			throw new IllegalArgumentException("invalidate state before start server");
//		}
		setStateInternal(LifecycleState.STARTING, this);
		startInternal();
		setStateInternal(LifecycleState.STARTED, this);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
//		setStateInternal
		stopInternal();
//		setStateInternal
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public LifecycleState getState() {
		
		return null;
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
