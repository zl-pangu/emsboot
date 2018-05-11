package com.zhph.controller.cf;

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
import com.zhph.model.cf.CfOfflaterSet;
import com.zhph.service.cf.CfOfflaterSetService;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

@Controller
@RequestMapping(ConstantCtl.CF_OFFLATER_CTLREQM)
public class CfOfflaterSetController {

	public static final Logger logger = LogManager.getLogger(CfOfflaterSetController.class);

	@Autowired
	private CfOfflaterSetService cfOfflaterSetService;

	/**
	 * 进入主页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/index")
	public ModelAndView gotoMain(HttpServletRequest req) throws Exception {
		ModelAndView model = new ModelAndView();
		model.setViewName(ConstantCtl.CF_OFFLATER_MAIN);
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
	public Object queryList(PageBean pageBean, CfOfflaterSet cfOfflaterSet) throws Exception {
		return cfOfflaterSetService.queryPageInfo(pageBean, cfOfflaterSet);
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
			req.setAttribute("data", cfOfflaterSetService.gotoEditById(id));
			model.setViewName(ConstantCtl.CF_OFFLATER_EDIT);
		} else {
			model.setViewName(ConstantCtl.CF_OFFLATER_ADD);
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
		CfOfflaterSet cfOfflaterSet = mapper.readValue(data.getBytes(), CfOfflaterSet.class);

		JSONObject json = cfOfflaterSetService.IfCheckIn(cfOfflaterSet);
		if ("false".equals(json.get("ifExists"))) {
			return cfOfflaterSetService.saveData(cmd, cfOfflaterSet);
		} else {
			Json js = new Json();
			js.setMsg(json.getString("msg"));
			js.setSuccess(false);
			return js;
		}

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
		return cfOfflaterSetService.delData(jsonData.getString("id"));
	}

}
