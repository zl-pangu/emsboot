package com.zhph.service.sys;

import java.util.List;

import com.zhph.model.sys.SysConfigType;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;


public interface SysConfigTypeService {
	
	
	/**
	 * 
	 * @Title getConfigTypesByPSysCode
	 * @Description TODO 根据父级编码 查询旗下所有子级
	 * @param  pSysCode
	 * @param     参数
	 * @return List<SysConfigType>    返回类型
	 *
	 */
	public List<SysConfigType> getConfigTypesByPSysCode(String pSysCode);
	
	
	/**
	 * 
	 * @Title getConfigValueByCodeAndPSysCode
	 * @Description TODO 根据父级编码 和其子级名称 查询 子级所对应的值
	 * @param  pSysCode
	 * @param  sysName
	 * @param     参数
	 * @return Integer    返回类型
	 *
	 */
	public Integer getConfigValueByCodeAndPSysCode(String pSysCode,String sysName);
	
	
	/**
	 * 
	 * @Title getCofigNameByCodeAndPSysCode
	 * @Description TODO 根据父级编码 和其子级值 查询 子级所对应的名字
	 * @param @param pSysCode
	 * @param @param sysValue
	 * @param @return    参数
	 * @return String    返回类型
	 *
	 */
	public String getCofigNameByCodeAndPSysCode(String pSysCode,int sysValue);

	Grid<SysConfigType> queryPageInfo(PageBean pageBean, SysConfigType configType) throws Exception;

	List<SysConfigType> queryPConfigType(String q) throws Exception;

	int queryOrder(String sysCode) throws Exception;

	String dictionaryCoding(String sysName, String pCode) throws Exception;

	void add(String data) throws Exception;

    void del(Long id) throws Exception;

	SysConfigType queryObjById(Long id) throws Exception;

    void update(String data) throws Exception;
}
