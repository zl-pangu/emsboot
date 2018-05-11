package com.zhph.model.cf;

import java.util.List;

/**
 * Create By lishuangjiang
 * 消分员工状态变更
 */
public class CfEmpStatus {


    private Long priNumber;
    private String empNo;//员工编号
    private Integer status;//状态
    private String startDate;//开始时间
    private String endDate;//结束时间
    private String remark;//描述
    private String createDate;
    private String updateDate;
    private String creator;
    private String updator;

    private String empName;//员工姓名
    private String jobName;//职位
    private String statusName;//
    private Integer isend;//是否结束
    private String deptArea;//大区
    private String orgNo;//分公司
    private String salesDept;//营业部
    private String deptCode;//团队编码
    private String otherQuery;
    private String enterDate;//入职时间
    private String deptName;//团队名称
    private List<Integer> sysUser;//权限

    public List<Integer> getSysUser() {
        return sysUser;
    }

    public void setSysUser(List<Integer> sysUser) {
        this.sysUser = sysUser;
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
        this.empNo = empNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getIsend() {
        return isend;
    }

    public void setIsend(Integer isend) {
        this.isend = isend;
    }

    public String getDeptArea() {
        return deptArea;
    }

    public void setDeptArea(String deptArea) {
        this.deptArea = deptArea;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getSalesDept() {
        return salesDept;
    }

    public void setSalesDept(String salesDept) {
        this.salesDept = salesDept;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getOtherQuery() {
        return otherQuery;
    }

    public void setOtherQuery(String otherQuery) {
        this.otherQuery = otherQuery;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
