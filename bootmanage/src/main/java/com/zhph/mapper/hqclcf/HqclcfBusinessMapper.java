package com.zhph.mapper.hqclcf;

import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfRank;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HqclcfBusinessMapper {

    List<HqclcfRank> selectRankList();

    int deleteByPrimaryKey(String prinumber);

    int insert(HqclcfBusiness hqclcfBusiness);

    int insertSelective(HqclcfBusiness hqclcfBusiness);

    int updateByPrimaryKeySelective(HqclcfBusiness hqclcfBusiness);

    int updateByPrimaryKey(HqclcfBusiness hqclcfBusiness);
    public List<HqclcfBusiness> getBusinessByCondition(HqclcfBusiness hqclcfBusiness);


    /**
     * 根据岗位ID和岗位所属的部门查询这些岗位包含的职务
     * @param deptId
     * @param postId
     * @return
     */
    List<HqclcfBusiness>  queryBusinessByDeptIdAndPost(@Param("deptId")Long deptId, @Param("postId")Long postId);

    /**
     * 通过职务编码查询职务
     * @param posCode
     * @return
     */
    HqclcfBusiness queryBusinessByCode(@Param("posCode")String posCode);

    /**
     * 通过主键标识获取职务信息
     *
     * @param priNumber
     * @return
     */
    public HqclcfBusiness queryBusinessByPrimaryKey(@Param("priNumber") String priNumber) throws Exception;

    /**
     * 准确检测职务名是否被使用
     * @param hqclcfBusiness
     * @return
     */
    public List<HqclcfBusiness> checkName(HqclcfBusiness hqclcfBusiness);
}