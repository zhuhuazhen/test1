package com.hzyw.iot.platform.models.rule;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/29.
 */
public class RuleJsonTemplate {
    private String ruleId;
    private String ruleName;
    private Target target;
    private Source source;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }
}
