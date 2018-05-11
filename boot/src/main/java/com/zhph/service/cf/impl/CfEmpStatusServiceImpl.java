package com.zhph.service.cf.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.exception.AppException;
import com.zhph.mapper.cf.CfAttendanceDtoMapper;
import com.zhph.mapper.cf.CfEmpStatusMapper;
import com.zhph.mapper.common.ZhphObjectMapper;
import com.zhph.mapper.hqclcf.HqclcfDeptMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.mapper.hqclcf.HqclcfPostMapper;
import com.zhph.mapper.sys.SysConfigTypeMapper;
import com.zhph.model.cf.CfEmpStatus;
import com.zhph.model.cf.dto.CfAttendanceDto;
import com.zhph.model.hqclcf.HqclcfDept;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfPost;
import com.zhph.model.sys.SysConfigType;
import com.zhph.service.cf.CfEmpStatusService;
import com.zhph.service.common.BaseService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 * Create By lishuangjiang
 * 消分员工状态变更
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CfEmpStatusServiceImpl implements CfEmpStatusService {

    @Autowired
    private CfEmpStatusMapper cfEmpStatusMapper;

    @Autowired
    private SysConfigTypeMapper seSysConfigTypeMapper;

    @Autowired
    private HqclcfEmpMapper hqclcfEmpMapper;

    @Autowired
    private CfAttendanceDtoMapper cfAttendanceDtoMapper;

    @Autowired
    private HqclcfPostMapper hqclcfPostMapper;

    @Autowired
    private HqclcfDeptMapper hqclcfDeptMapper;

    @Autowired
    private BaseService baseService;

    @Autowired
    private ZhphObjectMapper zhphObjectMapper;


    public static final Logger logger = LogManager.getLogger(CfEmpStatusServiceImpl.class);


    /**
     * 赋值Code
     *
     * @param status
     * @return
     * @throws Exception
     */
    public CfEmpStatus dtoValueCode(CfEmpStatus status) throws Exception {
        CfAttendanceDto dto = cfAttendanceDtoMapper.queryDeptDto(status.getEmpNo());
        if (dto != null) {
            if (dto.getDeptCodeInfo() != null && !dto.getDeptCodeInfo().equals("")) {
                List<String> list = Arrays.asList(dto.getDeptCodeInfo().split(","));
                for (String s : list) {
                    switch (s.substring(0, 1)) {
                        case "1":
                            status.setDeptArea(s.substring(2).toString());// --> 大区Code
                            break;
                        case "2":
                            status.setOrgNo(s.substring(2).toString());//    --> 分中心Code
                            break;
                        case "4":
                            status.setDeptCode(s.substring(2).toString());// --> 团队Code
                            break;
                        case "3":
                            status.setSalesDept(s.substring(2).toString());//-->营业部Code
                            break;
                    }
                }
            }
        }
        return status;
    }


    @Override
    public CfEmpStatus queryEmpByEmpNo(String empNo) throws Exception {
        return null;
    }

    @Override
    public int insertSelective(CfEmpStatus empStatus) throws Exception {
        return 0;
    }

    private Boolean isOnlive(String empNo) throws Exception {
        /*需判断是否当前员工是否存在未结束状态管理*/
        try {
            CfEmpStatus isEndEmpStatus = new CfEmpStatus();
            isEndEmpStatus.setEmpNo(empNo);
            isEndEmpStatus.setSysUser(baseService.getOnlineUserBl());
            List<Map<String, String>> isEnd = cfEmpStatusMapper.queryList(isEndEmpStatus);
            for (Map<String, String> c : isEnd) {
                if (c.get("endDate") == null || c.get("endDate").equals("")) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public JSONObject insert(String data) throws Exception {

        JSONObject json = new JSONObject();

        CfEmpStatus empStatus = zhphObjectMapper.readValue(data, CfEmpStatus.class);

        /*需判断是否当前员工是否存在未结束状态管理*/
        Boolean flag = isOnlive(empStatus.getEmpNo());
        if (!flag) {
            json.put("code", "501");
            json.put("msg", "当前员工存在未结束记录！");
            return json;
        }

        empStatus.setCreator(CommonUtil.getOnlineFullName()); //creator
        empStatus.setCreateDate(DateUtil.parseDateFormat(new Date(), "yyyy-MM-dd")); //createDate

        try {
            int result = cfEmpStatusMapper.insert(empStatus);
            if (result > 0) {
                //新增成功同时将当前员工的状态同步  需求变更为定时,如果为当前时间则立即生效
                int timeResult = DateUtil.compare_date(DateUtil.parseStringToDate(empStatus.getStartDate(), "yyyy-MM-dd"), new Date());
                if (timeResult == -1 || timeResult == 0) {
                    HqclcfEmp emp = hqclcfEmpMapper.queryEmpByEmpNo(empStatus.getEmpNo());
                    switch (empStatus.getStatus()) {
                        case 401:
                            emp.setStatus(3);
                            break;
                        case 402:
                            emp.setStatus(4);
                            break;
                    }
                    hqclcfEmpMapper.updateEmpStatusByParams(emp);
                }
                json.put("code", "200");
                return json;
            } else {
                json.put("code", "501");
                json.put("msg", "新增异常！");
                return json;
            }
        } catch (Exception e) {
            throw new AppException("新增员工状态管理异常" + e.getMessage());
        }

    }

    @Override
    public CfEmpStatus queryEmpByPriMarkey(Long id) throws Exception {
        return null;
    }

    @Override
    public JSONObject updateEmpStatuses(String data) throws Exception {

        /**
         * update cf_emp_status
         */
        JSONObject json = new JSONObject();

        CfEmpStatus empStatus = zhphObjectMapper.readValue(data, CfEmpStatus.class);

        empStatus.setUpdator(CommonUtil.getOnlineFullName());
        empStatus.setUpdateDate(DateUtil.parseDateFormat(new Date(), "yyyy-MM-dd"));

        try {
            int result = cfEmpStatusMapper.updateEmpStatuses(empStatus);
            if (result > 0) {

                int timeResult = DateUtil.compare_date(DateUtil.parseStringToDate(empStatus.getStartDate(), "yyyy-MM-dd"), new Date());
                if (timeResult == -1 || timeResult == 0) {
                    HqclcfEmp emp = hqclcfEmpMapper.queryEmpByEmpNo(empStatus.getEmpNo());
                    switch (empStatus.getStatus()) {
                        case 402:
                            emp.setStatus(4);
                            break;
                        case 401:
                            emp.setStatus(3);
                            break;
                    }
                    hqclcfEmpMapper.updateEmpStatusByParams(emp);
                }


                json.put("code", "200");
                return json;
            } else {
                json.put("code", "501");
                json.put("msg", "修改中异常！");
                return json;
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            json.put("code", "200");
            json.put("msg", "修改过程异常！" + e.getMessage());
            return json;
        }
    }

    @Override
    public ModelAndView setEditEmpSatatus(ModelAndView model, Long priNumber) throws Exception {
        /**
         * get obj of cf_emp_status
         */
        CfEmpStatus cfEmpStatus = cfEmpStatusMapper.queryEmpByPriMarkey(priNumber);

        List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("", "", new ArrayList<Integer>()); //员工data
        Map<String, HqclcfEmp> empMap = new HashMap<>();
        for (HqclcfEmp emp : emps) {
            empMap.put(emp.getEmpNo(), emp);
        }

        if (empMap.containsKey(cfEmpStatus.getEmpNo())) {
            cfEmpStatus.setEmpName(empMap.get(cfEmpStatus.getEmpNo()).getEmpName());//员工姓名
            if (empMap.get(cfEmpStatus.getEmpNo()).getEnterDate() != null && !"".equals(empMap.get(cfEmpStatus.getEmpNo()).getEnterDate())) {
                cfEmpStatus.setEnterDate(DateUtil.parseDateFormat(empMap.get(cfEmpStatus.getEmpNo()).getEnterDate(), "yyyy-MM-dd"));
            }//入职时间
            if (null != empMap.get(cfEmpStatus.getEmpNo()).getPost() && !"".equals(empMap.get(cfEmpStatus.getEmpNo()).getPost())) {
                HqclcfPost post = hqclcfPostMapper.queryByPostNo(empMap.get(cfEmpStatus.getEmpNo()).getPost());
                if (null != post) {
                    cfEmpStatus.setJobName(post.getPostName());
                }
            }
        }

        /* deptArea orgNo salesDept deptCode */
        CfAttendanceDto dto = cfAttendanceDtoMapper.queryDeptDto(cfEmpStatus.getEmpNo());
        if (dto != null) {
            if (dto.getDeptInfo() != null && !dto.getDeptInfo().equals("")) {
                List<String> list = Arrays.asList(dto.getDeptInfo().split(","));
                StringBuffer deptArea = null;
                StringBuffer orgNo = null;
                StringBuffer salesDept = null;
                StringBuffer deptCode = null;

                for (String s : list) {
                    if (s.substring(0, 1).equals("1")) {
                        deptArea = new StringBuffer(s.substring(2).toString());
                    }
                    if (s.substring(0, 1).equals("2")) {
                        orgNo = new StringBuffer(s.substring(2).toString());
                    }
                    if (s.substring(0, 1).equals("4")) {
                        deptCode = new StringBuffer(s.substring(2).toString());
                    }
                    if (s.substring(0, 1).equals("3")) {
                        salesDept = new StringBuffer(s.substring(2).toString());
                    }
                }
                StringBuffer buffer = new StringBuffer((deptArea == null ? "" : deptArea + "-") + (orgNo == null ? "" : orgNo + "-") + (salesDept == null ? "" : salesDept + "-") + (deptCode == null ? "" : deptCode));
                String str = buffer.substring(buffer.lastIndexOf("-") + 1);
                if (!str.equals("") && null != str) {
                    cfEmpStatus.setDeptName(buffer.toString());
                } else {
                    cfEmpStatus.setDeptName(buffer.substring(0, buffer.lastIndexOf("-")));
                }
            }

        }
        if (cfEmpStatus != null) {
            model.addObject("empStatus", cfEmpStatus);
        }
        return model;
    }

    @Override
    public JSONObject importExl(MultipartFile file, HttpServletRequest req, HttpServletResponse res) throws Exception {

        JSONObject json = new JSONObject();
        InputStream inputStream = file.getInputStream();

        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheetAt(0);
        int trLength = sheet.getLastRowNum();
        HSSFRow row = sheet.getRow(0);
        short tdLength = row.getLastCellNum();

        if (trLength == 0) {
            json.put("code", "501");
            json.put("msg", "没有导入任何数据！");
            return json;
        }


        //查询消金系统全部的员工信息
        HqclcfEmp queryParm = new HqclcfEmp();
        queryParm.setBusinessLine(2);
        List<HqclcfEmp> empList = hqclcfEmpMapper.queryAllEmp(queryParm);
        Map<String, HqclcfEmp> empMap = new HashMap<>();
        for (HqclcfEmp emp : empList) {
            empMap.put(emp.getEmpNo(), emp);
        }

        List<CfEmpStatus> list = new ArrayList<>();
        for (int i = 1; i < trLength + 1; i++) {
            CfEmpStatus status = new CfEmpStatus();
            HSSFRow tr = sheet.getRow(i);
            for (int j = 0; j < tdLength; j++) {
                HSSFCell cell = tr.getCell(j);
                if (cell != null) {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                }
                String cellValue = cell.getStringCellValue().trim();
                switch (j) {
                    case 0:
                        if ("".equals(cellValue)) {
                            json.put("code", "502");
                            json.put("msg", "员工编码不能为空！");
                            return json;
                        }

                        if (empMap.get(cellValue.trim()) == null) {
                            json.put("code", "503");
                            json.put("msg", "员工编码不存在！");
                            return json;
                        }

                        if (!isOnlive(cellValue.trim())) {
                            json.put("code", "503");
                            json.put("msg", "当前员工存在未结束记录！");
                            return json;
                        }

                        for (CfEmpStatus e : list) {
                            if (null != e && null != e.getEmpNo()) {
                                if (cellValue.trim().equals(e.getEmpNo())) {
                                    json.put("code", "508");
                                    json.put("msg", "导入数据中存在重复员工编码！");
                                    return json;
                                }
                            }
                        }

                        HqclcfEmp emp = empMap.get(cellValue);
                        status.setEmpNo(emp.getEmpNo());//员工编码
                        status.setJobName(emp.getPost());//职位名称
                        status = dtoValueCode(status); //部门全部code
                        break;
                    case 1:
                        if ("".equals(cellValue)) {
                            json.put("code", "504");
                            json.put("msg", "员工姓名不能为空！");
                            return json;
                        }
                        HqclcfEmp oughtName = empMap.get(status.getEmpNo());
                        if (null != oughtName) {
                            if (!oughtName.getEmpName().equals(cellValue.trim())) {
                                json.put("code", "509");
                                json.put("msg", "员工姓名不一致！");
                                return json;
                            }
                            status.setEmpName(cellValue.trim());
                        }
                        break;
                    case 2:
                        if ("".equals(cellValue)) {
                            json.put("code", "505");
                            json.put("msg", "状态不能为空！");
                            return json;
                        } else if ("停薪停职".equals(cellValue.trim())) {
                            status.setStatus(401);
                        } else if ("停薪留职".equals(cellValue.trim())) {
                            status.setStatus(402);
                        } else {
                            throw new AppException("状态码不正确！");
                        }
                        break;
                    case 3:
                        if ("".equals(cellValue)) {
                            json.put("code", "506");
                            json.put("msg", "开始时间不能为空！");
                            return json;
                        }
                        Date start = null;
                        start = DateUtil.parseStringToDate(cellValue, "yyyy-MM-dd");
                        if (null != start) {
                            status.setStartDate(DateUtil.parseDateFormat(start, "yyyy-MM-dd"));
                        } else {
                            json.put("code", "507");
                            json.put("msg", "开始时间格式不正确！");
                            return json;
                        }
                        break;
                    case 4:
                        status.setRemark(!"".equals(cellValue) ? cellValue : "无");
                        break;

                }
            }
            status.setCreateDate(DateUtil.parseDateFormat(new Date(), "yyyy-MM-dd"));
            status.setCreator(CommonUtil.getOnlineFullName());
            list.add(status);
        }

        //导入进去
        int result = cfEmpStatusMapper.batchInsertList(list);
        if (result > 0) {
            json.put("code", "200");
        } else {
            json.put("code", "505");
            json.put("msg", "导入数据插入异常！");
        }
        return json;
    }

    @Override
    public int delEmpStatusesByEmpNo(String empNo) throws Exception {
        return 0;
    }

    @Override
    public int delEmpStatusesById(Long id) throws Exception {
        return 0;
    }

    @Override
    public Object relieve(Long priNumber, String endDate) throws Exception {
        JSONObject json = new JSONObject();
        HqclcfEmp emp = null;
        String relieveTime = DateUtil.parseDateFormat(new Date(), "yyyy-MM");
        String endDates = DateUtil.parseDateFormat(DateUtil.parseStringToDate(endDate,"yyyy-MM"),"yyyy-MM").toString();
        CfEmpStatus empStatus = new CfEmpStatus();
        empStatus.setPriNumber(priNumber);
        empStatus.setEndDate(endDate);

        try {
            //查询该记录是否已结束
            CfEmpStatus cfEmpStatus = cfEmpStatusMapper.queryEmpByPriMarkey(priNumber);
            if (cfEmpStatus != null) {
                if (cfEmpStatus.getEndDate() != null && !cfEmpStatus.getEndDate().equals("")) {
                    json.put("code", "503");
                    json.put("msg", "此记录已结束！");
                    return json;
                } else {
                    int result = cfEmpStatusMapper.updateEmpStatuses(empStatus);
                    if (result > 0) {
                        if (relieveTime.equals(endDates) || DateUtil.compare_date(DateUtil.parseStringToDate(endDates,"yyyy-MM"),DateUtil.parseStringToDate(relieveTime,"yyyy-MM")) == -1) {
                            emp = new HqclcfEmp();
                            emp.setEmpNo(cfEmpStatus.getEmpNo());
                            emp.setStatus(1);
                            hqclcfEmpMapper.updateEmpStatusByParams(emp);
                        }
                        json.put("code", "200");
                        return json;
                    } else {
                        json.put("code", "501");
                        json.put("msg", "数据库修改失败！");
                        return json;
                    }
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            json.put("code", "502");
            json.put("msg", "异常" + e.getMessage() + "!");
            return json;
        }
        return json;
    }

    @Override
    public ModelAndView queryEmpStatusList(ModelAndView model) throws Exception {
        /**
         * 从sysConfigType获取员工可以变更的状态
         */
        Json json = new Json();
        List<SysConfigType> configTypeStatus = seSysConfigTypeMapper.getConfigByPSysCode(Constant.XJ_EMPSTATUS_LIST);
        List<SysConfigType> configTypeIsEnd = seSysConfigTypeMapper.getConfigByPSysCode(Constant.CF_EMPSTATUS_LIST_ISEND);
        List<SysConfigType> jobStatus = seSysConfigTypeMapper.getConfigByPSysCode("job_status");
        model.addObject("posts", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfPostMapper.queryAll(new HqclcfPost()))));
        model.addObject("jobStatus", jobStatus);
        model.addObject("statusList", configTypeStatus);
        model.addObject("isendList", configTypeIsEnd);

        return model;
    }

    @Override
    public ModelAndView queryDeptArea(ModelAndView model) throws Exception {
        /**
         * 获取消分下大区，分中心，营业部
         */
        HqclcfDept dept = new HqclcfDept();
        dept.setBusinessLine(2); // 消分
        dept.setDeptType("1");//大区
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(dept);
        dept.setDeptType("2");//分中心
        List<HqclcfDept> orgNo = hqclcfDeptMapper.queryAll(dept);
        dept.setDeptType("3");//营业部
        List<HqclcfDept> salesDept = hqclcfDeptMapper.queryAll(dept);

        model.addObject("salesDept", salesDept);
        model.addObject("orgNo", orgNo);
        model.addObject("deptArea", depts);
        return model;
    }


    @Override
    public JSONObject queryByq(String q, PageBean pageBean, int rows) throws Exception {

        /**
         * fully search
         */
        JSONObject json = new JSONObject();
        pageBean.setLimit(rows);
        List<Integer> IntegerList = baseService.getOnlineUserBl();//可查看权限
        PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
        List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq(q, "排空", IntegerList);

        for (HqclcfEmp emp : emps) {
            if (emp != null) {
                if (emp.getPost() != null && !emp.getPost().equals("")) {
                    HqclcfPost post = hqclcfPostMapper.queryByPostNo(emp.getPost());
                    if (post != null) {
                        String postName = post.getPostName();
                        if (postName != null && !"".equals(postName)) {
                            emp.setPost(postName);//职位
                        }
                    }
                }

                if (emp.getDeptNo() != null && !"".equals(emp.getDeptNo())) {
                    CfAttendanceDto dto = cfAttendanceDtoMapper.queryDeptDto(emp.getEmpNo());

                    if (dto != null) {
                        if (dto.getDeptInfo() != null && !dto.getDeptInfo().equals("")) {
                            List<String> list = Arrays.asList(dto.getDeptInfo().split(","));
                            StringBuffer deptArea = null;
                            StringBuffer orgNo = null;
                            StringBuffer salesDept = null;
                            StringBuffer deptCode = null;

                            for (String s : list) {
                                if (s.substring(0, 1).equals("1")) {
                                    deptArea = new StringBuffer(s.substring(2).toString());
                                }
                                if (s.substring(0, 1).equals("2")) {
                                    orgNo = new StringBuffer(s.substring(2).toString());
                                }
                                if (s.substring(0, 1).equals("3")) {
                                    salesDept = new StringBuffer(s.substring(2).toString());
                                }
                                if (s.substring(0, 1).equals("4")) {
                                    deptCode = new StringBuffer(s.substring(2).toString());
                                }

                            }
                            StringBuffer buffer = new StringBuffer((deptArea == null ? "" : deptArea + "-") + (orgNo == null ? "" : orgNo + "-") + (salesDept == null ? "" : salesDept + "-") + (deptCode == null ? "" : deptCode));
                            String str = buffer.substring(buffer.lastIndexOf("-") + 1);
                            if (!str.equals("") && null != str) {
                                emp.setDeptName(buffer.toString());
                            } else {
                                emp.setDeptName(buffer.substring(0, buffer.lastIndexOf("-")));
                            }
                        }


                    }

                }

            }

        }
        PageInfo<HqclcfEmp> pageInfo = new PageInfo<>(emps);
        Grid<HqclcfEmp> grid = new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        json.put("rows", grid.getData());
        json.put("total", grid.getCount());
        return json;
    }

    public List<CfEmpStatus> getData(CfEmpStatus status) throws Exception {
        return cfEmpStatusMapper.queryAllEmpStatuses(status);
    }

    @Override
    public Grid<Map<String, String>> queryPageInfo(PageBean pageBean, CfEmpStatus cfEmpStatus) throws Exception {
        PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
        List<Map<String, String>> lists = buildQueryList(cfEmpStatus);
        PageInfo<Map<String, String>> pageInfo = new PageInfo<>(lists);
        Grid<Map<String, String>> grid = new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
    }

    private List<Map<String, String>> buildQueryList(CfEmpStatus cfEmpStatus) throws Exception {
        try {
            List<Integer> bl = baseService.getOnlineUserBl();
            cfEmpStatus.setSysUser(bl);
            List<Map<String, String>> lists = cfEmpStatusMapper.queryList(cfEmpStatus);
            for (Map<String, String> map : lists) {

                String deptInfo = null != map.get("deptInfo") ? map.get("deptInfo") : "";
                if (!"".equals(deptInfo)) {
                    String[] deptTemp;
                    String[] depts = deptInfo.split(",");
                    for (String dept : depts) {
                        deptTemp = dept.split("=");
                        switch (deptTemp[0]) {
                            case "1":
                                map.put("deptArea", deptTemp[1]);
                                break;
                            case "4":
                                map.put("deptCode", deptTemp[1]);
                                break;
                            case "3":
                                map.put("salesDept", deptTemp[1]);
                                break;
                            case "2":
                                map.put("orgNo", deptTemp[1]);
                                break;
                        }
                    }
                }
            }
            return lists;
        } catch (Exception e) {
            throw new AppException("获取当前登录用户权限失败" + e.getMessage());
        }

    }

    @Override
    public void exportExl(CfEmpStatus cfEmpStatus, HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {

            List<Map<String, String>> lists = buildQueryList(cfEmpStatus);
            String[] colTitleAry = {"序号", "员工编码", "员工姓名", "部门", "职位", "状态", "开始时间", "结束时间", "描述"};
            String[][] array = new String[lists.size()][colTitleAry.length];
            short[] colWidthAry = {80, 120, 120, 150, 150, 150, 120, 120, 400};
            int i = 0;
            for (Map<String, String> map : lists) {
                int x = i++;
                array[x][0] = x + 1 + "";
                array[x][1] = map.get("empNo");
                array[x][2] = map.get("empName");
                String deptArea = map.get("deptArea") == null ? "" : map.get("deptArea");
                try {
                    if (!"".equals(deptArea) && null != deptArea) {
                        HqclcfEmp emp = hqclcfEmpMapper.queryEmpByEmpNo(array[x][1]);
                        if (null != emp && !"".equals(emp.getDeptNo())) {
                            array[x][3] = hqclcfDeptMapper.queryDeptByCode(emp.getDeptNo()).getDeptName();
                        }
                    } else {
                        array[x][3] = "";
                    }
                } catch (Exception e) {
                    throw new AppException("员工编码或者部门编码不规范！" + e.getMessage());
                }
                //array[x][3] = map.get("deptArea")==null?"":map.get("deptArea");//大区
                //array[x][4] = map.get("orgNo")==null?"":map.get("orgNo");//分中心
                //array[x][5] = map.get("salesDept")==null?"":map.get("salesDept");//营业部
                //array[x][6] = map.get("deptCode")==null?"":map.get("deptCode");//团队
                array[x][4] = map.get("jobName") == null ? "" : map.get("jobName");//职位

                Object status = map.get("statusName");
                if (status.toString().equals("401")) {
                    array[x][5] = "停职停薪";
                } else if (status.toString().equals("402")) {
                    array[x][5] = "停职留薪";
                } else {
                    throw new AppException("数据状态异常！");
                }

                array[x][6] = map.get("startDate") == null ? "" : map.get("startDate");
                array[x][7] = map.get("endDate") == null ? "" : map.get("endDate");
                array[x][8] = map.get("remark") == null ? "" : map.get("remark");

            }
            //导出Util
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "考勤统计", 1);
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void autoEmpStatus() throws Exception {
        cfEmpStatusMapper.autoEmpStatus();
    }

}
