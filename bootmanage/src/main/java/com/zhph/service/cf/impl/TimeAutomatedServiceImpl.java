package com.zhph.service.cf.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.log.SysoCounter;
import com.zhph.commons.Constant;
import com.zhph.mapper.cf.CfTimeLockMapper;
import com.zhph.mapper.cf.CfVacateManageMapper;
import com.zhph.mapper.cf.TimeAutomatedMapper;
import com.zhph.mapper.hqclcf.HqclcfEmpMapper;
import com.zhph.model.cf.CfTimeLock;
import com.zhph.model.cf.TimeAutomated;
import com.zhph.model.cf.TimeAutomatedBak;
import com.zhph.model.common.RankType;
import com.zhph.model.hqclcf.HqclcfEmp;
import com.zhph.model.hqclcf.HqclcfPersonTransfer;
import com.zhph.model.sys.SysUser;
import com.zhph.service.cf.TimeAutomatedService;
import com.zhph.util.CommonEnum;
import com.zhph.util.DateTimeUtil;
import com.zhph.util.DateUtil;
import com.zhph.util.ExcelUtil;
import com.zhph.util.Grid;
import com.zhph.util.Json;
import com.zhph.util.PageBean;
import com.zhph.util.StringUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.util.CellRangeAddress;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考勤排班
 * @author roilat-D
 */
@Service
public class TimeAutomatedServiceImpl implements TimeAutomatedService{

	@Resource
    private TimeAutomatedMapper timeAutomatedMapper;
    @Resource
    private CfVacateManageMapper cfVacateManageMapper;
    @Resource
    private HqclcfEmpMapper hqclcfEmpMapper;
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	@Resource
	private CfTimeLockMapper cfTimeLockMapper;

    public Grid<TimeAutomated> dataGrid(TimeAutomatedBak xjTimeAutomatedBak, PageBean pageBean) throws Exception {
        Grid<TimeAutomated> grid = new Grid<>();
    	if(pageBean != null){
    		PageHelper.startPage(pageBean.getPage(), pageBean.getLimit());
    	}
        List<TimeAutomated> list = timeAutomatedMapper.page(xjTimeAutomatedBak);
        if(list !=null && list.size() > 0){
			for (TimeAutomated automated : list) {
				String deptInfoStr = (String) automated.getDeptInfo();
				String[] temp;
				if(!StringUtil.isEmpty(deptInfoStr)){
					String[] depts = deptInfoStr.split(",");
					for (String dept : depts) {
						temp = dept.split("=");
						if("1".equals(temp[0])){
							automated.setRegion(temp[1]);
						}else if("2".equals(temp[0])){
							automated.setFiliale(temp[1]);
						}else if("3".equals(temp[0])){
							automated.setBusinessDept(temp[1]);
						}else if("4".equals(temp[0])){
						}
					}
				}
			}
		}
        PageInfo<TimeAutomated> pageInfo=new PageInfo<>(list);
        grid.setData(pageInfo.getList());
        grid.setCount(pageInfo.getTotal());
        return grid;
    }

    /**
     * 统计字段对应的值
     * @param fields
     * @param xjTimeAutomated
     * @param keyName
     * @return
     * @throws Exception
     */
    public Integer getworkDays(Field[] fields,TimeAutomated xjTimeAutomated,String keyName,Map<String,Object> currentDayAllMonth) throws Exception {
        Integer cot = 0;
        Object o = xjTimeAutomated;
        for (Field f:fields){
            if (f.getType() == Integer.class){
                boolean accessFlag = f.isAccessible();
                f.setAccessible(true);
                Integer val = (Integer) f.get(o);
                if (CommonEnum.getName(val)!=null&&CommonEnum.getName(val).equals(keyName)){
                    if (currentDayAllMonth.get(f.getName())!=null){
                        cot++;
                    }
                }
                f.setAccessible(accessFlag);
            }
        }
        return cot;
    }
    
    public void addAllEmp(SysUser user) throws Exception {
        String gzym = DateTimeUtil.getDateAddMonth(DateUtil.getCurrentDate("yyyy-MM"),1);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("gzym", gzym);
        params.put("businessLine", Constant.BUSINESS_LINE_CF);
        List<HqclcfEmp> empApprovalList = hqclcfEmpMapper.queryForScheduling(params);
        this.addTimeAutomateBatch(empApprovalList,user,gzym);
    }

    public void addOneEmpForEntry(HqclcfEmp emp,SysUser user) throws Exception {
        Date startDate = emp.getEnterDate();
        int cot = DateUtil.getYearMonthCountBak(startDate,new Date());
        Calendar cal = Calendar.getInstance();
        Integer yyyy = cal.get(Calendar.YEAR);
        Integer mm = cal.get(Calendar.MONTH)+1;//0代表1月
        Integer dd = cal.get(Calendar.DAY_OF_MONTH);
        String gzym = yyyy+"-"+String.format("%02d", mm);
        if (cot<0){//入职时间在这以后
        	if (dd>=27){
        		//日期在27号以后则要把下个月的给生成
				String newGzym = DateTimeUtil.getDateAddMonth(gzym, 1);// 下个月
        		Map<String,Object> map = new HashMap<String,Object>();
        		map.put("empNo", emp.getEmpNo());
        		map.put("gzym", newGzym);
        		timeAutomatedMapper.deleteByEmpnoAndGzym(map);
        		List<HqclcfEmp> hqclcfEmps = new ArrayList<HqclcfEmp>();
                hqclcfEmps.add(emp);
                this.addTimeAutomateBatch(hqclcfEmps,user,newGzym);
        	}
        	return;
        }
        boolean add = false;
		while (cot > 0) {// 入职时间在当前时间之前
            List<HqclcfEmp> hqclcfEmps = new ArrayList<HqclcfEmp>();
            add = hqclcfEmps.add(emp);
            this.addTimeAutomateBatch(hqclcfEmps,user,gzym);
            gzym = DateTimeUtil.getDateAddMonth(gzym,-1);
            cot--;
        }
		System.out.println(add);
        yyyy = Integer.valueOf(gzym.split("-")[0]);
        mm = Integer.valueOf(gzym.split("-")[1]);
        Integer days = DateTimeUtil.getCurrentMonthAllDays(yyyy,mm);
        if (cot==0){//入职时间在当前月
            this.addTimeAutomate(startDate,yyyy,mm,emp,user,days);
            if (dd>=27){//入职时间在当月并且在27号（含）之后入职的要把下个月的排班给添加上
                List<HqclcfEmp> hqclcfEmps = new ArrayList<>();
                hqclcfEmps.add(emp);
                this.addTimeAutomateBatch(hqclcfEmps,user,DateTimeUtil.getDateAddMonth(gzym,1));
            }
        }
    }

    public void addOneEmpForLeave(HqclcfEmp emp, SysUser user) throws Exception {
        Date startDate = emp.getLeaveDate();
        if(startDate == null){
        	return ;
        }
        Calendar cal = Calendar.getInstance();
        Integer yyyy = cal.get(Calendar.YEAR);
        Integer mm = cal.get(Calendar.MONTH)+1;//0代表1月
        Integer dd = cal.get(Calendar.DAY_OF_MONTH);
        int cot = DateUtil.getYearMonthCountBak(startDate,new Date());
		if (cot < 0) {// 离职时间在当前时间之后（下个月）
			if (cot == -1 && dd > 27) {// 如果离职时间是下个月，且当前时间是27号之后，则需要更新下个月的排班数据
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("empNo", emp.getEmpNo());
				map.put("gzym", DateTimeUtil.getDateAddMonth(yyyy + "-" + String.format("%02d", mm), 1));// 下个月
				List<TimeAutomated> automateds = timeAutomatedMapper.queryByEmpnoAndGzym(map);
				if (automateds != null && automateds.size() != 0) {
					TimeAutomated automated = automateds.get(0);
					int leaveDays = DateTimeUtil.getDaysFromDateForMonth(DateUtil.parseDateFormat(emp.getLeaveDate(), "yyyy-MM-dd"));
					Calendar leaveCal = Calendar.getInstance();
					leaveCal.setTime(emp.getLeaveDate());
					int leaveDate = leaveCal.get(Calendar.DAY_OF_MONTH);
					for (int i = 1; i < leaveDays; i++) {// 当天要排班
						Field f = automated.getClass().getDeclaredField(DateTimeUtil.getDayMap().get(leaveDate + i));
						boolean accessFlag = f.isAccessible();
						f.setAccessible(true);
						f.set(automated, CommonEnum.消金考勤排班_空_不可修改.getKey());
						f.setAccessible(accessFlag);
					}
					automated.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
					automated.setUpdator(user.getUserName());
					timeAutomatedMapper.edit(automated);
				}
				return;
			}
		}
        String gzym = yyyy+"-"+String.format("%02d", mm);
        Map<String, Object> map = new HashMap<String, Object>();
        while (cot>0){//离职时间在当前时间之前，需要把当月及之前的排班表给干掉
           /* List<XjTimeAutomated> xjTimeAutomatedList = baseLogicInterface.queryEntityByHql("from XjTimeAutomated where empNo=? and gzym=?",new Object[]{emp.getEmpNo(),gzYm});
            if (xjTimeAutomatedList!=null&&xjTimeAutomatedList.size()!=0){
                XjTimeAutomated xjTimeAutomated = xjTimeAutomatedList.get(0);
                baseLogicInterface.saveLog(JSONObject.toJSONString(xjTimeAutomated),"离职审批，离职时间在当前时间之前删除已经存在的排班表",xjTimeAutomated.getClass(),user,OperateType.DELETE,log);
                baseLogicInterface.deleteEntity(xjTimeAutomated);
            }*/
			map.put("empNo", emp.getEmpNo());
			map.put("gzym", gzym);
			timeAutomatedMapper.deleteByEmpnoAndGzym(map);
			gzym = DateTimeUtil.getDateAddMonth(gzym, -1);
            cot--;
        }
		if (cot == 0) {// 离职时间在当前月
			map.put("empNo", emp.getEmpNo());
			map.put("gzym", gzym);
			List<TimeAutomated> automateds = timeAutomatedMapper.queryByEmpnoAndGzym(map);
			if (automateds != null && automateds.size() != 0) {
				TimeAutomated automated = automateds.get(0);
				int leaveDays = DateTimeUtil.getDaysFromDateForMonth(DateUtil.parseDateFormat(emp.getLeaveDate(), "yyyy-MM-dd"));
				Calendar leaveCal = Calendar.getInstance();
				leaveCal.setTime(emp.getLeaveDate());
				int leaveDate = leaveCal.get(Calendar.DAY_OF_MONTH);
				for (int i = 1; i < leaveDays; i++) {// 当天要排班
					Field f = automated.getClass().getDeclaredField(
							DateTimeUtil.getDayMap().get(leaveDate + i));
					boolean accessFlag = f.isAccessible();
					f.setAccessible(true);
					f.set(automated, CommonEnum.消金考勤排班_空_不可修改.getKey());
					f.setAccessible(accessFlag);
				}
				automated.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				automated.setUpdator(user.getUserName());
				timeAutomatedMapper.edit(automated);
				/*
				 String oldData = JSONObject.toJSONString(automated);
				 baseLogicInterface.saveLog("离职日期在当月修改当月的排班表" + oldData, automated, automated.getClass(),
						user, OperateType.UPDATE, log);*/
			}
            if (dd>=27){//离职在当月并且日期在27号之后的要把下个月的排班给删除
    			map.put("empNo", emp.getEmpNo());
    			map.put("gzym", DateTimeUtil.getDateAddMonth(yyyy + "-" + String.format("%02d", mm), 1));//下个月
    			timeAutomatedMapper.deleteByEmpnoAndGzym(map);
                /*automateds = baseLogicInterface.queryEntityByHql("from XjTimeAutomated where empNo=? and gzym=?",new Object[]{emp.getEmpNo(),DateTimeUtil.getDateAddMonth(gzym,1)});
                if (automateds!=null&&automateds.size()!=0){
                    XjTimeAutomated xjTimeAutomated = automateds.get(0);
                    baseLogicInterface.saveLog(JSONObject.toJSONString(xjTimeAutomated),"离职审批，离职在当月并且日期在27号之后的要把下个月的排班给删除",xjTimeAutomated.getClass(),user,OperateType.DELETE,log);
                    baseLogicInterface.deleteEntity(xjTimeAutomated);
                }*/
            }
        }
    }

    public List<TimeAutomated> getList(PageBean pageFilter,TimeAutomatedBak xjTimeAutomatedBak) throws Exception {
        /*return xjTimeAutomatedDaoInterface.getList(pageFilter,xjTimeAutomatedBak);*/
    	return null;
    }

    public int updateBatch(List<TimeAutomated> timeAutomateds,SysUser user) throws Exception {
    	int n = 0;
		if (timeAutomateds != null && timeAutomateds.size() != 0) {
			for (TimeAutomated timeAutomated : timeAutomateds) {
				timeAutomated.setUpdateTime(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
				timeAutomated.setUpdator(user.getFullName());
				n += timeAutomatedMapper.edit(timeAutomated);
			}
		}
		return n;
    }

    public void exportExl(HttpServletResponse response, TimeAutomatedBak automatedBak) throws Exception {
        List<TimeAutomated> timeAutomateds = timeAutomatedMapper.page(automatedBak);
        Integer yyyy = Integer.valueOf(automatedBak.getGzym().split("-")[0]);
        Integer mm = Integer.valueOf(automatedBak.getGzym().split("-")[1]);
        Integer days = DateTimeUtil.getCurrentMonthAllDays(yyyy,mm);
        Map<?,?> map = DateTimeUtil.getCurrentMonthAllWeekNameBak(yyyy,mm);
        String[] title1 = new String[days];
        String[] title2 = new String[days];
        for (int i=0;i<title2.length;i++){
            title1[i] = i+1+"";
            title2[i] = map.get(i+1).toString();
        }
        String[] temp = (String[]) Array.newInstance(String.class, 8+title1.length);
        String[] ss = {"序号","大区","分公司","营业部","员工编号","员工姓名","职位","年月"};
        System.arraycopy(ss, 0, temp, 0, 8); 
        System.arraycopy(title1, 0, temp, 8, title1.length); 
        String[][] colTitleAry = {temp,title2};
        String[][] convStr = new String[timeAutomateds.size()][colTitleAry[0].length];
        short[] colWidthAry = new short[colTitleAry[0].length];
        for (int i=0;i<colWidthAry.length;i++){
            colWidthAry[i] = 80;
        }
        for (int i=0;i<timeAutomateds.size();i++){
            TimeAutomated automated = timeAutomateds.get(i);
			String deptInfoStr = (String) automated.getDeptInfo();
			if(!StringUtil.isEmpty(deptInfoStr)){
				String[] depts = deptInfoStr.split(",");
				for (String dept : depts) {
					temp = dept.split("=");
					if("1".equals(temp[0])){
						automated.setRegion(temp[1]);
					}else if("2".equals(temp[0])){
						automated.setFiliale(temp[1]);
					}else if("3".equals(temp[0])){
						automated.setBusinessDept(temp[1]);
					}else if("4".equals(temp[0])){
					}
				}
			}
            convStr[i][0] = i+1+"";
            convStr[i][1] = automated.getRegion();
            convStr[i][2] = automated.getFiliale();
            convStr[i][3] = automated.getBusinessDept();
            convStr[i][4] = automated.getEmpNo();
            convStr[i][5] = automated.getEmpName();
            convStr[i][6] = automated.getJobName();
            convStr[i][7] = automated.getGzym();
            convStr[i][8] = CommonEnum.getName(automated.getOne());
            convStr[i][9] = CommonEnum.getName(automated.getTwo());
            convStr[i][10] = CommonEnum.getName(automated.getThree());
            convStr[i][11] = CommonEnum.getName(automated.getFour());
            convStr[i][12] = CommonEnum.getName(automated.getFive());
            convStr[i][13] = CommonEnum.getName(automated.getSix());
            convStr[i][14] = CommonEnum.getName(automated.getSeven());
            convStr[i][15] = CommonEnum.getName(automated.getEight());
            convStr[i][16] = CommonEnum.getName(automated.getNine());
            convStr[i][17] = CommonEnum.getName(automated.getTen());
            convStr[i][18] = CommonEnum.getName(automated.getEleven());
            convStr[i][19] = CommonEnum.getName(automated.getTwelve());
            convStr[i][20] = CommonEnum.getName(automated.getThirteen());
            convStr[i][21] = CommonEnum.getName(automated.getFourteen());
            convStr[i][22] = CommonEnum.getName(automated.getFifteen());
            convStr[i][23] = CommonEnum.getName(automated.getSixteen());
            convStr[i][24] = CommonEnum.getName(automated.getSeventeen());
            convStr[i][25] = CommonEnum.getName(automated.getEighteen());
            convStr[i][26] = CommonEnum.getName(automated.getNineteen());
            convStr[i][27] = CommonEnum.getName(automated.getTwenty());
            convStr[i][28] = CommonEnum.getName(automated.getTwentyOne());
            convStr[i][29] = CommonEnum.getName(automated.getTwentyTwo());
            convStr[i][30] = CommonEnum.getName(automated.getTwentyThree());
            convStr[i][31] = CommonEnum.getName(automated.getTwentyFour());
            convStr[i][32] = CommonEnum.getName(automated.getTwentyFive());
            convStr[i][33] = CommonEnum.getName(automated.getTwentySix());
            convStr[i][34] = CommonEnum.getName(automated.getTwentySeven());
            convStr[i][35] = CommonEnum.getName(automated.getTwentyEight());
            if (days>28){
                convStr[i][36] = CommonEnum.getName(automated.getTwentyNine());
                if (days>29){
                    convStr[i][37] = CommonEnum.getName(automated.getThirty());
                    if (days>30){
                        convStr[i][38] = CommonEnum.getName(automated.getThirtyOne());
                    }
                }
            }
        }
        ExcelUtil execlUtil = new ExcelUtil();
        List<CellRangeAddress> regions = new ArrayList<CellRangeAddress>();
        regions.add(new CellRangeAddress(0, 1, 0, 0));	//序号
        regions.add(new CellRangeAddress(0, 1, 1, 1));	//大区
        regions.add(new CellRangeAddress(0, 1, 2, 2));	//分公司
        regions.add(new CellRangeAddress(0, 1, 3, 3));	//营业部
        regions.add(new CellRangeAddress(0, 1, 4, 4));	//员工编号
        regions.add(new CellRangeAddress(0, 1, 5, 5));	//员工姓名
        regions.add(new CellRangeAddress(0, 1, 6, 6));	//职位
        regions.add(new CellRangeAddress(0, 1, 7, 7));	//年月
        execlUtil.writeExecl(colTitleAry,colWidthAry,convStr,null,response,"消金考勤排班" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()),regions,8,2);
    }

    /**
     * 加排班表-单个不足月
     * @param startDate
     * @param yyyy
     * @param mm
     * @param emp
     * @param user
     * @param days
     * @throws Exception
     */
	private void addTimeAutomate(Date startDate,Integer yyyy,Integer mm,HqclcfEmp emp,SysUser user,Integer days) throws Exception {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startDate);
       Integer dd = cal.get(Calendar.DAY_OF_MONTH);
        //当月休息日
        List<Integer> restDays = DateTimeUtil.getCurrentMonthRestDays(yyyy,mm);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("empNo", emp.getEmpNo());
		map.put("gzym", yyyy+"-"+String.format("%02d", mm));
		timeAutomatedMapper.deleteByEmpnoAndGzym(map);
       /* List<TimeAutomated> xjTimeAutomatedList = baseLogicInterface.queryEntityByHql("from XjTimeAutomated where empNo=? and gzym=?",new Object[]{emp.getEmpNo(),yyyy+"-"+mm});
        if (xjTimeAutomatedList!=null&&xjTimeAutomatedList.size()!=0){
        	TimeAutomated xjTimeAutomated = xjTimeAutomatedList.get(0);
            baseLogicInterface.saveLog(JSONObject.toJSONString(xjTimeAutomated),"删除老的排班表",xjTimeAutomated.getClass(),user, OperateType.DELETE,log);
            baseLogicInterface.deleteEntity(xjTimeAutomated);
        }*/
		String rankType = emp.getPosition();
		if (rankType != null && rankType.equals(RankType.SALES_DIRECTOR.getNum())) {// 销管总监不参加排班
			return;
		}
		TimeAutomated timeAutomated = new TimeAutomated();
		timeAutomated.setEmpNo(emp.getEmpNo());
		timeAutomated.setGzym(yyyy + "-" + String.format("%02d", mm));
		timeAutomated.setCreator(user.getFullName());
		timeAutomated.setUpdator(user.getFullName());
		timeAutomated.setDays(days);
		timeAutomated.setJobName(RankType.getName(rankType));
		List<Integer> newRestDays = new ArrayList<>();// 真实的休息时间
		for (int i = 0; i < restDays.size(); i++) {
			if (restDays.get(i) >= dd) {
				newRestDays.add(restDays.get(i));
			}
		}
		for (int i = 1; i < dd; i++) {
			Field f = timeAutomated.getClass().getDeclaredField(DateTimeUtil.getDayMap().get(i));
			boolean accessFlag = f.isAccessible();
			f.setAccessible(true);
			f.set(timeAutomated, CommonEnum.消金考勤排班_空_不可修改.getKey());
			f.setAccessible(accessFlag);
		}
		if (rankType != null && !"".equals(rankType)
				&& (RankType.CITY_MANAGER.getNum().equals(rankType) || RankType.SALES_MANAGER.getNum().equals(rankType) || RankType.ADMIN_STAFF.getNum().equals(rankType))) {
			for (Integer _a : newRestDays) {
				Field f = timeAutomated.getClass().getDeclaredField(DateTimeUtil.getDayMap().get(_a));
				boolean accessFlag = f.isAccessible();
				f.setAccessible(true);
				f.set(timeAutomated, CommonEnum.消金考勤排班_休_可修改.getKey());
				f.setAccessible(accessFlag);
			}
		}
		List<TimeAutomated> list = new ArrayList<TimeAutomated>();
		list.add(timeAutomated);
        this.saveList(list);
    }

    /**
     * 添加排班表-批量 足月
     * @param hqclcfEmps
     * @param user
     * @param gzym
     * @throws Exception
     */
    private void addTimeAutomateBatch(List<HqclcfEmp> hqclcfEmps, SysUser user,String gzym) throws Exception {
    	List<String> empNos = new ArrayList<String>();
		if (hqclcfEmps != null && hqclcfEmps.size() != 0) {
			List<TimeAutomated> timeAutomatedList = new ArrayList<>();
			// 当月总的天数
			Integer yyyy = Integer.valueOf(gzym.split("-")[0]);
			Integer mm = Integer.valueOf(gzym.split("-")[1]);
			Integer days = DateTimeUtil.getCurrentMonthAllDays(yyyy, mm);
			// 当月休息日
			List<Integer> restDays = DateTimeUtil.getCurrentMonthRestDays(yyyy, mm);
			int n = 0;
			for (HqclcfEmp emp : hqclcfEmps) {
				String rankType = emp.getPosition();
				empNos.add(emp.getEmpNo());
				if (rankType != null && rankType.equals(RankType.SALES_DIRECTOR.getNum())) {// 销管总监不参加排班
					continue;
				}
				TimeAutomated timeAutomated = new TimeAutomated();
				timeAutomated.setEmpNo(emp.getEmpNo());
				timeAutomated.setGzym(gzym);
				timeAutomated.setCreator(user.getFullName());
				timeAutomated.setUpdator(user.getFullName());
				timeAutomated.setDays(days);
				timeAutomated.setJobName(RankType.getName(rankType));
				// 只有行政专员、营业部经理、城市分中心经理才会初始化双休日
				if (rankType != null && !"".equals(rankType)
						&& (RankType.CITY_MANAGER.getNum().equals(rankType) || RankType.SALES_MANAGER.getNum().equals(rankType) || RankType.ADMIN_STAFF.getNum().equals(rankType))) {
					for (Integer _a : restDays) {
						Field f = timeAutomated.getClass().getDeclaredField(DateTimeUtil.getDayMap().get(_a));
						boolean accessFlag = f.isAccessible();
						f.setAccessible(true);
						f.set(timeAutomated, CommonEnum.消金考勤排班_休_可修改.getKey());
						f.setAccessible(accessFlag);
					}
				}
				if ("2".equals(emp.getStatus())) {// 当月需要离职的员工
			    	Calendar leaveCal = Calendar.getInstance();
			    	leaveCal.setTime(emp.getLeaveDate());
			    	int start = leaveCal.get(Calendar.DAY_OF_MONTH);
			    	leaveCal.set(Calendar.DAY_OF_MONTH, 1);
			    	leaveCal.add(Calendar.MONTH, 1);
			    	leaveCal.add(Calendar.DAY_OF_MONTH, -1);
			    	int end = leaveCal.get(Calendar.DAY_OF_MONTH);
					int leaveDays = end - start;
					for (int i = 1; i < leaveDays; i++) {//当天要排班
						Field f = timeAutomated.getClass().getDeclaredField(DateTimeUtil.getDayMap().get(start));
						boolean accessFlag = f.isAccessible();
						f.setAccessible(true);
						f.set(timeAutomated, CommonEnum.消金考勤排班_空_不可修改.getKey());
						f.setAccessible(accessFlag);
					}
				}
				timeAutomatedList.add(timeAutomated);
				n ++;
				if (n % 500 == 0 || n == hqclcfEmps.size()) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("empNos", empNos);
					map.put("gzym", gzym);
					timeAutomatedMapper.deleteByEmpnosAndGzym(map);
					empNos.clear();
				}
			}
			saveList(timeAutomatedList);
        }
    }
    
    private int saveList(List<TimeAutomated> timeAutomatedList){
		if(timeAutomatedList != null && timeAutomatedList.size() > 0){
			SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
			int i = 0;
			try {
				for (TimeAutomated timeAutomated : timeAutomatedList) {
					timeAutomatedMapper.add(timeAutomated);
					i++;
					if ((i > 0 && i % 100 == 0) || i == timeAutomatedList.size()) {
						session.commit();
						session.clearCache();
					}
				}
			} catch (Exception e) {
				session.rollback();
				e.printStackTrace();
			} finally {
				session.close();
			}
			return i;
		}else{
			return 0;
		}
    }

    
    /**根据工资年月和人员编号删除该人当月的排班*/
    public int deleteXjTimeAutomatedByGzymAndEmpno(String gzYmMin, String gzYmMax, String empNo) throws Exception {
    	/*return xjTimeAutomatedDaoInterface.deleteXjTimeAutomatedByGzymAndEmpno(gzYmMin, gzYmMax, empNo);*/
    	return 0;
    }
    
    public int updateJobNameInXjTimeAutomatedByGzymAndEmpno(String gzYmMin, String gzYmMax, String empNo, String jobName) throws Exception{
    	/*return xjTimeAutomatedDaoInterface.updateJobNameInXjTimeAutomatedByGzymAndEmpno(gzYmMin, gzYmMax, empNo, jobName);*/
    	return 0;
    }

	@Override
	public CfTimeLock queryCfTimelock(CfTimeLock cfTimeLock) {
		return cfTimeLockMapper.queryIsLock(cfTimeLock);
	}

	/**
	 * 人员异动时，修改排班数据（员工的业务线）
	 */
	@Override
	public Json updateForEmployerTransfor(HqclcfPersonTransfer hqclcfPersonTransfer, SysUser sysUser) throws Exception {
		Json json = new Json();
		//1、数据基本处理
		if(hqclcfPersonTransfer == null || sysUser == null){
			//为空错误返回
			json.setSuccess(false);
			json.setMsg("传入参数为空，未成功修改排班信息！");
			return json;
		}
		if(StringUtil.isEmpty(hqclcfPersonTransfer.getTransferTime())){
			json.setSuccess(false);
			json.setMsg("生效日期为空！");
			return json;
		}
		Integer priBL = null;
		Integer newBL = null;
		try {
			priBL = Integer.parseInt(hqclcfPersonTransfer.getPriBusinessLine());
			newBL = Integer.parseInt(hqclcfPersonTransfer.getNewBusinessLine());
		} catch (Exception e) {
		}
		if(priBL == null || newBL == null){
			json.setMsg("未知的异动前业务特殊条线或者异动后业务条线！");
			json.setSuccess(false);
			return json;
		}
		
		if(!Constant.BUSINESS_LINE_CF.equals(priBL) && !Constant.BUSINESS_LINE_CF.equals(newBL)){
			//异动前后都不是消分，不需要处理
			json.setMsg("员工异动前后的业务条线都不是消分，不需要进行处理！");
			json.setSuccess(true);
			return json;
		}
		
		
		String transferTimeStr = hqclcfPersonTransfer.getTransferTime();//生效年月
		Date transferTime = DateUtil.convertStringToDate(transferTimeStr, "yyyy-MM");
		Calendar curDate = Calendar.getInstance();
		int cot = DateUtil.getYearMonthCountBak(new Date(), transferTime);// 生效时间-当前时间
        if(cot < 0){//生效时间在此之前
        	json.setSuccess(true);
        	json.setMsg("生效时间在当月之前，不需要进行处理！");
        	return json;
        }
		
		//2、异动前是消分员工，做删除排班信息即可
		if(Constant.BUSINESS_LINE_CF.equals(priBL)){

	        if(cot == 0){//当月，则删除当月排班数据
        		Map<String,Object> map = new HashMap<String,Object>();
				map.put("empNo", hqclcfPersonTransfer.getEmpNo());
				map.put("gzym",
						curDate.get(Calendar.YEAR) + "-" + String.format("%02d", curDate.get(Calendar.MONTH) + 1));// i当月需要+1
        		timeAutomatedMapper.deleteByEmpnoAndGzym(map);
	        }
        	if(cot > 0){//如果生效日期是下个月，直接删除，不管下个月没有生成
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("empNo", hqclcfPersonTransfer.getEmpNo());
				map.put("gzym",
						curDate.get(Calendar.YEAR) + "-" + String.format("%02d", curDate.get(Calendar.MONTH) + 2));// 下个月月份需要+2
				timeAutomatedMapper.deleteByEmpnoAndGzym(map);
	        }
		}
		
		//3、异动后是消分员工
		if(Constant.BUSINESS_LINE_CF.equals(newBL)){
	        
	        //员工信息重新处理(查询出员工信息，并将职务设置成新的职务以用于生成排班数据)
	        HqclcfEmp emp = hqclcfEmpMapper.queryEmpByEmpNo(hqclcfPersonTransfer.getEmpNo());
	        if(emp == null){
	        	json.setSuccess(false);
	        	json.setMsg("传入的员工编号无法查询到员工信息!");
	        	return json;
	        }
	        emp.setPosition(hqclcfPersonTransfer.getNewHqPosition());
	        
	        if(cot == 0){//当月，重新生成
        		Map<String,Object> map = new HashMap<String,Object>();
        		String gzym = curDate.get(Calendar.YEAR) + "-" + String.format("%02d", curDate.get(Calendar.MONTH)+1);//当月月份需要加1
        		map.put("empNo", emp.getEmpNo());
        		map.put("gzym",gzym );//下个月月份需要加2
        		timeAutomatedMapper.deleteByEmpnoAndGzym(map);
        		List<HqclcfEmp> hqclcfEmps = new ArrayList<HqclcfEmp>();
                hqclcfEmps.add(emp);
				this.addTimeAutomateBatch(hqclcfEmps, sysUser, gzym);
	        }
	        
	        if(cot > 0 && curDate.get(Calendar.DAY_OF_MONTH) > 27){//生效日期是下个月，且当前时间在27号，则生成下个月的排班
        		Map<String,Object> map = new HashMap<String,Object>();
        		String gzym = curDate.get(Calendar.YEAR) + "-" + String.format("%02d", curDate.get(Calendar.MONTH)+2);//下个月月份需要加2
        		map.put("empNo", emp.getEmpNo());
        		map.put("gzym",gzym );
        		timeAutomatedMapper.deleteByEmpnoAndGzym(map);
        		List<HqclcfEmp> hqclcfEmps = new ArrayList<HqclcfEmp>();
                hqclcfEmps.add(emp);
				this.addTimeAutomateBatch(hqclcfEmps, sysUser, gzym);
	        }
		}
		json.setSuccess(true);
		return json;
	}
	
	public static void main(String[] args) throws Exception {
		Date transferTime = DateUtil.convertStringToDate("2018-01", "yyyy-MM");
        int cot = DateUtil.getYearMonthCountBak(transferTime,new Date());
        System.out.println(cot);
	}
}
