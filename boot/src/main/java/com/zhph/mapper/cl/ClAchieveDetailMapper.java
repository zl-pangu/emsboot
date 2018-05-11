package com.zhph.mapper.cl;


import java.util.List;

import com.zhph.model.cl.ClAchieveDetail;

public interface ClAchieveDetailMapper {

	Integer deleteById(Long id);

    Integer insert(ClAchieveDetail record);

    Integer update(ClAchieveDetail record);

	List<ClAchieveDetail> queryByPage(ClAchieveDetail params);

	List<ClAchieveDetail> queryByConditions(ClAchieveDetail params);
	
	Integer queryPageCount(ClAchieveDetail params);

	Integer deleteByGzym(String gzym);

	ClAchieveDetail queryById(Long priNumber);

	List<ClAchieveDetail> queryClAchieveDetailsFromBusinessDb(String gzym);
    
}