package com.zhph.service.cl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.zhph.model.cf.CfCardAbnormity;
import com.zhph.model.cl.ClOrgTask;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

public interface ClOrgTaskService {

	/**
	 * 分页
	 * @param pageBean
	 * @param params
	 * @return
	 */
	Grid<Map<String, String>> queryPageInfo(PageBean pageBean, ClOrgTask params)throws Exception;
    
	/**
     * 
     * 导入
     * @param file
     * @param req
     * @param res
     * @throws Exception
     */
    void importExl(MultipartFile file, HttpServletRequest req, HttpServletResponse res) throws Exception;

	void buildListTpl(HttpServletRequest req, ModelAndView modelAndView)throws Exception;

    
}
