package com.zhph.controller.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhph.annotation.IgnoreLogin;
import com.zhph.exception.AppException;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysUser;
import com.zhph.service.common.LoginService;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.service.sys.SysUserService;
import com.zhph.util.StringUtil;
import com.zhph.util.VerifyCodeUtils;

@Controller
public class LoginController {
	private static String LOGIN_PAGE = "login";
	private static String INDEX_PAGE = "index";

	@Autowired
	private LoginService loginService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysConfigTypeService sysConfigTypeService;

	public static final Logger logger = LogManager.getLogger(LoginController.class);

	@IgnoreLogin
	@RequestMapping("/")
	public Object index(HttpServletRequest req, HttpServletResponse res) throws Exception {
		HttpSession session = req.getSession();
		SysUser onlineUser = (SysUser) session.getAttribute("onlineUser");

		long id = 282;
		SysConfigType sysConfigType = sysConfigTypeService.queryObjById(id);
		Integer if_yzm = sysConfigType.getSysValue();
		req.setAttribute("IF_YZM", if_yzm);

		if (onlineUser == null)
			return LOGIN_PAGE;
		else {
			req.setAttribute("onlineUser", onlineUser);
			return INDEX_PAGE;
		}
	}

	@IgnoreLogin
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object doLogin(HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map<String, Object> obj = new HashMap<>();
		obj.put("code", 200);

		long id = 282;
		SysConfigType sysConfigType = sysConfigTypeService.queryObjById(id);
		Integer if_yzm = sysConfigType.getSysValue();

		String userName = req.getParameter("userName");
		String pwd = req.getParameter("pwd");
		String yzm = req.getParameter("yzm");

		String yzm_v = (String) req.getSession().getAttribute("yzm_v");
		try {
			if (StringUtil.isEmpty(userName)) {
				throw new AppException("登录名不能为空！", 511);
			}
			if (StringUtil.isEmpty(pwd)) {
				throw new AppException("密码不能为空！", 511);
			}
			if (if_yzm != 0) {
				if (StringUtil.isEmpty(yzm)) {
					throw new AppException("验证码不能为空！", 511);
				}
				if (!yzm.toLowerCase().equals(yzm_v.toLowerCase())) {
					throw new AppException("验证码不正确！", 511);
				}
			}

			loginService.doLogin(req, userName, pwd);

		} catch (AppException e) {
			if (e.getStatus() == 531) {
				SysUser params = new SysUser();
				params.setUserName(userName);
				params.setIsEnable(0);
				sysUserService.updateLoginErrorCount(params);
			} else if (e.getStatus() == 532) {
				SysUser params = new SysUser();
				params.setUserName(userName);
				sysUserService.updateLoginErrorCount(params);
			}
			// 捕获的异常截取有用的信息段输出
			obj.put("code", e.getStatus());
			obj.put("msg", e.getMessage());
		}
		return obj;
	}

	@ResponseBody
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public Object doLogout(HttpServletRequest req, HttpServletResponse res) throws Exception {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("code", 200);

		HttpSession session = req.getSession();
		session.invalidate();
		obj.put("msg", "注销成功！");

		return obj;
	}

	@IgnoreLogin
	@ResponseBody
	@RequestMapping(value = "/getYzm", method = RequestMethod.GET)
	public void getYzm(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String verifyCode = VerifyCodeUtils.generateVerifyCode(5);
		VerifyCodeUtils.outputImage(90, 31, res.getOutputStream(), verifyCode);
		req.getSession().setAttribute("yzm_v", verifyCode);
	}
}
