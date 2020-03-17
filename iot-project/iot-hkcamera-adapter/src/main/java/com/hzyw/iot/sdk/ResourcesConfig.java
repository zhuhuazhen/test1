package com.hzyw.iot.sdk;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ResourcesConfig {
    public static String[] deviceIp;
    public static String[] user;
    public static String[] password;
    public static String[] port;
    public static String postUrl;
    public static String picPath;
    static {
		InputStream in = null;
		Properties pro = new Properties();
		try {
			in = ResourcesConfig.class.getClassLoader().getResourceAsStream("ResourcesConfig.properties");
			pro.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String _host = pro.get("device.ip").toString();
 		String _port = pro.get("device.port").toString();
		String _user = pro.get("device.user").toString();
		String _password = pro.get("device.passwd").toString();
		String _portUrl = pro.get("device.postUrl").toString();
		String _picPath = pro.get("pic.path").toString();
		System.out.println(">>>>host = " + _host);
		System.out.println(">>>>_user = " + _user);
		System.out.println(">>>>password = " + _password);
		System.out.println(">>>>port = " + _port);
		System.out.println(">>>>_portUrl = " + _portUrl);
		System.out.println(">>>>_picPath = " + _picPath);
		
		//...............
		deviceIp = _host.split(",");
		user = _user.split(",");
		password = _password.split(",");
		port = _port.split(",");
		postUrl = _portUrl;
		picPath = _picPath;
	}
     
    public static void getConfig(){
    	
    }
      
}