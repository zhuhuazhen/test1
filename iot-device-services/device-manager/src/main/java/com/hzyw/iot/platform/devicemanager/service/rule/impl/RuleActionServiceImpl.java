package com.hzyw.iot.platform.devicemanager.service.rule.impl;

import com.hzyw.iot.platform.devicemanager.domain.rule.MethodComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.RuleActionDO;
import com.hzyw.iot.platform.devicemanager.mapper.rule.RuleActionDao;
import com.hzyw.iot.platform.devicemanager.service.rule.RuleActionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author by wj
 * @blame IOT Team
 * @date 2019/11/13.
 */
@Slf4j
@Service
public class RuleActionServiceImpl implements RuleActionService {
    @Resource
    RuleActionDao ruleActionDao;

    @Override
    public List<MethodComboBoxDO> selDeviceMethodBiz(String deviceId, String deviceDomain) {
        return ruleActionDao.selDeviceMethod(deviceId,deviceDomain);
    }

    @Override
    public RuleActionDO getOneRuleActionBiz(String code) {
        return ruleActionDao.getOneRuleAction(code);
    }

    @Override
    public List<RuleActionDO> findRuleActionListBiz(RuleActionDO ruleAction) {
        return ruleActionDao.findRuleActionList(ruleAction);
    }

    @Override
    public int saveRuleActionBiz(RuleActionDO ruleAction) throws Exception {
        return ruleActionDao.saveRuleAction(ruleAction);
    }

    @Override
    public void updateRuleActionBiz(RuleActionDO ruleAction) throws Exception {
        ruleActionDao.updateRuleAction(ruleAction);
    }

    @Override
    public void deleteByIdBiz(RuleActionDO ruleAction) throws Exception {
        ruleActionDao.deleteById(ruleAction);
    }

    @Override
    public void batchDeleteBiz(List<RuleActionDO> ruleActionList) throws Exception {
        ruleActionDao.batchDelete(ruleActionList);
    }
}
