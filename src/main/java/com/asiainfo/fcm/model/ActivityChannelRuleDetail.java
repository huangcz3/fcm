package com.asiainfo.fcm.model;

/**
 * Created by PuMg on 2017/7/6/0006.
 */
public class ActivityChannelRuleDetail {
    //渠道规则ID
    private String ruleId;
    //规则名称
    private String ruleName;
    //渠道规则值
    private String ruleValue;
    //渠道扩展值
    private String ruleExtendedValue;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }

    public String getRuleExtendedValue() {
        return ruleExtendedValue;
    }

    public void setRuleExtendedValue(String ruleExtendedValue) {
        this.ruleExtendedValue = ruleExtendedValue;
    }
}
