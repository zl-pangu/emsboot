
package com.zhph.mapper.hqclcf;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfJobTransfer;
import com.zhph.model.hqclcf.HqclcfLeave;

@Repository
public interface HqclcfEmpMapper {

	/**
	 * 查询所有审批通过员工（含在职，离职，停薪停职，停薪留职）
	 *
	 * @param params
	 * @return
	 */
	List<HqclcfEmp> queryAllEmp(HqclcfEmp params);

	/**
	 * 更新员工表信息
	 *
	 * @param params
	 */
	int updateById(HqclcfEmp params);

	/**
	 * 离职淘汰修改员工信息
	 *
	 * @param params
	 */
	void updateEmpById(HqclcfLeave params);

	/**
	 * 员工状态变更
	 * 
	 * @param emp
	 * @return
	 */
	int updateEmpStatusByParams(HqclcfEmp emp);

	/**
	 * 离职转在职修改员工信息
	 *
	 * @param params
	 */
	void updateJobById(HqclcfJobTransfer params);

	/**
	 * 离职淘汰新增根据员工 员工姓名”（只能输入在职员工）选择员工， 可自动带出员工编号、部门、 业务条线、职务、职级、岗位、入职时间和员工主体
	 *
	 * @param empNo
	 * @param empName
	 * @return
	 */
	HashMap<String, Object> leaveEmpByEmpNameOrNo(@Param("empName") String empName, @Param("empNo") String empNo) throws SQLException;

	/**
	 * 离职转在职 员工姓名查询，查询范围为离职员工），则自动带出员工姓名、原部门 (显示该部门的所有上级部门如：总部职能部-行政管理部)、原业务条线、
	 * 原职位、原职务、原职级、原入职日期
	 *
	 * @param empNo
	 * @return
	 */
	HashMap<String, Object> jobTransferEmpByEmpNameOrNo(@Param("empName") String empName, @Param("empNo") String empNo) throws SQLException;

	/**
	 * 根据银行卡查询员工信息
	 *
	 * @param 
	 * @return
	 */
	int empByBankCode(String pfBankCode);

	/**
	 * 根据员工编号查询在职人员信息
	 *
	 * @param empNo
	 * @return
	 */
	public HqclcfEmp queryEmpByEmpNo(String empNo);

	/**
	 * 通过员工编码或者姓名模糊查询empInfo
	 *
	 * @param searchValue
	 * @return
	 */
	public List<HqclcfEmp> queryEmpByq(@Param("searchValue") String searchValue, @Param("isBl") String isBl, @Param("loginUserId") List<Integer> loginUserId);

	/**
	 * 通过员工编码获取指定人员(包括不在职)
	 *
	 * @param empNo
	 * @return
	 */
	public HqclcfEmp queryEmpNameByNo(@Param("empNo") String empNo);

	/**
	 * 通过ID获取员工信息
	 *
	 * @param id
	 * @return
	 */
	public List<HqclcfEmp> queryDataById(@Param("id") String id);

	public List<HqclcfEmp> getEmpListByType(@Param("userId") String userId, @Param("q") String q);

	public void updateEmpSubjectByEmpNo(@Param("empSubject") String empSubject, @Param("empNo") String empNo);

	/**
	 * 获取对应部门员工的编制数
	 * 
	 * @param deptNo
	 * @return
	 */
	public int queryEmpOrganizatDept(@Param("deptNo") String deptNo);
	
	/**
	 * 获取对应部门某职位经理的姓名和编号
	 * 
	 * @param deptNo
	 * @return
	 */
	public HqclcfEmp queryEmpClAchieveDetailDept(@Param("deptNo") String deptNo);

	/**
	 * 获取对应岗位员工的编制数
	 * 
	 * @param postNo
	 * @return
	 */
	public int queryEmpOrganizatPost(@Param("postNo") String postNo);

	/**
	 * 无状态获取员工
	 * 
	 * @param emp
	 * @return
	 */
	List<HqclcfEmp> queryAllWithoutStatus(HqclcfEmp emp);

	List<HqclcfEmp> queryEmpByMap(Map<String, Object> map);

	/**
	 * 导出查询
	 * 
	 * @param hqclcfEmp
	 * @return
	 */
	public List<HqclcfEmp> queryAllEmpForExport(HqclcfEmp hqclcfEmp);

	public void AutoTfEmpType();

	/**
	 * 排班员工查询
	 * 
	 * @param params
	 * @return
	 */
	List<HqclcfEmp> queryForScheduling(HqclcfEmp params);

	List<HqclcfEmp> queryForScheduling(Map<String, Object> params);

	List<Map<String,String>> queryEmpByq1(HashMap<String, String> queryMap);

	List<Map<String, Object>> queryEmpByq2(@Param("searchValue") String searchValue, @Param("isBl") String isBl, @Param("loginUserId") List<Integer> loginUserId);

	/**
	 * 按照工资年月备份员工信息表
	 * 
	 * @param gzym
	 * @return
	 */
	public int bakupHqclcfEmpForPerGzym(String gzym);

	/**
	 * 按照工资年月删除备份员工表的信息
	 * 
	 * @param gzym
	 * @return
	 */
	public int deleteHqclcfEmpBakByGzym(String gzym);

	/**
	 * 获取信贷业务经理职位的所有员工
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> querySalaryEmp();
	
	/**
	 * 获取信贷团队经理职位的所有员工
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryTeamManagerEmp();
	
	/**
	 * 获取信贷营业部经理职位的所有员工
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryOrgManagerEmp();
	
	/**
	 * 获取信贷区域经理职位的所有员工
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryAreaManagerEmp();
}
