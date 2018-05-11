package com.zhph.service.hqclcf;


import com.alibaba.fastjson.JSONObject;
import com.zhph.model.hqclcf.*;
import com.zhph.model.vo.ResultVo;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface HqclcfPersonTransferService {
	/**
	 * 条件查询人员异动
	 * @param pageBean
	 * @param hqclcfPersonTransfer
	 * @return
	 * @throws Exception
	 */
	public Grid<HqclcfPersonTransfer>getPersonTransferByCondition(PageBean pageBean,HqclcfPersonTransfer hqclcfPersonTransfer)throws Exception;
	/**
	 * 删除选中的人员异动记录
	 * @return
	 * @throws Exception
	 */
	public ResultVo deleteByPrimaryKey(HqclcfPersonTransfer hqclcfPersonTransfer)throws Exception;
	/**
	 * 修改之前查询选中人的信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public HqclcfPersonTransfer geTransferById(Long id)throws Exception;

	/**
	 * 选择性修改
	 * @return
	 * @throws Exception
	 */
	public int updateByPrimaryKeySelective(HqclcfPersonTransfer hqclcfPersonTransfer,MultipartHttpServletRequest request,HttpServletRequest req)throws Exception;

	/**
	 * 导出人员异动记录
	 * @param hqclcfPersonTransfer
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void exportExl(HqclcfPersonTransfer hqclcfPersonTransfer, HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * 启用状态部门
	 * @return
	 * @throws Exception
	 */
	public ModelAndView queryAllOpenTypes(ModelAndView model,Long id) throws Exception;

	/**
	 * 启用状态岗位
	 * @return
	 * @throws Exception
	 */
	public List<HqclcfPost> queryAllOpenPost() throws Exception;

	/**
	 * 启用状态职级
	 * @return
	 * @throws Exception
	 */
	public List<HqclcfRank> queryAllOpenRank() throws Exception;

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
	public JSONObject queryAllPersonTransfer(String data, String q,PageBean pageBean,int rows) throws Exception;

	/**
	 * 检索业务条线
	 * @param key
	 * @return
	 * @throws Exception
	 */
	String queryBusinessLine(String key,String constant) throws Exception;

	/**
	 * 选择性插入信息
	 * @param hqclcfPersonTransfer
	 * @return
	 * @throws Exception
	 */
	int insertSelective(HqclcfPersonTransfer hqclcfPersonTransfer, MultipartHttpServletRequest request, HttpServletRequest req) throws Exception;

	/**
	 * 获取带姓名的personTransfer对象
	 * @param request
	 * @param id
	 * @throws Exception
	 */
	Boolean setEditPersonTransfer(ModelAndView request,Long id ) throws Exception;


	/**
	 * 定时任务检查需要生效的人员异动记录
	 * @throws Exception
	 */
	void quartzMission() throws Exception;

	/**
	 * 异动生效
	 * @param emp
	 * @throws Exception
	 */
	int updateEmpTrans(HqclcfPersonTransfer emp) throws Exception;


	/**
	 * 插入、修改判断是否立即生效公共方法
	 * @param emp
	 * @throws Exception
	 */
	int dateUtilMethod(HqclcfPersonTransfer emp) throws Exception;

	/**
	 * 下载hdfs文件
	 * @param file
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	public void downloadHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * 预览文件
	 * @param file
	 * @param req
	 * @param res
	 * @throws Exception
	 */
	void previewHdsfFile(HqclcfEmpFile file, HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * 检测当前部门或者岗位编制是否满编
	 * @param deptNo
	 * @param postNo
	 * @return
	 * @throws Exception
	 */
	JSONObject queryEmpOrganizat(String deptNo,String postNo) throws Exception;

}
