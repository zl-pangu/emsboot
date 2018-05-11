package com.zhph.model.cf;

import com.zhph.util.CommonEnum;
import com.zhph.util.DateUtil;

import java.io.Serializable;

/**
 * 考勤排班
 * Created by liuxin on 2016/12/12.
 */
public class TimeAutomated implements Serializable{
    private static final long serialVersionUID = 1L;

    private Long priNumber;
    private String empNo;//员工编号
    private String gzym;//工资年月
    private String createTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");//创建时间
    private String updateTime = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");//修改时间
    private String creator;//创建人
    private String updator;//修改人
    private Integer days;//当月天数

    private Integer one = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer two = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer three = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer four = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer five = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer six = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer seven = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer eight = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer nine = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer ten = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer eleven = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twelve = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer thirteen = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer fourteen = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer fifteen = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer sixteen = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer seventeen = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer eighteen = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer nineteen = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twenty = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentyOne = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentyTwo = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentyThree = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentyFour = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentyFive = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentySix = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentySeven = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentyEight = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer twentyNine = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer thirty = CommonEnum.消金考勤排班_班_可修改.getKey();
    private Integer thirtyOne = CommonEnum.消金考勤排班_班_可修改.getKey();

    private String deptCode;//部门编码
    private String region;//大区
    private String filiale;//分公司
    private String businessDept;//营业部
    private String empName;//员工姓名
    private String jobName;//职位
    private String rankType;//职级类别
    private String deptInfo;//部门信息

    private Integer weekendRestCot=0;//周末休息天数
    private Integer workDayRestCot=0;//工作日休息天数
    
    //**查询参数**/
    private String orderBy;
    
    public String getDeptInfo() {
		return deptInfo;
	}

	public void setDeptInfo(String deptInfo) {
		this.deptInfo = deptInfo;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Long getPriNumber() {
        return priNumber;
    }

    public void setPriNumber(Long priNumber) {
        this.priNumber = priNumber;
    }

        public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getGzym() {
        return gzym;
    }

    public void setGzym(String gzym) {
        this.gzym = gzym;
    }

        public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

        public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

        public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

        public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

        public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

        public Integer getOne() {
        return one;
    }

    public void setOne(Integer one) {
        this.one = one;
    }

        public Integer getTwo() {
        return two;
    }

    public void setTwo(Integer two) {
        this.two = two;
    }

        public Integer getThree() {
        return three;
    }

    public void setThree(Integer three) {
        this.three = three;
    }

        public Integer getFour() {
        return four;
    }

    public void setFour(Integer four) {
        this.four = four;
    }

        public Integer getFive() {
        return five;
    }

    public void setFive(Integer five) {
        this.five = five;
    }

        public Integer getSix() {
        return six;
    }

    public void setSix(Integer six) {
        this.six = six;
    }

        public Integer getSeven() {
        return seven;
    }

    public void setSeven(Integer seven) {
        this.seven = seven;
    }

        public Integer getEight() {
        return eight;
    }

    public void setEight(Integer eight) {
        this.eight = eight;
    }

        public Integer getNine() {
        return nine;
    }

    public void setNine(Integer nine) {
        this.nine = nine;
    }

        public Integer getTen() {
        return ten;
    }

    public void setTen(Integer ten) {
        this.ten = ten;
    }

        public Integer getEleven() {
        return eleven;
    }

    public void setEleven(Integer eleven) {
        this.eleven = eleven;
    }

        public Integer getTwelve() {
        return twelve;
    }

    public void setTwelve(Integer twelve) {
        this.twelve = twelve;
    }

        public Integer getThirteen() {
        return thirteen;
    }

    public void setThirteen(Integer thirteen) {
        this.thirteen = thirteen;
    }

        public Integer getFourteen() {
        return fourteen;
    }

    public void setFourteen(Integer fourteen) {
        this.fourteen = fourteen;
    }

        public Integer getFifteen() {
        return fifteen;
    }

    public void setFifteen(Integer fifteen) {
        this.fifteen = fifteen;
    }

        public Integer getSixteen() {
        return sixteen;
    }

    public void setSixteen(Integer sixteen) {
        this.sixteen = sixteen;
    }

        public Integer getSeventeen() {
        return seventeen;
    }

    public void setSeventeen(Integer seventeen) {
        this.seventeen = seventeen;
    }

        public Integer getEighteen() {
        return eighteen;
    }

    public void setEighteen(Integer eighteen) {
        this.eighteen = eighteen;
    }

        public Integer getNineteen() {
        return nineteen;
    }

    public void setNineteen(Integer nineteen) {
        this.nineteen = nineteen;
    }

        public Integer getTwenty() {
        return twenty;
    }

    public void setTwenty(Integer twenty) {
        this.twenty = twenty;
    }

        public Integer getTwentyOne() {
        return twentyOne;
    }

    public void setTwentyOne(Integer twentyOne) {
        this.twentyOne = twentyOne;
    }

        public Integer getTwentyTwo() {
        return twentyTwo;
    }

    public void setTwentyTwo(Integer twentyTwo) {
        this.twentyTwo = twentyTwo;
    }

        public Integer getTwentyThree() {
        return twentyThree;
    }

    public void setTwentyThree(Integer twentyThree) {
        this.twentyThree = twentyThree;
    }

        public Integer getTwentyFour() {
        return twentyFour;
    }

    public void setTwentyFour(Integer twentyFour) {
        this.twentyFour = twentyFour;
    }

        public Integer getTwentyFive() {
        return twentyFive;
    }

    public void setTwentyFive(Integer twentyFive) {
        this.twentyFive = twentyFive;
    }

        public Integer getTwentySix() {
        return twentySix;
    }

    public void setTwentySix(Integer twentySix) {
        this.twentySix = twentySix;
    }

        public Integer getTwentySeven() {
        return twentySeven;
    }

    public void setTwentySeven(Integer twentySeven) {
        this.twentySeven = twentySeven;
    }

        public Integer getTwentyEight() {
        return twentyEight;
    }

    public void setTwentyEight(Integer twentyEight) {
        this.twentyEight = twentyEight;
    }

        public Integer getTwentyNine() {
        return twentyNine;
    }

    public void setTwentyNine(Integer twentyNine) {
        this.twentyNine = twentyNine;
    }

        public Integer getThirty() {
        return thirty;
    }

    public void setThirty(Integer thirty) {
        this.thirty = thirty;
    }

        public Integer getThirtyOne() {
        return thirtyOne;
    }

    public void setThirtyOne(Integer thirtyOne) {
        this.thirtyOne = thirtyOne;
    }

        public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    ////@Transient
    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    //@Transient
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    //@Transient
    public String getFiliale() {
        return filiale;
    }

    public void setFiliale(String filiale) {
        this.filiale = filiale;
    }

    //@Transient
    public String getBusinessDept() {
        return businessDept;
    }

    public void setBusinessDept(String businessDept) {
        this.businessDept = businessDept;
    }

    //@Transient
    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    //@Transient
    public String getRankType() {
        return rankType;
    }

    public void setRankType(String rankType) {
        this.rankType = rankType;
    }

    //@Transient
    public Integer getWeekendRestCot() {
        return weekendRestCot;
    }
    public void setWeekendRestCot(Integer weekendRestCot) {
        this.weekendRestCot = weekendRestCot;
    }

    //@Transient
    public Integer getWorkDayRestCot() {
        return workDayRestCot;
    }
    public void setWorkDayRestCot(Integer workDayRestCot) {
        this.workDayRestCot = workDayRestCot;
    }
}
