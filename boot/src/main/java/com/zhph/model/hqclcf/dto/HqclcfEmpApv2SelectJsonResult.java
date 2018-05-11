package com.zhph.model.hqclcf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class HqclcfEmpApv2SelectJsonResult implements Serializable{

    @JsonProperty("businessLine")
    private Integer businessLine;
    @JsonProperty("postTree")
    private  Object postTree;
    @JsonProperty("businessData")
    private  Object businessData;
    @JsonProperty("workspaceData")
    private Object workspaceData;
    @JsonProperty("empNo")
    private String empNo;
    @JsonProperty("rankData")
    private Object rankData;
    @JsonProperty("fulltimeEndDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date fulltimeEndDate;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("success")
    private boolean success=true;


    public Object getRankData() {
        return rankData;
    }

    public void setRankData(Object rankData) {
        this.rankData = rankData;
    }

    public Date getFulltimeEndDate() {
        return fulltimeEndDate;
    }

    public void setFulltimeEndDate(Date fulltimeEndDate) {
        this.fulltimeEndDate = fulltimeEndDate;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmpNo() {
        return empNo;
    }
    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }
    public Object getWorkspaceData() {
        return workspaceData;
    }
    public void setWorkspaceData(Object workspaceData) {
        this.workspaceData = workspaceData;
    }
    public Object getPostTree() {
        return postTree;
    }
    public void setPostTree(Object postTree) {
        this.postTree = postTree;
    }
    public Integer getBusinessLine() {
        return businessLine;
    }
    public void setBusinessLine(Integer businessLine) {
        this.businessLine = businessLine;
    }
    public Object getBusinessData() {
        return businessData;
    }
    public void setBusinessData(Object businessData) {
        this.businessData = businessData;
    }

}
