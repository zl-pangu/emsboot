package com.zhph.service.sys;

import java.util.List;
import java.util.Map;

import com.zhph.model.sys.SysCalendarPool;
import com.zhph.util.Json;

/**
 * 系统日历表(用于工作日/非工作日数据查询/保存)Service接口
 * @author roilat-D
 *
 */
public interface SysCalendarPoolService {
	
	/**
	 * 根据条件查询日历时间列表
	 * @return
	 */
	public List<SysCalendarPool> findByCondtion(Map<String,Object> condition,Boolean refresh) throws Exception;
	
	/**
	 * 更新日历时间列表
	 * @return
	 */
	public Json refreshPool(Map<String, Object> condition) throws Exception;
	
	/**
	 * 批量插入
	 * @param calendarPools
	 * @return
	 * @throws Exception
	 */
	public int saveList(List<SysCalendarPool> calendarPools) throws Exception;
	
	/**
	 * 按条件删除数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int deleteByCondtion(Map<String,Object> map) throws Exception;
	
	/**
	 * 判断是否是当月最后一个工作日
	 * @param Date
	 * @return
	 * @throws Exception
	 */
	public boolean ifLastWorkDay(String date) throws Exception;

}
