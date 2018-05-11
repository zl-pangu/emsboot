package com.zhph.service.hqclcf;

import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.vo.ResultVo;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

import java.util.List;

public interface HqclcfRankService {
	/**
	 * 新增
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public ResultVo insert(HqclcfRank record) throws Exception;

	/**
	 * check rankName is Used !
	 * @param rankName
	 * @return
	 * @throws Exception
	 */
	public ResultVo checkRankName(String rankName,String no) throws Exception;

	/**
	 * 删除
	 * 
	 * @param hqclcfRank
	 * @return
	 * @throws Exception
	 */
	public ResultVo deleteByPrimaryKey(HqclcfRank hqclcfRank) throws Exception;

	/**
	 * 条件分页查询
	 * 
	 * @param pageBean
	 * @param hqclcfRank
	 * @return
	 * @throws Exception
	 */
	public Grid<HqclcfRank> getRankByCondition(PageBean pageBean, HqclcfRank hqclcfRank) throws Exception;

	/**
	 * 
	 * @param record
	 * @return
	 * @throws Exception
	 */
	public ResultVo updateByPrimaryKeySelective(HqclcfRank record) throws Exception;

	/**
	 * 通过UUID生成职级编号
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getRankNoIdByUUId() throws Exception;

	/**
	 * 按时间降序查询无条件查询所有职级信息
	 * 
	 * @return
	 */
	public List<HqclcfRank> getLatestRank() throws Exception;

	/**
	 * 
	 * @param no
	 * @return
	 * @throws Exception
	 */
	public HqclcfRank getRankByNo(String no) throws Exception;
}
