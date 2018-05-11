package com.zhph.model.sys.dto;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/8.
 */
public class SysUserAddParams implements Serializable {
    private SysUserData data;
    private Long[] allNodes;
    private Long[] hqNodes;
    private Long[] clNodes;
    private Long[] cfNodes;

    public SysUserData getData() {
        return data;
    }

    public void setData(SysUserData data) {
        this.data = data;
    }

    public Long[] getAllNodes() {
        return allNodes;
    }

    public void setAllNodes(Long[] allNodes) {
        this.allNodes = allNodes;
    }

    public Long[] getHqNodes() {
        return hqNodes;
    }

    public void setHqNodes(Long[] hqNodes) {
        this.hqNodes = hqNodes;
    }

    public Long[] getClNodes() {
        return clNodes;
    }

    public void setClNodes(Long[] clNodes) {
        this.clNodes = clNodes;
    }

    public Long[] getCfNodes() {
        return cfNodes;
    }

    public void setCfNodes(Long[] cfNodes) {
        this.cfNodes = cfNodes;
    }
}
