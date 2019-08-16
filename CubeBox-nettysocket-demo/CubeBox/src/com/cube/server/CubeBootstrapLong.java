package com.cube.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.annotation.Resource;

 
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.cube.logic.ProcessRunnable;

@Component
// @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CubeBootstrapLong implements Runnable {

    private static final Logger LOG = Logger.getLogger(CubeBootstrapLong.class);
    private volatile boolean run = false;

    /**
     * 逻辑处理线程
     */
    public static ProcessRunnable processRunnable = new ProcessRunnable();

    @Autowired
    private ServerBootstrap serverBootstrap;
    @Resource(name = "CubeChannelInitLong")
    private ChannelInitializer<SocketChannel> cubeChannelInitLong;
    @Resource(name = "boss")
    private EventLoopGroup boss;
    @Resource(name = "cubeTcpWorker")
    private EventLoopGroup worker;

    /**
     * 本地tcp服务端口
     */
    @Value("${serviceLong.port}")
    private String servicePort;

//    @Autowired
//    private DbManager dbManager;
    
    public void run() {
        LOG.info("设置serverbootstrap");
        // 设置工作线程池
        serverBootstrap.group(boss, worker);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        serverBootstrap.childHandler(cubeChannelInitLong);
        serverBootstrap.childOption(ChannelOption.WRITE_BUFFER_HIGH_WATER_MARK, 32 * 1024);
        serverBootstrap.childOption(ChannelOption.WRITE_BUFFER_LOW_WATER_MARK, 8 * 1024);
        // 设置为pooled的allocator,
        // netty4.0这个版本默认是unpooled,必须设置参数-Dio.netty.allocator.type pooled,
        // 或直接指定pooled
        serverBootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 直接发包
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        Channel ch;
        try {

            // 启动工作线程
            Thread processThread = new Thread(processRunnable);
            LOG.info("处理器设置为daemon");
            //setDaemon方法把java的线程设置为守护线程，此方法的调用必须在线程启动之前执行。只有在当前jvm中所有的线程都为守护线程时，jvm才会退出。
            processThread.setDaemon(true);
            LOG.info("启动处理器进程");
            processThread.start();

            ChannelFuture bindf = serverBootstrap.bind(Integer.valueOf(servicePort));
            ChannelFuture bsync = bindf.sync();

            ch = bsync.channel();
            LOG.info((new StringBuilder()).append("listen port=:").append(servicePort).toString());
            run = true;

            LOG.info("等待TCP结束...");
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.info("interrupted", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public boolean isRun() {
        return run;
    }

    /*
     * Runtime.getRuntime().addShutdownHook(new Thread("System Shutdown Hooker")
     * {
     * @Override public void run() { LOG.info("系统关闭中...");
     * boss.shutdownGracefully(); worker.shutdownGracefully(); } });
     */
}
