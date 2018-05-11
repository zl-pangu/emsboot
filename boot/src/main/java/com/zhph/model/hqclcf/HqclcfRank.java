package com.zhph.model.hqclcf;

import java.io.Serializable;

public class HqclcfRank implements Serializable {
    private String prinumber;//主键

    private String no;//职级编号

    private String name;//职级名称

    private String status;//状态 1-启动  2-禁用

    private String createDate;//创建时间

    private String createName;//创建人

    private String comments;//描述

    private static final long serialVersionUID = 1L;

    public String getPrinumber() {
        return prinumber;
    }

    public void setPrinumber(String prinumber) {
        this.prinumber = prinumber == null ? null : prinumber.trim();
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no == null ? null : no.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }


    public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName == null ? null : createName.trim();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }
}