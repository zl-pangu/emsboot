package com.zhph.controller.hqclcf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhph.annotation.SameUrlData;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.util.Json;
import com.zhph.util.RandomUtil;
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

/**
 * Created by zhouliang on 2017/11/28.
 */
@Controller
@RequestMapping(ConstantCtl.HQCLCFDEPT_CTLREQM)
public class HqclcfDeptController {

    @Autowired
    private HqclcfDeptService hqclcfDeptService;
    @Autowired
    private BaseService baseService;

    public static final Logger logger = LogManager.getLogger(HqclcfDeptController.class);

    @RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
    public String init() throws Exception {
        return ConstantCtl.HQCLCFDEPT_INITVIEW_NAME;
    }

    /**
     * 注意：data，userID都允许为空；不传就查询全部的树；
     *
     * @param data   对应 HqclcfDept：前台传参例子： var obj=new Object();
     *               obj.部门字段=你需要的查询的值;
     *               data=JSON.stringify(obj);
     * @param userId 就是用户的ID：这个参数树用来构建某个用户勾选的数据权限树
     * @return
     * @throws Exception
     */
    @RequestMapping(value = ConstantCtl.TREE, method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getTree(@Param("data") String data, @Param("userId") Long userId) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("code", "200");
        try {
            obj.put("data", hqclcfDeptService.buildDeptTree(data, userId));
        } catch (Exception e) {
            obj.put("code", "500");
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    @RequestMapping("buildTreeByUserbL")
    @ResponseBody
    public Object buildTreeByUserbL() throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("code", "200");
        try {
            obj.put("data", hqclcfDeptService.buildTreeByUserbL(baseService.getOnlineUserBl()));
        } catch (Exception e) {
            obj.put("code", "500");
            obj.put("msg", e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }


    @RequestMapping(value = "addInit", method = RequestMethod.GET)
    public ModelAndView addInit(HttpServletRequest req, @Param("id") Long id) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);
        req.setAttribute("superDept", hqclcfDeptService.queryDept(id));
        req.setAttribute("deptCode", RandomUtil.getOrderIdByUUId());
        model.setViewName("/pages/hqclcf/hqclcfdept/hqclcfdeptAdd");
        return model;
    }

    @SameUrlData
    @RequestMapping(value = ConstantCtl.ADD, method = RequestMethod.POST)
    @ResponseBody
    public Object add(@Param("data") String data) throws Exception {
        Json json=new Json();
        try {
            HqclcfDept dept = hqclcfDeptService.addDept(data);
            json.setObj(dept);
            json.setSuccess(true);
        } catch (Exception e) {
            json.setMsg(e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping(value = "getDeptType", method = RequestMethod.POST)
    @ResponseBody
    public Object getDeptType(@Param("businessLine") Integer businessLine) throws Exception {
        return hqclcfDeptService.buildDeptTypeByBl(businessLine);
    }

    @RequestMapping(value = "editInit", method = RequestMethod.GET)
    public ModelAndView editInit(HttpServletRequest req, @Param("id") Long id) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);
        req.setAttribute("dept", hqclcfDeptService.queryDept(id));
        req.setAttribute("superDept", hqclcfDeptService.querySuperDept(id));
        req.setAttribute("deptTypes", hqclcfDeptService.buildDeptTypeById(id));
        model.setViewName("/pages/hqclcf/hqclcfdept/hqclcfdeptEdit");
        return model;
    }

    @SameUrlData
    @RequestMapping(value = ConstantCtl.EDIT, method = RequestMethod.POST)
    @ResponseBody
    public Object edit(@Param("data") String data) throws Exception {
        return hqclcfDeptService.editDept(data);
    }

    @RequestMapping(value = ConstantCtl.DEL, method = RequestMethod.POST)
    @ResponseBody
    public Json del(@Param("id") Long id) throws Exception {
        return hqclcfDeptService.del(id);
    }

    @RequestMapping(value = ConstantCtl.DETAIL, method = RequestMethod.POST)
    @ResponseBody
    public Object detail(@Param("id") Long id) throws Exception {
        return hqclcfDeptService.detail(id);
    }

    @RequestMapping(value = "checkDeptPrepare", method = RequestMethod.POST)
    @ResponseBody
    public Object checkDeptPrepare(@Param("id") Long id, @Param("type") String type) throws Exception {
        return hqclcfDeptService.checkDeptPrepare(id, type);
    }


    @RequestMapping("checkDeptEnable")
    @ResponseBody
    public Object checkDeptEnable(@Param("id") Long id, @Param("sl") String sl) throws Exception {
        return hqclcfDeptService.checkDeptEnable(id, sl);
    }

    @RequestMapping("/queryDeptTree")
    @ResponseBody
    public JSONObject queryDeptTree(@Param("isShowChild") String isShowChild) {
        JSONObject obj = new JSONObject();
        try {
            JSONArray jsonArray = hqclcfDeptService.queryDeptTreeByisShowChild(isShowChild);
            obj.put("code", 200);
            obj.put("data", jsonArray);
        } catch (Exception e) {
            obj.put("code", 500);
            obj.put("msg", e.getMessage());
        }
        return obj;
    }


}
