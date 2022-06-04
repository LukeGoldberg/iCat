package org.logan.core.container;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.logan.core.listener.HostConfigListener;
import org.logan.core.valve.HostValve;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Host extends BaseContainer {
	
	private static final Logger logger = LoggerFactory.getLogger(Host.class);

	protected ExecutorService startStopExecutor = Executors.newCachedThreadPool();
	
	private volatile boolean init = false;
	
	@Override
	protected void initInternal() {
		if (init) {
			return;
		}
		logger.info("Host init starting...");
		addLifecycleListener(new HostConfigListener());
		getPipeline().addValve(new HostValve());
		init = true;
		for (Container context : getChildren()) {
			context.init();
		}
	}

	@Override
	protected void startInternal() {
		if (!init) {
			init();
		}
		// start defined context
        for (Container context : getChildren()) {
            startStopExecutor.submit(new Engine.StartChild(context));
        }
	}

	@Override
	public void pause() {
		
	}
	
	@Override
	protected void stopInternal() {
		
	}
	
}
