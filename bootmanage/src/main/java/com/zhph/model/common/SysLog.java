package com.zhph.model.common;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhouliang on 2018/1/16.
 */
public class SysLog implements Serializable{

    private Long id;
    private String createName;
    private Date createTime;
    private String oldData;
    private String newData;
    private String operateClass;
    private OperateType operateType;

    public String getOperateClass() {
        return operateClass;
    }

    public void setOperateClass(String operateClass) {
        this.operateClass = operateClass;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOldData() {
        return oldData;
    }

    public void setOldData(String oldData) {
        this.oldData = oldData;
    }

    public String getNewData() {
        return newData;
    }

    public void setNewData(String newData) {
        this.newData = newData;
    }

    public OperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(OperateType operateType) {
        this.operateType = operateType;
    }
}
