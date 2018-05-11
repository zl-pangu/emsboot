package com.zhph.model.cf.dto;

/**
 * Create By lishuangjiang
 */
public class CfAttendanceDto {

    private String empNo;
    private String deptInfo;
    private String deptCodeInfo;

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getDeptInfo() {
        return deptInfo;
    }

    public void setDeptInfo(String deptInfo) {
        this.deptInfo = deptInfo;
    }

    public String getDeptCodeInfo() {
        return deptCodeInfo;
    }

    public void setDeptCodeInfo(String deptCodeInfo) {
        this.deptCodeInfo = deptCodeInfo;
    }
}
