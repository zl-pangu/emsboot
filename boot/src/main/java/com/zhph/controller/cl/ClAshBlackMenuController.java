package com.zhph.controller.cl;

import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.cl.ClAshBlackMenuModel;
import com.zhph.service.common.BaseService;
import com.zhph.service.cl.ClAshBlackMenuService;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Create by zhaoqixiang
 * Description: 灰黑名单
 * 2018/1/16
 **/
@Controller
@RequestMapping(ConstantCtl.Cl_ASH_BLACK)
public class ClAshBlackMenuController {
    @Autowired
    private BaseService baseService;
    @Autowired
    private ClAshBlackMenuService clAshBlackMenuService;

    @RequestMapping(value = ConstantCtl.INIT, method = RequestMethod.GET)
    public ModelAndView init(HttpServletRequest req) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        baseService.addObject(modelAndView, Constant.CODE_LIST_TPL, Constant.ASH_BLACK_YPE);
        modelAndView.setViewName(ConstantCtl.Cl_ASH_BLACK_PAGE);
        return modelAndView;
    }

    @RequestMapping(value = ConstantCtl.LIST, method = RequestMethod.POST)
    @ResponseBody
    public Grid<ClAshBlackMenuModel> list(PageBean pageBean, ClAshBlackMenuModel params) {
        return clAshBlackMenuService.queryPageInfo(pageBean, params);
    }

    @RequestMapping(value = ConstantCtl.IMPORTEXL, method = RequestMethod.POST)
    @ResponseBody
    public Json ImportExl(MultipartHttpServletRequest request) {
        Json json = null;
        try {
            json = clAshBlackMenuService.importExl(request);
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
        }
        return json;
    }

}
