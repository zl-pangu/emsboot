package com.zhph.model.cf;

import java.io.Serializable;
import java.util.List;

/**
 * 请休假管理
 */
public class CfVacateManage implements Serializable {

	/**
	 * @Fields serialVersionUID : 
	 */
	private static final long serialVersionUID = 1L;

	private Long priNumber;
	private String empNo; // 员工编号
	private String empName; // 员工姓名
	private String regionId; // 大区编码 数据库不做保存
	private String filialeId; // 分公司编码 数据库不做保存
	private String businessDeptId; // 营业部编码 数据库不做保存
	private String teamId; // 团队编码 数据库不做保存
	private String deptId; // 部门编码
	private String leaveType; // 请假类型 1:事假 2:病假 3：年休假 4.婚假 5.丧假 6.工伤假 7.陪产假 8.产假 9.公休假 10.产检假
	private String leaveInterval; // 请假时间-区间
	private String startTime; // 请假开始时间
	private String endTime; // 请假结束时间
	private String totalTime; // 总共请假时间
	private String startTimecorspMonth; // 请假开始时间对应的月份
	private String endTimecorspMonth; // 请假结束时间对应的月份
	private String dayBystart; // 请假开始时间对应的月份的合计时间
	private String dayByend; // 请假开始时间对应的月份的合计时间
	private String leaveReason; // 请假原因
	private String status; // 状态 →未提交 :1、未审批:2、通过:3、拒绝:4。
	private String post; // 职务
	private String auditOpinion; // 审核意见
	private String dateofLeave; // 请假的操作时间
	private String rankType; // 职级类别
	private String createuser;
	private String salesCreateFlag; // 标记营业部创建城市经理；
	
	/*********************query parameters******************************/
	private String gzYm;// 年月
	private String businessDeptFlag; // 营业部登录的时候标记下(营业部经理登录时需要查询分公司中的城市分公司经理员工)
	private String filialePostNo;//分中心经理的职务编号(营业部经理登录时需要查询分公司中的城市分公司经理员工)
	private String parentDeptId;//营业部登录的时候查询条件需要查询出rootDeptId,即分中心(营业部经理登录时需要查询分公司中的城市分公司经理员工)
	private String[] leaveTypes;//请假类型数组
	private List<Long> deptIds;//部门权限数组
	
	/*********************query parameters******************************/
	
	/***************************额外需要查询的参数*****************************************/
	private String postName;//职务名称
	/***************************额外需要查询的参数*****************************************/

	
	public String getPostName() {
		return postName;
	}

	public String getFilialePostNo() {
		return filialePostNo;
	}

	public void setFilialePostNo(String filialePostNo) {
		this.filialePostNo = filialePostNo;
	}

	public List<Long> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(List<Long> deptIds) {
		this.deptIds = deptIds;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public Long getPriNumber() {
		return priNumber;
	}

	public void setPriNumber(Long priNumber) {
		this.priNumber = priNumber;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getFilialeId() {
		return filialeId;
	}

	public void setFilialeId(String filialeId) {
		this.filialeId = filialeId;
	}

	public String getBusinessDeptId() {
		return businessDeptId;
	}

	public void setBusinessDeptId(String businessDeptId) {
		this.businessDeptId = businessDeptId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getLeaveInterval() {
		return leaveInterval;
	}

	public void setLeaveInterval(String leaveInterval) {
		this.leaveInterval = leaveInterval;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getStartTimecorspMonth() {
		return startTimecorspMonth;
	}

	public void setStartTimecorspMonth(String startTimecorspMonth) {
		this.startTimecorspMonth = startTimecorspMonth;
	}

	public String getEndTimecorspMonth() {
		return endTimecorspMonth;
	}

	public void setEndTimecorspMonth(String endTimecorspMonth) {
		this.endTimecorspMonth = endTimecorspMonth;
	}

	public String getDayBystart() {
		return dayBystart;
	}

	public void setDayBystart(String dayBystart) {
		this.dayBystart = dayBystart;
	}

	public String getDayByend() {
		return dayByend;
	}

	public void setDayByend(String dayByend) {
		this.dayByend = dayByend;
	}

	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getRankType() {
		return rankType;
	}

	public void setRankType(String rankType) {
		this.rankType = rankType;
	}

	public String getGzYm() {
		return gzYm;
	}

	public void setGzYm(String gzYm) {
		this.gzYm = gzYm;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}

	public String getDateofLeave() {
		return dateofLeave;
	}

	public void setDateofLeave(String dateofLeave) {
		this.dateofLeave = dateofLeave;
	}

	public String getBusinessDeptFlag() {
		return businessDeptFlag;
	}

	public void setBusinessDeptFlag(String businessDeptFlag) {
		this.businessDeptFlag = businessDeptFlag;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getSalesCreateFlag() {
		return salesCreateFlag;
	}

	public void setSalesCreateFlag(String salesCreateFlag) {
		this.salesCreateFlag = salesCreateFlag;
	}
	
	public String getParentDeptId() {
		return parentDeptId;
	}

	public void setParentDeptId(String parentDeptId) {
		this.parentDeptId = parentDeptId;
	}

	public String[] getLeaveTypes() {
		return leaveTypes;
	}

	public void setLeaveTypes(String[] leaveTypes) {
		this.leaveTypes = leaveTypes;
	}
	
}
