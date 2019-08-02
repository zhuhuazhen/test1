package com.iot.mqttTest;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

import com.iot.mqtt.PushCallBack;
import com.iot.util.Format;


public class MqttCallbackImpl implements MqttCallback{
	private static final Logger LOG = LogManager.getLogger(PushCallBack.class);
	 public void connectionLost(Throwable cause) {
         System.out.println("connectionLost");
         
    /*  // 连接丢失后，一般在这里面进行重连
 		LOG.info("[MQTT] 连接断开，30S之后尝试重连...");
 		while (true) {
 			try {
 				Thread.sleep(30000);
 				//mqttConn.reConnect();
 				break;
 			} catch (Exception e) {
 				e.printStackTrace();
 				continue;
 			}
 		}*/
     }

     public void messageArrived(String topic, MqttMessage message) {
         System.out.println("topic:"+topic);//订阅的主题(厂商/设备唯一标识)
         System.out.println("Qos:"+message.getQos());
         System.out.println("message content:"+new String(message.getPayload()));//接收的byte数据
         
         
         /*Format format = new Format();
         format.configure(topic,new String(message.getPayload()))*/;
     }

     public void deliveryComplete(IMqttDeliveryToken token) {
         System.out.println("deliveryComplete---------"+ token.isComplete());
     }

 }	
	
