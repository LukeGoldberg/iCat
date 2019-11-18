package org.logan.core.lifecycle;

import org.logan.util.Const;

public enum LifecycleState {
	
	NEW(false, null),
    INITIALIZING(false, Const.BEFORE_INIT_EVENT),
    INITIALIZED(false, Const.AFTER_INIT_EVENT),
    STARTING_PREP(false, Const.BEFORE_START_EVENT),
    STARTING(true, Const.START_EVENT),
    STARTED(true, Const.AFTER_START_EVENT),
    STOPPING_PREP(true, Const.BEFORE_STOP_EVENT),
    STOPPING(false, Const.STOP_EVENT),
    STOPPED(false, Const.AFTER_STOP_EVENT),
    DESTROYING(false, Const.BEFORE_DESTROY_EVENT),
    DESTROYED(false, Const.AFTER_DESTROY_EVENT),
    FAILED(false, null);

    private final boolean available;
    private final String lifecycleEvent;

    private LifecycleState(boolean available, String lifecycleEvent) {
        this.available = available;
        this.lifecycleEvent = lifecycleEvent;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getLifecycleEvent() {
        return lifecycleEvent;
    }
	
}

