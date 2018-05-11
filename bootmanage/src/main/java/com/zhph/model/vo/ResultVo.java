package com.zhph.model.vo;

/**
 * 控制器返回结果到前台对象
 * 
 * @author yuel
 * @version 1.00
 * @see 参考类1
 * @Date 2015年5月18日 下午3:20:15
 */
public class ResultVo {

	/** 返回信息 */
	private String info;
	/** 返回状态(0=失败;1=成功;n=失败;y=成功;) */
	private Object status;
	/** 返回类型 */
	private String type;
	/** 返回数据 */
	private Object data;

	public ResultVo() {
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	/** 返回状态(0=失败;1=成功;n=失败;y=成功;) */
	public Object getStatus() {
		return status;
	}

	/** 返回状态(0=失败;1=成功;n=失败;y=成功;) */
	public void setStatus(Object status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
