package com.zhph.service.cl.impl;

import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.mapper.cl.ClAshBlackMenuMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.model.cl.ClAshBlackMenuModel;
import com.zhph.model.sys.SysConfigType;
import com.zhph.service.common.BaseService;
import com.zhph.service.cl.ClAshBlackMenuService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Create by zhaoqixiang
 * Description: 灰黑名单服务
 * 2018/1/16
 **/
@Service
public class ClAshBlackMenuServiceImpl implements ClAshBlackMenuService {
    @Autowired
    private HqclcfEmpMapper HqclcfEmpMapper;
    @Autowired
    private SysConfigTypeMapper sysConfigTypeMapper;
    @Autowired
    private BaseService baseService;
    @Autowired
    private ClAshBlackMenuMapper ClAshBlackMenuMapper;

    public static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ClAshBlackMenuServiceImpl.class);

    @Override
    public Grid<ClAshBlackMenuModel> queryPageInfo(PageBean pageBean, ClAshBlackMenuModel params) {
        PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
        if (StringUtil.isNotEmpty(params.getDeptNo())) {//根据部门编码查询当前

        }
        List<ClAshBlackMenuModel> list = ClAshBlackMenuMapper.queryAllAhsBlack(params);
        PageInfo<ClAshBlackMenuModel> pageInfo = new PageInfo<>(list);
        Grid<ClAshBlackMenuModel> grid = new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
    }

    @Override
    public void exportExl(ClAshBlackMenuModel params, HttpServletRequest ret, HttpServletResponse res) {
       /* try {
            List<HqclcfLeave> list = hqclcfLeaveMapper.queryAllLeave(params);
            String[] colTitleAry = {"序号", "部门", "职位", "职务", "职级", "员工工号", "姓名", "入职时间", "离职时间", "离职原因", "备注"};
            String[][] array = new String[list.size()][colTitleAry.length];
            short[] colWidthAry = {80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 200};
            for (int i = 0; i < list.size(); i++) {
                HqclcfLeave hqclcfLeave = list.get(i);
                array[i][0] = i + 1 + ""; //序号
                array[i][1] = hqclcfLeave.getDeptName();//部门
                array[i][2] = hqclcfLeave.getPostName();//职位
                array[i][3] = hqclcfLeave.getPositionName();//职务
                array[i][4] = hqclcfLeave.getRankName();//职级
                array[i][5] = hqclcfLeave.getEmpNo();//员工工号
                array[i][6] = hqclcfLeave.getEmpName();//员工姓名
                array[i][7] = DateUtil.parseDateFormat(hqclcfLeave.getEntryTime(), "yyyy-MM-dd");//入职时间
                array[i][8] = DateUtil.parseDateFormat(hqclcfLeave.getExitTime(), "yyyy-MM-dd");//离职时间
                SysConfigType sysConfigType = sysConfigTypeMapper.querySingleBySysVal(hqclcfLeave.getLeavingReason(), Constant.DIMISSION_TYPE);
                array[i][9] = sysConfigType.getSysName();//离职原因
                array[i][10] = hqclcfLeave.getAppOpinions();//意见
            }
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "离职淘汰");
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
            e.printStackTrace();
        }*/
    }

    @Override
    public Json importExl(MultipartHttpServletRequest request) {
        //查询系统配置的里面是否有该文件映射名称
        Json json = new Json();
        List<SysConfigType> sysConfigTypeList = sysConfigTypeMapper.getConfigByPSysCode("ash_black");
        if (sysConfigTypeList.size() > 0) {
            for (SysConfigType sysConfigType : sysConfigTypeList) {
                MultipartFile file = request.getFile(sysConfigType.getSysCode());//读取文件
                if (file != null) {
                    String type = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1, file.getOriginalFilename().length());
                    if (Constant.IMPORT_FILE_TYPE_XLS.equals(type) || Constant.IMPORT_FILE_TYPE_XLSX.equals(type)) {//校验文件类型
                        ExcelUtil excelUtil = new ExcelUtil();
                        try {
                            ImportParams params = new ImportParams();
                            params.setHeadRows(1);
                            params.setNeedVerfiy(true);
                            List<ClAshBlackMenuModel> models = excelUtil.readExecl(file.getInputStream(), ClAshBlackMenuModel.class, params);//解析xls为对象
                            if (models.size() > 0) {
                                //数据入库
                                ClAshBlackMenuMapper.importAhsBlackData(models);
                                json.setMsg("导入数据成功");
                                json.setSuccess(true);
                                return json;
                            } else {
                                json.setSuccess(false);
                                json.setMsg("导入数据模板不正确请检查");
                                return json;
                            }
                        } catch (Exception e) {
                            json.setMsg("导入exl时出现:" + e.getMessage());
                            json.setSuccess(false);
                            return json;
                        }
                    } else {
                        json.setSuccess(false);
                        json.setMsg("只能导入xls和xlsx");
                    }
                } else {
                    json.setMsg("没有相关文件导入");
                    json.setSuccess(false);
                    return json;
                }
            }

        } else {
            json.setMsg("没有匹配到相关文件映射");
            json.setSuccess(false);
            return json;
        }
        return json;

    }
}