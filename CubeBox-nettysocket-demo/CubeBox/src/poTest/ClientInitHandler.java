package poTest;

import org.apache.log4j.Logger;

import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.ChannelInboundHandlerAdapter;  
  
  

import com.cube.core.CubeRun;
  
public class ClientInitHandler extends ChannelInboundHandlerAdapter {  
	private static final Logger LOG = Logger.getLogger(ClientInitHandler.class); 
    @Override  
    public void channelActive(ChannelHandlerContext ctx) throws Exception {  
    	System.out.println("HelloClientIntHandler.channelActive");  
        Person person = new Person();  
        person.setName("guowl");  
        person.setSex("man");  
        person.setAge(30);  
        ctx.write(person);  
        ctx.flush();  
    }  
}  