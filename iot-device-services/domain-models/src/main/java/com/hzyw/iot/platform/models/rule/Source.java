package com.hzyw.iot.platform.models.rule;

import java.util.List;

/**
 * @author by early
 * @blame IOT Team
 * @date 2019/9/29.
 */

public class Source {

    private List<Condition> conditionList;
    private String sourceId;
    private int sourceFlag;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getSourceFlag() {
        return sourceFlag;
    }

    public void setSourceFlag(int sourceFlag) {
        this.sourceFlag = sourceFlag;
    }

    public List<Condition> getConditionList() {
        return conditionList;
    }

    public void setConditionList(List<Condition> conditionList) {
        this.conditionList = conditionList;
    }
}
