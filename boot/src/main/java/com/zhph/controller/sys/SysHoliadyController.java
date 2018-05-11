package com.zhph.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.sys.SysHoliday;
import com.zhph.service.sys.SysHolidayService;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

@Controller
@RequestMapping(ConstantCtl.HOLIDAY_CTLREQM)
public class SysHoliadyController {

	public static final Logger logger = LogManager.getLogger(SysHoliadyController.class);

	@Autowired
	private SysHolidayService sysHolidayService;

	/**
	 * 进入主页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/index")
	public ModelAndView gotoMain(HttpServletRequest req) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName(ConstantCtl.HOLIDAY_MAIN);
		return model;
	}

	/**
	 * 查询列表
	 * 
	 * @param pageBean
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = ConstantCtl.LIST, method = RequestMethod.POST)
	public Object queryList(PageBean pageBean, SysHoliday holiday) throws Exception {
		return sysHolidayService.queryPageInfo(pageBean, holiday);
	}

	/**
	 * 新增修改
	 * 
	 * @param cmd
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ConstantCtl.EDIT, method = RequestMethod.GET)
	public ModelAndView gotoEdit(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ModelAndView model = new ModelAndView();
		String id = req.getParameter("id");
		String cmd = req.getParameter("cmd");
		if ("U".equals(cmd)) {
			req.setAttribute("holiday", sysHolidayService.gotoEditById(id));
			model.setViewName(ConstantCtl.HOLIDAY_EDIT);
		} else {
			model.setViewName(ConstantCtl.HOLIDAY_ADD);
		}
		return model;
	}

	/**
	 * 保存
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = ConstantCtl.SAVE, method = RequestMethod.POST)
	public Json save(HttpServletRequest req) throws Exception {
		String data = req.getParameter("data");
		String cmd = req.getParameter("cmd");

		ObjectMapper mapper = new ObjectMapper();
		SysHoliday holiday = mapper.readValue(data.getBytes(), SysHoliday.class);

		return sysHolidayService.saveData(cmd, holiday);
	}

	/**
	 * 删除
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = ConstantCtl.DEL, method = RequestMethod.POST)
	public Json del(HttpServletRequest req) throws Exception {
		JSONObject jsonData = JSONObject.parseObject(req.getParameter("data"));
		return sysHolidayService.delData(jsonData.getString("id"));
	}

	@ResponseBody
	@RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.POST)
	public Json autoDate(HttpServletRequest req) throws Exception {
		String year = req.getParameter("year");
		return sysHolidayService.autoHoliday(year);
	}

}
