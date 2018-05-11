/* 
 * CfPaidLeave.java created on 2018-01-12 下午 14:15:42 by roilatD
 */ 
package com.zhph.model.cf;
import java.util.Date;
/**
 * 月调休天数配置表
 * @author: roilatD
 * @since: 2018-01-12 下午 14:15:42
 */
public class CfPaidLeave {
	/**
	 * 唯一标识符
	 */
	private Long priNumber;

	/**
	 * 工资年月
	 */
	private String gzym;

	/**
	 * 工作日调休天数
	 */
	private String paidLeaveDays;

	/**
	 * 周末调休天数
	 */
	private String weekendPaidLeave;

	/**
	 * 描述
	 */
	private String comments;

	/**
	 * 创建人姓名
	 */
	private String createName;

	/**
	 * 创建时间
	 */
	private Date createDate;

	/**
	 * 修改人姓名
	 */
	private String updateName;

	/**
	 * 修改时间
	 */
	private Date updateDate;


	public Long getPriNumber() {
		return priNumber;
	}
	public void setPriNumber(Long priNumber) {
		this.priNumber = priNumber;
	}
	public String getGzym() {
		return gzym;
	}
	public void setGzym(String gzym) {
		this.gzym = gzym;
	}
	public String getPaidLeaveDays() {
		return paidLeaveDays;
	}
	public void setPaidLeaveDays(String paidLeaveDays) {
		this.paidLeaveDays = paidLeaveDays;
	}
	public String getWeekendPaidLeave() {
		return weekendPaidLeave;
	}
	public void setWeekendPaidLeave(String weekendPaidLeave) {
		this.weekendPaidLeave = weekendPaidLeave;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateName() {
		return updateName;
	}
	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((priNumber == null) ? 0 : priNumber.hashCode());
		result = prime * result + ((gzym == null) ? 0 : gzym.hashCode());
		result = prime * result + ((paidLeaveDays == null) ? 0 : paidLeaveDays.hashCode());
		result = prime * result + ((weekendPaidLeave == null) ? 0 : weekendPaidLeave.hashCode());
		result = prime * result + ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((createName == null) ? 0 : createName.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((updateName == null) ? 0 : updateName.hashCode());
		result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
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
		CfPaidLeave other = (CfPaidLeave) obj;
		if (priNumber == null) {
			if (other.priNumber != null)
				return false;
		} else if (!priNumber.equals(other.priNumber))
			return false;
		if (gzym == null) {
			if (other.gzym != null)
				return false;
		} else if (!gzym.equals(other.gzym))
			return false;
		if (paidLeaveDays == null) {
			if (other.paidLeaveDays != null)
				return false;
		} else if (!paidLeaveDays.equals(other.paidLeaveDays))
			return false;
		if (weekendPaidLeave == null) {
			if (other.weekendPaidLeave != null)
				return false;
		} else if (!weekendPaidLeave.equals(other.weekendPaidLeave))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (createName == null) {
			if (other.createName != null)
				return false;
		} else if (!createName.equals(other.createName))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (updateName == null) {
			if (other.updateName != null)
				return false;
		} else if (!updateName.equals(other.updateName))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CfPaidLeave [ priNumber=" + priNumber + ", gzym=" + gzym + ", paidLeaveDays=" + paidLeaveDays + ", weekendPaidLeave=" + weekendPaidLeave + ", comments=" + comments + ", createName=" + createName + ", createDate=" + createDate + ", updateName=" + updateName + ", updateDate=" + updateDate + "]";
	}
}