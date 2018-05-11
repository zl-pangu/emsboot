package com.zhph.service.sys;

import java.util.List;

import com.zhph.model.sys.SysResources;
import com.zhph.model.sys.vo.SysResourcesUpdateVo;
import com.zhph.model.vo.ResultVo;
import com.zhph.model.vo.TreeVo;

public interface SysResourcesService {
	
	/**
	 * 
	 * @Title getZtreeRoot
	 * @Description TODO 查询资源树
	 * @param @param id
	 * @param @param check_state
	 * @param @param checked
	 * @param @return    参数
	 * @return Tree    返回类型
	 *
	 */
	public TreeVo getZtreeRoot(Long id,int check_state, boolean checked);
	
	
	
	/**
	 * 
	 * @Title findById
	 * @Description TODO 根据主键查询资源
	 * @param  id
	 * @param 
	 * @return Resources    返回类型
	 *
	 */
	public SysResources findById(Long id);
	
	/**
	 * 
	 * @Title insert
	 * @Description 新增资源
	 * @param @param resources    参数
	 * @return void    返回类型
	 *
	 */
	public SysResources insert(SysResources resources, Long userId);
	
	/**
	 * 
	 * @Title update
	 * @Description 修改资源
	 * @param  resourcesUpdateDomain    参数
	 * @return void    返回类型
	 *
	 */
	public ResultVo update(SysResourcesUpdateVo resourcesUpdateDomain,Long userId);
	
	/**
	 * 
	 * @Title delete
	 * @Description TODO 删除资源
	 * @param @param resorucesId
	 * @param     参数
	 * @return ResultVo    返回类型
	 *
	 */
	public ResultVo delete(Long resorucesId);
	
	
	/**
	 * 
	 * @Title findMenuByResourcesId
	 * @Description TODO 获取权限菜单树
	 * @param @param resourcesIds
	 * @param @return    参数
	 * @return TreeVo    返回类型
	 *
	 */
	public TreeVo findMenuByResourcesId(List<Long> resourcesIds);
	
	/**
	 * 
	 * @Title findTreeBySysType
	 * @Description TODO 根据所属系统查询 树
	 * @param @param resourcesSysType
	 * @param @return    参数
	 * @return TreeVo    返回类型
	 *
	 */
	public TreeVo findTreeBySysType(int resourcesSysType);
	
	/**
	 * 
	 * @Title getResourcesIdByRoleIds
	 * @Description TODO 根据用户角色查询该用户所有的权限url
 	 * @param  roleIds
	 * @return List<SysResources>    返回类型
	 *
	 */
	public List<SysResources> getResourcesByRoleIds(List<Integer> roleIds);
	
	
	/**
	 * 
	 * @Title findAllURI
	 * @Description TODO 查询所有的URI权限
	 * @param @return    参数
	 * @return List<String>    返回类型
	 *
	 */
	public List<String> findAllURI();
	
	/**
	 * 
	 * @Title checkNameRepeat
	 * @Description TODO 查询资源名称有多少个
	 * @param @param resourcesName
	 * @param @return    参数
	 * @return Integer    返回类型
	 *
	 */
	public Integer checkNameRepeat(String resourcesName);
}
