package com.zhph.model.hqclcf;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HqclcfLeave implements Serializable {

    private static final long serialVersionUID = 7860965201965781323L;

    private Long priNumber;    //唯一ID
    private String empNo;    //员工编号
    private String empName;    //员工姓名
    private String empSubject;  //员工主体 
    private String deptNo;    //部门
    private String deptName; //部门名称
    private Integer businessLine;//业务条线
    private String post;//岗位
    private String postName;//岗位名称
    private String position;//职务
    private String positionName;//职务名称
    private String rank;//职级
    private String rankName;//职级名称
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date entryTime;//入职时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date exitTime;//离职/淘汰日期
    private String gzYm;//工资年月
    private String leavingReason;//离职/淘汰原因
    private String appStatus;//审批结果
    private Integer status;//状态
    private String appOpinions;//审批意见
    private String approverName;//审批人
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date approverTime;//审批时间
    private String createName; //创建人
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date createDate; //创建时间
    private String updateName; //修改人
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date updateDate; //修改时间
    private String remark; //备注

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date exitTimeMin;//工资年月下限
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date exitTimeMax;//工资年月下限

    private Integer showChild;//是否显示下级部门,1:是，0：否

    private List<HqclcfDept> deptList;
    
    
    private String loginUserId; //当前登录用户Id
    
    private Long deptHid;// 部门id
    
    private String deptPname;// 上级部门-部门

    

	public String getDeptPname() {
		return deptPname;
	}

	public void setDeptPname(String deptPname) {
		this.deptPname = deptPname;
	}

	public Long getDeptHid() {
		return deptHid;
	}

	public void setDeptHid(Long deptHid) {
		this.deptHid = deptHid;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
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

    public String getEmpSubject() {
        return empSubject;
    }

    public void setEmpSubject(String empSubject) {
        this.empSubject = empSubject;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public Integer getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(Integer businessLine) {
        this.businessLine = businessLine;
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

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getExitTime() {
        return exitTime;
    }

    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }

    public String getGzYm() {
        return gzYm;
    }

    public void setGzYm(String gzYm) {
        this.gzYm = gzYm;
    }

    public String getLeavingReason() {
        return leavingReason;
    }

    public void setLeavingReason(String leavingReason) {
        this.leavingReason = leavingReason;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAppOpinions() {
        return appOpinions;
    }

    public void setAppOpinions(String appOpinions) {
        this.appOpinions = appOpinions;
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

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getExitTimeMin() {
        return exitTimeMin;
    }

    public void setExitTimeMin(Date exitTimeMin) {
        this.exitTimeMin = exitTimeMin;
    }

    public Date getExitTimeMax() {
        return exitTimeMax;
    }

    public void setExitTimeMax(Date exitTimeMax) {
        this.exitTimeMax = exitTimeMax;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public Date getApproverTime() {
        return approverTime;
    }

    public void setApproverTime(Date approverTime) {
        this.approverTime = approverTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public Integer getShowChild() {
        return showChild;
    }

    public void setShowChild(Integer showChild) {
        this.showChild = showChild;
    }

    public List<HqclcfDept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<HqclcfDept> deptList) {
        this.deptList = deptList;
    }
}
