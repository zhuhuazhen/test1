package com.cube.logic;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
 

import com.cube.core.CubeRun;
import com.cube.event.CubeMsg;
import com.cube.event.EventEnum;
import com.cube.event.ReplyEvent;

/**
 * 工作对象
 * @description Worker
 * @author tengyz
 * @version 0.1
 * @date 2014年8月6日
 */
public class ProcessRunnable implements Runnable {
	private static final Logger LOG = Logger.getLogger(ProcessRunnable.class);

    private ConcurrentHashMap<String, ReplyEvent> replyMap = new ConcurrentHashMap<String, ReplyEvent>();
    // 任务队列
    private ConcurrentLinkedQueue<CubeMsg> workqueue = new ConcurrentLinkedQueue<CubeMsg>();
    private volatile boolean isRunning = false;

    public void putReply(ReplyEvent reply) {
           replyMap.put(reply.getId(), reply);
    }

    public ReplyEvent getReply(String id) {
        return replyMap.get(id);
    }

    public ReplyEvent removeReply(String id) {
        return replyMap.remove(id);
    }
    
    public ConcurrentHashMap<String, ReplyEvent> getReplyAll() {
        return replyMap;
    }

    /**
     * 向事件队列中添加事件
     */
    public boolean pushUpMsg(CubeMsg msg) {
        if (isRunning) {
            synchronized (workqueue) {
                boolean ret = workqueue.add(msg);
                if(ret){
                    workqueue.notifyAll();
                }
                return ret;
            }
        } else {
            return false;
        }
    }
    
    public ProcessRunnable() {
        isRunning = false;
    }
 
    public void run() {
        LOG.info("ProcessRunnable worker starting...");
        isRunning = true;
        while (true) {
            try {
                CubeMsg msg = null;
                synchronized (workqueue) {
                    msg = workqueue.poll();
                    if(msg == null){
                        workqueue.wait();
                        continue;
                    }
                }

                // 具体工作
                EventEnum event =msg.getType();
                if (event == null) {
                    LOG.info("event is null......continue");
                    continue;
                }

                Process process = ProcessManager.getInstance().getProcess(event);
                if (process == null) {
                    LOG.info("event is:{} without process"+event.getVal());
                    continue;
                }
                //LOG.info(process.toString()+"{}=======process the msg:{}"+ event.getVal());
                process.excute(msg);

            } catch (Exception e) {
                LOG.error("ProcessRunnable Worker exception", e);
            }

        }
    }

}
