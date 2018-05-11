package com.zhph.controller.cl;

import java.util.List;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.ConstantCtl;
import com.zhph.controller.sys.SysHoliadyController;
import com.zhph.model.cf.CfCardAbnormity;
import com.zhph.model.cl.ClOrgTask;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.service.cl.ClOrgTaskService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

@Controller
@RequestMapping(ConstantCtl.CL_ORGTASK)
public class ClOrgTaskController {
	
	public static final Logger logger = LogManager.getLogger(ClOrgTaskController.class);

	
	@Autowired
	private ClOrgTaskService clOrgTaskService;
	@Autowired
	private HqclcfDeptService HqclcfDeptService;
	
	@RequestMapping(value=ConstantCtl.INIT,method= RequestMethod.GET)
	public ModelAndView init (HttpServletRequest req) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		List<HqclcfDept> queryOrgParams = HqclcfDeptService.queryOrgParams();
		modelAndView.addObject("deptNoList", queryOrgParams);
		modelAndView.setViewName(ConstantCtl.INITVIEW_ORGTASK);
		clOrgTaskService.buildListTpl(req, modelAndView);
		return modelAndView;
	}
	
	@RequestMapping(value = ConstantCtl.LIST, method = RequestMethod.POST)
	@ResponseBody
	public Grid<Map<String,String>> list(PageBean pageBean,ClOrgTask params){
		Grid<Map<String, String>> grid=new Grid<>();
        try {
            grid = clOrgTaskService.queryPageInfo(pageBean, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  grid;
    }
	
	
	
	 /**
     * 导入
     * @param request
     * @return
     */
	@RequestMapping("importExl")
    @ResponseBody
    public Object importExl(MultipartFile file, HttpServletRequest req, HttpServletResponse res){
        JSONObject json=new JSONObject();
        json.put("code",200);
        try {
        	clOrgTaskService.importExl(file,req,res);
        } catch (Exception e) {
            json.put("code",500);
            json.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return json;
    }


}
