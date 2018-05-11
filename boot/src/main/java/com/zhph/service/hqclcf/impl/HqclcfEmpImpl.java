package com.zhph.service.hqclcf.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.mapper.cf.CfEmpStatusMapper;
import com.zhph.mapper.common.ZhphObjectMapper;
import com.zhph.mapper.hqclcf.HqclcfBusinessMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpFileMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.hqclcf.HqclcfJobTransferMapper;
import com.zhph.mapper.hqclcf.HqclcfLeaveMapper;
import com.zhph.mapper.hqclcf.HqclcfPersonTransferMapper;
import com.zhph.mapper.hqclcf.HqclcfPostMapper;
import com.zhph.mapper.hqclcf.HqclcfRankMapper;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.mapper.sys.SysWorkplacesetMapper;
import com.zhph.model.cf.CfEmpStatus;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfJobTransfer;
import com.zhph.model.hqclcf.HqclcfLeave;
import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.service.common.BaseService;
import com.zhph.service.common.FileUploadService;
import com.zhph.service.hqclcf.HqclcfBusinessService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.service.hqclcf.HqclcfEmpService;
import com.zhph.service.hqclcf.HqclcfPostService;
import com.zhph.service.hqclcf.HqclcfRankService;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.util.CommonUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.ExcelUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import com.zhph.util.StringUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class HqclcfEmpImpl implements HqclcfEmpService {

	public static final Logger logger = LogManager.getLogger(HqclcfEmpImpl.class);

	@Resource
	private BaseService baseService;

	@Autowired
	private HqclcfEmpMapper hqclcfEmpMapper;

	@Autowired
	private HqclcfEmpFileMapper hqclcfEmpFileMapper;

	@Autowired
	private SysConfigTypeService sysConfigTypeService;

	@Autowired
	private HqclcfDeptMapper hqclcfDeptMapper;

	@Autowired
	private HqclcfPostMapper hqclcfPostMapper;

	@Autowired
	private HqclcfBusinessMapper hqclcfBusinessMapper;

	@Autowired
	private HqclcfRankMapper hqclcfRankMapper;

	@Autowired
	private SysWorkplacesetMapper sysWorkplacesetMapper;

	@Autowired
	private ZhphObjectMapper zhphObjectMapper;

	@Autowired
	private HqclcfPersonTransferMapper hqclcfPersonTransferMapper;

	@Resource
	private HqclcfDeptService hqclcfDeptService;

	@Resource
	private CfEmpStatusMapper cfEmpStatusMapper;

	@Resource
	private HqclcfPostService hqclcfPostService;

	@Resource
	private HqclcfBusinessService hqclcfBusinessService;

	@Resource
	private HqclcfRankService hqclcfRankService;

	@Resource
	private SysConfigTypeMapper sysConfigTypeMapper;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private HqclcfLeaveMapper hqclcfLeaveMapper;

	@Autowired
	private HqclcfJobTransferMapper hqclcfJobTransferMapper;

	@Override
	public Grid<HqclcfEmp> queryPageInfo(PageBean pageBean, HqclcfEmp params) throws Exception {
		PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());

		if (params.getDeptNo() != null && params.getDeptNo() != "null" && params.getDeptNo() != "") {
			Long deptId = Long.valueOf(params.getDeptNo());
			HqclcfDept dept = hqclcfDeptService.queryDept(deptId);
			params.setDeptNo(dept.getDeptCode());
			params.setDeptHid(deptId);
		}

		// 添加数据权限
		params.setUserId(CommonUtil.getOnlineUser().getUserId());

		List<HqclcfEmp> list = hqclcfEmpMapper.queryAllEmp(params);
		PageInfo<HqclcfEmp> pageInfo = new PageInfo<>(list);
		Grid<HqclcfEmp> grid = new Grid<>();
		grid.setCount(pageInfo.getTotal());
		grid.setData(pageInfo.getList());
		return grid;
	}

	@Override
	public HqclcfEmp gotoEditById(String Id) throws Exception {
		HqclcfEmp hqclcfEmp = new HqclcfEmp();
		try {
			List<HqclcfEmp> ls = hqclcfEmpMapper.queryDataById(Id);
			if (ls.size() > 0) {
				hqclcfEmp = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hqclcfEmp;
	}

	@Override
	public List<HqclcfEmpFile> getFilesByEmpNo(String empno) throws Exception {
		List<HqclcfEmpFile> ls = null;
		try {
			ls = hqclcfEmpFileMapper.getFilesByEmpNo(empno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls;

	}

	@Override
	public JSONObject saveData(HqclcfEmp hqclcfEmp, MultipartHttpServletRequest mhsRequest) throws Exception {

		JSONObject obj = new JSONObject();
		try {
			// 户籍地址
			StringBuffer nativePlace = new StringBuffer();
			nativePlace.append(null != hqclcfEmp.getHjProvince() ? hqclcfEmp.getHjProvince() : "");
			nativePlace.append(null != hqclcfEmp.getHjCity() ? hqclcfEmp.getHjCity() : "");
			nativePlace.append(null != hqclcfEmp.getHjArea() ? hqclcfEmp.getHjArea() : "");
			nativePlace.append(null != hqclcfEmp.getHjxjdz() ? hqclcfEmp.getHjxjdz() : "");
			hqclcfEmp.setNativePlace(nativePlace.toString());

			// 现居地址
			StringBuffer addr = new StringBuffer();
			addr.append(null != hqclcfEmp.getXjProvince() ? hqclcfEmp.getXjProvince() : "");
			addr.append(null != hqclcfEmp.getXjCity() ? hqclcfEmp.getXjCity() : "");
			addr.append(null != hqclcfEmp.getXjArea() ? hqclcfEmp.getHjArea() : "");
			addr.append(null != hqclcfEmp.getXjxxdz() ? hqclcfEmp.getXjxxdz() : "");
			hqclcfEmp.setAddr(addr.toString());

			// 银行卡开户行地址
			StringBuffer openBankOrg = new StringBuffer();
			openBankOrg.append(hqclcfEmp.getBankProvince());
			openBankOrg.append(hqclcfEmp.getBankCity());
			openBankOrg.append(hqclcfEmp.getBankxxdj());
			hqclcfEmp.setOpenBankOrg(openBankOrg.toString());

			// 部门code
			Long deptId = hqclcfEmp.getDeptHid();
			HqclcfDept dept = hqclcfDeptService.queryDept(deptId);
			hqclcfEmp.setDeptNo(dept.getDeptCode());

			// 岗位Code
			Long postId = hqclcfEmp.getPostHid();
			HqclcfPost post = hqclcfPostMapper.queryByPostId(postId, null);
			hqclcfEmp.setPost(post.getPostNo());

			// 隐藏的bl
			hqclcfEmp.setBusinessLine(hqclcfEmp.getBusinessLineHid());

			// 备注
			Integer businessLine = hqclcfEmp.getBusinessLine();
			switch (businessLine) {
			case 1:
				hqclcfEmp.setComments(hqclcfEmp.getZbbz());
				break;
			case 2:
				hqclcfEmp.setComments(hqclcfEmp.getXjbz());
				break;
			case 3:
				hqclcfEmp.setComments(hqclcfEmp.getXdbz());
				break;
			}

			// 附件上传
			fileUploadService.uploadFile(mhsRequest, hqclcfEmp);

			// 添加修改信息
			hqclcfEmp.setUpdatetime(new Date());
			hqclcfEmp.setUpdator(CommonUtil.getOnlineFullName());

			// 修改员工
			hqclcfEmpMapper.updateById(hqclcfEmp);

			obj.put("success", true);
			obj.put("msg", "提交成功！");
		} catch (Exception e) {
			obj.put("success", false);
			obj.put("msg", "提交失败！");
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public void exportExl(HqclcfEmp data, HttpServletRequest req, HttpServletResponse res) {

		String exportModule = req.getParameter("exportModule");

		try {

			// 部门转化
			if (data.getDeptNo() != null && data.getDeptNo() != "null" && data.getDeptNo() != "") {
				Long deptId = Long.valueOf(data.getDeptNo());
				HqclcfDept dept = hqclcfDeptService.queryDept(deptId);
				data.setDeptNo(dept.getDeptCode());
				data.setDeptHid(deptId);

			}
			// 添加数据权限
			data.setUserId(CommonUtil.getOnlineUser().getUserId());
			List<HqclcfEmp> list = hqclcfEmpMapper.queryAllEmpForExport(data);

			// 数据字典Map
			HashMap<String, String> sct_map = new HashMap<String, String>();
			List<SysConfigType> sct_list = sysConfigTypeMapper.getSysConfigTypeList();
			if (sct_list.size() > 0) {
				for (SysConfigType sct : sct_list) {
					if (sct.getpSysCode() != "" && sct.getpSysCode() != "null" && sct.getpSysCode() != null) {
						sct_map.put(sct.getpSysCode() + "_" + sct.getSysValue().toString(), sct.getSysName());
					}
				}
			}

			if ("1".equals(exportModule)) {
				// 全员
				String[] colTitleAry = { "序号", "业务线", "一级承担部门", "二级承担部门", "三级承担部门", "四级承担部门", "五级承担部门", "六级承担部门", "七级承担部门", "八级承担部门", "九级承担部门", "办公地点", "城市级别", "工号", "姓名", "职级", "职务", "岗位", "入职日期", "转正日期", "离职日期", "离职原因", "状态", "身份证号码", "联系方式", "性别", "学历", "民族", "婚姻状况", "户籍地址", "现居地址", "紧急联系人", "紧急联系人关系", "紧急联系人电话", "备注" };
				String[][] array = new String[list.size()][colTitleAry.length];
				short[] colWidthAry = { 80, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180 };
				for (int i = 0; i < list.size(); i++) {
					HqclcfEmp hqclcfEmp = list.get(i);
					array[i][0] = i + 1 + ""; // 序号
					array[i][1] = sct_map.get(Constant.BUSINESS_LINE + "_" + hqclcfEmp.getBusinessLine()); // 业务线
					array[i][2] = getCdDeptName("1", hqclcfEmp.getCd_name());// 一级承担部门
					array[i][3] = getCdDeptName("2", hqclcfEmp.getCd_name());// 二级承担部门
					array[i][4] = getCdDeptName("3", hqclcfEmp.getCd_name());// 三级承担部门
					array[i][5] = getCdDeptName("4", hqclcfEmp.getCd_name());// 四级承担部门
					array[i][6] = getCdDeptName("5", hqclcfEmp.getCd_name());// 六级承担部门
					array[i][7] = getCdDeptName("6", hqclcfEmp.getCd_name());// 七级承担部门
					array[i][8] = getCdDeptName("7", hqclcfEmp.getCd_name());// 八级承担部门
					array[i][9] = getCdDeptName("8", hqclcfEmp.getCd_name());// 九级承担部门
					array[i][10] = getCdDeptName("9", hqclcfEmp.getCd_name());// 九级承担部门
					array[i][11] = hqclcfEmp.getWorkOrgNo_name();// 办公地点
					if (hqclcfEmp.getBusinessLine() != null) {
						String bl = hqclcfEmp.getBusinessLine().toString();
						// 城市级别
						if ("1".equals(bl)) {
							array[i][12] = sct_map.get(Constant.CITY_LEVEL_HQ + "_" + hqclcfEmp.getCityLevel());
						} else if ("2".equals(bl)) {
							array[i][12] = sct_map.get(Constant.CITY_LEVEL_XJ + "_" + hqclcfEmp.getCityLevel());
						} else if ("3".equals(bl)) {
							array[i][12] = sct_map.get(Constant.CITY_LEVEL_XD + "_" + hqclcfEmp.getCityLevel());
						} else {
							array[i][12] = null;
						}
					} else {
						array[i][12] = null;
					}
					array[i][13] = hqclcfEmp.getEmpNo();// 工号
					array[i][14] = hqclcfEmp.getEmpName();// 姓名
					array[i][15] = hqclcfEmp.getRankName();// 职级
					array[i][16] = hqclcfEmp.getPosName();// 职务
					array[i][17] = hqclcfEmp.getPostName();// 岗位
					array[i][18] = DateUtil.parseDateFormat(hqclcfEmp.getEnterDate(), "yyyy-MM-dd");// 入职日期
					array[i][19] = DateUtil.parseDateFormat(hqclcfEmp.getTfDate(), "yyyy-MM-dd");// 转正日期
					array[i][20] = DateUtil.parseDateFormat(hqclcfEmp.getLeaveDate(), "yyyy-MM-dd");// 离职日期
					array[i][21] = null;// 离职原因
					array[i][22] = sct_map.get(Constant.JOB_STATUS + "_" + hqclcfEmp.getStatus());// 状态
					array[i][23] = hqclcfEmp.getIdCard();// 身份证号码
					array[i][24] = hqclcfEmp.getMobilePhone();// 联系方式
					array[i][25] = sct_map.get(Constant.SEX + "_" + hqclcfEmp.getSex()); // 性别
					array[i][26] = sct_map.get(Constant.EDU + "_" + hqclcfEmp.getEduType());// 学历
					array[i][27] = sct_map.get(Constant.NATION + "_" + hqclcfEmp.getSex());// 民族
					array[i][28] = sct_map.get(Constant.MARRIAGE + "_" + hqclcfEmp.getSex());// 婚姻状况
					array[i][29] = hqclcfEmp.getNativePlace();// 户籍地址
					array[i][30] = hqclcfEmp.getAddr();// 现居地址
					array[i][31] = hqclcfEmp.getUrgency();// 紧急联系人
					array[i][32] = sct_map.get(Constant.URGENCY_RELATION + "_" + hqclcfEmp.getSex());// 紧急联系人关系
					array[i][33] = hqclcfEmp.getUrgencyPhone();// 紧急联系人电话
					array[i][34] = hqclcfEmp.getComments();// 备注
				}
				ExcelUtil excelUtil = new ExcelUtil();
				excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "员工信息管理（花名册的内容-全员）", 1);

			} else if ("2".equals(exportModule)) {
				// 信贷
				String[] colTitleAry = { "序号", "区域", "营业部", "部门", "岗位", "工号", "姓名", "身份证号码", "银行卡名称", "银行卡号", "开户支行", "入职日期", "离职日期", "联系电话", "状态", "备注" };
				String[][] array = new String[list.size()][colTitleAry.length];
				short[] colWidthAry = { 80, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180 };
				for (int i = 0; i < list.size(); i++) {
					HqclcfEmp hqclcfEmp = list.get(i);
					array[i][0] = i + 1 + ""; // 序号
					array[i][1] = getDeptParentByDeptNo("2", hqclcfEmp.getP_name());// 区域
					array[i][2] = getDeptParentByDeptNo("3", hqclcfEmp.getP_name());// 营业部
					array[i][3] = hqclcfEmp.getDeptName();// 部门
					array[i][4] = hqclcfEmp.getPostName();// 岗位
					array[i][5] = hqclcfEmp.getEmpNo();// 工号
					array[i][6] = hqclcfEmp.getEmpName();// 姓名
					array[i][7] = hqclcfEmp.getIdCard();// 身份证号码
					array[i][8] = hqclcfEmp.getPfBankCode_name();// 银行卡名称
					array[i][9] = hqclcfEmp.getPfBankNo();// 银行卡号
					array[i][10] = hqclcfEmp.getOpenBankOrg();// 开户支行
					array[i][11] = DateUtil.parseDateFormat(hqclcfEmp.getEnterDate(), "yyyy-MM-dd");// 入职日期
					array[i][12] = DateUtil.parseDateFormat(hqclcfEmp.getLeaveDate(), "yyyy-MM-dd");// 离职日期
					array[i][13] = hqclcfEmp.getMobilePhone();// 联系电话
					array[i][14] = sct_map.get(Constant.JOB_STATUS + "_" + hqclcfEmp.getStatus());// 状态
					array[i][15] = hqclcfEmp.getComments();// 备注
				}
				ExcelUtil excelUtil = new ExcelUtil();
				excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "员工信息管理（员工关系组-普惠）", 1);

			} else if ("3".equals(exportModule)) {
				// 消分
				String[] colTitleAry = { "序号", "大区", "分公司", "营业部", "团队", "工号", "姓名", "性别", "职级", "职务", "岗位", "部门", "入职日期", "转正日期", "离职日期", "离职原因", "状态", "银行卡名称", "银行卡号", "开户支行", "证件类型", "证件号码", "联系电话", "学历", "备注" };
				String[][] array = new String[list.size()][colTitleAry.length];
				short[] colWidthAry = { 80, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180 };
				for (int i = 0; i < list.size(); i++) {
					HqclcfEmp hqclcfEmp = list.get(i);
					array[i][0] = i + 1 + ""; // 序号
					array[i][1] = getDeptParentByDeptNo("1", hqclcfEmp.getP_name());// 大区
					array[i][2] = getDeptParentByDeptNo("2", hqclcfEmp.getP_name());// 分公司
					array[i][3] = getDeptParentByDeptNo("3", hqclcfEmp.getP_name());// 营业部
					array[i][4] = getDeptParentByDeptNo("5", hqclcfEmp.getP_name());// 团队
					array[i][5] = hqclcfEmp.getEmpNo();// 工号
					array[i][6] = hqclcfEmp.getEmpName();// 姓名
					array[i][7] = sct_map.get(Constant.SEX + "_" + hqclcfEmp.getSex());// 姓别
					array[i][8] = hqclcfEmp.getRankName();// 职级
					array[i][9] = hqclcfEmp.getPosName();// 职务
					array[i][10] = hqclcfEmp.getPostName();// 岗位
					array[i][11] = hqclcfEmp.getDeptName();// 部门
					array[i][12] = DateUtil.parseDateFormat(hqclcfEmp.getEnterDate(), "yyyy-MM-dd");// 入职日期
					array[i][13] = DateUtil.parseDateFormat(hqclcfEmp.getTfDate(), "yyyy-MM-dd");// 转正日期
					array[i][14] = DateUtil.parseDateFormat(hqclcfEmp.getLeaveDate(), "yyyy-MM-dd");// 离职日期
					array[i][15] = null;// 离职原因
					array[i][16] = sct_map.get(Constant.JOB_STATUS + "_" + hqclcfEmp.getStatus());// 状态
					array[i][17] = hqclcfEmp.getPfBankCode_name();// 银行卡名称
					array[i][18] = hqclcfEmp.getPfBankNo();// 银行卡号
					array[i][19] = hqclcfEmp.getOpenBankOrg();// 开户支行
					array[i][20] = sct_map.get(Constant.ID_TYPE + "_" + hqclcfEmp.getIdType());// 证件类型
					array[i][21] = hqclcfEmp.getIdCard();// 证件号码
					array[i][22] = hqclcfEmp.getMobilePhone();// 联系电话
					array[i][23] = sct_map.get(Constant.EDU + "_" + hqclcfEmp.getEduType());// 学历
					array[i][24] = hqclcfEmp.getComments();// 备注
				}
				ExcelUtil excelUtil = new ExcelUtil();
				excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "员工信息管理（员工关系组-消分）", 1);

			} else if ("4".equals(exportModule)) {
				String[] colTitleAry = { "序号", "业务线", "一级承担部门", "二级承担部门", "三级承担部门", "四级承担部门", "办公地点", "城市级别", "工号", "姓名", "职级", "职务", "岗位", "入职日期", "转正日期", "离职日期", "离职原因", "状态", "身份证号码", "银行卡名称", "银行卡号", "开户支行", "联系电话", "备注" };
				String[][] array = new String[list.size()][colTitleAry.length];
				short[] colWidthAry = { 80, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180 };
				for (int i = 0; i < list.size(); i++) {
					HqclcfEmp hqclcfEmp = list.get(i);
					array[i][0] = i + 1 + ""; // 序号
					array[i][1] = sct_map.get(Constant.BUSINESS_LINE + "_" + hqclcfEmp.getBusinessLine()); // 业务线
					array[i][2] = getCdDeptName("1", hqclcfEmp.getCd_name());// 一级承担部门
					array[i][3] = getCdDeptName("2", hqclcfEmp.getCd_name());// 二级承担部门
					array[i][4] = getCdDeptName("3", hqclcfEmp.getCd_name());// 三级承担部门
					array[i][5] = getCdDeptName("4", hqclcfEmp.getCd_name());// 四级承担部门
					array[i][6] = hqclcfEmp.getWorkOrgNo_name();// 办公地点
					if (hqclcfEmp.getBusinessLine() != null) {
						String bl = hqclcfEmp.getBusinessLine().toString();
						// 城市级别
						if ("1".equals(bl)) {
							array[i][7] = sct_map.get(Constant.CITY_LEVEL_HQ + "_" + hqclcfEmp.getCityLevel());
						} else if ("2".equals(bl)) {
							array[i][7] = sct_map.get(Constant.CITY_LEVEL_XJ + "_" + hqclcfEmp.getCityLevel());
						} else if ("3".equals(bl)) {
							array[i][7] = sct_map.get(Constant.CITY_LEVEL_XD + "_" + hqclcfEmp.getCityLevel());
						} else {
							array[i][7] = null;
						}
					} else {
						array[i][7] = null;
					}
					array[i][8] = hqclcfEmp.getEmpNo();// 工号
					array[i][9] = hqclcfEmp.getEmpName();// 姓名
					array[i][10] = hqclcfEmp.getRankName();// 职级
					array[i][11] = hqclcfEmp.getPosName();// 职务
					array[i][12] = hqclcfEmp.getPostName();// 岗位
					array[i][13] = DateUtil.parseDateFormat(hqclcfEmp.getEnterDate(), "yyyy-MM-dd");// 入职日期
					array[i][14] = DateUtil.parseDateFormat(hqclcfEmp.getTfDate(), "yyyy-MM-dd");// 转正日期
					array[i][15] = DateUtil.parseDateFormat(hqclcfEmp.getLeaveDate(), "yyyy-MM-dd");// 离职日期
					array[i][16] = null;// 离职原因
					array[i][17] = sct_map.get(Constant.JOB_STATUS + "_" + hqclcfEmp.getStatus());// 状态
					array[i][18] = hqclcfEmp.getIdCard();// 身份证号码
					array[i][19] = hqclcfEmp.getPfBankCode_name();// 银行卡名称
					array[i][20] = hqclcfEmp.getPfBankNo();// 银行卡号
					array[i][21] = hqclcfEmp.getOpenBankOrg();// 开户支行
					array[i][22] = hqclcfEmp.getMobilePhone();// 联系电话
					array[i][23] = hqclcfEmp.getComments();// 备注
				}
				ExcelUtil excelUtil = new ExcelUtil();
				excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "员工信息管理（员工关系组-总部）", 1);

			} else if ("5".equals(exportModule)) {
				String[] colTitleAry = { "序号", "业务线", "一级承担部门", "二级承担部门", "三级承担部门", "四级承担部门", "六级承担部门", "七级承担部门", "八级承担部门", "九级承担部门", "办公地点", "城市级别", "工号", "姓名", "职级", "职务", "岗位", "入职日期", "转正日期", "离职日期", "离职原因", "状态", "身份证号码", "银行卡名称", "银行卡号", "开户支行", "联系电话", "备注" };
				String[][] array = new String[list.size()][colTitleAry.length];
				short[] colWidthAry = { 80, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180 };
				for (int i = 0; i < list.size(); i++) {
					HqclcfEmp hqclcfEmp = list.get(i);
					array[i][0] = i + 1 + ""; // 序号
					array[i][1] = sct_map.get(Constant.BUSINESS_LINE + "_" + hqclcfEmp.getBusinessLine()); // 业务线
					array[i][2] = getCdDeptName("1", hqclcfEmp.getCd_name());// 一级承担部门
					array[i][3] = getCdDeptName("2", hqclcfEmp.getCd_name());// 二级承担部门
					array[i][4] = getCdDeptName("3", hqclcfEmp.getCd_name());// 三级承担部门
					array[i][5] = getCdDeptName("4", hqclcfEmp.getCd_name());// 四级承担部门
					array[i][6] = getCdDeptName("5", hqclcfEmp.getCd_name());// 六级承担部门
					array[i][7] = getCdDeptName("6", hqclcfEmp.getCd_name());// 七级承担部门
					array[i][8] = getCdDeptName("7", hqclcfEmp.getCd_name());// 八级承担部门
					array[i][9] = getCdDeptName("8", hqclcfEmp.getCd_name());// 九级承担部门
					array[i][10] = hqclcfEmp.getWorkOrgNo_name();// 办公地点
					if (hqclcfEmp.getBusinessLine() != null) {
						String bl = hqclcfEmp.getBusinessLine().toString();
						// 城市级别
						if ("1".equals(bl)) {
							array[i][11] = sct_map.get(Constant.CITY_LEVEL_HQ + "_" + hqclcfEmp.getCityLevel());
						} else if ("2".equals(bl)) {
							array[i][11] = sct_map.get(Constant.CITY_LEVEL_XJ + "_" + hqclcfEmp.getCityLevel());
						} else if ("3".equals(bl)) {
							array[i][11] = sct_map.get(Constant.CITY_LEVEL_XD + "_" + hqclcfEmp.getCityLevel());
						} else {
							array[i][11] = null;
						}
					} else {
						array[i][11] = null;
					}
					array[i][12] = hqclcfEmp.getEmpNo();// 工号
					array[i][13] = hqclcfEmp.getEmpName();// 姓名
					array[i][14] = hqclcfEmp.getRankName();// 职级
					array[i][15] = hqclcfEmp.getPosName();// 职务
					array[i][16] = hqclcfEmp.getPostName();// 岗位
					array[i][17] = DateUtil.parseDateFormat(hqclcfEmp.getEnterDate(), "yyyy-MM-dd");// 入职日期
					array[i][18] = DateUtil.parseDateFormat(hqclcfEmp.getTfDate(), "yyyy-MM-dd");// 转正日期
					array[i][19] = DateUtil.parseDateFormat(hqclcfEmp.getLeaveDate(), "yyyy-MM-dd");// 离职日期
					array[i][20] = null;// 离职原因
					array[i][21] = sct_map.get(Constant.JOB_STATUS + "_" + hqclcfEmp.getStatus());// 状态
					array[i][22] = hqclcfEmp.getIdCard();// 身份证号码
					array[i][23] = hqclcfEmp.getPfBankCode_name();// 银行卡名称
					array[i][24] = hqclcfEmp.getPfBankNo();// 银行卡号
					array[i][25] = hqclcfEmp.getOpenBankOrg();// 开户支行
					array[i][26] = hqclcfEmp.getMobilePhone();// 联系电话
					array[i][27] = hqclcfEmp.getComments();// 备注
				}

				ExcelUtil excelUtil = new ExcelUtil();
				excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "员工信息管理（薪资酬组）", 1);
			}
		} catch (Exception e) {
			logger.error("导出失败：" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public JSONArray getCitySelectData(String bl) throws Exception {
		JSONArray jsonArr = new JSONArray();
		String city_level_code = "";
		if ("1".equals(bl)) {
			city_level_code = "city_level_hq";
		} else if ("2".equals(bl)) {
			city_level_code = "city_level_xj";
		} else if ("3".equals(bl)) {
			city_level_code = "city_level_xd";
		}
		if (!"".equals(city_level_code)) {
			List<SysConfigType> ls = sysConfigTypeService.getConfigTypesByPSysCode(city_level_code);
			if (ls.size() > 0) {
				for (SysConfigType s : ls) {
					JSONObject json = new JSONObject();
					json.put("code", s.getSysValue());
					json.put("name", s.getSysName());
					jsonArr.add(json);
				}
			}
		}
		return jsonArr;
	}

	@Override
	public ModelAndView queryAllOpenTypes(ModelAndView model) throws Exception {
		Json json = new Json();
		model.addObject("workOrgNoList", sysWorkplacesetMapper.queryAllWorkplaceset(new SysWorkplaceset()));
		model.addObject("workOrgNoListTpl", zhphObjectMapper.writeValueAsString(json.getObj(sysWorkplacesetMapper.queryAllWorkplaceset(new SysWorkplaceset()))));
		model.addObject("deptListsTpl", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfDeptMapper.queryAll(new HqclcfDept()))));
		model.addObject("postListTpl", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfPostMapper.queryAll(new HqclcfPost()))));
		model.addObject("positionListTpl", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfBusinessMapper.getBusinessByCondition(new HqclcfBusiness()))));
		model.addObject("rankListTpl", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfRankMapper.getRankByCondition(new HqclcfRank()))));

		return model;
	}

	@Override
	public HqclcfEmp queryEmpByEmpNo(String empNo) throws Exception {
		return hqclcfEmpMapper.queryEmpByEmpNo(empNo);
	}

	@Override
	public String getZhWorkExpByEmpNo(String empNo) throws Exception {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer ZhWorkExp = new StringBuffer();

		// 获取入职日期
		HqclcfEmp hqclcfemp = hqclcfEmpMapper.queryEmpByEmpNo(empNo);
		if (!"".equals(hqclcfemp.getEmpNo()) && hqclcfemp.getEmpNo() != null) {
			ZhWorkExp.append(dateFormater.format(hqclcfemp.getCreatetime())).append("：自").append(dateFormater.format(hqclcfemp.getEnterDate())).append("入职，");
			if (hqclcfemp.getTfDate() != null) {
				ZhWorkExp.append("于").append(dateFormater.format(hqclcfemp.getTfDate())).append("转正，");
			}
			ZhWorkExp.append("在【").append(hqclcfemp.getDeptName()).append("】担任【").append(hqclcfemp.getPostName()).append("】一职。");
			ZhWorkExp.append("</br>");
		}

		// 获取异动信息
		HqclcfPersonTransfer record = new HqclcfPersonTransfer();
		record.setEmpNo(empNo);
		List<HqclcfPersonTransfer> ls = hqclcfPersonTransferMapper.getPersonTransferByCondition(record);
		if (ls.size() > 0) {
			for (HqclcfPersonTransfer emp : ls) {
				// 生效时间必须在当前年月以前的
				String cur_date = DateUtil.getCurrentDate("yyyy-MM");
				if (cur_date.compareTo(emp.getTransferTime()) >= 0) {
					ZhWorkExp.append(dateFormater.format(dateFormater.parse(emp.getCreateDate()))).append(":【").append(baseService.getCodeNameByDmlxAndDm(Constant.TRANSFER_TYPE, emp.getTransferType())).append("】");
					ZhWorkExp.append("自").append(emp.getTransferTime()).append("生效，");
					ZhWorkExp.append("由【");
					if (hqclcfDeptService.queryDeptByDeptCode(emp.getPriDeptNo()) != null) {
						ZhWorkExp.append(hqclcfDeptService.queryDeptByDeptCode(emp.getPriDeptNo()).getDeptName()).append("--");
					}

					if (hqclcfPostService.getHqclcfPostBypostCode(emp.getPriPostNo()) != null) {
						ZhWorkExp.append(hqclcfPostService.getHqclcfPostBypostCode(emp.getPriPostNo()).getPostName()).append("--");
					}
					if (hqclcfBusinessService.queryBusinessByNo(emp.getPriHqPosition()) != null) {
						ZhWorkExp.append(hqclcfBusinessService.queryBusinessByNo(emp.getPriHqPosition()).getPosName()).append("--");
					}
					ZhWorkExp.append(emp.getPriHqRank()).append("】");

					ZhWorkExp.append("转成【");
					if (hqclcfDeptService.queryDeptByDeptCode(emp.getNewDept()) != null) {
						ZhWorkExp.append(hqclcfDeptService.queryDeptByDeptCode(emp.getNewDept()).getDeptName()).append("--");
					}
					if (hqclcfPostService.getHqclcfPostBypostCode(emp.getNewPost()) != null) {
						ZhWorkExp.append(hqclcfPostService.getHqclcfPostBypostCode(emp.getNewPost()).getPostName()).append("--");
					}
					if (hqclcfBusinessService.queryBusinessByNo(emp.getNewHqPosition()) != null) {
						ZhWorkExp.append(hqclcfBusinessService.queryBusinessByNo(emp.getNewHqPosition()).getPosName()).append("--");
					}
					if (hqclcfRankService.getRankByNo(emp.getNewHqRank()) != null) {
						ZhWorkExp.append(hqclcfRankService.getRankByNo(emp.getNewHqRank()).getName());
					}
					ZhWorkExp.append("】。</br>");
				}
			}
		}

		// 获取离职淘汰信息 HQCLCF_EMP_LEAVE
		HqclcfLeave hqlcfLeave = new HqclcfLeave();
		hqlcfLeave.setEmpNo(empNo);
		hqlcfLeave.setStatus(2);// 有效
		hqlcfLeave.setAppStatus("1"); // 同意离职
		List<HqclcfLeave> hl_ls = hqclcfLeaveMapper.queryAllLeave(hqlcfLeave);
		if (hl_ls.size() > 0) {
			for (HqclcfLeave hl : hl_ls) {
				ZhWorkExp.append(dateFormater.format(hl.getCreateDate())).append("：因【").append(baseService.getCodeNameByDmlxAndDm(Constant.DIMISSION_TYPE, hl.getLeavingReason())).append("】经上级部门审批，自").append(dateFormater.format(hl.getExitTime())).append("正式离职。<br/>");
			}

		}

		// 获取离职转在职信息HQCLCF_EMP_JOBTRANSFER
		HqclcfJobTransfer hqclcfJobTransfer = new HqclcfJobTransfer();
		hqclcfJobTransfer.setEmpNo(empNo);
		List<HqclcfJobTransfer> hj_ls = hqclcfJobTransferMapper.queryAllJobTransfer(hqclcfJobTransfer);
		if (hj_ls.size() > 0) {
			for (HqclcfJobTransfer hj : hj_ls) {
				ZhWorkExp.append(dateFormater.format(hj.getCreateDate())).append("：自").append(dateFormater.format(hj.getEnterDate())).append("办理离职转在职。<br/>");
			}
		}

		// 获取消金员工状态变更记录
		List<CfEmpStatus> cs_ls = cfEmpStatusMapper.queryCfEmpStatusByEmpNo(empNo);
		if (cs_ls.size() > 0) {
			for (CfEmpStatus cs : cs_ls) {
				ZhWorkExp.append(DateUtil.getFormatDateStr(cs.getCreateDate(), "yyyy-MM-dd")).append("：因【状态变更】，自").append(cs.getStartDate()).append("至").append(cs.getEndDate()).append("变更为【").append(baseService.getCodeNameByDmlxAndDm(Constant.XJ_EMPSTATUS_LIST, cs.getStatus().toString())).append("】<br/>");
			}
		}
		return ZhWorkExp.toString();

	}

	@Override
	public void AutoTfEmpType() throws Exception {
		// 调用存储过程
		hqclcfEmpMapper.AutoTfEmpType();
		logger.debug("执行员工自动转正存储过程：STP_AUTO_CALCULATE_EMPTYPE");
	}

	public String getDeptParentByDeptNo(String detp_type, String p_name) {
		String s_name = "";
		if (p_name != null && p_name != "" && p_name != "null") {
			HashMap<String, String> map = new HashMap<String, String>();
			String[] str_arr = p_name.split(",");
			if (str_arr.length > 0) {
				for (int i = 0; i < str_arr.length; i++) {
					String[] ss = str_arr[i].split("=");
					if (!map.containsKey(ss[0])) {
						map.put(ss[0], ss[1]);
					}
				}
			}
			switch (detp_type) {
			case "1":
				s_name = map.get("1");
				break;
			case "2":
				s_name = map.get("2");
				break;
			case "3":
				s_name = map.get("3");
				break;
			case "4":
				s_name = map.get("4");
				break;
			case "5":
				s_name = map.get("5");
				break;
			default:
				s_name = "";
				break;
			}
		}
		return s_name;
	}

	public String getCdDeptName(String detp_type, String p_name) {
		String s_name = "";
		if (p_name != null && p_name != "" && p_name != "null") {
			HashMap<String, String> map = new HashMap<String, String>();
			String[] str_arr = p_name.split(",");
			if (str_arr.length > 0) {
				int j = 1;
				for (int i = str_arr.length - 1; i >= 0; i--) {
					String[] ss = str_arr[i].split("=");
					map.put(String.valueOf(j), ss[1]);
					j++;
				}
			}
			switch (detp_type) {
			case "1":
				s_name = map.get("1");
				break;
			case "2":
				s_name = map.get("2");
				break;
			case "3":
				s_name = map.get("3");
				break;
			case "4":
				s_name = map.get("4");
				break;
			case "5":
				s_name = map.get("5");
				break;
			case "6":
				s_name = map.get("6");
				break;
			case "7":
				s_name = map.get("7");
				break;
			case "8":
				s_name = map.get("8");
				break;
			default:
				s_name = "";
				break;
			}
		}
		return s_name;
	}

	@Override
	public Json bakupHqclcfEmpByGzym(String gzym) throws Exception {
		Json json = new Json();
		if (StringUtil.isEmpty(gzym)) {
			json.setSuccess(false);
			json.setMsg("传入的工资年月有误！");
			return json;
		}
		try {
			DateUtil.parseStringToDate(gzym, "yyyy-MM");
		} catch (Exception e) {
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg("传入的工资年月有误！");
			return json;
		}
		hqclcfEmpMapper.deleteHqclcfEmpBakByGzym(gzym);
		hqclcfEmpMapper.bakupHqclcfEmpForPerGzym(gzym);
		json.setSuccess(true);
		return json;
	}

}
