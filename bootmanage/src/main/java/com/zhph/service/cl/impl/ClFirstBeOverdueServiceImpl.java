package com.zhph.service.cl.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.config.datasource.DataSourceNameList;
import com.zhph.config.datasource.DataSourceSelector;
import com.zhph.mapper.cl.ClFirstBeOverdueMapper;
import com.zhph.mapper.cl.ClGzymMapper;
import com.zhph.mapper.common.ZhphObjectMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.sys.SysWorkplacesetMapper;
import com.zhph.model.cl.ClFirstBeOverdue;
import com.zhph.model.cl.ClOrgTask;
import com.zhph.model.hqclcf.HqclcfBusiness;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.hqclcf.HqclcfRank;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.model.sys.SysZhphBank;
import com.zhph.service.cl.ClFirstBeOverdueService;
import com.zhph.service.common.BaseService;
import com.zhph.service.sys.imp.SysZhphBankServiceImpl;
import com.zhph.util.ExcelUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;

@Service
@Transactional(rollbackFor = Exception.class)
public class ClFirstBeOverdueServiceImpl implements ClFirstBeOverdueService {
	
	public static final Logger logger = LogManager.getLogger(ClFirstBeOverdueServiceImpl.class);
	
	@Autowired
	private ClFirstBeOverdueMapper clFirstBeOverdueMapper;
	@Autowired
	private HqclcfDeptMapper hqclcfDeptMapper;
	@Resource
	private BaseService baseService;
	@Autowired
	private ZhphObjectMapper zhphObjectMapper;
	@Autowired
	private SysWorkplacesetMapper sysWorkplacesetMapper;
	@Autowired 
	private ClGzymMapper clgzymMapper;
	@Override
	public Grid<Map<String, String>> queryPageInfo(PageBean pageBean, ClFirstBeOverdue params) {
		PageHelper.startPage(pageBean.getPage(),pageBean.getLimit());
		List<Map<String, String>> lists = clFirstBeOverdueMapper.queryList(params);
		
		for (Map<String, String> map : lists) {
			String deptInfo = null != map.get("deptInfo") ? map.get("deptInfo") : "";
			if (!"".equals(deptInfo)) {
                String[] deptTemp;
                String[] depts = deptInfo.split(",");
                for (String dept : depts) {
                    deptTemp = dept.split("=");
                    switch (deptTemp[0]){
                    case "1":
                        map.put("region",deptTemp[1]);
                        break;
                    case "2":
                        map.put("filiale",deptTemp[1]);
                        break;
                    case "3":
                        map.put("dept",deptTemp[1]);
                        break;
                    case "4":
                        map.put("team",deptTemp[1]);
                        break;
                    }
                }
            }
        }

        PageInfo<Map<String, String>> pageInfo=new PageInfo<>(lists);
        Grid<Map<String,String>> grid=new Grid<>();
        grid.setCount(pageInfo.getTotal());
        grid.setData(pageInfo.getList());

        return grid;
	}

	@Override
	public void exportExl(ClFirstBeOverdue data, HttpServletRequest req, HttpServletResponse res) {
		try {
			//查询所有信贷分公司信息
//	        List<HqclcfDept> deptList = hqclcfDeptMapper.queryOrgParams();
//	        Map<String,String> deptMap=new HashMap<>();
//	        for (HqclcfDept dept:deptList) {
//	        	deptMap.put(dept.getDeptNo(),dept.getDeptName());
//	        }
	        
			SysWorkplaceset sysWorkplaceset = new SysWorkplaceset();
			sysWorkplaceset.setBusinessLine("3");
			List<SysWorkplaceset> queryAllOrgList = sysWorkplacesetMapper.queryAllWorkplaceset(sysWorkplaceset);
			Map<String,String> orgMap=new HashMap<>();
			for (SysWorkplaceset sysWorkplaceset2 : queryAllOrgList) {
				orgMap.put(sysWorkplaceset2.getAreaCode(), sysWorkplaceset2.getArea());
			}
			
            List<ClFirstBeOverdue> list = clFirstBeOverdueMapper.queryAllFirstBeOverdueExl(data);
            String[] colTitleAry = {"序号","合同号","客户姓名","业务经理姓名","团队经理","团队经理编号","门店销售经理","门店销售经理编号","区域经理","区域经理编号","放款时间","逾期天数","合同额","放款额","分公司","年月"};
            String[][] array = new String[list.size()][colTitleAry.length];
            short[] colWidthAry = {80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};
            for (int i = 0; i < list.size(); i++) {
                ClFirstBeOverdue first = list.get(i);
                array[i][0] = i + 1 + ""; //序号
                array[i][1] = first.getLoanContractNo();//合同号
                array[i][2] = first.getLoanName();//贷款人姓名
                array[i][3] = first.getBusinessManagerName();//业务经理姓名
                array[i][4] = first.getTeamManagerName();//团队经理姓名
                array[i][5] = first.getTeamManagerNo();//团队经理编号
                array[i][6] = first.getOrgManagerName();//城市经理姓名
                array[i][7] = first.getOrgManagerNo();//城市经理编号
                array[i][8] = first.getAreaManagerName();//区域经理姓名
                array[i][9] = first.getAreaManagerNo();//区域经理编号
                array[i][10] = first.getPayDate();//实际放款日
                array[i][11] = first.getBeOverdueNum();//逾期天数
                array[i][12] = first.getLoanAmount();//合同额
                array[i][13] = first.getGrantLoanAmount();//放款额
                if (first.getOrgNo() != null) {
                    array[i][14] = orgMap.get(first.getOrgNo());
                } else {
                	
                    array[i][14] = null;
                }
                array[i][15] = first.getGzYm();//工资年月
            }
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "首逾明细",1);
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
            e.printStackTrace();
        }
		
	}

	@Override
	public void buildListTpl(HttpServletRequest req, ModelAndView modelAndView) throws Exception {
		Json json=new Json();
        ObjectMapper mapper=new ObjectMapper();
        //部门
        HqclcfDept dept=new HqclcfDept();
        modelAndView.addObject("orgNoListTpl", zhphObjectMapper.writeValueAsString(json.getObj(sysWorkplacesetMapper.queryAllWorkplaceset(new SysWorkplaceset()))));
	}

	@Override
	@DataSourceSelector(DataSourceNameList.BUSI_DATASOURCE)
	public void ClFirstBeOverduTime() throws Exception {
		//查询所有信贷分公司信息
        List<HqclcfDept> deptList = hqclcfDeptMapper.queryOrgParams();
        Map<String,HqclcfDept> deptMap=new HashMap<>();
        for (HqclcfDept dept:deptList) {
        	deptMap.put(dept.getDeptCode(),dept);
        }
        List<ClFirstBeOverdue> list=new ArrayList<>();
		String currentGzym = clgzymMapper.queryCurrGzym().getCurrentGzym();
		List<ClFirstBeOverdue> queryClFirstBeOverdue = clFirstBeOverdueMapper.queryClFirstBeOverdue(currentGzym);
		ClFirstBeOverdue clFirstBeOverdue = new ClFirstBeOverdue();
		for (int i = 0; i < queryClFirstBeOverdue.size(); i++) {
			clFirstBeOverdue.setLoanContractNo(queryClFirstBeOverdue.get(i).getLoanContractNo());//合同编号
			clFirstBeOverdue.setLoanName(queryClFirstBeOverdue.get(i).getLoanName());//贷款人姓名
			clFirstBeOverdue.setLoanAmount(queryClFirstBeOverdue.get(i).getLoanAmount());//合同额
			clFirstBeOverdue.setGrantLoanAmount(queryClFirstBeOverdue.get(i).getGrantLoanAmount()); //放款额
			clFirstBeOverdue.setGrantLoanDate(queryClFirstBeOverdue.get(i).getGrantLoanDate());//约定放款日
			clFirstBeOverdue.setPayDate(queryClFirstBeOverdue.get(i).getPayDate());//实际放款日
            clFirstBeOverdue.setBusinessManagerNo(queryClFirstBeOverdue.get(i).getBusinessManagerNo());//业务经理编号
			clFirstBeOverdue.setBusinessManagerName(queryClFirstBeOverdue.get(i).getBusinessManagerName());//业务经理名字
			clFirstBeOverdue.setTeamManagerNo(queryClFirstBeOverdue.get(i).getTeamManagerNo());//团队经理编号
			clFirstBeOverdue.setTeamManagerName(queryClFirstBeOverdue.get(i).getTeamManagerName());//团队经理名字
			clFirstBeOverdue.setOrgManagerNo(queryClFirstBeOverdue.get(i).getOrgManagerNo());//城市经理编号
			clFirstBeOverdue.setOrgManagerName(queryClFirstBeOverdue.get(i).getOrgManagerName());//城市经理名称
			clFirstBeOverdue.setAreaManagerNo(queryClFirstBeOverdue.get(i).getAreaManagerNo());//区域经理编号
			clFirstBeOverdue.setAreaManagerName(queryClFirstBeOverdue.get(i).getAreaManagerName());//区域经理名称
			clFirstBeOverdue.setOrgNo(queryClFirstBeOverdue.get(i).getOrgNo());//分公司编号
			clFirstBeOverdue.setDeptNo(queryClFirstBeOverdue.get(i).getDeptNo());//部门编号
			clFirstBeOverdue.setBeOverdueNum(queryClFirstBeOverdue.get(i).getBeOverdueNum());//逾期天数
			clFirstBeOverdue.setGzYm(currentGzym);
	}
		list.add(clFirstBeOverdue);
		clFirstBeOverdueMapper.insertSomething(list);
	}

}
