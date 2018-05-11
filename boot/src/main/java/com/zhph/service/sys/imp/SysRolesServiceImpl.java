package com.zhph.service.sys.imp;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhph.mapper.sys.SysRolesMapper;
import com.zhph.model.sys.SysRoles;
import com.zhph.service.sys.SysRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/17.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRolesServiceImpl implements SysRolesService {

	@Autowired
	private SysRolesMapper sysRolesMapper;

	@Override
	public int deleteByPrimaryKey(Long roleId) {
		return sysRolesMapper.deleteByPrimaryKey(roleId);
	}

	@Override
	public int insert(SysRoles record) {
		return sysRolesMapper.insert(record);
	}

	public int insertSelective(SysRoles record) {
		return sysRolesMapper.insertSelective(record);
	}

	@Override
	public SysRoles selectByPrimaryKey(Long roleId) {
		return sysRolesMapper.selectByPrimaryKey(roleId);
	}

	@Override
	public int updateByPrimaryKeySelective(SysRoles record) {
		return sysRolesMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(SysRoles record) {
		return sysRolesMapper.updateByPrimaryKey(record);
	}

	/**
	 * 分页查询
	 *
	 * @param pageSize
	 * @param curPage
	 * @param map
	 * @return
	 */
	@Override
	public Page<SysRoles> selectByRoleCodeAndRoleName(Integer pageSize, Integer curPage, Map<String, String> map) {
		PageHelper.startPage(curPage, pageSize);
		List<SysRoles> dataList = sysRolesMapper.selectByRoleCodeAndRoleName(map);
		Page<SysRoles> page = (Page<SysRoles>) dataList;
		return page;
	}

	@Override
	public List<SysRoles> checkRoleCode(Map<String, String> map) {
		return sysRolesMapper.selectByRoleCodeAndRoleName(map);
	}

	@Override
	public List<SysRoles> checkRoleName(Map<String, String> map) {
		return sysRolesMapper.selectByRoleCodeAndRoleName(map);
	}

	@Override
	public List<SysRoles> selectRolesByUserId(String userId) {
		return sysRolesMapper.selectRolesByUserId(userId);
	}

	/**
	 * 增加时候 调用
	 * 
	 * @param resourcesId
	 * @return
	 */
	public int insertRoleAndResource(String resourcesId) {
		return sysRolesMapper.insertRoleAndResource(resourcesId);
	}

	/**
	 * 更新时候调用
	 * 
	 * @param roleId
	 * @param resourcesId
	 * @return
	 */
	public int updateRoleAndResource(Long roleId, String resourcesId) {
		return sysRolesMapper.updateRoleAndResource(roleId, resourcesId);
	}

	@Override
	public int insertAll(SysRoles sysRoles, String[] resoucesIds) {
		int ret = insertSelective(sysRoles);
		for (String resourcesId : resoucesIds) {
			insertRoleAndResource(resourcesId);
		}
		return ret;
	}

	@Override
	public int updateAll(SysRoles sysRoles, String[] resoucesIds) {
		Long roleId = sysRoles.getRoleId();
		int ret = updateByPrimaryKeySelective(sysRoles);
		for (String resourcesId : resoucesIds) {
			updateRoleAndResource(roleId, resourcesId);
		}
		return ret;
	}

	@Override
	public List<Long> selectNodeIds(Long roleId) {
		return sysRolesMapper.selectNodeIds(roleId);
	}

	@Override
	public int delResourceByRoleId(Long roleId) {
		return sysRolesMapper.delResourceByRoleId(roleId);
	}

	@Override
	public List<SysRoles> queryAllRoles() {
		return sysRolesMapper.queryAllRoles();
	}
}
