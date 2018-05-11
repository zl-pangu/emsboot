package com.zhph.model.cf;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CfOfflaterSet {
	private String id;

	private String cosMonth;

	private String cosWorkdayDays;

	private String cosWeekendDays;

	private String remark;

	private String createName;

	private String cosMonth_s;

	private String cosMonth_e;

	private String result;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCosMonth() {
		return cosMonth;
	}

	public void setCosMonth(String cosMonth) {
		this.cosMonth = cosMonth;
	}

	public String getCosWorkdayDays() {
		return cosWorkdayDays;
	}

	public void setCosWorkdayDays(String cosWorkdayDays) {
		this.cosWorkdayDays = cosWorkdayDays;
	}

	public String getCosWeekendDays() {
		return cosWeekendDays;
	}

	public void setCosWeekendDays(String cosWeekendDays) {
		this.cosWeekendDays = cosWeekendDays;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCosMonth_s() {
		return cosMonth_s;
	}

	public void setCosMonth_s(String cosMonth_s) {
		this.cosMonth_s = cosMonth_s;
	}

	public String getCosMonth_e() {
		return cosMonth_e;
	}

	public void setCosMonth_e(String cosMonth_e) {
		this.cosMonth_e = cosMonth_e;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}