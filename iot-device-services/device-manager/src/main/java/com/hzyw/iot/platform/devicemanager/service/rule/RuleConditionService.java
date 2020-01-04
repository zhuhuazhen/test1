package com.hzyw.iot.platform.devicemanager.service.rule;

import com.hzyw.iot.platform.devicemanager.domain.rule.ConditionComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.DeviceComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.DomainComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.RuleConditionDO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author by wj
 * @blame IOT Team
 * @date 2019/11/13.
 */
public interface RuleConditionService {
    /**
     * 下拉框 查设备大类
     * @return
     */
    List<DomainComboBoxDO> selDeviceDomainBiz();

    /**
     * 下拉框 查 设备ID 信息
     * @param deviceDomain
     * @return
     */
    List<DeviceComboBoxDO> selDeviceInfoBiz(@Param("deviceDomain") String deviceDomain);

    /**
     * 下拉框 事件类型:警告(属性), 查条件属性名
     * @param deviceId 设备ID (是否单个设备？是)
     * @param domainId 大类ID (是否单个设备？否)
     * @return
     */
    List<ConditionComboBoxDO> selDeviceAttributeBiz(@Param("deviceId") String deviceId, @Param("domainId") String domainId);

    /**
     * 下拉框 事件类型:联动(动作), 查条件动作名
     * @param deviceId 设备ID  (是否单个设备？是)
     * @param deviceDomain 大类ID (是否单个设备？否)
     * @return
     */
    List<ConditionComboBoxDO> selDeviceMethodBiz(@Param("deviceId") String deviceId, @Param("deviceDomain") String deviceDomain);

    /**
     * 查看规则条件信息
     * @param code
     * @return
     */
    RuleConditionDO getOneRuleConditionBiz(@Param("code") String code);

    /**
     * 查询规则条件列表
     * @param ruleCondition
     * @return
     */
    List<RuleConditionDO> findRuleConditionListBiz(RuleConditionDO ruleCondition);

    /**
     * 新增规则条件
     * @param ruleCondition
     * @return
     */
    int saveRuleConditionBiz(RuleConditionDO ruleCondition)throws Exception;

    /**
     * 更新规则条件
     */
    void updateRuleConditionBiz(RuleConditionDO ruleCondition)throws Exception;

    /**
     * 批量删除规则条件
     */
    void batchDeleteBiz(List<RuleConditionDO>  ruleConditionList)throws Exception;

    /**
     * 删除单条规则条件
     */
    void deleteByIdBiz(RuleConditionDO ruleCondition)throws Exception;

}
