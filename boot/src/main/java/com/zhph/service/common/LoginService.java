package com.zhph.service.common;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
	public void doLogin(HttpServletRequest req, String userName, String pwd) throws Exception;
}
