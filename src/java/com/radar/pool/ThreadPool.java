package com.radar.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPool
{
	
	private static ExecutorService executorService = Executors.newFixedThreadPool(512);
	private static final Logger log = LoggerFactory.getLogger(ThreadPool.class);
    private ThreadPool(){
    	throw new RuntimeException("不允许实例化ThreadPool");
    }
    
    public static void addWork(final QueueTask task){
    	
        executorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					task.executeTask();
				} catch (Exception e) {
					e.printStackTrace();
					log.error("任务执行异常："+e.getMessage());
				}
			}
		});
    }
    
    public static void shutdown(){
    	if(executorService!=null){
        	executorService.shutdown();
    	}
    }
}