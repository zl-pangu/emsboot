package com.zhph.controller.cf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhph.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.commons.ConstantCtl;
import com.zhph.mapper.cf.CfTimeLockMapper;
import com.zhph.model.cf.CfTimeLock;
import com.zhph.model.cf.CfVacateManage;
import com.zhph.model.common.RankType;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfGzym;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.sys.SysHoliday;
import com.zhph.model.sys.SysUser;
import com.zhph.service.cf.CfVacateManageService;
import com.zhph.service.common.BaseService;
import com.zhph.service.hqclcf.HqclcfBusinessService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.service.hqclcf.HqclcfEmpService;
import com.zhph.service.sys.SysHolidayService;
import com.zhph.util.CalculateHoursUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

/**
 * 请休假管理Controller
 * 
 * @author roilat-D
 *
 */
@Controller
@RequestMapping(ConstantCtl.CF_VACATEMANAGE)
public class CfVacateManageController {
	protected Log log = LogFactory.getLog(CfVacateManageController.class);

	@Resource
	private CfVacateManageService vacateManageService;

	@Resource
	private HqclcfDeptService hqclcfDeptService;

	@Resource
	private HqclcfBusinessService hqclcfBusinessService;

	@Resource
	private BaseService baseService;

	@Resource
	private HqclcfEmpService hqclcfEmpService;

	@Resource
	private SysHolidayService sysHolidayService;
	@Resource
	private CfTimeLockMapper cfTimeLockMapper;

	/**
	 * 初始化页面数据
	 * 
	 * @throws Exception
	 */
	@RequestMapping("init")
	public ModelAndView gotoInitPage(HttpServletRequest request) throws Exception {
		try {
			baseService.buildCfInitReq(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView view = new ModelAndView();
		baseService.addObject(view, Constant.CODE_LIST, Constant.LEAVE_TYPE);
		view.setViewName(ConstantCtl.VACATEMANAGE_INIT_ADDRESS);
		return view;
	}

	@RequestMapping("list")
	@ResponseBody
	public Grid<CfVacateManage> list(CfVacateManage queryParams, PageBean pageBean, HttpServletRequest request) {
		try {
			SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
			List<HqclcfDept> cfDepts = onlineUser.getCfDepts();// 有权限查看的部门
			if (onlineUser.getHqclcfEmp() != null
					&& RankType.SALES_MANAGER.getNum().equals(onlineUser.getHqclcfEmp().getPosition())) {// 职务是营业部经理
				if (onlineUser.getFiliale() != null) {// 营业部经理可以处理上级为分公司的部门的数据
					cfDepts.add(onlineUser.getFiliale());
				}
			}
			if (cfDepts != null && cfDepts.size() > 0) {
				List<Long> deptIds = new ArrayList<Long>();// 数据权限基本范围
				for (HqclcfDept dept : cfDepts) {
					deptIds.add(dept.getId());
				}
				queryParams.setDeptIds(deptIds);
			}
			Grid<CfVacateManage> grid = vacateManageService.queryByPage(queryParams, pageBean);
			return grid;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return new Grid<CfVacateManage>();
	}

	/**
	 * 添加页面
	 */
	@RequestMapping("gotoAdd")
	public ModelAndView addInit() throws Exception {
		ModelAndView view = new ModelAndView();
		baseService.addObject(view, Constant.CODE_LIST, Constant.LEAVE_TYPE);
		view.setViewName(ConstantCtl.VACATEMANAGE_ADD_ADDRESS);
		return view;
	}

	/**
	 * 保存操作
	 */
	@RequestMapping("save")
	@ResponseBody
	public Json save(CfVacateManage manage, HttpServletRequest request) {
		SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
		Json result = new Json();
		try {
			CfVacateManage vacat = new CfVacateManage();
			// 获取员工信息
			HqclcfEmp hqclcfEmp = hqclcfEmpService.queryEmpByEmpNo(manage.getEmpNo());
			if (hqclcfEmp == null) {
				result.setSuccess(false);
				result.setMsg("未查询到员工信息！");
				return result;
			}

			vacat.setEmpNo(manage.getEmpNo());
			vacat.setEmpName(hqclcfEmp.getEmpName());
			vacat.setLeaveType(manage.getLeaveType());
			String totalTime = manage.getTotalTime();
			vacat.setStartTime((manage.getStartTime().length() == 16) ? manage.getStartTime()
					: manage.getStartTime().substring(0, 13) + ":00");
			vacat.setEndTime((manage.getEndTime().length() == 16) ? manage.getEndTime()
					: manage.getEndTime().substring(0, 13) + ":00");
			vacat.setLeaveInterval(vacat.getStartTime() + " 至  " + vacat.getEndTime());

			vacat.setTotalTime(totalTime);
			vacat.setStartTimecorspMonth(manage.getStartTime().substring(0, 7));
			vacat.setEndTimecorspMonth(manage.getEndTime().substring(0, 7));
			vacat.setLeaveReason(manage.getLeaveReason());
			vacat.setStatus(manage.getStatus());
			vacat.setDateofLeave(manage.getStartTime().substring(0, 7));
			boolean flag = true;
			String post = hqclcfEmp.getPosition();// 职务
			vacat.setPost(post);
			vacat.setRankType(hqclcfEmp.getRank());

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("deptCode", hqclcfEmp.getDeptNo());
			List<HqclcfDept> depts = hqclcfDeptService.queryParentDepts(params);
			if (depts != null && depts.size() > 0) {
				vacat.setDeptId(depts.get(0).getId() + "");
				for (HqclcfDept dept : depts) {
					if (Constant.DEPT_TYPE_LEVEL1.equals(dept.getDeptType())) {// 分部--大区--1
						vacat.setRegionId(dept.getId() + "");
					}
					if (Constant.DEPT_TYPE_LEVEL2.equals(dept.getDeptType())) {// 区域--分公司--2
						vacat.setFilialeId(dept.getId() + "");
					}
					if (Constant.DEPT_TYPE_LEVEL3.equals(dept.getDeptType())) {// 营业部--营业部--3
						vacat.setBusinessDeptId("" + dept.getId());
					}
					if (Constant.DEPT_TYPE_LEVEL4.equals(dept.getDeptType())) {// 团队--4
						vacat.setTeamId(dept.getId() + "");
					}
				}
			}
			// 某个营业部录入城市经理的时候做标记
			if (RankType.CITY_MANAGER.getNum().equals(post)) {
				// 一种是营业部录入 另外一种是城市分中心经理自己录入(必须限制唯一录入)
				if (onlineUser.getCurrentDept() != null) {
					vacat.setSalesCreateFlag(onlineUser.getCurrentDept().getDeptCode());
				}
			}
			SysHoliday holiday = new SysHoliday();
			List<SysHoliday> holidays = sysHolidayService.queryAll(holiday);

			// 是否跨月
			if (manage.getStartTime().substring(0, 7).equals(manage.getEndTime().substring(0, 7))) {//不跨月
				vacat.setDayBystart(totalTime);
				vacat.setDayByend(totalTime);
				flag = false;
			} else {
				// 获取不同职级跨月的总天数
				CalculateHoursUtil util = new CalculateHoursUtil();
				Map<String, Object> map = util.calculateHoursBydifferentMonth(manage.getStartTime(),
						manage.getEndTime(), post, holidays);
				String day1 = map.get("startTimecorspMonth").toString();
				String day2 = map.get("endTimecorspMonth").toString();
				vacat.setDayBystart(day1);
				vacat.setDayByend(day2);
			}
			vacat.setCreateuser(onlineUser.getFullName());
			vacat.setPriNumber(manage.getPriNumber());
			Json json = vacateManageService.saveOrUpdate(vacat, flag, manage, holidays);
			if (json == null || !json.isSuccess()) {
				result.setSuccess(false);
				result.setMsg(json.getMsg());
				return result;
			}
			result.setSuccess(json.isSuccess());
		} catch (Exception e) {
			log.error("保存请休假管理出错！", e);
			e.printStackTrace();
			result.setMsg(e.getMessage());
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * easyui-combogrid
	 */
	@RequestMapping("queryEmp")
	@ResponseBody
	public Object querySalaryEmp(HttpServletRequest request, @Param("data") String data, @Param("q") String q, PageBean pageBean,@Param("rows") int rows,@Param("page") int page) throws Exception {
		JSONObject ret = new JSONObject();
		Grid<Map<String, Object>> grid = new Grid<Map<String, Object>>();
		// 当不是超级管理员登录的时候
		SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
		Map<String, Object> queryParam = new HashMap<String, Object>();
		List<HqclcfDept> cfDepts = onlineUser.getCfDepts();// 有权限查看的部门
		if (onlineUser.getHqclcfEmp() != null
				&& RankType.SALES_MANAGER.getNum().equals(onlineUser.getHqclcfEmp().getPosition())) {// 职务是营业部经理
			if (onlineUser.getFiliale() != null) {// 营业部经理可以处理上级为分公司的部门的数据
				cfDepts.add(onlineUser.getFiliale());
			}
		}
		List<Long> deptIds = new ArrayList<Long>();// 数据权限基本范围
		if (cfDepts != null && cfDepts.size() > 0) {
			for (HqclcfDept dept : cfDepts) {
				deptIds.add(dept.getId());
			}
		}
		queryParam.put("deptIds", deptIds);
		if (!StringUtil.isEmpty(q)){
			queryParam.put("empNo",q.toUpperCase());
		}

		try {
			PageHelper.startPage(page, rows);
			grid = vacateManageService.querySalaryEmp(queryParam, pageBean);
			ret.put("rows", grid.getData());
			ret.put("total", grid.getCount());
		} catch (Exception e) {
			log.error("为新增请休假管理查询员工信息时异常", e);
		}
		return ret;
	}

	/**
	 * 结束时间计算节假日天数
	 */
	@RequestMapping("statisticsTotalday")
	@ResponseBody
	public Map<String, Object> statisticsTotalday(HttpServletRequest request, String startTime, String endTime,
			String empNo,String priNumber) throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		HqclcfGzym gzym = vacateManageService.queryCurrGzym();
		Date curGzym = format.parse(gzym.getCurrentGzym());
		Map<String, Object> map = new HashMap<String, Object>();
		Date startDate = null;
		Date endDate = null;
		// 验证选择是否符合逻辑
		if (empNo == null || "".equals(empNo)) {
			map.put("msg", "员工编码不能为空！</br>");
			map.put("success", false);
			map.put("reset", "2");
			return map;
		}
		// 开始时间只能大于等于工资年月

		if (startTime == null || "".equals(startTime)) {
			map.put("msg", "起始时间不能为空！</br>");
			map.put("success", false);
			map.put("reset", "1");
			return map;
		} else if (endTime == null || "".equals(endTime)) {
			map.put("msg", "结束时间不能为空！</br>");
			map.put("success", false);
			map.put("reset", "1");
			return map;
		} else {
			// 如果两者都不为空，比较大小
			startDate = DateUtil.parseStringToDate(startTime + ":00", "yyyy-MM-dd HH:mm");
			endDate = DateUtil.parseStringToDate(endTime + ":00", "yyyy-MM-dd HH:mm");
			int i = startDate.compareTo(endDate);
			if (i != -1) {
				map.put("msg", "请假起始时间不能大于结束时间！");
				map.put("success", false);
				map.put("reset", "2");
				return map;
			}
			Date parse = format.parse(startTime.substring(0, 7));
			// 如果开始时间比工资年月小
			if (parse.compareTo(curGzym) == -1) {
				map.put("msg", "请假起始时间不能小于当前工资年月！");
				map.put("success", false);
				map.put("reset", "2");
				return map;
			}
			// 在检查下开始时间在这个工资年月内是否被锁定了
			Integer yyyy = Integer.valueOf(startTime.substring(0, 7).split("-")[0]);
			Integer mm = Integer.valueOf(startTime.substring(0, 7).split("-")[1]);
			CfTimeLock timelock = new CfTimeLock();
			timelock.setYear(yyyy);
			timelock.setMonth(mm);
			CfTimeLock xjTimeLockList = cfTimeLockMapper.queryIsLock(timelock);
			if ( xjTimeLockList != null && xjTimeLockList.getIsLock() == 1) {
				// 开始时间被锁定
				map.put("msg", "开始时间为" + startTime.substring(0, 7) + "的年月，在考勤排班里已经被锁定！");
				map.put("success", false);
				map.put("reset", "2");
				return map;
			}
		}
		// 这里判断为不为空 返回提示信息
		Json js = vacateManageService.calculationbydatas(empNo, startDate, endDate,priNumber);
		map.put("success", js.isSuccess());
		map.put("totalday", js.getObj());
		map.put("msg", js.getMsg());
		return map;
	}

	/**
	 * @Title: 提交
	 * @param：@throws Exception
	 * @Description：(提交所选状态为 未提交和拒绝的)
	 */
	@RequestMapping("submitDatas")
	@ResponseBody
	public Json submitDatas(String ids) throws Exception {
		Json json = new Json();
		try {
			JSONArray array = JSONArray.parseArray(ids);
			List<Long> list = new ArrayList<Long>();
			for (int i = 0; i < array.size(); i++) {
				list.add(array.getLong(i));
			}
			// 修改这些数据的状态
			Json js = vacateManageService.submitThisDatas(list);
			if (js.isSuccess()) {
				json.setSuccess(true);
				json.setMsg("提交成功！");
			} else {
				json.setSuccess(false);
				json.setMsg("提交失败");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			json.setSuccess(false);
			json.setMsg(e.getMessage());
		}
		return json;
	}

	/**
	 * 撤销：待审核
	 */
	@RequestMapping("revoke")
	@ResponseBody
	public Json revoke(String ids) {
		Json json = new Json();
		try {
			JSONArray array = JSONArray.parseArray(ids);
			List<Long> list = new ArrayList<Long>();
			for (int i = 0; i < array.size(); i++) {
				list.add(array.getLong(i));
			}
			Json js = vacateManageService.revokeThisDatas(list);
			if (js.isSuccess()) {
				json.setSuccess(true);
				json.setMsg("撤销成功！");
			} else {
				json.setSuccess(false);
				json.setMsg("撤销失败！");
			}
		} catch (Exception e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			log.error(e.getMessage(), e);
		}
		return json;
	}

	/**
	 * 审核：待审核
	 */
	@RequestMapping("review")
	@ResponseBody
	public Json review(String ids, String status, String auditOpinion) {
		Json json = new Json();
		try {
			// 把这些ID放到list
			JSONArray array = JSONArray.parseArray(ids);
			List<Long> list = new ArrayList<Long>();
			for (int i = 0; i < array.size(); i++) {
				list.add(array.getLong(i));
			}
			// 这些数据的应该设置的状态和审核意见都是一样
			Json js = vacateManageService.reviewThisDatas(list, status, auditOpinion);
			if (js.isSuccess()) {
				json.setSuccess(true);
				json.setMsg("审核成功！");
			} else {
				json.setSuccess(false);
				json.setMsg("审核失败！");
			}
		} catch (Exception e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			log.error(e.getMessage(), e);
		}
		return json;
	}

	/**
	 * 反审核 通过-未提交
	 */
	@RequestMapping("unreview")
	@ResponseBody
	public Json unreview(String ids) {
		Json json = new Json();
		try {
			JSONArray array = JSONArray.parseArray(ids);
			List<Long> list = new ArrayList<Long>();
			for (int i = 0; i < array.size(); i++) {
				list.add(array.getLong(i));
			}
			Json js = vacateManageService.unreviewThisdatas(list);
			if (js.isSuccess()) {
				json.setSuccess(true);
				json.setMsg("反审核成功！");
			} else {
				json.setSuccess(false);
				json.setMsg("反审核失败！");
			}
		} catch (Exception e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			log.error(e.getMessage(), e);
		}
		return json;
	}

	/**
	 * 删除
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Json delete(String ids, HttpServletRequest request) {
		Json json = new Json();
		try {
			Json js = vacateManageService.delete(ids);
			json.setSuccess(js.isSuccess());
			json.setMsg(js.getMsg());
			return json;
		} catch (Exception e) {
			json.setMsg(e.getMessage());
			json.setSuccess(false);
			log.error("删除请休假管理出错！", e);
			return json;
		}
	}

	/**
	 * 导出
	 */
	@RequestMapping("exportExl")
	public void exportExl(CfVacateManage queryParams, HttpServletRequest request, HttpServletResponse response) {
		try {
			SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
			List<HqclcfDept> cfDepts = onlineUser.getCfDepts();// 有权限查看的部门
			if (onlineUser.getHqclcfEmp() != null
					&& RankType.SALES_MANAGER.getNum().equals(onlineUser.getHqclcfEmp().getPosition())) {// 职务是营业部经理
				if (onlineUser.getFiliale() != null) {// 营业部经理可以处理上级为分公司的部门的数据
					cfDepts.add(onlineUser.getFiliale());
				}
			}
			if (cfDepts != null && cfDepts.size() > 0) {
				List<Long> deptIds = new ArrayList<Long>();// 数据权限基本范围
				for (HqclcfDept dept : cfDepts) {
					deptIds.add(dept.getId());
				}
				queryParams.setDeptIds(deptIds);
			}
			// 分页设置为空 不分页查询多少导出多少
			Grid<CfVacateManage> grid = vacateManageService.queryByPage(queryParams, null);
			List<CfVacateManage> list = grid.getData();
			
			//获取授权的部门信息及上级部门信息（被授权的部门的上级部门信息需要被展示）
			List<Long> deptIds = new ArrayList<Long>();
			if (cfDepts != null && cfDepts.size() > 0) {
				for (HqclcfDept dept : cfDepts) {
					deptIds.add(dept.getId());
				}
			}
			Map<String, Object> map = new HashMap<String,Object>();
			map.put("type", "up");
			map.put("deptIds", deptIds);
			List<HqclcfDept> allDepts = hqclcfDeptService.queryParentDepts(map);//授权部门及上级部门信息
			
			List<HqclcfRank> hqclcfRanks = hqclcfBusinessService.getRankList();
			vacateManageService.exportExlByquery(list, allDepts, hqclcfRanks, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
