package com.hzyw.iot.netty.channelhandler;

import com.hzyw.iot.util.ByteUtils;
import com.hzyw.iot.util.constant.ProtocalAdapter;
import com.hzyw.iot.vo.dc.GlobalInfo;
import com.hzyw.iot.vo.dc.RTUCommandInfo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * 指令下发
 *
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-11-09 13:35
 */
@Sharable
public class CommandHandler extends MessageToByteEncoder<ByteBuf> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandHandler.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
    	System.out.println(">>>>>>>>CommandHandler::encode -------out.writeBytes(msg)--");
        out.writeBytes(msg);
    }
    
    public static void writeCommand(String sn, String command, int commandType) {
        if (CollectionUtils.isEmpty(GlobalInfo.SN_CHANNEL_INFO_MAP)) {
            LOGGER.warn("global snChannelInfo is empty.");
            return;
        }
        if (GlobalInfo.SN_CHANNEL_INFO_MAP.containsKey(sn)) {
            Channel channel = GlobalInfo.SN_CHANNEL_INFO_MAP.get(sn).getChannel();//获取要下发的sn设备对应的channel
            LOGGER.info("\n command: ({}) \n type: ({}) \n sn: ({})", command, commandType, sn);

            ByteBuf byteBuf = Unpooled.buffer();
            if (commandType == 1) {
                byteBuf.writeBytes(command.getBytes());
            } else if (commandType == 2) {
                try {
                    //byteBuf.writeBytes(ByteUtils.hex2byte(command));
                    com.hzyw.iot.util.constant.ConverUtil.hexStrToByteArr(command);
                } catch (Exception e) {
                    LOGGER.error("bad command to hex: {} ", command,e);
                    return;
                }
            }
            channel.writeAndFlush(byteBuf).addListener((ChannelFutureListener) future -> { //监听下发的请求执行是否成功！(执行结果，不是响应)
                if (future.isSuccess()) {
                    LOGGER.info("ok");
                } else {
                    LOGGER.error("send data to client exception occur: {}", future.cause());
                }
            });
        } else {
            LOGGER.warn("no channel in global channelInfo by this sn:{}", sn);
        }
    }
}
