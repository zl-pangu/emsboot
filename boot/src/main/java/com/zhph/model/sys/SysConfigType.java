package com.zhph.model.sys;

import java.io.Serializable;

public class SysConfigType implements Serializable{

	/** id */
	private Long id;
	/** 编码 */
	private String sysCode;
	/** 名称 */
	private String sysName;
	/** 值 */
	private Integer sysValue;
	/** 排序 */
	private Integer sort;
	/** 父级id */
	private Long pId;
	/** 父级编码 */
	private String pSysCode;
	/** 描述 */
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSysCode() {
		return sysCode;
	}

	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public Integer getSysValue() {
		return sysValue;
	}

	public void setSysValue(Integer sysValue) {
		this.sysValue = sysValue;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	public String getpSysCode() {
		return pSysCode;
	}

	public void setpSysCode(String pSysCode) {
		this.pSysCode = pSysCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
