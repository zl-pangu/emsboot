package com.zhph.service.cl.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.config.datasource.DataSourceNameList;
import com.zhph.config.datasource.DataSourceSelector;
import com.zhph.mapper.cl.ClAchieveDetailMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.model.cl.ClAchieveDetail;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.sys.SysUser;
import com.zhph.service.cl.ClAchieveDetailService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import com.zhph.util.StringUtil;

/**
 * 信贷业绩明细
 */
@Service
public class ClAchieveDetailServiceImpl implements ClAchieveDetailService {
	private Log log = LogFactory.getLog(ClAchieveDetailServiceImpl.class);

	@Resource
	private HqclcfEmpMapper hqclcfEmpMapper;
	
	@Resource
	private ClAchieveDetailMapper clAchieveDetailMapper ;
	
	@Resource
	private HqclcfDeptMapper hqclcfDeptMapper;
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	
	/*@Autowired
	private ClAchieveDetailDaoInterface xdAchieveDetailDao;
*/

	@Override
	public Grid<ClAchieveDetail> queryClAchieveDetailGrid(ClAchieveDetail params, PageBean pageBean) throws Exception {
		log.debug("业绩明细分页查询-开始");
		Grid<ClAchieveDetail> grid = new Grid<ClAchieveDetail>();
    	if(pageBean != null){
    		PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
    	}
        List<ClAchieveDetail> list = clAchieveDetailMapper.queryByPage(params);
        dealDeptInfo(list);
        PageInfo<ClAchieveDetail> pageInfo=new PageInfo<>(list);
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
		log.debug("业绩明细分页查询-完成");
		return grid;
	}

	@Override
	public Grid<ClAchieveDetail> queryClAchieveDetailGrid(ClAchieveDetail params) throws Exception {
		Grid<ClAchieveDetail> grid = new Grid<ClAchieveDetail>();
		log.debug("业绩明细分页查询-开始");
		PageHelper.startPage(params.getPage(), params.getRows());
        List<ClAchieveDetail> list = clAchieveDetailMapper.queryByPage(params);
        dealDeptInfo(list);
        PageInfo<ClAchieveDetail> pageInfo=new PageInfo<>(list);
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
		log.debug("业绩明细分页查询-完成");
		return grid;
	}

	/**
	 * 将部门信息从一个整串中拆分出来
	 * @param list
	 */
	private void dealDeptInfo(List<ClAchieveDetail> list){
        if(list != null && list.size() > 0){
        	String[] deptInfos;
        	for (ClAchieveDetail clAchieveDetail : list) {
        		String deptInfoStr = clAchieveDetail.getDeptInfo();
        		if(!StringUtil.isEmpty(deptInfoStr)){
        			deptInfos = deptInfoStr.split(",");//temps[0]:deptType;temps[1]:deptCode;temps[2]:deptName;
        			if(deptInfos != null && deptInfos.length > 0){
        				String[] temps;
        				for (String s : deptInfos) {
        					if(StringUtil.isEmpty(s)){
        						continue;
        					}
        					temps = s.split("=");
        					if(temps == null || temps.length != 3){
        						continue;
        					}
        					if(Constant.DEPT_TYPE_LEVEL1.equals(temps[0])){
        						clAchieveDetail.setAreaNo(temps[1]);
        						clAchieveDetail.setAreaName(temps[2]);
        					}else if(Constant.DEPT_TYPE_LEVEL2.equals(temps[0])){
        						clAchieveDetail.setOrgNo(temps[1]);
        						clAchieveDetail.setOrgName(temps[2]);
        					}else if(Constant.DEPT_TYPE_LEVEL3.equals(temps[0])){
        						clAchieveDetail.setDeptNo(temps[1]);
        						clAchieveDetail.setDeptName(temps[2]);
        					}else if(Constant.DEPT_TYPE_LEVEL4.equals(temps[0])){
        						clAchieveDetail.setTeamNo(temps[1]);
        						clAchieveDetail.setTeamName(temps[2]);
        					}
						}
        			}
        		}
			}
        }
	}
	
	@Override
	public Integer queryClAchieveDetailsCount(ClAchieveDetail params) throws Exception {
		return clAchieveDetailMapper.queryPageCount(params);
	}
	
	/**
	 * 需要访问另外一个数据源
	 * @param gzym
	 * @return
	 */
	@DataSourceSelector(DataSourceNameList.BUSI_DATASOURCE)
	public List<ClAchieveDetail> queryClAchieveDetailsFromBusinessDb(String gzym){
		return clAchieveDetailMapper.queryClAchieveDetailsFromBusinessDb(gzym);
	}

	@Override
	public List<ClAchieveDetail> syncClAchieveDetail(String gzym, SysUser sysUser) throws Exception {
		if (StringUtil.isEmpty(gzym)) {
			throw new Exception("需要具体的工资年月才能同步");
		}

		// 删除历史数据
		clAchieveDetailMapper.deleteByGzym(gzym);

		// 从业务库中查询业绩明细
		List<ClAchieveDetail> detailList = this.queryClAchieveDetailsFromBusinessDb(gzym);

		for (int i = 0; i < detailList.size(); i++) {
			  //获取到业务经理的编号(员工编号)和姓名
			  String empNo = detailList.get(i).getBusinessManagerNo();
			  HqclcfEmp emp = hqclcfEmpMapper.queryEmpNameByNo(empNo);
			  if (emp!=null) {
			  detailList.get(i).setBusinessManagerName(emp.getEmpName());
			  
			  //添加业务专员所对应的团队编号到业务明细表的部门编号
			  detailList.get(i).setCreatorNo(emp.getDeptNo());
			  
			  //获取团队经理编号和姓名(Pid)
			  HqclcfDept dept = hqclcfDeptMapper.queryDeptByCode(emp.getDeptNo());
			  
			  Long pid = dept.getPid();
			  HqclcfEmp emp2 = hqclcfEmpMapper.queryEmpClAchieveDetailDept(String.valueOf(pid));
			  //将团队经理信息添加到业绩明细
			  if (emp2!=null) {
				  detailList.get(i).setTeamManagerNo(emp2.getEmpNo());
				  detailList.get(i).setTeamManagerName(emp2.getEmpName());
				
				  //获取营业部经理的编号和姓名
				  HqclcfDept dept2 = hqclcfDeptMapper.queryDeptByCode(emp2.getDeptNo());
				  Long pid2 = dept2.getPid();
				  HqclcfEmp emp3 = hqclcfEmpMapper.queryEmpClAchieveDetailDept(String.valueOf(pid2));
				  //将营业部经理信息添加到业绩明细
				  if (emp3!=null) {
					  detailList.get(i).setOrgManagerNo(emp3.getEmpNo());
					  detailList.get(i).setOrgManagerName(emp3.getEmpName());
					  
					  //获取区域经理编号和姓名
					  HqclcfDept dept3 = hqclcfDeptMapper.queryDeptByCode(emp3.getDeptNo());
					  Long pid3 = dept3.getPid();
					  HqclcfEmp emp4 = hqclcfEmpMapper.queryEmpClAchieveDetailDept(String.valueOf(pid3));
					  if (emp4!=null) {
						  detailList.get(i).setAreaManagerNo(emp4.getEmpNo());
						  detailList.get(i).setAreaManagerName(emp4.getEmpName());
					   }
				    }	  
				}
			}
		}
		
		// 保存业绩明细到薪资数据库
		this.saveList(detailList, sysUser);
		/*baseDao.saveLog(null, detailList, ClAchieveDetail.class, sysUser, OperateType.SAVE, log);*/

		return detailList;
	}
	
	private Integer saveList(List<ClAchieveDetail> achieveDetails,SysUser sysUser){
		if (achieveDetails != null) {
			int i = 0;
			Date now = new Date();
			String userName = sysUser == null ? "" : sysUser.getUserName();
			SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			try {
				for (ClAchieveDetail detail : achieveDetails) {
					detail.setCreateTime(now);
					detail.setCreatorNo(userName);
					clAchieveDetailMapper.insert(detail);
					i++;
					if ((i > 0 && i % 100 == 0) || i == achieveDetails.size()) {
						session.commit();
						session.clearCache();
					}
				}
			} catch (Exception e) {
				session.rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}
			return i;
		} else {
			return 0;
		}
	}

	@Override
	public List<ClAchieveDetail> queryClAchieveDetailList(ClAchieveDetail params) throws Exception {
		return clAchieveDetailMapper.queryByConditions(params);
	}

	@Override
	public ClAchieveDetail deleteClAchieveDetail(ClAchieveDetail params, SysUser sysUser) throws Exception {
		if (params == null || params.getPriNumber() == null) {
			throw new Exception("必要的参数不能为空");
		}

		// 查询要删除的业绩明细
		List<ClAchieveDetail> list = clAchieveDetailMapper.queryByConditions(params);
		if (list == null || list.size() <= 0) {
			throw new Exception("不存在要删除的业绩明细");
		} else if (list.size() > 1) {
			throw new Exception("错误，发现多条要删除的数据");
		}

		// 删除
		ClAchieveDetail detail = list.get(0);
		clAchieveDetailMapper.deleteById(detail.getPriNumber());

		// 记录日志
		/*baseDao.saveLog(detail, null, ClAchieveDetail.class, user, OperateType.DELETE, log);*/
		return detail;
	}

	@Override
	public ClAchieveDetail updateClAchieveDetail(ClAchieveDetail queryParams, ClAchieveDetail modifyParams, SysUser sysUser) throws Exception {
		// 必要参数检查
		if (queryParams == null || modifyParams == null) {
			throw new Exception("参数不能为空");
		}
		Long priNumber = queryParams.getPriNumber();
		if (priNumber == null) {
			throw new Exception("参数[priNumber]不能为空");
		}

		// 检查要修改的数据是否正确
		HqclcfEmp emp = null;
		String businessManagerNo = modifyParams.getBusinessManagerNo();
		if (StringUtil.isEmpty(businessManagerNo)) {
			throw new Exception("业务经理不能为空");
		}
		emp = hqclcfEmpMapper.queryEmpByEmpNo(businessManagerNo);
		if (emp == null) {
			throw new Exception("员工信息中不存在录入的业务经理[" + businessManagerNo + "]");
		}
		String teamManagerNo = modifyParams.getTeamManagerNo();
		if (!StringUtil.isEmpty(teamManagerNo)) {
			emp = hqclcfEmpMapper.queryEmpByEmpNo(teamManagerNo);
			if (emp == null) {
				throw new Exception("员工信息中不存在录入的团队经理[" + teamManagerNo + "]");
			}
		}
		String orgManagerNo = modifyParams.getOrgManagerNo();
		if (!StringUtil.isEmpty(orgManagerNo)) {
			emp = hqclcfEmpMapper.queryEmpByEmpNo(orgManagerNo);
			if (emp == null) {
				throw new Exception("员工信息中不存在录入的门店经理[" + orgManagerNo + "]");
			}
		}
		String areaManagerNo = modifyParams.getAreaManagerNo();
		if (!StringUtil.isEmpty(areaManagerNo)) {
			emp = hqclcfEmpMapper.queryEmpByEmpNo(areaManagerNo);
			if (emp == null) {
				throw new Exception("员工信息中不存在录入的区域经理[" + areaManagerNo + "]");
			}
		}
		String wfEmpNo = modifyParams.getWfEmpNo();
		if (!StringUtil.isEmpty(wfEmpNo)) {
			emp = hqclcfEmpMapper.queryEmpByEmpNo(wfEmpNo);
			if (emp == null) {
				throw new Exception("员工信息中不存在录入的外访专员[" + wfEmpNo + "]");
			}
		}
		String xsEmpNo = modifyParams.getXsEmpNo();
		if (!StringUtil.isEmpty(xsEmpNo)) {
			emp = hqclcfEmpMapper.queryEmpByEmpNo(xsEmpNo);
			if (emp == null) {
				throw new Exception("员工信息中不存在录入的信审专员[" + xsEmpNo + "]");
			}
		}
		String kfEmpNo = modifyParams.getKfEmpNo();
		if (!StringUtil.isEmpty(kfEmpNo)) {
			emp = hqclcfEmpMapper.queryEmpByEmpNo(kfEmpNo);
			if (emp == null) {
				throw new Exception("员工信息中不存在录入的客服专员[" + kfEmpNo + "]");
			}
		}
		String deptNo = modifyParams.getDeptNo();
		if (!StringUtil.isEmpty(deptNo)) {
			HqclcfDept deptDept = hqclcfDeptMapper.queryDeptByCode(deptNo);
			if (deptDept == null || !"1".equals(deptDept.getStatus())) {
				throw new Exception("系统中不存在录入的部门编号[" + deptNo + "]");
			}
		}
		Date now = new Date();// 当前时间
		String userName = sysUser == null ? "" : sysUser.getFullName();

		// 检查业绩明细是否存在
		List<ClAchieveDetail> list = clAchieveDetailMapper.queryByConditions(queryParams);
		if (list == null || list.size() <= 0) {
			throw new Exception("不存在要修改的业绩明细");
		} else if (list.size() > 1) {
			throw new Exception("存在多条业绩明细[priNumber=" + priNumber + "]");
		}
		ClAchieveDetail detail = list.get(0);// 新的
		
		detail.setBusinessManagerNo(businessManagerNo);
		detail.setTeamManagerNo(teamManagerNo);
		detail.setOrgManagerNo(orgManagerNo);
		detail.setAreaManagerNo(areaManagerNo);
		detail.setWfEmpNo(wfEmpNo);
		detail.setXsEmpNo(xsEmpNo);
		detail.setKfEmpNo(kfEmpNo);
		detail.setDeptNo(deptNo);
		detail.setUpdateTime(now);
		detail.setModifierNo(userName);

		// 保存修改
		clAchieveDetailMapper.update(detail);

		// 保存日志
		/*ClAchieveDetail oldDetail = new ClAchieveDetail();
		 * BeanUtils.copyProperties(detail, oldDetail);
		 * baseDao.saveLog(oldDetail, detail, ClAchieveDetail.class, user, OperateType.UPDATE, log);*/

		return detail;
	}

	@Override
	public void batchUpdateClAchieveDetail(List<Long> priNumberList, List<String> selNoList, ClAchieveDetail modifyParams, SysUser sysUser) throws Exception {
		// 必要参数检查
		if (priNumberList == null || priNumberList.size() <= 0) {
			throw new Exception("必须传入要修改的业绩明细的主键");
		}
		if (selNoList == null || selNoList.size() <= 0) {
			throw new Exception("必须传入要修改的字段编号");
		}
		if (modifyParams == null) {
			throw new Exception("参数错误");
		}

		// 检查修改的数据
		HqclcfEmp emp = null;
		String businessManagerNo = modifyParams.getBusinessManagerNo();
		if (selNoList.contains("businessManagerNo")) {
			if (StringUtil.isEmpty(businessManagerNo)) {
				throw new Exception("业务经理编号不能为空值");
			}
			emp = hqclcfEmpMapper.queryEmpByEmpNo(businessManagerNo);
			if (emp == null) {
				throw new Exception("员工信息中不存在录入的业务经理[" + businessManagerNo + "]");
			}
		}
		String teamManagerNo = modifyParams.getTeamManagerNo();
		if (selNoList.contains("teamManagerNo")) {
			if (!StringUtil.isEmpty(teamManagerNo)) {
				emp = hqclcfEmpMapper.queryEmpByEmpNo(teamManagerNo);
				if (emp == null) {
					throw new Exception("员工信息中不存在录入的团队经理[" + teamManagerNo + "]");
				}
			}
		}
		String orgManagerNo = modifyParams.getOrgManagerNo();
		if (selNoList.contains("orgManagerNo")) {
			if (!StringUtil.isEmpty(orgManagerNo)) {
				emp = hqclcfEmpMapper.queryEmpByEmpNo(orgManagerNo);
				if (emp == null) {
					throw new Exception("员工信息中不存在录入的营业部经理[" + orgManagerNo + "]");
				}
			}
		}
		String areaManagerNo = modifyParams.getAreaManagerNo();
		if (selNoList.contains("areaManagerNo")) {
			if (!StringUtil.isEmpty(areaManagerNo)) {
				emp = hqclcfEmpMapper.queryEmpByEmpNo(areaManagerNo);
				if (emp == null) {
					throw new Exception("员工信息中不存在录入的区域经理[" + areaManagerNo + "]");
				}
			}
		}
		String wfEmpNo = modifyParams.getWfEmpNo();
		if (selNoList.contains("wfEmpNo")) {
			if (!StringUtil.isEmpty(wfEmpNo)) {
				emp = hqclcfEmpMapper.queryEmpByEmpNo(wfEmpNo);
				if (emp == null) {
					throw new Exception("员工信息中不存在录入的外访专员[" + wfEmpNo + "]");
				}
			}
		}
		String xsEmpNo = modifyParams.getXsEmpNo();
		if (selNoList.contains("xsEmpNo")) {
			if (!StringUtil.isEmpty(xsEmpNo)) {
				emp = hqclcfEmpMapper.queryEmpByEmpNo(xsEmpNo);
				if (emp == null) {
					throw new Exception("员工信息中不存在录入的信审专员[" + xsEmpNo + "]");
				}
			}
		}
		String kfEmpNo = modifyParams.getKfEmpNo();
		if (selNoList.contains("kfEmpNo")) {
			if (!StringUtil.isEmpty(kfEmpNo)) {
				emp = hqclcfEmpMapper.queryEmpByEmpNo(kfEmpNo);
				if (emp == null) {
					throw new Exception("员工信息中不存在录入的客服专员[" + kfEmpNo + "]");
				}
			}
		}
		String curDeptNo = modifyParams.getCurDeptNo();
		if (!StringUtil.isEmpty(curDeptNo)) {
			HqclcfDept deptDept = hqclcfDeptMapper.queryDeptByCode(curDeptNo);
			if (deptDept == null || !"1".equals(deptDept.getStatus())) {
				throw new Exception("系统中不存在录入的部门编号[" + curDeptNo + "]");
			}
		}

		Date now = new Date();// 当前时间
		String userName = sysUser == null ? "" : sysUser.getUserName();

		ClAchieveDetail detail = null;
		/*ClAchieveDetail oldDetail = null;
		List<ClAchieveDetail> oldDetailList = new ArrayList<ClAchieveDetail>();*/
		List<ClAchieveDetail> newDetailList = new ArrayList<ClAchieveDetail>();
		for (Long priNumber : priNumberList) {
			detail = clAchieveDetailMapper.queryById(priNumber);
			/*oldDetail = new ClAchieveDetail();
			BeanUtils.copyProperties(detail, oldDetail);
			oldDetailList.add(oldDetail);*/
			if (selNoList.contains("businessManagerNo"))
				detail.setBusinessManagerNo(businessManagerNo);
			if (selNoList.contains("teamManagerNo"))
				detail.setTeamManagerNo(teamManagerNo);
			if (selNoList.contains("orgManagerNo"))
				detail.setOrgManagerNo(orgManagerNo);
			if (selNoList.contains("areaManagerNo"))
				detail.setAreaManagerNo(areaManagerNo);
			if (selNoList.contains("wfEmpNo"))
				detail.setWfEmpNo(wfEmpNo);
			if (selNoList.contains("xsEmpNo"))
				detail.setXsEmpNo(xsEmpNo);
			if (selNoList.contains("kfEmpNo"))
				detail.setKfEmpNo(kfEmpNo);
			if (selNoList.contains("curDeptNo"))
				detail.setCurDeptNo(curDeptNo);
			detail.setUpdateTime(now);
			detail.setModifierNo(userName);
			newDetailList.add(detail);
		}

		// 保存修改
		batchUpdate(newDetailList);

		// 保存日志
		/*baseDao.saveLog(oldDetailList, newDetailList, ClAchieveDetail.class, sysUser, OperateType.UPDATE, log);*/
	}
	
	public int batchUpdate(List<ClAchieveDetail> clAchieveDetails){
		if(clAchieveDetails != null && clAchieveDetails.size() > 0){
			SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			int i = 0;
			try {
				for (ClAchieveDetail achieveDetail : clAchieveDetails) {
					clAchieveDetailMapper.update(achieveDetail);
					i++;
					if ((i > 0 && i % 100 == 0) || i == clAchieveDetails.size()) {
						session.commit();
						session.clearCache();
					}
				}
			} catch (Exception e) {
				session.rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}
			return i;
		}else{
			return 0;
		}
	}

	@Override
	public <T> void exportClAchieveDetail(HttpServletResponse res, ClAchieveDetail params, Class<T> clz) throws Exception {
		Workbook wb = null;
		String suffix = null;
		if (clz != null && XSSFWorkbook.class.equals(clz)) {
			wb = new XSSFWorkbook();
			suffix = ".xlsx";
		} else {
			wb = new HSSFWorkbook();
			suffix = ".xls";
		}

		// 业绩明细
		Sheet sheet = wb.createSheet("业绩明细");
		List<ClAchieveDetail> list = clAchieveDetailMapper.queryByConditions(params);
        dealDeptInfo(list);
		xdAchieveDetailSheetBuilder(sheet, list);

		
		// 按区域分组统计
		List<ClAchieveDetail> areaList = groupByArea(list);
		/*List<ClAchieveDetail> areaList = clAchieveDetailMapper.queryClAchieveDetailsGroupByArea(params);*/
		Sheet areaSheet = wb.createSheet("按区域分组统计");
		xdAchieveDetailGroupByAreaSheetBuilder(areaSheet, areaList);

		// 按分公司分组统计
		List<ClAchieveDetail> orgList = groupByOrg(list);
		Sheet orgSheet = wb.createSheet("按分公司分组统计");
		/*List<ClAchieveDetail> orgList = clAchieveDetailMapper.queryClAchieveDetailsGroupByOrg(params);*/
		xdAchieveDetailGroupByOrgSheetBuilder(orgSheet, orgList);

		// 按团队分组统计
		List<ClAchieveDetail> teamList = groupByTeam(list);
		Sheet teamSheet = wb.createSheet("按团队分组统计");
		/*List<ClAchieveDetail> teamList = clAchieveDetailMapper.queryClAchieveDetailsGroupByTeam(params);*/
		xdAchieveDetailGroupByTeamSheetBuilder(teamSheet, teamList);

		// 按个人分组统计
		List<ClAchieveDetail> businessList = groupByBusiness(list);
		Sheet businessSheet = wb.createSheet("按个人分组统计");
		/*List<ClAchieveDetail> businessList = clAchieveDetailMapper.queryClAchieveDetailsGroupByBusiness(params);*/
		xdAchieveDetailGroupByBusinessSheetBuilder(businessSheet, businessList);

		res.setCharacterEncoding("utf-8");
		String dtStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String fileName = "业绩明细";
		res.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(dtStr + "_" + fileName + suffix, "utf-8"));

		ServletOutputStream sos = null;
		try {
			sos = res.getOutputStream();
			wb.write(sos);
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (sos != null) {
					sos.close();
				}
				if(wb != null){
					wb.close();
				}
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
	}
	
	private List<ClAchieveDetail> groupByArea(List<ClAchieveDetail> list){
		Map<String,Map<String,ClAchieveDetail>> gzymMap = new HashMap<String,Map<String,ClAchieveDetail>>();//用于分组计算
		Map<String,ClAchieveDetail> areaMap;
		ClAchieveDetail temp;
		if(list != null && list.size() > 0){
			BigDecimal tempBd;
			for (ClAchieveDetail detail : list) {
				if(detail.getGzym() == null || detail.getAreaNo() == null){
					continue;
				}
				areaMap = gzymMap.get(detail.getGzym());
				if(areaMap == null){
					areaMap = new HashMap<String,ClAchieveDetail>();
					gzymMap.put(detail.getGzym(), areaMap);
				}
				temp = areaMap.get(detail.getAreaNo());
				if(temp == null){
					temp = new ClAchieveDetail();
					temp.setAreaNo(detail.getAreaNo());
					temp.setAreaName(detail.getAreaName());
					temp.setLoanAmount(new BigDecimal(0));
					temp.setGrantLoanAmount(new BigDecimal(0));
					temp.setGzym(detail.getGzym());
					areaMap.put(detail.getAreaNo(), temp);
				}
				tempBd = detail.getLoanAmount() == null ? new BigDecimal(0) : detail.getLoanAmount();
				temp.setLoanAmount(tempBd.add(temp.getLoanAmount()));
				tempBd = detail.getGrantLoanAmount() == null ? new BigDecimal(0) : detail.getGrantLoanAmount();
				temp.setGrantLoanAmount(tempBd.add(temp.getGrantLoanAmount()));
				temp = null;
				areaMap = null;
			}//end for
			List<String> gzymKey = new ArrayList<String>(gzymMap.keySet());
			Collections.sort(gzymKey,new Comparator<String>() {
				public int compare(String str1, String str2) {
					return str2.compareToIgnoreCase(str1);
				}
			});
			Iterator<String> gzymIter = gzymKey.iterator();
			List<ClAchieveDetail> areaList = new ArrayList<ClAchieveDetail>();
			while(gzymIter.hasNext()){
				areaMap = gzymMap.get(gzymIter.next());
				if(areaMap == null || areaMap.size() <= 0)continue;
				List<String> areaKey = new ArrayList<String>(areaMap.keySet());
				Collections.sort(areaKey);
				Iterator<String> areaIter = areaKey.iterator();
				while(areaIter.hasNext()){
					areaList.add(areaMap.get(areaIter.next()));
				}
			}
			return areaList;
		}
		return null;
	}
	private List<ClAchieveDetail> groupByOrg(List<ClAchieveDetail> list){
		Map<String,Map<String,ClAchieveDetail>> gzymMap = new HashMap<String,Map<String,ClAchieveDetail>>();//用于分组计算
		Map<String,ClAchieveDetail> orgMap;
		ClAchieveDetail temp;
		if(list != null && list.size() > 0){
			BigDecimal tempBd;
			for (ClAchieveDetail detail : list) {
				if(detail.getGzym() == null || detail.getOrgNo() == null){
					continue;
				}
				orgMap = gzymMap.get(detail.getGzym());
				if(orgMap == null){
					orgMap = new HashMap<String,ClAchieveDetail>();
					gzymMap.put(detail.getGzym(), orgMap);
				}
				temp = orgMap.get(detail.getOrgNo());
				if(temp == null){
					temp = new ClAchieveDetail();
					temp.setOrgNo(detail.getOrgNo());
					temp.setOrgName(detail.getOrgName());
					temp.setLoanAmount(new BigDecimal(0));
					temp.setGrantLoanAmount(new BigDecimal(0));
					temp.setGzym(detail.getGzym());
					orgMap.put(detail.getOrgNo(), temp);
				}
				tempBd = detail.getLoanAmount() == null ? new BigDecimal(0) : detail.getLoanAmount();
				temp.setLoanAmount(tempBd.add(temp.getLoanAmount()));
				tempBd = detail.getGrantLoanAmount() == null ? new BigDecimal(0) : detail.getGrantLoanAmount();
				temp.setGrantLoanAmount(tempBd.add(temp.getGrantLoanAmount()));
				temp = null;
				orgMap = null;
			}//end for
			List<String> gzymKey = new ArrayList<String>(gzymMap.keySet());
			Collections.sort(gzymKey,new Comparator<String>() {
				public int compare(String str1, String str2) {
					return str2.compareToIgnoreCase(str1);
				}
			});
			Iterator<String> gzymIter = gzymKey.iterator();
			List<ClAchieveDetail> orgList = new ArrayList<ClAchieveDetail>();
			while(gzymIter.hasNext()){
				orgMap = gzymMap.get(gzymIter.next());
				if(orgMap == null || orgMap.size() <= 0)continue;
				List<String> orgKey = new ArrayList<String>(orgMap.keySet());
				Collections.sort(orgKey);
				Iterator<String> orgIter = orgKey.iterator();
				while(orgIter.hasNext()){
					orgList.add(orgMap.get(orgIter.next()));
				}
			}
			return orgList;
		}
		return null;
	}
	private List<ClAchieveDetail> groupByTeam(List<ClAchieveDetail> list){
		
		Map<String,Map<String,Map<String,Map<String,ClAchieveDetail>>>> gzymMap = new HashMap<String,Map<String,Map<String,Map<String,ClAchieveDetail>>>>();//用于分组计算
		Map<String,Map<String,Map<String,ClAchieveDetail>>> orgMap;
		Map<String,Map<String,ClAchieveDetail>> teamMap;
		Map<String,ClAchieveDetail> teamManagerMap;
		ClAchieveDetail temp;
		if(list != null && list.size() > 0){
			BigDecimal tempBd;
			for (ClAchieveDetail detail : list) {
				if(detail.getGzym() == null || detail.getOrgNo() == null || detail.getTeamNo() == null || detail.getTeamManagerNo() == null){
					continue;
				}
				
				orgMap = gzymMap.get(detail.getGzym());
				if(orgMap == null){
					orgMap = new HashMap<String,Map<String,Map<String,ClAchieveDetail>>>();
					gzymMap.put(detail.getGzym(), orgMap);
				}
				teamMap = orgMap.get(detail.getOrgNo());
				if(teamMap == null){
					teamMap = new HashMap<String,Map<String,ClAchieveDetail>>();
					orgMap.put(detail.getOrgNo(), teamMap);
				}
				teamManagerMap = teamMap.get(detail.getTeamNo());
				if(teamManagerMap == null){
					teamManagerMap = new HashMap<String,ClAchieveDetail>();
					teamMap.put(detail.getTeamNo(), teamManagerMap);
				}
				temp = teamManagerMap.get(detail.getTeamManagerNo());
				if(temp == null){
					temp = new ClAchieveDetail();
					temp.setOrgNo(detail.getOrgNo());
					temp.setOrgName(detail.getOrgName());
					temp.setTeamNo(detail.getTeamNo());
					temp.setTeamName(detail.getTeamName());
					temp.setTeamManagerNo(detail.getTeamManagerNo());
					temp.setTeamManagerName(detail.getTeamManagerName());
					temp.setLoanAmount(new BigDecimal(0));
					temp.setGrantLoanAmount(new BigDecimal(0));
					temp.setGzym(detail.getGzym());
					teamManagerMap.put(detail.getTeamManagerNo(), temp);
				}
				tempBd = detail.getLoanAmount() == null ? new BigDecimal(0) : detail.getLoanAmount();
				temp.setLoanAmount(tempBd.add(temp.getLoanAmount()));
				tempBd = detail.getGrantLoanAmount() == null ? new BigDecimal(0) : detail.getGrantLoanAmount();
				temp.setGrantLoanAmount(tempBd.add(temp.getGrantLoanAmount()));
			}//end for
			List<String> gzymKey = new ArrayList<String>(gzymMap.keySet());
			Collections.sort(gzymKey,new Comparator<String>() {
				public int compare(String str1, String str2) {
					return str2.compareToIgnoreCase(str1);
				}
			});
			List<ClAchieveDetail> teamManagerList = new ArrayList<ClAchieveDetail>();
			Iterator<String> gzymIter = gzymKey.iterator();
			while(gzymIter.hasNext()){//工资年月
				orgMap = gzymMap.get(gzymIter.next());
				if(orgMap == null || orgMap.size() <= 0)continue;
				List<String> orgKey = new ArrayList<String>(orgMap.keySet());
				Collections.sort(orgKey);
				Iterator<String> orgIter = orgKey.iterator();
				while(orgIter.hasNext()){//分公司
					teamMap = orgMap.get(orgIter.next());
					if(teamMap == null || teamMap.size() <= 0)continue;
					List<String> teamKey = new ArrayList<String>(teamMap.keySet());
					Collections.sort(teamKey);
					Iterator<String> teamIter = teamKey.iterator();
					while(teamIter.hasNext()){//团队
						teamManagerMap = teamMap.get(teamIter.next());
						if(teamManagerMap == null || teamManagerMap.size() <= 0)continue;
						List<String> teamManagerKey = new ArrayList<String>(teamManagerMap.keySet());
						Collections.sort(teamManagerKey);
						Iterator<String> teamManagerIter = teamManagerKey.iterator();
						while(teamManagerIter.hasNext()){//团队经理
							teamManagerList.add(teamManagerMap.get(teamManagerIter.next()));
						}//END 团队经理
					}//END 团队
				}//END 分公司
			}//END 工资年月
			return teamManagerList;
		}
		return null;
	}
	private List<ClAchieveDetail> groupByBusiness(List<ClAchieveDetail> list){
		Map<String,Map<String,Map<String,ClAchieveDetail>>> gzymMap = new HashMap<String,Map<String,Map<String,ClAchieveDetail>>>();//年月map
		Map<String,Map<String,ClAchieveDetail>> orgMap;
		Map<String,ClAchieveDetail> busiManagerMap;
		ClAchieveDetail temp;
		if(list != null && list.size() > 0){
			BigDecimal tempBd;
			for (ClAchieveDetail detail : list) {
				if(detail.getGzym() == null || detail.getOrgNo() == null || detail.getBusinessManagerNo() == null){
					continue;
				}
				
				orgMap = gzymMap.get(detail.getGzym());
				if(orgMap == null){//分公司map
					orgMap = new HashMap<String,Map<String,ClAchieveDetail>>();
					gzymMap.put(detail.getGzym(), orgMap);
				}
				busiManagerMap = orgMap.get(detail.getOrgNo());
				if(busiManagerMap == null){//分公司经理map
					busiManagerMap = new HashMap<String,ClAchieveDetail>();
					orgMap.put(detail.getOrgNo(), busiManagerMap);
				}
				temp = busiManagerMap.get(detail.getBusinessManagerNo());
				if(temp == null){
					temp = new ClAchieveDetail();
					temp.setOrgNo(detail.getOrgNo());
					temp.setOrgName(detail.getOrgName());
					temp.setBusinessManagerNo(detail.getBusinessManagerNo());
					temp.setBusinessManagerName(detail.getBusinessManagerName());
					temp.setLoanAmount(new BigDecimal(0));
					temp.setGrantLoanAmount(new BigDecimal(0));
					temp.setGzym(detail.getGzym());
					busiManagerMap.put(detail.getBusinessManagerNo(), temp);
				}
				tempBd = detail.getLoanAmount() == null ? new BigDecimal(0) : detail.getLoanAmount();
				temp.setLoanAmount(tempBd.add(temp.getLoanAmount()));
				tempBd = detail.getGrantLoanAmount() == null ? new BigDecimal(0) : detail.getGrantLoanAmount();
				temp.setGrantLoanAmount(tempBd.add(temp.getGrantLoanAmount()));
			}//end for
			List<String> gzymKey = new ArrayList<String>(gzymMap.keySet());
			Collections.sort(gzymKey,new Comparator<String>() {
				public int compare(String str1, String str2) {
					return str2.compareToIgnoreCase(str1);
				}
			});
			List<ClAchieveDetail> busiManagerList = new ArrayList<ClAchieveDetail>();
			Iterator<String> gzymIter = gzymKey.iterator();
			while(gzymIter.hasNext()){//工资年月
				orgMap = gzymMap.get(gzymIter.next());
				if(orgMap == null || orgMap.size() <= 0)continue;
				List<String> orgKey = new ArrayList<String>(orgMap.keySet());
				Collections.sort(orgKey);
				Iterator<String> orgIter = orgKey.iterator();
				while(orgIter.hasNext()){//分公司
					busiManagerMap = orgMap.get(orgIter.next());
					if(busiManagerMap == null || busiManagerMap.size() <= 0)continue;
					List<String> busiManagerKey = new ArrayList<String>(busiManagerMap.keySet());
					Collections.sort(busiManagerKey);
					Iterator<String> busiManagerIter = busiManagerKey.iterator();
					while(busiManagerIter.hasNext()){//分公司经理
						busiManagerList.add(busiManagerMap.get(busiManagerIter.next()));
					}//END 分公司经理
				}//END 分公司
			}//END 工资年月
			return busiManagerList;
		}
		return null;
	}

	/**
	 * 业绩明细sheet构建方法
	 * 
	 * @Title: xdAchieveDetailSheetBuilder
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	private void xdAchieveDetailSheetBuilder(Sheet sheet, List<ClAchieveDetail> list) throws Exception {
		if (sheet == null)
			return;
		Workbook wb = sheet.getWorkbook();
		Cell cell = null;
		Row row = null;
		int index = 0;

		// 表头1
		String[] titleLine1 = { "序号", "合同信息", "", "", "", "", "", "业务经理", "", "团队经理", "", "营业部经理", "", "区域经理", "", "外访专员", "", "信审专员", "", "客服专员", "", "区域", "", "分公司", "", "部门", "", "团队", "", "工资年月" };
		int[] titleWidths = { 6, 23, 17, 8, 8, 11, 11, 14, 13, 14, 13, 14, 10, 14, 10, 14, 10, 14, 10, 14, 10, 25, 9, 9, 13, 9, 9, 9, 9, 9, 9 };
		final int ratio = 273;// 宽度调整系数
		Font font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		font.setFontHeightInPoints((short) 11);
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(font);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		row = sheet.createRow(index++);
		for (int i = 0, len = titleLine1.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine1[i]);
			cell.setCellStyle(titleStyle);
			sheet.setColumnWidth(i, titleWidths[i] * ratio);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 6));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 7, 8));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 10));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 11, 12));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 14));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 15, 16));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 17, 18));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 19, 20));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 21, 22));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 23, 24));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 25, 26));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 27, 28));

		// 表头2
		String[] titleLine2 = { "", "合同号", "客户姓名", "合同额", "放款额", "约定放款日", "实际放款日", "编号", "姓名", "编号", "姓名", "编号", "姓名", "编号", "姓名", "编号", "姓名", "编号", "姓名", "编号", "姓名", "编号", "名称", "编号", "名称", "编号", "名称", "编号", "名称", "" };
		row = sheet.createRow(index++);
		int i = 0;
		int len = 0;
		for (i = 0, len = titleLine2.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine2[i]);
			cell.setCellStyle(titleStyle);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 29, 29));

		// 冻结表头
		sheet.createFreezePane(0, 2);

		// 写入数据
		Font dataFont = wb.createFont();
		dataFont.setFontName(HSSFFont.FONT_ARIAL);
		dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		dataFont.setFontHeightInPoints((short) 10);
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setFont(dataFont);
		dataStyle.setBorderLeft((short) 1);
		dataStyle.setBorderRight((short) 1);
		dataStyle.setBorderTop((short) 1);
		dataStyle.setBorderBottom((short) 1);
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		dataStyle.setWrapText(true);
		if (list != null && list.size() > 0)
			for (ClAchieveDetail detail : list) {
				row = sheet.createRow(index++);
				int colIndex = 0;
				cell = row.createCell(colIndex++);
				cell.setCellValue(index - 2);
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getLoanContractNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getLoanName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getGrantLoanAmount() == null){
					cell.setCellValue("");
				} else{
					cell.setCellValue(detail.getGrantLoanAmount().doubleValue());
				}
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getGrantLoanDate());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getPayDate());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getBusinessManagerNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getBusinessManagerName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getTeamManagerNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getTeamManagerName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgManagerNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgManagerName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getAreaManagerNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getAreaManagerName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getWfEmpNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getWfEmpName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getXsEmpNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getXsEmpName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getKfEmpNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getKfEmpName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getAreaName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getDeptNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getDeptName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getTeamNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getTeamName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getGzym());
				cell.setCellStyle(dataStyle);
			}
	}

	/**
	 * 业绩明细按区域统计sheet构建
	 * 
	 * @Title: xdAchieveDetailGroupByAreaSheetBuilder
	 * @param @param sheet
	 * @param @param list
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	private void xdAchieveDetailGroupByAreaSheetBuilder(Sheet sheet, List<ClAchieveDetail> list) throws Exception {
		if (sheet == null)
			return;
		Workbook wb = sheet.getWorkbook();
		Cell cell = null;
		Row row = null;
		int index = 0;

		// 表头1
		String[] titleLine1 = { "序号", "区域", "", "合同额", "放款额", "工资年月" };
		int[] titleWidths = { 6, 25, 10, 11, 11, 9 };
		final int ratio = 273;// 宽度调整系数
		Font font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		font.setFontHeightInPoints((short) 11);
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(font);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		row = sheet.createRow(index++);
		for (int i = 0, len = titleLine1.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine1[i]);
			cell.setCellStyle(titleStyle);
			sheet.setColumnWidth(i, titleWidths[i] * ratio);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));

		// 表头2
		String[] titleLine2 = { "", "编号", "名称", "", "", "" };
		row = sheet.createRow(index++);
		int i = 0;
		int len = 0;
		for (i = 0, len = titleLine2.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine2[i]);
			cell.setCellStyle(titleStyle);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));

		// 冻结表头
		sheet.createFreezePane(0, 2);

		// 写入数据
		Font dataFont = wb.createFont();
		dataFont.setFontName(HSSFFont.FONT_ARIAL);
		dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		dataFont.setFontHeightInPoints((short) 10);
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setFont(dataFont);
		dataStyle.setBorderLeft((short) 1);
		dataStyle.setBorderRight((short) 1);
		dataStyle.setBorderTop((short) 1);
		dataStyle.setBorderBottom((short) 1);
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		dataStyle.setWrapText(true);
		if (list != null && list.size() > 0)
			for (ClAchieveDetail detail : list) {
				row = sheet.createRow(index++);
				int colIndex = 0;
				cell = row.createCell(colIndex++);
				cell.setCellValue(index - 2);
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getAreaNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getAreaName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getGrantLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getGrantLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getGzym());
				cell.setCellStyle(dataStyle);
			}
	}

	/**
	 * 业绩明细按分公司统计sheet构建
	 * 
	 * @Title: xdAchieveDetailSheetBuilder
	 * @param @param sheet
	 * @param @param list
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	private void xdAchieveDetailGroupByOrgSheetBuilder(Sheet sheet, List<ClAchieveDetail> list) throws Exception {
		if (sheet == null)
			return;
		Workbook wb = sheet.getWorkbook();
		Cell cell = null;
		Row row = null;
		int index = 0;

		// 表头1
		String[] titleLine1 = { "序号", "分公司", "", "合同额", "放款额", "工资年月" };
		int[] titleWidths = { 6, 9, 13, 9, 9, 9 };
		final int ratio = 273;// 宽度调整系数
		Font font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		font.setFontHeightInPoints((short) 11);
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(font);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		row = sheet.createRow(index++);
		for (int i = 0, len = titleLine1.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine1[i]);
			cell.setCellStyle(titleStyle);
			sheet.setColumnWidth(i, titleWidths[i] * ratio);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));

		// 表头2
		String[] titleLine2 = { "", "编号", "名称", "", "", "" };
		row = sheet.createRow(index++);
		int i = 0;
		int len = 0;
		for (i = 0, len = titleLine2.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine2[i]);
			cell.setCellStyle(titleStyle);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));

		// 冻结表头
		sheet.createFreezePane(0, 2);

		// 写入数据
		Font dataFont = wb.createFont();
		dataFont.setFontName(HSSFFont.FONT_ARIAL);
		dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		dataFont.setFontHeightInPoints((short) 10);
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setFont(dataFont);
		dataStyle.setBorderLeft((short) 1);
		dataStyle.setBorderRight((short) 1);
		dataStyle.setBorderTop((short) 1);
		dataStyle.setBorderBottom((short) 1);
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		dataStyle.setWrapText(true);
		if (list != null && list.size() > 0)
			for (ClAchieveDetail detail : list) {
				row = sheet.createRow(index++);
				int colIndex = 0;
				cell = row.createCell(colIndex++);
				cell.setCellValue(index - 2);
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getGrantLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getGrantLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getGzym());
				cell.setCellStyle(dataStyle);
			}
	}

	/**
	 * 业绩明细按团队统计sheet构建
	 * 
	 * @Title: xdAchieveDetailGroupByTeamSheetBuilder
	 * @param @param sheet
	 * @param @param list
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	private void xdAchieveDetailGroupByTeamSheetBuilder(Sheet sheet, List<ClAchieveDetail> list) throws Exception {
		if (sheet == null)
			return;
		Workbook wb = sheet.getWorkbook();
		Cell cell = null;
		Row row = null;
		int index = 0;

		// 表头1
		String[] titleLine1 = { "序号", "分公司", "", "团队", "", "团队经理", "", "合同额", "放款额", "工资年月" };
		int[] titleWidths = { 6, 9, 13, 9, 9, 14, 16, 9, 9, 9 };
		final int ratio = 273;// 宽度调整系数
		Font font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		font.setFontHeightInPoints((short) 11);
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(font);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		row = sheet.createRow(index++);
		for (int i = 0, len = titleLine1.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine1[i]);
			cell.setCellStyle(titleStyle);
			sheet.setColumnWidth(i, titleWidths[i] * ratio);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 6));

		// 表头2
		String[] titleLine2 = { "", "编号", "名称", "编号", "名称", "编号", "姓名", "", "", "" };
		row = sheet.createRow(index++);
		int i = 0;
		int len = 0;
		for (i = 0, len = titleLine2.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine2[i]);
			cell.setCellStyle(titleStyle);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 8, 8));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 9, 9));

		// 冻结表头
		sheet.createFreezePane(0, 2);

		// 写入数据
		Font dataFont = wb.createFont();
		dataFont.setFontName(HSSFFont.FONT_ARIAL);
		dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		dataFont.setFontHeightInPoints((short) 10);
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setFont(dataFont);
		dataStyle.setBorderLeft((short) 1);
		dataStyle.setBorderRight((short) 1);
		dataStyle.setBorderTop((short) 1);
		dataStyle.setBorderBottom((short) 1);
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		dataStyle.setWrapText(true);
		if (list != null && list.size() > 0)
			for (ClAchieveDetail detail : list) {
				row = sheet.createRow(index++);
				int colIndex = 0;
				cell = row.createCell(colIndex++);
				cell.setCellValue(index - 2);
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getTeamNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getTeamName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getTeamManagerNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getTeamManagerName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getGrantLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getGrantLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getGzym());
				cell.setCellStyle(dataStyle);
			}
	}

	/**
	 * 业绩明细按个人统计sheet构建
	 * 
	 * @Title: xdAchieveDetailGroupByBusinessSheetBuilder
	 * @param @param sheet
	 * @param @param list
	 * @param @throws Exception
	 * @return void
	 * @throws
	 */
	private void xdAchieveDetailGroupByBusinessSheetBuilder(Sheet sheet, List<ClAchieveDetail> list) throws Exception {
		if (sheet == null)
			return;
		Workbook wb = sheet.getWorkbook();
		Cell cell = null;
		Row row = null;
		int index = 0;

		// 表头1
		String[] titleLine1 = { "序号", "分公司", "", "业务经理", "", "合同额", "放款额", "工资年月" };
		int[] titleWidths = { 6, 9, 13, 16, 13, 9, 9, 9 };
		final int ratio = 273;// 宽度调整系数
		Font font = wb.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		font.setFontHeightInPoints((short) 11);
		CellStyle titleStyle = wb.createCellStyle();
		titleStyle.setFont(font);
		titleStyle.setBorderLeft((short) 1);
		titleStyle.setBorderRight((short) 1);
		titleStyle.setBorderTop((short) 1);
		titleStyle.setBorderBottom((short) 1);
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		titleStyle.setWrapText(true);
		titleStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		row = sheet.createRow(index++);
		for (int i = 0, len = titleLine1.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine1[i]);
			cell.setCellStyle(titleStyle);
			sheet.setColumnWidth(i, titleWidths[i] * ratio);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 4));

		// 表头2
		String[] titleLine2 = { "", "编号", "名称", "编号", "姓名", "", "", "" };
		row = sheet.createRow(index++);
		int i = 0;
		int len = 0;
		for (i = 0, len = titleLine2.length; i < len; i++) {
			cell = row.createCell(i);
			cell.setCellValue(titleLine2[i]);
			cell.setCellStyle(titleStyle);
		}
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 6, 6));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 7, 7));

		// 冻结表头
		sheet.createFreezePane(0, 2);

		// 写入数据
		Font dataFont = wb.createFont();
		dataFont.setFontName(HSSFFont.FONT_ARIAL);
		dataFont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		dataFont.setFontHeightInPoints((short) 10);
		CellStyle dataStyle = wb.createCellStyle();
		dataStyle.setFont(dataFont);
		dataStyle.setBorderLeft((short) 1);
		dataStyle.setBorderRight((short) 1);
		dataStyle.setBorderTop((short) 1);
		dataStyle.setBorderBottom((short) 1);
		dataStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		dataStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		dataStyle.setWrapText(true);
		if (list != null && list.size() > 0)
			for (ClAchieveDetail detail : list) {
				row = sheet.createRow(index++);
				int colIndex = 0;
				cell = row.createCell(colIndex++);
				cell.setCellValue(index - 2);
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getOrgName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getBusinessManagerNo());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getBusinessManagerName());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				if (detail.getGrantLoanAmount() == null)
					cell.setCellValue("");
				else
					cell.setCellValue(detail.getGrantLoanAmount().doubleValue());
				cell.setCellStyle(dataStyle);
				cell = row.createCell(colIndex++);
				cell.setCellValue(detail.getGzym());
				cell.setCellStyle(dataStyle);
			}
	}

	@Override
	public ClAchieveDetail queryClAchieveDetailByPriNumber(Long priNumber) throws Exception {
		if (priNumber == null)
			return null;

		ClAchieveDetail params = new ClAchieveDetail();
		params.setPriNumber(priNumber);
		List<ClAchieveDetail> details = clAchieveDetailMapper.queryByConditions(params);
		//添家数据到details
		String empNo = details.get(0).getBusinessManagerNo();
		HqclcfEmp emp = hqclcfEmpMapper.queryEmpNameByNo(empNo);
		  if (emp!=null) {
			  details.get(0).setBusinessManagerName(emp.getEmpName());
		  
		  //获取团队经理编号和姓名(Pid)
		  HqclcfDept dept = hqclcfDeptMapper.queryDeptByCode(emp.getDeptNo());
		  
		  Long pid = dept.getPid();
		  HqclcfEmp emp2 = hqclcfEmpMapper.queryEmpClAchieveDetailDept(String.valueOf(pid));
		  //将团队经理信息添加到业绩明细
		  if (emp2!=null) {
			  details.get(0).setTeamManagerNo(emp2.getEmpNo());
			  details.get(0).setTeamManagerName(emp2.getEmpName());
			
			  //获取营业部经理的编号和姓名
			  HqclcfDept dept2 = hqclcfDeptMapper.queryDeptByCode(emp2.getDeptNo());
			  Long pid2 = dept2.getPid();
			  HqclcfEmp emp3 = hqclcfEmpMapper.queryEmpClAchieveDetailDept(String.valueOf(pid2));
			  //将营业部经理信息添加到业绩明细
			  if (emp3!=null) {
				  details.get(0).setOrgManagerNo(emp3.getEmpNo());
				  details.get(0).setOrgManagerName(emp3.getEmpName());
				  
				  //获取区域经理编号和姓名
				  HqclcfDept dept3 = hqclcfDeptMapper.queryDeptByCode(emp3.getDeptNo());
				  Long pid3 = dept3.getPid();
				  HqclcfEmp emp4 = hqclcfEmpMapper.queryEmpClAchieveDetailDept(String.valueOf(pid3));
				  if (emp4!=null) {
					  details.get(0).setAreaManagerNo(emp4.getEmpNo());
					  details.get(0).setAreaManagerName(emp4.getEmpName());
				   }
			    }	  
			}
		}
		  //获取大区，部门，分公司，团队的编号和名称
		  String deptInfo = details.get(0).getDeptInfo();
		  String s = deptInfo.replace(",","=");
		  String[] splitInfo = s.split("=");
		  //团队
		  details.get(0).setTeamNo(splitInfo[1]);
		  details.get(0).setTeamName(splitInfo[2]);
		  //营业部
		  details.get(0).setDeptNo(splitInfo[4]);
		  details.get(0).setDeptName(splitInfo[5]);
		  //分公司
		  details.get(0).setOrgNo(splitInfo[7]);
		  details.get(0).setOrgName(splitInfo[8]);
		  //大区
		  details.get(0).setAreaNo(splitInfo[10]);
		  details.get(0).setAreaName(splitInfo[11]);
		  
		  
		  
		if (details == null || details.size() <= 0)
			return null;
		else
			return details.get(0);
	}

	@Override
	public List<Map<String, Object>> querySalaryEmp() throws Exception {
		return hqclcfEmpMapper.querySalaryEmp();
	}

	@Override
	public List<Map<String, Object>> queryTeamManagerEmp() {
		return hqclcfEmpMapper.queryTeamManagerEmp();
	}

	@Override
	public List<Map<String, Object>> queryOrgManagerEmp() {
		return hqclcfEmpMapper.queryOrgManagerEmp();
	}

	@Override
	public List<Map<String, Object>> queryAreaManagerEmp() {
		return hqclcfEmpMapper.queryAreaManagerEmp();
	}

	@Override
	public List<Map<String, Object>> queryArea() {
		return hqclcfDeptMapper.queryArea();
	}

	@Override
	public List<Map<String, Object>> queryOrg(@Param("q") String deptCode) {
		return hqclcfDeptMapper.queryOrg(deptCode);
	}

	@Override
	public List<Map<String, Object>> queryDept(@Param("deptCode") String deptCode) {
		return hqclcfDeptMapper.queryDept(deptCode);
	}

	@Override
	public List<Map<String, Object>> queryTeam(@Param("deptCode") String deptCode) {
		return hqclcfDeptMapper.queryTeam(deptCode);
	}
}
