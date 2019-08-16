package com.cube.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import com.cube.event.CubeMsg;
import com.cube.event.EventEnum;
import com.cube.event.ByteObjConverter;
import com.cube.event.ByteBufToBytes;;

public class TransforDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    	//解码器
//        if (in.readableBytes() < 4) {
//             ctx.pipeline().close();
//            return;
//        }
//        CubeMsg event = new CubeMsg();
//        event.setCtx(ctx);
//        event.setType(EventEnum.valuesOf(in.readShort()));
//        // 读取长度
//        short length = in.readShort();
//        byte[] data = new byte[length];
//        in.readBytes(data);
//        event.setData(data);
//        out.add(event);
    	ByteBufToBytes read = new ByteBufToBytes();  
        Object obj = ByteObjConverter.ByteToObject(read.read(in));  
        out.add(obj);  
    }

}
