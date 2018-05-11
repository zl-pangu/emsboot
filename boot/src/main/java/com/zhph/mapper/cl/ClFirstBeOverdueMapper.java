package com.zhph.mapper.cl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zhph.model.cl.ClFirstBeOverdue;
import com.zhph.model.cl.ClOrgTask;

@Repository
public interface ClFirstBeOverdueMapper {

	
	
	List<ClFirstBeOverdue> queryAllFirstBeOverdueExl(ClFirstBeOverdue params);
	List<ClFirstBeOverdue> queryAllFirstBeOverdue(ClFirstBeOverdue params);

	List<Map<String, String>> queryList(ClFirstBeOverdue params);
	
	/**
	 * 根据工资年月查询首逾明细
	 * 
	 * @param gzym
	 * @return
	 * @throws Exception
	 */
	public List<ClFirstBeOverdue> queryClFirstBeOverdue(String gzym) throws Exception;

	void insertSomething(List<ClFirstBeOverdue> item);
}
