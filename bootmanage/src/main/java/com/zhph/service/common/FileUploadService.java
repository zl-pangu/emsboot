package com.zhph.service.common;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zhph.model.hqclcf.HqclcfEmp;

/**
 * @author lxp
 * @date 2018年1月18日 上午11:24:06
 * @parameter
 * @return
 */
public interface FileUploadService {
	/**
	 * 员工附件上传
	 * 
	 * @param request
	 */
	public void uploadFile(MultipartHttpServletRequest request, HqclcfEmp emp) throws Exception;

}
