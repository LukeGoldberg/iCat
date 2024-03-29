package org.logan.core.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.logan.core.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Engine extends BaseContainer {
	
	private static final Logger logger = LoggerFactory.getLogger(Engine.class);

	private Service service;
	
	protected ExecutorService startStopExecutor = Executors.newCachedThreadPool();

	@Override
	protected void initInternal() {
		logger.info("Engine init starting...");
		for (Container host : getChildren()) {			
			host.init();
		}
	}
	
	@Override
	protected void startInternal() {
		logger.info("Engine starting...");
        for (Container host : getChildren()) {
            startStopExecutor.submit(new StartChild(host));
        }
	}
	
	@Override
	public void stopInternal() {
        List<Future<Void>> results = new ArrayList<>();
        for (Container child : getChildren()) {
            results.add(startStopExecutor.submit(new StopChild(child)));
        }
        boolean fail = false;
        for (Future<Void> result : results) {
            try {
                result.get();
            } catch (Exception e) {
                logger.error("containerBase.threadedStopFailed" + e);
                fail = true;
            }
        }
        if (fail) {
            throw new IllegalArgumentException("containerBase.threadedStopFailed");
        }
	}
	
	public static class StartChild implements Runnable {
        private Container child;
        public StartChild(Container child) {
            this.child = child;
        }
        @Override
        public void run() {
            child.start();
        }
    }
	
	public static class StopChild implements Callable<Void> {
        private Container child;
        public StopChild(Container child) {
            this.child = child;
        }
        @Override
        public Void call() {
            if (child.getState().isAvailable()) {
                child.stop();
            }
            return null;
        }
    }

	@Override
	public void pause() {
		
	}
	
	public void setService(Service service) {
		this.service = service;
	}
	
}
