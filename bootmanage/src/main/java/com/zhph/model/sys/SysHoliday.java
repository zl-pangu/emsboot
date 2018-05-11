package com.zhph.model.sys;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author lxp
 * @date 2017年12月19日 下午1:26:22
 * @parameter
 * @return
 */

public class SysHoliday implements Serializable {

	private String id;

	private String holidayName; // 节假日名称

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date holidayStartDate; // 节假日开始日期

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date holidayEndDate; // 节假日结束日期

	private String offLaterDate; // 调休上班日

	private String ifauto; // 是否自动生成

	private String remark; // 描述

	private String createName; // 创建人 CREATE_NAME

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date createDate; // 创建时间 CREATE_DATE

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHolidayName() {
		return holidayName;
	}

	public void setHolidayName(String holidayName) {
		this.holidayName = holidayName;
	}

	public Date getHolidayStartDate() {
		return holidayStartDate;
	}

	public void setHolidayStartDate(Date holidayStartDate) {
		this.holidayStartDate = holidayStartDate;
	}

	public Date getHolidayEndDate() {
		return holidayEndDate;
	}

	public void setHolidayEndDate(Date holidayEndDate) {
		this.holidayEndDate = holidayEndDate;
	}

	public String getOffLaterDate() {
		return offLaterDate;
	}

	public void setOffLaterDate(String offLaterDate) {
		this.offLaterDate = offLaterDate;
	}

	public String getIfauto() {
		return ifauto;
	}

	public void setIfauto(String ifauto) {
		this.ifauto = ifauto;
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

}
