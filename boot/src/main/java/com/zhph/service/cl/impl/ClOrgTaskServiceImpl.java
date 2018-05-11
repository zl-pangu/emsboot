package com.zhph.service.cl.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.exception.AppException;
import com.zhph.mapper.cl.ClGzymMapper;
import com.zhph.mapper.cl.ClOrgTaskMapper;
import com.zhph.mapper.common.ZhphObjectMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.sys.SysWorkplacesetMapper;
import com.zhph.model.cl.ClOrgTask;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.sys.SysWorkplaceset;
import com.zhph.service.cl.ClOrgTaskService;
import com.zhph.util.CommonUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;
@Service
@Transactional(rollbackFor = Exception.class)
public class ClOrgTaskServiceImpl implements ClOrgTaskService {

	@Autowired
	private ClOrgTaskMapper clOrgTaskMapper;
	@Autowired
	private HqclcfDeptMapper hqclcfDeptMapper;
	@Autowired
	private ZhphObjectMapper zhphObjectMapper;
	@Autowired
	private ClGzymMapper clGzymMapper;
	@Autowired
	private SysWorkplacesetMapper sysWorkplacesetMapper;
	
	@Override
	public Grid<Map<String, String>> queryPageInfo(PageBean pageBean, ClOrgTask params) {
		PageHelper.startPage(pageBean.getPage(),pageBean.getLimit());
		List<Map<String, String>> lists = clOrgTaskMapper.queryList(params);
		
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
	public void importExl(MultipartFile file, HttpServletRequest req, HttpServletResponse res) throws Exception {
		InputStream inputStream = file.getInputStream();
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int trLength = sheet.getLastRowNum();
        HSSFRow row = sheet.getRow(0);
        short tdLength = row.getLastCellNum();
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");//正整数
        
        if (trLength == 0)
            throw new AppException("未导入任何数据！");

        //查询所有信贷分公司信息
        SysWorkplaceset sysWorkplaceset = new SysWorkplaceset();
		sysWorkplaceset.setBusinessLine("3");
		List<SysWorkplaceset> queryAllOrgList = sysWorkplacesetMapper.queryAllWorkplaceset(sysWorkplaceset);
		Map<String,SysWorkplaceset> orgMap=new HashMap<>();
		for (SysWorkplaceset sysWorkplaceset2 : queryAllOrgList) {
			orgMap.put(sysWorkplaceset2.getAreaCode(), sysWorkplaceset2);
		}

        //查询工资年月分公司销售任务
        ClOrgTask params = new ClOrgTask();
        String currentGzym = clGzymMapper.queryCurrGzym().getCurrentGzym();
        params.setGzYm(currentGzym);
        List<ClOrgTask> queryAllOrgTask = clOrgTaskMapper.queryAllOrgTask(params);
        Map<String,ClOrgTask> orgTaskMap=new HashMap<>();
        for (ClOrgTask orgTask:queryAllOrgTask) {
        	orgTaskMap.put(orgTask.getGzYm(),orgTask);
        }

        if (queryAllOrgTask.size()>0) {
        	clOrgTaskMapper.delByGzYm(currentGzym);
        }

        SysWorkplaceset sysWorkplaceset2 = new SysWorkplaceset();
        List<ClOrgTask> list=new ArrayList<>();
        for (int i = 1; i < trLength + 1; i++) {
        	ClOrgTask clOrgTask=new ClOrgTask();
            HSSFRow tr = sheet.getRow(i);
            
            for (int j = 0; j < tdLength; j++) {
            	 HSSFCell cell = tr.getCell(j);
            	switch (j) {
				case 0:
					if(cell==null)
						throw new AppException("分公司不能为空！");
					break;

				case 1:
					if(cell==null)
						throw new AppException("销售任务不能为空！");
					break;
				}
                
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String cellValue = cell.getStringCellValue().trim();
                sysWorkplaceset2.setArea(cellValue);
                sysWorkplaceset2.setBusinessLine("3");
                List<SysWorkplaceset> queryAll =sysWorkplacesetMapper.queryAllWorkplaceset(sysWorkplaceset2);
             
                String areaCode =null;
                for (SysWorkplaceset sysWorkplaceset3 : queryAll) {
                	areaCode = sysWorkplaceset3.getAreaCode();
				}
                switch (j) {
                case 0:

                if (orgMap.get(areaCode)==null)
                    throw new AppException("分公司不存在！");

                clOrgTask.setDeptNo(areaCode);
                break;
                case 1:
                	if (!pattern.matcher(cellValue).matches())
                        throw new Exception("销售任务必须为正整数");
                	clOrgTask.setSalesTarget(cellValue);
                    break;
                }
            }
            	clOrgTask.setGzYm(currentGzym);
                clOrgTask.setCreateDate(new Date());
                clOrgTask.setCreateName(CommonUtil.getOnlineFullName());
                list.add(clOrgTask);
        }
        //导入进去
        	clOrgTaskMapper.insertSomething(list);
	}

	@Override
	public void buildListTpl(HttpServletRequest req, ModelAndView modelAndView) throws Exception {
		Json json=new Json();
        ObjectMapper mapper=new ObjectMapper();
        //部门
        HqclcfDept dept=new HqclcfDept();
        SysWorkplaceset sysWorkplaceset = new SysWorkplaceset();
        sysWorkplaceset.setBusinessLine("3");
        modelAndView.addObject("orgNoListTpl", zhphObjectMapper.writeValueAsString(json.getObj(sysWorkplacesetMapper.queryAllWorkplaceset(sysWorkplaceset))));
        modelAndView.addObject("orgNoList", sysWorkplacesetMapper.queryAllWorkplaceset(sysWorkplaceset));
	}
}
	
	
