package com.cube.utils.newStringUtils;

import java.util.List;

 

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * 解码器
 * 客户端和服务端均有使用
 * 0-1字节表示整个消息的长度（单位：字节）
 * 2-3字节代表消息类型，对应annotation
 * 余下的是消息的json字符串（UTF-8编码）
 * 
 * @author xingchencheng
 *
 */

public class MsgDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
		System.out.println("===========decode========readableBytes==="+buf.readableBytes());
		if (buf.readableBytes() < 2) {
			return;
		}
		short jsonBytesLength = (short) (buf.readShort() - 2);
		byte[] tmp = new byte[jsonBytesLength];
		buf.readBytes(tmp);
		String json = new String(tmp, "UTF-8");
		System.out.println("===========json==========="+json);
		list.add(json);
	}
}
