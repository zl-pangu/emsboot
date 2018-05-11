package com.zhph.controller.cl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zhph.commons.ConstantCtl;
import com.zhph.model.cl.ClCreditCoremanagerM2Det;
import com.zhph.service.cl.ClSixMonthM2Service;
import com.zhph.util.PageBean;

@Controller
@RequestMapping(ConstantCtl.CL_SIXMONTHM2)
public class ClSixMonthM2Controller {

	@Autowired
	private ClSixMonthM2Service clSixMonthM2Service;

	/**
	 * 进入主页
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest req) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(ConstantCtl.CL_SIXMONTHM2_MAIN);
		return modelAndView;
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
	public Object queryList(PageBean pageBean, ClCreditCoremanagerM2Det clSixMonthM2) throws Exception {
		return clSixMonthM2Service.queryPageInfo(pageBean, clSixMonthM2);
	}

	/**
	 * 导出
	 * 
	 * @param hqclcfEmp
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	@RequestMapping(value = ConstantCtl.EXPORTEXL, method = RequestMethod.GET)
	public void exportExl(ClCreditCoremanagerM2Det clSixMonthM2, HttpServletRequest req, HttpServletResponse res) throws Exception {
		clSixMonthM2Service.exportExl(clSixMonthM2, req, res);
	}
}
