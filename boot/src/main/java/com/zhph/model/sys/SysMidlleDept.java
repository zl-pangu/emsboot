package com.zhph.model.sys;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/23.
 */
public class SysMidlleDept implements Serializable{
    private String oldCode;
    private Long newId;

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public Long getNewId() {
        return newId;
    }

    public void setNewId(Long newId) {
        this.newId = newId;
    }

}
