package com.zhph.controller.sys;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.dto.SysConfigAddParams;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.util.CommonUtil;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouliang on 2018/2/6.
 */
@RequestMapping("/sys/configType")
@Controller
public class SysConfigTypeController {

    public static final Logger logger = LogManager.getLogger(SysConfigTypeController.class);

    @Resource
    private SysConfigTypeService sysConfigTypeService;

    /**
     * 初始化页面
     * @return
     */
    @RequestMapping("init")
    public String init(){
        return "/pages/sys/configType/sys_configType_index";
    }

    /**
     * 数据字典查询
     * @param configType
     * @param pageBean
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Object list(SysConfigType configType, PageBean pageBean) {
        Grid<SysConfigType> grid=new Grid<>();
        try {
            grid=sysConfigTypeService.queryPageInfo(pageBean,configType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grid;
    }

    /**
     * 页面跳转
     * @return
     */
    @RequestMapping("pageJump")
    public ModelAndView pageJump(HttpServletRequest req, @Param("jumpType") String jumpType) {
        ModelAndView model=new ModelAndView();
        try {
            switch (jumpType){
                case "add":
                    model.setViewName("/pages/sys/configType/sys_configType_add");
                    break;
                case "edit":
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * 查询出我们要新建的字典类型
     */
    @RequestMapping("queryPno")
    @ResponseBody
    public Object queryPno(@Param("q")String q){
        List<SysConfigType> list=new ArrayList<>();
        try {
            list = sysConfigTypeService.queryPConfigType(q);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @RequestMapping("queryOrder")
    @ResponseBody
    public Object queryOrder(@Param("sysCode")String sysCode){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try {
            obj.put("data",sysConfigTypeService.queryOrder(sysCode));
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("dictionaryCoding")
    @ResponseBody
    public Object dictionaryCoding(@Param("sysName")String sysName,@Param("pCode")String pCode){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try {
            obj.put("data",sysConfigTypeService.dictionaryCoding(sysName, pCode));
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     *新增
     * @param data
     */
    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(@Param("data")String data){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try {
            sysConfigTypeService.add(data);
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 删除
     * @param id
     */
    @RequestMapping("del")
    @ResponseBody
    public JSONObject del(@Param("id")Long id){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try{
            sysConfigTypeService.del(id);
        }catch (Exception e){
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }


    @RequestMapping("editInit")
    public String editInit(@Param("id") Long id,HttpServletRequest request) {
        try {
            SysConfigType configType = sysConfigTypeService.queryObjById(id);
            request.setAttribute("configType",configType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/pages/sys/configType/sys_configType_edit";
    }

    /**
     * 修改
     * @param data
     * @return
     */
    @RequestMapping("edit")
    @ResponseBody
    public JSONObject edit(@Param("data")String data){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try{
            sysConfigTypeService.update(data);
        }catch (Exception e){
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
}
