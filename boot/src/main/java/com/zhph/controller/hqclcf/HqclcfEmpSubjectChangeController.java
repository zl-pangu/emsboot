package com.zhph.controller.hqclcf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.hqclcf.HqclcfEmpSubjectChange;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfEmpSubjectChangeService;
import com.zhph.util.CommonUtil;
import com.zhph.util.PageBean;

@Controller
@RequestMapping(ConstantCtl.EMPSUBJECT_CHANGE)
public class HqclcfEmpSubjectChangeController {

	@Autowired
	private BaseService baseService;

	@Autowired
	private HqclcfEmpSubjectChangeService hqclcfEmpSubjectChangeService;

	@RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest req) throws Exception {
		ModelAndView modelAndView = new ModelAndView();

		// 审核状态
		baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.EMPCHANGE_STATUS);
		baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.EMPCHANGE_STATUS);
		baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.EMP_SUBJECT);

		modelAndView.addObject("curUser", CommonUtil.getOnlineFullName());

		modelAndView.setViewName(ConstantCtl.EMPSUBJECT_CHANGE_MAIN);
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
	public Object queryList(PageBean pageBean, HqclcfEmpSubjectChange empSubjectChange, HttpServletRequest req) throws Exception {
		return hqclcfEmpSubjectChangeService.queryPageInfo(pageBean, empSubjectChange);
	}

	/**
	 * 新增、审核
	 * 
	 * @param cmd
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = ConstantCtl.EDIT, method = RequestMethod.GET)
	public ModelAndView gotoEdit(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ModelAndView modelAndView = new ModelAndView();

		String id = req.getParameter("id");

		String cmd = req.getParameter("cmd");
		modelAndView.addObject("cmd", cmd);

		baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.EMP_SUBJECT);
		baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.BUSINESS_LINE);
		baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.EMPCHANGE_STATUS);

		if ("U".equals(cmd)) {
			// 修改
			HqclcfEmpSubjectChange empSubjectChange = hqclcfEmpSubjectChangeService.getEditById(id);
			modelAndView.addObject("data", empSubjectChange);

			String empNo = empSubjectChange.getEmpNo().toString();
			modelAndView.addObject("files", hqclcfEmpSubjectChangeService.getFilesByEmpNo(empNo, id));

			modelAndView.setViewName(ConstantCtl.EMPSUBJECT_CHANGE_EDIT);

		} else if ("AU".equals(cmd)) {
			// 审核
			HqclcfEmpSubjectChange empSubjectChange = hqclcfEmpSubjectChangeService.getEditById(id);
			modelAndView.addObject("data", empSubjectChange);

			String empNo = empSubjectChange.getEmpNo().toString();
			modelAndView.addObject("files", hqclcfEmpSubjectChangeService.getFilesByEmpNo(empNo, id));

			modelAndView.setViewName(ConstantCtl.EMPSUBJECT_CHANGE_AUDIT);
		} else if ("V".equals(cmd)) {
			// 查看
			HqclcfEmpSubjectChange empSubjectChange = hqclcfEmpSubjectChangeService.getEditById(id);
			modelAndView.addObject("data", empSubjectChange);

			String empNo = empSubjectChange.getEmpNo().toString();
			modelAndView.addObject("files", hqclcfEmpSubjectChangeService.getFilesByEmpNo(empNo, id));

			modelAndView.setViewName(ConstantCtl.EMPSUBJECT_CHANGE_INFO);
		} else {
			// 新增
			modelAndView.setViewName(ConstantCtl.EMPSUBJECT_CHANGE_ADD);
		}

		return modelAndView;
	}

	/**
	 * 提交
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = ConstantCtl.SAVE, method = RequestMethod.POST)
	public JSONObject save(HttpServletRequest req, HqclcfEmpSubjectChange empSubjectChange, MultipartHttpServletRequest mhsRequest) throws Exception {
		return hqclcfEmpSubjectChangeService.saveData(req, empSubjectChange, mhsRequest);
	}

	/**
	 * 获取员工列表
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/getEmpList")
	public Object getHqclcfEmpList(HttpServletRequest req, HttpServletResponse resp, PageBean pageBean) throws Exception {
		String param = req.getParameter("q");
		return hqclcfEmpSubjectChangeService.getEmpListByType(param, pageBean);
	}

	/**
	 * 撤销
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/cancel")
	public JSONObject cancel(HttpServletRequest req) throws Exception {
		String id = req.getParameter("id");
		return hqclcfEmpSubjectChangeService.cancelData(id);
	}

	/**
	 * 删除
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/del")
	public JSONObject del(HttpServletRequest req) throws Exception {
		String id = req.getParameter("id");
		String empNo = req.getParameter("empNo");
		System.out.println(id + "==" + empNo);
		return hqclcfEmpSubjectChangeService.delData(id, empNo);
	}

}
