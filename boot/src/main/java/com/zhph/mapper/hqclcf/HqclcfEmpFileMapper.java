package com.zhph.mapper.hqclcf;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfEmpFile;
import com.zhph.model.hqclcf.HqclcfEmpTempFile;

/**
 * Created by zhouliang on 2017/12/26.
 */
@Repository
public interface HqclcfEmpFileMapper {
	/**
	 * 插入一条附件信息
	 * 
	 * @param file
	 */
	void insertempFile(HqclcfEmpFile file);

	void batchInsertFile(@Param("files") List<HqclcfEmpFile> files);

	void deleteFile(@Param("empNo") String empNo, @Param("fileType") Integer fileType);

	List<HqclcfEmpFile> queryEmpFile(HqclcfEmpFile file);

	/**
	 * 员工号和文件类型查询文件
	 * 
	 * @param empNo
	 * @param fileType
	 * @return
	 */
	HqclcfEmpFile queryEmpFileByEmpNoAndFileType(@Param("empNo") String empNo, @Param("fileType") Integer fileType);

	/**
	 * 根据员工号和文件类型查询文件 返回多条记录
	 * @param empNo
	 * @param fileType
	 * @return
	 */
	List<HqclcfEmpFile> queryFileByParam(@Param("empNo") String empNo, @Param("fileType") Integer fileType);

	List<HqclcfEmpFile> getFilesByEmpNo(@Param("empNo") String empNo);

	/**
	 * 更新业务条线
	 * 
	 * @param
	 */
	void editBl(HqclcfEmp emp);

	/**
	 * 通过员工编码、文件类型、业务条线删除附件信息
	 * 
	 * @param empFile
	 * @return
	 */
	int deleteFileByBusiness(HqclcfEmpFile empFile);

	/**
	 * 修改附件信息
	 * 
	 * @param empFile
	 * @return
	 */
	int eidtEmpFile(HqclcfEmpFile empFile);

	List<HqclcfEmpTempFile> getTempFilesByEmpNo(@Param("empNo") String empNo, @Param("fid") String fid);

	void insertEmpTempFile(HqclcfEmpTempFile hqclcfEmpTempFile);

	void delTempFile(@Param("empNo") String empNo, @Param("fid") String fid);

	void delTempTempFile(@Param("empNo") String empNo, @Param("fileType") String fileType, @Param("fid") String fid);

}
