package com.zhph.model.cf;


import java.io.Serializable;
import java.util.List;

import com.zhph.model.sys.SysUser;

/**
 * 考勤排班
 * Created by liuxin on 2016/12/12.
 */
public class TimeAutomatedBak implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long priNumber;
    private String empNo;//员工编号
    private String gzym;//工资年月
    private String creator;//创建人
    private String updator;//修改人
    private Integer days;//当月天数


    private String deptCode;//部门编码
    private String region;//大区
    private String filiale;//分公司
    private String businessDept;//营业部
    private String empName;//员工姓名
    private String jobName;//职位
    private String[] ids;//
    private SysUser user;
    //**查询参数**/
    private String orderBy;
    private List<String> deptNos;
    
    public List<String> getDeptNos() {
		return deptNos;
	}

	public void setDeptNos(List<String> deptNos) {
		this.deptNos = deptNos;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
    public SysUser getUser() {
        return user;
    }
    public void setUser(SysUser user) {
        this.user = user;
    }
    public String[] getIds() {
        return ids;
    }
    public void setIds(String[] ids) {
        this.ids = ids;
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
    public String getGzym() {
        return gzym;
    }
    public void setGzym(String gzym) {
        this.gzym = gzym;
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
    public Integer getDays() {
        return days;
    }
    public void setDays(Integer days) {
        this.days = days;
    }
    public String getDeptCode() {
        return deptCode;
    }
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
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
    public String getBusinessDept() {
        return businessDept;
    }
    public void setBusinessDept(String businessDept) {
        this.businessDept = businessDept;
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
}
