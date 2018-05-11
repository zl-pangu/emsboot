package com.zhph.model.hqclcf;

import java.io.Serializable;

public class HqclcfBusiness implements Serializable {
    private String prinumber;//主键id

    private String posCode;//职务编码

    private String posName;//职务名称

    private String status;//状态

    private String creator;//创造者

    private String createTime;//创建时间

    private String updator;//修改者

    private String updateTime;//修改时间

    private String comments;//标志
    private String rankName;//职级名称
    private String rankCode;//职级编码
    

    public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	private static final long serialVersionUID = 1L;

    public String getPrinumber() {
		return prinumber;
	}

	public void setPrinumber(String prinumber) {
		this.prinumber = prinumber;
	}

	public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode == null ? null : posCode.trim();
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName == null ? null : posName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime == null ? null : updateTime.trim();
    }

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

    public String getRankCode() {
        return rankCode;
    }

    public void setRankCode(String rankCode) {
        this.rankCode = rankCode;
    }

    @Override
    public String toString() {
        return "HqclcfBusiness{" +
                "prinumber='" + prinumber + '\'' +
                ", posCode='" + posCode + '\'' +
                ", posName='" + posName + '\'' +
                ", status='" + status + '\'' +
                ", creator='" + creator + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updator='" + updator + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", comments='" + comments + '\'' +
                ", rankName='" + rankName + '\'' +
                ", rankCode='" + rankCode + '\'' +
                '}';
    }
}