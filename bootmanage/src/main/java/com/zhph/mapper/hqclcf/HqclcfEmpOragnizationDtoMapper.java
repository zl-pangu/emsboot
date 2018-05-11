package com.zhph.mapper.hqclcf;

import com.zhph.model.hqclcf.dto.HqclcfEmpOragnizationDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Create By lishuangjiang
 * 人员异动编制排查
 */
@Repository
public interface HqclcfEmpOragnizationDtoMapper {

    /**
     * 查询岗位编制
     * @param postNo
     * @param empNo
     * @return
     */
    HqclcfEmpOragnizationDto queryEmpOrganizationPost(@Param("postNo") String postNo,@Param("empNo") String empNo);

    /**
     * 查询部门编制
     * @param deptNo
     * @param empNo
     * @return
     */
    HqclcfEmpOragnizationDto queryEmpOrganizationDept(@Param("deptNo") String deptNo,@Param("empNo") String empNo);

}
