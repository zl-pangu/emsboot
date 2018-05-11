package com.zhph.mapper.demo;

import com.zhph.model.Sex;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SexMapper {

	public List<Sex> getAll();

	public Sex findById(@Param("id") Integer id);


}
