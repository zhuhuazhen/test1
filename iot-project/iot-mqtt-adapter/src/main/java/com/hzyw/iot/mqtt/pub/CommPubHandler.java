package com.hzyw.iot.mqtt.pub;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 发布端
 */
public class CommPubHandler {
	private MqttClient sampleClient;// 创建客户端
	//public MqttConnectOptions options;// 创建链接参数
	public static int qos = 1;//通道
	public static String url = "tcp://47.106.189.255:1883";//地址
	public static String userName = "test";//用户名
	public static String password = "test";//密码
	public static String clientId = "pubClient";//发布ID
	
	//内存存储
	public CommPubHandler () throws MqttException {
		sampleClient = new MqttClient(url, clientId, new MemoryPersistence());
	}
	
	public void Publish(String topic, String content) {
		try {
			// 创建链接参数
            MqttConnectOptions options = new MqttConnectOptions();
			// 在重新启动和重新连接时记住状态
			options.setCleanSession(false);
			// 设置连接的用户名
			options.setUserName(userName);
			options.setPassword(password.toCharArray());
			// 遗愿消息
			//options.setWill("pub", "pub down!~".getBytes(), qos, true);
			// 建立连接
			sampleClient.connect(options);
			// 创建消息
			MqttMessage message = new MqttMessage(content.getBytes());
			// 设置消息的服务质量
			message.setQos(qos);
			// 发布消息
			sampleClient.publish(topic, message);
			// 断开连接
			sampleClient.disconnect();
			// 关闭客户端
			sampleClient.close();
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}

	}

}
