package com.zhph.model.hqclcf.dto;

import com.zhph.model.sys.SysConfigType;

import java.io.Serializable;

public class HqclcfEmpApvQueryParam implements Serializable{
   private boolean flag;
   private SysConfigType sysConfigType;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public SysConfigType getSysConfigType() {
        return sysConfigType;
    }

    public void setSysConfigType(SysConfigType sysConfigType) {
        this.sysConfigType = sysConfigType;
    }
}
