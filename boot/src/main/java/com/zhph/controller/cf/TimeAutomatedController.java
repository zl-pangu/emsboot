package com.zhph.controller.cf;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.model.cf.CfOfflaterSet;
import com.zhph.model.cf.CfPaidLeave;
import com.zhph.model.cf.CfTimeLock;
import com.zhph.model.cf.TimeAutomated;
import com.zhph.model.cf.TimeAutomatedBak;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.sys.SysUser;
import com.zhph.service.cf.CfOfflaterSetService;
import com.zhph.service.cf.TimeAutomatedService;
import com.zhph.service.common.BaseService;
import com.zhph.util.DateTimeUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import com.zhph.util.StringUtil;

/**
 * 考勤排班
 */
@Controller
@RequestMapping("/cf/timeAutomated")
public class TimeAutomatedController {
    private Log log = LogFactory.getLog(TimeAutomatedController.class);
    @Resource
    private TimeAutomatedService timeAutomatedService;
    @Resource
    private CfOfflaterSetService cfOfflaterSetService;
    @Resource
    private BaseService baseService;

    @RequestMapping("getMonthList")
    @ResponseBody
    public JSONArray getMonthList(String gzym){
    	if(StringUtil.isEmpty(gzym)){
    		gzym = DateUtil.getCurrentDate("yyyy-MM");
    	}
        Integer yyyy = Integer.valueOf(gzym.split("-")[0]);
        Integer mm = Integer.valueOf(gzym.split("-")[1]);
        return (JSONArray) JSONArray.toJSON(DateTimeUtil.getCurrentMonthAllWeekName(yyyy,mm));
    }

    @RequestMapping("index")
    public ModelAndView index(HttpServletRequest request) throws Exception {
        ModelAndView model = new ModelAndView("pages/cf/timeautomated/timeautomated_list");
        /*HashMap<String, HashMap<String, Boolean>> primary_user_key = (HashMap<String, HashMap<String, Boolean>>) request.getSession().getAttribute(Constants.PRIMARY_KEY_USER);
        HashMap<String, Boolean> role = primary_user_key.get("XjTimeAutomatedController");
        boolean isSelect = false;
        if (role.containsKey("update")){
            isSelect = true;
        }
        model.addObject("isSelect",isSelect);*/
        model.addObject("isSelect",true);
        String gzym = DateUtil.getCurrentDate("yyyy-MM");
        Integer yyyy = Integer.valueOf(gzym.split("-")[0]);
        Integer mm = Integer.valueOf(gzym.split("-")[1]);
        model.addObject("monthList", baseService.getSysDicJsonStr(DateTimeUtil.getCurrentMonthAllWeekName(yyyy,mm)));
        SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
        List<HqclcfDept> listDepts = onlineUser.getCfDepts();
        
        List<HqclcfDept> dqList = new ArrayList<HqclcfDept>();//大区
        List<HqclcfDept> orgList = new ArrayList<HqclcfDept>();//分公司
        List<HqclcfDept> businessDeptList = new ArrayList<HqclcfDept>();//营业部
        if(listDepts != null && listDepts.size() > 0	){
        	for (HqclcfDept hqclcfDept : listDepts) {
				if(Constant.DEPT_TYPE_LEVEL1.equals(hqclcfDept.getDeptType())){
					dqList.add(hqclcfDept);
				}else if(Constant.DEPT_TYPE_LEVEL2.equals(hqclcfDept.getDeptType())){
					orgList.add(hqclcfDept);
				}else if(Constant.DEPT_TYPE_LEVEL3.equals(hqclcfDept.getDeptType())){
					businessDeptList.add(hqclcfDept);
				}
			}
        	
        }
        model.addObject("dqList", dqList);
        model.addObject("orgList", orgList);
        model.addObject("businessDeptList", businessDeptList);
        model.addObject("gzym", gzym);
        return model;
    }

    @RequestMapping("dataGrid")
    @ResponseBody
    public Grid<TimeAutomated> dataGrid(PageBean pageBean, TimeAutomatedBak timeAutomatedBak,HttpServletRequest request){
        Grid<TimeAutomated> grid = new Grid<>();
        try {
            SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
			List<HqclcfDept> cfDepts = onlineUser.getCfDepts();// 有权限查看的部门
			if (cfDepts != null && cfDepts.size() > 0) {
				List<String> deptNos = new ArrayList<String>();// 数据权限基本范围
				for (HqclcfDept dept : cfDepts) {
					deptNos.add(dept.getDeptCode());
				}
				timeAutomatedBak.setDeptNos(deptNos);
			}
            grid = timeAutomatedService.dataGrid(timeAutomatedBak,pageBean);
        }catch (Exception e){
            log.error("查询考勤排班出错",e);
        }
        return grid;
    }

    /*@RequestMapping("updatePage")*/
    public ModelAndView updatePage(String ids,String gzym,HttpServletRequest request) throws Exception {
        ModelAndView modelAndView = new ModelAndView("pages/cf/timeautomated/timeautomated_update");
        TimeAutomatedBak automatedBak = new TimeAutomatedBak();
        automatedBak.setIds(ids.replace("[","").replace("]","").split(","));
        List<?> list = timeAutomatedService.getList(null,automatedBak);
        modelAndView.addObject("datalist",JSONObject.toJSON(list));
        modelAndView.addObject("monthList",this.getMonthList(gzym));
        modelAndView.addObject("sysDay", DateUtil.getCurrentDate("yyyy-MM-dd").split("-")[2]);
        boolean ifUpdateNextMonth = false;//是否修改下个月
        if (DateTimeUtil.compareDate(gzym,DateTimeUtil.getDateAddMonth(DateUtil.getCurrentDate("yyyy-MM"),1),"yyyy-MM")){//所选年月为当前月份的下一个月
        	ifUpdateNextMonth = true;
        }
        SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
        boolean isAdmin = onlineUser.getIsSuperAdmin() == 1;//是否是管理员
        modelAndView.addObject("isAdmin",isAdmin);
        modelAndView.addObject("ifUpdateNextMonth",ifUpdateNextMonth);
        return modelAndView;
    }

    @RequestMapping("update")
    @ResponseBody
    public Json update(@RequestBody List<TimeAutomated> timeAutomateds,HttpServletRequest request,PageBean pageBean){
        Json json = new Json();
        SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
        try {
            if (timeAutomateds!=null&&timeAutomateds.size()!=0){
				for (TimeAutomated timeAutomated : timeAutomateds) {
					System.out.println(timeAutomated.toString());
                    CfPaidLeave leave = new CfPaidLeave();
                    leave.setGzym(timeAutomated.getGzym());
                    
                    String gzym = timeAutomated.getGzym();
                    CfOfflaterSet cSet = new CfOfflaterSet();
                    cSet.setCosMonth_s(gzym);
                    
                    Grid<CfOfflaterSet> grid = cfOfflaterSetService.queryPageInfo(null, cSet);
                    if(grid.getData() != null && grid.getData().size() > 0){
                    	CfOfflaterSet paidLeave = grid.getData().get(0);
                    	
                        //检查周末调休天数
                        String weekendPaidLeave = paidLeave.getCosWeekendDays();
                        if (weekendPaidLeave!=null&&!"".equals(weekendPaidLeave)&&!"0".equals(weekendPaidLeave)){
                            if (timeAutomated.getWeekendRestCot()-Integer.parseInt(weekendPaidLeave)>0){
                            	json.setMsg(timeAutomated.getEmpName()+"当月周末调休数["+timeAutomated.getWorkDayRestCot()+"]大于系统设置当月周末调休数["+weekendPaidLeave+"]，请联系相关人员！");
                            	json.setSuccess(false);
                            	return json;
                            }
                        }else {
                            if (timeAutomated.getWeekendRestCot()>0){
                            	json.setMsg(timeAutomated.getEmpName()+"当月周末不允许调休，请联系相关人员！");
                            	json.setSuccess(false);
                            	return json;
                            }
                        }
                        //检查工作日调休天数
                        String paidLeaveDays = paidLeave.getCosWorkdayDays();
                        if (paidLeaveDays!=null&&!"".equals(paidLeaveDays)&&!"0".equals(paidLeaveDays)){
                            if (timeAutomated.getWorkDayRestCot()-Integer.parseInt(paidLeaveDays)>0){
                            	json.setMsg(timeAutomated.getEmpName()+"当月工作日调休数["+timeAutomated.getWorkDayRestCot()+"]大于系统设置当月工作日调休数["+paidLeaveDays+"]，请联系相关人员！");
                            	json.setSuccess(false);
                            	return json;
                            }
                        }else {
                            if (timeAutomated.getWorkDayRestCot()>0){
                            	json.setMsg(timeAutomated.getEmpName()+"当月工作日不允许调休，请联系相关人员！");
                            	json.setSuccess(false);
                            	return json;
                            }
                        }
                    }else{
                        if (timeAutomated.getWeekendRestCot()>0||timeAutomated.getWorkDayRestCot()>0){
                        	json.setMsg(timeAutomated.getEmpName()+"当月不允许调休，请联系相关人员！");
                        	json.setSuccess(false);
                        	return json;
                        }
                    }
                }
            }
            timeAutomatedService.updateBatch(timeAutomateds, onlineUser);
            json.setSuccess(true);
            json.setMsg("修改成功！");
        } catch (AppException e){
            log.error("排班修改失败：",e);
            json.setMsg("修改失败---"+e.getMessage());
        } catch (Exception e){
            log.error("排班修改失败：",e);
            json.setMsg("修改失败！");
        }
        return json;
    }

    /**
     * 导出
     * @param response
     * @param exportParam
     */
    @RequestMapping("exportExl")
    public void exportExl(HttpServletResponse response, TimeAutomatedBak timeAutomatedBak,HttpServletRequest request){
        try {
            SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
			List<HqclcfDept> cfDepts = onlineUser.getCfDepts();// 有权限查看的部门
			if (cfDepts != null && cfDepts.size() > 0) {
				List<String> deptNos = new ArrayList<String>();// 数据权限基本范围
				for (HqclcfDept dept : cfDepts) {
					deptNos.add(dept.getDeptCode());
				}
				timeAutomatedBak.setDeptNos(deptNos);
			}
			if(StringUtil.isEmpty(timeAutomatedBak.getGzym())){
				log.error("请选择年月!");
				PrintWriter pw = response.getWriter();
				pw.write("<script>alert('" + new String("请选择年月".getBytes("UTF-8"), "ISO-8859-1") + "!');</script>");
				pw.flush();
				pw.close();
				return;
			}
            timeAutomatedService.exportExl(response,timeAutomatedBak);
        }catch (Exception e){
            log.error("导出失败",e);
        }
    }

    /**
     * 如果有年月被锁定，则msg返回这些年月
     * @param dates
     * @return
     * @throws Exception
     */
    @RequestMapping("checkTimeLock")
    @ResponseBody
    public Json checkTimeLock(String date) throws Exception {
        Json json = new Json();
        if(StringUtil.isEmpty(date)){
        	json.setSuccess(true);
        	return json;
        }
        String[] dates = date.split(",");
        List<String> locked = new ArrayList<String>();
        String[] temp;
        for (String s : dates) {
        	temp = s.split("-");
        	if(temp.length != 2){
        		locked.add(s);
        	}else{
        		Integer yyyy = Integer.valueOf(temp[0]);
        		Integer mm = Integer.valueOf(temp[1]);
        		CfTimeLock lock = new CfTimeLock();
        		lock.setYear(yyyy);
        		lock.setMonth(mm);
        		CfTimeLock cfTimeLock = timeAutomatedService.queryCfTimelock(lock);
        			if (cfTimeLock != null && cfTimeLock.getIsLock()==1){
        				locked.add(s);
        		}
        	}
		}
        if(locked.size() > 0){
        	json.setMsg(StringUtil.join(locked,","));
        	json.setSuccess(false);
        }else{
        	json.setSuccess(true);
        }
        return json;
    }
    
    /**
     * 手动生成排班计划
     * @param response
     * @param exportParam
     */
    @RequestMapping("generate")
    @ResponseBody
    public Json generate(HttpServletResponse response, TimeAutomatedBak timeAutomatedBak,HttpServletRequest request){
    	Json json = new Json();
		try {
			log.info("消金考勤排班开始……");
			SysUser user = new SysUser();
			user.setFullName("admin");
			timeAutomatedService.addAllEmp(user);
			json.setSuccess(true);
			json.setMsg("消金考勤排班成功！");
			log.info("消金考勤排班结束……");
		} catch (Exception e) {
			log.error("消金考勤排班错误……", e);
			e.printStackTrace();
			log.error(e.getMessage());
			json.setMsg("消金考勤排班错误……");
			json.setSuccess(false);
		}
    	return json;
    }
    
}
