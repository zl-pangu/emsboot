package com.zhph.model.hqclcf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zhouliang on 2018/2/2.
 */
public class HqclcfEmpApvBrFlag {
    @JsonProperty("flag_badinfo")
    private int flagBadinfo; //1：调用成功有数据 0：调用成功无数据 99：调用失败

    public int getFlagBadinfo() {
        return flagBadinfo;
    }

    public void setFlagBadinfo(int flagBadinfo) {
        this.flagBadinfo = flagBadinfo;
    }
}
