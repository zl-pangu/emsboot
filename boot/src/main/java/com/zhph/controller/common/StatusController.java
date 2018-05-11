package com.zhph.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhph.annotation.IgnoreLogin;

@Controller
public class StatusController {
	@IgnoreLogin
	@RequestMapping("/404")
	public Object index404(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "/pages/common/404";
	}

	@IgnoreLogin
	@RequestMapping("/loginError")
	public Object loginError(HttpServletRequest req, HttpServletResponse res) throws Exception {
		return "/pages/common/loginError";
	}

}
