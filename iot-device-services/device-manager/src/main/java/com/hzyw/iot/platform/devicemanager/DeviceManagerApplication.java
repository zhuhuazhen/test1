package com.hzyw.iot.platform.devicemanager;

import com.hzyw.iot.platform.droolsmanager.service.ReloadDroolsRulesService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * DeviceManagerApplication
 *
 * @blame IOT Team
 */
/*@RibbonClient(name = "springcloud-provider-config", configuration = RibbonConfiguration.class)
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, value = ExtendRibbon.class)})
@EnableHystrix*/

@SpringBootApplication
//@EnableEurekaClient
//@MapperScan(basePackages = "com.hzyw.iot.platform.devicemanager.dao",sqlSessionFactoryRef = "sqlSessionFactory")
@MapperScan("com.hzyw.iot.platform.devicemanager.mapper")
@ComponentScan(basePackages = {"com.hzyw.iot.platform"})
public class DeviceManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeviceManagerApplication.class, args);
    }

    @Resource
    private ReloadDroolsRulesService reloadDroolsRulesService;

    @PostConstruct
    public void init() {
        reloadDroolsRulesService.reload();
    }
}
