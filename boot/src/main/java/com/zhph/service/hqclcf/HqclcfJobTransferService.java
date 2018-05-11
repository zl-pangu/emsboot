package com.zhph.service.hqclcf;

import com.alibaba.fastjson.JSONArray;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfJobTransfer;
import com.zhph.model.hqclcf.dto.HqclcfEmpApv2SelectJsonResult;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HqclcfJobTransferService {

    Grid<HqclcfJobTransfer> queryPageInfo(PageBean pageBean, HqclcfJobTransfer params) throws Exception;

    /**
     * 新增
     *
     * @param readValue
     * @return
     */
    Json add(HqclcfJobTransfer readValue,HttpServletRequest res);

    /**
     * 修改
     *
     * @param data
     * @return
     */
    Json editById(HttpServletRequest req,HqclcfJobTransfer readValue);


    /**
     * 离职转在职新增查询，根据姓名或员工号
     *
     * @param empName
     * @param empNo
     * @return
     */
    HashMap<String, Object> queryjobTransferEmpByEmpNameOrNo(String empName, String empNo);


    /**
     * 获取上级部门树
     *
     * @return
     */
    JSONArray getParentDeptTree(String userId, Long deptId, String deptCode) throws Exception;

    /**
     * 获取业务条线
     *
     * @param id
     * @return
     */
    Object getBusLine(Long id);


    /**
     * 检查是否超编
     * @param transfer
     * @return
     */
    Json checkIsFull(HqclcfJobTransfer transfer) throws Exception;

    /**
     * 检查按钮权限
     * @param request
     */
    void showBtnList(HttpServletRequest request, ModelAndView modelAndView);

	void buildEditFormReult(Long id, HttpServletRequest req) throws Exception;
	
	/**
	 * 生成部门及以下岗位联动
	 * @Title: bulidSelectDataByDeptId 
	 * @Description: 
	 * @param @param data
	 * @param @return
	 * @param @throws Exception   
	 * @return HqclcfEmpApv2SelectJsonResult    
	 * @throws
	 */
	HqclcfEmpApv2SelectJsonResult bulidSelectDataByDeptId(String data) throws Exception;
	
	/**
	 * 获取离职的人数
	 * @Title: queryAllLeave 
	 * @Description: 
	 * @param @param data
	 * @param @param q
	 * @param @return
	 * @param @throws Exception   
	 * @return List<Map<String,Object>>    
	 * @throws
	 */
	public List<Map<String, Object>> queryAllHqclcfJob(String data, String q) throws Exception;
	
	void buildListTpl(HttpServletRequest req, ModelAndView modelAndView) throws Exception;
}
