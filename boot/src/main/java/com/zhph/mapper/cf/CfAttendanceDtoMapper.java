package com.zhph.mapper.cf;

import com.zhph.model.cf.dto.CfAttendanceDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Create By lishuangjiang
 */
@Repository
public interface CfAttendanceDtoMapper {

    /**
     * 部门dto
     * @param empNo
     * @return
     */
    CfAttendanceDto queryDeptDto(@Param("empNo") String empNo);



}
