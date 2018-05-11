package com.zhph.model.hqclcf.dto;

/**
 * Create By lishuangjiang
 * 人员异动编制排查
 */
public class HqclcfEmpOragnizationDto {

    private String empNo;
    private String empName;
    private String post;
    private String deptNo;
    private Integer usedNo;

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public Integer getUsedNo() {
        return usedNo;
    }

    public void setUsedNo(Integer usedNo) {
        this.usedNo = usedNo;
    }
}
