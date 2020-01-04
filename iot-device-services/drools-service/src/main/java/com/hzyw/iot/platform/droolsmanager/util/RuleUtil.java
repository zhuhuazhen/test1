package com.hzyw.iot.platform.droolsmanager.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hzyw.iot.platform.droolsmanager.domain.RuleResult;

import org.apache.commons.codec.digest.DigestUtils;
import org.drools.javaparser.utils.StringEscapeUtils;
import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.internal.utils.KieHelper;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.hzyw.iot.platform.models.rule.RuleJsonTemplate;
import com.hzyw.iot.platform.models.rule.Source;
import com.hzyw.iot.platform.models.rule.Target;
import com.hzyw.iot.platform.models.rule.Condition;
import com.hzyw.iot.platform.droolsmanager.template.LinkageTemplate;

public class RuleUtil {

    /**
     * 生成原始UUID
     *
     * @return
     */
    private static String UUIDString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    /**
     * 生成格式化UUID
     *
     * @return
     */
    public static String UUIDFormatString(String replace) {
        String uuid = UUID.randomUUID().toString().replaceAll(replace, "");
        return uuid;
    }

    /**
     * 值加密
     *
     * @return
     */
    public static String MD5AndUUID() {
        //时间戳
        String timeJab = String.valueOf(System.currentTimeMillis());
        //UUID+时间戳
        String concat = UUIDString().concat(timeJab);
        return DigestUtils.md5Hex(concat);
    }

    /**
     * 生成不重复的规则编码
     *
     * @return
     */
    public static String typeJoinTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(new Date());
        Integer math = (int) ((Math.random() * 9 + 1) * 1000000);
        String code = dateNowStr.concat(math.toString());
        return code;
    }

    public static String rule(JSONObject json) {
        String rule = ruleWordExchangsST(json);
        return rule;
    }

    /**
     * 业务规则生成
     */
    public static String ruleWordExchangsST(JSONObject json) {
        STGroup group = new STGroupString(LinkageTemplate.linkageST);
        ST stFile = group.getInstanceOf("wordImport");
        ST stRule = group.getInstanceOf("ruleValue");
        //JSONObject jsonObject = JSONObject.parseObject(json);
        RuleJsonTemplate ruleJsonTemplate = json.toJavaObject(RuleJsonTemplate.class);
        //RuleJsonTemplate ruleJsonTemplate = JSON.parseObject(json,RuleJsonTemplate.class);
        Source source = ruleJsonTemplate.getSource();
        Target target = ruleJsonTemplate.getTarget();
        List<Condition> list = source.getConditionList();
        String conStr="";
        for(Condition condition : list){
            conStr +=condition.getKey()+" "+condition.getConstraint()+" "+condition.getValue()+" ,";
        }
        conStr += "sourceDeviceId ==\""+ source.getSourceId() +"\"";
        /*String actionStr="";
        Map<String, Object> map  = target.getIn();
        for (Map.Entry<String, Object> m : map.entrySet()) {
            actionStr += m.getKey() + ":" + m.getValue();
        }*/
        stRule.add("condition", conStr);
        stRule.add("action", StringEscapeUtils.escapeJava(json.getJSONObject("target").toJSONString()));
        stRule.add("rule", ruleJsonTemplate.getRuleId());
        stFile.add("rules", stRule);
        String result = stFile.render();
        return result;
    }


}
