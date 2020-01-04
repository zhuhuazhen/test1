package com.hzyw.iot.platform.devicemanager.kafka;

import com.hzyw.iot.platform.models.transfer.DefaultDeviceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/22.
 */

@Slf4j
public class DeviceAccessConsumer {

    @Autowired
    private KafkaMsgProcessor kafkaMsgProcessor;

    @KafkaListener(topics = "#{topicAces}", groupId = "#{groupId}")
    public void processMessage(ConsumerRecord<?, String> record) {
        String topic = record.topic();
        String msg = record.value();
        log.info("@@@START to process kafka message, topic = {}, msg = {}", topic, msg);
        try {
            kafkaMsgProcessor.processAccess(msg);
        } catch (DefaultDeviceException | Exception e) {
            log.warn("@@@ProcessError, topic = {}, error = {}", topic, e.getMessage());
            e.printStackTrace();
        } finally {
            //TODO: record audit
            log.info("@@@END of process kafka message!");
        }
    }
}
