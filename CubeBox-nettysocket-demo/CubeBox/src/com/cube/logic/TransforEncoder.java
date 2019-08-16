package com.cube.logic;

import java.io.UnsupportedEncodingException;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.cube.event.ByteObjConverter;
import com.cube.event.CubeMsg;

public class TransforEncoder extends MessageToByteEncoder<CubeMsg> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CubeMsg msg, ByteBuf out) throws Exception {
    //编码器
    	byte[] datas = ByteObjConverter.ObjectToByte(msg);  
        out.writeBytes(datas);  
        ctx.flush();  
    }

}
