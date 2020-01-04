package com.hzyw.iot.platform.devicemanager.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/21.
 */
@Slf4j
public class DeviceMetrxConsumer {

    @Autowired
    private KafkaMsgProcessor kafkaMsgProcessor;

    @KafkaListener(topics = "#{topicMetrx}", groupId = "#{groupId}")
    public void processMessage(ConsumerRecord<?, String> record) {
        String topic = record.topic();
        String msg = record.value();
        log.info("@@@START of process kafka message, topic = {}, msg = {}", topic, msg);
        try {
            kafkaMsgProcessor.processMetrx(msg);
        } catch (Exception e) {
            log.warn("@@@ProcessError, topic = {}, error = {}, msg={}", topic, e.getMessage(), msg);
            e.printStackTrace();
        } finally {
            //TODO: record audit
            log.info("@@@END of process kafka message!");
        }
    }
}
