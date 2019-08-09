package com.hzyw.iot.mqtt.sub;

/**
 * 统一订阅盒子设备消息
 * 1，获取   /公司编号/版本号/namespace1/devicepub 设备端发布的消息
 * 2,把消息字符串转化为MessageVO；
 * 3，根据type类型 转化为特定类型的VO ，如DevInfoDataVO
 * 4，把MessageVO 再此转化为JSON串 发送到KAFKA ，根据消息类型来发送到不同的KAFKA队列
 * 
 * kafka队列如下：
 *  iot_topic_dataAcess_request
	iot_topic_dataAcess_response
	
	iot_topic_dataAcess_devInfoResponse
	iot_topic_dataAcess_metricInfoResponse
	iot_topic_dataAcess_devSignlResponse

 */
public class GatewayMqttSub {

}
