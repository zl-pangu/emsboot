package com.zhph.service.demo;

import com.github.pagehelper.Page;
import com.zhph.model.SalaryP2pEmp;

public interface SalaryP2pEmpService {

	
	public Page<SalaryP2pEmp> getAll(Integer pageSize, Integer curPage);
	
	
	public void insert(SalaryP2pEmp salaryP2pEmp);

}
