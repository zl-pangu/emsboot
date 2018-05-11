package com.zhph.service.sys;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhph.model.sys.SysZhphBank;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.springframework.web.servlet.ModelAndView;

public interface SysZhphBankService {

	/**
	 * 分页
	 * @param pageBean
	 * @param params
	 * @return
	 */
	Grid<SysZhphBank> queryPageInfo(PageBean pageBean, SysZhphBank params);

	
	/**
	 * 查询银行卡编码是否重复
	 * @param bankCode
	 * @return
	 */
	public Integer checkBank(String bankCode);

	/**
	 * 删除
	 * @param readValue
	 * @return
	 */
	void del(SysZhphBank readValue) throws Exception;

	/**新增
	 * @param readValue
	 * @return
	 */
	Json add(SysZhphBank readValue);


	/**
	 * 导出
	 * @param data
	 * @param req
	 * @param res
	 */
	void exportExl(SysZhphBank data, HttpServletRequest req, HttpServletResponse res);


	/**
	 * 修改
	 * @param data
	 * @return
	 */
	Json editById(String data);


	void showBtnList(HttpServletRequest request, ModelAndView modelAndView);
	
	
}
