package com.zhph.model.hqclcf;

import java.io.Serializable;
import java.util.List;

public class HqclcfPersonTransfer implements Serializable {

    private Long priNumber;//主键标识

    private String empNo;    //员工编码

    private String empName; //员工姓名

    private String priDeptNo; //原部门编号

    private String priPostNo;//原岗位编号

    private String priHqPosition;//原职务

    private String priHqRank;//原职级

    private String priBusinessLine;//原业务条线

    private String transferType;//异动类型

    private String transferTime;//生效时间

    private String descript;//描述

    private String status;//状态[1.已生效	2.未生效]

    private String createName;    //创建人

    private String createDate;//创建日期

    private String newDept;//新部门

    private String newPost;//新岗位

    private String newHqPosition;//新职务

    private String newHqRank;//新职级

    private String newBusinessLine;    //新业务条线

    private List<Integer> loginUserId; //当前登录用户Id

    public List<Integer> getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(List<Integer> loginUserId) {
        this.loginUserId = loginUserId;
    }

    private static final long serialVersionUID = 1L;

    public String getNewDept() {
        return newDept;
    }

    public void setNewDept(String newDept) {
        this.newDept = newDept;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Long getPriNumber() {
        return priNumber;
    }

    public void setPriNumber(Long priNumber) {
        this.priNumber = priNumber;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo == null ? null : empNo.trim();
    }


    public String getPriDeptNo() {
        return priDeptNo;
    }

    public void setPriDeptNo(String priDeptNo) {
        this.priDeptNo = priDeptNo == null ? null : priDeptNo.trim();
    }


    public String getPriPostNo() {
        return priPostNo;
    }

    public void setPriPostNo(String priPostNo) {
        this.priPostNo = priPostNo == null ? null : priPostNo.trim();
    }

    public String getPriHqPosition() {
        return priHqPosition;
    }

    public void setPriHqPosition(String priHqPosition) {
        this.priHqPosition = priHqPosition == null ? null : priHqPosition.trim();
    }

    public String getPriHqRank() {
        return priHqRank;
    }

    public void setPriHqRank(String priHqRank) {
        this.priHqRank = priHqRank == null ? null : priHqRank.trim();
    }


    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType == null ? null : transferType.trim();
    }

    public String getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(String transferTime) {
        this.transferTime = transferTime == null ? null : transferTime.trim();
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript == null ? null : descript.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName == null ? null : createName.trim();
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate == null ? null : createDate.trim();
    }


    public String getNewPost() {
        return newPost;
    }

    public void setNewPost(String newPost) {
        this.newPost = newPost == null ? null : newPost.trim();
    }


    public String getNewHqPosition() {
        return newHqPosition;
    }

    public void setNewHqPosition(String newHqPosition) {
        this.newHqPosition = newHqPosition == null ? null : newHqPosition.trim();
    }

    public String getNewHqRank() {
        return newHqRank;
    }

    public void setNewHqRank(String newHqRank) {
        this.newHqRank = newHqRank == null ? null : newHqRank.trim();
    }

    public String getNewBusinessLine() {
        return newBusinessLine;
    }

    public void setNewBusinessLine(String newBusinessLine) {
        this.newBusinessLine = newBusinessLine;
    }

    public String getPriBusinessLine() {
        return priBusinessLine;
    }

    public void setPriBusinessLine(String priBusinessLine) {
        this.priBusinessLine = priBusinessLine;
    }

    @Override
    public String toString() {
        return "HqclcfPersonTransfer{" +
                "priNumber=" + priNumber +
                ", empNo='" + empNo + '\'' +
                ", empName='" + empName + '\'' +
                ", priDeptNo='" + priDeptNo + '\'' +
                ", priPostNo='" + priPostNo + '\'' +
                ", priHqPosition='" + priHqPosition + '\'' +
                ", priHqRank='" + priHqRank + '\'' +
                ", priBusinessLine='" + priBusinessLine + '\'' +
                ", transferType='" + transferType + '\'' +
                ", transferTime='" + transferTime + '\'' +
                ", descript='" + descript + '\'' +
                ", status='" + status + '\'' +
                ", createName='" + createName + '\'' +
                ", createDate='" + createDate + '\'' +
                ", newDept='" + newDept + '\'' +
                ", newPost='" + newPost + '\'' +
                ", newHqPosition='" + newHqPosition + '\'' +
                ", newHqRank='" + newHqRank + '\'' +
                ", newBusinessLine='" + newBusinessLine + '\'' +
                '}';
    }
}