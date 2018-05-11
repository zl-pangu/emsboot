package com.zhph.mapper.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhph.model.sys.SysCalendarPool;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SysCalendarPoolMapperTest {
	
	@Autowired
	private SysCalendarPoolMapper sysCalendarPoolMapper;
	
	@Test
	public void testInsert() {
		SysCalendarPool sysCalendarPool = new SysCalendarPool();
		int day = 2;
		int week = 5;
		int start = 20171200;
		for(;day<31;day++){
			sysCalendarPool.setCldDateBeta(start+day);
//		sysCalendarPool.setCldDay(1);
			week++;
			week %= 7;
			if(week ==0 || week == 6	){
				sysCalendarPool.setCldFlg("1");
			}else{
				sysCalendarPool.setCldFlg("0");
			}
			if(day == 31){
				sysCalendarPool.setIfLastWorkDay("1");
			}else{
				sysCalendarPool.setIfLastWorkDay("0");
			}
			sysCalendarPool.setLastWkDt(start+day-1);
			sysCalendarPool.setNextWkDt(start+day+1);
			sysCalendarPool.setWeek(week);
			System.out.println(sysCalendarPoolMapper.insert(sysCalendarPool ));
		}
	}
	
	@Test
	public void testQueryByCondtion() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("cldMonth", 12);
		map.put("cldDateStart", 20171201);
		map.put("cldDateEnd", 20171211);
		List<SysCalendarPool> list = sysCalendarPoolMapper.queryByCondtion(map);
		if(list != null && list.size() > 0){
			for(SysCalendarPool calendarPool:list){
				System.out.println(calendarPool);
			}
		}
	}

	@Test
	public void testDeleteByCondtion(){
		Map<String, Object> map = new HashMap<String,Object>();
		int i = sysCalendarPoolMapper.deleteByCondition(map);
		System.out.println(i);
	}
	
}
