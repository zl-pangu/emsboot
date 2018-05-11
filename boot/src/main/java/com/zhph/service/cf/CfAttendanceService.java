package com.zhph.service.cf;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.cf.CfAttendance;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Create By lishuangjiang
 */

public interface CfAttendanceService {

    /**
     * 获取考勤记录
     * @param cfAttendance
     * @return
     */
    Grid<Map<String,String>> queryPageInfo(PageBean pageBean, CfAttendance cfAttendance,String gzym) throws Exception;

    /**
     * 导出人员异动记录
     * @param cfAttendance
     * @param req
     * @param res
     * @throws Exception
     */
    public void exportExl(CfAttendance cfAttendance, HttpServletRequest req, HttpServletResponse res) throws Exception;

    /**
     * 启用状态部门
     * @return
     * @throws Exception
     */
    public ModelAndView queryAllOpenTypes(ModelAndView model) throws Exception;


    /**
     * 启用状态职务
     * @return
     * @throws Exception
     */
    public JSONObject queryAllOpenBusiness(String posCode) throws Exception;

    /**
     * 通过部门编号查询指定部门和部门下的所有启用状态的岗位
     * @return
     * @throws Exception
     */
    public JSONObject queryDeptAndPostListByDeptNo(Long id) throws Exception;


    /**
     * 条件检索人员异动记录
     * @return
     * @throws Exception
     */
    public List<HqclcfEmp> queryAllPersonTransfer(String data, String q) throws Exception;

    /**
     * 检索业务条线
     * @param key
     * @return
     * @throws Exception
     */
    String queryBusinessLine(String key,String constant) throws Exception;

    /**
     * 生成考勤统计表
     * @throws Exception
     */
    JSONObject saveAttendance(String gzym) throws Exception;

    /**
     * 锁定
     * @throws Exception
     */
    JSONObject lock(CfAttendance attendance) throws Exception;

    /**
     * 解锁
     * @throws Exception
     */
    JSONObject unlock(CfAttendance attendance) throws Exception;



}
