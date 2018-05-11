package com.zhph.service.sys.imp;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhph.commons.ConstantCtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.sys.SysZhphBankMapper;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.sys.SysConfigType;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.model.sys.SysZhphBank;
import com.zhph.service.sys.SysZhphBankService;
import com.zhph.util.CommonUtil;
import com.zhph.util.ExcelUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.springframework.web.servlet.ModelAndView;

@Service
@Transactional(rollbackFor = Exception.class)
public class SysZhphBankServiceImpl implements SysZhphBankService {

    public static final Logger logger = LogManager.getLogger(SysZhphBankServiceImpl.class);

    @Autowired
    private SysZhphBankMapper sysZhphBankMapper;
    @Autowired
    private HqclcfEmpMapper hqclcfEmpMapper;

    @Override
    public Grid<SysZhphBank> queryPageInfo(PageBean pageBean, SysZhphBank params) {
        PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
        List<SysZhphBank> list = sysZhphBankMapper.queryAllZhphBank(params);
        PageInfo<SysZhphBank> pageInfo = new PageInfo<>(list);
        Grid<SysZhphBank> grid = new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
    }


    @Override
    public Integer checkBank(String bankCode) {
        return sysZhphBankMapper.findBankCountById(bankCode);
    }


    @Override
    public void del(SysZhphBank readValue) throws Exception{
            if (readValue.getPriNumber() == null) {
                throw new AppException("id不能为空");
            }
             int empByBankCode = hqclcfEmpMapper.empByBankCode(readValue.getBankCode());
            if (empByBankCode >0) {
            	throw new AppException("该银行卡在员工信息已被使用，不能删除！");
            } else {
                sysZhphBankMapper.delById(readValue.getPriNumber());
            }
    }


    @Override
    public Json add(SysZhphBank readValue) {
        Json json = new Json();
        try {
            Date date = new Date();
            readValue.setCreateName(CommonUtil.getOnlineFullName());
            readValue.setCreateTime(date.toString());
            //少工资年月
            sysZhphBankMapper.addByObj(readValue);
            json.setSuccess(true);
            json.setMsg("新增成功！");
        } catch (Exception e) {
            json.setSuccess(false);
            json.setMsg(e.getMessage());
            logger.error("新增失败：" + e.getMessage());
            e.printStackTrace();
        }
        return json;
    }


    @Override
    public void exportExl(SysZhphBank data, HttpServletRequest req, HttpServletResponse res) {
        try {
            List<SysZhphBank> list = sysZhphBankMapper.queryAllZhphBank(data);
            String[] colTitleAry = {"序号", "银行编码", "银行名称", "银行全称", "状态", "备注"};
            String[][] array = new String[list.size()][colTitleAry.length];
            short[] colWidthAry = {80, 100, 100, 100, 100, 200};
            for (int i = 0; i < list.size(); i++) {
                SysZhphBank bank = list.get(i);
                array[i][0] = i + 1 + ""; //序号
                array[i][1] = bank.getBankCode();//银行编码
                array[i][2] = bank.getBankName();//银行名称
                array[i][3] = bank.getBankFullName();//银行全称
                array[i][4] = "1".equals(bank.getStatus()) ? "启用" : "禁用"; //状态
                array[i][5] = bank.getRemark();
            }
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "银行卡设置", 1);
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public Json editById(String data) {
        Json json = new Json();
        try {
            ObjectMapper mapper = new ObjectMapper();
            SysZhphBank params = mapper.readValue(data.getBytes("utf-8"), SysZhphBank.class);
            params.setUpdateName(CommonUtil.getOnlineFullName());
            params.setUpdateTime(new Date().toString());
            sysZhphBankMapper.updateById(params);
            json.setSuccess(true);
        } catch (IOException e) {
            json.setMsg(e.getMessage());
            logger.error("修改失败：" + e.getMessage());
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public void showBtnList(HttpServletRequest request, ModelAndView modelAndView) {
        //按钮权限 根据当前session中存的资源权限
        List<String> resourcesUrl = (List<String>) request.getSession().getAttribute("resourcesUrl");
        if (resourcesUrl.contains(ConstantCtl.BANK_CTLREQM + ConstantCtl.ADD)) {
            //是否展示新增按钮
            modelAndView.addObject("addBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("addBtn", Constant.DISABLE);
        }
        if (resourcesUrl.contains(ConstantCtl.BANK_CTLREQM + ConstantCtl.EDIT)) {
            //是否展示编辑按钮
            modelAndView.addObject("editBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("editBtn", Constant.DISABLE);
        }
        if (resourcesUrl.contains(ConstantCtl.BANK_CTLREQM + ConstantCtl.QUERY)) {
            //是否展示查询按钮
            modelAndView.addObject("queryBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("queryBtn", Constant.DISABLE);
        }
        if (resourcesUrl.contains(ConstantCtl.BANK_CTLREQM + ConstantCtl.DEL)) {
            //是否展示删除按钮
            modelAndView.addObject("delBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("delBtn", Constant.DISABLE);
        }
        //是否展示导出按钮
        if (resourcesUrl.contains(ConstantCtl.BANK_CTLREQM + ConstantCtl.EXPORTEXL)) {
            modelAndView.addObject("exportExlBtn", Constant.ENABLE);
        } else {
            modelAndView.addObject("exportExlBtn", Constant.DISABLE);
        }
    }
}
