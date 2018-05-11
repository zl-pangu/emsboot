/*
 * CfPaidLeaveServiceImpl.java created on 2018-01-12 下午 14:15:42 by roilatD
 */
package com.zhph.service.cf.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.exception.AppException;
import com.zhph.mapper.cf.CfPaidLeaveMapper;
import com.zhph.model.cf.CfPaidLeave;
import com.zhph.service.cf.CfPaidLeaveService;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
 * TODO  该模块的功能待消除，需要删除对应的表和mapper.xml
 * 月调休天数配置表 Service 实现
 * @author: roilatD
 * @since: 2018-01-12
 */
@Service("cfPaidLeaveService")
@Deprecated
public class CfPaidLeaveServiceImpl implements CfPaidLeaveService {

	@Resource
	private CfPaidLeaveMapper cfPaidLeaveMapper; 
	@Override
	public Grid<CfPaidLeave> queryByPage(CfPaidLeave queryParams, PageBean pageBean) throws AppException {
		if(pageBean != null){
    		PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
    	}
        List<CfPaidLeave> list = cfPaidLeaveMapper.page(queryParams);
        PageInfo<CfPaidLeave> pageInfo=new PageInfo<>(list);
        Grid<CfPaidLeave> grid=new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
	}


}
