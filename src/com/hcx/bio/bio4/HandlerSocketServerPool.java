package com.hcx.bio.bio4;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hongcaixia
 * @version 1.0
 * @date 2021/1/14 15:11
 */
public class HandlerSocketServerPool {

    //创建一个线程池成员变量用于存储线程池对象
    private ExecutorService executorService;

    //创建对象时初始化线程池对象
    public HandlerSocketServerPool(int maxThreadNum,int queueSize){
        executorService = new ThreadPoolExecutor(3, maxThreadNum, 120, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize));
    }

    //提交任务给线程池的任务队列，等待线程池处理
    public void execute(Runnable target){
        executorService.execute(target);
    }
}
