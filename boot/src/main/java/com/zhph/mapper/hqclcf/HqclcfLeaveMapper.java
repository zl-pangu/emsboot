package com.zhph.mapper.hqclcf;

import com.zhph.model.hqclcf.HqclcfLeave;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface HqclcfLeaveMapper {

	List<HqclcfLeave> queryAllLeave(HqclcfLeave params);

	void delById(Long priNumber);

	void updateById(HqclcfLeave params);

	void appById(HqclcfLeave params);

	void addByObj(HqclcfLeave readValue);

	/**
	 * 根据员工编号和业务线查询
	 * @param empNo
	 * @param busLine
	 * @return
	 */
	List<Map<String,Object>> queryFileMap(@Param("empNo") String empNo,@Param("busLine") String busLine);
//	HqclcfLeave querySigleLeave(HqclcfLeave params);
	
	/**
     * 通过主键 标识查询对象
     * @param priNumber
     * @return
     */
	HqclcfLeave selectByPrimaryKey(@Param("priNumber") Long priNumber);
	
	HqclcfLeave selectByEmpNo(@Param("empNo") String empNo);
	
	int queryEmpByworkNo(@Param("leaveDate")  String leaveDate);
}
