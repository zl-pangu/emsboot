package com.zhph.controller.hqclcf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.hqclcf.HqclcfJobTransfer;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfEmpApvService;
import com.zhph.service.hqclcf.HqclcfJobTransferService;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(ConstantCtl.HQCLCF_JOBTRANS)
public class HqclcfJobTransferController {
    @Autowired
    private BaseService baseService;
    @Autowired
    private HqclcfJobTransferService hqclcfJobTransferService;
    @Resource
    private HqclcfEmpApvService empApvService;
   

    @RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest req) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        baseService.addObject(modelAndView, Constant.CODE_LIST, Constant.BUSINESS_LINE);
        baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);
        baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.EMP_SUBJECT);//增加主体
        modelAndView.setViewName(ConstantCtl.INITVIEW_JOBTRANS);
        hqclcfJobTransferService.showBtnList(req,modelAndView);
        hqclcfJobTransferService.buildListTpl(req, modelAndView);
        return modelAndView;
    }

    @RequestMapping(value = ConstantCtl.LIST, method = RequestMethod.POST)
    @ResponseBody
    public Grid<HqclcfJobTransfer> list(PageBean pageBean, HqclcfJobTransfer params) throws Exception {
        Grid<HqclcfJobTransfer> grid = hqclcfJobTransferService.queryPageInfo(pageBean, params);
        return grid;
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
        model.setViewName(ConstantCtl.INITVIEW_JOBTRANS_ADD);//指定新增页面
        return model;
    }

    @RequestMapping(value = ConstantCtl.ADD, method = RequestMethod.POST)
    @ResponseBody
    public Object add(HqclcfJobTransfer data,HttpServletRequest res) throws Exception {
        JSONObject obj=new JSONObject();
        obj.put("success",true);
        try {
        	hqclcfJobTransferService.add(data, res);
        } catch (Exception e) {
            obj.put("success",false);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
  	    return obj;
    }
    
    /**
     * 修改初始化
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("editInit")
    public ModelAndView editInit(HttpServletRequest req,Long id) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);//业务条线初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.DIMISSION_TYPE);//离职淘汰原因初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.DIMISSION_TYPE);//离职淘汰原因初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.EMP_SUBJECT);
        baseService.addObject(model, Constant.CODE_LIST, Constant.EMP_SUBJECT);
        hqclcfJobTransferService.buildEditFormReult(id,req);
        model.setViewName(ConstantCtl.INITVIEW_JOBTRANS_EDIT);//指定修改页面
        return model;
    }
    
    /**
     * 详情初始化
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("detailInit")
    public ModelAndView detailInit(HttpServletRequest req,Long id) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);//业务条线初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.DIMISSION_TYPE);//离职淘汰原因初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.DIMISSION_TYPE);//离职淘汰原因初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.EMP_SUBJECT);
        baseService.addObject(model, Constant.CODE_LIST, Constant.EMP_SUBJECT);
        hqclcfJobTransferService.buildEditFormReult(id,req);
        model.setViewName(ConstantCtl.INITVIEW_JOBTRANS_DETAIL);//指定详情页面
        return model;
    }

    @RequestMapping(value = ConstantCtl.EDIT, method = RequestMethod.POST)
    @ResponseBody
    public Object editIint(HttpServletRequest req,HqclcfJobTransfer data) throws Exception {
    	 JSONObject obj=new JSONObject();
         obj.put("success",true);
         try {
        	 hqclcfJobTransferService.editById(req,data);
         } catch (Exception e) {
             obj.put("success",false);
             obj.put("msg",e.getMessage());
             e.printStackTrace();
         }
   	    return obj;
        //return hqclcfJobTransferService.editById(data);
    }


    @RequestMapping("/queryjobTransferInfo")
    @ResponseBody
    public Object queryjobTransferInfo(@Param("empName") String empName, @Param("empNo") String empNo) {
        return hqclcfJobTransferService.queryjobTransferEmpByEmpNameOrNo(empName, empNo);
    }

    @RequestMapping("/queryParentDepts")
    @ResponseBody
    public JSONObject queryParentDepts(@Param("userId") String userId, @Param("deptId") Long deptId, @Param("deptCode") String deptCode) {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jsonArray = hqclcfJobTransferService.getParentDeptTree(userId, deptId, deptCode);
            //获取上级部门
            obj.put("data", jsonArray);
            obj.put("code", 200);
            obj.put("msg", "读取数据成功!");
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
            return obj;
        }
        return obj;
    }

    @RequestMapping("/queryBuslineBydeptId")
    @ResponseBody
    public Object queryjobTransferInfo(@Param("deptId") Long deptId) {
        return hqclcfJobTransferService.getBusLine(deptId);
    }
    
    /**
     * 构建下拉数据
     * @param data
     * @return
     * @throws Exception
     */
    @RequestMapping("getSelectData")
    @ResponseBody
    public Object getSelectData(@Param("data") String data) throws Exception{
        return empApvService.bulidSelectDataByDeptId(data);
    }
    
    /**
     * 员工姓名或者，编号查询员工信息
     * @param data
     * @param q
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryByq")
    @ResponseBody
    public Object queryByq(@Param("data") String data, @Param("q") String q, @Param("rows") int rows,@Param("page") int page) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = hqclcfJobTransferService.queryAllHqclcfJob(data, q);
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
}
