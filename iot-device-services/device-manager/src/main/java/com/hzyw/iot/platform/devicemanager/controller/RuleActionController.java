package com.hzyw.iot.platform.devicemanager.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzyw.iot.platform.devicemanager.domain.comm.ResultVO;
import com.hzyw.iot.platform.devicemanager.domain.rule.MethodComboBoxDO;
import com.hzyw.iot.platform.devicemanager.domain.rule.RuleActionDO;
import com.hzyw.iot.platform.devicemanager.service.rule.RuleActionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 规则动作 管理
 *
 * @blame Android Team
 */
@Slf4j
@RestController
@RequestMapping("/rule/action")
@Api(description = "规则动作管理相关接口")
public class RuleActionController {
    @Autowired
    private RuleActionService ruleActionService;

    /**
     * 获取规则动作信息
     *
     * @param
     * @return
     */
    @GetMapping("/getOneRuleAction")
    @ApiOperation(value = "获取规则动作信息")
    public ResultVO<RuleActionDO> getOneRuleAction(@RequestParam("code") String codeId) {
        try {
            RuleActionDO ruleActionResult = ruleActionService.getOneRuleActionBiz(codeId);
            ResultVO resultDate=new ResultVO(0,"查询成功!",ruleActionResult);
            return resultDate;
        } catch (Exception e) {
            log.error("获取规则动作信息失败! Exception: "+e.getMessage());
            return ResultVO.failed("获取规则动作信息失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 下拉框 动作规则配置, 查条件方法名
     * @Param("deviceId")
     * @Param("deviceDomain")
     * @return
     */
    @GetMapping("/selMethodCombo")
    @ApiOperation(value = "下拉框 动作规则配置, 查条件方法名")
    public ResultVO<RuleActionDO> selDeviceMethod(@RequestParam("deviceId") String deviceId,@RequestParam("deviceDomain") String deviceDomain) {
        try {
            List<MethodComboBoxDO> methodComboList = ruleActionService.selDeviceMethodBiz(deviceId,deviceDomain);
            ResultVO resultDate=new ResultVO(0,"查询成功!",methodComboList);
            return resultDate;
        } catch (Exception e) {
            log.error("下拉框 查条件方法名失败! Exception: "+e.getMessage());
            return ResultVO.failed("下拉框 查条件方法名失败! Exception: "+e.getMessage());
        }
    }

    @PostMapping("/list")
    @ApiOperation(value = "查询规则动作列表（含过滤、分页等）")
    public PageInfo listRuleAction(@RequestBody(required = false) RuleActionDO ruleAction,
                                   @RequestParam int pageNum, @RequestParam int pageSize) {
        PageHelper.startPage(pageNum, pageSize, "DEVICE_TYPE,DEVICE_ID desc");
        List<RuleActionDO> list = ruleActionService.findRuleActionListBiz(ruleAction);
        return new PageInfo<>(list);
    }

    @PostMapping("/save")
    @ApiOperation(value = "保存规则动作")
    public Object saveRuleAction(@RequestBody RuleActionDO ruleAction) {
        try {
            ruleActionService.saveRuleActionBiz(ruleAction);
            return ResultVO.success("添加成功!");
        } catch (Exception e) {
            log.error("保存规则动作 添加失败! Exception: "+e.getMessage());
            return ResultVO.failed("添加失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 修改规则动作
     * @param ruleAction
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "修改规则动作")
    public Object updateRuleAction(@RequestBody RuleActionDO ruleAction) {
        try {
            ruleActionService.updateRuleActionBiz(ruleAction);
            return ResultVO.success("更新成功!");
        } catch (Exception e) {
            log.error("修改规则动作 更新失败! Exception: "+e.getMessage());
            return ResultVO.failed("更新失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 删除规则动作
     * @param ruleAction
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除规则动作")
    public ResultVO deleteRuleAction(@RequestBody RuleActionDO ruleAction) {
        try {
            ruleActionService.deleteByIdBiz(ruleAction);
            return ResultVO.success("删除成功!");
        } catch (Exception e) {
            log.error("删除规则动作 删除失败! Exception: "+e.getMessage());
            return ResultVO.failed("删除失败! Exception: "+e.getMessage());
        }
    }

    /**
     * 批量删除规则动作
     * @param ruleActionList
     * @return
     */
    @PostMapping("/batchDelete")
    @ApiOperation(value = "批量删除规则动作")
    public ResultVO deleteBatchRuleAction(@RequestBody List<RuleActionDO> ruleActionList) {
        try {
            ruleActionService.batchDeleteBiz(ruleActionList);
            return ResultVO.success("删除成功!");
        } catch (Exception e) {
            log.error("批量删除规则动作 删除失败! Exception: "+e.getMessage());
            return ResultVO.failed("删除失败! Exception: "+e.getMessage());
        }
    }
}
