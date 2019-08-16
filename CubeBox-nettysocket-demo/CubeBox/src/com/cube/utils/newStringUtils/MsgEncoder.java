package com.cube.utils.newStringUtils;

 

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器
 * 客户端和服务端均有使用
 * 0-1字节表示整个消息的长度（单位：字节）
 * 2-3字节代表消息类型，对应annotation
 * 余下的是消息的json字符串（UTF-8编码）
 * 
 * @author xingchencheng
 *
 */

public class MsgEncoder extends MessageToByteEncoder<Object>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf buf) throws Exception {
		System.out.println("===========decode========msg==="+msg);
		byte[] jsonBytes = msg.toString().getBytes("UTF-8");
		buf.writeShort(jsonBytes.length + 2);
		buf.writeBytes(jsonBytes);
	}
	
}
