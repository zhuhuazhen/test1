package com.hzyw.iot.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
//import com.hzyw.iot.config.NettyConfig;

 
/**
 * 
 * 接口监听服务
 * 
 * @author Administrator
 *
 */
@Component
public class ListenerService implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListenerService.class);
    private static ExecutorService singleThreadExecutor = Executors.newFixedThreadPool(2);
    
    //@Autowired
    //private NettyConfig nettyConfig;
    
    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Camera Service Start..");
        new ShutdownSampleHook(Thread.currentThread());
        // 获取已配置的设备信息
        //ioTService.loadIotMapper2Global(InetAddress.getLocalHost().getHostAddress(), port);
        //端口监听
        /*singleThreadExecutor.submit(
        	new Runnable() {
				@Override
				public void run() {
					//PLC
					new RTUPortListener(12345, new NioEventLoopGroup(), new NioEventLoopGroup());
				}
			}
        );*/
        
         
    }
}
class ShutdownSampleHook extends Thread {
    private Thread mainThread;
    @Override
    public void run() {
        System.out.println("监听到服务停止信号.....");
        mainThread.interrupt();//给主线程发送一个中断信号
        try {
        	System.out.println("等待执行完毕.....");
        	Thread.currentThread().sleep(1000*5);
            mainThread.join(); //等待 mainThread 正常运行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主程序将正常退出.....");
    }

    public ShutdownSampleHook(Thread mainThread) {
        this.mainThread=mainThread;
        Runtime.getRuntime().addShutdownHook(this);
    }
}