package com.hzyw.iot.platform.devicemanager.mapper.rule;

import com.hzyw.iot.platform.devicemanager.domain.rule.ConditionComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.DeviceComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.DomainComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.RuleConditionDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 规则条件管理
 * @author wj
 * @date 2019-11-12
 */
public interface RuleConditionDao {
    /**
     * 下拉框 查设备大类
     * @return
     */
    List<DomainComboBoxDO> selDeviceDomain();

    /**
     * 下拉框 查 设备ID 信息
     * @param deviceDomain
     * @return
     */
    List<DeviceComboBoxDO> selDeviceInfo(@Param("deviceDomain") String deviceDomain);

    /**
     * 下拉框 事件类型:警告(属性), 查条件属性名
     * @param deviceId 设备ID (是否单个设备？是)
     * @param domainId 大类ID (是否单个设备？否)
     * @return
     */
    List<ConditionComboBoxDO> selDeviceAttribute(@Param("deviceId") String deviceId, @Param("domainId") String domainId);

    /**
     * 下拉框 事件类型:联动(动作), 查条件动作名
     * @param deviceId 设备ID  (是否单个设备？是)
     * @param deviceDomain 大类ID (是否单个设备？否)
     * @return
     */
    List<ConditionComboBoxDO> selDeviceMethod(@Param("deviceId") String deviceId, @Param("deviceDomain") String deviceDomain);
    /**
     * 查看规则条件信息
     * @param code
     * @return
     */
    RuleConditionDO getOneRuleCondition(@Param("code") String code);

    /**
     * 查询规则条件列表
     * @param ruleCondition
     * @return
     */
    List<RuleConditionDO> findRuleConditionList(RuleConditionDO ruleCondition);

    /**
     * 新增规则条件
     * @param ruleCondition
     * @return
     */
    int saveRuleCondition(RuleConditionDO ruleCondition)throws Exception;

    /**
     * 更新规则条件
     */
    void updateRuleCondition(RuleConditionDO ruleCondition)throws Exception;

    /**
     * 批量删除规则条件
     */
    void batchDelete(List<RuleConditionDO>  ruleConditionList)throws Exception;

    /**
     * 删除单条规则条件
     */
    void deleteById(RuleConditionDO ruleCondition)throws Exception;
}
