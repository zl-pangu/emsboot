package com.zhph.service.cl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhph.model.cl.ClCreditCoremanagerM2Det;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

/**
 * @author lxp
 * @date 2018年1月23日 下午3:33:22
 * @parameter
 * @return
 */
public interface ClSixMonthM2Service {

	/**
	 * 分页
	 * 
	 * @param pageBean
	 * @param params
	 * @return
	 */
	public Grid<ClCreditCoremanagerM2Det> queryPageInfo(PageBean pageBean, ClCreditCoremanagerM2Det clSixMonthM2) throws Exception;

	/**
	 * 导出
	 * 
	 * @param data
	 * @param req
	 * @param res
	 */
	public void exportExl(ClCreditCoremanagerM2Det clSixMonthM2, HttpServletRequest req, HttpServletResponse res) throws Exception;

	/**
	 * 定时生成c-m1和m2+及m2+明细
	 * 
	 * @throws Exception
	 */
	public void AutoGenerateM1AndM2(String gzYm) throws Exception;

}
