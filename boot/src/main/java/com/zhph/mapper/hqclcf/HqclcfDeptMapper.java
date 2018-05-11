package com.zhph.mapper.hqclcf;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfPost;

@Repository
public interface HqclcfDeptMapper {

    List<HqclcfDept> queryAll(HqclcfDept dept) throws Exception;

    List<HqclcfDept> queryTreeByParams(HqclcfDept dept) throws Exception;

    void addDept(HqclcfDept dept) throws Exception;

    void editEdit(HqclcfDept dept) throws Exception;

    void delById(Long id) throws Exception;

    /**
     * 根据业务条线查询部门树
     *
     * @param businessLine
     * @return
     * @throws Exception
     */
    List<HqclcfDept> queryTreeByOnlineUserBl(@Param("businessLine") List<Integer> businessLine) throws Exception;

    void updateDeptByPdept(@Param("list") List<HqclcfDept> lists);

    /**
     * 根据ID查询父节点
     *
     * @param map
     * @return
     */
    List<HqclcfDept> queryPidObjById(Map<String, Object> map);

    /**
     * 根据部门id查询这个部门下的所有启用状态的岗位
     *
     * @param id
     * @return
     */
    List<HqclcfPost> queryPostByDeptId(@Param("id") Long id, @Param("status") String status);

    HqclcfDept queryDeptByCode(@Param("deptCode") String deptCode);
    
    HqclcfDept queryDeptNameByCode(@Param("deptName") String deptCode);

    HqclcfDept queryDeptChildNameByCode(@Param("hqclcfDept")HqclcfDept hqclcfDept);
    /**
     * 根据部门id查询对应部门信息
     * @param id
     * @return
     */
    HqclcfDept queryDeptById(@Param("id") Long id);

    /**
     * 根据用户Id查询所在部门及所有上级部门节点
     *
     * @param userId/deptId/deptCode
     * @param type                   up向上查询，or 向下查询
     * @return
     */
    List<HqclcfDept> queryParentOrSubDepts(Map<String, Object> map);

    /**
     * 查询部门根据userID
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryDeptMapByMap(Map<String, String> map);

	/**
	 * 查询用户拥有权限的部门s
	 * @param userId
	 */
	List<HqclcfDept> queryAuthedDeptsByUserId(String userId);


	
	/**
	 * 查询信贷所有分公司
	 * @param businessLine
	 * @param deptType
	 * @return
	 * @throws Exception
	 */
	List<HqclcfDept> queryOrgParams() throws Exception;

    /**
     * 根据id查询消分的部门关系
     * @param id
     * @return
     * @throws Exception
     */
	List<HqclcfDept> queryCfDeptStartWithId(Long id) throws Exception;


    /**
     *
     * @param map type :up 向上查,down 向下查(包含自己),deptId:当前传入的节点
     * @return
     * @throws Exception
     */
    List<HqclcfDept>queryChildNodeBydeptId(Map map)throws Exception;


    void updataById(HqclcfDept dept);
    
    /**
	 * 获取信贷区域
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryArea();
	
	/**
	 * 获取信贷分公司
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryOrg(@Param("deptCode") String deptCode);
	
	/**
	 * 获取信贷部门
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryDept(@Param("deptCode") String deptCode);
	
	/**
	 * 获取信贷团队
	 * 
	 * @param gzym
	 * @return
	 */
	List<Map<String, Object>> queryTeam(@Param("deptCode") String deptCode);
    
	
	
}
