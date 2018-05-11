package com.zhph.service.sys.imp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.mapper.sys.SysCalendarPoolMapper;
import com.zhph.model.sys.SysCalendarPool;
import com.zhph.service.sys.SysCalendarPoolService;
import com.zhph.util.DateUtil;
import com.zhph.util.HttpClientHelper;
import com.zhph.util.Json;
import com.zhph.util.StringUtil;


/**
 *  * 系统日历表(用于工作日/非工作日数据查询/保存)Service实现类
 * @author roilat-D
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class SysCalendarPoolServiceImpl implements SysCalendarPoolService {

	@Value("${sys.calendarquery.url}")  
	private String sysCalendarQueryUrl;
	@Resource
	private SysCalendarPoolMapper sysCalendarPoolMapper;
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	protected Log log = LogFactory.getLog(SysCalendarPoolServiceImpl.class);
	
	/**
	 * 根据条件查询日历时间列表
	 */
	@Override
	public List<SysCalendarPool> findByCondtion(Map<String, Object> condition, Boolean refresh) throws Exception {
		if(condition == null){
			condition = new HashMap<String,Object>();
		}
		if(refresh){
			condition.put("refreshType", Constant.SYS_CALENDAR_REFRESH_TYPE_TIME_INTERVAL);
			Json js = refreshPool(condition);
			if(!js.isSuccess()){
				throw new AppException("日历数据刷新失败！",500);
			}
		}
		List<SysCalendarPool> list = sysCalendarPoolMapper.queryByCondtion(condition) ;
		return list;
	}

	/**
	 * 更新日历时间列表
	 * 入参refreshType=year时，cldYear参数必传；
	 * 入参refreshType=month时，cldMonth参数必传；
	 * 入参refreshType=interval时，refreshStartDate和refreshEndDate必传；
	 */
	@Override
	public Json refreshPool(Map<String, Object> condition) throws Exception {
		/*
		 * 1、检测是否在更新中（刷新中：返回刷新中；否则：更新状态为刷新中）
		 * 2、组织参数：无参返回；更新方式：
		 * 2、调用接口数据
		 * 3、删除对应的数据
		 * 4、插入新数据
		 */
		Json ret = new Json();
		if(condition == null || condition.isEmpty()){
			ret.setSuccess(false);
			ret.setMsg("未传入日历更新的时间范围！");
			return ret;
			//condition.put("cldMonth", Calendar.getInstance().get(Calendar.MONTH)+1);
		}
		
		List<String[]> tasks = new ArrayList<String[]>();//需要完成的日历更新任务；将大时间段内数据拆分成多次完成
		Map<String,Object> delMap = new HashMap<String,Object>();
		String refreshType = (String) condition.get("refreshType");
		if(Constant.SYS_CALENDAR_REFRESH_TYPE_YEAR.equals(refreshType)){//按年更新
			String cldYear = (String) condition.get("cldYear");
			if (StringUtil.isEmpty(cldYear)) {
				cldYear = Calendar.getInstance().get(Calendar.YEAR)+"";
			} else {
				if(!cldYear.matches("\\d{4}")){
					ret.setSuccess(false);
					ret.setMsg("年份信息有误!");
					return ret;
				}
			}
			
			//默认将一年的日历信息通过4次操作完成
			String[] st = new String[2];
			st[0] = cldYear + "-01-01";
			st[1] = cldYear + "-03-31";
			tasks.add(st);
			st = new String[2];
			st[0] = cldYear + "-04-01";
			st[1] = cldYear + "-06-30";
			tasks.add(st);
			st = new String[2];
			st[0] = cldYear + "-07-01";
			st[1] = cldYear + "-09-30";
			tasks.add(st);
			st = new String[2];
			st[0] = cldYear + "-10-01";
			st[1] = cldYear + "-12-31";
			tasks.add(st);
			delMap.put("cldDateStart", Integer.parseInt(cldYear + "0101"));
			delMap.put("cldDateEnd", Integer.parseInt(cldYear + "1231"));
		}else if(Constant.SYS_CALENDAR_REFRESH_TYPE_MONTH.equals(refreshType)){//按月更新
			String cldMonth = (String) condition.get("cldMonth");
			String[] st = new String[2];
			if (StringUtil.isEmpty(cldMonth)) {
				st[0] = "" + Calendar.getInstance().get(Calendar.YEAR) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-01";
				st[1] = "" + Calendar.getInstance().get(Calendar.YEAR) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-31";
			} else {
				if(!cldMonth.matches("\\d{4}-((1[0-2])|(0[1-9]))")){
					ret.setSuccess(false);
					ret.setMsg("月份信息有误!");
					return ret;
				}
				st[0] = cldMonth + "-01";
				st[1] = cldMonth + "-31";
				delMap.put("cldDateStart", Integer.parseInt(cldMonth + "01"));
				delMap.put("cldDateEnd", Integer.parseInt(cldMonth + "31"));
			}
			tasks.add(st);
		}else if(Constant.SYS_CALENDAR_REFRESH_TYPE_TIME_INTERVAL.equals(refreshType)){//按时间段更新
			String startDateStr = (String) condition.get("refreshStartDate");
			String endDateStr = (String) condition.get("refreshEndDate");
			if (StringUtil.isEmpty(startDateStr) || StringUtil.isEmpty(endDateStr)) {
				ret.setSuccess(false);
				ret.setMsg("必须传入开始时间(refreshStartDate)和结束时间(refreshEndDate)！");
				return ret;
			}
			if(!startDateStr.matches("\\d{4}-((1[0-2])|(0[1-9]))-(0[1-9]|[1-2][0-9]|3[0-1])")){
				ret.setSuccess(false);
				ret.setMsg("开始时间(refreshStartDate)格式错误，格式是【yyyy-MM-dd】！");
				return ret;
			}
			if(!endDateStr.matches("\\d{4}-((1[0-2])|(0[1-9]))-(0[1-9]|[1-2][0-9]|3[0-1])")){
				ret.setSuccess(false);
				ret.setMsg("结束时间(refreshEndDate)格式错误，格式是【yyyy-MM-dd】！");
				return ret;
			}
			Date startDate = DateUtil.convertStringToDate(startDateStr, "yyyy-MM-dd");
			Date endDate = DateUtil.convertStringToDate(endDateStr, "yyyy-MM-dd");
			int interval = DateUtil.daysBetween(startDate, endDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			int temp;//temp=0,do once
			String[] st;
			do{
				st = new String[2];
				temp = Math.min(interval, 90);
				st[0] = DateUtil.parseDateFormat(cal.getTime(), "yyyy-MM-dd");
				cal.add(Calendar.DAY_OF_YEAR, temp);
				st[1] = DateUtil.parseDateFormat(cal.getTime(), "yyyy-MM-dd");
				interval -= temp;
				tasks.add(st);
			}while(interval >= 0 && temp > 0);
			delMap.put("cldDateStart", Integer.parseInt(startDateStr.replace("-", "")));
			delMap.put("cldDateEnd", Integer.parseInt(endDateStr.replace("-", "")));
		}else{
			ret.setSuccess(false);
			ret.setMsg("未知的更新方式！【可选的更新方式有：按年更新，按月更新，按时间段更新！】");
			return ret;
		}
		
		if (tasks.size() > 0) {//deal tasks
			Map<String, String> paramMap = new HashMap<String,String>();
			Json json;
			JSONObject dataObj;
			JSONArray dataArray;
			SysCalendarPool calendarPool;
			List<SysCalendarPool> calendarPools = new ArrayList<SysCalendarPool>();
			for (String[] ss : tasks) {//loop tasks
				int[] flags = {0,1};
				paramMap.clear();
				paramMap.put("startDate", ss[0]);
				paramMap.put("endDate", ss[1]);
				for (int flag: flags) {
					paramMap.put("flag", flag+"");
					json = HttpClientHelper.postForm(sysCalendarQueryUrl, paramMap);
					if (!json.isSuccess()) {
						throw new Exception("调用接口错误！错误信息【" + json.getMsg() + "】");
					}
					dataObj = JSONObject.parseObject((String) json.getObj());
					dataArray = dataObj.getJSONArray("data");
					if (dataArray.size() > 0) {//deal response
						for (Object obj : dataArray) {
							calendarPool = JSONObject.parseObject(obj.toString(), SysCalendarPool.class);
							if (calendarPool.getCldDate() > 0) {
								int currMonth = calendarPool.getCldDate() % 10000 / 100;
								int nextMonth = calendarPool.getNextWkDt() % 10000 / 100;
								if (currMonth != nextMonth && Constant.SYS_CALENDAR_REFRESH_IF_WORKDAY_YES
										.equals(calendarPool.getCldFlg())) {
									/*calendarPool.getNextWkDt() - calendarPool.getCldDate() >= 31*/
									// 同一个月内，当前时间不是本月最后一个工作日
									calendarPool.setIfLastWorkDay(Constant.SYS_CALENDAR_REFRESH_IF_LAST_WORKDAY_YES);
								} else {
									calendarPool.setIfLastWorkDay(Constant.SYS_CALENDAR_REFRESH_IF_LAST_WORKDAY_NO);
								}
								calendarPools.add(calendarPool);
							}
						}
					} //end deal response
				}
			}//end loop tasks
			
			int i = this.deleteByCondtion(delMap);
			log.info("success to delete " + i + " records");
			i = this.saveList(calendarPools);
			log.info("success to save " + i + " records");
			//this.markLaskWorkDateForEachMonth((String) delMap.get("cldDateStart"), (String) delMap.get("cldDateEnd"));
			ret.setSuccess(true);
		} // end deal tasks
		return ret;
	}

	@Override
	public int saveList(List<SysCalendarPool> calendarPools) throws Exception {
		if(calendarPools != null && calendarPools.size() > 0){
			SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			int i = 0;
			try {
				for (SysCalendarPool calendarPool : calendarPools) {
					sysCalendarPoolMapper.insert(calendarPool);
					i++;
					if ((i > 0 && i % 100 == 0) || i == calendarPools.size()) {
						session.commit();
						session.clearCache();
					}
				}
			} catch (Exception e) {
				session.rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}
			return i;
		}else{
			return 0;
		}
	}

	@Override
	public int deleteByCondtion(Map<String, Object> map) throws Exception {
		return sysCalendarPoolMapper.deleteByCondition(map);
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private void markLaskWorkDateForEachMonth(String startDateStr,String endDateStr){
		Map<String,Object> map = new HashMap<String,Object>();
		int startDate = Integer.parseInt(startDateStr);
		int endDate = Integer.parseInt(endDateStr);
		startDate = startDate/100*100+1;
		endDate = endDate/100*100+31;
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("cldFlg", Constant.SYS_CALENDAR_REFRESH_IF_WORKDAY_YES);
		List<SysCalendarPool> list = sysCalendarPoolMapper.findLastWorkDays(map);
		if(list != null && list.size() > 0){
			List<Integer> ids = new ArrayList<Integer>(list.size());
			for(SysCalendarPool pool:list){
				ids.add(pool.getCldDate());
			}
			map.clear();
			map.put("ifLastWorkDay",Constant.SYS_CALENDAR_REFRESH_IF_LAST_WORKDAY_YES );
			map.put("ids", ids);
			int i = sysCalendarPoolMapper.markAsLastWorkDay(map);
			log.info("markAsLastWorkDay do update"+i + "records");
		}
		
	}

	@Override
	public boolean ifLastWorkDay(String date) throws Exception {
		Map<String, Object> map = new HashMap<String,Object>();
		if(StringUtil.isEmpty(date)){
			throw new Exception("传入时间为空！");
		}
		if(!date.matches("\\d{4}-((1[0-2])|(0[1-9]))-(0[1-9]|[1-2][0-9]|3[0-1])")){
			throw new Exception("传入时间格式有误，格式是【yyyy-MM-dd】");
		}
		map.put("cldDate", Integer.parseInt(date.replace("-", "")));
		List<SysCalendarPool> list = sysCalendarPoolMapper.queryByCondtion(map);
		if(list == null || list.size() <= 0){
			map.clear();
			map.put("refreshType", Constant.SYS_CALENDAR_REFRESH_TYPE_TIME_INTERVAL);
			map.put("refreshStartDate",date);
			map.put("refreshEndDate",date);
			this.refreshPool(map);
			map.clear();
			map.put("cldDate", Integer.parseInt(date.replace("-", "")));
			list = sysCalendarPoolMapper.queryByCondtion(map);
			if(list ==null || list.size() <= 0){
				//throw new Exception("未获取到相关数据！");
				return false;//一般此时，该日期为休息日
			}
		}
		return Constant.SYS_CALENDAR_REFRESH_IF_LAST_WORKDAY_YES.equals(list.get(0).getIfLastWorkDay());
	}
	
	
}
