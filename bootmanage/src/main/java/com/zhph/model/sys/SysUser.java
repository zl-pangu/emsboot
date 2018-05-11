package com.zhph.model.sys;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;

public class SysUser implements Serializable {
	private static final long serialVersionUID = -6080472750699276803L;

	private Long id;
	private String userId;
	private String userName;
	private String pwd;
	private String pwdBak;
	private String teleNum;
	private String mobile;
	private String email;
	private String fullName;
	private String empNo;
	private Integer isDelete;
	private Integer isEnable;
	private Integer isSuperAdmin;
	private Date lastLoginTime;
	private Integer loginErrorCount;
	private Date lastChangePwdTime;
	private String creatorUserId;
	private Date createTime;
	private String updateUserId;
	private Date updateTime;
	private String blSelect;// 选择的chexkBox -（业务条线的集合）
	
	private HqclcfDept region; // 大区或其它：部门类型为1
	private HqclcfDept filiale; // 分公司或其它：部门类型为2
	private HqclcfDept salesOfffice; // 营业部或其它：部门类型为3
	private HqclcfDept team;// 团队或其它：部门类型为4
	
	private HqclcfDept currentDept;//直接所在部门
	
	private List<HqclcfDept> hqDepts;//授权的部门列表：总部(登录后获取)
	private List<HqclcfDept> cfDepts;//授权的部门列表：消分(登录后获取)
	private List<HqclcfDept> clDepts;//授权的部门列表：信贷(登录后获取)
	
	private HqclcfEmp hqclcfEmp;//登录用户的员工信息,该字段可能为空
	
	public HqclcfEmp getHqclcfEmp() {
		return hqclcfEmp;
	}
	public void setHqclcfEmp(HqclcfEmp hqclcfEmp) {
		this.hqclcfEmp = hqclcfEmp;
	}
	public HqclcfDept getCurrentDept() {
		return currentDept;
	}
	public void setCurrentDept(HqclcfDept currentDept) {
		this.currentDept = currentDept;
	}
	public List<HqclcfDept> getHqDepts() {
		return hqDepts;
	}
	public void setHqDepts(List<HqclcfDept> hqDepts) {
		this.hqDepts = hqDepts;
	}
	public List<HqclcfDept> getClDepts() {
		return clDepts;
	}
	public void setClDepts(List<HqclcfDept> clDepts) {
		this.clDepts = clDepts;
	}
	public List<HqclcfDept> getCfDepts() {
		return cfDepts;
	}
	public void setCfDepts(List<HqclcfDept> cfDepts) {
		this.cfDepts = cfDepts;
	}
	public HqclcfDept getRegion() {
		return region;
	}
	public void setRegion(HqclcfDept dept) {
		this.region = dept;
	}
	public HqclcfDept getFiliale() {
		return filiale;
	}
	public void setFiliale(HqclcfDept filiale) {
		this.filiale = filiale;
	}
	public HqclcfDept getSalesOfffice() {
		return salesOfffice;
	}
	public void setSalesOfffice(HqclcfDept salesOfffice) {
		this.salesOfffice = salesOfffice;
	}
	public HqclcfDept getTeam() {
		return team;
	}
	public void setTeam(HqclcfDept team) {
		this.team = team;
	}
	public String getBlSelect() {
		return blSelect;
	}
	public void setBlSelect(String blSelect) {
		this.blSelect = blSelect;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPwdBak() {
		return pwdBak;
	}

	public void setPwdBak(String pwdBak) {
		this.pwdBak = pwdBak;
	}

	public String getTeleNum() {
		return teleNum;
	}

	public void setTeleNum(String teleNum) {
		this.teleNum = teleNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public Integer getIsSuperAdmin() {
		return isSuperAdmin;
	}

	public void setIsSuperAdmin(Integer isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getLoginErrorCount() {
		return loginErrorCount;
	}

	public void setLoginErrorCount(Integer loginErrorCount) {
		this.loginErrorCount = loginErrorCount;
	}

	public Date getLastChangePwdTime() {
		return lastChangePwdTime;
	}

	public void setLastChangePwdTime(Date lastChangePwdTime) {
		this.lastChangePwdTime = lastChangePwdTime;
	}

	public String getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(String creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
