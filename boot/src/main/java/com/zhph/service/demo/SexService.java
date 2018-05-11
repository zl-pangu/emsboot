package com.zhph.service.demo;

import java.util.List;

import com.zhph.model.Sex;

public interface SexService {

	
	public List<Sex> getAll();
	
	public Sex findById(int id);
	
	public  void test();
}
