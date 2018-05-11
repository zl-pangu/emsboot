package com.zhph.model.hqclcf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

public class HqclcfEmpApvIdArray implements Serializable{
    @JsonProperty("deptId")
    private Long deptId;
    @JsonProperty("postId")
    private Long postId;
    @JsonProperty("businessCode")
    private String businessCode;
    @JsonProperty("workCode")
    private String workCode;
    @JsonProperty("businessLine")
    private Integer businessLine;
    @JsonProperty("enterDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date enterDate;
    @JsonProperty("fulltimeBeginDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date fulltimeBeginDate;
    @JsonProperty("type")
    private  String type;


    public Date getFulltimeBeginDate() {
        return fulltimeBeginDate;
    }
    public void setFulltimeBeginDate(Date fulltimeBeginDate) {
        this.fulltimeBeginDate = fulltimeBeginDate;
    }
    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public Integer getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Integer businessLine) {
        this.businessLine = businessLine;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
