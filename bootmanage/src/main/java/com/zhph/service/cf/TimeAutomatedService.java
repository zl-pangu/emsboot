package com.zhph.service.cf;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.zhph.model.cf.CfTimeLock;
import com.zhph.model.cf.TimeAutomated;
import com.zhph.model.cf.TimeAutomatedBak;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import com.zhph.model.sys.SysUser;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

/**
 * Created by liuxin on 2016/12/12.
 */
public interface TimeAutomatedService {
    /**
     * 获取考勤统计
     * @param xjTimeAutomatedBak
     * @param pageFilter
     * @return
     * @throws Exception
     */
    public Grid<TimeAutomated> dataGrid(TimeAutomatedBak timeAutomated, PageBean pageFilter)throws Exception;

    /**
     * 导出
     * @param response
     * @param xjTimeAutomatedBak
     * @throws Exception
     */
    public void exportExl(HttpServletResponse response,TimeAutomatedBak timeAutomated)throws Exception;

    /**
     * 统计字段对应的值
     * @param fields
     * @param xjTimeAutomated
     * @param keyName
     * @return
     * @throws Exception
     */
    public Integer getworkDays(Field[] fields, TimeAutomated xjTimeAutomated, String keyName, Map<String,Object> currentDayAllMonth) throws Exception;
    
    /**
     * 自动生成所有的排班
     * @param user
     * @throws Exception
     */
    public void addAllEmp(SysUser user)throws Exception;

    /**
     * 入职添加单个排班
     * @throws Exception
     */
    public void addOneEmpForEntry(HqclcfEmp emp, SysUser user)throws Exception;

    /**
     * 离职添加单个排班
     * @throws Exception
     */
    public void addOneEmpForLeave(HqclcfEmp emp, SysUser user)throws Exception;
    
    /**
     * 员工异动时，修改排班信息
     * @param hqclcfPersonTransfer
     * @param sysUser
     * @return
     * @throws Exception
     */
	public Json updateForEmployerTransfor(HqclcfPersonTransfer hqclcfPersonTransfer, SysUser sysUser) throws Exception;

    /**
     * 获取排班列表
     * @param pageFilter
     * @param xjTimeAutomatedBak
     * @return
     * @throws Exception
     */
    public List<TimeAutomated> getList(PageBean pageBean,TimeAutomatedBak xjTimeAutomatedBak)throws Exception;

    /**
     * 批量修改
     * @param timeAutomateds
     * @throws Exception
     */
    public int updateBatch(List<TimeAutomated> timeAutomateds,SysUser user)throws Exception;

    /**根据工资年月和人员编号删除该人当月的排班*/
    public int deleteXjTimeAutomatedByGzymAndEmpno(String gzYmMin, String gzYmMax, String empNo) throws Exception;
    
    /**根据工资年月和人员编号该人当月排班的职位*/
    public int updateJobNameInXjTimeAutomatedByGzymAndEmpno(String gzYmMin, String gzYmMax, String empNo, String jobName) throws Exception;

	public CfTimeLock queryCfTimelock(CfTimeLock lock);
}
