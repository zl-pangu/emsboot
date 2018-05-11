package com.zhph.mapper.cf;

import com.zhph.model.cf.DaysNode;
import org.springframework.stereotype.Repository;

@Repository
public interface DaysNodeMapper {

	/**
	 * 保存数据
	 */
	public int save( DaysNode daysNode);
	
	/**
	 * 根据keyId（即请休假ID）
	 */
	public int deleteByKeyId(Long keyId);
}