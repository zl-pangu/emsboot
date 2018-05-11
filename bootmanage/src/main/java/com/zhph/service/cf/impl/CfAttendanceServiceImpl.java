package com.zhph.service.cf.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhph.commons.Constant;
import com.zhph.mapper.cf.*;
import com.zhph.mapper.common.ZhphObjectMapper;
import com.zhph.mapper.hqclcf.*;
import com.zhph.model.cf.*;
import com.zhph.model.cf.dto.CfAttendanceDto;
import com.zhph.model.hqclcf.*;
import com.zhph.model.sys.SysConfigType;
import com.zhph.service.cf.CfAttendanceService;
import com.zhph.service.common.BaseService;
import com.zhph.service.sys.SysConfigTypeService;
import com.zhph.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Create By lishuangjiang
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CfAttendanceServiceImpl implements CfAttendanceService {

    @Autowired
    private CfOfflaterSetMapper cfOfflaterSetMapper;

    @Autowired
    private CfAttendanceDtoMapper cfAttendanceDtoMapper;

    @Autowired
    private CfAttendanceMapper cfAttendanceMapper;

    @Autowired
    private CfVacateManageMapper cfVacateManageMapper;

    @Autowired
    private CfCardAbnormityMapper cfCardAbnormityMapper;

    @Autowired
    private HqclcfGzymMapper hqclcfGzymMapper;

    @Autowired
    private TimeAutomatedMapper timeAutomated;

    @Autowired
    private CfTimeLockMapper cfTimeLockMapper;

    @Autowired
    private HqclcfDeptMapper hqclcfDeptMapper;

    @Autowired
    private HqclcfPostMapper hqclcfPostMapper;

    @Autowired
    private HqclcfBusinessMapper hqclcfBusinessMapper;

    @Autowired
    private HqclcfRankMapper hqclcfRankMapper;

    @Autowired
    private ZhphObjectMapper zhphObjectMapper;

    @Autowired
    private HqclcfEmpMapper hqclcfEmpMapper;

    @Autowired
    private BaseService baseService;

    @Resource
    private SysConfigTypeService sysConfigTypeService;

    public static final Logger logger = LogManager.getLogger(CfAttendanceServiceImpl.class);

    /**
     * 生成考勤统计
     *
     * @throws Exception
     */
    @Override
    public JSONObject saveAttendance(String gzym) throws Exception {
        JSONObject json = new JSONObject();

        int del = cfAttendanceMapper.deleteCfAttendanceBatch("", gzym);

        List<CfAttendance> list = save(gzym);

        if (list.size() <= 0) {
            json.put("code", "503");
            json.put("msg", "没有查询到需要生成的数据！");
            return json;
        }

        int result = 0;
        for (CfAttendance c : list) {
            result = cfAttendanceMapper.insertCfAttendanceBatch(c);
        }

        if (result > 0) {
            json.put("code", "200");
            return json;
        }
        json.put("code", "500");
        json.put("msg", "生成考勤异常，请重新尝试！");
        return json;
    }

    @Override
    public JSONObject lock(CfAttendance attendance) throws Exception {
        JSONObject json = new JSONObject();
        int res = DateUtil.compare_date(DateUtil.parseStringToDate(attendance.getGzym(),"yyyy-MM"),DateUtil.parseStringToDate(hqclcfGzymMapper.queryCurrGzym().getCurrentGzym(),"yyyy-MM"));
        if(res == 1){
            json.put("code", "509");
            json.put("msg", "锁定时间超过当前工资年月！");
            return json;
        } //当前工资年月进行对比

        CfTimeLock timeLock = new CfTimeLock();
        timeLock.setYear(Integer.valueOf(attendance.getGzym().split("-")[0]));
        timeLock.setMonth(Integer.valueOf(attendance.getGzym().split("-")[1]));
        CfTimeLock queryResult = cfTimeLockMapper.queryIsLock(timeLock);
        if (queryResult != null) {
            if (queryResult.getIsLock().toString().equals(Constant.ENABLE)) {
                json.put("code", "502");
                json.put("msg", "当前工资年月已锁定！");
                return json;
            }
            queryResult.setIsLock(1);
            int updateResult = cfTimeLockMapper.updateLock(queryResult);
            if (updateResult > 0) {
                json.put("code", "200");
                return json;
            }
            json.put("code", "503");
            json.put("msg", "锁定时异常！");
            return json;
        }

        timeLock.setIsLock(0);
        int insertReturn = cfTimeLockMapper.insertLock(timeLock);
        if (insertReturn > 0) {
            json.put("code", "200");
            return json;
        }
        json.put("code", "501");
        json.put("msg", "当前工资年月未锁定，但新增异常！");

        return json;
    }

    @Override
    public JSONObject unlock(CfAttendance attendance) throws Exception {
        JSONObject json = new JSONObject();
        int res = DateUtil.compare_date(DateUtil.parseStringToDate(attendance.getGzym(),"yyyy-MM"),DateUtil.parseStringToDate(hqclcfGzymMapper.queryCurrGzym().getCurrentGzym(),"yyyy-MM"));
        if(res == 1){
            json.put("code", "509");
            json.put("msg", "解锁时间超过当前工资年月！");
            return json;
        } //当前工资年月进行对比

        CfTimeLock timeLock = new CfTimeLock();
        timeLock.setYear(Integer.valueOf(attendance.getGzym().split("-")[0]));
        timeLock.setMonth(Integer.valueOf(attendance.getGzym().split("-")[1]));
        CfTimeLock queryResult = cfTimeLockMapper.queryIsLock(timeLock);
        if (queryResult != null) {
            if (queryResult.getIsLock().toString().equals(Constant.DISABLE)) {
                json.put("code", "502");
                json.put("msg", "当前工资年月处于解锁状态！");
                return json;
            }
            queryResult.setIsLock(0);
            int updateResult = cfTimeLockMapper.updateLock(queryResult);
            if (updateResult > 0) {
                json.put("code", "200");
                return json;
            }
            json.put("code", "503");
            json.put("msg", "解锁时失败，请重试！");
            return json;
        }
        json.put("code", "504");
        json.put("msg", "解锁失败,未对此工资年月进行锁定！");
        return json;
    }

    /**
     * 传入工资年月生成考勤统计表到db
     *
     * @throws Exception
     */
    public List<CfAttendance> save(String gzym) throws Exception {

        if ("".equals(gzym) || gzym == null) {
            gzym = hqclcfGzymMapper.queryCurrGzym().getCurrentGzym();
        }

        List<CfAttendance> returnValue = new ArrayList<>();
        List<TimeAutomated> autoMated = timeAutomated.queryAutoMatedByGzym(gzym); //考勤data
        Field[] fields = TimeAutomated.class.getDeclaredFields();
        Integer yyyy = Integer.valueOf(gzym.split("-")[0]);
        Integer mm = Integer.valueOf(gzym.split("-")[1]);
        Integer days = DateTimeUtil.getCurrentMonthAllDays(yyyy, mm);//当月总的天数
        Map currentDayAllMonth = DateTimeUtil.getCurrentDayAllMonth(yyyy, mm);//获取当月实际天数对应多好号
        CfOfflaterSet set = cfOfflaterSetMapper.queryDataByGzym(gzym);  //当月可调休天数
        Integer tot = null;
        if (null != set) {
            tot = Integer.parseInt((set.getCosWorkdayDays() == null ? 0 : set.getCosWorkdayDays()).toString()) + Integer.parseInt((set.getCosWeekendDays() == null ? 0 : set.getCosWeekendDays()).toString());//当前工资年月工作日可调休和周末可调休
        } else {
            tot = 0;
        }

        //员工data 应为消分，通过审核，在职，停职停薪，停职留薪状态等
        List<Integer> oughtOnline = new ArrayList<>();
        oughtOnline.add(2);
        List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq("", "排空", oughtOnline); //员工data
        Map<String, HqclcfEmp> empMap = new HashMap<>();
        for (HqclcfEmp emp : emps) {
            if (null != emp && null != emp.getBusinessLine()) {
                if (emp.getBusinessLine() == 2 && emp.getStatus() != 2 && emp.getStatusApp() == 1) {  //只取消分
                    empMap.put(emp.getEmpNo(), emp);
                }
            }
        }
        for (TimeAutomated au : autoMated) {
            CfAttendance attendance = new CfAttendance();                       //考勤统计data
            Integer ought = getworkDays(fields, au, "班", currentDayAllMonth);

            attendance.setGzym(gzym); //工资年月
            attendance.setEmpNo(au.getEmpNo()); //员工编码
            attendance.setCreator(CommonUtil.getOnlineFullName()); //创建人
            attendance.setCreateTime(DateUtil.parseDateFormat(new Date(), "yyyy-MM-dd")); //创建时间

            if (empMap.containsKey(au.getEmpNo())) {

                CfAttendanceDto dto = cfAttendanceDtoMapper.queryDeptDto(au.getEmpNo());

                if (dto != null) {
                    if (dto.getDeptCodeInfo() != null && !dto.getDeptCodeInfo().equals("")) {
                        List<String> list = Arrays.asList(dto.getDeptCodeInfo().split(","));
                        for (String s : list) {
                            switch (s.substring(0, 1)) {
                                case "1":
                                    attendance.setDeptCode(s.substring(2).toString());//部门编码  --> 大区
                                    break;
                                case "2":
                                    attendance.setPostName(s.substring(2).toString());//岗位编码    --> 分中心
                                    break;
                                case "3":
                                    attendance.setBusinessNameo(s.substring(2).toString());//职务编码-->营业部
                                    break;
                                case "4":
                                    attendance.setRankName(s.substring(2).toString());//职级编码 --> 团队
                                    break;
                            }
                        }
                    }

                }
                attendance.setEmpName(empMap.get(au.getEmpNo()).getEmpName());//员工姓名
                attendance.setJobName(empMap.get(au.getEmpNo()).getPost());//职位
                attendance.setEnterDate(DateUtil.parseDateFormat(empMap.get(au.getEmpNo()).getEnterDate(), "yyyy-MM-dd"));//入职时间
                if (null != empMap.get(au.getEmpNo()).getLeaveDate()) {
                    attendance.setEnterDate(DateUtil.parseDateFormat(empMap.get(au.getEmpNo()).getLeaveDate(), "yyyy-MM-dd"));//离职时间
                }
                //attendance.setBusinessLine(empMap.get(au.getEmpNo()).getBusinessLine());//业务条线
            }

            //attendance.setOughtAttend(ought - (days - 4) >= 0 ? Integer.valueOf(days - 4) : ought);//应出勤天数

            if (ought >= (days - tot)) {
                attendance.setOughtAttend(days - tot);
            }

            if (ought < (days - tot)) {
                attendance.setOughtAttend(ought);
            }


            /*请假*/
            CfVacateManage vacate = new CfVacateManage();

            vacate.setEmpNo(au.getEmpNo());//员工编码
            vacate.setGzYm(gzym);//工资年月
            vacate.setStatus("3");//审批通过

            vacate.setLeaveType("1");
            vacate.setLeaveTypes(new String[]{"1"});//事假
            attendance.setPersonalLeave(cfVacateManageMapper.getDaysForMonth(vacate));

            vacate.setLeaveType("2");
            vacate.setLeaveTypes(new String[]{"2"});//病假
            attendance.setSickLeave(cfVacateManageMapper.getDaysForMonth(vacate));

            vacate.setLeaveTypes(new String[]{"3", "4", "5"});//年、婚、丧假
            attendance.setWelfareLeave1(cfVacateManageMapper.getDaysForMonth(vacate));

            vacate.setLeaveTypes(new String[]{"6", "7", "8", "10"});//产假/产检假/陪产假/工伤假
            attendance.setWelfareLeave2(cfVacateManageMapper.getDaysForMonth(vacate));

            CfCardAbnormity cardAbn = cfCardAbnormityMapper.queryAbnByEmpNo(au.getEmpNo(), gzym);
            if (cardAbn != null) {
                attendance.setCommuteClockAbnormal(Integer.parseInt(cardAbn.getUpWorkAbnormityNo()) + Integer.parseInt(cardAbn.getOffWorkAbnormityNo()));//上下班打卡异常之和
                attendance.setSpotCheckClockAbnormal(Integer.parseInt(cardAbn.getCheckAbnormityNo()));//抽查异常打卡之和
                attendance.setAbsenteeism(Double.valueOf(cardAbn.getAbsenteeismNo()));//旷工次数
            }
            attendance.setRealAttend(attendance.getOughtAttend() - attendance.getPersonalLeave() - attendance.getSickLeave() - attendance.getWelfareLeave1() - attendance.getWelfareLeave2() - attendance.getAbsenteeism());
            returnValue.add(attendance);
        }

        return returnValue;
    }

    /**
     * 统计考勤
     *
     * @param fields
     * @param timeAutomated
     * @param keyName
     * @return
     * @throws Exception
     */
    public Integer getworkDays(Field[] fields, TimeAutomated timeAutomated, String keyName, Map<String, Object> currentDayAllMonth) throws Exception {
        Integer cot = 0;
        Object o = timeAutomated;
        for (Field f : fields) {  //遍历属性
            if (f.getType() == Integer.class) { //判断属性是否属于Integer
                boolean accessFlag = f.isAccessible();
                f.setAccessible(true); //设置可访问私有
                Integer val = (Integer) f.get(o); //获取属性值
                if (CommonEnum.getName(val) != null && CommonEnum.getName(val).equals(keyName)) { // 通过枚举判断是否存在对应val的值，和是否为‘班’
                    if (null != currentDayAllMonth.get(f.getName())) {  //属性名是否存在map的对应value中
                        cot++;
                    }
                }
                f.setAccessible(accessFlag);
            }
        }
        return cot;
    }

    @Override
    public String queryBusinessLine(String key, String constant) throws Exception {
        List<SysConfigType> sysConfigTypeList = sysConfigTypeService.getConfigTypesByPSysCode(constant);
        for (SysConfigType keyValue : sysConfigTypeList) {
            if (key.equals(keyValue.getSysValue().toString())) {
                return keyValue.getSysName();
            }
        }
        return "None";
    }

    @Override
    public ModelAndView queryAllOpenTypes(ModelAndView model) throws Exception {
        Json json = new Json();
        model.addObject("depts", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfDeptMapper.queryAll(new HqclcfDept()))));
        model.addObject("dept", hqclcfDeptMapper.queryAll(new HqclcfDept()));
        model.addObject("posts", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfPostMapper.queryAll(new HqclcfPost()))));
        model.addObject("post", hqclcfPostMapper.queryAll(new HqclcfPost()));
        model.addObject("ranks", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfRankMapper.getRankByCondition(new HqclcfRank()))));
        model.addObject("rank", hqclcfRankMapper.getRankByCondition(new HqclcfRank()));
        model.addObject("businesses", zhphObjectMapper.writeValueAsString(json.getObj(hqclcfBusinessMapper.getBusinessByCondition(new HqclcfBusiness()))));
        model.addObject("business", hqclcfBusinessMapper.getBusinessByCondition(new HqclcfBusiness()));
        model.addObject("gzym", hqclcfGzymMapper.queryCurrGzym());
        return model;
    }

    @Override
    public JSONObject queryDeptAndPostListByDeptNo(Long id) throws Exception {
        JSONObject json = new JSONObject();
        HqclcfDept dept = new HqclcfDept();
        dept.setId(id);
        List<HqclcfDept> depts = hqclcfDeptMapper.queryAll(dept);
        try {
            if (depts.size() > 0) {
                json.put("code", "200");
                json.put("dept", depts.get(0));
                Long deptCode = depts.get(0).getId();
                List<HqclcfPost> postByDept = hqclcfPostMapper.queryAll(new HqclcfPost());
                List<HqclcfPost> newPost = new ArrayList<>();
                for (int i = 0; i < postByDept.size(); i++) {
                    if (postByDept.get(0).getPriNumber() == 1) {
                        postByDept.remove(i);
                        continue;
                    }
                    if (deptCode.equals(Long.valueOf(postByDept.get(i).getDeptNo()))) {
                        newPost.add(postByDept.get(i));
                    }
                }
                json.put("post", newPost);
                return json;
            } else {
                json.put("code", "500");
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("code", "500");
        return json;
    }

    @Override
    public List<HqclcfEmp> queryAllPersonTransfer(String data, String q) throws Exception {
        HqclcfEmp emp = new HqclcfEmp();
        if (!("".equals(q)) && q != null) {
            List<HqclcfEmp> emps = hqclcfEmpMapper.queryEmpByq(q, "", new ArrayList<Integer>());
            return emps;
        }
        return hqclcfEmpMapper.queryAllEmp(new HqclcfEmp());
    }

    @Override
    public JSONObject queryAllOpenBusiness(String posCode) throws Exception {
        JSONObject json = new JSONObject();
        HqclcfBusiness business = new HqclcfBusiness();
        List<String> rankLevel = new ArrayList<>();
        business.setPosCode(posCode);
        try {
            if (posCode != null && !("".equals(posCode))) {
                List<HqclcfBusiness> businesses = hqclcfBusinessMapper.getBusinessByCondition(business);
                if (businesses.size() > 0) {
                    if (!(businesses.get(0).getRankName().equals("")) && businesses.get(0).getRankName() != null) {
                        String[] array = businesses.get(0).getRankName().split(" ");
                        for (int i = 0; i < array.length; i++) {
                            if (array[i] != null || !("".equals(array[i]))) {
                                rankLevel.add(array[i]);
                            }
                        }
                    }
                }
                json.put("rank", rankLevel);
                json.put("code", "200");
                json.put("business", businesses.get(0));
                return json;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        json.put("code", 500);
        return json;
    }


    @Override
    public Grid<Map<String, String>> queryPageInfo(PageBean pageBean, CfAttendance cfAttendance, String gzym) throws Exception {
        if (cfAttendance.getGzym() == null) {
            cfAttendance.setGzym(hqclcfGzymMapper.queryCurrGzym().getCurrentGzym());
        }
        PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
        List<Map<String, String>> lists = buildQueryList(cfAttendance);
        attendance(lists);
        PageInfo<Map<String, String>> pageInfo = new PageInfo<>(lists);
        Grid<Map<String, String>> grid = new Grid<>();
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
    }

    private List<Map<String, String>> buildQueryList(CfAttendance cfAttendance) throws Exception {
        buildQueryParams(cfAttendance);
        List<Map<String, String>> lists = cfAttendanceMapper.queryList(cfAttendance);
        for (Map<String, String> map : lists) {

            String deptInfo = null != map.get("deptInfo") ? map.get("deptInfo") : "";
            if (!"".equals(deptInfo)) {
                String[] deptTemp;
                String[] depts = deptInfo.split(",");
                for (String dept : depts) {
                    deptTemp = dept.split("=");
                    switch (deptTemp[0]) {
                        case "1":
                            map.put("deptCode", deptTemp[1]);
                            break;
                        case "2":
                            map.put("postName", deptTemp[1]);
                            break;
                        case "4":
                            map.put("rankName", deptTemp[1]);
                            break;
                        case "3":
                            map.put("businessNameo", deptTemp[1]);
                            break;
                    }
                }
            }
        }
        return lists;
    }

    private void buildQueryParams(CfAttendance cfAttendance) throws Exception {
        String region = cfAttendance.getDeptCode();
        String filiale = cfAttendance.getPostName();
        String dept = cfAttendance.getBusinessNameo();
        if ("".equals(region)) {
            if ("".equals(filiale)) {
                if (!"".equals(dept)) {
                    cfAttendance.setOnlyDeptCode(dept);
                }
            } else {
                cfAttendance.setOnlyDeptCode(filiale);
            }
        } else {
            cfAttendance.setOnlyDeptCode(region);
        }
    }

    /**
     * 传入统计2
     * @param attendance
     * @return
     * @throws Exception
     */
    private void attendance(List<Map<String, String>> attendance) throws Exception {

        for (Map<String, String> map : attendance) {

            if ("".equals(map.get("gzym")) || map.get("gzym") == null) {
                map.put("gzym", hqclcfGzymMapper.queryCurrGzym().getCurrentGzym());
            }

            Field[] fields = TimeAutomated.class.getDeclaredFields();
            Integer yyyy = Integer.valueOf(map.get("gzym").split("-")[0]);
            Integer mm = Integer.valueOf(map.get("gzym").split("-")[1]);
            Integer days = DateTimeUtil.getCurrentMonthAllDays(yyyy, mm);//当月总的天数
            Map currentDayAllMonth = DateTimeUtil.getCurrentDayAllMonth(yyyy, mm);//获取当月实际天数对应多好号
            CfOfflaterSet set = cfOfflaterSetMapper.queryDataByGzym(map.get("gzym"));//当月可调休天数
            Integer tot = null;
            if (null != set) {
                tot = Integer.parseInt((set.getCosWorkdayDays() == null ? 0 : set.getCosWorkdayDays()).toString()) + Integer.parseInt((set.getCosWeekendDays() == null ? 0 : set.getCosWeekendDays()).toString());//当前工资年月工作日可调休和周末可调休
            } else {
                tot = 0;
            }

            HashMap<String, String> query = new HashMap<>();
            query.put("empNo", map.get("empNo"));
            query.put("gzym", map.get("gzym"));
            TimeAutomated au = timeAutomated.queryByEmpNoAndGzym(query);

            if (null != au) {
                if (au.getEmpNo().equals(map.get("empNo")) && au.getGzym().equals(map.get("gzym"))) {
                    Integer ought = getworkDays(fields, au, "班", currentDayAllMonth); //应出勤天数

                    if (ought >= (days - tot)) {
                        map.put("oughtAttend", (days - tot) + "");
                    }

                    if (ought < (days - tot)) {
                        map.put("oughtAttend", ought + "");
                    }

                    //map.put("oughtAttend", (ought - (days - 4 - tot) >= 0 ? Integer.valueOf(days - 4) : ought).toString()); //应出勤天数

                         /*请假*/
                    CfVacateManage vacate = new CfVacateManage();

                    vacate.setEmpNo(au.getEmpNo());//员工编码
                    vacate.setGzYm(map.get("gzym"));//工资年月
                    vacate.setStatus("3");//审批通过

                    vacate.setLeaveType("1");
                    vacate.setLeaveTypes(new String[]{"1"});//事假
                    map.put("personalLeave", cfVacateManageMapper.getDaysForMonth(vacate).toString());

                    vacate.setLeaveType("2");
                    vacate.setLeaveTypes(new String[]{"2"});//病假
                    map.put("sickLeave", cfVacateManageMapper.getDaysForMonth(vacate).toString());

                    vacate.setLeaveTypes(new String[]{"3", "4", "5"});//年、婚、丧假
                    map.put("welfareLeave1", cfVacateManageMapper.getDaysForMonth(vacate).toString());

                    vacate.setLeaveTypes(new String[]{"6", "7", "8", "10"});//产假/产检假/陪产假/工伤假
                    map.put("welfareLeave2", cfVacateManageMapper.getDaysForMonth(vacate).toString());

                    CfCardAbnormity cardAbn = cfCardAbnormityMapper.queryAbnByEmpNo(au.getEmpNo(), map.get("gzym"));
                    if (null != cardAbn) {
                        map.put("commuteClockAbnormal", (Integer.parseInt(null == cardAbn.getUpWorkAbnormityNo() ? "0" : cardAbn.getUpWorkAbnormityNo()) + Integer.parseInt(null == cardAbn.getOffWorkAbnormityNo() ? "0" : cardAbn.getOffWorkAbnormityNo())) + "");//上下班打卡异常之和
                        map.put("spotCheckClockAbnormal", (Integer.parseInt(null == cardAbn.getCheckAbnormityNo() ? "0" : cardAbn.getCheckAbnormityNo())) + "");//抽查异常打卡之和
                        map.put("absenteeism", null == cardAbn.getAbsenteeismNo() ? "0" : cardAbn.getAbsenteeismNo());//旷工次数
                    } else {
                        map.put("commuteClockAbnormal", "0");//上下班打卡异常之和
                        map.put("spotCheckClockAbnormal", "0");//抽查异常打卡之和
                        map.put("absenteeism", "0");//旷工次数
                    }
                    Integer oughtAttend = Integer.parseInt(map.get("oughtAttend") == null ? "0" : map.get("oughtAttend"));
                    Double personalLeave = Double.parseDouble(map.get("personalLeave") == null ? "0" : map.get("personalLeave"));
                    Double sickLeave = Double.parseDouble(map.get("sickLeave") == null ? "0" : map.get("sickLeave"));
                    Double welfareLeave1 = Double.parseDouble(map.get("welfareLeave1") == null ? "0" : map.get("welfareLeave1"));
                    Double welfareLeave2 = Double.parseDouble(map.get("welfareLeave2") == null ? "0" : map.get("welfareLeave2"));
                    Double welfareLeave3 = Double.parseDouble(map.get("absenteeism") == null ? "0" : map.get("absenteeism"));
                    Double result = oughtAttend - personalLeave - sickLeave - welfareLeave1 - welfareLeave2 - welfareLeave3;
                    map.put("realAttend", result + "");
                }
            }

        }

    }

    @Override
    public void exportExl(CfAttendance cfAttendance, HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {

            List<Map<String, String>> lists = buildQueryList(cfAttendance);
            attendance(lists);
            String[] colTitleAry = {"序号", "年月", "员工编码", "员工姓名", "大区", "分中心", "营业部", "团队", "职位", "入职时间", "离职时间", "应出勤天数", "实际出勤天数", "事假天数", "病假天数", "年/婚/丧假天数", "产假类/工伤类天数", "上下班打卡异常次数", "抽查打卡异常次数", "旷工天数"};
            String[][] array = new String[lists.size()][colTitleAry.length];
            short[] colWidthAry = {80, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100};

            int i = 0;
            for (Map<String, String> map : lists) {
                int x = i++;
                array[x][0] = x + 1 + "";
                array[x][1] = map.get("gzym");
                array[x][2] = map.get("empNo");
                array[x][3] = map.get("empName");
                array[x][4] = map.get("deptCode") == null ? "" : map.get("deptCode");
                array[x][5] = map.get("postName") == null ? "" : map.get("postName");
                array[x][6] = map.get("businessNameo") == null ? "" : map.get("businessNameo");
                array[x][7] = map.get("rankName") == null ? "" : map.get("rankName");
                array[x][8] = map.get("jobName") == null ? "" : map.get("jobName");
                array[x][9] = map.get("enterDate") == null ? "" : map.get("enterDate");
                array[x][10] = map.get("leaveDate") == null ? "" : map.get("leaveDate");
                array[x][11] = map.get("oughtAttend") == null ? "" : map.get("oughtAttend");
                array[x][12] = map.get("realAttend") == null ? "" : map.get("realAttend");
                array[x][13] = map.get("personalLeave") == null ? "" : map.get("personalLeave");
                array[x][14] = map.get("sickLeave") == null ? "" : map.get("sickLeave");
                array[x][15] = map.get("welfareLeave1") == null ? "" : map.get("welfareLeave1");
                array[x][16] = map.get("welfareLeave2") == null ? "" : map.get("welfareLeave2");
                array[x][17] = map.get("commuteClockAbnormal") == null ? "" : map.get("commuteClockAbnormal");
                array[x][18] = map.get("spotCheckClockAbnormal") == null ? "" : map.get("spotCheckClockAbnormal");
                array[x][19] = map.get("absenteeism") == null ? "" : map.get("absenteeism");

            }

            //导出Util
            ExcelUtil excelUtil = new ExcelUtil();
            excelUtil.writeExecl(colTitleAry, colWidthAry, array, null, res, "考勤统计", 1);
        } catch (Exception e) {
            logger.error("导出失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}