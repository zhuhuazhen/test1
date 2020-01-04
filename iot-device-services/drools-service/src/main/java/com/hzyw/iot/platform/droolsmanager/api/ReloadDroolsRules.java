package com.hzyw.iot.platform.droolsmanager.api;

import com.hzyw.iot.platform.droolsmanager.domain.Rule;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReloadDroolsRules extends JpaRepository<Rule, Long> {
    //查找规则
    //Rule findByRuleKey(String ruleKey);
    void deleteByRuleKey(String rulekey);
}
