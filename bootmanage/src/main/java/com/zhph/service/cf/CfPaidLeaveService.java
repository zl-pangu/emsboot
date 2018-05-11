/*
 * CfPaidLeaveService.java created on 2018-01-12 下午 14:15:42 by roilatD
 */
package com.zhph.service.cf;

import com.zhph.exception.AppException;
import com.zhph.model.cf.CfPaidLeave;
import com.zhph.util.Grid;
import com.zhph.util.PageBean;

/**
 * TODO  该模块的功能待消除，需要删除对应的表和mapper.xml
 * 月调休天数配置表 Service 接口
 * @author: roilatD
 * @since: 2018-01-12
 */
public interface CfPaidLeaveService {
	
    /**
     * 分页查询
     * @Title: queryByPage
     * @param：@param queryParams
     * @param：@param index
     * @param：@param rows
     */
    Grid<CfPaidLeave> queryByPage(CfPaidLeave queryParams, PageBean pageBean)
        throws AppException;
}
