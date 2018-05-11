/* 
 * SysCalendarPool.java created on 2017-12-22 上午 10:01:05 by roilatD
 */ 
package com.zhph.model.sys;

 * 系统日历表(用于工作日/非工作日数据查询/保存)
 * javadoc for com.zhph.sysCalendarPool.entity.SysCalendarPool
 * @author: roilatD
 * @since: 2017-12-22 上午 10:01:05
 */
public class SysCalendarPool{

	 * 当前日期
	 */
	private Integer cldDate;

	/**
	 * 当前日
	 */
	private Integer cldDay;

	/**
	 * 当前月
	 */
	private Integer cldMonth;

	/**
	 * 当前年
	 */
	private Integer cldYear;

	/**
	 * 当前星期(3-周三，周日：用”0”表示)
	 */
	private Integer week;

	/**
	 * 上一个工作日
	 */
	private Integer lastWkDt;

	/**
	 * 下一个工作日
	 */
	private Integer nextWkDt;

	/**
	 * 日期类型(0：工作日；1：休息日)
	 */
	private String cldFlg;

	/**
	 * 是否月底最后一个工作日(1是,0不是)
	 */
	private String ifLastWorkDay;


	public Integer getCldDate() {
		return cldDate;
	}

	public void setCldDateBeta(Integer cldDate) {
		this.cldDay = cldDate % 100;
		int temp = cldDate / 100;
		this.cldMonth = temp % 100;
		this.cldYear = temp / 100;
		this.cldDate = cldDate;
	}
	
		this.cldDate = cldDate;
	}

		return cldDay;
	}

		this.cldDay = cldDay;
	}

		return cldMonth;
	}

		this.cldMonth = cldMonth;
	}

		return cldYear;
	}

		this.cldYear = cldYear;
	}

		return week;
	}

		this.week = week;
	}

		return lastWkDt;
	}

		this.lastWkDt = lastWkDt;
	}

		return nextWkDt;
	}

		this.nextWkDt = nextWkDt;
	}

		return cldFlg;
	}

		this.cldFlg = cldFlg;
	}

		return ifLastWorkDay;
	}

		this.ifLastWorkDay = ifLastWorkDay;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cldDate == null) ? 0 : cldDate.hashCode());
		result = prime * result + ((cldDay == null) ? 0 : cldDay.hashCode());
		result = prime * result + ((cldMonth == null) ? 0 : cldMonth.hashCode());
		result = prime * result + ((cldYear == null) ? 0 : cldYear.hashCode());
		result = prime * result + ((week == null) ? 0 : week.hashCode());
		result = prime * result + ((lastWkDt == null) ? 0 : lastWkDt.hashCode());
		result = prime * result + ((nextWkDt == null) ? 0 : nextWkDt.hashCode());
		result = prime * result + ((cldFlg == null) ? 0 : cldFlg.hashCode());
		result = prime * result + ((ifLastWorkDay == null) ? 0 : ifLastWorkDay.hashCode());
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
		SysCalendarPool other = (SysCalendarPool) obj;
		if (cldDate == null) {
			if (other.cldDate != null)
				return false;
		} else if (!cldDate.equals(other.cldDate))
			return false;
		if (cldDay == null) {
			if (other.cldDay != null)
				return false;
		} else if (!cldDay.equals(other.cldDay))
			return false;
		if (cldMonth == null) {
			if (other.cldMonth != null)
				return false;
		} else if (!cldMonth.equals(other.cldMonth))
			return false;
		if (cldYear == null) {
			if (other.cldYear != null)
				return false;
		} else if (!cldYear.equals(other.cldYear))
			return false;
		if (week == null) {
			if (other.week != null)
				return false;
		} else if (!week.equals(other.week))
			return false;
		if (lastWkDt == null) {
			if (other.lastWkDt != null)
				return false;
		} else if (!lastWkDt.equals(other.lastWkDt))
			return false;
		if (nextWkDt == null) {
			if (other.nextWkDt != null)
				return false;
		} else if (!nextWkDt.equals(other.nextWkDt))
			return false;
		if (cldFlg == null) {
			if (other.cldFlg != null)
				return false;
		} else if (!cldFlg.equals(other.cldFlg))
			return false;
		if (ifLastWorkDay == null) {
			if (other.ifLastWorkDay != null)
				return false;
		} else if (!ifLastWorkDay.equals(other.ifLastWorkDay))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SysCalendarPool [ cldDate=" + cldDate + ", cldDay=" + cldDay + ", cldMonth=" + cldMonth + ", cldYear=" + cldYear + ", week=" + week + ", lastWkDt=" + lastWkDt + ", nextWkDt=" + nextWkDt + ", cldFlag=" + cldFlg + ", ifLastWorkDay=" + ifLastWorkDay + "]";
	}
}