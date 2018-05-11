package com.zhph.model.sys;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26.
 */
public class SysMidFileType implements Serializable{
    private String oldFileType;
    private Integer newFileType;
    private Integer businessline;

    public String getOldFileType() {
        return oldFileType;
    }

    public void setOldFileType(String oldFileType) {
        this.oldFileType = oldFileType;
    }

    public Integer getNewFileType() {
        return newFileType;
    }

    public void setNewFileType(Integer newFileType) {
        this.newFileType = newFileType;
    }

    public Integer getBusinessline() {
        return businessline;
    }

    public void setBusinessline(Integer businessline) {
        this.businessline = businessline;
    }
}
