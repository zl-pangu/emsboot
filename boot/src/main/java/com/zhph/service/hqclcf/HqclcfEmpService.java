package com.zhph.service.hqclcf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;


public interface HqclcfEmpService {

	// 查询列表
	public Grid<HqclcfEmp> queryPageInfo(PageBean pageBean, HqclcfEmp params) throws Exception;

	// 查询单条
	public HqclcfEmp gotoEditById(String Id) throws Exception;

	// 获取附件
	public List<HqclcfEmpFile> getFilesByEmpNo(String empno) throws Exception;

	// 保存
	public JSONObject saveData(HqclcfEmp hqclcfEmp, MultipartHttpServletRequest mhsRequest) throws Exception;

	// 导出
	public void exportExl(HqclcfEmp data, HttpServletRequest req, HttpServletResponse res);

	public JSONArray getCitySelectData(String bl) throws Exception;

	public ModelAndView queryAllOpenTypes(ModelAndView model) throws Exception;

	public HqclcfEmp queryEmpByEmpNo(String empNo) throws Exception;

	public String getZhWorkExpByEmpNo(String empNo) throws Exception;

	public void AutoTfEmpType() throws Exception;
	
	public Json bakupHqclcfEmpByGzym(String gzym) throws Exception;

}
