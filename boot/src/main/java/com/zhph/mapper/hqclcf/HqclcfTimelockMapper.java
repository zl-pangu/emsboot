package com.zhph.mapper.hqclcf;

import com.zhph.mapper.cf.CfTimeLockMapper;
import com.zhph.model.hqclcf.HqclcfTimelock;
import java.util.List;

/**
 * 消金考勤编辑锁Mapper
 * @author roilat-D
 * {@link CfTimeLockMapper}
 */
@Deprecated
public interface HqclcfTimelockMapper {

	/**
	 * 按条件查询
	 * @param timelock
	 * @return
	 */
	List<HqclcfTimelock> queryByCondition(HqclcfTimelock timelock);

}