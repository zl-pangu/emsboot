package com.zhph.service.hqclcf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.hqclcf.HqclcfEmpSubjectChange;
import com.zhph.model.hqclcf.HqclcfEmpTempFile;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

/**
 * @author lxp
 * @date 2018年1月8日 下午1:36:50
 * @parameter
 * @return
 */
public interface HqclcfEmpSubjectChangeService {

	// 查询列表
	public Grid<HqclcfEmpSubjectChange> queryPageInfo(PageBean pageBean, HqclcfEmpSubjectChange empSubjectChange) throws Exception;

	// 查询单条
	public HqclcfEmpSubjectChange getEditById(String Id) throws Exception;

	// 获取附件
	public List<HqclcfEmpTempFile> getFilesByEmpNo(String empno, String fid) throws Exception;

	// 获取员工列表
	public JSONObject getEmpListByType(String q, PageBean pageBean) throws Exception;

	// 保存
	public JSONObject saveData(HttpServletRequest req, HqclcfEmpSubjectChange empSubjectChange, MultipartHttpServletRequest mhsRequest) throws Exception;

	// 撤销
	public JSONObject cancelData(String Id) throws Exception;

	// 删除
	public JSONObject delData(String Id, String empNo) throws Exception;

}
