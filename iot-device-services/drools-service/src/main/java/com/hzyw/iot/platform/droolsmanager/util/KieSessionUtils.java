package com.hzyw.iot.platform.droolsmanager.util;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

public class KieSessionUtils {
    private static KieBase kieBase;

    //定义规则文件的包名，与drl文件里的package对应
    private static final String drlPackage = "rules";

    //定义drl文件的存放路径，静态变量需要通过在其set方法上打@Value注解，才可实现配置注入
    private static String drlPath;

    //通过配置拉取路径，使用配置中心可以实时更改通过@Value拉取的配置
    @Value("${drools.points.drlPath}")
    public void setDrlPath(String drlPath){
        KieSessionUtils.drlPath = drlPath;
    }

    /**
     *  生成kieSeesion会话
     * @param ruleName
     * @return
     * @throws Exception
     */
    public static KieSession newKieSession(String ruleName) throws Exception {
        //无状态的kieSession，和有状态相比，区别在于不维持会话，即使用完后自动释放资源，不需要手动调dispose
        //StatelessKieSession kieSession = getKieBase(ruleName).newStatelessKieSession();
        //有状态的kieSession
        KieSession kieSession = getKieBase(ruleName).newKieSession();
        //添加监听器，这里加的是对规则文件运行debug监听器，测试时最好加上，用于排查问题，生产上可视情况去掉
        kieSession.addEventListener(new DebugRuleRuntimeEventListener());
        return kieSession;
    }

    /**
     *  生成kieBase
     * @param ruleName 规则文件名
     * @return
     * @throws Exception
     */
    protected static KieBase getKieBase(String ruleName) throws Exception {
        //判断kieBase和需要获取的规则文件是否存在，不存在则重新初始化kieBase
        if (kieBase ==null || kieBase.getRule(drlPackage,ruleName)==null) {
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kfs = kieServices.newKieFileSystem();
            //获取规则数据源，这里由于本人项目使用的是springboot，打包会打成jar包，如果想做实时更新，drl文件需要放在jar包外面
            //获取resource的方式很多，不一定要用读取文件的方式，可根据自己的设计和业务场景采取不同方案
            Resource resource = kieServices.getResources().newFileSystemResource(new File(drlPath+"/"+ruleName));
            resource.setResourceType(ResourceType.DRL);
            kfs.write(resource);
            KieBuilder kieBuilder = kieServices.newKieBuilder(kfs).buildAll();
            if (kieBuilder.getResults().getMessages(Message.Level.ERROR).size() > 0) {
                throw new Exception();
            }
            KieContainer kieContainer = kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
            kieBase = kieContainer.getKieBase();
        }
        return kieBase;
    }

    /**
     * 更新规则
     * @param ruleName 规则名和规则文件名
     * @throws Exception
     */
    public static void refreshRules(String ruleName) throws Exception {
        //判断规则不为null，则移除规则
        if (kieBase !=null && kieBase.getRule(drlPackage,ruleName)!=null){
            //为了方便，本人把规则名和drl文件名称统一定义了
            kieBase.removeRule(drlPackage,ruleName);
            //重新初始化kieBase
            getKieBase(ruleName);
        }
    }
}
