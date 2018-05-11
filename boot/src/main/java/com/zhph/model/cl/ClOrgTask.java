package com.zhph.model.cl;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClOrgTask implements Serializable{

	private static final long serialVersionUID = 7298241674538212927L;
	
	private Long id;// 主键ID
	private String salesTarget;//销售任务
	private String deptNo;//分公司
	private String gzYm;//工资年月
	private String createName; //创建人
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date createDate; //创建时间
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSalesTarget() {
		return salesTarget;
	}
	public void setSalesTarget(String salesTarget) {
		this.salesTarget = salesTarget;
	}
	
	
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getGzYm() {
		return gzYm;
	}
	public void setGzYm(String gzYm) {
		this.gzYm = gzYm;
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
