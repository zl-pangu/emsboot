package com.zhph.service.sys.imp;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.mapper.sys.SysHolidayMapper;
import com.zhph.model.sys.SysHoliday;
import com.zhph.service.sys.SysHolidayService;
import com.zhph.util.CommonUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysHolidayServiceImpl implements SysHolidayService {

	public static final Logger logger = LogManager.getLogger(SysHolidayServiceImpl.class);

	@Autowired
	private SysHolidayMapper SysHolidayMapper;

	@Override
	public Grid<SysHoliday> queryPageInfo(PageBean pageBean, SysHoliday holiday) throws Exception {

		Grid<SysHoliday> grid = null;
		try {
			PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
			List<SysHoliday> list = SysHolidayMapper.queryAllHoliday(holiday);
			PageInfo<SysHoliday> pageInfo = new PageInfo<>(list);
			grid = new Grid<>();
			grid.setData(pageInfo.getList());
			grid.setCount(pageInfo.getTotal());
		} catch (Exception e) {
			logger.error("查询节假日管理失败:" + e.getMessage());
			e.printStackTrace();
		}
		return grid;
	}

	@Override
	public SysHoliday gotoEditById(String Id) throws Exception {
		SysHoliday holiday = new SysHoliday();
		try {
			List<SysHoliday> ls = SysHolidayMapper.queryHolidayById(Id);
			if (ls.size() > 0) {
				holiday = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return holiday;
	}

	@Override
	public Json saveData(String cmd, SysHoliday holiday) throws Exception {
		Json json = new Json();
		try {
			// 查重
			List<SysHoliday> ls = SysHolidayMapper.checkIfExsits(holiday);

			if (ls.size() <= 0) {
				if ("A".equals(cmd)) {
					holiday.setCreateName(CommonUtil.getOnlineFullName());
					holiday.setCreateDate(new Date());
					SysHolidayMapper.insertData(holiday);
				} else if ("U".equals(cmd)) {
					SysHolidayMapper.updateById(holiday);
				}
				json.setSuccess(true);
			} else {
				json.setSuccess(false);
				json.setMsg("重复录入无法继续录入！");
			}

		} catch (Exception e) {
			json.setSuccess(false);
			json.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public Json delData(String id) throws Exception {
		Json json = new Json();
		try {
			SysHolidayMapper.delById(id);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setSuccess(false);
			json.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public Json autoHoliday(String year) throws Exception {
		Json json = new Json();
		try {
			json.setMsg("接口实现中。。。");
			json.setSuccess(true);
		} catch (Exception e) {
			json.setSuccess(false);
			json.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	/**
	 * 
	 */
	@Override
	public List<SysHoliday> queryAll(SysHoliday holiday) throws Exception {
		return SysHolidayMapper.queryAllHoliday(holiday);
	}
}
