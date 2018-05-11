package com.zhph.model.cl;

import java.math.BigDecimal;

public class ClSalaryP2pOrg {

	private BigDecimal priNumber;

	private String no;

	private String name;

	private String status;

	private String orgDesc;

	private String createName;

	private String createDate;

	private String isHqPiEnter;

	private String province;

	private String isHqPiEnter60;

	private String categorynum;

	private String minWageStandard;

	private String region;

	public BigDecimal getPriNumber() {
		return priNumber;
	}

	public void setPriNumber(BigDecimal priNumber) {
		this.priNumber = priNumber;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no == null ? null : no.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public String getOrgDesc() {
		return orgDesc;
	}

	public void setOrgDesc(String orgDesc) {
		this.orgDesc = orgDesc == null ? null : orgDesc.trim();
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName == null ? null : createName.trim();
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate == null ? null : createDate.trim();
	}

	public String getIsHqPiEnter() {
		return isHqPiEnter;
	}

	public void setIsHqPiEnter(String isHqPiEnter) {
		this.isHqPiEnter = isHqPiEnter == null ? null : isHqPiEnter.trim();
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province == null ? null : province.trim();
	}

	public String getIsHqPiEnter60() {
		return isHqPiEnter60;
	}

	public void setIsHqPiEnter60(String isHqPiEnter60) {
		this.isHqPiEnter60 = isHqPiEnter60 == null ? null : isHqPiEnter60.trim();
	}

	public String getCategorynum() {
		return categorynum;
	}

	public void setCategorynum(String categorynum) {
		this.categorynum = categorynum == null ? null : categorynum.trim();
	}

	public String getMinWageStandard() {
		return minWageStandard;
	}

	public void setMinWageStandard(String minWageStandard) {
		this.minWageStandard = minWageStandard == null ? null : minWageStandard.trim();
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region == null ? null : region.trim();
	}
}