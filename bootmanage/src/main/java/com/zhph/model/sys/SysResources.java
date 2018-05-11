package com.zhph.model.sys;

import java.util.Date;

public class SysResources {
	
	/** 资源是否是子节点 ------是 */
	public static final Integer RESOURCES_IS_END_YES=1;
	/** 资源是否是子节点 ------不是 */
	public static final Integer RESOURCES_IS_END_NO=0;
	/** 资源是否是启用 ------启用 */
	public static final Integer RESOURCES_IS_USE_YES=1;
	/** 资源是否是启用 ------停用 */
	public static final Integer RESOURCES_IS_USE_NO=0;

	/** ID */
	private Long resourcesId;
	/** 资源父级ID */
	private Long resourcesParentId;
	/** 资源名称 */
	private String resourcesName;
	/** 资源URL */
	private String resourcesUrl;
	/** 资源排序 */
	private Integer resourcesSortId;
	/** 资源是否启用 ：1：启用； 0：停用 */
	private Integer resourcesIsUse;
	/** 资源是否是子节点 ：1：是； 0：不是 */
	private Integer resourcesIsEnd;
	/** 资源类型 1：菜单，2：按钮 */
	private Integer resourcesType;
	/** 资源描述 */
	private String resourcesDescription;
	/** 资源有多少子集 */
	private Integer resourcesChildSize;
	/** 是否是权限模块 */
	private Integer resourcesIsPopedoms;
	/** 资源级别（如：一级菜单、二级菜单 ） */
	private Integer resourcesLevelNumber;
	/** 资源全路径Id */
	private String resourcesFullId;
	/** 资源全路径Name */
	private String resourcesFullName;
	/** 资源菜单所属系统 1:信贷薪资系统 ；2：消金薪资系统 ；3：总部薪资系统 */
	private Integer resourcesSysType;
	/** 创建时间 */
	private Date createTime;
	/** 创建人 */
	private Long creatorId;
	/** 修改时间 */
	private Date updateTime;
	/** 修改人 */
	private Long updateId;

	public Long getResourcesId() {
		return resourcesId;
	}

	public void setResourcesId(Long resourcesId) {
		this.resourcesId = resourcesId;
	}

	public Long getResourcesParentId() {
		return resourcesParentId;
	}

	public void setResourcesParentId(Long resourcesParentId) {
		this.resourcesParentId = resourcesParentId;
	}

	public String getResourcesName() {
		return resourcesName;
	}

	public void setResourcesName(String resourcesName) {
		this.resourcesName = resourcesName;
	}

	public String getResourcesUrl() {
		return resourcesUrl;
	}

	public void setResourcesUrl(String resourcesUrl) {
		this.resourcesUrl = resourcesUrl;
	}

	public Integer getResourcesSortId() {
		return resourcesSortId;
	}

	public void setResourcesSortId(Integer resourcesSortId) {
		this.resourcesSortId = resourcesSortId;
	}

	public Integer getResourcesIsUse() {
		return resourcesIsUse;
	}

	public void setResourcesIsUse(Integer resourcesIsUse) {
		this.resourcesIsUse = resourcesIsUse;
	}

	public Integer getResourcesIsEnd() {
		return resourcesIsEnd;
	}

	public void setResourcesIsEnd(Integer resourcesIsEnd) {
		this.resourcesIsEnd = resourcesIsEnd;
	}

	public Integer getResourcesType() {
		return resourcesType;
	}

	public void setResourcesType(Integer resourcesType) {
		this.resourcesType = resourcesType;
	}

	public String getResourcesDescription() {
		return resourcesDescription;
	}

	public void setResourcesDescription(String resourcesDescription) {
		this.resourcesDescription = resourcesDescription;
	}

	public Integer getResourcesChildSize() {
		return resourcesChildSize;
	}

	public void setResourcesChildSize(Integer resourcesChildSize) {
		this.resourcesChildSize = resourcesChildSize;
	}

	public Integer getResourcesIsPopedoms() {
		return resourcesIsPopedoms;
	}

	public void setResourcesIsPopedoms(Integer resourcesIsPopedoms) {
		this.resourcesIsPopedoms = resourcesIsPopedoms;
	}

	public Integer getResourcesLevelNumber() {
		return resourcesLevelNumber;
	}

	public void setResourcesLevelNumber(Integer resourcesLevelNumber) {
		this.resourcesLevelNumber = resourcesLevelNumber;
	}

	public String getResourcesFullId() {
		return resourcesFullId;
	}

	public void setResourcesFullId(String resourcesFullId) {
		this.resourcesFullId = resourcesFullId;
	}

	public String getResourcesFullName() {
		return resourcesFullName;
	}

	public void setResourcesFullName(String resourcesFullName) {
		this.resourcesFullName = resourcesFullName;
	}

	public Integer getResourcesSysType() {
		return resourcesSysType;
	}

	public void setResourcesSysType(Integer resourcesSysType) {
		this.resourcesSysType = resourcesSysType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

}
