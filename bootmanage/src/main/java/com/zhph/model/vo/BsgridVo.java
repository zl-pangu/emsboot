package com.zhph.model.vo;

import java.io.Serializable;
import java.util.List;

public class BsgridVo<T> implements Serializable{
	private static final long serialVersionUID = -2517517491552986107L;

	private Boolean success;
	
	private Long totalRows;
	
	private Long curPage;
	
	private List<T> data;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Long totalRows) {
		this.totalRows = totalRows;
	}

	public Long getCurPage() {
		return curPage;
	}

	public void setCurPage(Long curPage) {
		this.curPage = curPage;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

}
