package com.hzyw.iot.platform.devicemanager.service.rule.impl;

import com.hzyw.iot.platform.devicemanager.domain.rule.ConditionComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.DeviceComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.DomainComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.RuleConditionDO;
import com.hzyw.iot.platform.devicemanager.mapper.rule.RuleConditionDao;
import com.hzyw.iot.platform.devicemanager.service.rule.RuleConditionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/2.
 */
@Slf4j
@Service
public class RuleConditionServiceImpl implements RuleConditionService {
    @Resource
    RuleConditionDao ruleConditionDao;

    @Override
    public List<DomainComboBoxDO> selDeviceDomainBiz() {
        return ruleConditionDao.selDeviceDomain();
    }

    @Override
    public List<DeviceComboBoxDO> selDeviceInfoBiz(String deviceDomain) {
        return ruleConditionDao.selDeviceInfo(deviceDomain);
    }

    @Override
    public List<ConditionComboBoxDO> selDeviceAttributeBiz(String deviceId, String domainId) {
        return ruleConditionDao.selDeviceAttribute(deviceId,domainId);
    }

    @Override
    public List<ConditionComboBoxDO> selDeviceMethodBiz(String deviceId, String deviceDomain) {
        return ruleConditionDao.selDeviceMethod(deviceId,deviceDomain);
    }

    @Override
    public RuleConditionDO getOneRuleConditionBiz(String code) {
        return ruleConditionDao.getOneRuleCondition(code);
    }

    @Override
    public List<RuleConditionDO> findRuleConditionListBiz(RuleConditionDO ruleCondition) {
        return ruleConditionDao.findRuleConditionList(ruleCondition);
    }

    @Override
    public int saveRuleConditionBiz(RuleConditionDO ruleCondition) throws Exception {
        return ruleConditionDao.saveRuleCondition(ruleCondition);
    }

    @Override
    public void updateRuleConditionBiz(RuleConditionDO ruleCondition) throws Exception {
        ruleConditionDao.updateRuleCondition(ruleCondition);
    }

    @Override
    public void batchDeleteBiz(List<RuleConditionDO> ruleConditionList) throws Exception {
        ruleConditionDao.batchDelete(ruleConditionList);
    }

    @Override
    public void deleteByIdBiz(RuleConditionDO ruleCondition) throws Exception {
        ruleConditionDao.deleteById(ruleCondition);
    }
}
