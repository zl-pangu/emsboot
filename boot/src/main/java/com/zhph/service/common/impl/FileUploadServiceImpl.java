package com.zhph.service.common.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zhph.commons.Constant;
import com.zhph.config.UrlConfig;
import com.zhph.mapper.hqclcf.HqclcfEmpFileMapper;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfEmpTempFile;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysUser;
import com.zhph.service.common.FileUploadService;
import com.zhph.util.CommonUtil;
import com.zhph.util.FileUpload;

/**
 * @author lxp
 * @date 2018年1月18日 上午11:25:43
 * @parameter
 * @return
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FileUploadServiceImpl implements FileUploadService {

	public static final Logger logger = LogManager.getLogger(FileUploadServiceImpl.class);

	@Autowired
	private UrlConfig urlConfig;

	@Autowired
	private SysConfigTypeMapper sysConfigTypeMapper;

	@Autowired
	private HqclcfEmpFileMapper hqclcfEmpFileMapper;

	/**
	 * 附件上传
	 * 
	 * @param request
	 * @param emp
	 * @param flag
	 * @throws Exception
	 */
	public void uploadFile(MultipartHttpServletRequest request, HqclcfEmp emp) throws Exception {
		Integer businessLine = emp.getBusinessLine();
		switch (businessLine) {
		case 1:// 总部
			Iterator<Map.Entry<String, Integer>> hqIter = buildFileMapName(Constant.ZB_FILE).entrySet().iterator();
			uploadFile2Hdfs(hqIter, emp, request, Constant.ZHPHHQ);
			break;
		case 2:// 消分
			Iterator<Map.Entry<String, Integer>> xfIter = buildFileMapName(Constant.XF_FILE).entrySet().iterator();
			uploadFile2Hdfs(xfIter, emp, request, Constant.ZHPHXJ);
			break;
		case 3:// 信贷
			Iterator<Map.Entry<String, Integer>> xdIter = buildFileMapName(Constant.XD_FILE).entrySet().iterator();
			uploadFile2Hdfs(xdIter, emp, request, Constant.ZHXD);
			break;
		}
	}

	/**
	 * 附件类型转换
	 * 
	 * @param sysCode
	 * @return
	 * @throws Exception
	 */
	private Map<String, Integer> buildFileMapName(String sysCode) throws Exception {
		Map<String, Integer> fileMap = new HashMap<>();
		List<SysConfigType> listFiles = sysConfigTypeMapper.getConfigByPSysCode(sysCode);
		List<SysConfigType> empimgpotos = sysConfigTypeMapper.getConfigByPSysCode(Constant.EMPIMGPOTO);
		SysConfigType config = empimgpotos.size() == 1 ? empimgpotos.get(0) : null;
		fileMap.put(config.getSysCode(), config.getSysValue());
		for (SysConfigType configType : listFiles) {
			fileMap.put(configType.getSysCode(), configType.getSysValue());
		}
		return fileMap;
	}

	/**
	 * 附件上传与附件数据保存
	 * 
	 * @param hqIter
	 * @param emp
	 * @param request
	 * @param zhPath
	 * @param flag
	 *            '1' 员工附件正式表 '2'员工附件临时表
	 * @throws Exception
	 */
	private void uploadFile2Hdfs(Iterator<Map.Entry<String, Integer>> hqIter, HqclcfEmp emp, MultipartHttpServletRequest request, String zhPath) throws Exception {
		SysUser onlineUser = CommonUtil.getOnlineUser();
		while (hqIter.hasNext()) {
			Map.Entry<String, Integer> hqNext = hqIter.next();
			Integer fileType = hqNext.getValue();
			String fileKey = hqNext.getKey();
			MultipartFile file = request.getFile(fileKey);
			if (file != null) {
				if ("".equals(file.getOriginalFilename())) {
					continue;
				}

				String flag = String.valueOf(request.getAttribute("flag"));
				String fid = String.valueOf(request.getAttribute("fid"));
				if ("1".equals(flag)) {
					HqclcfEmpFile empFile = new HqclcfEmpFile();
					empFile.setBusinessLine(emp.getBusinessLine());
					empFile.setCreateTime(new Date());
					empFile.setCreator(onlineUser.getFullName());
					empFile.setEmpNo(emp.getEmpNo());
					empFile.setFileName(file.getOriginalFilename());
					empFile.setFileType(fileType);
					empFile.setFileExtend(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
					try {
						FileUpload.upload(file, emp.getEmpNo(), zhPath, empFile.getFileType().toString(), empFile.getFileName(), logger, urlConfig.getFileUpload());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					hqclcfEmpFileMapper.deleteFile(empFile.getEmpNo(), empFile.getFileType());
					hqclcfEmpFileMapper.insertempFile(empFile);
				} else {
					HqclcfEmpTempFile hqclcfEmpTempFile = new HqclcfEmpTempFile();
					hqclcfEmpTempFile.setFid(fid);
					hqclcfEmpTempFile.setBusinessLine(emp.getBusinessLine());
					hqclcfEmpTempFile.setEmpNo(emp.getEmpNo());
					hqclcfEmpTempFile.setFileName(file.getOriginalFilename());
					hqclcfEmpTempFile.setFileType(fileType);
					hqclcfEmpTempFile.setFileExtend(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
					hqclcfEmpTempFile.setCreateTime(new Date());
					hqclcfEmpTempFile.setCreator(onlineUser.getFullName());
					try {
						FileUpload.upload(file, emp.getEmpNo(), zhPath, hqclcfEmpTempFile.getFileType().toString(), hqclcfEmpTempFile.getFileName(), logger, urlConfig.getFileUpload());
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					hqclcfEmpFileMapper.delTempTempFile(hqclcfEmpTempFile.getEmpNo(), String.valueOf(hqclcfEmpTempFile.getFileType()), hqclcfEmpTempFile.getFid());
					hqclcfEmpFileMapper.insertEmpTempFile(hqclcfEmpTempFile);
				}
			}
		}
	}

}
