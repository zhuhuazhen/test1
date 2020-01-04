package com.hzyw.iot.platform.devicemanager.kafka.config;

import com.hzyw.iot.platform.devicemanager.kafka.DeviceAccessConsumer;
import com.hzyw.iot.platform.devicemanager.kafka.DeviceMetrxConsumer;
import com.hzyw.iot.platform.devicemanager.kafka.KafkaMsgProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * KafkaTopicConfiguration
 *
 * @blame Android Team
 */
@Configuration
@EnableConfigurationProperties(KafkaTopicProperties.class)
@EnableKafka
public class KafkaTopicConfiguration {

    private final KafkaTopicProperties properties;

    public KafkaTopicConfiguration(KafkaTopicProperties properties) {
        this.properties = properties;
    }

    @Bean(name = "topicAces")
    public String[] topicAces() {
        return properties.getTopicAces();
    }

    @Bean(name = "topicMetrx")
    public String[] topicMetrx() {
        return properties.getTopicMetrx();
    }

    @Bean(name = "groupId")
    public String groupId() {
        return properties.getGroupId();
    }

    @Bean(name = "topicOpt")
    public String topicOpt() {
        return properties.getTopicOpt();
    }

    @Bean
    public KafkaMsgProcessor kafkaMsgProcessor(){
        return  new KafkaMsgProcessor();
    }

    @Bean
    public DeviceAccessConsumer deviceAccessConsumer(){
        return  new DeviceAccessConsumer();
    }

    @Bean
    public DeviceMetrxConsumer deviceMetrxConsumer(){
        return  new DeviceMetrxConsumer();
    }
}