package com.hzyw.iot.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.hzyw.iot.config.NettyConfig;
import com.hzyw.iot.netty.RTUPortListener;
import com.hzyw.iot.redis.IoTService;
import io.netty.channel.nio.NioEventLoopGroup;

 
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
    @Autowired
    private NettyConfig nettyConfig;
    
    @Autowired
    private IoTService ioTService;

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Netty Start..");
        Integer port = nettyConfig.getPort();
        if (port == null) {
            LOGGER.warn("port is empty.");
            return;
        }
        // 获取已配置的设备信息
        //ioTService.loadIotMapper2Global(InetAddress.getLocalHost().getHostAddress(), port);
        // 端口监听
        singleThreadExecutor.submit(
        	new Runnable() {
				@Override
				public void run() {
					//PLC
					new RTUPortListener(12345, new NioEventLoopGroup(), new NioEventLoopGroup());
				}
			}
        );
        /*singleThreadExecutor.submit( 
            new Runnable() {
				@Override
				public void run() {
					//探测器
		            new RTUPortListener(5678, new NioEventLoopGroup(), new NioEventLoopGroup());
				}
			} 
         );*/
    }
}