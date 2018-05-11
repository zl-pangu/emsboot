package com.zhph.model.hqclcf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by zhouliang on 2018/2/2.
 */
public class HqclcfEmpApvBrItem implements Serializable{
    @JsonProperty("caseTime")
    private String casetime;//发生时间区间
    @JsonProperty("caseTypeCode")
    private String casetypecode;//信息类别

    public String getCasetime() {
        return casetime;
    }

    public void setCasetime(String casetime) {
        this.casetime = casetime;
    }

    public String getCasetypecode() {
        return casetypecode;
    }

    public void setCasetypecode(String casetypecode) {
        this.casetypecode = casetypecode;
    }
}
