package com.zhph.service.cl.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.zhph.mapper.cf.HqclcfGzymMapper;
import com.zhph.model.hqclcf.HqclcfGzym;
import com.zhph.service.cl.HqclcfGzymService;

@Service
public class HqclcfGzymServiceImpl implements HqclcfGzymService {

	@Resource
	private HqclcfGzymMapper hqclcfGzymMapper;


	public List<HqclcfGzym> querySalaryGzym() throws Exception {
		return hqclcfGzymMapper.queryXjGzym();
	}

	public HqclcfGzym queryCurrGzym() throws Exception {
		return hqclcfGzymMapper.queryCurrGzym();
	}
}
