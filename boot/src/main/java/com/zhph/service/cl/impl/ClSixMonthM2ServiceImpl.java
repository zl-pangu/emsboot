package com.zhph.service.cl.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.mapper.cl.ClSixMonthM2Mapper;
import com.zhph.model.cl.ClCreditBranchCm1;
import com.zhph.model.cl.ClCreditBranchM2;
import com.zhph.model.cl.ClCreditBranchRcl;
import com.zhph.model.cl.ClCreditCoremanagerCm1;
import com.zhph.model.cl.ClCreditCoremanagerM2;
import com.zhph.model.cl.ClCreditCoremanagerM2Det;
import com.zhph.model.cl.ClCreditCoremanagerRcl;
import com.zhph.model.cl.ClSalaryP2pOrg;
import com.zhph.service.cl.ClSixMonthM2Service;
import com.zhph.util.CommonUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.ExcelUtil;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

/**
 * @author lxp
 * @date 2018年1月23日 下午3:36:58
 * @parameter
 * @return
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class ClSixMonthM2ServiceImpl implements ClSixMonthM2Service {

	public static final Logger logger = LogManager.getLogger(ClSixMonthM2ServiceImpl.class);

	@Autowired
	private ClSixMonthM2Mapper clSixMonthM2Mapper;

	@Override
	public Grid<ClCreditCoremanagerM2Det> queryPageInfo(PageBean pageBean, ClCreditCoremanagerM2Det clSixMonthM2) throws Exception {
		PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());

		List<ClCreditCoremanagerM2Det> list = clSixMonthM2Mapper.queryAllM2Detail(clSixMonthM2);
		PageInfo<ClCreditCoremanagerM2Det> pageInfo = new PageInfo<>(list);
		Grid<ClCreditCoremanagerM2Det> grid = new Grid<>();
		grid.setCount(pageInfo.getTotal());
		grid.setData(pageInfo.getList());

		return grid;
	}

	@Override
	public void exportExl(ClCreditCoremanagerM2Det clSixMonthM2, HttpServletRequest req, HttpServletResponse res) throws Exception {

		List<ClCreditCoremanagerM2Det> clSixMonthM2List = clSixMonthM2Mapper.queryAllM2Detail(clSixMonthM2);

		String[] colTitleAry = { "合同号", "客户姓名", "业务经理姓名", "业务经理编号", "团队经理", "团队经理编号", "门店销售经理", "门店销售经理编号", "区域经理", "区域经理编号", "放款时间", "逾期天数", "合同额", "放款额", "分公司", "年月" };
		String[][] convStr = new String[clSixMonthM2List.size()][colTitleAry.length];
		short[] colWidthAry = { 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100 };
		for (int i = 0; i < clSixMonthM2List.size(); i++) {
			ClCreditCoremanagerM2Det csmm = clSixMonthM2List.get(i);
			convStr[i][0] = csmm.getLoanContractNo();
			convStr[i][1] = csmm.getLoanName();
			convStr[i][2] = csmm.getEmpName();
			convStr[i][3] = csmm.getEmpNo();
			convStr[i][4] = csmm.getTeamManager();
			convStr[i][5] = csmm.getTeamManagerNo();
			convStr[i][6] = csmm.getCityManager();
			convStr[i][7] = csmm.getCityManagerNo();
			convStr[i][8] = csmm.getGegionManager();
			convStr[i][9] = csmm.getGegionManagerNo();
			convStr[i][10] = csmm.getPayDate();
			convStr[i][11] = String.valueOf(csmm.getOverdueDays());
			convStr[i][12] = String.valueOf(csmm.getLoanAmount());
			convStr[i][13] = String.valueOf(csmm.getGrantLoanAmount());
			convStr[i][14] = csmm.getOrg();
			convStr[i][15] = csmm.getGzYm();
		}

		ExcelUtil excelUtil = new ExcelUtil();
		excelUtil.writeExecl(colTitleAry, colWidthAry, convStr, null, res, "近六个月M2明细", 1);
	}

	@Override
	public void AutoGenerateM1AndM2(String gzYm) throws Exception {

		clSixMonthM2Mapper.delClCreditCoremanagerRcl(gzYm);
		clSixMonthM2Mapper.delClCreditCoremanagerCm1(gzYm);
		clSixMonthM2Mapper.delClCreditCoremanagerM2(gzYm);
		clSixMonthM2Mapper.delClCreditCoremanagerM2Det(gzYm);

		// 查询所有分公司
		List<ClSalaryP2pOrg> orgList = clSixMonthM2Mapper.selectSalaryP2pOrg();
		Map<String, String> orgMapName = new HashMap<>();// 分公司map
		if (orgList != null && orgList.size() > 0) {
			for (ClSalaryP2pOrg org : orgList) {
				orgMapName.put(org.getName(), org.getNo());
			}
		}
		orgMapName.put("全国", "全国");

		// 查询入催率
		List<ClCreditCoremanagerRcl> rclList = clSixMonthM2Mapper.selectRclList();
		if (rclList != null && rclList.size() > 0) {
			List<ClCreditCoremanagerRcl> storerclList = new ArrayList<>();
			for (ClCreditCoremanagerRcl map : rclList) {
				ClCreditCoremanagerRcl rcl = new ClCreditCoremanagerRcl();
				rcl.setGzYm(gzYm);
				rcl.setOrgName(map.getOrgName());
				rcl.setOrgNo(orgMapName.get(rcl.getOrgName()));
				rcl.setRcl((BigDecimal) map.getRcl());
				rcl.setCreateTime(DateUtil.getCurrentDate());
				storerclList.add(rcl);
			}
			clSixMonthM2Mapper.saveClCreditStoreManagerRCL(storerclList);
		}

		// 保存c-m1迁徙率
		List<ClCreditCoremanagerCm1> qxlList = clSixMonthM2Mapper.selectQxlList();
		if (qxlList != null && qxlList.size() > 0) {
			List<ClCreditCoremanagerCm1> cm1list = new ArrayList<>();
			for (ClCreditCoremanagerCm1 map : qxlList) {
				ClCreditCoremanagerCm1 cm1 = new ClCreditCoremanagerCm1();
				cm1.setGzYm(gzYm);
				cm1.setOrgName(map.getOrgName());
				cm1.setOrgNo(orgMapName.get(cm1.getOrgName()));
				cm1.setQxl((BigDecimal) map.getQxl());
				cm1.setCreateTime(DateUtil.getCurrentDate());
				cm1list.add(cm1);
			}
			clSixMonthM2Mapper.saveClCreditCoremanagerCm1(cm1list);
		}

		// 保存m2+逾期率
		List<ClCreditCoremanagerM2> yqlList = clSixMonthM2Mapper.selectYqlList();
		if (yqlList != null && yqlList.size() > 0) {
			List<ClCreditCoremanagerM2> m2List = new ArrayList<>();
			for (ClCreditCoremanagerM2 map : yqlList) {
				ClCreditCoremanagerM2 m2 = new ClCreditCoremanagerM2();
				m2.setGzYm(gzYm);
				m2.setOrgName(map.getOrgName());
				m2.setOrgNo(orgMapName.get(m2.getOrgName()));
				m2.setYql((BigDecimal) map.getYql());
				m2.setCreateTime(DateUtil.getCurrentDate());
				m2List.add(m2);
			}
			clSixMonthM2Mapper.saveClCreditCoremanagerM2(m2List);
		}

		// 保存m2+明细
		List<ClCreditCoremanagerM2Det> mTwoDetailList = clSixMonthM2Mapper.selectM2List();
		if (mTwoDetailList != null && mTwoDetailList.size() > 0) {
			Set<String> loanSet = new HashSet<>();// 合同号去重
			List<ClCreditCoremanagerM2Det> detaillist = new ArrayList<>();
			for (ClCreditCoremanagerM2Det map : mTwoDetailList) {
				if (!loanSet.add(map.getLoanContractNo())) {
					continue;
				}
				ClCreditCoremanagerM2Det detail = new ClCreditCoremanagerM2Det();
				detail.setGzYm(gzYm);
				detail.setEmpNo(map.getEmpNo());
				detail.setEmpName(map.getEmpName());
				detail.setLoanContractNo(map.getLoanContractNo());
				detail.setLoanName(map.getLoanName());
				try { // 团队，城市，区域
					detail.setTeamManagerNo("");
					detail.setTeamManager("");
					detail.setCityManagerNo("");
					detail.setCityManager("");
					detail.setGegionManagerNo("");
					detail.setGegionManager("");
				} catch (Exception e) {
					logger.error("定时M2+明细获取团队经理、营业部销售经理出错empNo:" + detail.getEmpNo() + ",empName:" + detail.getEmpName(), e);
				}
				// 查询逾期天数
				List<ClCreditCoremanagerM2Det> overdueDaysList = clSixMonthM2Mapper.selectOverdueDaysList(detail.getLoanContractNo());
				detail.setOverdueDays(0);
				if (overdueDaysList != null && overdueDaysList.size() > 0 && !"0".equals(String.valueOf(overdueDaysList.get(0).getOverdueDays()))) {
					detail.setOverdueDays(Integer.valueOf(overdueDaysList.get(0).getOverdueDays()));
				}
				detail.setPayDate(map.getPayDate());
				detail.setLoanAmount(Integer.valueOf(map.getLoanAmount()));
				detail.setGrantLoanAmount(Integer.valueOf(map.getGrantLoanAmount()));
				detail.setOrg(map.getOrg());
				if (!CommonUtil.StringIfNullOrEmpty(detail.getOrg())) {
					detail.setOrgno(orgMapName.get(detail.getOrg()));
				}
				detail.setCreateTime(DateUtil.getCurrentDate());
				detaillist.add(detail);
			}
			clSixMonthM2Mapper.saveClCreditCoremanagerM2Det(detaillist);
		}

		try {
			// buildBranchIndex(gzYm);
		} catch (Exception e) {

		}
	}

	/**
	 * 处理部门风控指标
	 */
	private void buildBranchIndex(String gzYm) throws Exception {

		clSixMonthM2Mapper.delClCreditBranchRcl(gzYm);
		clSixMonthM2Mapper.delClCreditBranchCm1(gzYm);
		clSixMonthM2Mapper.delClCreditBranchM2(gzYm);

		// 保存分部入催率
		List<ClCreditBranchRcl> rclList = clSixMonthM2Mapper.selectBrclList();
		if (rclList != null && rclList.size() > 0) {
			List<ClCreditBranchRcl> storerclList = new ArrayList<>();
			for (ClCreditBranchRcl map : rclList) {
				ClCreditBranchRcl rcl = new ClCreditBranchRcl();
				rcl.setGzYm(gzYm);
				rcl.setBranchName(map.getBranchName());
				rcl.setRcl((BigDecimal) map.getRcl());
				rcl.setCreateTime(DateUtil.getCurrentDate());
				storerclList.add(rcl);
			}
			clSixMonthM2Mapper.saveClCreditBranchRcl(storerclList);
		}

		// 保存分部C-M1迁徙率
		List<ClCreditBranchCm1> qxlList = clSixMonthM2Mapper.selectBqxlList();
		if (qxlList != null && qxlList.size() > 0) {
			List<ClCreditBranchCm1> cm1list = new ArrayList<>();
			for (ClCreditBranchCm1 map : qxlList) {
				ClCreditBranchCm1 cm1 = new ClCreditBranchCm1();
				cm1.setGzYm(gzYm);
				cm1.setBranchName(map.getBranchName());
				cm1.setQxl((BigDecimal) map.getQxl());
				cm1.setCreateTime(DateUtil.getCurrentDate());
				cm1list.add(cm1);
			}
			clSixMonthM2Mapper.saveClCreditBranchCm1(cm1list);
		}

		// 保存分部m2+
		List<ClCreditBranchM2> yqlList = clSixMonthM2Mapper.selectByqlList();
		if (yqlList != null && yqlList.size() > 0) {
			List<ClCreditBranchM2> m2List = new ArrayList<>();
			for (ClCreditBranchM2 map : yqlList) {
				ClCreditBranchM2 m2 = new ClCreditBranchM2();
				m2.setGzYm(gzYm);
				m2.setBranchName(map.getBranchName());
				m2.setYql((BigDecimal) map.getYql());
				m2.setCreateTime(DateUtil.getCurrentDate());
				m2List.add(m2);
			}
			clSixMonthM2Mapper.saveClCreditBranchM2(m2List);
		}

	}

}
