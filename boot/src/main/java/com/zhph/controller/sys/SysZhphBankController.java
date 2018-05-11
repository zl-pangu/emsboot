package com.zhph.controller.sys;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.sys.SysZhphBank;
import com.zhph.service.sys.SysZhphBankService;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;


@Controller
@RequestMapping(ConstantCtl.BANK_CTLREQM)
public class SysZhphBankController {

    @Autowired
    private SysZhphBankService sysZhphBankService;


    @RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest req) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ConstantCtl.INITVIEW_BANKNAME);
        sysZhphBankService.showBtnList(req,modelAndView);
        return modelAndView;
    }

    @RequestMapping(value = ConstantCtl.LIST, method = RequestMethod.POST)
    @ResponseBody
    public Grid<SysZhphBank> list(PageBean pageBean, SysZhphBank params) throws Exception {
        Grid<SysZhphBank> grid = sysZhphBankService.queryPageInfo(pageBean, params);
        return grid;
    }

    @RequestMapping(value = ConstantCtl.DEL, method = RequestMethod.POST)
    @ResponseBody
    public Object del(@Param("data") String data){
    	JSONObject obj=new JSONObject();
    	obj.put("success", false);
        ObjectMapper mapper = new ObjectMapper();
        try {
        	sysZhphBankService.del(mapper.readValue(data.getBytes("utf-8"), SysZhphBank.class));			
		} catch (Exception e) {
			obj.put("success", true);
			obj.put("msg", e.getMessage());
			e.printStackTrace();
		}
        return obj;
    }


    @RequestMapping(value = ConstantCtl.ADD, method = RequestMethod.POST)
    @ResponseBody
    public Json add(@Param("data") String data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return sysZhphBankService.add(mapper.readValue(data.getBytes("utf-8"), SysZhphBank.class));
    }

    @RequestMapping(value = ConstantCtl.EXPORTEXL, method = RequestMethod.GET)
    public void exportExl(SysZhphBank data, HttpServletRequest req, HttpServletResponse res) throws Exception {
        sysZhphBankService.exportExl(data, req, res);
    }

    @RequestMapping(value = ConstantCtl.EDIT, method = RequestMethod.POST)
    @ResponseBody
    public Json editIint(@Param("data") String data) throws Exception {
        return sysZhphBankService.editById(data);
    }

    @RequestMapping("/checkBankCodeIsExist")
    @ResponseBody
    public Object checkBankCode(@Param("bankCode") String bankCode) {
        return sysZhphBankService.checkBank(bankCode);
    }
}
