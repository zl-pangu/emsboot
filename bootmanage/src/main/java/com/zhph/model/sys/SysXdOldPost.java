package com.zhph.model.sys;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/27.
 */
public class SysXdOldPost implements Serializable{
    private String workPlace;
    private String team;
    private String rankName;
    private String postNo;
    private String postName;
    private String pPostNo;
    private String pPostName;

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getPostNo() {
        return postNo;
    }

    public void setPostNo(String postNo) {
        this.postNo = postNo;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getpPostNo() {
        return pPostNo;
    }

    public void setpPostNo(String pPostNo) {
        this.pPostNo = pPostNo;
    }

    public String getpPostName() {
        return pPostName;
    }

    public void setpPostName(String pPostName) {
        this.pPostName = pPostName;
    }
}
