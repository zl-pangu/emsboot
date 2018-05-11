package com.zhph.service.sys.imp;

import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhph.commons.Constant;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.sys.dto.SysUserAddParams;
import com.zhph.model.sys.dto.SysUserData;
import com.zhph.model.sys.dto.SysUserDeptDataAuth;
import com.zhph.service.common.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhph.exception.AppException;
import com.zhph.mapper.sys.SysRolesMapper;
import com.zhph.mapper.sys.SysUserMapper;
import com.zhph.model.sys.SysUser;
import com.zhph.service.sys.SysUserService;
import com.zhph.util.CommonUtil;
import com.zhph.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserServiceImpl implements SysUserService {
	@Autowired
	private SysUserMapper sysUserMapper;
	@Autowired
	private SysRolesMapper sysRolesMapper;
	@Resource
	private HqclcfDeptMapper hqclcfDeptMapper;
	@Autowired
	private HqclcfEmpMapper hqclcfEmpMapper;
	@Resource
    private BaseService baseService;

	@Override
	public Page<SysUser> queryAllUserByInfo(SysUser params, Integer pageNum, Integer pageSize) throws Exception {
		PageHelper.startPage(pageNum, pageSize);
		List<SysUser> sysUsers = sysUserMapper.queryAllUserByInfo(params);
		Page<SysUser> userPage = (Page<SysUser>)sysUsers;
		return userPage;
	}

	@Override
	public void deleteUsers(Long[] ids) throws AppException {
		SysUser temp = null;
		for (Long id : ids) {
			temp = sysUserMapper.queryUserById(id);
			if ("1".equals(temp.getUserId()))
				throw new AppException("不允许删除超级管理员", 501);
			Integer count = baseService.queryOperatePeople(temp.getUserName());
			if (count>0)
				throw new AppException("不允许删除当前用户因为在日志表有操作记录",501);
			sysUserMapper.updateUserToDeleted(id);
			sysUserMapper.deleteDeptAuth(temp.getUserId());
		}
	}

	@Override
	public SysUser queryUserById(Long id) throws Exception {
		return sysUserMapper.queryUserById(id);
	}

	@Override
	public void saveEdit(String data) throws Exception {
		if (data == null)
			throw new AppException("参数为空", 501);
		ObjectMapper mapper=new ObjectMapper();
		SysUserAddParams sysUserAddParams = mapper.readValue(data.getBytes(), SysUserAddParams.class);
		SysUserData userData = sysUserAddParams.getData();
		Long[] roleIds = userData.getRoleIds();
		SysUser params=new SysUser();
		params.setId(null!=userData.getId()?userData.getId():null);
		params.setEmail(null != userData.getEmail() ? userData.getEmail() : null);
		params.setUserName(null!=userData.getUserName()?userData.getUserName():null);
		params.setFullName(null!=userData.getFullName()?userData.getFullName():null);
		params.setMobile(null!=userData.getMobile()?userData.getMobile():null);
		params.setEmpNo(null!=userData.getEmpNo()?userData.getEmpNo():null);
		params.setIsEnable(null!=userData.getIsEnable()?userData.getIsEnable():null);
		params.setTeleNum(null!=userData.getTeleNum()?userData.getTeleNum():null);
		params.setPwd(null!=userData.getPwd()?userData.getPwd():null);
		Integer[] blSelect = userData.getBlSelect();
		StringBuilder sb=new StringBuilder("");
		for (Integer bl : blSelect) {
			sb.append(bl+".");
		}
		params.setBlSelect(sb.toString());
		if (params.getId() == null)
			throw new AppException("参数错误，主键为空", 501);
		SysUser user = sysUserMapper.queryUserById(params.getId());
		if (user == null)
			throw new AppException("不存在该用户", 502);
		if (user.getIsSuperAdmin() == 1)
			throw new AppException("不允许修改超级管理员", 503);
		String userName = params.getUserName();
		if (StringUtil.isEmpty(userName))
			throw new AppException("参数错误，登录名为空", 501);
		if (!userName.equals(user.getUserName()) && sysUserMapper.queryUserByUserName(userName) != null)
			throw new AppException("参数错误，登录名" + userName + "已经存在", 501);
		if (StringUtil.isEmpty(params.getFullName()))
			throw new AppException("参数错误，真实姓名为空", 501);
		if (StringUtil.isEmpty(params.getMobile()))
			throw new AppException("参数错误，手机为空", 501);
		if (StringUtil.isEmpty(params.getEmail()))
			throw new AppException("参数错误，E-mail为空", 501);
		if (StringUtil.isEmpty(params.getEmpNo()))
			throw new AppException("参数错误，员工编号为空", 501);
		if (null == params.getIsEnable())
			throw new AppException("参数错误，状态为空", 501);

		Date now = new Date();
		String pwd = params.getPwd();
		if (StringUtil.isNotEmpty(pwd)) {
			String encodePwd = CommonUtil.encodePwd(pwd);
			params.setPwd(encodePwd);
			params.setPwdBak(pwd);
			params.setLastChangePwdTime(null);
		}

		params.setUpdateUserId(CommonUtil.getOnlineUserId());
		params.setUpdateTime(now);

		sysUserMapper.saveEdit(params);
		// 解除原先的角色
		String userId = user.getUserId();
		sysRolesMapper.unGrantRolesByUserId(userId);
		// 重新授予角色
		if (roleIds != null) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("userId", userId);
			for (Long roleId : roleIds) {
				p.put("roleId", roleId);
				sysRolesMapper.grantRoleToUser(p);
			}
		}
		//删除原来的userId的数据
		sysUserMapper.deleteDeptAuth(userId);
		//再新增进去
		List<SysUserDeptDataAuth> list = buildDeptAuthData(sysUserAddParams, userId);
		if (list.size()>0){
			sysUserMapper.batchInsertUserDeptAuth(list);
		}
	}
	@Override
	public void saveAdd(String data) throws Exception {
		if (data == null)
			throw new AppException("参数为空", 501);
		ObjectMapper mapper=new ObjectMapper();
		SysUserAddParams sysUserAddParams = mapper.readValue(data.getBytes(), SysUserAddParams.class);
		SysUserData userData = sysUserAddParams.getData();
		Long[] roleIds = userData.getRoleIds();
		SysUser params=new SysUser();
		params.setEmail(null != userData.getEmail() ? userData.getEmail() : null);
		params.setUserName(null!=userData.getUserName()?userData.getUserName():null);
		params.setFullName(null!=userData.getFullName()?userData.getFullName():null);
		params.setMobile(null!=userData.getMobile()?userData.getMobile():null);
		params.setEmpNo(null!=userData.getEmpNo()?userData.getEmpNo():null);
		params.setIsEnable(null!=userData.getIsEnable()?userData.getIsEnable():null);
		params.setTeleNum(null!=userData.getTeleNum()?userData.getTeleNum():null);
		params.setPwd(null!=userData.getPwd()?userData.getPwd():null);
		Integer[] blSelect = userData.getBlSelect();
		StringBuilder sb=new StringBuilder("");
		for (Integer bl : blSelect) {
			sb.append(bl+".");
		}
		params.setBlSelect(sb.toString());
		String userName = params.getUserName();
		String empNo = params.getEmpNo();
		if (StringUtil.isEmpty(userName))
			throw new AppException("参数错误，登录名为空", 501);
		if (sysUserMapper.queryUserByUserName(userName) != null)
			throw new AppException("参数错误，登录名" + userName + "已经存在", 501);
		Map<String,String> map=new HashMap<>();
		map.put("empNo",empNo);
		if (sysUserMapper.quertUserCount(map)>0)
			throw new AppException("参数错误，员工编码" + empNo + "已经存在", 501);
		if (StringUtil.isEmpty(params.getFullName()))
			throw new AppException("参数错误，真实姓名为空", 501);
		if (StringUtil.isEmpty(params.getMobile()))
			throw new AppException("参数错误，手机为空", 501);
		if (StringUtil.isEmpty(params.getEmail()))
			throw new AppException("参数错误，E-mail为空", 501);
		if (StringUtil.isEmpty(params.getEmpNo()))
			throw new AppException("参数错误，员工编号为空", 501);
		if (null == params.getIsEnable())
			throw new AppException("参数错误，状态为空", 501);

		Date now = new Date();

		String userId = CommonUtil.getCustomPrimaryKey();
		params.setUserId(userId);


		String pwd = params.getPwd();
		if (StringUtil.isEmpty(pwd))
			pwd = "12345678";// 默认密码12345678
		String encodePwd = CommonUtil.encodePwd(pwd);
		params.setPwd(encodePwd);
		params.setPwdBak(pwd);

		params.setIsDelete(0);
		params.setIsSuperAdmin(0);
		params.setLoginErrorCount(0);
		params.setCreatorUserId(CommonUtil.getOnlineUserId());
		params.setCreateTime(now);

		sysUserMapper.saveAdd(params);
		//记录在用户-数据权限中间表
		List<SysUserDeptDataAuth> list = buildDeptAuthData(sysUserAddParams, userId);
		if(list.size()>0){
			sysUserMapper.batchInsertUserDeptAuth(list);
		}
		// 授予角色
		if (roleIds != null) {
			Map<String, Object> p = new HashMap<String, Object>();
			p.put("userId", userId);
			for (Long roleId : roleIds) {
				p.put("roleId", roleId);
				sysRolesMapper.grantRoleToUser(p);
			}
		}
	}




	/**
	 * 构建用户对应的数据权限
	 * @param data
	 */
	private List<SysUserDeptDataAuth> buildDeptAuthData(SysUserAddParams data,String userId) throws Exception {
		List<SysUserDeptDataAuth> list=new ArrayList<>();
		SysUserData userData = data.getData();
		Integer[] blSelect = userData.getBlSelect();
		for (Integer bl : blSelect) {
			switch (bl){
				case 0://全部
					bulidListDeptId(data.getAllNodes(),list,userId);
					break;
				case 1://总部
					bulidListDeptId(data.getHqNodes(),list,userId);
					break;
				case 2://信贷
					bulidListDeptId(data.getClNodes(),list,userId);
					break;
				case 3://消分
					bulidListDeptId(data.getCfNodes(),list,userId);
					break;
			}
		}
		return list;
	}

	/**
	 * 提取buildList
	 * @param nodes
	 * @param list
	 * @param userId
	 */
	private void bulidListDeptId(Long[] nodes, List<SysUserDeptDataAuth> list, String userId) {
		if (nodes!=null) {
			for (Long deptId: nodes) {
				SysUserDeptDataAuth auth=new SysUserDeptDataAuth();
				auth.setUserId(userId);
				auth.setDeptAuthId(deptId);
				list.add(auth);
			}
		}
	}


	@Override
	public void updateLoginErrorCount(SysUser params) throws Exception {
		sysUserMapper.updateLoginErrorCount(params);
	}

	@Override
	public void saveChangePwd(String userName, String pwd, String newPwd, String rePwd) throws Exception {
		SysUser user = sysUserMapper.queryUserWithPwdByUserName(userName);
		// SysUser oldUser = new SysUser();
		// BeanUtils.copyProperties(user, oldUser);

		String pwdEncode = CommonUtil.encodePwd(pwd);
		String newPwdEncode = CommonUtil.encodePwd(newPwd);

		if (user == null)
			throw new AppException("用户不存在！");
		else if (user.getIsDelete() == 1)
			throw new AppException("用户不存在！");
		else if (!pwdEncode.equals(user.getPwd()))
			throw new AppException("原密码错误！");
		else if (user.getIsEnable() != 1)
			throw new AppException("用户已被禁用！");
		else if (newPwd.equals(pwd))
			throw new AppException("新密码不能与原密码相同！");

		Date now = new Date();

		user.setPwd(newPwdEncode);
		user.setPwdBak(newPwd);
		user.setLastChangePwdTime(now);
		user.setLoginErrorCount(0);

		sysUserMapper.saveChangePwd(user);
	}

	@Override
	public void buildUserSelectBl(SysUser user, HttpServletRequest req) throws Exception {
		String blSelect = user.getBlSelect();
		List<Integer> list = new ArrayList<>();
		if (!"".equals(blSelect)&&blSelect!=null) {
			String[] split = blSelect.split(Constant.SPOT);
			for (String s : split) {
				list.add(Integer.valueOf(s));
			}
		}
		Map<String,String> map=new HashMap<>();
		map.put("userId",user.getUserId());
		List<Map<String, Object>> depts = hqclcfDeptMapper.queryDeptMapByMap(map);
		StringBuilder hqsb=new StringBuilder("");
		StringBuilder xfsb=new StringBuilder("");
		StringBuilder xdsb=new StringBuilder("");
		for (Map<String, Object> dept:depts) {
			String deptName = null != dept.get("DEPT_NAME") ? (String) dept.get("DEPT_NAME") : "";
			BigDecimal bl =null!=dept.get("BUSINESS_LINE")?(BigDecimal)dept.get("BUSINESS_LINE"):new BigDecimal(4);
			int businessLine = bl.intValue();
			switch (businessLine){
				case 1://总
					hqsb.append(deptName+",");
					break;
				case 2://消
					xfsb.append(deptName+",");
					break;
				case 3://信
					xdsb.append(deptName+",");
					break;
				case 4:
					hqsb.append(deptName+",");
					xfsb.append(deptName+",");
					xdsb.append(deptName+",");
			}
		}
		String all="";
		String hq="";
		String xf="";
		String xd="";
		for (Integer bl: list){
				switch (bl){
					case 0://全
						all=hqsb.append(xfsb).append(xdsb).toString();
						break;
					case 1://总
						hq=hqsb.toString();
						break;
					case 2://信贷
						xd=xdsb.toString();
						break;
					case 3://消分
						xf=xfsb.toString();
						break;
				}
		}


		req.setAttribute("blList",list);
		req.setAttribute("allInput",all);
		req.setAttribute("hqInput",hq);
		req.setAttribute("xfInput",xf);
		req.setAttribute("xdInput",xd);
	}

	@Override
	public List<HqclcfEmp> queryEmpByBl(String q) throws Exception {
		Map<String,Object> map=new HashMap<>();
		List<Integer> onlineUserBl = baseService.getOnlineUserBl();
		map.put("q",q);
		map.put("bls",onlineUserBl);
		List<HqclcfEmp> list =hqclcfEmpMapper.queryEmpByMap(map);
        return list;
	}
}
