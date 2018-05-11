package com.clachieve.test;


import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import com.zhph.model.sys.SysUser;
import com.zhph.service.cf.TimeAutomatedService;
import com.zhph.service.cl.ClAchieveDetailService;
import com.zhph.service.hqclcf.HqclcfEmpService;
import com.zhph.util.Json;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TimeAutomatedServiceImplTest {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	@Resource
	private HqclcfEmpMapper hqclcfEmpMapper;
	@Resource
	private TimeAutomatedService timeAutomatedService;
	@Resource
	private ClAchieveDetailService clAchieveDetailService;
	@Resource
	private HqclcfEmpService hqclcfEmpService;

	@Test
	public void testSyncClAchieveDetailEntry() throws Exception {
		SysUser sysUser = new SysUser();
		sysUser.setUserName("超级管理员");
		clAchieveDetailService.syncClAchieveDetail("2017-12",sysUser);
	}
	
	@Test
	public void testBakupHqclcfEmpByGzymEntry() throws Exception {
		hqclcfEmpService.bakupHqclcfEmpByGzym("2018-02");
	}
	
	
	@Test
	public void testAddOneEmpForEntry() throws Exception {
		HqclcfEmp emp = hqclcfEmpMapper.queryEmpByEmpNo("ZHXJCHENGD0744");
		SysUser user = new SysUser();
		user.setFullName("admin");
		timeAutomatedService.addOneEmpForEntry(emp, user );
	}
	
	@Test
	public void testAddOneEmpForLeave() throws Exception {
		HqclcfEmp emp = hqclcfEmpMapper.queryEmpByEmpNo("ZHXJCHENGD0744");
		SysUser user = new SysUser();
		user.setFullName("admin1");
		timeAutomatedService.addOneEmpForLeave(emp, user );
	}
	
	@Test
	public void updateForEmployerTransfor() throws Exception{
		HqclcfPersonTransfer hqclcfPersonTransfer = new HqclcfPersonTransfer();
		hqclcfPersonTransfer.setEmpNo("ZHXJCHENGD0744");
		hqclcfPersonTransfer.setPriBusinessLine("2");
		hqclcfPersonTransfer.setTransferTime("2018-02");
		hqclcfPersonTransfer.setNewBusinessLine("2");
		hqclcfPersonTransfer.setNewHqPosition("ZW13FED");//营业部经理
		SysUser sysUser = new SysUser();
		sysUser.setFullName("aaa");
		Json json = timeAutomatedService.updateForEmployerTransfor(hqclcfPersonTransfer, sysUser);
		System.out.println(json);
	}

}
