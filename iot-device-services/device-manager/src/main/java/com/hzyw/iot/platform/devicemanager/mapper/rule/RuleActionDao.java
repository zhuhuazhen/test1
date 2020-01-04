package com.hzyw.iot.platform.devicemanager.mapper.rule;

import com.hzyw.iot.platform.devicemanager.domain.rule.ConditionComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.MethodComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.RuleActionDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 规则动作管理
 * @author  wj
 * @date 2019-11-12
 */
public interface RuleActionDao {
    /**
     * 下拉框 规则 动作配置, 查条件动作名
     * @param deviceId 设备ID  (是否单个设备？是)
     * @param deviceDomain 大类ID (是否单个设备？否)
     * @return
     */
    List<MethodComboBoxDO> selDeviceMethod(@Param("deviceId") String deviceId, @Param("deviceDomain") String deviceDomain);

    /**
     * 查看 规则动作信息
     * @param code
     * @return
     */
    RuleActionDO getOneRuleAction(@Param("code") String code);

    /**
     * 查询规则 动作列表
     * @param ruleAction
     * @return
     */
    List<RuleActionDO> findRuleActionList(RuleActionDO ruleAction);

    /**
     * 新增规则 动作
     * @param ruleAction
     * @return
     */
    int saveRuleAction(RuleActionDO ruleAction)throws Exception;

    /**
     * 更新规则动作
     */
    void updateRuleAction(RuleActionDO ruleAction)throws Exception;

    /**
     * 批量删除规则动作
     */
    void batchDelete(List<RuleActionDO>  ruleActionList)throws Exception;

    /**
     * 删除单条规则动作
     */
    void deleteById(RuleActionDO ruleAction)throws Exception;
}
