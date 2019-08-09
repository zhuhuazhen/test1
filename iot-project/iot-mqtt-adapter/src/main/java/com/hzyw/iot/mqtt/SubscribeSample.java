package com.hzyw.iot.mqtt;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;

import com.hzyw.iot.mqtt.callback.MqttCallbackImpl;
import com.hzyw.iot.service.RedisService;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;



/**
 *订阅端
 */
public class SubscribeSample {
	// 缓存测试
	//@Autowired
	//private RedisService redisService;
    public static void main(String[] args) throws MqttException {   
    	
    	//1,获取所有纳管状态下的主题，以及HOST TOPIC clientid userName passWord 协议类型=mqtt,namespace
    	//2,循环处理以下逻辑，建立长连接
        String HOST =  "tcp://47.106.189.255:1883";//"tcp://iot.eclipse.org:1883";
        String TOPIC = "hzyw/1.0/shenzhen/devicepub";// 公司编号/版本号/区域/devicepub
        int qos = 1;
        String clientid = "hello1";
        String userName = "test";
        String passWord = "test";
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            MqttClient client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            // 遗愿消息
            options.setWill("hzyw/1.0/shenzhen/iotpub/msg", "sub down!~".getBytes(), 1, false);
            // 设置回调函数
            client.setCallback(new MqttCallbackImpl());
            // 连接
            client.connect(options);
            // 订阅消息	
            client.subscribe(TOPIC, qos);
            
            
            //平台上线消息
            Map<String,Object> type = new HashMap<String,Object>();
    		Map<String,Object> dataKeyValue = new HashMap<String,Object>();
    		dataKeyValue.put("serviceid", "adapter01");
    		dataKeyValue.put("status", "online");
    		type.put("type", "serviceOnOff");
    		type.put("data", dataKeyValue);
    		JSONObject json = JSONUtil.parseObj(type);
         	MqttMessage message = new MqttMessage(json.toString().getBytes());
            client.publish("hzyw/1.0/shenzhen/iotpub/msg", message);
            
            
            /*//(测试) 断开连接
            client.disconnect();
            // 关闭客户端
            //client.close();
            // 重连
            client.reconnect();
            // 断开后重新订阅主题
            client.subscribe(TOPIC, qos);
            System.out.println(client);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
