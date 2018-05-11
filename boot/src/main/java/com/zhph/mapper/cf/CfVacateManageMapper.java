package com.zhph.mapper.cf;

import com.zhph.exception.AppException;
import com.zhph.model.cf.CfVacateManage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
public interface CfVacateManageMapper {

	 /**
     * 分页查询
     * 
     * @Title: queryByPage
     * @param：@param queryParams
     * @param：@param index
     * @param：@param rows
     */
    List<CfVacateManage> queryByPage(CfVacateManage queryParams)
        throws AppException;
    /**
     * 查询所有记录数
     */
    public Long queryVacateManageCount(CfVacateManage queryParams)
        throws Exception;
    
    /**
     * 根据员工编号获取 职级类型
     * 
     * @throws
     */
    public List<Map<String, Object>> queryByempNo(String empNo);

    /**
     * 统计指定月份、指定休假类型天数
     * @param vacateManage
     * @return
     */
    public Double getDaysForMonth(CfVacateManage vacateManage);
    
    /**
     * 修改多条数据的状态
     * @param map
     * @return
     */
	int updateStatusByIds(Map<String, Object> map);
	
	/**
	 * 保存数据
	 * @param vacat
	 * @return
	 */
	int save(CfVacateManage vacat);
	
	/**
	 * 修改数据
	 * @param vacat
	 * @return
	 */
	int update(CfVacateManage vacat);
	
	List<Map<String, Object>> querySalaryEmp(Map<String, Object> queryParam);
	
	/**
	 * getById
	 * @param priNumber
	 * @return
	 */
	CfVacateManage getById(Long priNumber);
	int delete(String id);
}