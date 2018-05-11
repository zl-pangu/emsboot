package com.zhph.model.sys;

import java.io.Serializable;

public class SysRoles implements Serializable {
    private static final long serialVersionUID = -771030234853615538L;
    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色是否启用 0：禁用；1：启用
     */
    private Integer roleIsUse;

    /**
     * 角色信息描述
     */
    private String roleDescription;

    /**
     * 创建者名称
     */
    private String createName;

    /**
     * 创建日期
     */
    private String createDate;

    /**
     * 修改者名称
     */
    private String updateName;

    /**
     * 修改日期
     */
    private String updateDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public Integer getRoleIsUse() {
        return roleIsUse;
    }

    public void setRoleIsUse(Integer roleIsUse) {
        this.roleIsUse = roleIsUse;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}