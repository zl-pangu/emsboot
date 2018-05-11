package com.zhph.service.cf;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.cf.CfOfflaterSet;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

/**
 * @author lxp
 * @date 2017年12月22日 上午10:43:30
 * @parameter
 * @return
 */
public interface CfOfflaterSetService {

	// 查询列表
	public Grid<CfOfflaterSet> queryPageInfo(PageBean pageBean, CfOfflaterSet offlaterSet) throws Exception;

	// 查询单条
	public CfOfflaterSet gotoEditById(String Id) throws Exception;

	// 保存
	public Json saveData(String cmd, CfOfflaterSet offlaterSet) throws Exception;

	// 删除
	public Json delData(String id) throws Exception;

	// 判断当前月份是否已经填写
	public JSONObject IfCheckIn(CfOfflaterSet offlaterSet) throws Exception;

}
