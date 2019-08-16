package com.cube.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
 

import com.cube.core.CubeRun;

public class NetUtils {

	private static final Logger LOG = Logger.getLogger(CubeRun.class);

    public static void sendPass(ChannelHandlerContext ctx) {
        if (ctx.channel().isWritable()) {
            ByteBuf buf = ByteBufUtils.passSecure();
            ctx.pipeline().writeAndFlush(buf);
        } else {
            ctx.pipeline().close();
        }

    }

    public static void sendUnpass(ChannelHandlerContext ctx) {
        if (ctx.channel().isWritable()) {
            ByteBuf buf = ByteBufUtils.unpassSecure();
            ctx.pipeline().writeAndFlush(buf).addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.pipeline().close();
        }
    }

    public static void sendHeartBeat(ChannelHandlerContext ctx) {
        if (ctx.channel().isWritable()) {
        	LOG.debug("sendHeart发送心跳");
            ctx.pipeline().writeAndFlush(ByteBufUtils.headBeat());
        } else {
            ctx.pipeline().close();
            LOG.debug("不sendHeart发送心跳");
        }
    }

    /**
     * 验证mac地址是否正确
     */
    public static boolean validateMac(String mac) {
        if (StringUtils.isBlank(mac)) {
            return false;
        }
        if (mac.length() != 17) {
            return false;
        }
        return mac.matches("([a-zA-z0-9][a-zA-z0-9]:){5}[a-zA-z0-9][a-zA-z0-9]");
    }
}
