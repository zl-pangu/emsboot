package com.zhph.model.hqclcf.dto;

/**
 * Created by zhouliang on 2018/2/2.
 * 不良信息 case时间
 */
public enum HqclcfEmpApvEnumCaseTime {
    Within3M("三个月以内", "[0,0.25)"),
    Within6M("六个月以内", "[0.25,0.5)"),
    Within6MAnd1Y("六个月以上一年以内", "[0.5,1)"),
    Within1YAnd2("一年以上两年以内", "[1,2)"),
    Within2YAnd5("两年以上五年以内", "[2,5)"),
    Within5YAnd10("五年以上十年以内", "[5,10)"),
    Within10YAnd15("十年以上十五年以内", "[10,15)"),
    Above20("二十年以上", "二十年以上");

     private  String timeDescription;//时间说明
     private String section;//区间

    private HqclcfEmpApvEnumCaseTime(String timeDescription,String section){
        this.section=section;
        this.timeDescription=timeDescription;
    }

    //构造方法 --通过区间获取时间说明
    public static String getTimeDescription(String section) {
        for (HqclcfEmpApvEnumCaseTime casetime: HqclcfEmpApvEnumCaseTime.values()) {
            if (casetime.getSection().equals(section)){
                return casetime.getTimeDescription();
            }
        }
        return null;
    }

    public String getTimeDescription() {
        return timeDescription;
    }

    public void setTimeDescription(String timeDescription) {
        this.timeDescription = timeDescription;
    }

    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }
}
