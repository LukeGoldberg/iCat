package org.logan.core.lifecycle;

import java.util.concurrent.CopyOnWriteArrayList;

import org.logan.core.event.LifecycleEvent;
import org.logan.core.listener.LifecycleListener;

public abstract class BaseLifecycle implements Lifecycle {

	/*
	 * 同样觉得接口 Lifecycle 不需要存在。
	 */
	
	private final CopyOnWriteArrayList<LifecycleListener> lifecycleListeners = new CopyOnWriteArrayList<>();
	
	private volatile LifecycleState state = LifecycleState.NEW;
	
	@Override
	public void addLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLifecycleListener(LifecycleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		setStateInternal(LifecycleState.INITIALIZING, this);
		initInternal();
		setStateInternal(LifecycleState.INITIALIZED, this);
	}

	@Override
	public void start() {
		setStateInternal(LifecycleState.STARTING, this);
		startInternal();
		setStateInternal(LifecycleState.STARTED, this);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		stopInternal();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public LifecycleState getState() {
		
		return null;
	}
	
	@Override
	public void pause() {
		
	}

	protected abstract void initInternal();
	
	protected abstract void startInternal();
	
	protected abstract void stopInternal();
	
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
	
}
