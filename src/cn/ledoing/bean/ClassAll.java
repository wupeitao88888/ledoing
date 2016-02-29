package cn.ledoing.bean;

import java.util.List;

public class ClassAll {
	private String errorCode;
	private String errorMessage;
	private String total_count;
	private List<ClassList> list;

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

	public String getTotal_count() {
		return total_count;
	}

	public void setTotal_count(String total_count) {
		this.total_count = total_count;
	}

	public List<ClassList> getList() {
		return list;
	}

	public void setList(List<ClassList> list) {
		this.list = list;
	}

	public ClassAll(String errorCode, String errorMessage, String total_count,
			List<ClassList> list) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.total_count = total_count;
		this.list = list;
	}

	public ClassAll() {
		super();
	}

}
