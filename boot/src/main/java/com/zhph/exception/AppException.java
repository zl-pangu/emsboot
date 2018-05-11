package com.zhph.exception;

public class AppException extends Exception {
	private static final long serialVersionUID = 6324925572816297819L;

	private Integer status;

	public AppException() {
		super();
		this.status = 500;
	}

	public AppException(String msg) {
		super(msg);
		this.status = 500;
	}

	public AppException(String msg, Integer status) {
		super(msg);
		this.status = status;
	}

	public AppException(Exception e) {
		super(e);
	}

	public Integer getStatus() {
		return status;
	}

}
