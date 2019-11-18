package org.logan.core.container;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.collections.CollectionUtils;
import org.logan.core.Pipeline;
import org.logan.core.event.ContainerEvent;
import org.logan.core.listener.ContainerListener;
import org.logan.util.Const;

public class BaseContainer implements Container {
	
	/*
	 * 不需要有 Container 的接口，不要为了抽象而抽象，考虑将 Container 接口删除掉。
	 */
	
	private final CopyOnWriteArraySet<Container> children = new CopyOnWriteArraySet<>();
	
	private final CopyOnWriteArrayList<ContainerListener> listeners = new CopyOnWriteArrayList<>();
	
	private String name;
	
	private Container parent;
	
	private Pipeline pipeline;
	
	@Override
	public void addChild(Container child) {
		if (child == null) {
			return;
		}
		child.setParent(this);
		children.add(child);
		fireContainerEvent(new ContainerEvent(Const.ADD_CHILD_EVENT, child));
	}
	
	@Override
	public void addContainerListener(ContainerListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeContainerListener(ContainerListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void fireContainerEvent(final ContainerEvent event) {
		if (CollectionUtils.isEmpty(listeners)) {
			return;
		}
		listeners.forEach(listener -> listener.fireEvent(event));
	}
	
	@Override
	public String getName() {
		return name;
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
	public Set<Container> getChildren() {
		return children;
	}
	
	@Override
	public Pipeline getPipeline() {
		return pipeline;
	}

}
