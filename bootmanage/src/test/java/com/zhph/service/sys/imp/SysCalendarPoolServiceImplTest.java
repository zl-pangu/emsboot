package com.zhph.service.sys.imp;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhph.model.sys.SysCalendarPool;
import com.zhph.service.sys.SysCalendarPoolService;
import com.zhph.util.Json;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SysCalendarPoolServiceImplTest {
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	@Autowired
	private SysCalendarPoolService sysCalendarPoolService;
	@Test
	public void testRefreshPool() throws Exception {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("refreshType", "year");
		condition.put("cldYear", "2017");
		Json js = sysCalendarPoolService.refreshPool(condition );
		System.out.println(js);
	}
	

	@Test
	public void testFindByCondtion() throws Exception{
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("cldDateStart", 20170120);
		condition.put("cldDateEnd", 20170220);
		condition.put("refreshStartDate", "2017-01-20");
		condition.put("refreshEndDate", "2017-02-20");
		List<SysCalendarPool> list = sysCalendarPoolService.findByCondtion(condition, true);
		if(list != null && list.size() > 0){
			for(SysCalendarPool calendarPool:list){
				System.out.println(calendarPool);
			}
		}
	}
	
	@Test
	public void testIfLastWorkDay() throws Exception{
		System.out.println(sysCalendarPoolService.ifLastWorkDay("2017-12-29"));
		/*System.out.println(sysCalendarPoolService.ifLastWorkDay("2018-11-21"));
		System.out.println(sysCalendarPoolService.ifLastWorkDay("2018-01-31"));
		System.out.println(sysCalendarPoolService.ifLastWorkDay("2018-01-30"));*/

	}
	
}
