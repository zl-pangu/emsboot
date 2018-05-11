package com.zhph.model.sys.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/8.
 */
public class SysUserDeptDataAuth implements Serializable{
    private Long id;
    private String userId;
    private Long deptAuthId;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getDeptAuthId() {
        return deptAuthId;
    }
    public void setDeptAuthId(Long deptAuthId) {
        this.deptAuthId = deptAuthId;
    }
}
