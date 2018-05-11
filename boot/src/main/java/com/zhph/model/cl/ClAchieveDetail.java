package com.zhph.model.cl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 业绩明细表
 * @author roilat-D
 *
 */
public class ClAchieveDetail implements Serializable {
	private static final long serialVersionUID = 83717798241182339L;

	private Long priNumber;// 主键序列
	private String loanContractNo;// 合同编号
	private String loanName;// 贷款人姓名
	private BigDecimal loanAmount;// 合同额
	private BigDecimal grantLoanAmount;// 放款额
	private String grantLoanDate;// 约定放款日
	private String payDate;// 实际放款日
	private String businessManagerNo;// 业务经理编号
	private String teamManagerNo;// 团队经理编号
	private String orgManagerNo;// 门店经理编号
	private String areaManagerNo;// 区域经理编号
	private String wfEmpNo;// 外访专员编号
	private String xsEmpNo;// 信审专员编号
	private String kfEmpNo;// 客服专员编号
	private String curDeptNo;// 部门编号:只存放当前所有部门即可
	private String gzym;// 工资年月
	private Date createTime;// 创建时间
	private String creatorNo;// 创建者
	private Date updateTime;// 修改时间
	private String modifierNo;// 修改者

	// 非数据库表字段
	private String businessManagerName;// 业务经理姓名
	private String teamManagerName;// 团队经理姓名
	private String orgManagerName;// 门店经理姓名
	private String areaManagerName;// 区域经理姓名
	private String wfEmpName;// 外访专员姓名
	private String xsEmpName;// 信审专员姓名
	private String kfEmpName;// 客服专员姓名
	private String areaName;// 区域名称
	private String orgName;// 分公司名称
	private String salesName;//营业部名称
	private String deptName;// 部门名称
	private String teamName;// 团队名称
	private String areaNo;// 区域编号
	private String orgNo;// 分公司编号
	private String salesNo;// 营业部编号
	private String deptNo;//部门编号
	private String teamNo;// 团队编号
	private String deptInfo;//部门信息：把所有parent部门信息拼接起来

	private String creatorName;// 创建者
	private String modifierName;// 修改者

	//查询条件
	private String gzymMin;// 工资年月下限
	private String gzymMax;// 工资年月上限
	private List<String> deptNos;//部门编号

	private Integer page;// 页码
	private Integer rows;// 页大小

	private String sort;// 排序字段
	private String order;// desc|asc
	public Long getPriNumber() {
		return priNumber;
	}
	public void setPriNumber(Long priNumber) {
		this.priNumber = priNumber;
	}
	public String getLoanContractNo() {
		return loanContractNo;
	}
	public void setLoanContractNo(String loanContractNo) {
		this.loanContractNo = loanContractNo;
	}
	public String getLoanName() {
		return loanName;
	}
	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}
	public BigDecimal getLoanAmount() {
		return loanAmount;
	}
	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}
	public BigDecimal getGrantLoanAmount() {
		return grantLoanAmount;
	}
	public void setGrantLoanAmount(BigDecimal grantLoanAmount) {
		this.grantLoanAmount = grantLoanAmount;
	}
	public String getGrantLoanDate() {
		return grantLoanDate;
	}
	public void setGrantLoanDate(String grantLoanDate) {
		this.grantLoanDate = grantLoanDate;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getBusinessManagerNo() {
		return businessManagerNo;
	}
	public void setBusinessManagerNo(String businessManagerNo) {
		this.businessManagerNo = businessManagerNo;
	}
	public String getTeamManagerNo() {
		return teamManagerNo;
	}
	public void setTeamManagerNo(String teamManagerNo) {
		this.teamManagerNo = teamManagerNo;
	}
	public String getOrgManagerNo() {
		return orgManagerNo;
	}
	public void setOrgManagerNo(String orgManagerNo) {
		this.orgManagerNo = orgManagerNo;
	}
	public String getAreaManagerNo() {
		return areaManagerNo;
	}
	public void setAreaManagerNo(String areaManagerNo) {
		this.areaManagerNo = areaManagerNo;
	}
	public String getWfEmpNo() {
		return wfEmpNo;
	}
	public void setWfEmpNo(String wfEmpNo) {
		this.wfEmpNo = wfEmpNo;
	}
	public String getXsEmpNo() {
		return xsEmpNo;
	}
	public void setXsEmpNo(String xsEmpNo) {
		this.xsEmpNo = xsEmpNo;
	}
	public String getKfEmpNo() {
		return kfEmpNo;
	}
	public void setKfEmpNo(String kfEmpNo) {
		this.kfEmpNo = kfEmpNo;
	}
	public String getCurDeptNo() {
		return curDeptNo;
	}
	public void setCurDeptNo(String curDeptNo) {
		this.curDeptNo = curDeptNo;
	}
	public String getGzym() {
		return gzym;
	}
	public void setGzym(String gzym) {
		this.gzym = gzym;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreatorNo() {
		return creatorNo;
	}
	public void setCreatorNo(String creatorNo) {
		this.creatorNo = creatorNo;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getModifierNo() {
		return modifierNo;
	}
	public void setModifierNo(String modifierNo) {
		this.modifierNo = modifierNo;
	}
	public String getBusinessManagerName() {
		return businessManagerName;
	}
	public void setBusinessManagerName(String businessManagerName) {
		this.businessManagerName = businessManagerName;
	}
	public String getTeamManagerName() {
		return teamManagerName;
	}
	public void setTeamManagerName(String teamManagerName) {
		this.teamManagerName = teamManagerName;
	}
	public String getOrgManagerName() {
		return orgManagerName;
	}
	public void setOrgManagerName(String orgManagerName) {
		this.orgManagerName = orgManagerName;
	}
	public String getAreaManagerName() {
		return areaManagerName;
	}
	public void setAreaManagerName(String areaManagerName) {
		this.areaManagerName = areaManagerName;
	}
	public String getWfEmpName() {
		return wfEmpName;
	}
	public void setWfEmpName(String wfEmpName) {
		this.wfEmpName = wfEmpName;
	}
	public String getXsEmpName() {
		return xsEmpName;
	}
	public void setXsEmpName(String xsEmpName) {
		this.xsEmpName = xsEmpName;
	}
	public String getKfEmpName() {
		return kfEmpName;
	}
	public void setKfEmpName(String kfEmpName) {
		this.kfEmpName = kfEmpName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getSalesName() {
		return salesName;
	}
	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getAreaNo() {
		return areaNo;
	}
	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}
	public String getOrgNo() {
		return orgNo;
	}
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
	public String getSalesNo() {
		return salesNo;
	}
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getTeamNo() {
		return teamNo;
	}
	public void setTeamNo(String teamNo) {
		this.teamNo = teamNo;
	}
	public String getDeptInfo() {
		return deptInfo;
	}
	public void setDeptInfo(String deptInfo) {
		this.deptInfo = deptInfo;
	}
	public String getCreatorName() {
		return creatorName;
	}
	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}
	public String getModifierName() {
		return modifierName;
	}
	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
	public String getGzymMin() {
		return gzymMin;
	}
	public void setGzymMin(String gzymMin) {
		this.gzymMin = gzymMin;
	}
	public String getGzymMax() {
		return gzymMax;
	}
	public void setGzymMax(String gzymMax) {
		this.gzymMax = gzymMax;
	}
	public List<String> getDeptNos() {
		return deptNos;
	}
	public void setDeptNos(List<String> deptNos2) {
		this.deptNos = deptNos2;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}

}
