package com.zhph.controller.cf;

import com.zhph.commons.ConstantCtl;
import com.zhph.model.cf.CfAttendance;
import com.zhph.service.cf.CfAttendanceService;
import com.zhph.service.cf.CfEmpStatusService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Create By lishuangjiang
 */
@Controller
@RequestMapping(ConstantCtl.HQCLCF_ATTENDANCE)
public class CfAttendanceController {

    @Autowired
    CfAttendanceService cfAttendanceService;

    @Autowired
    CfEmpStatusService cfEmpStatusService;

    /**
     * 数据信息查询
     * @return
     */
    @RequestMapping(ConstantCtl.LIST)
    @ResponseBody
    public Grid<Map<String,String>> list(PageBean pageBean, CfAttendance cfAttendance,@Param("gzym") String gzym){
        Grid<Map<String, String>> grid=new Grid<>();
        try {
            grid = cfAttendanceService.queryPageInfo(pageBean, cfAttendance,gzym);
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
        cfAttendanceService.queryAllOpenTypes(model);//部分初始化展示
        cfEmpStatusService.queryDeptArea(model);//初始化大区，分中心，营业部等
        model.setViewName(ConstantCtl.ATTENDANCE_INIT_ADDRESS);//指定跳转页面
        return model;
    }



    /**
     * 导出考勤统计信息
     * @param cfAttendance
     * @param req
     * @param res
     * @throws Exception
     */
    @RequestMapping(value =ConstantCtl.EXPORTEXL,method = RequestMethod.GET)
    public void exportExl(CfAttendance cfAttendance, HttpServletRequest req, HttpServletResponse res) throws Exception{
        cfAttendanceService.exportExl(cfAttendance,req,res);
    }

    /**
     * 用户点击生成考勤统计表
     * @return
     * @throws Exception
     */
    @RequestMapping("/saveAttendance")
    @ResponseBody
    public Object saveAttendance(@Param("gzym") String gzym) throws Exception{
        return cfAttendanceService.saveAttendance(gzym);
    }

    /**
     * 锁定当前工资年月
     * @return
     * @throws Exception
     */
    @RequestMapping("/lock")
    @ResponseBody
    public Object lock(CfAttendance attendance) throws Exception{
        return cfAttendanceService.lock(attendance);
    }

    /**
     * 解锁当前工资年月
     * @return
     * @throws Exception
     */
    @RequestMapping("/unlock")
    @ResponseBody
    public Object unlock(CfAttendance attendance) throws Exception{
        return cfAttendanceService.unlock(attendance);
    }

    /**
     * 通过职务主键标识职级等级
     * @param posCode 职级编码
     * @return
     * @throws Exception
     */
    @RequestMapping(ConstantCtl.HQCLC_PERSON_TRANSFER_BUSINESS)
    @ResponseBody
    public Object queryBusinessByPosCode(@Param("posCode") String posCode) throws Exception{
        return cfAttendanceService.queryAllOpenBusiness(posCode);
    }

    @RequestMapping("/queryByq")
    @ResponseBody
    public Object queryByq(@Param("data") String data,@Param("q") String q) throws Exception{
        return cfAttendanceService.queryAllPersonTransfer(data,q);
    }



}
