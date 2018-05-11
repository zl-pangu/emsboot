package com.zhph.service.cf.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.mapper.cf.CfOfflaterSetMapper;
import com.zhph.model.cf.CfOfflaterSet;
import com.zhph.service.cf.CfOfflaterSetService;
import com.zhph.util.CommonUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

/**
 * @author lxp
 * @date 2017年12月22日 上午10:43:59
 * @parameter
 * @return
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class CfOfflaterSetServiceImpl implements CfOfflaterSetService {

	public static final Logger logger = LogManager.getLogger(CfOfflaterSetServiceImpl.class);

	@Autowired
	private CfOfflaterSetMapper cfOfflaterSetMapper;

	@Override
	public Grid<CfOfflaterSet> queryPageInfo(PageBean pageBean, CfOfflaterSet offlaterSet) throws Exception {

		Grid<CfOfflaterSet> grid = null;
		try {
			if (pageBean != null) {
				PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
			}
			List<CfOfflaterSet> list = cfOfflaterSetMapper.queryAllData(offlaterSet);
			PageInfo<CfOfflaterSet> pageInfo = new PageInfo<>(list);
			grid = new Grid<>();
			grid.setData(pageInfo.getList());
			grid.setCount(pageInfo.getTotal());
		} catch (Exception e) {
			logger.error("查询调休工作日设置失败:" + e.getMessage());
			e.printStackTrace();
		}
		return grid;
	}

	@Override
	public CfOfflaterSet gotoEditById(String Id) throws Exception {

		CfOfflaterSet offlaterSet = new CfOfflaterSet();
		try {
			List<CfOfflaterSet> ls = cfOfflaterSetMapper.queryDataById(Id);
			if (ls.size() > 0) {
				offlaterSet = ls.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return offlaterSet;
	}

	@Override
	public Json saveData(String cmd, CfOfflaterSet offlaterSet) throws Exception {

		Json json = new Json();
		try {
			if ("A".equals(cmd)) {
				offlaterSet.setCreateName(CommonUtil.getOnlineFullName());
				offlaterSet.setCreateDate(new Date());
				cfOfflaterSetMapper.insertData(offlaterSet);
			} else if ("U".equals(cmd)) {
				cfOfflaterSetMapper.updateById(offlaterSet);
			}
			json.setSuccess(true);
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
			cfOfflaterSetMapper.delById(id);
			json.setSuccess(true);
		} catch (Exception e) {
			json.setSuccess(false);
			json.setMsg(e.getMessage());
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public JSONObject IfCheckIn(CfOfflaterSet offlaterSet) throws Exception {
		JSONObject json = new JSONObject();
		try {
			List<CfOfflaterSet> ls = cfOfflaterSetMapper.ifCheckIn(offlaterSet);
			if (ls.size() > 0) {
				json.put("ifExists", "true");
				json.put("msg", offlaterSet.getCosMonth() + "已经录入，无法再次录入！");
			} else {
				// 只有修改的时候才判断调休天数是否大于排班调休天数之和。
				if (!CommonUtil.StringIfNullOrEmpty(offlaterSet.getId())) {
					int weds = Integer.valueOf(CommonUtil.ChangeNullString(offlaterSet.getCosWeekendDays()));

					Map<String, String> params = new HashMap<String, String>();
					params.put("cosMonth", offlaterSet.getCosMonth());
					params.put("result", "");
					cfOfflaterSetMapper.checkWorkDayAndWeekDay(params);
					String rs = params.get("result");
					if (!CommonUtil.StringIfNullOrEmpty(rs)) {
						if (weds < Integer.valueOf(rs)) {
							json.put("ifExists", "true");
							json.put("msg", "调休天数不能小于排班中休假天数之和！");
						} else {
							json.put("ifExists", "false");
						}
					}
				} else {
					json.put("ifExists", "false");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}
}
