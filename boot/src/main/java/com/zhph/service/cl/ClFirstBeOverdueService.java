package com.zhph.service.cl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.zhph.model.cl.ClFirstBeOverdue;
import com.zhph.model.cl.ClOrgTask;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

public interface ClFirstBeOverdueService {

	Grid<Map<String, String>> queryPageInfo(PageBean pageBean, ClFirstBeOverdue params);

	/**
	 * 导出
	 * @param data
	 * @param req
	 * @param res
	 */
	void exportExl(ClFirstBeOverdue data, HttpServletRequest req, HttpServletResponse res);

	void buildListTpl(HttpServletRequest req, ModelAndView modelAndView)throws Exception;
	
	void ClFirstBeOverduTime() throws Exception;
	
}
