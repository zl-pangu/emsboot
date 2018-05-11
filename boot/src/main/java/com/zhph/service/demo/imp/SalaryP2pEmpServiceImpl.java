package com.zhph.service.demo.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhph.mapper.demo.SalaryP2pEmpMapper;
import com.zhph.model.SalaryP2pEmp;
import com.zhph.service.demo.SalaryP2pEmpService;

@Service
@Transactional(rollbackFor=Exception.class)
public class SalaryP2pEmpServiceImpl implements SalaryP2pEmpService {
	
	@Autowired
	private SalaryP2pEmpMapper salaryP2pEmpDao;

	public Page<SalaryP2pEmp> getAll(Integer pageSize, Integer curPage) {
		PageHelper.startPage(curPage, pageSize);
		
		
		List<SalaryP2pEmp> dataList =salaryP2pEmpDao.getAll();
		Page<SalaryP2pEmp> page = (Page<SalaryP2pEmp>) dataList;
		return page;
		
	}

	public void insert(SalaryP2pEmp salaryP2pEmp) {
		salaryP2pEmpDao.insert(salaryP2pEmp);
	}

}
