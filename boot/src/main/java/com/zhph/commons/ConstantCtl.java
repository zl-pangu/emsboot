package com.zhph.commons;

/**
 * Created by zhouliang on 2017/11/24.
 */
public class ConstantCtl {
	// ########################################################
	public static final String INIT = "/init";
	public static final String LIST = "/list";
	public static final String EDIT = "/edit";
	public static final String APP = "/app";// 审批
	public static final String EDITINIT = "/editInit";
	public static final String DETINIT = "/detInit";// 详情初始
	public static final String ADD = "/add";
	public static final String DEL = "/del";
	public static final String SAVE = "/save";
	public static final String QUERY = "/query";
	public static final String QUERYALL = "/queryAll";
	public static final String EXPORTEXL = "/exportExl";
	public static final String IMPORTEXL = "/importExl";
	public static final String TREE = "/tree";
	public static final String DETAIL = "/detail";
	public static final String ADDEMP = "/addEmp";
	// ########################################################
	// ### 工作地设置
	// ########################################################
	public static final String WORKPLACE_CTLREQM = "/sys/workplaceset";
	public static final String PROVINCELIST_KEY = "provinceList";
	public static final String SYSWORKPLACESET_KEY = "sysWorkplaceset";
	public static final String INITVIEW_NAME = "/pages/sys/workplaceset/workplacesetIndex";
	// ########################################################
	// ### 部门管理
	// ########################################################
	public static final String HQCLCFDEPT_CTLREQM = "/hqclcf/hqclcfdept";
	public static final String HQCLCFDEPT_INITVIEW_NAME = "/pages/hqclcf/hqclcfdept/hqclcfdeptIndex";
	// ########################################################
	// ### 银行卡设置
	// ########################################################
	public static final String BANK_CTLREQM = "/sys/bank";
	public static final String INITVIEW_BANKNAME = "/pages/sys/bank/sys_bank_init";
	// ########################################################
	// ### 离职淘汰
	// ########################################################
	public static final String HQCLCF_LEAVE = "/hqclcf/hqclcfLeave";
	public static final String INITVIEW_HQCLCF = "/pages/hqclcf/HqclcfLeave/hqclcf_Leave_init";
	public static final String HQCLC_LEAVE_ADD = "/pages/hqclcf/HqclcfLeave/hqclcf_Leave_add";
	public static final String HQCLC_LEAVE_EDIT = "/pages/hqclcf/HqclcfLeave/hqclcf_Leave_edit";
	public static final String HQCLC_LEAVE_DETAIL = "/pages/hqclcf/HqclcfLeave/hqclcf_Leave_detail";
	public static final String HQCLC_LEAVE_APP = "/pages/hqclcf/HqclcfLeave/hqclcf_Leave_app";
	// ########################################################
	// ### 职级管理
	// ########################################################
	public static final String HQCLC_RANK = "/hqclcf/hqclcfRank";
	public static final String HQCLC_RANK_INITVIEW_NAME = "/pages/hqclcf/rank/hqclcf_rank_list";
	// ########################################################
	// ### 职务管理
	// ########################################################
	public static final String HQCLC_BUSINESS = "/hqclcf/hqclcfBusiness";
	public static final String HQCLC_BUSINESS_INITVIEW_NAME = "/pages/hqclcf/business/hqclcf_business_index";
	// ########################################################
	// ### 离职转在职
	// ########################################################
	public static final String HQCLCF_JOBTRANS = "/hqclcf/hqclcfJobTransfer";
	public static final String INITVIEW_JOBTRANS = "/pages/hqclcf/hqclcfJobTransfer/hqclcf_JobTransfer_init";
	public static final String INITVIEW_JOBTRANS_ADD = "/pages/hqclcf/hqclcfJobTransfer/hqclcf_JobTransfer_add";
	public static final String INITVIEW_JOBTRANS_EDIT = "/pages/hqclcf/hqclcfJobTransfer/hqclcf_JobTransfer_edit";
	public static final String INITVIEW_JOBTRANS_DETAIL = "/pages/hqclcf/hqclcfJobTransfer/hqclcf_JobTransfer_detail";
	// ########################################################
	// ### 人员异动管理
	// ########################################################
	public static final String HQCLC_PERSON_TRANSFER = "/hqclcf/hqclcfPersonTransfer";
	public static final String HQCLC_PERSON_TRANSFER_INITVIEW_NAME = "/pages/hqclcf/personTransfer/hqclcf_transfer_index";
	public static final String HQCLC_PERSON_TRANSFER_ADD_ADDRESS = "/pages/hqclcf/personTransfer/hqclcf_transfer_add";
	public static final String HQCLC_PERSON_TRANSFER_EDIT_ADDRESS = "/pages/hqclcf/personTransfer/hqclcf_transfer_edit";
	// ########################################################
	// ### 员工信息管理
	// ########################################################
	public static final String HQCLC_EMP = "/hqclcf/hqclcfEmp";
	public static final String INITVIEW_EMP = "/pages/hqclcf/hqclcfEmp/heclcf_emp_init";
	public static final String INITVIEW_EMP_EDIT = "/pages/hqclcf/hqclcfEmp/hqclcf_emp_edit";
	public static final String INITVIEW_EMP_DET = "/pages/hqclcf/hqclcfEmp/hqclcf_emp_edt";
	public static final String HQCLC_PERSON_TRANSFER_DEPT_POST_GET = "/deptAndPostByDeptNo";
	public static final String HQCLC_PERSON_TRANSFER_BUSINESS = "/queryBusinessByPosCode";
	// ########################################################
	// ### 岗位信息管理
	// ########################################################
	public static final String HQCLC_POST = "/hqclcf/hqclcfPost";
	public static final String HQCLCFPOST_ZTREE_NAME = "/pages/hqclcf/post/hqclcf_post_index";
	public static final String HQCLCFPOST_ADD_ADDRESS = "/pages/hqclcf/post/hqclcf_post_add";
	public static final String HQCLCFPOST_EDIT_ADDRESS = "/pages/hqclcf/post/hqclcf_post_edit";
	public static final String HQCLCFPOST_STATUS_SWITCH = "/status";

	// ########################################################
	// ### 节假日管理
	// ########################################################
	public static final String HOLIDAY_CTLREQM = "/sys/holiday";
	public static final String HOLIDAY_MAIN = "/pages/sys/holiday/sys_holiday_main";
	public static final String HOLIDAY_ADD = "/pages/sys/holiday/sys_holiday_add";
	public static final String HOLIDAY_EDIT = "/pages/sys/holiday/sys_holiday_edit";

	// ########################################################
	// ### 调休工作日设置
	// ########################################################
	public static final String CF_OFFLATER_CTLREQM = "/cf/offlaterset";
	public static final String CF_OFFLATER_MAIN = "/pages/cf/offlaterset/cf_offlaterset_main";
	public static final String CF_OFFLATER_ADD = "/pages/cf/offlaterset/cf_offlaterset_add";
	public static final String CF_OFFLATER_EDIT = "/pages/cf/offlaterset/cf_offlaterset_edit";
	// ########################################################
	// ### 消金考勤统计
	// ########################################################
	public static final String HQCLCF_ATTENDANCE = "/hqclcf/attendance";
	public static final String ATTENDANCE_ZTREE_NAME = "/pages/hqclcf/Post/hqclcf_post_index";
	public static final String ATTENDANCE_INIT_ADDRESS = "/pages/cf.attendance/cf_attendance_index";
	public static final String ATTENDANCE_EDIT_ADDRESS = "/pages/hqclcf/post/hqclcf_post_edit";
	public static final String ATTENDANCE_STATUS_SWITCH = "/status";

	// ########################################################
	// ### 消金考勤统计
	// ########################################################
	public static final String CF_VACATEMANAGE = "/cf/vacatemanage";
	public static final String VACATEMANAGE_INIT_ADDRESS = "pages/cf/vacatemanage/vacatemanage_list";
	public static final String VACATEMANAGE_ADD_ADDRESS = "pages/cf/vacatemanage/vacatemanage_add";
	public static final String VACATEMANAGE_EDIT_ADDRESS = "pages/cf/vacatemanage/vacatemanage_edit";

	// ########################################################
	// ### 员工主体变更
	// ########################################################
	public static final String EMPSUBJECT_CHANGE = "/hqclcf/empsubject";
	public static final String EMPSUBJECT_CHANGE_MAIN = "pages/hqclcf/empsubject/hqclcf_empsubject_change_main";
	public static final String EMPSUBJECT_CHANGE_ADD = "pages/hqclcf/empsubject/hqclcf_empsubject_change_add";
	public static final String EMPSUBJECT_CHANGE_EDIT = "pages/hqclcf/empsubject/hqclcf_empsubject_change_edit";
	public static final String EMPSUBJECT_CHANGE_INFO = "pages/hqclcf/empsubject/hqclcf_empsubject_change_info";
	public static final String EMPSUBJECT_CHANGE_AUDIT = "pages/hqclcf/empsubject/hqclcf_empsubject_change_audit";

	// ########################################################
	// ### 信贷分公司销售任务
	// ########################################################
	public static final String CL_ORGTASK = "/cl/clOrgTask";
	public static final String INITVIEW_ORGTASK = "/pages/cl/clOrgTask/cl_org_task_init";

	// ########################################################
	// ### 消分员工状态变更
	// ########################################################
	public static final String CF_SATATUS = "/cf/empStatus";
	public static final String CF_INDEX_PAGE = "/pages/cf/empStatus/cf_empstatus_index";
	public static final String CF_INDEX_PAGE_ADD = "/pages/cf/empStatus/cf_empstatus_add";
	public static final String CF_INDEX_PAGE_EDIT = "/pages/cf/empStatus/cf_empstatus_edit";
	public static final String CF_BUILD_END_TIME = "/pages/cf/empStatus/cf_empstatus_build_end_time";

	// ########################################################
	// ### 首逾明细
	// ########################################################
	public static final String CL_FIRSTOVER = "/cl/ClFirstBeOverdue";
	public static final String INITVIEW_INITVIEW_ORGTASK = "/pages/cl/clFirstBeOverdue/cl_first_be_overdue_init";
	// ########################################################
	// ### 灰黑名单
	// ########################################################
	public static final String Cl_ASH_BLACK = "/cl/ClAshBlackMenu";
	public static final String Cl_ASH_BLACK_PAGE = "/pages/cl/clAshBlackMenu/cl_ashblack_init";

	// ########################################################
	// ### 近六个月M2+（含M2）
	// ########################################################
	public static final String CL_SIXMONTHM2 = "/cl/clSixMonthM2";
	public static final String CL_SIXMONTHM2_MAIN = "/pages/cl/clsixmonthM2/cl_sixmonthM2_main";

}