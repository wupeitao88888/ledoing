package cn.ledoing.bean;

import java.util.List;

public class FindTitle {
	private String errorCode;
	private String errorMessage;
	private String total_count;
	private List<FindTitleList> list;

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

	public List<FindTitleList> getList() {
		return list;
	}

	public void setList(List<FindTitleList> list) {
		this.list = list;
	}

	public FindTitle(String errorCode, String errorMessage, String total_count,
			List<FindTitleList> list) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.total_count = total_count;
		this.list = list;
	}

	public FindTitle() {
		super();
	}

}
