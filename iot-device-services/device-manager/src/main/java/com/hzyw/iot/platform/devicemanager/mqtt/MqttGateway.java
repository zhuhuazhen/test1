package com.hzyw.iot.platform.devicemanager.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * MqttGateway
 *
 * @blame Android Team
 */
@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface MqttGateway {
    void sendToMqtt(String data);
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String data);
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String data);
}