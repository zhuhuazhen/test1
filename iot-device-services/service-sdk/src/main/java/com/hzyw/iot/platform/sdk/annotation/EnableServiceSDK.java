package com.hzyw.iot.platform.sdk.annotation;

import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.annotation.*;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/8/22.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@EnableFeignClients(basePackages = {"com.hzyw.iot.platform.sdk"})
public @interface EnableServiceSDK {
}
