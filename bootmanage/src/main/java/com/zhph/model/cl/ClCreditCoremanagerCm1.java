package com.zhph.model.cl;

import java.math.BigDecimal;

public class ClCreditCoremanagerCm1 {
	private BigDecimal priNumber;

	private String orgName;

	private BigDecimal qxl;

	private String gzYm;

	private String createTime;

	private String orgNo;

	public BigDecimal getPriNumber() {
		return priNumber;
	}

	public void setPriNumber(BigDecimal priNumber) {
		this.priNumber = priNumber;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName == null ? null : orgName.trim();
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

	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo == null ? null : orgNo.trim();
	}
}