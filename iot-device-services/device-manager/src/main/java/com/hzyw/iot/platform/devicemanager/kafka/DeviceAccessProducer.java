package com.hzyw.iot.platform.devicemanager.kafka;

import com.hzyw.iot.platform.devicemanager.kafka.config.KafkaTopicProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * DeviceAccessProducer
 *
 * @blame Android Team
 */
@Service
@Slf4j
public class DeviceAccessProducer {

    private final KafkaTopicProperties topicProperties;

    private final KafkaTemplate<Integer, String> kafkaTemplate;

    public DeviceAccessProducer(KafkaTemplate kafkaTemplate, KafkaTopicProperties topicProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicProperties = topicProperties;
    }

    public void sendDeviceOptMsg(String json){
        sendMessage(topicProperties.getTopicOpt(),json);
    }

    public void sendMessage(String topic, String data) {
        log.info("kafka sendMessage start");
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(topic, data);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("kafka sendMessage error, ex = {}, topic = {}, data = {}", ex, topic, data);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                log.info("kafka sendMessage end");
            }
        });
        log.info("kafka sendMessage success topic = {}, data = {}", topic, data);
    }
}
