package com.zhph.model.hqclcf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by zhouliang on 2018/2/2.
 */
public class HqclcfEmpApvBrProduct implements Serializable{
    @JsonProperty("sdCheckresult")
    private String sdcheckresult;//涉毒
    @JsonProperty("xdCheckresult")
    private String xdcheckresult;//吸毒
    @JsonProperty("checkCount2")
    private String checkcount2;//违反行为的数量
    @JsonProperty("ztCheckresult")
    private String ztcheckresult;
    @JsonProperty("item")
    private HqclcfEmpApvBrItem item;//案件详细内容
    @JsonProperty("costTime")
    private int costtime;//返回时间
    @JsonProperty("wfxwCheckresult")
    private String wfxwcheckresult;//违法行为

    public String getSdcheckresult() {
        return sdcheckresult;
    }

    public void setSdcheckresult(String sdcheckresult) {
        this.sdcheckresult = sdcheckresult;
    }

    public String getXdcheckresult() {
        return xdcheckresult;
    }

    public void setXdcheckresult(String xdcheckresult) {
        this.xdcheckresult = xdcheckresult;
    }

    public String getCheckcount2() {
        return checkcount2;
    }

    public void setCheckcount2(String checkcount2) {
        this.checkcount2 = checkcount2;
    }

    public String getZtcheckresult() {
        return ztcheckresult;
    }

    public void setZtcheckresult(String ztcheckresult) {
        this.ztcheckresult = ztcheckresult;
    }

    public HqclcfEmpApvBrItem getItem() {
        return item;
    }

    public void setItem(HqclcfEmpApvBrItem item) {
        this.item = item;
    }

    public int getCosttime() {
        return costtime;
    }

    public void setCosttime(int costtime) {
        this.costtime = costtime;
    }

    public String getWfxwcheckresult() {
        return wfxwcheckresult;
    }

    public void setWfxwcheckresult(String wfxwcheckresult) {
        this.wfxwcheckresult = wfxwcheckresult;
    }
}
