package com.zhph.service.hqclcf.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhph.mapper.cf.CfTimeLockMapper;
import com.zhph.mapper.hqclcf.HqclcfTimelockMapper;
import com.zhph.model.hqclcf.HqclcfTimelock;
import com.zhph.service.hqclcf.HqclcfTimelockService;

/**
 * 消金考勤编辑锁Service实现
 * @author roilat-D
 * {@link CfTimeLockMapper}
 *  */
@Service
@Transactional(rollbackFor=Exception.class)
@Deprecated
public class HqclcfTimelockServiceImpl implements HqclcfTimelockService{

	@Resource
	private HqclcfTimelockMapper hqclcfTimelockMapper;
	
	public static final Logger logger = LogManager.getLogger(HqclcfTimelockServiceImpl.class);

	@Override
	public List<HqclcfTimelock> queryByCondition(HqclcfTimelock timelock) throws Exception {
		return hqclcfTimelockMapper.queryByCondition(timelock);
	}

}
