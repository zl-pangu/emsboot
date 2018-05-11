package com.zhph.commons;

import java.io.File;

/**
 * @author Jianglinghao
 * @ClassName Constant
 * @Description TODO 常量类
 * @date 2017年8月24日
 */
public class Constant {
    // ########################################################
    // ### 常量定义
    // ########################################################
    /**
     * 导出文件的路径.
     */
    public static final String EXPORT_FILE_PATH = File.separator + "opt" + File.separator + "salary" + File.separator + "downLoad";
    /**
     * UTF-8  编码
     */
    public static final String UTF_ENCODING = "UTF-8";
    /**
     * hdfs薪资文件上传下载固定地址
     */
    public static final String HDFS_OPT_PATH = "/salary/opt/upload/empTable";
    /**
     * 文件上传下载文件夹打头
     */
    public static final String ZHPHHQ = "ZHPHHQ";
    public static final String ZHPHXJ = "ZHPHXJ";
    public static final String ZHXD = "ZHXD";
    public static final String EMPIMGPOTO = "empImgPoto";

    /**
     * 反斜杠.
     */
    public static final String BACK_SLANT = "/";
    /**
     * 启用 禁用
     */
    public static final String ENABLE = "1";
    public static final String DISABLE = "0";
    /**
     * json msg code
     */
    public static final String JSON_MSG_KEY = "msg";
    public static final String JSON_MSG = "";
    public static final String JSON_CODE_KEY = "code";
    public static final String JSON_SUCCESS = "200";
    public static final String JSON_FAIL = "500";
    /**
     * 下拉框展示
     */
    public static final Integer CODE_LIST = 1;
    /**
     * 数据表格展示
     */
    public static final Integer CODE_LIST_TPL = 2;
    /**
     * 职级类别展示
     */
    public static final Integer RANK_CODE_LIST = 3;
    /**
     * split分隔符点
     */
    public static final String SPOT = "\\.";
    // 总部员工编码序列
    public static final String SEQ_HQCLCF_ZBEMPNO = "SEQ_HQCLCF_ZBEMPNO";
    // 消分员工编码序列
    public static final String SEQ_HQCLCF_XFEMPNO = "SEQ_HQCLCF_XFEMPNO";
    // 信贷员工编码序列
    public static final String SEQ_HQCLCF_XDEMPNO = "SEQ_HQCLCF_XDEMPNO";
    // ########################################################
    // ### 字典表 码值
    // ########################################################
    /**
     * 系统类型
     */
    public static final String SYSTEM_TYPE = "system_type";
    /**
     * 业务条线
     */
    public static final String BUSINESS_LINE = "business_line";
    /**
     * 异动类型
     */
    public static final String TRANSFER_TYPE = "transfer_type";
    /**
     * 信贷部门类别
     */
    public static final String CL_DEPT_TYPE = "cl_dept_type";
    /**
     * 消分部门类别
     */
    public static final String XF_DEPT_TYPE = "xf_dept_type";
    /**
     * 离职淘汰原因
     */
    public static final String DIMISSION_TYPE = "dimission_type";
    /**
     * 职级类别
     */
    public static final String RANK_TYPE = "rank_leave_type";
    /**
     * 员工审批状态
     */
    public static final String STATUS_APP = "status_app";
    /**
     * 员工在职状态
     */
    public static final String JOB_STATUS = "job_status";
    /**
     * 主体类型
     */
    public static final String EMP_SUBJECT = "emp_subject";
    /**
     * 证件类型
     */
    public static final String ID_TYPE = "id_type";
    /**
     * 员工类型
     */
    public static final String EMP_TYPE = "emp_type";
    /**
     * 婚姻状况
     */
    public static final String MARRIAGE = "marriage";
    /**
     * 户籍性质
     */
    public static final String NATIVE_TYPE = "native_type";
    /**
     * 民族
     */
    public static final String NATION = "nation";
    /**
     * 学历
     */
    public static final String EDU = "edu";
    /**
     * 总部城市类别
     */
    public static final String CITY_LEVEL_HQ = "city_level_hq";
    /**
     * 信贷城市类别
     */
    public static final String CITY_LEVEL_XD = "city_level_xd";
    /**
     * 消分城市类别
     */
    public static final String CITY_LEVEL_XJ = "city_level_xj";
    /**
     * 性别
     */
    public static final String SEX = "sex";
    /**
     * 教育方式
     */
    public static final String EDU_TYPE = "edu_type";

    /**
     * 紧急联系人关系
     */
    public static final String URGENCY_RELATION = "urgency_relation";

    /**
     * 批准结果
     */
    public static final String APP_STATUS = "app_status";
    /**
     * 离职淘汰状态
     */
    public static final String STATUS = "status";

    /**
     * 灰黑类型
     */
    public static final String ASH_BLACK_YPE="ash_black_type";

    /**
     * 消金-请假类型
     */
    public static final String LEAVE_TYPE = "leave_type";

    public static final String SYS_CALENDAR_REFRESH_TYPE_MONTH = "month";// 日历表数据更新方式：按月更新
    public static final String SYS_CALENDAR_REFRESH_TYPE_YEAR = "year";// 日历表数据更新方式：按年更新
    public static final String SYS_CALENDAR_REFRESH_TYPE_TIME_INTERVAL = "interval";// 日历表数据更新方式：时间间隔

    public static final String SYS_CALENDAR_REFRESH_IF_LAST_WORKDAY_YES = "1";// 是否为当月最后一个工作日：是
    public static final String SYS_CALENDAR_REFRESH_IF_LAST_WORKDAY_NO = "0";// 是否为当月最后一个工作日：否
    public static final String SYS_CALENDAR_REFRESH_IF_WORKDAY_YES = "0";// 是否为工作日：是
    public static final String SYS_CALENDAR_REFRESH_IF_WORKDAY_NO = "1";// 是否为工作日：否(休息日)

 
    /**
     * 总部文件类型
     */
    public static final String ZB_FILE = "zb_file";
    /**
     * 消分文件类型
     */
    public static final String XF_FILE = "xf_file";
    /**
     * 信贷文件类
     */
    public static final String XD_FILE = "xd_file";
    
    /**
     * 离职淘汰上传附件
     */
    public static final String LEAVE_FILE = "leave_file";

    /**
     * 员工状态码
     */
    public static final Integer JOB_ON = 1;// 在职
    public static final Integer JOB_OFF = 2;// 离职
    public static final Integer JOB_TXTZ = 3;// 停薪停职
    public static final Integer JOB_TXLZ = 4;// 停薪留职

    /**
     * 员工离职申请审批状态
     */
    public static final String LEAVE_APP_WAITE = "0";//未审核
    public static final String LEAVE_APP_PASS = "1";//通过
    public static final String LEAVE_APP_REFUSE = "2";//拒绝 or 同意留职


    /**
     * 是否显示下级部门
     */
    public static final String ON_SHOW_CHILD_DEPT = "1";//显示
    public static final String OFF_SHOW_CHILD_DEPT = "0";//不显示


    /**
     * 主体变更审核状态
     */
    public static final String EMPCHANGE_STATUS = "ztbg_shzt";

    public static final String NOMAL_APPROVE_STAT_NOTCOMMIT = "1";//一般审核状态：未提交
    public static final String NOMAL_APPROVE_STAT_WAITING_APPROVE = "2";//一般审核状态：待审核
    public static final String NOMAL_APPROVE_STAT_PASS = "3";//一般审核状态：通过
    public static final String NOMAL_APPROVE_STAT_REFUSE = "4";//一般审核状态：拒绝

    /**
     * 获取一般审核状态的中文描述
     *
     * @param status
     * @return
     */
    public static final String getNomalApproveStatusDesc(String status) {
        if (NOMAL_APPROVE_STAT_NOTCOMMIT.equals(status)) {
            return "未提交";
        } else if (NOMAL_APPROVE_STAT_WAITING_APPROVE.equals(status)) {
            return "待审核";
        } else if (NOMAL_APPROVE_STAT_PASS.equals(status)) {
            return "通过";
        } else if (NOMAL_APPROVE_STAT_REFUSE.equals(status)) {
            return "拒绝";
        } else {
            return "";
        }
    }

    public static final Integer BUSINESS_LINE_HQ = new Integer(1);// 业务条线: 总部
    public static final Integer BUSINESS_LINE_CF = new Integer(2);// 业务条线：消分
    public static final Integer BUSINESS_LINE_CL = new Integer(3);// 业务条线：信贷

    /**
     * 消分员工状态变更
     */
    public static final String CF_EMPSTATUS_LIST = "xf_status_list";//状态
    public static final String XJ_EMPSTATUS_LIST = "cf_status_list";//消金员工config配置状态P_CODE
    public static final String CF_EMPSTATUS_LIST_ISEND = "cf_status_isend";//是否变更

    public static final String DEPT_TYPE_LEVEL1 = "1";//部门类型：分部--大区--1
    public static final String DEPT_TYPE_LEVEL2 = "2";//部门类型：区域--分公司--2
    public static final String DEPT_TYPE_LEVEL3 = "3";//部门类型：营业部--营业部--3
    public static final String DEPT_TYPE_LEVEL4 = "4";//部门类型：部门--4
    public static final String DEPT_TYPE_LEVEL5 = "5";//部门类型：团队--5


    /**
     * 导入exl文件类型
     */
    public static final String IMPORT_FILE_TYPE_XLSX = "xlsx";
    public static final String IMPORT_FILE_TYPE_XLS = "xls";
}