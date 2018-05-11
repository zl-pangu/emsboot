package com.zhph.model.cl;

import java.math.BigDecimal;

public class ClCreditBranchRcl {
	private BigDecimal priNumber;

	private String branchName;

	private BigDecimal rcl;

	private String gzYm;

	private String createTime;

	public BigDecimal getPriNumber() {
		return priNumber;
	}

	public void setPriNumber(BigDecimal priNumber) {
		this.priNumber = priNumber;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName == null ? null : branchName.trim();
	}

	public BigDecimal getRcl() {
		return rcl;
	}

	public void setRcl(BigDecimal rcl) {
		this.rcl = rcl;
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
}