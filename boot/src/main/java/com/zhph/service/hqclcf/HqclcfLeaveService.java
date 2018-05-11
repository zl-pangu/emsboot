package com.zhph.service.hqclcf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfLeave;
import com.zhph.model.sys.SysUser;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface HqclcfLeaveService {

    /**
     * 分页
     *
     * @param pageBean
     * @param params
     * @return
     */
    Grid<HqclcfLeave> queryPageInfo(PageBean pageBean, HqclcfLeave params)throws Exception;

    /**
     * 删除
     *
     * @param readValue
     * @return
     */
    Json del(HqclcfLeave readValue);

    /**
     * 导出
     *
     * @param data
     * @param req
     * @param res
     */
    void exportExl(HqclcfLeave data, HttpServletRequest req, HttpServletResponse res);

    /**
     * 编辑
     *
     * @param leave
     * @return
     */
    //Json editById(HqclcfLeave leave, HttpServletRequest res);
	void editById(HqclcfLeave leave, MultipartHttpServletRequest multipartHttpServletRequest,HttpServletRequest res)throws Exception;


    /**
     * 审批
     *
     * @param data
     * @return
     */
//    Json appById(String data, HttpServletRequest res) throws Exception;
    JSONObject appById(Long id,HqclcfLeave leave, SysUser onlineUser,HttpServletRequest res) throws Exception;
    /**
     * 新增
     *
     * @param readValue
     * @return
     */
    void add(HqclcfLeave hqclcfLeave, HttpServletRequest res, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;

    HqclcfLeave queryLeave(Long id) throws Exception;

    /**
     * 离职淘汰管理新增员工每，根据员工姓名
     *
     * @param empName
     * @return
     */
    Map<String, Object> queryleaveEmpByempName(String empName, String empNo) throws Exception;


    /**
     * 上传
     *
     * @param request
     * @param empNo
     * @throws Exception
     */
    HqclcfEmpFile uploadFile(MultipartHttpServletRequest request, String empNo) throws Exception;


    /**
     * 预览上传文件
     *
     * @return
     */
    void preViewFile(HqclcfEmpFile hqclcfEmpFile, HttpServletResponse res);


    /**
     * 删除上传文件
     *
     * @return
     */
    Json delFile(HqclcfEmpFile file);


    /**
     * 查询用户当前上传文件
     *
     * @param empName
     * @return
     */
    List<Map<String, Object>> queryfileName(String empName, String busLine);


    /**
     * 检查按钮权限
     *
     * @param request
     */
    void showBtnList(HttpServletRequest request, ModelAndView modelAndView);

    public ModelAndView queryAllOpenTypes(ModelAndView model, Long id)throws Exception;

	
    List<Map<String, String>> queryAllLeave(String q) throws Exception;
	
    void buildEditFormReult(Long id, HttpServletRequest req) throws Exception;

	void buildListTpl(HttpServletRequest req, ModelAndView modelAndView) throws Exception;




	
}
