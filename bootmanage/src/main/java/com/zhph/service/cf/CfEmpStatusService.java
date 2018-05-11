package com.zhph.service.cf;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.cf.CfEmpStatus;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Create By lishuangjiang
 * 消分员工状态变更
 */
public interface CfEmpStatusService {

    /**
     * 查询所有员工状态变更记录
     * @param empStatus
     * @return
     */
    Grid<Map<String,String>> queryPageInfo(PageBean pageBean, CfEmpStatus empStatus) throws Exception;

    /**
     * 通过员工编码查询员工状态变更记录
     * @param empNo
     * @return
     */
    CfEmpStatus queryEmpByEmpNo(String empNo ) throws Exception;

    /**
     * 选择性插入
     * @param empStatus
     * @return
     */
    int insertSelective(CfEmpStatus empStatus) throws Exception;

    /**
     * 整体插入
     * @param data
     * @return
     */
    JSONObject insert(String data) throws Exception;

    /**
     * 通过主键标识查询员工状态变更记录
     * @param id
     * @return
     */
    CfEmpStatus queryEmpByPriMarkey(Long id ) throws Exception;

    /**
     * 修改
     * @param data
     * @return
     */
    JSONObject updateEmpStatuses(String data) throws Exception;

    /**
     * 在model设置修改empSatatus对象
     * @param model
     * @param priNumber
     * @return
     * @throws Exception
     */
    ModelAndView setEditEmpSatatus(ModelAndView model, Long priNumber) throws Exception;

    JSONObject importExl(MultipartFile file,HttpServletRequest req,HttpServletResponse res) throws Exception;

    /**
     * 员工编码删除
     * @param empNo
     * @return
     */
    int delEmpStatusesByEmpNo(String empNo ) throws Exception;

    /**
     * 主键标识删除
     * @param id
     * @return
     */
    int delEmpStatusesById(Long id ) throws Exception;

    /**
     * 解除当前记录
     * @param priNumber
     * @return
     * @throws Exception
     */
    Object relieve(Long priNumber,String endDate) throws Exception;

    /**
     * 获取员工状态和是否变更
     * @param model
     * @return
     * @throws Exception
     */
    ModelAndView queryEmpStatusList(ModelAndView model) throws Exception;

    /**
     * queryByQ
     * @param q
     */
    JSONObject queryByq(String q,PageBean pageBean, int rows) throws Exception;

    /**
     * 初始化大区、分中心、营业部
     * @param model
     * @return
     * @throws Exception
     */
    ModelAndView queryDeptArea(ModelAndView model) throws Exception;

    /**
     * 导出消分员工状态变更
     * @param cfEmpStatus
     * @param req
     * @param res
     * @throws Exception
     */
    void exportExl(CfEmpStatus cfEmpStatus, HttpServletRequest req, HttpServletResponse res) throws Exception;

    /**
     * 定时任务
     * @throws Exception
     */
    void autoEmpStatus() throws Exception;



}
