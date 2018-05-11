package com.zhph.mapper.sys;

import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfPost;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/22.
 */
public interface SysDataMigratMapper {
/*    List<SysDataMigratDept> queryAll() throws Exception;

    SysDataMigratDept queryById(Long id) throws Exception;

    void addMiddleDept(SysMidlleDept sysMidlleDept);

    List<SysOldXjDept> queryAllXjdept() throws Exception;*/

    List<HqclcfDept> queryNewXjDept() throws Exception;
    List<HqclcfDept> queryXddeptbyparms() throws Exception;
    List<Map<String,Object>> queryxjpidMap() throws Exception;

    void updateXjdeptPid(HqclcfDept dept);

    List<Map<String,String>> queryXdArea() throws Exception;

    List<Map<String,String>> queryXdDqAndRegion() throws Exception;

    List<Map<String,String>> queryXdYyb2Org() throws Exception;

    List<Map<String,Object>> generatedXdTeam() throws Exception;

    List<Map<String,Object>> queryOldPost() throws Exception;

    List<Map<BigDecimal,BigDecimal>> queryPostIdWithOldId() throws Exception;

    List<HqclcfPost> queryAllPost() throws Exception;


    void  updatePid(HqclcfPost post) throws Exception;

    List<Map<String,String>> queryXfPost() throws Exception;

    List<Map<String,String>> queryOldPostNo2newId() throws Exception;

    List<Map<String,String>> queryOldWork() throws Exception;

    List<Map<String,String>> queryXdWork() throws Exception;

    List<Map<String,String>> queryHqWork() throws Exception;

    List<Map<String,String>> queryBank() throws Exception;

    List<Map<String,Object>> queryXfAllEmp() throws Exception;

    List<Map<String,Object>> queryHqEmp() throws Exception;

    List<Map<String,String>> queryXfFileInfo() throws Exception;

    List<Map<String,String>> queryRankByOldAndNew2Hr() throws Exception;

/*    void addXdPostList(SysXdOldPost post);*/

    List<Map<String,String>> queryListXdPost()throws Exception;

    void generateXjAutoMeted(); //考勤排班生成

    int  generateXjAutoMetedWithRef(@Param("gzym") String gzym); //同步考勤排班附表生成

    void generateXjPaidLeave(); //调休天数生成

    void generateXjNodeDays(); //请休假管理附表生成

    void generateXjVacatemanage(); //请休假管理正表生成

    void generateXjAbnormity(); //打卡异常生成

    void generateXjTimestatistical(); //考勤统计生成

    void generateXjEmpStatus(); //考勤统计生成

    int deleteHqclcfEmpBakByGzym(@Param("gzym") String gzym); //取消同步考勤排班附表生成

}
