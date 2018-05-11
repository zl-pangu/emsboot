package com.zhph.controller.cf;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.annotation.SameUrlData;
import com.zhph.model.cf.CfCardAbnormity;
import com.zhph.model.cf.dto.CfCardAbResult;
import com.zhph.model.hqclcf.HqclcfGzym;
import com.zhph.service.cf.CfCardAbnormityService;
import com.zhph.service.cl.HqclcfGzymService;
import com.zhph.service.common.BaseService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouliang on 2018/1/2.
 */
@Controller
@RequestMapping("/cf/cardAbnormity")
public class CfCardAbnormityController {
    @Resource
    private CfCardAbnormityService cfCardAbnormityService;
    @Resource
    private BaseService baseService;
    @Resource
    private HqclcfGzymService hqclcfGzymService;

    @RequestMapping("/init")
    public ModelAndView init(HttpServletRequest req) throws Exception{
        ModelAndView model=new ModelAndView();
        baseService.buildCfInitReq(req);
        HqclcfGzym hqclcfGzym = hqclcfGzymService.queryCurrGzym();
        model.addObject("gzYm",hqclcfGzym.getCurrentGzym());
        model.setViewName("/pages/cf/cardAbnormity/cf_cardAbnormity_index");
        return model;
    }

    @RequestMapping("list")
    @ResponseBody
    public Grid<Map<String,String>> list(PageBean pageBean,CfCardAbnormity card){
        Grid<Map<String, String>> grid=new Grid<>();
        try {
            grid = cfCardAbnormityService.queryPageInfo(pageBean, card);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  grid;
    }

    @RequestMapping("addInit")
    public ModelAndView addInit() throws Exception{
        ModelAndView model=new ModelAndView();
        model.setViewName("/pages/cf/cardAbnormity/cf_cardAbnormity_add");
        return  model;
    }

    @RequestMapping("queryEmp")
    @ResponseBody
    public Object queryEmp(String q, @Param("rows") int rows,@Param("page") int page) throws Exception{
        JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, String>> grid=new Grid<>();
        List<Map<String, String>> lists = cfCardAbnormityService.queryEmpByQAndGetData(q);
        PageInfo<Map<String, String>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
        return obj;
    }

    @SameUrlData
    @RequestMapping("add")
    @ResponseBody
    public JSONObject add(@Param("data")String data){
        JSONObject json=new JSONObject();
        json.put("code",200);
        try {
            cfCardAbnormityService.addCardAbnormity(data);
        } catch (Exception e) {
            json.put("code",500);
            json.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return  json;
    }

    @RequestMapping("importExl")
    @ResponseBody
    public Object importExl(MultipartFile file, HttpServletRequest req, HttpServletResponse res){
        JSONObject json=new JSONObject();
        json.put("code",200);
        try {
            cfCardAbnormityService.importExl(file,req,res);
        } catch (Exception e) {
            json.put("msg",e.getMessage());
            json.put("code",500);
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping("buildDeptSelect")
    @ResponseBody
    public Object buildDeptSelect(Long id){
        CfCardAbResult result=new CfCardAbResult();
        try {
            result = cfCardAbnormityService.buildDeptSelect(id);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  result;
    }

    @RequestMapping("exportExl")
    @ResponseBody
    private JSONObject exportExl(CfCardAbnormity cardAbnormity,HttpServletResponse res){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try {
            cfCardAbnormityService.exportExl(cardAbnormity,res);
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("del")
    @ResponseBody
    private Object del(Long id){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try {
            cfCardAbnormityService.delele(id);
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }



    @RequestMapping("editInit")
    public String editInit(HttpServletRequest req,Long id){
        try {
            cfCardAbnormityService.bulidInitAtr(req,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/pages/cf/cardAbnormity/cf_cardAbnormity_edit";
    }

    @RequestMapping("edit")
    @ResponseBody
    public Object edit(@Param("data")String data){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try {
            cfCardAbnormityService.editCardAbnormity(data);
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
}
