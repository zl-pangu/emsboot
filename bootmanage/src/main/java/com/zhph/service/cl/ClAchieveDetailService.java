package com.zhph.service.cl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;

import com.zhph.model.cl.ClAchieveDetail;
import com.zhph.model.sys.SysUser;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;


public interface ClAchieveDetailService {

	public Grid<ClAchieveDetail> queryClAchieveDetailGrid(ClAchieveDetail params, PageBean pageBean) throws Exception;

	/**
	 * 业绩明细分页查询
	 */
	public Grid<ClAchieveDetail> queryClAchieveDetailGrid(ClAchieveDetail params) throws Exception;

	/**
	 * 根据一个或多个条件查询业绩明细总数
	 */
	public Integer queryClAchieveDetailsCount(ClAchieveDetail params) throws Exception;

	/**
	 * 根据工资年月进行同步
	 */
	public List<ClAchieveDetail> syncClAchieveDetail(String gzYm, SysUser sysUser) throws Exception;

	/**
	 * 业绩明细不分页查询
	 */
	public List<ClAchieveDetail> queryClAchieveDetailList(ClAchieveDetail params) throws Exception;

	/**
	 * 删除一个业绩明细
	 */
	public ClAchieveDetail deleteClAchieveDetail(ClAchieveDetail params, SysUser sysUser) throws Exception;

	/**
	 * 保存修改的业绩明细
	 */
	public ClAchieveDetail updateClAchieveDetail(ClAchieveDetail queryParams, ClAchieveDetail modifyParams, SysUser sysUser) throws Exception;

	/**
	 * 保存批量修改的业绩明细
	 */
	public void batchUpdateClAchieveDetail(List<Long> priNumberList, List<String> selNoList, ClAchieveDetail modifyParams, SysUser sysUser) throws Exception;

	/**
	 * 导出业绩明细-excel处理
	 */
	public <T> void exportClAchieveDetail(HttpServletResponse res, ClAchieveDetail params, Class<T> clz) throws Exception;

	/**
	 * 根据主键序列查询业绩明细
	 */
	public ClAchieveDetail queryClAchieveDetailByPriNumber(Long priNumber) throws Exception;
	
	/**
	 * 获取信贷员工职务业务经理的信息
	 */
	List<Map<String, Object>> querySalaryEmp() throws Exception;
	/**
	 * 获取信贷员工职务团队经理的信息
	 */
    List<Map<String, Object>> queryTeamManagerEmp();
	
    /**
	 * 获取信贷员工职务营业部经理的信息
	 */
	List<Map<String, Object>> queryOrgManagerEmp();
	
	/**
	 * 获取信贷员工职务区域经理的信息
	 */
	List<Map<String, Object>> queryAreaManagerEmp();
	
	
	/**
	 * 获取信贷区域
	 * 
	 * @param gzym
	 * @return
	 */
    List<Map<String, Object>> queryArea();
	
	/**
	 * 获取信贷分公司
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryOrg(@Param("deptCode") String deptCode);
	
	/**
	 * 获取信贷部门
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryDept(@Param("deptCode") String deptCode);
	
	/**
	 * 获取信贷团队
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryTeam(@Param("deptCode") String deptCode);
}
