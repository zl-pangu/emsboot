package com.zhph.mapper.sys;

import java.util.List;

import com.zhph.model.sys.dto.SysConfigAddParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zhph.model.sys.SysConfigType;

@Repository
public interface SysConfigTypeMapper {

	public List<SysConfigType> getConfigByPSysCode(@Param("pSysCode") String pSysCode);

	public List<SysConfigType> getSysConfigTypeList();

	SysConfigType querySingleBySysVal(@Param("sysVal") String sysVal, @Param("p_sys_code") String p_sys_code);

	List<SysConfigType> queryList(SysConfigType configType);

	List<SysConfigType> queryPConfigType(@Param("q") String q);

	Integer querySortBypCode(SysConfigType configType);

	int queryDuplication(SysConfigType configType);

	void addSysType(SysConfigType configType);

	SysConfigType queryConfigTypeById(@Param("id") Long id);

	void del(@Param("id")Long id);

	void update(SysConfigType configType);
}
