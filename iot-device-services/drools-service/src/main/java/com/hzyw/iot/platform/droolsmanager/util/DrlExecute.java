package com.hzyw.iot.platform.droolsmanager.util;

import org.drools.core.impl.KnowledgeBaseImpl;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.command.CommandFactory;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.utils.KieHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DrlExecute {
    private static DecimalFormat df = new DecimalFormat("######0.00");
    protected static Logger logger = LoggerFactory.getLogger(DrlExecute.class);

    /**
     * 字符串模板方式读取规则
     *
     * @return 结果
     */
/*    static RuleResult rulePromote(PromoteExecute promoteExecute, Double moneySum) {
        // 判断业务规则是否存在
        RuleResult ruleresult = new RuleResult();
        ruleresult.setMoneySum(moneySum);
        logger.info("调整亮度前的数值" + moneySum);
        //统计完成后再将参数insert到联动规则中
        List cmdCondition = new ArrayList<>();
        cmdCondition.add(CommandFactory.newInsert(ruleresult));
        promoteExecute.getWorkSession().execute(CommandFactory.newBatchExecution(cmdCondition));
        logger.info("调整亮度后的数值" + ruleresult.getFinallyMoney());
        return ruleresult;
    }*/

    public static KieBase rulekieBase(String rule) {
        KieHelper helper = new KieHelper();
        try {
            helper.addContent(rule, ResourceType.DRL);
            return helper.build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("规则初始化失败");
        }

        //KieSession session = kieBase.newKieSession();
        //移除规则
        //kieBase.removeRule("rules","test");
        //重新添加规则
        //KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();
        //装入规则，可以装入多个
       // kb.add(ResourceFactory.newByteArrayResource(rule.getBytes("utf-8")), ResourceType.DRL);
        //kieBase.addKnowledgePackages(kb.getKnowledgePackages());

        //session.fireAllRules();

    }
}
