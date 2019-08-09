package com.hzyw.iot.kafka.consumer;

import java.util.Properties;

/**
 * 1，从KAFKA获取下发数据（实时获取）
 * 2, 判断服务是否在线
 * 3，判断设备是否已上线
 * 4，发送到MQTT服务器，关闭mqtt连接
 * 5，如果存在异常，检查看KAFKA是否已经提交offset
 * 
 */
public class RequestConsumer {
	 //参考 KafkaControllerTest  怎么做消费的
}
