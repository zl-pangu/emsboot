package com.zhph.mapper.sys;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhph.model.sys.SysHoliday;

/**
 * @author lxp
 * @date 2017年12月19日 下午5:26:22
 * @parameter
 * @return
 */

public interface SysHolidayMapper {

	public List<SysHoliday> queryAllHoliday(SysHoliday holiday);

	public List<SysHoliday> queryHolidayById(@Param("id") String id);

	public void insertData(SysHoliday holiday);

	public void updateById(SysHoliday holiday);

	public void delById(@Param("id") String id);

	public List<SysHoliday> checkIfExsits(SysHoliday holiday);
}
