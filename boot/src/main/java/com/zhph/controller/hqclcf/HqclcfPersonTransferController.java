package com.zhph.controller.hqclcf;

import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import com.zhph.model.vo.ResultVo;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfPersonTransferService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(ConstantCtl.HQCLC_PERSON_TRANSFER)
public class HqclcfPersonTransferController {
    @Resource
    private HqclcfPersonTransferService hqclcfPersonTransferService;


    @Autowired
    private BaseService baseService;

    public static final Logger logger = LogManager.getLogger(HqclcfPersonTransferController.class);

    /**
     * 数据信息查询
     *
     * @return
     */
    @RequestMapping(ConstantCtl.LIST)
    @ResponseBody
    public Grid<HqclcfPersonTransfer> list(PageBean pageBean, HqclcfPersonTransfer hqclcfPersonTransfer) throws Exception {
        Grid<HqclcfPersonTransfer> grid = hqclcfPersonTransferService.getPersonTransferByCondition(pageBean, hqclcfPersonTransfer);
        return grid;
    }

    /**
     * 页面初始化
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.INIT)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView model = new ModelAndView();
        hqclcfPersonTransferService.queryAllOpenTypes(model,new Long(5));//部分初始化展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);//业务条线初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.TRANSFER_TYPE);//异动类型初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.TRANSFER_TYPE);//异动类型初始化表格展示
        model.setViewName(ConstantCtl.HQCLC_PERSON_TRANSFER_INITVIEW_NAME);//指定跳转页面
        return model;
    }

    /**
     * 对选中的人员进行删除
     *
     * @param hqclcfPersonTransfer
     * @return
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.DEL)
    @ResponseBody
    public ResultVo deleteByPrimaryKey(HqclcfPersonTransfer hqclcfPersonTransfer) throws Exception {
        return hqclcfPersonTransferService.deleteByPrimaryKey(hqclcfPersonTransfer);
    }

    /**
     * 对选中的人员的信息进行初始化
     *
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("editInit")
    public ModelAndView editInit(HttpServletRequest request, Long id) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);//业务条线初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.TRANSFER_TYPE);//异动类型初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.TRANSFER_TYPE);//异动类型初始化表格展示
        hqclcfPersonTransferService.queryAllOpenTypes(model,id);//部分初始化展示
        hqclcfPersonTransferService.setEditPersonTransfer(model, id);
        model.setViewName(ConstantCtl.HQCLC_PERSON_TRANSFER_EDIT_ADDRESS);//指定编辑页面
        return model;
    }

    /**
     * 新增初始化(权限需要)
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("addInit")
    public ModelAndView addInit(HttpServletRequest request) throws Exception {
        ModelAndView model = new ModelAndView();
        baseService.addObject(model, Constant.CODE_LIST, Constant.BUSINESS_LINE);//业务条线初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.BUSINESS_LINE);//业务条线初始化表格展示
        baseService.addObject(model, Constant.CODE_LIST, Constant.TRANSFER_TYPE);//异动类型初始化下拉展示
        baseService.addObject(model, Constant.CODE_LIST_TPL, Constant.TRANSFER_TYPE);//异动类型初始化表格展示
        hqclcfPersonTransferService.queryAllOpenTypes(model,new Long(5));//部分初始化展示
        model.setViewName(ConstantCtl.HQCLC_PERSON_TRANSFER_ADD_ADDRESS);//指定新增页面
        return model;
    }

    /**
     * 插入人员异动记录
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.ADD)
    @ResponseBody
    public int addPersonTransfer(HqclcfPersonTransfer hqclcfPersonTransfer, MultipartHttpServletRequest request, HttpServletRequest req) throws Exception {
        return hqclcfPersonTransferService.insertSelective(hqclcfPersonTransfer, request, req);
    }

    /**
     * 修改选中的异动的信息
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.EDIT)
    @ResponseBody
    public int updateByPrimaryKeySelective(HqclcfPersonTransfer hqclcfPersonTransfer, MultipartHttpServletRequest request, HttpServletRequest req) throws Exception {
        return hqclcfPersonTransferService.updateByPrimaryKeySelective(hqclcfPersonTransfer, request, req);
    }

    /**
     * 导出人员异动信息
     *
     * @param hqclcfPersonTransfer
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping(value = ConstantCtl.EXPORTEXL, method = RequestMethod.GET)
    public void exportExl(HqclcfPersonTransfer hqclcfPersonTransfer, HttpServletRequest req, HttpServletResponse res) throws Exception {
        hqclcfPersonTransferService.exportExl(hqclcfPersonTransfer, req, res);
    }

    /**
     * 通过部门主键标识获取部门编码和部门下所有启动岗位
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.HQCLC_PERSON_TRANSFER_DEPT_POST_GET)
    @ResponseBody
    public Object queryDeptAndPostListByDeptNo(@Param("id") Long id) throws Exception {
        return hqclcfPersonTransferService.queryDeptAndPostListByDeptNo(id);
    }

    /**
     * 通过职务主键标识职级等级
     *
     * @param posCode 职级编码
     * @return
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.HQCLC_PERSON_TRANSFER_BUSINESS)
    @ResponseBody
    public Object queryBusinessByPosCode(@Param("posCode") String posCode) throws Exception {
        return hqclcfPersonTransferService.queryAllOpenBusiness(posCode);
    }

    @RequestMapping("/queryByq")
    @ResponseBody
    public JSONObject queryByq(@Param("data") String data, @Param("q") String q, PageBean pageBean,@Param("rows") int rows) throws Exception {
        return hqclcfPersonTransferService.queryAllPersonTransfer(data, q,pageBean,rows);
    }

    /**
     * 下载文件
     *
     * @param file
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping("downloadHdsfFile")
    public void downloadHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception {
        hqclcfPersonTransferService.downloadHdsfFile(file, req, res);
    }

    /**
     * 预览文件
     *
     * @param file
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping("previewHdsfFile")
    public void previewHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception {
        hqclcfPersonTransferService.previewHdsfFile(file, req, res);
    }

    @RequestMapping("queryEmpOrganizat")
    @ResponseBody
    public Object queryEmpOrganizat(@Param("deptNo") String deptNo, @Param("postNo") String postNo) throws Exception {
        return hqclcfPersonTransferService.queryEmpOrganizat(deptNo, postNo);
    }

}
