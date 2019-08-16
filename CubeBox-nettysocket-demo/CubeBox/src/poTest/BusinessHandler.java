package poTest;

import org.apache.log4j.Logger;

import com.cube.core.CubeRun;
import com.cube.event.CubeMsg;

import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelInboundHandlerAdapter;  
  
 
public class BusinessHandler extends ChannelInboundHandlerAdapter {  
	private static final Logger LOG = Logger.getLogger(BusinessHandler.class);  
  
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {  
        //Person person = (Person) msg; 
    	CubeMsg person = (CubeMsg) msg;
        System.out.println("BusinessHandler read msg from client :" + person);  
    }  
  
    @Override  
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {  
        ctx.flush();  
    }  
      
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
          
    }  
}  