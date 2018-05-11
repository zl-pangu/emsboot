package com.zhph.service.cf;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhph.exception.AppException;
import com.zhph.model.cf.CfVacateManage;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfGzym;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.sys.SysHoliday;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;


public interface CfVacateManageService {
    /**
     * 分页查询
     * @Title: queryByPage
     * @param：@param queryParams
     * @param：@param index
     * @param：@param rows
     */
    Grid<CfVacateManage> queryByPage(CfVacateManage queryParams, PageBean pageBean)
        throws AppException;
    
    /**
     * 查询所有记录数
     */
    public Long queryVacateManageCount(CfVacateManage queryParams)
        throws Exception;
    /**
     * 计算节假日时间
     * @param priNumber 
     */
    public Json calculationbydatas(String empNo, Date startDate, Date endDate, String priNumber)
        throws Exception;
    
    /**
     * 根据员工编号获取 职级类型
     * 
     * @throws
     */
    public List<Map<String, Object>> queryByempNo(String empNo);
    
    /**
     * 提交
     * 
     * @throws
     */
    public Json submitThisDatas(List<Long> list) throws Exception;
    
    /**
     * 撤销
     * 
     * @throws
     */
    public Json revokeThisDatas(List<Long> list) throws Exception;
    
    /**
     * 审核
     * 
     * @throws
     */
    Json reviewThisDatas(List<Long> list, String status, String auditOpinion)
        throws Exception;
    
    Json unreviewThisdatas(List<Long> list)
        throws Exception;
    
    /**
     * 查询导出
     * @throws
     */
    void exportExlByquery(List<CfVacateManage> list, List<HqclcfDept> hqclcfDepts, List<HqclcfRank> rankTypes,
        HttpServletRequest request, HttpServletResponse response);

    /**
     * 保存对象
     * @param vacat
     * @param holidays 
     * @param manage 
     * @param flag 
     * @return
     * @throws Exception 
     */
	Json saveOrUpdate(CfVacateManage vacat, boolean flag, CfVacateManage manage, List<SysHoliday> holidays) throws Exception;

	/**
	 * 查询员工信息
	 * @param queryParam
	 * @param pageBean 
	 * @return
	 * @throws Exception
	 */
	Grid<Map<String, Object>> querySalaryEmp(Map<String, Object> queryParam, PageBean pageBean) throws Exception;

	/**
	 * 查询当前工资年月信息
	 * @return
	 */
	HqclcfGzym queryCurrGzym() throws Exception;

	/**
	 * 删除请休假
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	Json delete(String ids) throws Exception;
}
