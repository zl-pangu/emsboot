package com.zhph.model.hqclcf;

import java.io.Serializable;

/**
 * Created by zhouliang on 2017/12/26.
 */
public class HqclcfPubCodeModel implements Serializable{
    private String caseSource;//案件来源
    private String caseType;//案件类型
    private String caseTime;//案件时间
    private String caseLevel;//案件级别

    public String getCaseSource() {
        return caseSource;
    }

    public void setCaseSource(String caseSource) {
        this.caseSource = caseSource;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseTime() {
        return caseTime;
    }

    public void setCaseTime(String caseTime) {
        this.caseTime = caseTime;
    }

    public String getCaseLevel() {
        return caseLevel;
    }

    public void setCaseLevel(String caseLevel) {
        this.caseLevel = caseLevel;
    }
}
