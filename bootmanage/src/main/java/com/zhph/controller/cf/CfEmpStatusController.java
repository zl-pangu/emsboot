package com.zhph.controller.cf;

import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.ConstantCtl;
import com.zhph.model.cf.CfEmpStatus;
import com.zhph.service.cf.CfAttendanceService;
import com.zhph.service.cf.CfEmpStatusService;
import com.zhph.service.common.BaseService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Create By lishuangjiang
 * 消分员工状态变更
 */
@Controller
@RequestMapping(ConstantCtl.CF_SATATUS)
public class CfEmpStatusController {

    @Autowired
    private CfEmpStatusService cfEmpStatusService;

    @Autowired
    private CfAttendanceService cfAttendanceService;

    @Autowired
    private BaseService baseService;
    /**
     * 数据信息查询
     * @return
     */
    @RequestMapping(ConstantCtl.LIST)
    @ResponseBody
    public Grid<Map<String,String>> list(PageBean pageBean, CfEmpStatus cfEmpStatus){
        Grid<Map<String, String>> grid=new Grid<>();
        try {
            grid = cfEmpStatusService.queryPageInfo(pageBean, cfEmpStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  grid;
    }
    /**
     * 页面初始化
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.INIT)
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception{
        ModelAndView model = new ModelAndView();
        model.setViewName(ConstantCtl.CF_INDEX_PAGE);//指定员工状态默认页面
        cfEmpStatusService.queryDeptArea(model);//大区、分中心、营业部初始化
        cfEmpStatusService.queryEmpStatusList(model);//员工状态是否变更下拉初始化
        return model;
    }

    /**
     * 编辑初始化(权限需要)
     * @param request
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("editInit")
    public ModelAndView editInit(HttpServletRequest request, Long id) throws Exception {
        ModelAndView model = new ModelAndView();
        cfEmpStatusService.queryEmpStatusList(model);//员工状态是否变更下拉初始化
        cfEmpStatusService.setEditEmpSatatus(model, id);
        model.setViewName(ConstantCtl.CF_INDEX_PAGE_EDIT);//指定编辑页面
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
        cfEmpStatusService.queryEmpStatusList(model);//员工状态是否变更下拉初始化
        model.setViewName(ConstantCtl.CF_INDEX_PAGE_ADD);//指定新增页面
        return model;
    }

    /**
     * 新增员工状态变更记录
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.ADD)
    @ResponseBody
    public Object add(@Param("data") String data) throws Exception{
        return cfEmpStatusService.insert(data);
    }

    /**
     * 修改员工状态变更记录
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.EDIT)
    @ResponseBody
    public Object edit(@Param("data") String data) throws Exception{
        return cfEmpStatusService.updateEmpStatuses(data);
    }

    /**
     * 导出员工状态变更
     * @param cfEmpStatus
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping(value =ConstantCtl.EXPORTEXL,method = RequestMethod.GET)
    public void exportExl(CfEmpStatus cfEmpStatus, HttpServletRequest req, HttpServletResponse res) throws Exception{
        cfEmpStatusService.exportExl(cfEmpStatus,req,res);
    }


    @RequestMapping(value =ConstantCtl.IMPORTEXL,method = RequestMethod.POST)
    @ResponseBody
    public Object importExl(MultipartFile file, HttpServletRequest req, HttpServletResponse res) throws Exception{
            return cfEmpStatusService.importExl(file,req,res);
    }

    /**
     * 解除当前记录
     * @return
     * @throws Exception
     */
    @RequestMapping("/relieve")
    @ResponseBody
    public Object unlock(@Param("priNumber") Long priNumber,@Param("endDate") String endDate) throws Exception{
        return cfEmpStatusService.relieve(priNumber,endDate);
    }


    /**
     * 查询员工
     * @return
     * @throws Exception
     */
    @RequestMapping("/queryByq")
    @ResponseBody
    public JSONObject queryByq(@Param("q") String q, PageBean pageBean,@Param("rows") int rows) throws Exception{
        return cfEmpStatusService.queryByq(q,pageBean,rows);
    }

    /**
     * @return
     * @throws Exception
     */
    @RequestMapping("/build_end_date")
    public String buildEndDate() throws Exception{
        return ConstantCtl.CF_BUILD_END_TIME;
    }


}
