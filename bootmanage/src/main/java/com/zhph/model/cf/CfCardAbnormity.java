package com.zhph.model.cf;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhouliang on 2018/1/2.
 * 打卡异常管理
 */
public class CfCardAbnormity implements Serializable{
    private Long   id;                 //唯一标识符
    private String gzYm;               //工资年月
    private String empNo;                     //员工编号
    private String empName;                   //员工姓名
    private String deptCode;                  //部门唯一编号
    private String jobNo;                     //职务编号
    private String upWorkAbnormityNo = "0";         //上班打卡异常次数
    private String offWorkAbnormityNo = "0";        //下班打卡异常次数
    private String checkAbnormityNo = "0";          //抽查异常次数
    private String abnormityTotalNo = "0";          //异常总次数
    private String absenteeismNo = "0";             //旷工次数

    private String region;					  //大区
    private String filiale;					  //分公司
    private String dept;					  //营业部
    private String team;					  //团队
    private String deptInfo;
    private String queryDeptParm;//部门查询参数


    private String creator;//创建人
    private String updator;//修改人
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createtime;//创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updatetime;//修改时间

    public String getQueryDeptParm() {
        return queryDeptParm;
    }

    public void setQueryDeptParm(String queryDeptParm) {
        this.queryDeptParm = queryDeptParm;
    }

    public String getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(String deptInfo) {
        this.deptInfo = deptInfo;
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGzYm() {
        return gzYm;
    }

    public void setGzYm(String gzYm) {
        this.gzYm = gzYm;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getUpWorkAbnormityNo() {
        return upWorkAbnormityNo;
    }

    public void setUpWorkAbnormityNo(String upWorkAbnormityNo) {
        this.upWorkAbnormityNo = upWorkAbnormityNo;
    }

    public String getOffWorkAbnormityNo() {
        return offWorkAbnormityNo;
    }

    public void setOffWorkAbnormityNo(String offWorkAbnormityNo) {
        this.offWorkAbnormityNo = offWorkAbnormityNo;
    }

    public String getCheckAbnormityNo() {
        return checkAbnormityNo;
    }

    public void setCheckAbnormityNo(String checkAbnormityNo) {
        this.checkAbnormityNo = checkAbnormityNo;
    }

    public String getAbnormityTotalNo() {
        return abnormityTotalNo;
    }

    public void setAbnormityTotalNo(String abnormityTotalNo) {
        this.abnormityTotalNo = abnormityTotalNo;
    }

    public String getAbsenteeismNo() {
        return absenteeismNo;
    }

    public void setAbsenteeismNo(String absenteeismNo) {
        this.absenteeismNo = absenteeismNo;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFiliale() {
        return filiale;
    }

    public void setFiliale(String filiale) {
        this.filiale = filiale;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
