package com.zhph.model.hqclcf;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HqclcfEmp implements Serializable {
	private static final long serialVersionUID = -550467832866722278L;
	private Long id;// 主键ID
	private String empNo;// 员工编号
	private String empName;// 员工姓名
	private String idcardName;// 身份证姓名
	private Integer sex;// 性别
	private Integer marriage;// 婚姻状况
	private Integer nativeType;// 户籍性质
	private String nativePlace; // 户籍所在地
	private String hjProvince;// 户籍地址
	private String hjCity;// 户籍地址对应省
	private String hjArea;// 户籍地址对应市
	private String hjxjdz;// 户籍地址对应区
	private String addr; // 现居地址
	private String xjProvince;// 现居地址对应省
	private String xjCity;// 现居地址对应市
	private String xjArea;// 现居地址对应区
	private String xjxxdz;// 现居地址详细地址
	private Integer idType;// 证件类型
	private String idCard;// 证件号码
	private String mobilePhone;// 移动电话
	private String urgencyPhone;// 紧急联系人电话
	private String urgency;// 紧急联系人
	private Integer urgencyRelation;// 紧急联系人关系
	private String email;// 邮箱
	private String companyMailbox;// 公司邮箱
	private Integer nation;// 民族
	private Integer empSubject;// 员工主体
	private Integer edu;// 学历
	private String schoolGraduation;// 毕业院校
	private String specialty;// 专业
	private Integer eduType;// 教育方式
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date lxEnterDate;// 利信入职时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date enterDate;// 入职时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date tfDate;// 转正日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date fulltimeBeginDate;// 全日制合同开始日期
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date fulltimeEndDate;// 全日制合同终止`日期
	private String workOrgNo;// 工作地
	private String socialSecurityOrgNo;// 社保购买地
	private String taxCode;// 个税编码
	private String wagesNo;// 是否协议工资
	private String wagesNum;// 协议工资金额
	private String referees;// 推荐人
	private String refereesNo;// 推荐人编号
	private Integer cityLevel;// 城市级别
	private String deptNo;// 部门
	private String qx;// 是否显示下级部门
	private String post;// 岗位
	private String position;// 职务
	private String rank;// 职级
	private String pfBankCode;// 银行卡名称
	private String pfBankNo;// 卡号
	private String openBankOrg;// 银行卡开户行地址
	private String bankProvince;// 银行卡开户行地址对应省
	private String bankCity;// 银行卡开户行地址对应市
	private String bankxxdj;// 银行卡开户行地址对应区

	private String pfBankSubbranch;// 银行卡开户支行
	private Integer empType;// 员工类型
	private String comments;// 员工备注
	private Integer status;// 在职状态
	private Integer statusApp;// 审批状态 1.通过 2拒绝 3撤销 4未审批
	private String resultsApp;// 审批结果
	private String reasonsApp;// 审批理由
	private Integer businessLine;// 业务条线
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date leaveDate;// 离职时间
	private String badCredit;// 不良信息
	private String ZHWorkExp;// 正和工作经历
	private String creator;// 创建人
	private String updator;// 修改人
	private String approver;// 审批人
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date createtime;// 创建时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date updatetime;// 修改时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date approverTime;// 审批时间

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date enterDateMin;// 最小入职时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date enterDateMax;// 最大入职时间

	/**
	 * 新增员工审批form额外参数
	 */
	private Long deptHid;// 部门id
	private Long postHid;// 岗位id
	private String zbbz;// 总部备注
	private String xjbz;// 消金备注
	private String xdbz;// 总部备注
	private Integer businessLineHid;// 业务条线

	private String operType = "";// 操作类型

	/**
	 * 查看详细 额外参数
	 */
	private String sex_name;// 性别
	private String nation_name;// 民族
	private String marriage_name;// 婚姻状况
	private String idType_name;// 证件类型
	private String nativeType_name;// 户籍性质
	private String urgencyRelation_name;// 紧急联系人关系
	private String eduType_name;// 教育方式
	private String empSubject_name;// 员工主体
	private String empType_name;// 员工类型
	private String businessLine_name;// 业务条线
	private String deptName;// 部门
	private String deptPname;// 上级部门-部门
	private String postName;// 岗位
	private String posName;// 职务
	private String rankName;// 职级
	private String workOrgNo_name;// 工作地
	private String pfBankCode_name;// 银行卡名称
	private String socialSecurityOrgNo_name;// 社保购买地
	private String wagesNo_name;// 是否协议工资
	private String cityLevel_name;// 城市级别

	private String p_name; // 大区，分中心，营业部，团队
	private String cd_name;

	/**
	 * 当前用户id
	 * 
	 * @return
	 */
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getP_name() {
		return p_name;
	}

	public void setP_name(String p_name) {
		this.p_name = p_name;
	}

	public String getCd_name() {
		return cd_name;
	}

	public void setCd_name(String cd_name) {
		this.cd_name = cd_name;
	}

	public Integer getBusinessLineHid() {
		return businessLineHid;
	}

	public void setBusinessLineHid(Integer businessLineHid) {
		this.businessLineHid = businessLineHid;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getZbbz() {
		return zbbz;
	}

	public void setZbbz(String zbbz) {
		this.zbbz = zbbz;
	}

	public String getXjbz() {
		return xjbz;
	}

	public void setXjbz(String xjbz) {
		this.xjbz = xjbz;
	}

	public String getXdbz() {
		return xdbz;
	}

	public void setXdbz(String xdbz) {
		this.xdbz = xdbz;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public String getHjProvince() {
		return hjProvince;
	}

	public void setHjProvince(String hjProvince) {
		this.hjProvince = hjProvince;
	}

	public String getHjCity() {
		return hjCity;
	}

	public void setHjCity(String hjCity) {
		this.hjCity = hjCity;
	}

	public String getHjArea() {
		return hjArea;
	}

	public void setHjArea(String hjArea) {
		this.hjArea = hjArea;
	}

	public String getHjxjdz() {
		return hjxjdz;
	}

	public void setHjxjdz(String hjxjdz) {
		this.hjxjdz = hjxjdz;
	}

	public String getXjProvince() {
		return xjProvince;
	}

	public void setXjProvince(String xjProvince) {
		this.xjProvince = xjProvince;
	}

	public String getXjCity() {
		return xjCity;
	}

	public void setXjCity(String xjCity) {
		this.xjCity = xjCity;
	}

	public String getXjArea() {
		return xjArea;
	}

	public void setXjArea(String xjArea) {
		this.xjArea = xjArea;
	}

	public String getXjxxdz() {
		return xjxxdz;
	}

	public void setXjxxdz(String xjxxdz) {
		this.xjxxdz = xjxxdz;
	}

	public Long getDeptHid() {
		return deptHid;
	}

	public void setDeptHid(Long deptHid) {
		this.deptHid = deptHid;
	}

	public Long getPostHid() {
		return postHid;
	}

	public void setPostHid(Long postHid) {
		this.postHid = postHid;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankxxdj() {
		return bankxxdj;
	}

	public void setBankxxdj(String bankxxdj) {
		this.bankxxdj = bankxxdj;
	}

	public String getCompanyMailbox() {
		return companyMailbox;
	}

	public void setCompanyMailbox(String companyMailbox) {
		this.companyMailbox = companyMailbox;
	}

	public String getIdcardName() {
		return idcardName;
	}

	public void setIdcardName(String idcardName) {
		this.idcardName = idcardName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getMarriage() {
		return marriage;
	}

	public void setMarriage(Integer marriage) {
		this.marriage = marriage;
	}

	public Integer getNativeType() {
		return nativeType;
	}

	public void setNativeType(Integer nativeType) {
		this.nativeType = nativeType;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Integer getIdType() {
		return idType;
	}

	public void setIdType(Integer idType) {
		this.idType = idType;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getUrgencyPhone() {
		return urgencyPhone;
	}

	public void setUrgencyPhone(String urgencyPhone) {
		this.urgencyPhone = urgencyPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getNation() {
		return nation;
	}

	public void setNation(Integer nation) {
		this.nation = nation;
	}

	public Integer getEmpSubject() {
		return empSubject;
	}

	public void setEmpSubject(Integer empSubject) {
		this.empSubject = empSubject;
	}

	public Integer getEdu() {
		return edu;
	}

	public void setEdu(Integer edu) {
		this.edu = edu;
	}

	public String getSchoolGraduation() {
		return schoolGraduation;
	}

	public void setSchoolGraduation(String schoolGraduation) {
		this.schoolGraduation = schoolGraduation;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public Integer getEduType() {
		return eduType;
	}

	public void setEduType(Integer eduType) {
		this.eduType = eduType;
	}

	public Date getLxEnterDate() {
		return lxEnterDate;
	}

	public void setLxEnterDate(Date lxEnterDate) {
		this.lxEnterDate = lxEnterDate;
	}

	public Date getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	public Date getTfDate() {
		return tfDate;
	}

	public void setTfDate(Date tfDate) {
		this.tfDate = tfDate;
	}

	public Date getFulltimeBeginDate() {
		return fulltimeBeginDate;
	}

	public void setFulltimeBeginDate(Date fulltimeBeginDate) {
		this.fulltimeBeginDate = fulltimeBeginDate;
	}

	public Date getFulltimeEndDate() {
		return fulltimeEndDate;
	}

	public void setFulltimeEndDate(Date fulltimeEndDate) {
		this.fulltimeEndDate = fulltimeEndDate;
	}

	public String getWorkOrgNo() {
		return workOrgNo;
	}

	public void setWorkOrgNo(String workOrgNo) {
		this.workOrgNo = workOrgNo;
	}

	public String getSocialSecurityOrgNo() {
		return socialSecurityOrgNo;
	}

	public void setSocialSecurityOrgNo(String socialSecurityOrgNo) {
		this.socialSecurityOrgNo = socialSecurityOrgNo;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getWagesNo() {
		return wagesNo;
	}

	public void setWagesNo(String wagesNo) {
		this.wagesNo = wagesNo;
	}

	public String getWagesNum() {
		return wagesNum;
	}

	public void setWagesNum(String wagesNum) {
		this.wagesNum = wagesNum;
	}

	public String getReferees() {
		return referees;
	}

	public void setReferees(String referees) {
		this.referees = referees;
	}

	public String getRefereesNo() {
		return refereesNo;
	}

	public void setRefereesNo(String refereesNo) {
		this.refereesNo = refereesNo;
	}

	public Integer getCityLevel() {
		return cityLevel;
	}

	public void setCityLevel(Integer cityLevel) {
		this.cityLevel = cityLevel;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getQx() {
		return qx;
	}

	public void setQx(String qx) {
		this.qx = qx;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getPfBankCode() {
		return pfBankCode;
	}

	public void setPfBankCode(String pfBankCode) {
		this.pfBankCode = pfBankCode;
	}

	public String getPfBankNo() {
		return pfBankNo;
	}

	public void setPfBankNo(String pfBankNo) {
		this.pfBankNo = pfBankNo;
	}

	public String getOpenBankOrg() {
		return openBankOrg;
	}

	public void setOpenBankOrg(String openBankOrg) {
		this.openBankOrg = openBankOrg;
	}

	public String getPfBankSubbranch() {
		return pfBankSubbranch;
	}

	public void setPfBankSubbranch(String pfBankSubbranch) {
		this.pfBankSubbranch = pfBankSubbranch;
	}

	public Integer getEmpType() {
		return empType;
	}

	public void setEmpType(Integer empType) {
		this.empType = empType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatusApp() {
		return statusApp;
	}

	public void setStatusApp(Integer statusApp) {
		this.statusApp = statusApp;
	}

	public String getResultsApp() {
		return resultsApp;
	}

	public void setResultsApp(String resultsApp) {
		this.resultsApp = resultsApp;
	}

	public String getReasonsApp() {
		return reasonsApp;
	}

	public void setReasonsApp(String reasonsApp) {
		this.reasonsApp = reasonsApp;
	}

	public Integer getBusinessLine() {
		return businessLine;
	}

	public void setBusinessLine(Integer businessLine) {
		this.businessLine = businessLine;
	}

	public Date getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getBadCredit() {
		return badCredit;
	}

	public void setBadCredit(String badCredit) {
		this.badCredit = badCredit;
	}

	public String getZHWorkExp() {
		return ZHWorkExp;
	}

	public void setZHWorkExp(String ZHWorkExp) {
		this.ZHWorkExp = ZHWorkExp;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Date getApproverTime() {
		return approverTime;
	}

	public void setApproverTime(Date approverTime) {
		this.approverTime = approverTime;
	}

	public Date getEnterDateMin() {
		return enterDateMin;
	}

	public void setEnterDateMin(Date enterDateMin) {
		this.enterDateMin = enterDateMin;
	}

	public Date getEnterDateMax() {
		return enterDateMax;
	}

	public void setEnterDateMax(Date enterDateMax) {
		this.enterDateMax = enterDateMax;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getUrgencyRelation() {
		return urgencyRelation;
	}

	public void setUrgencyRelation(Integer urgencyRelation) {
		this.urgencyRelation = urgencyRelation;
	}

	public String getSex_name() {
		return sex_name;
	}

	public void setSex_name(String sex_name) {
		this.sex_name = sex_name;
	}

	public String getNation_name() {
		return nation_name;
	}

	public void setNation_name(String nation_name) {
		this.nation_name = nation_name;
	}

	public String getMarriage_name() {
		return marriage_name;
	}

	public void setMarriage_name(String marriage_name) {
		this.marriage_name = marriage_name;
	}

	public String getIdType_name() {
		return idType_name;
	}

	public void setIdType_name(String idType_name) {
		this.idType_name = idType_name;
	}

	public String getNativeType_name() {
		return nativeType_name;
	}

	public void setNativeType_name(String nativeType_name) {
		this.nativeType_name = nativeType_name;
	}

	public String getUrgencyRelation_name() {
		return urgencyRelation_name;
	}

	public void setUrgencyRelation_name(String urgencyRelation_name) {
		this.urgencyRelation_name = urgencyRelation_name;
	}

	public String getEduType_name() {
		return eduType_name;
	}

	public void setEduType_name(String eduType_name) {
		this.eduType_name = eduType_name;
	}

	public String getEmpSubject_name() {
		return empSubject_name;
	}

	public void setEmpSubject_name(String empSubject_name) {
		this.empSubject_name = empSubject_name;
	}

	public String getEmpType_name() {
		return empType_name;
	}

	public void setEmpType_name(String empType_name) {
		this.empType_name = empType_name;
	}

	public String getBusinessLine_name() {
		return businessLine_name;
	}

	public void setBusinessLine_name(String businessLine_name) {
		this.businessLine_name = businessLine_name;
	}

	public String getDeptPname() {
		return deptPname;
	}

	public void setDeptPname(String deptPname) {
		this.deptPname = deptPname;
	}

	public String getPostName() {
		return postName;
	}

	public void setPostName(String postName) {
		this.postName = postName;
	}

	public String getPosName() {
		return posName;
	}

	public void setPosName(String posName) {
		this.posName = posName;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public String getWorkOrgNo_name() {
		return workOrgNo_name;
	}

	public void setWorkOrgNo_name(String workOrgNo_name) {
		this.workOrgNo_name = workOrgNo_name;
	}

	public String getPfBankCode_name() {
		return pfBankCode_name;
	}

	public void setPfBankCode_name(String pfBankCode_name) {
		this.pfBankCode_name = pfBankCode_name;
	}

	public String getSocialSecurityOrgNo_name() {
		return socialSecurityOrgNo_name;
	}

	public void setSocialSecurityOrgNo_name(String socialSecurityOrgNo_name) {
		this.socialSecurityOrgNo_name = socialSecurityOrgNo_name;
	}

	public String getWagesNo_name() {
		return wagesNo_name;
	}

	public void setWagesNo_name(String wagesNo_name) {
		this.wagesNo_name = wagesNo_name;
	}

	public String getCityLevel_name() {
		return cityLevel_name;
	}

	public void setCityLevel_name(String cityLevel_name) {
		this.cityLevel_name = cityLevel_name;
	}

}
