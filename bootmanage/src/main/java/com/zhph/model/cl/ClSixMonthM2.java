package com.zhph.model.cl;

import java.io.Serializable;

public class ClSixMonthM2 implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; // 主键序列
	private String loanContractNo;// 合同编号
	private String loanName;// 贷款人姓名
	private Float loanAmount;// 合同额
	private Float grantLoanAmount;// 放款额
	private String grantLoanDate;// 约定放款日
	private String payDate;// 实际放款日
	private String businessManagerNo;// 业务经理编号
	private String businessManagerName;// 业务经理姓名
	private String teamManagerNo;// 团队经理编号
	private String teamManagerName;// 团队经理姓名
	private String orgManagerNo;// 城市经理编号
	private String orgManagerName;// 城市经理姓名
	private String areaManagerNo;// 区域经理编号
	private String areaManagerName;// 区域经理姓名
	private String wfEmpNo;// 外访专员编号
	private String wfEmpName;// 外访专员姓名
	private String xsEmpNo;// 信审专员编号
	private String xsEmpName;// 信审专员姓名
	private String kfEmpNo;// 客服专员编号
	private String kfEmpName;// 客服专员姓名
	private String orgNo;// 分公司编号
	private String orgName;// 分公司名称
	private String deptNo;// 部门编号
	private String deptName;// 部门名称
	private String teamNo;// 团队编号
	private String teamName;// 团队名称
	private String beOverdueNum;// 逾期天数
	private String gzYm;// 工资年月
	private String createTime;// 创建时间
	private String creatorNo;// 创建者
	private String updateTime;// 修改时间
	private String modifierNo;// 修改者

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoanContractNo() {
		return loanContractNo;
	}

	public void setLoanContractNo(String loanContractNo) {
		this.loanContractNo = loanContractNo;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public Float getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Float loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Float getGrantLoanAmount() {
		return grantLoanAmount;
	}

	public void setGrantLoanAmount(Float grantLoanAmount) {
		this.grantLoanAmount = grantLoanAmount;
	}

	public String getGrantLoanDate() {
		return grantLoanDate;
	}

	public void setGrantLoanDate(String grantLoanDate) {
		this.grantLoanDate = grantLoanDate;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public String getBusinessManagerNo() {
		return businessManagerNo;
	}

	public void setBusinessManagerNo(String businessManagerNo) {
		this.businessManagerNo = businessManagerNo;
	}

	public String getBusinessManagerName() {
		return businessManagerName;
	}

	public void setBusinessManagerName(String businessManagerName) {
		this.businessManagerName = businessManagerName;
	}

	public String getTeamManagerNo() {
		return teamManagerNo;
	}

	public void setTeamManagerNo(String teamManagerNo) {
		this.teamManagerNo = teamManagerNo;
	}

	public String getTeamManagerName() {
		return teamManagerName;
	}

	public void setTeamManagerName(String teamManagerName) {
		this.teamManagerName = teamManagerName;
	}

	public String getOrgManagerNo() {
		return orgManagerNo;
	}

	public void setOrgManagerNo(String orgManagerNo) {
		this.orgManagerNo = orgManagerNo;
	}

	public String getOrgManagerName() {
		return orgManagerName;
	}

	public void setOrgManagerName(String orgManagerName) {
		this.orgManagerName = orgManagerName;
	}

	public String getAreaManagerNo() {
		return areaManagerNo;
	}

	public void setAreaManagerNo(String areaManagerNo) {
		this.areaManagerNo = areaManagerNo;
	}

	public String getAreaManagerName() {
		return areaManagerName;
	}

	public void setAreaManagerName(String areaManagerName) {
		this.areaManagerName = areaManagerName;
	}

	public String getWfEmpNo() {
		return wfEmpNo;
	}

	public void setWfEmpNo(String wfEmpNo) {
		this.wfEmpNo = wfEmpNo;
	}

	public String getWfEmpName() {
		return wfEmpName;
	}

	public void setWfEmpName(String wfEmpName) {
		this.wfEmpName = wfEmpName;
	}

	public String getXsEmpNo() {
		return xsEmpNo;
	}

	public void setXsEmpNo(String xsEmpNo) {
		this.xsEmpNo = xsEmpNo;
	}

	public String getXsEmpName() {
		return xsEmpName;
	}

	public void setXsEmpName(String xsEmpName) {
		this.xsEmpName = xsEmpName;
	}

	public String getKfEmpNo() {
		return kfEmpNo;
	}

	public void setKfEmpNo(String kfEmpNo) {
		this.kfEmpNo = kfEmpNo;
	}

	public String getKfEmpName() {
		return kfEmpName;
	}

	public void setKfEmpName(String kfEmpName) {
		this.kfEmpName = kfEmpName;
	}

	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getTeamNo() {
		return teamNo;
	}

	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getBeOverdueNum() {
		return beOverdueNum;
	}

	public void setBeOverdueNum(String beOverdueNum) {
		this.beOverdueNum = beOverdueNum;
	}

	public String getGzYm() {
		return gzYm;
	}

	public void setGzYm(String gzYm) {
		this.gzYm = gzYm;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreatorNo() {
		return creatorNo;
	}

	public void setCreatorNo(String creatorNo) {
		this.creatorNo = creatorNo;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getModifierNo() {
		return modifierNo;
	}

	public void setModifierNo(String modifierNo) {
		this.modifierNo = modifierNo;
	}

}
