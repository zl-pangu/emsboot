package com.zhph.model.cl;

import java.math.BigDecimal;

public class ClCreditCoremanagerM2Det {
	private BigDecimal priNumber;

	private String loanContractNo;

	private String loanName;

	private String empNo;

	private String empName;

	private String teamManagerNo;

	private String teamManager;

	private String cityManagerNo;

	private String cityManager;

	private String gegionManagerNo;

	private String gegionManager;

	private String payDate;

	private Integer overdueDays;

	private Integer loanAmount;

	private Integer grantLoanAmount;

	private String org;

	private String gzYm;

	private String createTime;

	private String orgno;

	public BigDecimal getPriNumber() {
		return priNumber;
	}

	public void setPriNumber(BigDecimal priNumber) {
		this.priNumber = priNumber;
	}

	public String getLoanContractNo() {
		return loanContractNo;
	}

	public void setLoanContractNo(String loanContractNo) {
		this.loanContractNo = loanContractNo == null ? null : loanContractNo.trim();
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName == null ? null : loanName.trim();
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo == null ? null : empNo.trim();
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName == null ? null : empName.trim();
	}

	public String getTeamManagerNo() {
		return teamManagerNo;
	}

	public void setTeamManagerNo(String teamManagerNo) {
		this.teamManagerNo = teamManagerNo == null ? null : teamManagerNo.trim();
	}

	public String getTeamManager() {
		return teamManager;
	}

	public void setTeamManager(String teamManager) {
		this.teamManager = teamManager == null ? null : teamManager.trim();
	}

	public String getCityManagerNo() {
		return cityManagerNo;
	}

	public void setCityManagerNo(String cityManagerNo) {
		this.cityManagerNo = cityManagerNo == null ? null : cityManagerNo.trim();
	}

	public String getCityManager() {
		return cityManager;
	}

	public void setCityManager(String cityManager) {
		this.cityManager = cityManager == null ? null : cityManager.trim();
	}

	public String getGegionManagerNo() {
		return gegionManagerNo;
	}

	public void setGegionManagerNo(String gegionManagerNo) {
		this.gegionManagerNo = gegionManagerNo == null ? null : gegionManagerNo.trim();
	}

	public String getGegionManager() {
		return gegionManager;
	}

	public void setGegionManager(String gegionManager) {
		this.gegionManager = gegionManager == null ? null : gegionManager.trim();
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate == null ? null : payDate.trim();
	}

	public Integer getOverdueDays() {
		return overdueDays;
	}

	public void setOverdueDays(int i) {
		this.overdueDays = i;
	}

	public Integer getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Integer loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Integer getGrantLoanAmount() {
		return grantLoanAmount;
	}

	public void setGrantLoanAmount(Integer grantLoanAmount) {
		this.grantLoanAmount = grantLoanAmount;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org == null ? null : org.trim();
	}

	public String getGzYm() {
		return gzYm;
	}

	public void setGzYm(String gzYm) {
		this.gzYm = gzYm == null ? null : gzYm.trim();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime == null ? null : createTime.trim();
	}

	public String getOrgno() {
		return orgno;
	}

	public void setOrgno(String orgno) {
		this.orgno = orgno == null ? null : orgno.trim();
	}
}