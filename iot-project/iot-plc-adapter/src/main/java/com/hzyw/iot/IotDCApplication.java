package com.hzyw.iot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

 import com.hzyw.iot.netty.channelhandler.CommandHandler;
import com.hzyw.iot.utils.IotInfoConstant;

/**
 * @author TheEmbers Guo
 * @version 1.0
 * createTime 2018-10-19 13:12
 */
@SpringBootApplication(scanBasePackages = "com.hzyw.iot")
//@RestController
//@RequestMapping(value = "/testXF")  //测试下发
public class IotDCApplication {
    public static void main(String[] args) {
       /* new SpringApplicationBuilder()
               // .banner(new TheEmbersBanner())
               // .bannerMode(Banner.Mode.LOG)
                .sources(IotDCApplication.class)
                .run(args);*/
        
        SpringApplication application = new SpringApplication(IotDCApplication.class);
		//application.addInitializers(new ApplicationStartedListener());
		SpringApplication.run(IotDCApplication.class, args);
		
    }
    
    //@RequestMapping(value = "/xf")
	public Map<String, Object> getThisMap(String msg) {
    	System.out.println("---------准备下发指令---------");
    	String _16str = "68 00 00 00 00 01 00 68 00 02 70 03 46 16";
		CommandHandler.writeCommand("sn1", _16str, 2);  //2表示入参 是16进制字符串
		Map<String, Object> map = new HashMap<>();
		map.put("Name", "LYW");
		map.put("Sex", "大老爷们");
		map.put("Message", msg);
		return map;
	}
    
}
