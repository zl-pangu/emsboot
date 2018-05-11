package com.zhph.mapper.hqclcf;

import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.sys.SysMidFileType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface HqclcfEmpApvMapper {
    /**
     * 查询全部的员工信息
     * @param emp
     * @return
     */
    List<HqclcfEmp> queryAll(HqclcfEmp emp);

    /**
     * 根据条件筛选员工信息
     * @param emp
     * @param status
     * @return
     */
    List<HqclcfEmp> queryEmpByStutas(@Param("emp") HqclcfEmp emp,@Param("status") String status);

    /**
     * 根据序列号查询序列
     * @param seqName
     * @return
     */
    String querySeqBySeqName(@Param("seqName")String seqName);

    /**
     * 根据部门和岗位ID，在职状态查询这个部门下岗位的人数
     * @param map
     * @return
     */
    Integer queryEmpSizeByDeptIdAndPostId(Map map);

    /**
     * 插入一条员工信息记录
     * @param emp
     */
    void insert(HqclcfEmp emp);

    /**
     * 修改员工
     * @param editEmp
     */
    void updateByEmp(HqclcfEmp editEmp);

    /**
     * 查询在使用的工作地和社保购买地人数
     * @param
     * @return
     */
    int queryEmpByworkNo(Map<String,String> map);

    List<Map<String,Object>> queryMidFileType(SysMidFileType sysMidFileType) throws Exception;
}
