package org.logan.core.container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.logan.core.listener.HostConfigListener;
import org.logan.core.valve.HostValve;

public class Host extends BaseContainer {
	
	private static final Logger logger = Logger.getLogger("Host");

	private String workDir;
	
	private String appBase;
	
	protected ExecutorService startStopExecutor = Executors.newCachedThreadPool();
	
	private volatile boolean init = false;
	
	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}
	
	public void setAppBase(String appBase) {
		this.appBase = appBase;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initInternal() {
		logger.info("Host init starting...");
		addLifecycleListener(new HostConfigListener());
		getPipeline().addValve(new HostValve());
		logger.info("Host add HostConfigListener");
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
		List<Future<Void>> results = new ArrayList<>();
        for (Container context : getChildren()) {
            results.add(startStopExecutor.submit(new Engine.StartChild(context)));
        }
        
        StringBuilder sb = new StringBuilder();
        for (Future<Void> result : results) {
            try {
                result.get();
            } catch (Throwable e) {
                logger.severe("containerBase.threadedStartFailed" + e);
                sb.append(e.getMessage() + "\r\n");
            }
        }
		
	}

	@Override
	protected void stopInternal() {
		// TODO Auto-generated method stub
		
	}
	
}
