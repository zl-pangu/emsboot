package com.zhph.service.cl;

import com.zhph.model.cl.ClAshBlackMenuModel;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Create by zhaoqixiang
 * Description:
 * 2018/1/16
 **/
public interface ClAshBlackMenuService {

    Grid<ClAshBlackMenuModel> queryPageInfo(PageBean pageBean, ClAshBlackMenuModel params);

    void exportExl(ClAshBlackMenuModel params, HttpServletRequest ret, HttpServletResponse res);

    Json importExl(MultipartHttpServletRequest request) throws Exception;
}
