package org.logan.core.lifecycle;

import java.util.concurrent.CopyOnWriteArrayList;

import org.logan.core.listener.LifecycleListener;

public class BaseLifecycle implements Lifecycle {

	/*
	 * ͬ�����ýӿ� Lifecycle ����Ҫ���ڡ�
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public LifecycleState getState() {
		
		return null;
	}

}
