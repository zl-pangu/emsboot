package com.zhph.service.hqclcf;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.sys.SysConfigType;
import com.zhph.util.Json;

public interface HqclcfPostService {

	JSONArray buildPostTree(Long id) throws Exception;

	/**
	 * 根据选择id查询岗位
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	HqclcfPost queryPost(Long id) throws Exception;

	/**
	 * 根据业务条线返回对应的岗位
	 * 
	 * @param businessLine
	 * @return
	 * @throws Exception
	 */
	List<SysConfigType> buildPostTypeByBl(Integer businessLine) throws Exception;

	/**
	 * 新增岗位
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	Json addPost(String data) throws Exception;

	/**
	 * 根据部门ID查询部门所属的业务条线分类
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<SysConfigType> buildPostTypeById(Long id) throws Exception;

	/**
	 * 通过主键标识查询faNodes
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	HqclcfPost querySuperPost(Long id) throws Exception;

	/**
	 * 编辑岗位
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	Json editPost(String data) throws Exception;

	/**
	 * 删除岗位
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Json del(Long id) throws Exception;

	JSONObject detail(Long id) throws Exception;

	/**
	 * 生成6位岗位编码
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrderIdByUUId() throws Exception;

	/**
	 * 构建部门树形结构
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONArray buildDeptTree() throws Exception;

	/**
	 * 获取指定部门信息
	 */
	public HqclcfDept getBusinessLine(Long id) throws Exception;

	/**
	 * 获取状态为启用的职务信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<HqclcfBusiness> queryAllBusiness() throws Exception;

	/**
	 * 获取状态为启用的部门信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<HqclcfDept> queryAllDepts() throws Exception;

	/**
	 * 通过岗位id判断其上级或下级是否存在开启或者禁用状态的岗位
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONObject judgeStatus(String code, int value) throws Exception;

	/**
	 * 
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	public HqclcfPost getHqclcfPostBypostCode(String postCode) throws Exception;


	/**
	 * 检查岗位是否超编
	 * @param postCode
	 * @return
	 * @throws Exception
	 */
	JSONObject checkprePost(String postCode) throws Exception;

}
