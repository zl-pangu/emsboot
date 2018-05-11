package com.zhph.mapper.hqclcf;

import com.zhph.model.hqclcf.HqclcfRank;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HqclcfRankMapper {
	/**
	 * 删除
	 * 
	 * @param prinumber
	 * @return
	 */
	int deleteByPrimaryKey(String prinumber);

	/**
	 * 插入
	 * 
	 * @param record
	 * @return
	 */
	int insert(HqclcfRank record);

	/**
	 * 选择性插入
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(HqclcfRank record);

	/**
	 * 通过主键标识查询对象信息
	 * 
	 * @param prinumber
	 * @return
	 */
	HqclcfRank selectByPrimaryKey(String prinumber);

	/**
	 * 选择性更新对象
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(HqclcfRank record);

	/**
	 * 通过主键标识更新对象
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKey(HqclcfRank record);

	/**
	 * 对象信息获取所有职级信息
	 * 
	 * @param hqclcfRank
	 * @return
	 */
	public List<HqclcfRank> getRankByCondition(HqclcfRank hqclcfRank);

	/**
	 * 通过职级编码获取所有对象信息
	 * 
	 * @param rankCode
	 * @return
	 */
	public List<HqclcfRank> getRankByRankCodes(@Param("rankCode") List<String> rankCode);

	/**
	 * 按时间降序查询无条件查询所有职级信息
	 * 
	 * @return
	 */
	public List<HqclcfRank> getLatestRank();

	public HqclcfRank getRankByNo(@Param("no") String no);

	/**
	 * check rankName is used !
	 * @param rank
	 * @return
	 */
	List<HqclcfRank> checkRankName(HqclcfRank rank);

}