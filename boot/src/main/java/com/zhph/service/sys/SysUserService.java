package com.zhph.service.sys;

import com.github.pagehelper.Page;
import com.zhph.exception.AppException;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.sys.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SysUserService {
	public Page<SysUser> queryAllUserByInfo(SysUser params, Integer pageNum, Integer pageSize) throws Exception;

	public void deleteUsers(Long[] ids) throws AppException;

	public SysUser queryUserById(Long id) throws Exception;

	public void saveEdit(String data) throws Exception;

	public void saveAdd(String data) throws Exception;

	public void updateLoginErrorCount(SysUser params) throws Exception;

	public void saveChangePwd(String userName, String pwd, String newPwd, String rePwd) throws Exception;

	void buildUserSelectBl(SysUser user, HttpServletRequest req) throws Exception;

    List<HqclcfEmp> queryEmpByBl(String q) throws Exception;
}
