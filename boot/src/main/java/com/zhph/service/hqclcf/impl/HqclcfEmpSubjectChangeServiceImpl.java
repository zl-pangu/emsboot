package com.zhph.service.hqclcf.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.mapper.hqclcf.HqclcfEmpFileMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpSubjectChangeMapper;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfEmpSubjectChange;
import com.zhph.model.hqclcf.HqclcfEmpTempFile;
import com.zhph.service.common.FileUploadService;
import com.zhph.service.hqclcf.HqclcfDeptService;
import com.zhph.service.hqclcf.HqclcfEmpSubjectChangeService;
import com.zhph.util.CommonUtil;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

/**
 * @author lxp
 * @date 2018年1月8日 下午1:41:03
 * @parameter
 * @return
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class HqclcfEmpSubjectChangeServiceImpl implements HqclcfEmpSubjectChangeService {

	public static final Logger logger = LogManager.getLogger(HqclcfEmpSubjectChange.class);

	@Autowired
	private HqclcfEmpSubjectChangeMapper hqclcfEmpSubjectChangeMapper;

	@Autowired
	private HqclcfEmpMapper hqclcfEmpMapper;

	@Autowired
	private HqclcfEmpFileMapper hqclcfEmpFileMapper;

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	private HqclcfDeptService hqclcfDeptService;

	@Override
	public Grid<HqclcfEmpSubjectChange> queryPageInfo(PageBean pageBean, HqclcfEmpSubjectChange empSubjectChange) throws Exception {
		Grid<HqclcfEmpSubjectChange> grid = null;

		if (empSubjectChange.getDeptNo() != null && empSubjectChange.getDeptNo() != "null" && empSubjectChange.getDeptNo() != "") {
			Long deptId = Long.valueOf(empSubjectChange.getDeptNo());
			HqclcfDept dept = hqclcfDeptService.queryDept(deptId);
			empSubjectChange.setDeptNo(dept.getDeptCode());
			empSubjectChange.setDeptHid(String.valueOf(deptId));
		}
		empSubjectChange.setUserId(CommonUtil.getOnlineUserId());
		try {
			PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
			List<HqclcfEmpSubjectChange> list = hqclcfEmpSubjectChangeMapper.queryAllList(empSubjectChange);
			PageInfo<HqclcfEmpSubjectChange> pageInfo = new PageInfo<>(list);
			grid = new Grid<>();
			grid.setData(pageInfo.getList());
			grid.setCount(pageInfo.getTotal());
		} catch (Exception e) {
			logger.error("查询失败:" + e.getMessage());
			e.printStackTrace();
		}
		return grid;
	}

	@Override
	public HqclcfEmpSubjectChange getEditById(String Id) throws Exception {
		HqclcfEmpSubjectChange empSubjectChange = new HqclcfEmpSubjectChange();
		try {
			empSubjectChange = hqclcfEmpSubjectChangeMapper.getEmpSubjectChangeById(Id);
		} catch (Exception e) {
			logger.error("获取失败:" + e.getMessage());
			e.printStackTrace();
		}
		return empSubjectChange;
	}

	@Override
	public List<HqclcfEmpTempFile> getFilesByEmpNo(String empno, String fid) throws Exception {
		return hqclcfEmpFileMapper.getTempFilesByEmpNo(empno, fid);
	}

	@Override
	public JSONObject saveData(HttpServletRequest req, HqclcfEmpSubjectChange empSubjectChange, MultipartHttpServletRequest mhsRequest) throws Exception {
		String cmd = req.getParameter("cmd");
		String bl = req.getParameter("businessLine");

		HqclcfEmp hqclcfemp = hqclcfEmpMapper.queryEmpByEmpNo(empSubjectChange.getEmpNo());
		hqclcfemp.setBusinessLine(Integer.valueOf(bl));

		JSONObject obj = new JSONObject();
		if ("A".equals(cmd)) {

			String id = CommonUtil.getCustomPrimaryKey();

			empSubjectChange.setBusinessLine(bl);
			empSubjectChange.setId(id);
			empSubjectChange.setStatus("1"); // 未审核状态
			empSubjectChange.setCreateDate(new Date());
			empSubjectChange.setCreateName(CommonUtil.getOnlineFullName());
			hqclcfEmpSubjectChangeMapper.insertData(empSubjectChange);

			mhsRequest.setAttribute("fid", id);
			mhsRequest.setAttribute("flag", "2"); // 先存附件临时表
			fileUploadService.uploadFile(mhsRequest, hqclcfemp);

			obj.put("success", true);
			obj.put("msg", "提交成功！");
		} else if ("U".equals(cmd)) {

			String id = empSubjectChange.getId();
			if (!CommonUtil.StringIfNullOrEmpty(id)) {

				empSubjectChange.setUpdateName(CommonUtil.getOnlineFullName());
				empSubjectChange.setUpdateDate(new Date());
				hqclcfEmpSubjectChangeMapper.editData(empSubjectChange);

				mhsRequest.setAttribute("fid", id);
				mhsRequest.setAttribute("flag", "2"); // 先存附件临时表
				fileUploadService.uploadFile(mhsRequest, hqclcfemp);

				obj.put("success", true);
				obj.put("msg", "提交成功！");
			} else {
				obj.put("success", false);
				obj.put("msg", "提交失败！");
			}
		} else if ("AU".equals(cmd)) {

			empSubjectChange.setAuditDate(new Date());
			empSubjectChange.setAuditName(CommonUtil.getOnlineFullName());
			hqclcfEmpSubjectChangeMapper.auditData(empSubjectChange);

			// 更新员工主体 HQCLCF_EMP
			String status = empSubjectChange.getStatus();
			if ("2".equals(status)) {
				hqclcfEmpMapper.updateEmpSubjectByEmpNo(empSubjectChange.getEmpsubjectNew(), empSubjectChange.getEmpNo());

				// 附件更新到正式表
				List<HqclcfEmpTempFile> hetf = hqclcfEmpFileMapper.getTempFilesByEmpNo(empSubjectChange.getEmpNo(), empSubjectChange.getId());
				if (hetf.size() > 0) {
					for (HqclcfEmpTempFile files : hetf) {
						hqclcfEmpFileMapper.deleteFile(files.getEmpNo(), files.getFileType());

						HqclcfEmpFile empFile = new HqclcfEmpFile();
						empFile.setBusinessLine(files.getBusinessLine());
						empFile.setCreateTime(files.getCreateTime());
						empFile.setCreator(files.getCreator());
						empFile.setEmpNo(files.getEmpNo());
						empFile.setFileName(files.getFileName());
						empFile.setFileType(files.getFileType());
						empFile.setFileExtend(files.getFileName().substring(files.getFileName().lastIndexOf(".")));

						hqclcfEmpFileMapper.insertempFile(empFile);
					}
				}
			}

			obj.put("success", true);
			obj.put("msg", "提交成功！");
		} else {
			obj.put("success", false);
			obj.put("msg", "提交失败！");
		}
		return obj;
	}

	@Override
	public JSONObject cancelData(String Id) throws Exception {
		JSONObject obj = new JSONObject();
		if (!"".equals(Id)) {
			hqclcfEmpSubjectChangeMapper.cancelData(Id);
			obj.put("success", true);
			obj.put("msg", "撤销成功！");
		} else {
			obj.put("success", false);
			obj.put("msg", "撤销失败！");
		}
		return obj;
	}

	@Override
	public JSONObject getEmpListByType(String q, PageBean pageBean) throws Exception {
		JSONObject json = new JSONObject();
		Grid<HqclcfEmp> grid = new Grid<>();
		PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());

		String userId = CommonUtil.getOnlineUserId();
		List<HqclcfEmp> ls = hqclcfEmpMapper.getEmpListByType(userId, q);

		PageInfo<HqclcfEmp> pageInfo = new PageInfo<>(ls);
		grid.setData(pageInfo.getList());
		grid.setCount(pageInfo.getTotal());
		json.put("rows", grid.getData());
		json.put("total", grid.getCount());

		return json;
	}

	@Override
	public JSONObject delData(String Id, String empNo) throws Exception {

		JSONObject obj = new JSONObject();
		if (!"".equals(Id) && !"".equals(empNo)) {
			hqclcfEmpSubjectChangeMapper.delData(Id);

			// 同时删除附加信息
			hqclcfEmpFileMapper.delTempFile(empNo, Id);

			obj.put("success", true);
			obj.put("msg", "删除成功！");
		} else {
			obj.put("success", false);
			obj.put("msg", "删除失败！");
		}
		return obj;
	}

}
