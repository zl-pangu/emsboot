package com.zhph.model.cl;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * Create by zhaoqixiang
 * Description: 灰黑名单
 * 2018/1/16
 **/
public class ClAshBlackMenuModel {
    private Long id;
    private String deptNo;
    private String deptName;//部门名称
    private String rank;//职级
    private String subcompany;//分公司
    private String Gzym;//工资年月
    @Excel(name = "员工编码", isImportField = "true")//校验表格字段
    private String empNo;
    @Excel(name = "员工姓名", isImportField = "true")
    private String empName;
    @Excel(name = "类型", replace = {"黑_1", "灰_2"}, isImportField = "true")//将导入类型转为黑转为1,灰转为2,如导出反序
    private Integer type;
    @Excel(name = "备注", isImportField = "true")
    private String description;

    public ClAshBlackMenuModel() {
    }

    public ClAshBlackMenuModel(Long id, String deptNo, String deptName, String rank, String subcompany, String gzym, String empNo, String empName, Integer type, String description) {
        this.id = id;
        this.deptNo = deptNo;
        this.deptName = deptName;
        this.rank = rank;
        this.subcompany = subcompany;
        Gzym = gzym;
        this.empNo = empNo;
        this.empName = empName;
        this.type = type;
        this.description = description;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getSubcompany() {
        return subcompany;
    }

    public void setSubcompany(String subcompany) {
        this.subcompany = subcompany;
    }

    public String getGzym() {
        return Gzym;
    }

    public void setGzym(String gzym) {
        Gzym = gzym;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
