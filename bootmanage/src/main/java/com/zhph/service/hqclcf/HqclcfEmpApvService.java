package com.zhph.service.hqclcf;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.dto.HqclcfEmpApv2SelectJsonResult;
import com.zhph.model.sys.SysUser;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface HqclcfEmpApvService {
    /**
     * 分页查询
     * @param pageBean
     * @param emp
     * @return
     * @throws Exception
     */
    Grid<HqclcfEmp> queryPageInfo(PageBean pageBean, HqclcfEmp emp) throws Exception;

    /**
     * 验证身份证
     * @param data
     * @return
     * @throws Exception
     */
    JSONObject empCheck(String data) throws Exception;

    HqclcfEmpApv2SelectJsonResult bulidSelectDataByDeptId(String data) throws Exception;

    void addEmp(HqclcfEmp emp, MultipartHttpServletRequest request) throws Exception;

    HqclcfEmp queryEmp(Long id) throws Exception;

    void buildEditFormReult(Long id, HttpServletRequest req) throws Exception;

    void showEmpPoto(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception;

    void previewHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception;

    void downloadHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception;

    void editEmp(HqclcfEmp emp, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;

    JSONObject apvEmp(Long id,HqclcfEmp emp, SysUser onlineUser) throws Exception;

    JSONObject delEmp(Long id) throws Exception;

    void buildListTpl(HttpServletRequest req, ModelAndView model) throws Exception;

    JSONObject checkIspreviewHdsfFile(HqclcfEmpFile file);
}
