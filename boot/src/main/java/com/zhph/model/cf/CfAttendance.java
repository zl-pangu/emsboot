package com.zhph.model.cf;

import com.zhph.util.DateUtil;

import java.io.Serializable;

/**
 * Create By lishuangjiang
 */
public class CfAttendance implements Serializable {

    private Long priNumber;//考勤统计主键标识
    private String empNo;//员工编号
    private String gzym;//工资年月
    private String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");//创建时间
    private String creator;//创建人
    private Integer oughtAttend = 0;//应出勤天数
    private Double realAttend = 0d;//实际出勤天数
    private Double personalLeave = 0d;//事假天数
    private Double sickLeave = 0d;//病假天数
    private Double welfareLeave = 0d;//福利假天数 暂时保留
    private Double welfareLeave1 = 0d;//年/婚/丧假
    private Double welfareLeave2 = 0d;//产假类/工伤假类
    private Integer commuteClockAbnormal = 0;//上下班打卡异常次数
    private Integer spotCheckClockAbnormal = 0;//抽查打卡异常次数
    private Double absenteeism = 0d;//旷工天数

    private String empName;//员工姓名
    private String deptCode;//大区
    private String postName;//分中心
    private String businessNameo;//营业部
    private String rankName;// 团队
    private String jobName;//职位
    private String onlyDeptCode;//唯一部门编码
    private Integer businessLine;//业务条线

    private String enterDate;//入职时间
    private String leaveDate;//离职时间

    public Long getPriNumber() {
        return priNumber;
    }

    public String getOnlyDeptCode() {
        return onlyDeptCode;
    }

    public void setOnlyDeptCode(String onlyDeptCode) {
        this.onlyDeptCode = onlyDeptCode;
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

    public String getGzym() {
        return gzym;
    }

    public void setGzym(String gzym) {
        this.gzym = gzym;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Integer getOughtAttend() {
        return oughtAttend;
    }

    public void setOughtAttend(Integer oughtAttend) {
        this.oughtAttend = oughtAttend;
    }

    public Double getRealAttend() {
        return realAttend;
    }

    public void setRealAttend(Double realAttend) {
        this.realAttend = realAttend;
    }

    public Double getPersonalLeave() {
        return personalLeave;
    }

    public void setPersonalLeave(Double personalLeave) {
        this.personalLeave = personalLeave;
    }

    public Double getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(Double sickLeave) {
        this.sickLeave = sickLeave;
    }

    public Double getWelfareLeave() {
        return welfareLeave;
    }

    public void setWelfareLeave(Double welfareLeave) {
        this.welfareLeave = welfareLeave;
    }

    public Double getWelfareLeave1() {
        return welfareLeave1;
    }

    public void setWelfareLeave1(Double welfareLeave1) {
        this.welfareLeave1 = welfareLeave1;
    }

    public Double getWelfareLeave2() {
        return welfareLeave2;
    }

    public void setWelfareLeave2(Double welfareLeave2) {
        this.welfareLeave2 = welfareLeave2;
    }

    public Integer getCommuteClockAbnormal() {
        return commuteClockAbnormal;
    }

    public void setCommuteClockAbnormal(Integer commuteClockAbnormal) {
        this.commuteClockAbnormal = commuteClockAbnormal;
    }

    public Integer getSpotCheckClockAbnormal() {
        return spotCheckClockAbnormal;
    }

    public void setSpotCheckClockAbnormal(Integer spotCheckClockAbnormal) {
        this.spotCheckClockAbnormal = spotCheckClockAbnormal;
    }

    public Double getAbsenteeism() {
        return absenteeism;
    }

    public void setAbsenteeism(Double absenteeism) {
        this.absenteeism = absenteeism;
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

    public String getBusinessNameo() {
        return businessNameo;
    }

    public void setBusinessNameo(String businessNameo) {
        this.businessNameo = businessNameo;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Integer getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Integer businessLine) {
        this.businessLine = businessLine;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
