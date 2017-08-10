package com.radar.pool;
/**
 * 队列任务接口
 * @ClassName:  QueueTask   
 *    
 * @author: sunshine  
 * @date:   2015年3月20日 下午3:02:26
 */
public interface QueueTask
{
    /**
     * 队列执行任务
     */
    public void executeTask() throws Exception;
}