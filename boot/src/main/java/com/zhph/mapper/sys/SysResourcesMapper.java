package com.zhph.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhph.model.sys.SysResources;

public interface SysResourcesMapper {
	
	/** 根据主键查询 资源 */
	public SysResources findById(@Param("id")  Long id);
	/** 根据父级ID查询 资源 */
	public List<SysResources> findByParentId(@Param("parentId") Long parentId);
	
	public void insert(@Param("t")SysResources resources );
	
	public void update(@Param("t")SysResources resources );
	
	public void deleteById(@Param("id") Long id);
	/** 根据权限ID 查询菜单资源 */
	public List<SysResources> findMenuByResourcesId(List<Long> resourcesIds);
	/** 查询某个系统下 所有的资源 */
	public List<SysResources> findAllByResourcesSysType(@Param("resourcesSysType") Integer resourcesSysType);
	/** 根据角色ID 查询角色对应的资源*/
	public List<SysResources> findResourcesByRoleId(List<Integer> roleIds);
	
	public Integer findRoleCountByResourcesId(@Param("resourcesId") Long resourcesId);
	
	public List<String> findAllURI();
	
	/**
	 * 
	 * @Title checkNameRepeat
	 * @Description TODO 查询资源名称是否重复
	 * @param  userName
	 * @param     参数
	 * @return Integer    返回类型
	 *
	 */
	public Integer checkNameRepeat(@Param("resourcesName") String resourcesName);
	
	/**
	 * 
	 * @Title findAllChildrenByResourcesId
	 * @Description TODO 根据传入的资源ID 查询该资源ID下所有的子资源
	 * @param  resourcesId
	 * @param     参数
	 * @return List<Integer>    返回类型
	 *
	 */
	public List<Integer> findAllChildrenByResourcesId(@Param("resourcesId") String resourcesId);
	
	/**
	 * 
	 * @Title updateResourcesIsUse
	 * @Description TODO 更新父类下 所有子类的 启用状态
	 * @param @param resourcesIsUse
	 * @param @param resourcesId    参数
	 * @return void    返回类型
	 *
	 */
	public void updateResourcesIsUse(@Param("resourcesIsUse") Integer resourcesIsUse,@Param("list") List<Integer> resourcesId);

}
