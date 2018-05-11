package com.zhph.mapper.demo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhph.model.SalaryP2pEmp;

public interface SalaryP2pEmpMapper {
	
	public List<SalaryP2pEmp> getAll();
	
	
	public void insert(@Param("t")SalaryP2pEmp salaryP2pEmp);
	
}
