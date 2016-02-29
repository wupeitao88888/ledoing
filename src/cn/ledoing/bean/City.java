package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable{
	private String errorCode;
	private String errorMessage;
	private List<ProvinceModel> list;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public List<ProvinceModel> getList() {
		return list;
	}

	public void setList(List<ProvinceModel> list) {
		this.list = list;
	}

	public City(String errorCode, String errorMessage, List<ProvinceModel> list) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.list = list;
	}

	public City() {
		super();
	}

}
