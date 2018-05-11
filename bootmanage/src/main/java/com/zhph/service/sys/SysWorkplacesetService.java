package com.zhph.service.sys;

import com.alibaba.fastjson.JSONObject;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/11/23.
 */
public interface SysWorkplacesetService {
    /**
     * 构建grid
     * @param pageBean
     * @param params
     * @return
     * @throws Exception
     */
    Grid<SysWorkplaceset> queryPageInfo(PageBean pageBean, SysWorkplaceset params) throws Exception;

    /**
     *查询省份
     * @return
     * @throws Exception
     */
    List<String> queryAllProvince() throws Exception;

    /**
     * 删除
     * @param workplaceset
     * @return
     * @throws Exception
     */
    Json del(SysWorkplaceset workplaceset) throws Exception;

    /**
     * 新增
     * @param work
     * @return
     */
    void add(SysWorkplaceset work) throws Exception;

    /**
     * 修改
     * @param data
     * @return
     * @throws Exception
     */
    void editById(String data) throws Exception;

    /**
     *导出excle
     * @param data
     * @param req
     * @param res
     * @throws Exception
     */
    void exportExl(SysWorkplaceset data, HttpServletRequest req, HttpServletResponse res) throws Exception;

    /**
     * 根据查询参数查询工作地
     * @param data
     * @return
     */
    List<SysWorkplaceset> queryWorkPlaceByParam(String data) throws Exception;

    JSONObject buildCityLevelByBl(String businessLine) throws Exception;
}
