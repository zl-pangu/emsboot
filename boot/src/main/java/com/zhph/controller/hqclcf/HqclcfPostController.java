package com.zhph.controller.hqclcf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfPostService;
import com.zhph.util.Json;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(ConstantCtl.HQCLC_POST)
public class HqclcfPostController {


    @Autowired
    private HqclcfPostService hqclcfPostService;
    @Autowired
    private BaseService baseService;

    public static final Logger logger = LogManager.getLogger(HqclcfPostController.class);

    @RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
    public String init(HttpServletRequest req) throws Exception {
        req.setAttribute("businesses", hqclcfPostService.queryAllBusiness());//职务信息初始化
        req.setAttribute("deptList", hqclcfPostService.queryAllDepts());//部门信息初始化
        return ConstantCtl.HQCLCFPOST_ZTREE_NAME;
    }


    @RequestMapping(value = ConstantCtl.TREE, method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getTree(HttpServletRequest req, HttpServletResponse res, @Param("id") Long id) throws Exception {
        return hqclcfPostService.buildPostTree(id);
    }

    @RequestMapping(value = "/dept/tree", method = RequestMethod.POST)
    @ResponseBody
    public JSONArray getDeptTree(HttpServletRequest req, HttpServletResponse res, @Param("id") Long id) throws Exception {
        return hqclcfPostService.buildDeptTree();
    }

    @RequestMapping(value = "/dept/businessLine", method = RequestMethod.POST)
    @ResponseBody
    public HqclcfDept getDeptBusinessLine(HttpServletRequest req, HttpServletResponse res, @RequestParam("id") Long id) throws Exception {
        return hqclcfPostService.getBusinessLine(id);
    }

    /**
     * @param req
     * @param id  主键标识 父节点
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "addInit", method = RequestMethod.GET)
    public ModelAndView addInit(HttpServletRequest req, @Param("id") Long id) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务线初始化
        req.setAttribute("superPost", hqclcfPostService.queryPost(id));//上级岗位初始化
        req.setAttribute("postNo", hqclcfPostService.getOrderIdByUUId());//岗位编码生成规则
        req.setAttribute("businesses", hqclcfPostService.queryAllBusiness());//职务信息初始化
        model.setViewName(ConstantCtl.HQCLCFPOST_ADD_ADDRESS);
        return model;
    }

    @RequestMapping(value = ConstantCtl.ADD, method = RequestMethod.POST)
    @ResponseBody
    public Json add(@Param("data") String data) throws Exception {
        return hqclcfPostService.addPost(data);
    }


    @RequestMapping(value = "getDeptType", method = RequestMethod.POST)
    @ResponseBody
    public Object getDeptType(@Param("businessLine") Integer businessLine) throws Exception {
        return hqclcfPostService.buildPostTypeByBl(businessLine);
    }

    @RequestMapping(value = "editInit", method = RequestMethod.GET)
    public ModelAndView editInit(HttpServletRequest req, @Param("id") Long id) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务线初始化
        req.setAttribute("superPost", hqclcfPostService.queryPost(hqclcfPostService.queryPost(id).getPostPid()));//上级岗位初始化
        req.setAttribute("post", hqclcfPostService.queryPost(id));//岗位编码生成规则
        req.setAttribute("businesses", hqclcfPostService.queryAllBusiness());//职务信息初始化
        model.setViewName(ConstantCtl.HQCLCFPOST_EDIT_ADDRESS);
        return model;
    }


    @RequestMapping(value = ConstantCtl.EDIT, method = RequestMethod.POST)
    @ResponseBody
    public Object edit(@Param("data") String data) throws Exception {
        return hqclcfPostService.editPost(data);
    }

    @RequestMapping(value = ConstantCtl.DEL, method = RequestMethod.POST)
    @ResponseBody
    public Json del(@Param("id") Long id) throws Exception {
        return hqclcfPostService.del(id);
    }

    @RequestMapping(value = ConstantCtl.DETAIL, method = RequestMethod.POST)
    @ResponseBody
    public Object detail(@Param("id") Long id) throws Exception {
        return hqclcfPostService.detail(id);
    }

    @RequestMapping(value = ConstantCtl.HQCLCFPOST_STATUS_SWITCH, method = RequestMethod.POST)
    @ResponseBody
    public Object judgeStatus(@Param("id") String code, @Param("value") int value) throws Exception {
        return hqclcfPostService.judgeStatus(code, value);
    }

    @RequestMapping("/checkprePost")
    @ResponseBody
    public Object checkprePost(@Param("id") String id) {
        JSONObject obj = new JSONObject();
        try {
            obj = hqclcfPostService.checkprePost(id);
        } catch (Exception e) {
            obj.put("success", false);
            obj.put("msg", e.getMessage());
        }
        return obj;
    }
}
