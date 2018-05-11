package com.zhph.controller.sys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.mapper.sys.SysUserMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.sys.dto.SysUserAddParams;
import com.zhph.model.sys.dto.SysUserData;
import com.zhph.service.common.BaseService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.zhph.annotation.IgnoreLogin;
import com.zhph.exception.AppException;
import com.zhph.model.sys.SysRoles;
import com.zhph.model.sys.SysUser;
import com.zhph.model.vo.BsgridVo;
import com.zhph.service.sys.SysRolesService;
import com.zhph.service.sys.SysUserService;
import com.zhph.util.StringUtil;

@Controller
@RequestMapping("/sys/user")
public class SysUserController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysRolesService sysRolesService;
	@Resource
	private SysUserMapper sysUserMapper;

	@RequestMapping("/index")
	public Object init(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "/pages/sys/user/userIndex";
	}

	@ResponseBody
	@RequestMapping(value = "/queryAll", method = RequestMethod.POST)
	public Object queryAll(HttpServletRequest req, HttpServletResponse res, SysUser params, Integer curPage, Integer pageSize) throws Exception {
		BsgridVo<SysUser> bsgridVo = new BsgridVo<SysUser>();
		try {
			Page<SysUser> userPage = sysUserService.queryAllUserByInfo(params, curPage, pageSize);
			bsgridVo.setCurPage(curPage.longValue());
			bsgridVo.setData(userPage);
			bsgridVo.setSuccess(true);
			bsgridVo.setTotalRows(userPage.getTotal());
		} catch (Exception e) {
			e.printStackTrace();
			bsgridVo.setSuccess(false);
		}
		return bsgridVo;
	}

	@ResponseBody
	@RequestMapping(value = "/deletes", method = RequestMethod.POST)
	public Object deleteUsers(HttpServletRequest req, HttpServletResponse res, String ids){
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("code", 200);
		try {
			if (ids == null)
                throw new AppException("参数错误：不能为空", 501);
			ids = ids.replaceAll("\\s", "").replaceAll(",{2,}", ",");
			if ("".equals(ids))
                throw new AppException("参数错误：不能为空", 501);

			Long[] idArr = StringUtil.splitToLongArray(ids, ",");
			sysUserService.deleteUsers(idArr);
			obj.put("msg", "删除用户成功！");
		} catch (AppException e) {
			obj.put("msg", e.getMessage());
			obj.put("code",e.getStatus());
			e.printStackTrace();
		}

		return obj;
	}

	@RequestMapping("/detail")
	public Object detailPage(HttpServletRequest req, HttpServletResponse res, String type, Long id) throws Exception {
		if (StringUtil.isEmpty(type))
			throw new AppException("参数错误：type不能为空", 501);

		req.setAttribute("type", type);

		List<SysRoles> allSysRoles = sysRolesService.queryAllRoles();
		req.setAttribute("allSysRoles", allSysRoles);

		if ("add".equals(type)) {

		} else {
			if (id == null)
				throw new AppException("参数错误：id不能为空", 501);
			SysUser user = sysUserService.queryUserById(id);
			if (user == null)
				throw new AppException("用户不存在", 502);
			if ("show".equals(type)){
				String creatorUserId = user.getCreatorUserId();
				String updateUserId = user.getUpdateUserId();
				Map<String,String> map=new HashMap<>();
				map.put("creatorUserId",creatorUserId);
				map.put("updateUserId",updateUserId);
				List<Map<String, Object>> list = sysUserMapper.queryUserinfoByuserID(map);
				String creatorUser="";
				String updateUser="";
				/*存的坑爹改的也坑爹*/
				if (list.size()>0){
					for (Map<String, Object> usermap: list) {
						if ((null!=user.getCreatorUserId()?user.getCreatorUserId():"").equals(null!=usermap.get("USER_ID")?(String)usermap.get("USER_ID"):"")){
							creatorUser=null!=usermap.get("FULL_NAME")?(String)usermap.get("FULL_NAME"):"";
						}
						if ((null!=user.getUpdateUserId()?user.getUpdateUserId():"").equals(null!=usermap.get("USER_ID")?(String)usermap.get("USER_ID"):"")){
							updateUser=null!=usermap.get("FULL_NAME")?(String)usermap.get("FULL_NAME"):"";
						}
					}
					user.setCreatorUserId(creatorUser);
					user.setUpdateUserId(updateUser);
				}else{
					user.setCreatorUserId(creatorUser);
					user.setUpdateUserId(updateUser);
				}
			}
			req.setAttribute("user", user);
			sysUserService.buildUserSelectBl(user,req);

			List<SysRoles> userSysRoles = sysRolesService.selectRolesByUserId(user.getUserId());// 用户拥有的角色
			req.setAttribute("userSysRoles", userSysRoles);

		}
		return "/pages/sys/user/userDetail";
	}

	@ResponseBody
	@RequestMapping(value = "/saveEdit", method = RequestMethod.POST)
	public Object saveEdit(HttpServletRequest req, HttpServletResponse res, @Param("data")String data,SysUser params, Long[] roleIds) throws Exception {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("code", 200);
		sysUserService.saveEdit(data);
		return obj;
	}

	@ResponseBody
	@RequestMapping(value = "/saveAdd", method = RequestMethod.POST)
	public Object saveAdd(HttpServletRequest req, HttpServletResponse res, @Param("data")String data,SysUser params, Long[] roleIds) throws Exception {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("code", 200);
		try {
			sysUserService.saveAdd(data);
		} catch (AppException e) {
			obj.put("code",e.getStatus());
			obj.put("msg",e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	@IgnoreLogin
	@RequestMapping(value = "/changePwdWin", method = RequestMethod.GET)
	public Object showChangePwdWin(HttpServletRequest req, HttpServletResponse res, String userName) throws Exception {
		if (StringUtil.isEmpty(userName)) {
			HttpSession session = req.getSession();
			SysUser onlineUser = (SysUser) session.getAttribute("onlineUser");
			if (onlineUser == null)
				throw new AppException("页面打开失败，参数缺失！");
			userName = onlineUser.getUserName();
		}
		if (StringUtil.isEmpty(userName))
			throw new AppException("页面打开失败，参数缺失！");
		req.setAttribute("userName", userName);

		return "/pages/common/changePwd";
	}

	@IgnoreLogin
	@ResponseBody
	@RequestMapping(value = "/saveChangePwd", method = RequestMethod.POST)
	public Object saveChangePwd(HttpServletRequest req, HttpServletResponse res, @RequestParam String userName, @RequestParam String pwd, @RequestParam String newPwd, @RequestParam String rePwd) throws Exception {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("code", 200);

		try {
			// 参数处理
			if (newPwd.length() < 8)
				throw new AppException("新密码太短了，不能少于8位！");
			else if (newPwd.length() > 16)
				throw new AppException("新密码太长了，不能超过16位！");
			else if (!newPwd.matches(".*[\\w\\S]+.*"))
				throw new AppException("新密码仅允许大小写字母、数字、下划线等常见符号！");
			else if (newPwd.matches(".*\\s+.*"))
				throw new AppException("新密码不能含有空字符！");
			else if (!newPwd.matches(".*[a-z]+.*") || !newPwd.matches(".*[A-Z]+.*") || !newPwd.matches(".*[0-9]+.*"))
				throw new AppException("新密码必须同时包含大小写字母与数字！");
			else if (newPwd.equals(pwd))
				throw new AppException("新密码不能与原密码相同！");

			if (!rePwd.equals(newPwd))
				throw new AppException("两次输入的密码必须相同！");

			sysUserService.saveChangePwd(userName, pwd, newPwd, rePwd);
			obj.put("msg", "密码修改成功！");
		} catch (Exception e) {
			e.printStackTrace();
			obj.put("code", 500);
			obj.put("msg", e.getMessage());
		}

		return obj;
	}


	@RequestMapping("queryEmpByBl")
	@ResponseBody
	public Object queryEmpByBl(@Param("q")String q, @Param("rows") int rows,@Param("page") int page){
		JSONObject ret = new JSONObject();
		PageHelper.startPage(page, rows);
		Grid<HqclcfEmp> grid=new Grid<>();
		try {
			List<HqclcfEmp>	list=sysUserService.queryEmpByBl(q);
			PageInfo<HqclcfEmp> pageInfo=new PageInfo<>(list);
			grid.setData(pageInfo.getList());
			grid.setCount(pageInfo.getTotal());
			ret.put("rows", grid.getData());
			ret.put("total", grid.getCount());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}
