package com.zhph.controller.hqclcf;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfLeave;
import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import com.zhph.model.sys.SysUser;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfLeaveService;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(ConstantCtl.HQCLCF_LEAVE)
public class HqclcfLeaveController {
    @Autowired
    private BaseService baseService;
    @Autowired
    private HqclcfLeaveService hqclcfLeaveService;


    @RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest req) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.BUSINESS_LINE);
        baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);
        baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.DIMISSION_TYPE);
        baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.DIMISSION_TYPE);
        baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.APP_STATUS);
        baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.APP_STATUS);
        baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.STATUS);
        baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.STATUS);
        baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.EMP_SUBJECT);
        //获取按钮权限
        hqclcfLeaveService.buildListTpl(req, modelAndView);
        modelAndView.setViewName(ConstantCtl.INITVIEW_HQCLCF);//指定新增页面
        return modelAndView;
    }

    @RequestMapping(value = ConstantCtl.LIST, method = RequestMethod.POST)
    @ResponseBody
    public Grid<HqclcfLeave> list(PageBean pageBean, HqclcfLeave params) throws Exception {
        Grid<HqclcfLeave> grid = hqclcfLeaveService.queryPageInfo(pageBean, params);
        return grid;
    }


    @RequestMapping(value = ConstantCtl.EXPORTEXL, method = RequestMethod.GET)
    public void exportExl(HqclcfLeave data, HttpServletRequest req, HttpServletResponse res) throws Exception {
        hqclcfLeaveService.exportExl(data, req, res);
    }

    @RequestMapping(value = ConstantCtl.EDIT)
    @ResponseBody
    public Object edit(HqclcfLeave leave,MultipartHttpServletRequest multipartHttpServletRequest,HttpServletRequest req){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try {
        	hqclcfLeaveService.editById(leave, multipartHttpServletRequest,req);
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

  
    
    /**
     * 新增初始化
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addInit")
    public ModelAndView addInit(HttpServletRequest request) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);//业务条线初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.DIMISSION_TYPE);//离职淘汰原因初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.DIMISSION_TYPE);//离职淘汰原因初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.EMP_SUBJECT);
        hqclcfLeaveService.queryAllOpenTypes(model,new Long(5));
        model.setViewName(ConstantCtl.HQCLC_LEAVE_ADD);//指定新增页面
        return model;
    }
    


    @RequestMapping(value = ConstantCtl.ADD)
    @ResponseBody
    public Object add(HqclcfLeave leave,MultipartHttpServletRequest request,HttpServletRequest req) throws Exception {
      JSONObject obj=new JSONObject();
      obj.put("success",true);
      try {
    	  hqclcfLeaveService.add(leave, req,request);
      } catch (Exception e) {
          obj.put("success",false);
          obj.put("msg",e.getMessage());
          e.printStackTrace();
      }
	return obj;
    }
    

    @RequestMapping(value = ConstantCtl.DEL, method = RequestMethod.POST)
    @ResponseBody
    public Json del(@Param("data") String data) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return hqclcfLeaveService.del(mapper.readValue(data.getBytes("utf-8"), HqclcfLeave.class));
    }
    
    @RequestMapping("detail")
    public ModelAndView detail(HttpServletRequest req,Long id) throws Exception{
        ModelAndView model=new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.EMP_SUBJECT);//主体
        baseService.addObject(model, Constant.CODE_LIST, Constant.DIMISSION_TYPE);//离职原因
        baseService.addObject(model, Constant.CODE_LIST, Constant.APP_STATUS);
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.APP_STATUS);
        hqclcfLeaveService.buildEditFormReult(id,req);
        model.setViewName(ConstantCtl.HQCLC_LEAVE_DETAIL);
        return model;
    }
    
    @RequestMapping("appInit")
    public ModelAndView appInit(HttpServletRequest req,Long id) throws Exception{
        ModelAndView model=new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.EMP_SUBJECT);//主体
        baseService.addObject(model, Constant.CODE_LIST, Constant.DIMISSION_TYPE);//离职原因
        baseService.addObject(model, Constant.CODE_LIST, Constant.APP_STATUS);
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.APP_STATUS);
        hqclcfLeaveService.buildEditFormReult(id,req);
        model.setViewName(ConstantCtl.HQCLC_LEAVE_APP);
        return model;
    }
    
    @RequestMapping("editInit")
    public ModelAndView editInit(HttpServletRequest req,Long id) throws Exception{
        ModelAndView model=new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.EMP_SUBJECT);//主体
        baseService.addObject(model, Constant.CODE_LIST, Constant.DIMISSION_TYPE);//离职原因
        baseService.addObject(model, Constant.CODE_LIST, Constant.APP_STATUS);
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.APP_STATUS);
        hqclcfLeaveService.buildEditFormReult(id,req);
        model.setViewName(ConstantCtl.HQCLC_LEAVE_EDIT);
        return model;
    }

    
    
    @RequestMapping(value = ConstantCtl.APP)
    @ResponseBody
    public Object app(@Param("id") Long id, HqclcfLeave leave,HttpServletRequest req) throws Exception {
        SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");
        return hqclcfLeaveService.appById(id, leave,onlineUser,req);
    }
    
    /**
     * 员工姓名或者，编号查询员工信息
     * @param data
     * @param q
     * @return
     * @throws Exception
     */
//    @RequestMapping("/queryByq")
//    @ResponseBody
//    public Object queryByq(@Param("data") String data, @Param("q") String q) throws Exception {
//        return hqclcfLeaveService.queryAllLeave(data, q);
//    }
    @RequestMapping("queryByq")
    @ResponseBody
    public Object queryEmp(String q, @Param("rows") int rows,@Param("page") int page) throws Exception{
        JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, String>> grid=new Grid<>();
        List<Map<String, String>> lists = hqclcfLeaveService.queryAllLeave(q);
        PageInfo<Map<String, String>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
        return obj;
    }
    
    

    @RequestMapping("/preViewFile")
    public void previewFile(HqclcfEmpFile hqclcfEmpFile, HttpServletResponse res) {
        hqclcfLeaveService.preViewFile(hqclcfEmpFile, res);
    }

    @RequestMapping("/delFile")
    @ResponseBody
    public Json delFile(HqclcfEmpFile hqclcfEmpFile) {
        return hqclcfLeaveService.delFile(hqclcfEmpFile);
    }

    @RequestMapping("/queryFiles")
    @ResponseBody
    public Object queryFiles(@Param("empNo") String empNo, @Param("busLine") String busLine) {
        return hqclcfLeaveService.queryfileName(empNo, busLine);
    }
    
}
