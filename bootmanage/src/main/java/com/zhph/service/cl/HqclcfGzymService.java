package com.zhph.service.cl;

import java.util.List;

import com.zhph.model.hqclcf.HqclcfGzym;

public interface HqclcfGzymService {
	
	public List<HqclcfGzym> querySalaryGzym() throws Exception;
	public HqclcfGzym queryCurrGzym() throws Exception;
}
