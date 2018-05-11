package com.zhph.model;

import java.io.Serializable;

public class Sex  implements Serializable{

	private static final long serialVersionUID = 1;

	private Integer id;

	private String sexName;

	private Integer sexValue;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSexName() {
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	public Integer getSexValue() {
		return sexValue;
	}

	public void setSexValue(Integer sexValue) {
		this.sexValue = sexValue;
	}
	
	@Override
	public String toString(){
		return "Sex[id="+id+",sexName="+sexName+",sexValue="+sexValue+"]";
	}

}
