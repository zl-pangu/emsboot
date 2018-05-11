package com.zhph.controller.sys;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.service.common.BaseService;
import com.zhph.service.sys.SysWorkplacesetService;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zhouliang on 2017/11/23.
 */
@Controller
@RequestMapping(ConstantCtl.WORKPLACE_CTLREQM)
public class SysWorkplacesetController {
    public static final Logger logger = LogManager.getLogger(SysWorkplacesetController.class);
    @Autowired
    private SysWorkplacesetService sysWorkplaceService;

    @Autowired
    private BaseService baseService;

    @RequestMapping(value = ConstantCtl.INIT,method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest req)throws Exception{
        ModelAndView model=new ModelAndView();
        model.addObject(ConstantCtl.PROVINCELIST_KEY,sysWorkplaceService.queryAllProvince());
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.CITY_LEVEL_HQ);
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.CITY_LEVEL_XJ);
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.CITY_LEVEL_XD);
        model.setViewName(ConstantCtl.INITVIEW_NAME);

        return model;

    }

    @RequestMapping(value = ConstantCtl.LIST,method = RequestMethod.POST)
    @ResponseBody
    public Grid<SysWorkplaceset> list(PageBean pageBean,SysWorkplaceset params){
        Grid<SysWorkplaceset>  grid = new Grid<>();
        try {
            grid = sysWorkplaceService.queryPageInfo(pageBean, params);
        } catch (Exception e) {
            logger.error("工作地查询失败！");
            e.printStackTrace();
        }
        return grid;
    }


    @RequestMapping(value = ConstantCtl.DEL,method =RequestMethod.POST)
    @ResponseBody
    public Json del(@Param("data")String data) throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        return sysWorkplaceService.del(mapper.readValue(data.getBytes(), SysWorkplaceset.class));
    }


    @RequestMapping(value = ConstantCtl.ADD, method = RequestMethod.POST)
    @ResponseBody
    public Json add(@Param("data") String data){
        Json json=new Json();
        ObjectMapper mapper = new ObjectMapper();
        try {
            sysWorkplaceService.add(mapper.readValue(data.getBytes(), SysWorkplaceset.class));
            json.setMsg("新增成功！");
        } catch (Exception e) {
            json.setSuccess(true);
            json.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping(value = ConstantCtl.EDIT,method = RequestMethod.POST)
    @ResponseBody
    public Json editIint(@Param("data") String data){
        Json json=new Json();
        try {
            sysWorkplaceService.editById(data);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping(value =ConstantCtl.EXPORTEXL,method = RequestMethod.GET)
    public void exportExl(SysWorkplaceset data, HttpServletRequest req, HttpServletResponse res){
        try {
            sysWorkplaceService.exportExl(data,req,res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/queryWorkPlaceByParam")
    @ResponseBody
    public Object queryWorkPlaceByParam(@Param("data") String data) throws Exception{
        return sysWorkplaceService.queryWorkPlaceByParam(data);
    }

    @RequestMapping("blSelect")
    @ResponseBody
    public Object blSelect(String businessLine) throws Exception{
        return  sysWorkplaceService.buildCityLevelByBl(businessLine);
    }
}
