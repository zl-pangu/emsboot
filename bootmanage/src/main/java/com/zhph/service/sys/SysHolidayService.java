package com.zhph.service.sys;

import java.util.List;

/** 
 * @author lxp 
 * @date 2017年12月19日 下午5:26:22 
 * @parameter  
 * @return  
 */

import com.zhph.model.sys.SysHoliday;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

public interface SysHolidayService {

	// 查询列表
	public Grid<SysHoliday> queryPageInfo(PageBean pageBean, SysHoliday holiday) throws Exception;

	// 查询单条
	public SysHoliday gotoEditById(String Id) throws Exception;

	// 保存
	public Json saveData(String cmd, SysHoliday holiday) throws Exception;

	// 删除
	public Json delData(String id) throws Exception;

	// 自动生成
	public Json autoHoliday(String year) throws Exception;

	public List<SysHoliday> queryAll(SysHoliday holiday) throws Exception;

}
