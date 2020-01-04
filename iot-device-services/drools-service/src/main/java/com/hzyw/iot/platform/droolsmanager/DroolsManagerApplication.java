package com.hzyw.iot.platform.droolsmanager;


import com.hzyw.iot.platform.droolsmanager.service.ReloadDroolsRulesService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * DroolsManagerApplication
 *
 * IOT Team
 */
@SpringBootApplication

public class DroolsManagerApplication {
   /* @Resource
    private ReloadDroolsRulesService reloadDroolsRulesService;*/
    public static void main(String[] args) {
        SpringApplication.run(DroolsManagerApplication.class, args);
    }
   /* @PostConstruct
    public void init() {
        reloadDroolsRulesService.reload();
    }*/

}
