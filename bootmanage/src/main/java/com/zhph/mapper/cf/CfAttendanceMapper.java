package com.zhph.mapper.cf;

import com.zhph.model.cf.CfAttendance;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Create By lishuangjiang
 */

@Repository
public interface CfAttendanceMapper {

    /**
     * 条件获取对象信息
     * @param params
     * @return
     */
    List<CfAttendance> queryAttendanceBygzym(CfAttendance params);

    /**
     * 批量生成考勤统计表
     * @param cfAttendance
     * @return
     */
    int insertCfAttendanceBatch(CfAttendance cfAttendance);

    /**
     * 删除指定工资年月的考勤统计
     * @param empNo
     * @param gzym
     * @return
     */
    int deleteCfAttendanceBatch(@Param("empNo") String empNo , @Param("gzym") String gzym);


    /**
     * 查询返回map结果集
     * @param cfAttendance
     * @return
     */
    List<Map<String,String>> queryList(CfAttendance cfAttendance);













}
