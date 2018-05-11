package com.zhph.model.sys;

import java.io.Serializable;

public class SysZhphBank implements Serializable{
	
	private static final long serialVersionUID = -2719541554625664962L;
	
	private Long priNumber;//主键
    private String bankCode;//银行编码
    private String bankName;//银行名称
    private String bankFullName;//银行全称
    private String status;//状态
    private String createTime;//创建时间
    private String updateTime;//修改时间
    private String createName;//创建人
    private String updateName;//修改人
    private String remark;//备注
    
    
	public Long getPriNumber() {
		return priNumber;
	}
	public void setPriNumber(Long priNumber) {
		this.priNumber = priNumber;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankFullName() {
		return bankFullName;
	}
	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    
    
}
