package com.zhph.model.sys.dto;

/**
 * Created by zhouliang on 2018/2/9.
 *
 * 数据字典新增参数dto
 */
public class SysConfigAddParams implements java.io.Serializable{
    //新增父节点
    private String sysName;
    private String sysCode;
    private String display0;
    private Integer order0;

    //新增子节点
    private Integer addType;
    private Integer childOrder;
    private String display1;
    private String pCode;
    private Long pId;
    private String pName;
    private String sysCode1;
    private String sysName1;

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getDisplay0() {
        return display0;
    }

    public void setDisplay0(String display0) {
        this.display0 = display0;
    }

    public Integer getOrder0() {
        return order0;
    }

    public void setOrder0(Integer order0) {
        this.order0 = order0;
    }

    public Integer getAddType() {
        return addType;
    }

    public void setAddType(Integer addType) {
        this.addType = addType;
    }

    public Integer getChildOrder() {
        return childOrder;
    }

    public void setChildOrder(Integer childOrder) {
        this.childOrder = childOrder;
    }

    public String getDisplay1() {
        return display1;
    }

    public void setDisplay1(String display1) {
        this.display1 = display1;
    }

    public String getpCode() {
        return pCode;
    }

    public void setpCode(String pCode) {
        this.pCode = pCode;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getSysCode1() {
        return sysCode1;
    }

    public void setSysCode1(String sysCode1) {
        this.sysCode1 = sysCode1;
    }

    public String getSysName1() {
        return sysName1;
    }

    public void setSysName1(String sysName1) {
        this.sysName1 = sysName1;
    }
}
