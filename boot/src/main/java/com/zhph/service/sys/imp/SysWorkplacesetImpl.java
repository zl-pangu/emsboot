package com.zhph.service.sys.imp;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.mapper.hqclcf.HqclcfEmpApvMapper;
import com.zhph.mapper.sys.SysWorkplacesetMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.service.hqclcf.HqclcfEmpApvService;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.service.sys.SysWorkplacesetService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2017/11/23.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysWorkplacesetImpl implements SysWorkplacesetService{
    @Autowired
    private SysWorkplacesetMapper sysWorkplacesetMapper;
    @Autowired
    private SysConfigTypeService sysConfigTypeService;
    @Resource
    private HqclcfEmpApvMapper hqclcfEmpApvMapper;
    public static final Logger logger = LogManager.getLogger(SysWorkplacesetImpl.class);

    @Override
    public Grid<SysWorkplaceset> queryPageInfo(PageBean pageBean, SysWorkplaceset params) throws Exception {
        Grid<SysWorkplaceset> grid= null;
        try {

            buildQueryParms(params);
            PageHelper.startPage(pageBean.getPage(),pageBean.getLimit());
            List<SysWorkplaceset> list = sysWorkplacesetMapper.queryAllWorkplaceset(params);
            PageInfo<SysWorkplaceset> pageInfo=new PageInfo<>(list);
            grid = new Grid<>();
            grid.setData(pageInfo.getList());
            grid.setCount(pageInfo.getTotal());
        } catch (Exception e) {
            logger.error("查询工作地设置失败:"+e.getMessage());
            e.printStackTrace();
        }
        return grid;
    }

    /**
     * 构建查询参数
     * @param params
     */
    private void buildQueryParms(SysWorkplaceset params) {
        //地区
        if (params.getArea()!=null&&!"".equals(params.getArea())){
            params.setOperType("areaFuzzyQuery");
        }
        //地区编码
        if(params.getAreaCode()!=null&&!"".equals(params.getAreaCode())){
            params.setOperType("areaCodeFuzzyQuery");
        }
    }

    @Override
    public List<String> queryAllProvince() throws Exception {
        try {
            return sysWorkplacesetMapper.queryAllProvince(Constant.ENABLE);
        } catch (Exception e) {
            logger.error("查询省份失败："+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Json del(SysWorkplaceset workplaceset) throws Exception{
        Json json=new Json();
        try {
            if (workplaceset.getId()==null)
                throw new AppException("id不能为空");
            //员工表使用了这个地区就不允许删除
            Map<String,String> map=new HashMap();
            map.put("workOrgNo",workplaceset.getAreaCode());
            Integer count = hqclcfEmpApvMapper.queryEmpByworkNo(map);
            if (count>0){
                json.setSuccess(true);
                json.setMsg("该地区在员工信息已被使用，不能删除！");
            }else{
                sysWorkplacesetMapper.delById(workplaceset.getId());
            }
        } catch (Exception e) {
            json.setSuccess(true);
            json.setMsg(e.getMessage());
            logger.error("删除失败："+e.getMessage());
            e.printStackTrace();
        }
        return json;
    }


    public void add(SysWorkplaceset work) throws Exception{
            work.setCreateName(CommonUtil.getOnlineFullName());
            work.setCreateDate(new Date());

            SysWorkplaceset workplaceset=new SysWorkplaceset();
            workplaceset.setAreaCode(work.getAreaCode());

            List<SysWorkplaceset> workplacesetList = sysWorkplacesetMapper.queryAllWorkplaceset(workplaceset);
            if (workplacesetList.size()>0)
                throw new AppException("工作地编码已经存在,不能重复录入");
            sysWorkplacesetMapper.addByObj(work);

    }

    @Override
    public void editById(String data) throws Exception {
            ObjectMapper mapper=new ObjectMapper();
            SysWorkplaceset params = mapper.readValue(data.getBytes("utf-8"), SysWorkplaceset.class);
            sysWorkplacesetMapper.updateById(params);
    }


    @Override
    public void exportExl(SysWorkplaceset data, HttpServletRequest req, HttpServletResponse res) throws Exception{
            Map<String,String> businessLineMap=new HashMap<>();
            data.setOperType("areaCodeFuzzyQuery");
            List<SysWorkplaceset> list = sysWorkplacesetMapper.queryAllWorkplaceset(data);

            List<SysConfigType> sysConfigTypeList = sysConfigTypeService.getConfigTypesByPSysCode(Constant.BUSINESS_LINE);
            for (SysConfigType configType: sysConfigTypeList) {
                businessLineMap.put(configType.getSysValue().toString(),configType.getSysName());
            }

            String[] colTitleAry = {"序号", "地区编码", "地区", "所属省份","业务条线","城市级别","状态", "备注"};
            String[][] array = new String[list.size()][colTitleAry.length];
            short[] colWidthAry = {80,100,100,100,100,100,100,200};
            for (int i = 0; i < list.size(); i++) {
                SysWorkplaceset workp = list.get(i);
                array[i][0] =  i+1+""; //序号
                array[i][1] = workp.getAreaCode();//地区编码
                array[i][2] =  workp.getArea(); //地区
                array[i][3] =  workp.getProvince(); //所属省份
                array[i][4] = null!=businessLineMap.get(workp.getBusinessLine())?businessLineMap.get(workp.getBusinessLine()):"";//业务条线
                switch (workp.getBusinessLine()){
                    case "1"://总部
                        for (SysConfigType hqCity:sysConfigTypeService.getConfigTypesByPSysCode(Constant.CITY_LEVEL_HQ)) {
                            if(hqCity.getSysValue().equals(workp.getCityLevel())){
                                array[i][5]=hqCity.getSysName();
                                break;
                            }
                        }
                        break;
                    case "2"://消分
                        for (SysConfigType xjCity:sysConfigTypeService.getConfigTypesByPSysCode(Constant.CITY_LEVEL_XJ)) {
                            if(xjCity.getSysValue().equals(workp.getCityLevel())){
                                array[i][5]=xjCity.getSysName();
                                break;
                            }
                        }
                        break;
                    case "3"://信贷
                        for (SysConfigType xdCity:sysConfigTypeService.getConfigTypesByPSysCode(Constant.CITY_LEVEL_XD)) {
                            if(xdCity.getSysValue().equals(workp.getCityLevel())){
                                array[i][5]=xdCity.getSysName();
                                break;
                            }
                        }
                        break;
                }
                array[i][6] =  "1".equals(workp.getStatus()) ? "启用" : "禁用"; //状态
                array[i][7] = workp.getRemark();
            }
            //导出Util
            ExcelUtil excelUtil=new ExcelUtil();
            excelUtil.writeExecl(colTitleAry,colWidthAry,array,null,res,"消金地区设置",1);
    }

    @Override
    public List<SysWorkplaceset> queryWorkPlaceByParam(String data) throws Exception {
        List<SysWorkplaceset> list =new ArrayList<>();
        if (data!=null){
            ObjectMapper mapper=new ObjectMapper();
            list=sysWorkplacesetMapper.queryAllWorkplaceset(mapper.readValue(data.getBytes("utf-8"), SysWorkplaceset.class));
        }
        return list;
    }

    @Override
    public JSONObject buildCityLevelByBl(String businessLine) throws Exception {
        JSONObject obj=new JSONObject();
        obj.put("code",200);
        List<SysConfigType> list=new ArrayList<>();
        obj.put("cityLeveList",list);
        try {
            switch (businessLine){
                case "1"://总部
                    obj.put("cityLeveList",sysConfigTypeService.getConfigTypesByPSysCode(Constant.CITY_LEVEL_HQ));
                    break;
                case "2"://消分
                    obj.put("cityLeveList",sysConfigTypeService.getConfigTypesByPSysCode(Constant.CITY_LEVEL_XJ));
                    break;
                case "3"://信贷
                    obj.put("cityLeveList",sysConfigTypeService.getConfigTypesByPSysCode(Constant.CITY_LEVEL_XD));
                    break;
            }
        } catch (Exception e) {
            obj.put("code",500);
            obj.put("msg",e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
}
