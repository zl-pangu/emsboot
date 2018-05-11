package com.zhph.service.sys;

import com.github.pagehelper.Page;
import com.zhph.model.sys.SysRoles;

import java.util.List;
import java.util.Map;

public interface SysRolesService {

	int deleteByPrimaryKey(Long roleId);

	int insert(SysRoles record);

	SysRoles selectByPrimaryKey(Long roleId);

	int updateByPrimaryKeySelective(SysRoles record);

	int updateByPrimaryKey(SysRoles record);

	/**
	 * 根据角色编码和者角色名称查询 /分页查询
	 *
	 * @param map
	 * @return
	 */
	public Page<SysRoles> selectByRoleCodeAndRoleName(Integer pageSize, Integer curPage, Map<String, String> map);

	public List<SysRoles> checkRoleCode(Map<String, String> map);

	public List<SysRoles> checkRoleName(Map<String, String> map);

	/**
	 * 根据用户id查询 拥有的角色
	 *
	 * @param userId
	 * @return
	 */
	public List<SysRoles> selectRolesByUserId(String userId);

	/**
	 * 将角色增加和 资源管理增加统一方法处理
	 * 
	 * @param sysRoles
	 * @param resoucesIds
	 * @return
	 */
	public int insertAll(SysRoles sysRoles, String[] resoucesIds);

	/**
	 * 将角色修改 和 资源管理增加统一方法处理
	 * 
	 * @param sysRoles
	 * @param resoucesIds
	 * @return
	 */
	public int updateAll(SysRoles sysRoles, String[] resoucesIds);

	/**
	 * 根据角色 roleId查询 对应的节点id集合
	 * 
	 * @param roleId
	 * @return
	 */
	List<Long> selectNodeIds(Long roleId);

	/**
	 * 根据roleId删除资源
	 * 
	 * @param roleId
	 * @return
	 */
	int delResourceByRoleId(Long roleId);

	public List<SysRoles> queryAllRoles();
}
