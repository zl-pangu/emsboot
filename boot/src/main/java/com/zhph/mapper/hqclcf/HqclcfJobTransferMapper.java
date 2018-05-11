package com.zhph.mapper.hqclcf;

import java.util.List;

import com.zhph.model.hqclcf.HqclcfJobTransfer;

public interface HqclcfJobTransferMapper {

	List<HqclcfJobTransfer> queryAllJobTransfer(HqclcfJobTransfer params);

	void addById(HqclcfJobTransfer readValue);

	void updateById(HqclcfJobTransfer params);

	HqclcfJobTransfer checkIsExsit(HqclcfJobTransfer params);
	
	public List<HqclcfJobTransfer> queryAllDept(HqclcfJobTransfer params);
	
	List<HqclcfJobTransfer> queryOldDeptJobTransfer(HqclcfJobTransfer params);
	
	
}
