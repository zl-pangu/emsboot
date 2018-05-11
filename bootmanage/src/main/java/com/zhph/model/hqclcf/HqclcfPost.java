package com.zhph.model.hqclcf;

import java.io.Serializable;

/**
 * Create by lsj
 */
public class HqclcfPost implements Serializable {

    private Long priNumber;//主键标识
    private String postNo;//岗位编码
    private String postName;//岗位名称
    private String deptNo;//部门编码
    private Integer businessLine;//业务线
    private Long postPid;//上级岗位
    private String rankNo;//职务编码
    private Integer organizat;//编制
    private String status;//状态
    private String description;//描述
    private String createBy;//创建者
    private String createTime;//创建时间

    public Long getPriNumber() {
        return priNumber;
    }

    public void setPriNumber(Long priNumber) {
        this.priNumber = priNumber;
    }

    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public Integer getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Integer businessLine) {
        this.businessLine = businessLine;
    }

    public Long getPostPid() {
        return postPid;
    }

    public void setPostPid(Long postPid) {
        this.postPid = postPid;
    }

    public String getRankNo() {
        return rankNo;
    }

    public void setRankNo(String rankNo) {
        this.rankNo = rankNo;
    }

    public Integer getOrganizat() {
        return organizat;
    }

    public void setOrganizat(Integer organizat) {
        this.organizat = organizat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
