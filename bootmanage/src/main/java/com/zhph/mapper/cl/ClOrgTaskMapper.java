package com.zhph.mapper.cl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zhph.model.cl.ClOrgTask;
@Repository
public interface ClOrgTaskMapper {

	List<ClOrgTask> queryAllOrgTask(ClOrgTask params);
	
	void insertSomething(List<ClOrgTask> item);
	
	List<Map<String,String>> queryList(ClOrgTask clOrgTask);
	
	void delByGzYm(String gzYm);

}
