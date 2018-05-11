package com.zhph.mapper.sys;

import java.util.List;
import java.util.Map;

import com.zhph.model.sys.SysCalendarPool;

public interface SysCalendarPoolMapper {
	
	public int insert(SysCalendarPool sysCalendarPool );
	public int update(SysCalendarPool sysCalendarPool );
	public int deleteByCondition(Map<String,Object> map );
	public List<SysCalendarPool> queryByCondtion(Map<String,Object> map );
	@Deprecated//<!-- 新增的方法，未经过测试，暂无用处 -->
	public List<SysCalendarPool> findLastWorkDays(Map<String, Object> map);
	@Deprecated//<!-- 新增的方法，未经过测试，暂无用处 -->
	public int markAsLastWorkDay(Map<String, Object> map);

}
