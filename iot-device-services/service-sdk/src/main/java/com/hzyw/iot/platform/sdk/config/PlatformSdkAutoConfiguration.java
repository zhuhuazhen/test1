package com.hzyw.iot.platform.sdk.config;

import com.hzyw.iot.platform.sdk.api.*;
import com.hzyw.iot.platform.sdk.rest.DeviceMgrOptCallBackController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/19.
 */
@Configuration
@EnableConfigurationProperties(PlatformSdkProperties.class)
public class PlatformSdkAutoConfiguration {

    private final  PlatformSdkProperties platformSdkProperties;

    public PlatformSdkAutoConfiguration(PlatformSdkProperties properties) {
        this.platformSdkProperties = properties;
        if(properties.isPreferUrl()){
            //选择ribbon或者consul;
        }
//        initialHandlers();
    }
    @Bean
    @ConditionalOnMissingBean(DeviceMgrOptCallBackController.class)
    public DeviceMgrOptCallBackController deviceMgrOptCallBackController(){
        return new DeviceMgrOptCallBackController();
    }
//    @Bean
//    @ConfigurationProperties(prefix = "sdk")
//    public SdkConnectServerProperties getPlatformSdkProperties() {
//        return new SdkConnectServerProperties();
//    }

    @Bean
    public TestHelloApi deviceHelloApi() {
        return new TestHelloApi();
    }

    @Bean
    public DeviceInfoApi deviceInfoApi() {
        return new DeviceInfoApi();
    }

    @Bean
    public DeviceOperationApi deviceOperationApi() {
        return new DeviceOperationApi();
    }

    @Bean
    public DeviceRelationApi deviceRelationApi() {
        return new DeviceRelationApi();
    }

    @Bean
    public DeviceStatusApi deviceStatusApi() {
        return new DeviceStatusApi();
    }
}
