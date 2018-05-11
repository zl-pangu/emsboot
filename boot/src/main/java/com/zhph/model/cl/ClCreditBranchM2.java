package com.zhph.model.cl;

import java.math.BigDecimal;

public class ClCreditBranchM2 {
    private BigDecimal priNumber;

    private String branchName;

    private BigDecimal yql;

    private String gzYm;

    private String createTime;

    public BigDecimal getPriNumber() {
        return priNumber;
    }

    public void setPriNumber(BigDecimal priNumber) {
        this.priNumber = priNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName == null ? null : branchName.trim();
    }

    public BigDecimal getYql() {
        return yql;
    }

    public void setYql(BigDecimal yql) {
        this.yql = yql;
    }

    public String getGzYm() {
        return gzYm;
    }

    public void setGzYm(String gzYm) {
        this.gzYm = gzYm == null ? null : gzYm.trim();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }
}