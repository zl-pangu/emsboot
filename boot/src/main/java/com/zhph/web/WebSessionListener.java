package com.zhph.web;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class WebSessionListener implements HttpSessionListener {
	@Override
	public void sessionCreated(HttpSessionEvent e) {
		HttpSession session = e.getSession();
		System.out.println("创建会话：" + session.getId());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent e) {
		HttpSession session = e.getSession();
		System.out.println("销毁会话：" + session.getId());
	}

}
