package com.zhph.mapper.hqclcf;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.zhph.model.hqclcf.HqclcfEmpSubjectChange;

public interface HqclcfEmpSubjectChangeMapper {

	public List<HqclcfEmpSubjectChange> queryAllList(HqclcfEmpSubjectChange empSubjectChange);

	public HqclcfEmpSubjectChange getEmpSubjectChangeById(@Param("id") String id);

	public void insertData(HqclcfEmpSubjectChange empSubjectChange);

	public void auditData(HqclcfEmpSubjectChange empSubjectChange);

	public void cancelData(@Param("id") String id);

	public void editData(HqclcfEmpSubjectChange empSubjectChange);

	public void delData(@Param("id") String id);

}