package com.zhph.controller.hqclcf;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
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
import com.zhph.mapper.sys.SysZhphBankMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.sys.SysZhphBank;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfEmpApvService;
import com.zhph.service.hqclcf.HqclcfEmpService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

@Controller
@RequestMapping(ConstantCtl.HQCLC_EMP)
public class HqclcfEmpController {

	@Autowired
	private HqclcfEmpService hqclcfEmpAppService;
	@Resource
	private HqclcfEmpApvService empApvService;
	@Autowired
	private BaseService baseService;
	@Autowired
	private SysZhphBankMapper bankMapper;

	@RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
	public ModelAndView init(HttpServletRequest req) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.BUSINESS_LINE);
		baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.EMP_TYPE);
		baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.JOB_STATUS);

		baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);
		baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.EMP_TYPE);
		baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.JOB_STATUS);

		hqclcfEmpAppService.queryAllOpenTypes(modelAndView);

		modelAndView.addObject("blselectList", baseService.buildBlByUserAndShowEnable());

		modelAndView.setViewName(ConstantCtl.INITVIEW_EMP);
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = ConstantCtl.LIST, method = RequestMethod.POST)
	public Grid<HqclcfEmp> list(PageBean pageBean, HqclcfEmp params) throws Exception {
		Grid<HqclcfEmp> gird = hqclcfEmpAppService.queryPageInfo(pageBean, params);
		return gird;
	}

	@RequestMapping(value = ConstantCtl.EDITINIT, method = RequestMethod.GET)
	public ModelAndView editInit(HttpServletRequest req) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		// 员工信息
		String id = req.getParameter("id");
		String cmd = req.getParameter("cmd");

		if ("U".equals(cmd)) {
			empApvService.buildEditFormReult(Long.valueOf(id), req);
			buildAddInitReq(modelAndView);
			modelAndView.setViewName(ConstantCtl.INITVIEW_EMP_EDIT);
		} else {

			HqclcfEmp hqclcfEmp = hqclcfEmpAppService.gotoEditById(id);
			// 把对象中code转化成Name
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("sex", Constant.SEX);
			map.put("marriage", Constant.MARRIAGE);
			map.put("nativeType", Constant.NATIVE_TYPE);
			map.put("idType", Constant.ID_TYPE);
			map.put("nation", Constant.NATION);
			map.put("urgencyRelation", Constant.URGENCY_RELATION);
			map.put("edu", Constant.EDU);
			map.put("eduType", Constant.EDU_TYPE);
			map.put("empSubject", Constant.EMP_SUBJECT);
			map.put("businessLine", Constant.BUSINESS_LINE);
			map.put("empType", Constant.EMP_TYPE);
			if ("1".equals(hqclcfEmp.getBusinessLine().toString())) {
				map.put("cityLevel", Constant.CITY_LEVEL_HQ);
			} else if ("2".equals(hqclcfEmp.getBusinessLine().toString())) {
				map.put("cityLevel", Constant.CITY_LEVEL_XJ);
			} else if ("3".equals(hqclcfEmp.getBusinessLine().toString())) {
				map.put("cityLevel", Constant.CITY_LEVEL_XD);
			}

			JSONObject jsonobj = baseService.changeCodeToName(hqclcfEmp, map);
			hqclcfEmp = (HqclcfEmp) JSONObject.toJavaObject(jsonobj, HqclcfEmp.class);

			// 银行卡名称CODE转NAME
			if (bankMapper.queryZhphBankByCode(jsonobj.get("pfBankCode").toString()).size() > 0 && jsonobj.get("pfBankCode").toString() != "" && jsonobj.get("pfBankCode").toString() != "null") {
				hqclcfEmp.setPfBankCode_name(bankMapper.queryZhphBankByCode(jsonobj.get("pfBankCode").toString()).get(0).getBankName());
			}

			// 获取正合工作经历
			hqclcfEmp.setZHWorkExp(hqclcfEmpAppService.getZhWorkExpByEmpNo(hqclcfEmp.getEmpNo()));

			modelAndView.addObject("data", hqclcfEmp);

			// 获取附件
			String empNo = hqclcfEmp.getEmpNo();
			List<HqclcfEmpFile> ls = hqclcfEmpAppService.getFilesByEmpNo(empNo);

			modelAndView.addObject("files", ls);
			modelAndView.setViewName(ConstantCtl.INITVIEW_EMP_DET);
		}
		return modelAndView;
	}

	@RequestMapping(value = ConstantCtl.DETINIT, method = RequestMethod.GET)
	public ModelAndView detInit(HttpServletRequest req) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		buildAddInitReq(modelAndView);
		modelAndView.setViewName(ConstantCtl.INITVIEW_EMP_DET);
		return modelAndView;
	}

	@ResponseBody
	@RequestMapping(value = ConstantCtl.SAVE, method = RequestMethod.POST)
	public Object save(HqclcfEmp hqclcfEmp, MultipartHttpServletRequest mhsRequest) throws Exception {
		return hqclcfEmpAppService.saveData(hqclcfEmp, mhsRequest);
	}

	@RequestMapping(value = ConstantCtl.EXPORTEXL, method = RequestMethod.GET)
	public void exportExl(HqclcfEmp data, HttpServletRequest req, HttpServletResponse res) throws Exception {
		hqclcfEmpAppService.exportExl(data, req, res);
	}

	@ResponseBody
	@RequestMapping("getCitySelectData")
	public Object getCitySelectData(HttpServletRequest req) throws Exception {
		return hqclcfEmpAppService.getCitySelectData(req.getParameter("bl"));
	}

	private void buildAddInitReq(ModelAndView model) throws Exception {
		String[] str = new String[] { Constant.SEX, Constant.MARRIAGE, Constant.NATIVE_TYPE, Constant.ID_TYPE, Constant.NATION, Constant.URGENCY_RELATION, Constant.EDU, Constant.EDU_TYPE, Constant.EMP_SUBJECT, Constant.BUSINESS_LINE, Constant.EMP_TYPE };
		for (int i = 0; i < str.length; i++) {
			baseService.addObject(model, Constant.CODE_LIST, str[i]);
		}
		SysZhphBank bank = new SysZhphBank();
		bank.setStatus("1");
		model.addObject("bankList", bankMapper.queryAllZhphBank(bank));
	}
}
