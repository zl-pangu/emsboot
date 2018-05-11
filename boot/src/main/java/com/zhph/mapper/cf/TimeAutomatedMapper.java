package com.zhph.mapper.cf;

import com.zhph.model.cf.TimeAutomated;
import com.zhph.model.cf.TimeAutomatedBak;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Create By lishuangjiang
 */
@Repository
public interface TimeAutomatedMapper {

    List<TimeAutomated> queryAutoMatedByGzym(@Param("value") String value);

	List<TimeAutomated> page(TimeAutomatedBak xjTimeAutomatedBak);

	int edit(TimeAutomated timeAutomated);

	int add(TimeAutomated timeAutomated);

	int deleteByEmpnoAndGzym(Map<String, Object> map);

	int deleteByEmpnosAndGzym(Map<String, Object> map);
	
	List<TimeAutomated> queryByEmpnoAndGzym(Map<String, Object> map);

	TimeAutomated queryByEmpNoAndGzym(Map<String,String> map);


}
