package test;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
 
// 该handler是InboundHandler类型
public class HelloServerInHandler extends ChannelInboundHandlerAdapter {
    @Override
    public boolean isSharable() {
        System.out.println("==============handler-sharable==============");
        return super.isSharable();
    }
 
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============channel-register==============");
    }
 
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============channel-unregister==============");
    }
 
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============channel-active==============");
 
    }
 
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============channel-inactive==============");
    }
 
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
 
        System.out.println("==============channel-read==============");
        System.out.println("the msg type is " + msg.getClass().getName());
        Integer integer = (Integer) msg;
        System.out.println("服务器端接收到的客户端的数字是" + integer);
 
 
        System.out.println("服务器向客户端写入整型数字2000");
        ctx.writeAndFlush(2000);
        ctx.close();
    }
 
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============channel-read-complete==============");
        ctx.flush();
    }
}