package com.zhph.mapper.cf;

import com.zhph.model.cf.CfOfflaterSet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface CfOfflaterSetMapper {

	// 查询列表
	public List<CfOfflaterSet> queryAllData(CfOfflaterSet offlaterSet);

	// 根据ID查询单条
	public List<CfOfflaterSet> queryDataById(@Param("id") String id);

	// 新增数据
	public void insertData(CfOfflaterSet holiday);

	// 更新数据
	public void updateById(CfOfflaterSet holiday);

	// 删除数据
	public void delById(@Param("id") String id);

	// 检出是否已经填写
	public List<CfOfflaterSet> ifCheckIn(CfOfflaterSet offlaterSet);

	public String checkWorkDayAndWeekDay(Map<String, String> params);

	// 查询指定工资年月记录信息
	public CfOfflaterSet queryDataByGzym(@Param("gzym") String gzym);

}