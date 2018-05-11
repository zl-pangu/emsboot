package com.zhph.model.hqclcf;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class HqclcfJobTransfer implements Serializable {

    private static final long serialVersionUID = 9220614326551317169L;

    private Long priNumber;    //唯一ID
    private String empNo;    //员工编号
    private String empName;    //员工姓名
    private String oldDeptNo;    //原部门
    private Integer oldBusinessLine;//原业务条线
    private String oldPost;//原岗位
    private String oldPosition;//原职务
    private String oldRank;//原职级
    private String deptNo;    //现部门
    private Integer businessLine;//现业务条线
    private String post;//现岗位 id
    private String position;//现职务
    private String rank;//现职级
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date leaveDate;//离职日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date oldEntryTime;//原入职时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date enterDate;//现入职时间
    private String gzYm;//工资年月
    private String empSubject;  //员工主体 
    private String comments;//描述
    private String createName; //创建人
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date createDate; //创建时间
    private String updateName; //修改人
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date updateDate; //修改时间
    
    private String updator; //修改人（对应员工表字段）
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date updatetime; //修改时间（对应员工表字段）
    
    private Integer status;//员工状态
    /*新增原职务名称及先职务名称*/
    private String oldDeptName;    //原部门
    private String oldPostName;//原岗位
    private String oldPositionName;//原职务
    private String oldRankName;//原职级
    private String deptName;    //现部门
    private String postName;//现岗位
    private String positionName;//现职务
    private String rankName;//现职级

    private String qx;// 是否显示现下级部门
    
   	private String oldqx;//是否显示原下级部门 
    
    private Integer showChild;//是否显示现部门的下级部门
    private Integer showOldChild;//是否显示原部门的下级部门

    private List<HqclcfDept> deptList;
    private List<HqclcfDept> olddeptList;

    private Long deptHid;// 现部门id
    
    private Long postHid;// 现岗位id
    
    private Long olddeptHid;// 原部门id
    
	private String loginUserId; //当前登录用户Id
    
	private String deptPname;// 现上级部门-部门
	
	private String oldDeptPname;// 原上级部门-部门
	
    private String operationType;
    
    private Integer businessLineHid;// 业务条线

    
    
    public Integer getBusinessLineHid() {
		return businessLineHid;
	}

	public void setBusinessLineHid(Integer businessLineHid) {
		this.businessLineHid = businessLineHid;
	}

	public String getOldDeptPname() {
		return oldDeptPname;
	}

	public void setOldDeptPname(String oldDeptPname) {
		this.oldDeptPname = oldDeptPname;
	}

	public String getDeptPname() {
		return deptPname;
	}

	public void setDeptPname(String deptPname) {
		this.deptPname = deptPname;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public Long getPostHid() {
		return postHid;
	}

	public void setPostHid(Long postHid) {
		this.postHid = postHid;
	}

	public String getQx() {
		return qx;
	}

	public void setQx(String qx) {
		this.qx = qx;
	}

	public String getOldqx() {
		return oldqx;
	}

	public void setOldqx(String oldqx) {
		this.oldqx = oldqx;
	}

	public Long getDeptHid() {
		return deptHid;
	}

	public void setDeptHid(Long deptHid) {
		this.deptHid = deptHid;
	}

	public Long getOlddeptHid() {
		return olddeptHid;
	}

	public void setOlddeptHid(Long olddeptHid) {
		this.olddeptHid = olddeptHid;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}

	public List<HqclcfDept> getOlddeptList() {
        return olddeptList;
    }

    public void setOlddeptList(List<HqclcfDept> olddeptList) {
        this.olddeptList = olddeptList;
    }

    public List<HqclcfDept> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<HqclcfDept> deptList) {
        this.deptList = deptList;
    }

    public Integer getShowOldChild() {
        return showOldChild;
    }

    public void setShowOldChild(Integer showOldChild) {
        this.showOldChild = showOldChild;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getOldDeptNo() {
        return oldDeptNo;
    }

    public void setOldDeptNo(String oldDeptNo) {
        this.oldDeptNo = oldDeptNo;
    }

    public Integer getOldBusinessLine() {
        return oldBusinessLine;
    }

    public void setOldBusinessLine(Integer oldBusinessLine) {
        this.oldBusinessLine = oldBusinessLine;
    }

    public String getOldPost() {
        return oldPost;
    }

    public void setOldPost(String oldPost) {
        this.oldPost = oldPost;
    }

    public String getOldPosition() {
        return oldPosition;
    }

    public void setOldPosition(String oldPosition) {
        this.oldPosition = oldPosition;
    }

    public String getOldRank() {
        return oldRank;
    }

    public void setOldRank(String oldRank) {
        this.oldRank = oldRank;
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

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Date getOldEntryTime() {
        return oldEntryTime;
    }

    public void setOldEntryTime(Date oldEntryTime) {
        this.oldEntryTime = oldEntryTime;
    }

    public String getGzYm() {
        return gzYm;
    }

    public void setGzYm(String gzYm) {
        this.gzYm = gzYm;
    }

    public String getEmpSubject() {
        return empSubject;
    }

    public void setEmpSubject(String empSubject) {
        this.empSubject = empSubject;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }

    public String getOldDeptName() {
        return oldDeptName;
    }

    public void setOldDeptName(String oldDeptName) {
        this.oldDeptName = oldDeptName;
    }

    public String getOldPostName() {
        return oldPostName;
    }

    public void setOldPostName(String oldPostName) {
        this.oldPostName = oldPostName;
    }

    public String getOldPositionName() {
        return oldPositionName;
    }

    public void setOldPositionName(String oldPositionName) {
        this.oldPositionName = oldPositionName;
    }

    public String getOldRankName() {
        return oldRankName;
    }

    public void setOldRankName(String oldRankName) {
        this.oldRankName = oldRankName;
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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
