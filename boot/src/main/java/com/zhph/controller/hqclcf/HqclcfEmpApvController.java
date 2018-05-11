package com.zhph.controller.hqclcf;

import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.Constant;
import com.zhph.mapper.sys.SysZhphBankMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.sys.SysUser;
import com.zhph.model.sys.SysZhphBank;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfEmpApvService;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/hqclcf/hqclcfempapv")
public class HqclcfEmpApvController {

    @Resource
    private HqclcfEmpApvService empApvService;

    @Resource
    private BaseService baseService;

    @Autowired
    private SysZhphBankMapper bankMapper;


    /**
     * 页面初始化
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("/init")
    public ModelAndView init(HttpServletRequest req) throws Exception {
        ModelAndView model=new ModelAndView();
        model.addObject("blselectList", baseService.buildBlByUserAndShowEnable());
        baseService.addObject(model, Constant.CODE_LIST,Constant.STATUS_APP);
        empApvService.buildListTpl(req,model);
        model.setViewName("/pages/hqclcf/empapv/hqclcf_empapv_index");
        return model;
    }

    /**
     * 页面数据
     * @param pageBean
     * @param emp
     * @return
     * @throws Exception
     */
    @RequestMapping("list")
    @ResponseBody
    public  Object list(PageBean pageBean, HqclcfEmp emp) throws Exception{
        return empApvService.queryPageInfo(pageBean,emp);
    }

    /**
     * 新增初始化
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("addInit")
    public ModelAndView addInit(HttpServletRequest req) throws  Exception{
        ModelAndView model=new ModelAndView();
        buildAddInitReq(model,req);
        model.setViewName("/pages/hqclcf/empapv/hqclcf_empapv_add");
        return model;
    }

    /**
     * 构建作用域的值
     * @param model
     * @param req
     * @throws Exception
     */
    private void buildAddInitReq(ModelAndView model,HttpServletRequest req) throws Exception{
        SysZhphBank bank=new SysZhphBank();
        bank.setStatus("1");
        model.addObject("bankList",bankMapper.queryAllZhphBank(bank));
        String[] str = new String[]{Constant.SEX, Constant.MARRIAGE,
                Constant.NATIVE_TYPE, Constant.ID_TYPE, Constant.NATION,
                Constant.URGENCY_RELATION, Constant.EDU,
                Constant.EDU_TYPE, Constant.EMP_SUBJECT,
                Constant.BUSINESS_LINE,Constant.EMP_TYPE
        };
        for (int i = 0; i < str.length; i++) {
            baseService.addObject(model,Constant.CODE_LIST,str[i]);
        }
    }

    /**
     * 修改初始化页面
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("editInit")
    public ModelAndView editInit(HttpServletRequest req,Long id) throws Exception{
        ModelAndView model=new ModelAndView();
        empApvService.buildEditFormReult(id,req);
        buildAddInitReq(model,req);
        model.setViewName("/pages/hqclcf/empapv/hqclcf_empapv_edit");
        return model;
    }

    /**
     * 审批页面初始化
     * @param req
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("apvEmpInit")
    public ModelAndView apvEmpInit(HttpServletRequest req,Long id) throws Exception{
        ModelAndView model=new ModelAndView();
        empApvService.buildEditFormReult(id,req);
        buildAddInitReq(model,req);
        model.setViewName("/pages/hqclcf/empapv/hqclcf_empapv_apvEmp");
        return model;
    }

    /**
     * 员工详情
     * @param req
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("detail")
    public ModelAndView detail(HttpServletRequest req,Long id) throws Exception{
        ModelAndView model=new ModelAndView();
        empApvService.buildEditFormReult(id,req);
        buildAddInitReq(model,req);
        model.setViewName("/pages/hqclcf/empapv/hqclcf_empapv_detail");
        return model;
    }

    /**
     * 撤销
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("del")
    @ResponseBody
    public Object del(@Param("id")Long id) throws Exception{
        return empApvService.delEmp(id);
    }

    /**
     * 员工属性验证验证
     * @throws Exception
     */
    @RequestMapping("empCheck")
    @ResponseBody
    public Object empCheck(String data) throws Exception{
        return empApvService.empCheck(data);
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

    /***
     * 员工新增
     * @param emp
     * @param request
     * @param req
     * @return
     * @throws Exception
     */
    @RequestMapping("add")
    @ResponseBody
    public Object add(HqclcfEmp emp,MultipartHttpServletRequest request,HttpServletRequest req) throws Exception{
        JSONObject obj=new JSONObject();
        obj.put("success",true);
        try {
            empApvService.addEmp(emp, request);
        } catch (Exception e) {
            obj.put("success",false);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 修改员工
     * @param emp
     * @param multipartHttpServletRequest
     * @return
     * @throws Exception
     */
    @RequestMapping("edit")
    @ResponseBody
    public Object edit(HqclcfEmp emp,MultipartHttpServletRequest multipartHttpServletRequest){
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        try {
            empApvService.editEmp(emp, multipartHttpServletRequest);
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 审批员工
     * @param id
     * @throws Exception
     */
    @RequestMapping("apvEmp")
    @ResponseBody
    public Object apvEmp(Long id, HqclcfEmp emp,HttpServletRequest req) throws Exception {
        SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");
        return empApvService.apvEmp(id, emp,onlineUser);
    }

    /**
     * 照片展示
     * @param file
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping("showImg")
    public void showImg(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res){
        try {
            empApvService.showEmpPoto(file, req, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 预览文件
     * @param file
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping("previewHdsfFile")
    public void previewHdsfFile(HqclcfEmpFile file,HttpServletRequest req, HttpServletResponse res) throws Exception{
        empApvService.previewHdsfFile(file, req, res);
    }

    /**
     * 下载文件
     * @param file
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping("downloadHdsfFile")
    public void downloadHdsfFile(HqclcfEmpFile file,HttpServletRequest req, HttpServletResponse res) throws Exception{
        empApvService.downloadHdsfFile(file,req,res);
    }

    @RequestMapping("checkIspreviewHdsfFile")
    @ResponseBody
    public Object checkIspreviewHdsfFile(HqclcfEmpFile file) throws Exception{
        return empApvService.checkIspreviewHdsfFile(file);
    }

}

