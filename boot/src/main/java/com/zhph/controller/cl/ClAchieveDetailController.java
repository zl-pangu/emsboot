package com.zhph.controller.cl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.model.cl.ClAchieveDetail;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfGzym;
import com.zhph.model.sys.SysUser;
import com.zhph.service.cl.ClAchieveDetailService;
import com.zhph.service.cl.HqclcfGzymService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import com.zhph.util.StringUtil;



/**
 * 信贷业绩明细
 */
@Controller
@RequestMapping("/cl/clAchieveDetail")
public class ClAchieveDetailController {
	private Log log = LogFactory.getLog(ClAchieveDetailController.class);

	private static final String PRINUMBER = "priNumber";// 定义全局变量
	private static final String DATA = "data";// 定义全局变量
	private static final String MSG = "msg";// 定义全局变量
	private static final String CSCW = "参数错误";// 定义全局变量

	@Resource
	private ClAchieveDetailService xdAchieveDetailLogic;
	@Resource
	private HqclcfGzymService hqclcfGzymService;
	
	@Resource
	private HqclcfDeptService hqclcfDeptService;
/*	@Autowired
	private CreditAreaManagementLogicInterface creditAreaManagementLogic;
	@Autowired
	private SalaryP2pOrgLogicInterface salaryP2pOrgLogic;
	@Autowired
	private DimNodeLogicInterface dimNodeLogic;*/

	/**
	 * 获取当前工资年月
	 */
	private String getCurrentGzym() throws Exception {
		HqclcfGzym salaryGzym = hqclcfGzymService.queryCurrGzym();
		if (salaryGzym == null) {
			return null;
		}
		String gzym = salaryGzym.getCurrentGzym();
		return gzym;
	}

	/**
	 * 判断当前日期是否允许同步业绩明细
	 */
	private static boolean checkNowIsAllowToSync() {
		int startDt = 1;
		int endDt = 15;
		int nowDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
		if (nowDate >= startDt && nowDate <= endDt) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 分公司权限控制
	 */
	private void orgPermissionControl(HttpServletRequest req, HttpServletResponse res, ClAchieveDetail params) throws Exception {
        SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");
		if (onlineUser == null) {
			throw new Exception("会话失效！");
		}

		HqclcfDept org = onlineUser.getCurrentDept();
		if (org == null) {
			throw new Exception("用户异常：当前用户没有被分配到部门或分公司");
		}
/*		Org org = onlineUser.getOrg();
		if (org == null) {
			throw new Exception("用户异常：当前用户没有被分配到部门或分公司");
		}

		String orgNo = org.getOrg_no();
		if (!"HQ".equals(orgNo)) {
			if (params != null) {
				params.setOrgNo(orgNo);
			}
			req.setAttribute("orgPermission", orgNo);
		}*/
	}

	/**
	 * setAttribute公共方法
	 * @Title: setAttributes
	 * @param @param req
	 * @return void
	 * @throws
	 */
	private void setAttributes(HttpServletRequest req) throws Exception {
        SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");

/*		req.setAttribute("areaList", creditAreaManagementLogic.queryAll());
		req.setAttribute("orgList", salaryP2pOrgLogic.querySalaryP2pOrg());
		req.setAttribute("deptList", dimNodeLogic.getDimNodesByTreeId(ConstantsApplication.JK_DEPT));
		req.setAttribute("teamList", dimNodeLogic.getDimNodesByTreeId(ConstantsApplication.JK_P2P_TEAM));*/
        
        List<HqclcfDept> depts = onlineUser.getClDepts();
        List<HqclcfDept> areaList = new ArrayList<>();
        List<HqclcfDept> orgList = new ArrayList<>();
        List<HqclcfDept> deptList = new ArrayList<>();
        List<HqclcfDept> teamList = new ArrayList<>();
        if(depts != null && depts.size() > 0){
        	for (HqclcfDept dept : depts) {
        		if(Constant.DEPT_TYPE_LEVEL1.equals(dept.getDeptType())){
        			areaList.add(dept);
        		}else if(Constant.DEPT_TYPE_LEVEL2.equals(dept.getDeptType())){
        			orgList.add(dept);
        		}else if(Constant.DEPT_TYPE_LEVEL3.equals(dept.getDeptType())){
        			deptList.add(dept);
        		}else if(Constant.DEPT_TYPE_LEVEL4.equals(dept.getDeptType())){
        			teamList.add(dept);
        		}
			}
        }
		req.setAttribute("areaList", areaList);
		req.setAttribute("orgList", orgList);
		req.setAttribute("deptList", deptList);
		req.setAttribute("teamList", teamList);
	}

	/**
	 * 初始化业绩明细页面
	 * @Title: init
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("init")
	public String init(HttpServletRequest req, HttpServletResponse res) {
		log.debug("初始化业绩明细页面-开始");
		try {
			HqclcfGzym salaryGzym = hqclcfGzymService.queryCurrGzym();
			if (salaryGzym != null) {
				req.setAttribute("gzym", salaryGzym.getCurrentGzym());
			}
			req.setAttribute("nowIsAllowToSync", checkNowIsAllowToSync());
			this.setAttributes(req);

			log.debug("初始化业绩明细页面-完成");
			return "pages/cl/clAchieveDetail/achievedetail_list";
		} catch (Exception e) {
			log.error("初始化业绩明细页面-异常", e);
			e.printStackTrace();
			req.setAttribute("reason", e.getMessage());
			return "pages/common/errors";
		}
	}

	/**
	 * 获取请求参数
	 * 
	 * @Title: getRequestParams
	 * @param @param req
	 * @param @return
	 * @return ClAchieveDetail
	 * @throws
	 */
	private ClAchieveDetail getRequestParams(HttpServletRequest req) {
		ClAchieveDetail params = new ClAchieveDetail();
		params.setPriNumber(StringUtil.isEmpty(req.getParameter(PRINUMBER)) ? null : Long.valueOf(req.getParameter(PRINUMBER)));
		params.setLoanContractNo(req.getParameter("loanContractNo"));
		params.setLoanName(req.getParameter("loanName"));
		params.setBusinessManagerNo(req.getParameter("businessManagerNo"));
		params.setTeamManagerNo(req.getParameter("teamManagerNo"));
		params.setOrgManagerNo(req.getParameter("orgManagerNo"));
		params.setAreaManagerNo(req.getParameter("areaManagerNo"));
		params.setWfEmpNo(req.getParameter("wfEmpNo"));
		params.setXsEmpNo(req.getParameter("xsEmpNo"));
		params.setKfEmpNo(req.getParameter("kfEmpNo"));
		params.setDeptNo(req.getParameter("deptNo"));
		params.setGzym(req.getParameter("gzym"));
		params.setGzymMin(req.getParameter("gzymMin"));
		params.setGzymMax(req.getParameter("gzymMax"));

		params.setPage(req.getParameter("page") == null ? null : Integer.valueOf(req.getParameter("page")));
		params.setRows(req.getParameter("rows") == null ? null : Integer.valueOf(req.getParameter("rows")));

		params.setSort(req.getParameter("sort"));
		params.setOrder(req.getParameter("order"));

		return params;
	}

	/**
	 * 异步查询页面明细数据
	 */
	@ResponseBody
	@RequestMapping("query")
	public Object queryClAchieveDetail(ClAchieveDetail clAchieveDetail,PageBean pageBean,HttpServletRequest request) {
		log.debug("异步查询页面明细数据-开始");
		JSONObject json = new JSONObject();
		json.put("code", "200");
		try {
            SysUser onlineUser = (SysUser) request.getSession().getAttribute("onlineUser");
			List<HqclcfDept> cfDepts = onlineUser.getCfDepts();// 有权限查看的部门
			if (cfDepts != null && cfDepts.size() > 0) {
				List<String> deptNos = new ArrayList<String>();// 数据权限基本范围
				for (HqclcfDept dept : cfDepts) {
					deptNos.add(dept.getDeptCode());
				}
				clAchieveDetail.setDeptNos(deptNos);
			}
			Grid<ClAchieveDetail> grid = xdAchieveDetailLogic.queryClAchieveDetailGrid(clAchieveDetail,pageBean);
			JSONObject data = new JSONObject();
			JSONObject gridObj = new JSONObject();
			gridObj.put("rows", grid.getData());
			gridObj.put("total", grid.getCount());
			data.put("grid", gridObj);
			json.put(DATA, data);
			log.debug("异步查询页面明细数据-结束");
		} catch (Exception e) {
			log.error("异步查询页面明细数据-异常", e);
			if (e instanceof Exception) {
				Exception e1 = (Exception) e;
				json.put("code", "500");
				json.put(MSG, e1.getMessage());
				return json;
			} else {
				json.put("code", "500");
				json.put(MSG, e.getMessage());
			}
			return json;
		}
		return json;
	}

	/**
	 * 检查是否存在当前年月的业绩明细数据
	 * 
	 * @Title: checkCurGzymClAchieveDetailIsEmpty
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("check")
	public Object checkCurGzymClAchieveDetailIsEmpty(HttpServletRequest req, HttpServletResponse res) {
		log.debug("检查是否存在当前年月的业绩明细数据-开始");
		JSONObject json = new JSONObject();
		json.put("success", true);
		try {
			boolean nowIsAllowToSync = checkNowIsAllowToSync();
			if (!nowIsAllowToSync) {
				throw new Exception("当前日期不允许同步业绩明细！");
			}
			String gzym = getCurrentGzym();
			if (StringUtil.isEmpty(gzym)) {
				throw new Exception("没有获取到当前工资年月");
			}
			ClAchieveDetail params = new ClAchieveDetail();
			params.setGzym(gzym);
			Integer count = xdAchieveDetailLogic.queryClAchieveDetailsCount(params);
			if (count == null) {
				throw new Exception("查询当前工资年月的业绩明细总数失败");
			}
			JSONObject data = new JSONObject();
			data.put("isExist", count > 0);
			json.put(DATA, data);
			log.debug("检查是否存在当前年月的业绩明细数据-结束");
		} catch (Exception e) {
			if (e instanceof Exception) {
				Exception e1 = (Exception) e;
				json.put(MSG, e1.getMessage());
			} else {
				json.put(MSG, e.getMessage());
			}
			json.put("success", false);
			log.error("检查是否存在当前年月的业绩明细数据-异常", e);
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping("syncWin")
	public String syncWin(HttpServletRequest req, HttpServletResponse res) {
		return	"pages/cl/clAchieveDetail/achieveDetail_syncWin";
	}

	/**
	 * 同步当前工资年月的业绩明细数据
	 * 
	 * @Title: syncCurGzymClAchieveDetail
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("sync")
	public Object syncCurGzymClAchieveDetail(HttpServletRequest req, HttpServletResponse res) {
		log.debug("同步当前工资年月的业绩明细数据-开始");
		JSONObject json = new JSONObject();
		
		try {
			boolean nowIsAllowToSync = checkNowIsAllowToSync();
			if (!nowIsAllowToSync) {
				throw new Exception("当前日期不允许同步业绩明细！");
			}
			String gzym = getCurrentGzym();
			if (StringUtil.isEmpty(gzym)) {
				throw new Exception("没有获取到当前工资年月");
			}
			//当月修改上月的数据
			String[] split = gzym.split("-");
			int month = Integer.valueOf(split[1]);
			int yaer = Integer.valueOf(split[0]);
			String gzymN =null;
			if (month==1) {
				 gzymN = (yaer-1) + "-" + (12);
			}else{
				 gzymN = (yaer) + "-" + (month-1);
			}
			
			ClAchieveDetail params = new ClAchieveDetail();
			params.setGzym(gzymN);
			Integer count = xdAchieveDetailLogic.queryClAchieveDetailsCount(params);
			if (count == null) {
				throw new Exception("查询当前工资年月的业绩明细总数失败");
			}
			if (count > 0) {
				throw new Exception("系统中已经存在当前工资年月的业绩明细数据，如需重新同步，请联系管理员。");
			}
			// 同步
	        SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");
			List<ClAchieveDetail> list = xdAchieveDetailLogic.syncClAchieveDetail(gzymN, onlineUser);
			int num = list == null ? 0 : list.size();
			JSONObject data = new JSONObject();
			data.put("num", num);
			json.put(DATA, data);
			json.put(MSG, "成功同步当前年月[" + gzymN + "]" + num + "条业绩明细");
			json.put("success", true);
			json.put("code",200);
			log.debug("同步当前工资年月的业绩明细数据-成功");
		} catch (Exception e) {
			if (e instanceof Exception) {
				json.put("success", false);
				Exception e1 = (Exception) e;
				json.put("code",500);
				json.put(MSG, e1.getMessage());
				return json;
			} else {
				json.put("code",500);
				json.put(MSG, e.getMessage());
			}
			log.error("同步当前工资年月的业绩明细数据-异常", e);
			e.printStackTrace();
			return json;
		}
		return json;
	}

	/**
	 * 导出业绩明细
	 * 
	 * @Title: exportClAchieveDetail
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	/*@ExportTarget(moduleName = "导出信贷业绩明细")*/
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public Object exportClAchieveDetail(HttpServletRequest req, HttpServletResponse res) {
		log.debug("导出业绩明细-开始");
		JSONObject json = new JSONObject();
		json.put("code", "200");

		try {
            SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");
			ClAchieveDetail params = getRequestParams(req);
			List<HqclcfDept> cfDepts = onlineUser.getCfDepts();// 有权限查看的部门
			if (cfDepts != null && cfDepts.size() > 0) {
				List<String> deptNos = new ArrayList<String>();// 数据权限基本范围
				for (HqclcfDept dept : cfDepts) {
					deptNos.add(dept.getDeptCode());
				}
				params.setDeptNos(deptNos);
			}
			/*this.orgPermissionControl(req, res, params);*/// 分公司权限控制
			xdAchieveDetailLogic.exportClAchieveDetail(res, params, HSSFWorkbook.class);// 导出为excle
			log.debug("导出业绩明细-完成");
			return null;
		} catch (Exception e) {
			log.error("导出业绩明细-异常", e);
			e.printStackTrace();
			req.setAttribute("reason", e.getMessage());
			return "pages/common/errors";
		}
	}

	@RequestMapping("deleteWin")
	public String deleteWin(HttpServletRequest req, HttpServletResponse res) {
		return	"pages/cl/clAchieveDetail/achieveDetail_deleteWin";
	}
	
	
	/**
	 * 删除一个业绩明细
	 * 
	 * @Title: deleteClAchieveDetails
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("delete")
	public Object deleteClAchieveDetail(HttpServletRequest req, HttpServletResponse res) {
		log.debug("删除一个业绩明细-开始");
		JSONObject json = new JSONObject();
		
		try {
			String priNumber = req.getParameter(PRINUMBER);
			if (StringUtil.isEmpty(priNumber)) {
				throw new Exception(CSCW);
			}
			ClAchieveDetail params = new ClAchieveDetail();
			params.setPriNumber(Long.valueOf(priNumber));
//			this.orgPermissionControl(req, res, params);

			ClAchieveDetail detail = xdAchieveDetailLogic.queryClAchieveDetailByPriNumber(params.getPriNumber());
			if (detail == null)
				throw new Exception("业绩明细不存在");
			String gzym = getCurrentGzym();
			if (StringUtil.isEmpty(gzym)) {
				throw new Exception("没有获取到当前工资年月");
			}
			if (!gzym.equals(detail.getGzym()))
				throw new Exception("只允许删除当前工资年月的数据");
	        SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");
			xdAchieveDetailLogic.deleteClAchieveDetail(params, onlineUser);
			json.put(MSG, "删除成功！");
			json.put("code", 200);
			json.put("success", true);
			log.debug("删除一个业绩明细-结束");
		} catch (Exception e) {
			json.put("success", false);
			if (e instanceof Exception) {
				Exception e1 = (Exception) e;
				json.put("code", 500);
				json.put(MSG, e1.getMessage());
				return json;
			} else {
				json.put("code", 500);
				json.put(MSG, e.getMessage());
			}
			log.error("删除一个业绩明细-异常", e);
			e.printStackTrace();
			return json;
		}
		return json;
	}

	/**
	 * 打开业绩明细详情窗口
	 * @Title: initWin
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("initWin")
	public String initWin(HttpServletRequest req, HttpServletResponse res) {
		log.debug("打开业绩明细详情窗口-开始");
		System.out.println("打开业绩明细详情窗口-开始");
		try {
			String priNumber = req.getParameter(PRINUMBER);
			if (StringUtil.isEmpty(priNumber)) {
				throw new Exception(CSCW);
			}
			String type = req.getParameter("type");
			if (StringUtil.isEmpty(type)) {
				throw new Exception(CSCW);
			}

			ClAchieveDetail params = new ClAchieveDetail();
			params.setPriNumber(Long.valueOf(priNumber));
             
//			this.orgPermissionControl(req, res, params);// 分公司权限控制

			ClAchieveDetail detail = xdAchieveDetailLogic.queryClAchieveDetailByPriNumber(params.getPriNumber());
			if (detail == null)
				throw new Exception("业绩明细不存在");
			req.setAttribute("detail", detail);

			req.setAttribute(PRINUMBER, priNumber);
			req.setAttribute("type", type);

			if ("update".equals(type)) {
				String gzym = getCurrentGzym();
				if (StringUtil.isEmpty(gzym)) {
					throw new Exception("没有获取到当前工资年月");
				}
				if (!gzym.equals(detail.getGzym()))
					throw new Exception("只允许修改当前工资年月的数据");

				this.setAttributes(req);
				return	"pages/cl/clAchieveDetail/achieveDetail_update";
			}

			log.debug("打开业绩明细详情窗口-完成");
			return "pages/cl/clAchieveDetail/achieveDetail_win";
		} catch (Exception e) {
			log.error("打开业绩明细详情窗口-异常", e);
			e.printStackTrace();
			req.setAttribute("reason", e.getMessage());
			return "pages/common/errors";
		}
	}

	/**
	 * 保存修改的业绩明细
	 * 
	 * @Title: updateDetail
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("update")
	public Object updateDetail(HttpServletRequest req, HttpServletResponse res,ClAchieveDetail params,@Param("data")String data) {
		log.debug("保存修改的业绩明细-开始");
		
		JSONObject json = JSON.parseObject(data);
		
		try {
			String priNumberString = json.getString("priNumber");
			String priNumber = priNumberString.replace(",","");
			
			String grantLoanAmountString = json.getString("grantLoanAmount");
			String grantLoanAmount = grantLoanAmountString.replace(",","");
			BigDecimal grantLoanAmountBd=new BigDecimal(grantLoanAmount);
			
			String loanAmountString = json.getString("loanAmount");
			String loanAmount = loanAmountString.replace(",","");
			BigDecimal loanAmountBd=new BigDecimal(loanAmount);
			
			String areaName = json.getString("areaName");
			HqclcfDept queryAreaNoByCode = hqclcfDeptService.queryDeptByCode(areaName);
			String orgName = json.getString("orgName");
			HqclcfDept queryOrgNoByCode = hqclcfDeptService.queryDeptChildNameByCode(orgName,queryAreaNoByCode.getDeptCode());
			String deptName = json.getString("deptName");
			HqclcfDept queryDeptNoByCode = hqclcfDeptService.queryDeptChildNameByCode(deptName,queryOrgNoByCode.getDeptCode());
			String teamName = json.getString("teamName");
			HqclcfDept queryTeamNoByCode = hqclcfDeptService.queryDeptChildNameByCode(teamName,queryDeptNoByCode.getDeptCode());
			
			json.put("areaNo", queryAreaNoByCode.getDeptCode());
			json.put("orgNo",queryOrgNoByCode.getDeptCode());
			json.put("deptNo",queryDeptNoByCode.getDeptCode());
			json.put("teamNo",queryTeamNoByCode.getDeptCode());
			
			/*params = getRequestParams(req);
			Long priNumber = params.getPriNumber();*/
			if (priNumber == null)
				throw new Exception("参数错误");

			//String gzym = getCurrentGzym();
			String gzym = json.getString("gzym");
			if (StringUtil.isEmpty(gzym)) {
				throw new Exception("没有获取到当前工资年月");
			}

			ClAchieveDetail detail = xdAchieveDetailLogic.queryClAchieveDetailByPriNumber(Long.parseLong(priNumber));
			if (!gzym.equals(detail.getGzym()))
				throw new Exception("只允许修改当前工资年月的数据");

			ClAchieveDetail queryParams = new ClAchieveDetail();
			queryParams.setPriNumber(detail.getPriNumber());
			//添加数据到修改后的对象
			/*params.setPriNumber(Long.parseLong(priNumber));
			String teamName = json.getString("teamName");
			String areaManagerNo = json.getString("areaManagerNo");
			String deptName = json.getString("deptName");
			String createTime = json.getString("createTime");*/
			
			
			@SuppressWarnings("unchecked")
			Map<String,Object> value = JSON.parseObject(data,Map.class);
			value.remove("priNumber");
			value.remove("grantLoanAmount");
			value.remove("loanAmount");
			String jsonString = JSON.toJSONString(value);
			params=JSON.parseObject(jsonString, ClAchieveDetail.class);
			params.setPriNumber(Long.parseLong(priNumber));
			params.setGrantLoanAmount(grantLoanAmountBd);
			params.setLoanAmount(loanAmountBd);
			params.setAreaName(areaName);
			params.setOrgName(orgName);
			params.setDeptName(deptName);
			params.setTeamName(teamName);
//			this.orgPermissionControl(req, res, queryParams);

	        SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");
			xdAchieveDetailLogic.updateClAchieveDetail(queryParams, params, onlineUser);
			json.put(MSG, "保存成功！");
			json.put("code", 200);
			json.put("success", true);
			log.debug("保存修改的业绩明细-结束");
		} catch (Exception e) {
			json.put("success", false);
			if (e instanceof Exception) {
				Exception e1 = (Exception) e;
				json.put("code", 500);
				json.put(MSG, e1.getMessage());
				return json;
			} else {
				json.put("code", 500);
				json.put(MSG, e.getMessage());
			}
			log.error("保存修改的业绩明细-异常", e);
			e.printStackTrace();
			return json;
		}
		return json;
	}

	/**
	 * 加载批量编辑窗口
	 * 
	 * @Title: initBatchUpdateWin
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@RequestMapping("batchUpdateWin")
	public Object initBatchUpdateWin(HttpServletRequest req, HttpServletResponse res) {
		log.debug("打开业绩明细批量修改窗口-开始");
		try {
			String priNumbers = req.getParameter("priNumbers");
			if (StringUtil.isEmpty(priNumbers)) {
				throw new Exception(CSCW);
			}

			// 检查是否是当前工资年月的业绩明细
			String gzym = getCurrentGzym();
			if (StringUtil.isEmpty(gzym)) {
				throw new Exception("没有获取到当前工资年月");
			}
			String[] priNumberArr = priNumbers.split(",");
			ClAchieveDetail detail = null;
			int notExist = 0;
			int notCurrent = 0;
			for (String priNumber : priNumberArr) {
				detail = xdAchieveDetailLogic.queryClAchieveDetailByPriNumber(Long.valueOf(priNumber));
				if (detail == null)
					notExist++;
				else if (!gzym.equals(detail.getGzym()))
					notCurrent++;
			}
			StringBuffer checkMsg = new StringBuffer("您选择的业绩明细中");
			if (notExist != 0)
				checkMsg.append("，有").append(notExist).append("条不存在");
			if (notCurrent != 0)
				checkMsg.append("，有").append(notCurrent).append("条不是当前工资年月的业绩明细");
			checkMsg.append("请重新选择！");
			if (notExist != 0 || notCurrent != 0)
				throw new Exception(checkMsg.toString());

			req.setAttribute("priNumbers", priNumbers);

			this.orgPermissionControl(req, res, null);// 分公司权限控制

			this.setAttributes(req);

			log.debug("打开业绩明细批量修改窗口-完成");
			return "zhphNewSalary/xdAchieveDetail/xdAchieveDetail_batchUpdateWin";
		} catch (Exception e) {
			log.error("打开业绩明细批量修改窗口-异常", e);
			e.printStackTrace();
			/*req.setAttribute(Constants.ERROR_REASON, e.getMessage());*/
			return "pages/common/errors";
		}
	}

	/**
	 * 业绩明细批量修改保存
	 * 
	 * @Title: batchUpdate
	 * @param @param req
	 * @param @param res
	 * @param @return
	 * @return Object
	 * @throws
	 */
	@ResponseBody
	@RequestMapping(value = "batchUpdate", method = RequestMethod.POST)
	public Object batchUpdate(HttpServletRequest req, HttpServletResponse res) {
		log.debug("保存批量修改的业绩明细-开始");
		JSONObject json = new JSONObject();
		json.put("success", true);
		try {
			// 参数检查
			String priNumbers = req.getParameter("priNumbers");
			if (priNumbers == null)
				throw new Exception(CSCW);
			else
				priNumbers = priNumbers.replaceAll("[\\ \\n\\t]", "");
			if (StringUtil.isEmpty(priNumbers))
				throw new Exception(CSCW);

			String selNos = req.getParameter("selNos");
			if (selNos == null)
				throw new Exception(CSCW);
			else
				selNos = selNos.replaceAll("[\\ \\n\\t]", "");
			if (StringUtil.isEmpty(selNos))
				throw new Exception(CSCW);

			// 参数处理
			String[] priNumberStrArr = priNumbers.split(",");
			int len = priNumberStrArr.length;
			Long[] priNumberArr = new Long[len];
			for (int i = 0; i < len; i++) {
				priNumberArr[i] = Long.parseLong(priNumberStrArr[i]);
			}
			List<Long> priNumberList = Arrays.asList(priNumberArr);
			String[] selNoArr = selNos.split(",");
			List<String> selNoList = Arrays.asList(selNoArr);
			ClAchieveDetail params = getRequestParams(req);

			// 检查是否是当前工资年月的业绩明细
			String gzym = getCurrentGzym();
			if (StringUtil.isEmpty(gzym)) {
				throw new Exception("没有获取到当前工资年月");
			}
			ClAchieveDetail detail = null;
			int notExist = 0;
			int notCurrent = 0;
			for (Long priNumber : priNumberArr) {
				detail = xdAchieveDetailLogic.queryClAchieveDetailByPriNumber(priNumber);
				if (detail == null)
					notExist++;
				else if (!gzym.equals(detail.getGzym()))
					notCurrent++;
			}
			StringBuffer checkMsg = new StringBuffer("您选择的业绩明细中");
			if (notExist != 0)
				checkMsg.append("，有").append(notExist).append("条不存在");
			if (notCurrent != 0)
				checkMsg.append("，有").append(notCurrent).append("条不是当前工资年月的业绩明细");
			checkMsg.append("请重新选择！");
			if (notExist != 0 || notCurrent != 0)
				throw new Exception(checkMsg.toString());

			// 保存
	        SysUser onlineUser = (SysUser) req.getSession().getAttribute("onlineUser");
			xdAchieveDetailLogic.batchUpdateClAchieveDetail(priNumberList, selNoList, params, onlineUser);
			json.put(MSG, "批量修改保存成功！");

			log.debug("保存批量修改的业绩明细-结束");
		} catch (Exception e) {
			json.put("success", false);
			if (e instanceof Exception) {
				json.put("code", "500");
				Exception e1 = (Exception) e;
				json.put(MSG, e1.getMessage());
				return json;
			} else {
				json.put("code", "500");
				json.put(MSG, e.getMessage());
			}
			log.error("保存批量修改的业绩明细-异常", e);
			e.printStackTrace();
			return json;
		}
		return json;
	}
	
	/**
     * 员工姓名或者，编号查询员工信息
     * @param data
     * @param q
     * @return
     * @throws Exception
     */
    @RequestMapping("/querySalaryEmp")
    @ResponseBody
    public Object queryByqSalaryEmp(@Param("rows") int rows,@Param("page") int page) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = xdAchieveDetailLogic.querySalaryEmp();
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
    
   
    @RequestMapping("/queryTeamManagerEmp")
    @ResponseBody
    public Object queryByqTeamManagerEmp(@Param("rows") int rows,@Param("page") int page) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = xdAchieveDetailLogic.queryTeamManagerEmp();
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
    
    @RequestMapping("/queryOrgManagerEmp")
    @ResponseBody
    public Object queryByqOrgManagerEmp(@Param("rows") int rows,@Param("page") int page) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = xdAchieveDetailLogic.queryOrgManagerEmp();
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
    
    @RequestMapping("/queryAreaManagerEmp")
    @ResponseBody
    public Object queryByqAreaManagerEmp(@Param("rows") int rows,@Param("page") int page) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = xdAchieveDetailLogic.queryAreaManagerEmp();
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
    
    @RequestMapping("/queryArea")
    @ResponseBody
    public Object queryByqArea(@Param("rows") int rows,@Param("page") int page,@Param("q") String q) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = xdAchieveDetailLogic.queryArea();
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
    
    @RequestMapping("/queryOrg")
    @ResponseBody
    public Object queryByqOrg(@Param("rows") int rows,@Param("page") int page,@Param("q") String q) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = xdAchieveDetailLogic.queryOrg(q);
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
    
    @RequestMapping("/queryDept")
    @ResponseBody
    public Object queryDept(@Param("rows") int rows,@Param("page") int page,@Param("q") String q) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = xdAchieveDetailLogic.queryDept(q);
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
    
    
    @RequestMapping("/queryTeam")
    @ResponseBody
    public Object queryByqTeam(@Param("rows") int rows,@Param("page") int page,@Param("q") String q) throws Exception {
    	JSONObject obj =new JSONObject();
        PageHelper.startPage(page, rows);
        Grid<Map<String, Object>> grid=new Grid<>();
        List<Map<String,Object>> lists = xdAchieveDetailLogic.queryTeam(q);
        PageInfo<Map<String, Object>> pageInfo=new PageInfo<>(lists);
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());
        obj.put("rows", grid.getData());
        obj.put("total", grid.getCount());
    	return obj;
    }
    
}
