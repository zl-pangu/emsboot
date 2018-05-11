package com.zhph.model.cl;

import java.math.BigDecimal;

public class ClCreditBranchCm1 {
	private BigDecimal priNumber;

	private String branchName;

	private BigDecimal qxl;

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

	public BigDecimal getQxl() {
		return qxl;
	}

	public void setQxl(BigDecimal qxl) {
		this.qxl = qxl;
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