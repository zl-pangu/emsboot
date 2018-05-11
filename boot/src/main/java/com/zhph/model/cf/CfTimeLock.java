package com.zhph.model.cf;

import java.io.Serializable;

/**
 * Create By lishuangjiang
 * 消分考勤编辑锁
 */
public class CfTimeLock implements Serializable {

    private Long priNumber;//主键标识
    private Integer year;//年
    private Integer month;//月
    private Integer isLock;//是否上锁

    public Long getPriNumber() {
        return priNumber;
    }

    public void setPriNumber(Long priNumber) {
        this.priNumber = priNumber;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }
}
