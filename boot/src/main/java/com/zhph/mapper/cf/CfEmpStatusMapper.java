package com.zhph.mapper.cf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zhph.model.cf.CfEmpStatus;

/**
 * Create By lishuangjiang 消分员工状态变更
 */
@Repository
public interface CfEmpStatusMapper {

	/**
	 * 查询所有员工状态变更记录
	 * 
	 * @param empStatus
	 * @return
	 */
	List<CfEmpStatus> queryAllEmpStatuses(CfEmpStatus empStatus);

	/**
	 * 通过员工编码查询员工状态变更记录
	 * 
	 * @param empNo
	 * @return
	 */
	CfEmpStatus queryEmpByEmpNo(@Param("empNo") String empNo);

	/**
	 * 选择性插入
	 * 
	 * @param empStatus
	 * @return
	 */
	int insertSelective(CfEmpStatus empStatus);

	/**
	 * 整体插入
	 * 
	 * @param empStatus
	 * @return
	 */
	int insert(CfEmpStatus empStatus);

	/**
	 * 通过主键标识查询员工状态变更记录
	 * 
	 * @param id
	 * @return
	 */
	CfEmpStatus queryEmpByPriMarkey(@Param("id") Long id);

	/**
	 * 修改
	 * 
	 * @param empStatus
	 * @return
	 */
	int updateEmpStatuses(CfEmpStatus empStatus);

	/**
	 * 员工编码删除
	 * 
	 * @param empNo
	 * @return
	 */
	int delEmpStatusesByEmpNo(@Param("empNo") String empNo);

	/**
	 * 主键标识删除
	 * 
	 * @param id
	 * @return
	 */
	int delEmpStatusesById(@Param("id") Long id);

	/**
	 * 批量插入
	 * 
	 * @param list
	 */
	int batchInsertList(List<CfEmpStatus> list);

	/**
	 * map 查询
	 * 
	 * @param queryMap
	 * @return
	 */
	List<Map<String, String>> queryEmpByQAndGetData(HashMap<String, String> queryMap);

	/**
	 * 查询返回map结果集
	 * 
	 * @param cfEmpStatus
	 * @return
	 */
	List<Map<String, String>> queryList(CfEmpStatus cfEmpStatus);

	/**
	 * 检测员工到期状态变更procedure
	 */
	void autoEmpStatus();

	List<CfEmpStatus> queryCfEmpStatusByEmpNo(@Param("empNo") String empNo);

}
