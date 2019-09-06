package com.hzyw.iot.clienttest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.hzyw.iot.netty.channelhandler.CommandHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * 测试，模拟设备上报消息
 *
 */

public class IotPlcAdapterAPPClientTest {
	static IotPlcAdapterAPPClientTestHandler client = new IotPlcAdapterAPPClientTestHandler();//读写
	
	public static void main(String[] args) {
		String _16str = "68 00 00 00 00 00 01 68 00 02 70 03 46 16";
		CommandHandler.writeCommand("sn1", _16str, 2);  //2表示入参 是16进制字符串
		    String host = "127.0.0.1"; 
		    int port = 9090; 
		    EventLoopGroup workerGroup = new NioEventLoopGroup(); 
		    try { 
		      Bootstrap b = new Bootstrap(); 
		      b.group(workerGroup); 
		      b.channel(NioSocketChannel.class); 
		      b.option(ChannelOption.SO_KEEPALIVE, true); 
		      b.handler(new ChannelInitializer<SocketChannel>() { 
		        @Override
		        public void initChannel(SocketChannel ch) throws Exception { 
		          ch.pipeline().addLast(client); 
		        } 
		      }); 
		      ChannelFuture f = b.connect(host, port).sync(); 
		      f.channel().closeFuture().sync(); 
		    } catch (InterruptedException e) { 
		      e.printStackTrace(); 
		    } finally { 
		      workerGroup.shutdownGracefully(); 
		    } 
		
	}
	 

}
