package com.hzyw.iot.platform.devicemanager.kafka.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * KafkaTopicProperties
 *
 * @blame Android Team
 */
@Data
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicProperties {

    private String groupId;
    /**
     * 下行控制数据
     */
    private String topicOpt;
    /**
     * 上行状态/响应数据
     */
    private String[] topicMetrx;
    /**
     * 上行进网/登陆数据
     */
    private String[] topicAces;
}
