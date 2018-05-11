/* 
 * HqclcfTimelock.java created on 2018-01-08 下午 18:09:34 by roilatD
 */ 
package com.zhph.model.hqclcf;
/**
 * 消金考勤编辑锁model
 * @author roilat-D
 *
 */public class HqclcfTimelock {
	/**
	 * 年
	 */
	private Integer year;

	/**
	 * 月
	 */
	private Integer month;

	/**
	 * 是否上锁(0:否,1:是)
	 */
	private Integer isLock;

	/**
	 * 业务条线
	 */
	private Integer businessLine;


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
	public Integer getBusinessLine() {
		return businessLine;
	}
	public void setBusinessLine(Integer businessLine) {
		this.businessLine = businessLine;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((year == null) ? 0 : year.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((isLock == null) ? 0 : isLock.hashCode());
		result = prime * result + ((businessLine == null) ? 0 : businessLine.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HqclcfTimelock other = (HqclcfTimelock) obj;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (isLock == null) {
			if (other.isLock != null)
				return false;
		} else if (!isLock.equals(other.isLock))
			return false;
		if (businessLine == null) {
			if (other.businessLine != null)
				return false;
		} else if (!businessLine.equals(other.businessLine))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HqclcfTimelock [ year=" + year + ", month=" + month + ", isLock=" + isLock + ", businessLine=" + businessLine + "]";
	}
}