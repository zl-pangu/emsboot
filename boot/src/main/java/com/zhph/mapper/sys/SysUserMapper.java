package com.zhph.mapper.sys;

import java.util.List;
import java.util.Map;

import com.zhph.model.sys.SysUser;
import com.zhph.model.sys.dto.SysUserDeptDataAuth;
import org.apache.ibatis.annotations.Param;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

public interface SysUserMapper {
	public List<SysUser> queryAllUserByInfo(SysUser params);

	public SysUser queryUserById(Long id);

	public void updateUserToDeleted(Long id);

	public void saveEdit(SysUser params);

	public void saveAdd(SysUser params);

	public SysUser queryUserByUserId(String userId);

	public SysUser queryUserByUserName(String userName);

	public SysUser queryUserWithPwdByUserName(String userName);

	public void resetLoginErrorCount(Long id);

	public void updateLoginErrorCount(SysUser params);

	public void saveChangePwd(SysUser params);

	public void batchInsertUserDeptAuth(List<SysUserDeptDataAuth> lists);

	public void deleteDeptAuth(@Param("userId") String userId);

	List<Long> queryDeptIdByUserId(Long userId);

    void batchUpdateUserDeptAuth(List<SysUserDeptDataAuth> list);

    List<Map<String,Object>> queryUserinfoByuserID(Map<String,String> map);

    Integer quertUserCount(Map<String,String> map);
    
}
