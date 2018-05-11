package com.zhph.service.hqclcf;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.sys.SysConfigType;
import com.zhph.util.Json;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/11/29.
 */
public interface HqclcfDeptService {
    /**
     * 根据添加生成所需要的树形结构
     * @param data
     * @return
     * @throws Exception
     */
    JSONArray buildDeptTree(String data,Long userId) throws Exception;
    /**
     * 根据选择id查询部门
     * @param id
     * @return
     * @throws Exception
     */
    HqclcfDept queryDept(Long id) throws Exception;
    /**
     * 根据业务条线返回对应的部门类型
     * @param businessLine
     * @return
     * @throws Exception
     */
    List<SysConfigType> buildDeptTypeByBl(Integer businessLine) throws Exception;

    /**
     * 新增部门
     * @param data
     * @return
     * @throws Exception
     */
    HqclcfDept addDept(String data) throws Exception;

    /**
     * 根据部门ID查询部门所属的业务条线分类
     * @param id
     * @return
     * @throws Exception
     */
    List<SysConfigType> buildDeptTypeById(Long id) throws Exception;

    HqclcfDept querySuperDept(Long id) throws Exception;

    JSONObject editDept(String data) throws Exception;

    Json del(Long id) throws Exception;

    JSONObject detail(Long id) throws Exception;

    JSONObject checkDeptPrepare(Long id, String type) throws Exception;

    JSONArray buildTreeByUserbL(List<Integer> bl) throws Exception;

    JSONObject checkDeptEnable(Long treeId, String sl) throws Exception;

    /**
     * @param isShow "1" 显示下级部门，"0" 显示部门
     * @return
     * @throws Exception
     */
    JSONArray queryDeptTreeByisShowChild(String isShow) throws Exception;

    /**
     * 构建岗位树 通过传入的部门id
     * @param id
     * @return
     * @throws Exception
     */
    JSONArray buildPostTreeByDeptId(Long id) throws Exception;
    
    /**
     * 根据条件查询部门信息（初次设计目的是为了查询不业务线的部门）
     * @param hqclcfDept
     * @return
     * @throws Exception
     */
	List<HqclcfDept> queryAll(HqclcfDept hqclcfDept) throws Exception;
	
    /**
     * 通过部门编码查询部门
     */
   HqclcfDept  queryDeptByDeptCode(String deptCode) throws Exception;

	
	/**
	 * 根据userId查询所在部门及所有上级部门节点
	 * @param userId/deptId/deptCode
	 * @return
	 * @throws Exception
	 */
	List<HqclcfDept> queryParentDepts(Map<String,Object> map);
	/**
	 * 根据userId查询所在部门及所有下级部门节点
	 * @param userId/deptId/deptCode
	 * @return
	 * @throws Exception
	 */
	List<HqclcfDept> queryChildDepts(Map<String,Object> map) throws Exception;
	
	/**
	 * 查询用户拥有权限的部门s
	 * @param userId
	 * @return
	 */
	List<HqclcfDept> queryAuthedDeptsByUserId(String userId);
	
	List<HqclcfDept> queryOrgParams() throws Exception;


    /**
     * 根据标识读取上下部门节点
     * @param type
     * @param id
     * @return
     * @throws Exception
     */
    List<HqclcfDept> queryDeptUpOrDown(String type,Long id) throws Exception;


    HqclcfDept queryDeptByCode(@Param("deptCode") String deptCode) throws Exception;
    
    HqclcfDept queryDeptChildNameByCode(String deptName,String deptCode) throws Exception;
}
