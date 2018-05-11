package com.zhph.controller.cl;


import java.util.Map;

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

import com.zhph.commons.ConstantCtl;
import com.zhph.model.cl.ClFirstBeOverdue;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.model.sys.SysZhphBank;
import com.zhph.service.cl.ClFirstBeOverdueService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

@Controller
@RequestMapping(ConstantCtl.CL_FIRSTOVER)
public class ClFirstBeOverdueController {
	
	@Autowired
	private ClFirstBeOverdueService clFirstBeOverdueService;
	
	public static final Logger logger = LogManager.getLogger(ClFirstBeOverdueController.class);
	
	@RequestMapping(value=ConstantCtl.INIT,method= RequestMethod.GET)
	public ModelAndView init (HttpServletRequest req) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(ConstantCtl.INITVIEW_INITVIEW_ORGTASK);
		clFirstBeOverdueService.buildListTpl(req, modelAndView);
		return modelAndView;
	}
	
	@RequestMapping(value = ConstantCtl.LIST, method = RequestMethod.POST)
	@ResponseBody
	public Grid<Map<String, String>> list(PageBean pageBean, ClFirstBeOverdue params) throws Exception {
		Grid<Map<String, String>> grid=new Grid<>();
        try {
            grid = clFirstBeOverdueService.queryPageInfo(pageBean, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  grid;
		}
	
	@RequestMapping(value = ConstantCtl.EXPORTEXL, method = RequestMethod.GET)
    public void exportExl(ClFirstBeOverdue data, HttpServletRequest req, HttpServletResponse res) throws Exception {
		clFirstBeOverdueService.exportExl(data, req, res);
    }
	
}
