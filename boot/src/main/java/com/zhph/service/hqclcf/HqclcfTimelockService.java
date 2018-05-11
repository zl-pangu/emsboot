package com.zhph.service.hqclcf;

import java.util.List;


import com.zhph.mapper.cf.CfTimeLockMapper;
import com.zhph.model.hqclcf.HqclcfTimelock;

/**
 * 消金考勤编辑锁Service接口
 * @author roilat-D
 * {@link CfTimeLockMapper}
 */
@Deprecated
public interface HqclcfTimelockService {

	List<HqclcfTimelock> queryByCondition(HqclcfTimelock timelock) throws Exception;
	
}
