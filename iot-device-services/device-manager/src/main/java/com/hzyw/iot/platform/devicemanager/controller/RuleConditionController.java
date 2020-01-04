package com.hzyw.iot.platform.devicemanager.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzyw.iot.platform.devicemanager.domain.comm.ResultVO;
import com.hzyw.iot.platform.devicemanager.domain.rule.*;
import com.hzyw.iot.platform.devicemanager.service.rule.RuleConditionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 规则条件 管理
 *
 * @blame IOT Team
 */
@Slf4j
@RestController
@RequestMapping("/rule/condition")
@Api(description = "规则条件管理相关接口")
public class RuleConditionController {

    @Autowired
    private RuleConditionService ruleConditionService;

    /**
     * 下拉框 查设备大类
     * @return
     */
    @GetMapping("/selDeviceDomainCombo")
    @ApiOperation(value = "下拉框 查设备大类")
    public ResultVO<List<DomainComboBoxDO>> selDeviceDomain() {
        try {
            List<DomainComboBoxDO> domainComboList = ruleConditionService.selDeviceDomainBiz();
            ResultVO resultDate=new ResultVO(0,"查询成功!",domainComboList);
            return resultDate;
        } catch (Exception e) {
            log.error("下拉框 查设备大类失败! Exception: "+e.getMessage());
            return ResultVO.failed("下拉框 查设备大类失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 下拉框 依 大类ID 查 设备
     * @Param("deviceDomain") 大类ID
     * @return
     */
    @GetMapping("/selDeviceCombo")
    @ApiOperation(value = "下拉框 依 大类ID 查设备")
    public ResultVO<List<DeviceComboBoxDO>> selDeviceInfo(@RequestParam("deviceDomain") String deviceDomain) {
        try {
            List<DeviceComboBoxDO> methodComboList = ruleConditionService.selDeviceInfoBiz(deviceDomain);
            ResultVO resultDate=new ResultVO(0,"查询成功!",methodComboList);
            return resultDate;
        } catch (Exception e) {
            log.error("下拉框 查设备ID失败! Exception: "+e.getMessage());
            return ResultVO.failed("下拉框 查设备ID失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 下拉框 事件类型:警告(属性), 查条件属性名; 联动(方法) 查条件动作名
     * @param eventType 事件类型 :1、警告(属性); 2、联动(方法)
     * @param deviceId 设备ID (是否单个设备？是)
     * @param domainId 大类ID (是否单个设备？否)
     * @return
     */
    @GetMapping("/selConditionCombo")
    @ApiOperation(value = "下拉框 事件类型:警告(属性), 查条件属性名; 联动(方法) 查条件动作名")
    public ResultVO<List<ConditionComboBoxDO>> selConditionCombo(@RequestParam("eventType") Integer eventType,@RequestParam("deviceId") String deviceId,@RequestParam("domainId") String domainId) {
        List<ConditionComboBoxDO> conditionComboList=new ArrayList<ConditionComboBoxDO>();
        try {
            if(eventType==1){//警告(属性) 事件
                conditionComboList = ruleConditionService.selDeviceAttributeBiz(deviceId,domainId);
            }else if(eventType==2){//联动(方法) 事件
                conditionComboList = ruleConditionService.selDeviceMethodBiz(deviceId,domainId);
            }
            ResultVO resultDate=new ResultVO(0,"查询成功!",conditionComboList);
            return resultDate;
        } catch (Exception e) {
            log.error("下拉框 查条件属性名失败! Exception: "+e.getMessage());
            return ResultVO.failed("下拉框 查条件属性名失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 获取规则条件信息
     *
     * @param
     * @return
     */
    @GetMapping("/getOneRuleCondition")
    @ApiOperation(value = "获取规则条件信息")
    public ResultVO<RuleConditionDO> getOneRuleCondition(@RequestParam("code") String codeId) {
        try {
            RuleConditionDO ruleConditionResult = ruleConditionService.getOneRuleConditionBiz(codeId);
            ResultVO resultDate=new ResultVO(0,"查询成功!",ruleConditionResult);
            return resultDate;
        } catch (Exception e) {
            log.error("获取规则条件信息失败! Exception: "+e.getMessage());
            return ResultVO.failed("获取规则条件信息失败! Exception: "+e.getMessage());
        }
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询规则条件列表（含过滤、分页等）")
    public PageInfo listRuleCondition(@RequestBody(required = false) RuleConditionDO ruleCondition,
                                   @RequestParam int pageNum, @RequestParam int pageSize) {
        PageHelper.startPage(pageNum, pageSize, "EVENT_TYPE,DEVICE_TYPE,DEVICE_ID desc");
        List<RuleConditionDO> list = ruleConditionService.findRuleConditionListBiz(ruleCondition);
        return new PageInfo<>(list);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存规则条件")
    public Object saveruleCondition(@RequestBody RuleConditionDO ruleCondition) {
        try {
            ruleConditionService.saveRuleConditionBiz(ruleCondition);
            return ResultVO.success("添加成功!");
        } catch (Exception e) {
            log.error("保存规则条件 添加失败! Exception: "+e.getMessage());
            return ResultVO.failed("添加失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 修改规则条件
     * @param ruleCondition
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改规则条件")
    public Object updateruleCondition(@RequestBody RuleConditionDO ruleCondition) {
        try {
            ruleConditionService.updateRuleConditionBiz(ruleCondition);
            return ResultVO.success("更新成功!");
        } catch (Exception e) {
            log.error("修改规则条件 更新失败! Exception: "+e.getMessage());
            return ResultVO.failed("更新失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 删除规则条件
     * @param ruleCondition
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除规则条件")
    public ResultVO deleteruleCondition(@RequestBody RuleConditionDO ruleCondition) {
        try {
            ruleConditionService.deleteByIdBiz(ruleCondition);
            return ResultVO.success("删除成功!");
        } catch (Exception e) {
            log.error("删除规则条件 删除失败! Exception: "+e.getMessage());
            return ResultVO.failed("删除失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 批量删除规则条件
     * @param ruleConditionList
     * @return
     */
    @PostMapping("/batchDelete")
    @ApiOperation(value = "批量删除规则条件")
    public ResultVO deleteBatchruleCondition(@RequestBody List<RuleConditionDO> ruleConditionList) {
        try {
            ruleConditionService.batchDeleteBiz(ruleConditionList);
            return ResultVO.success("删除成功!");
        } catch (Exception e) {
            log.error("批量删除规则条件 删除失败! Exception: "+e.getMessage());
            return ResultVO.failed("删除失败! Exception: "+e.getMessage());
        }
    }
}
