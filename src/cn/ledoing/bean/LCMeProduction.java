package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class LCMeProduction implements Serializable {
	private String errorCode;
	private String errorMessage;
	private String total_count;
	private List<LCMeProductionList> list;

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

	public List<LCMeProductionList> getList() {
		return list;
	}

	public void setList(List<LCMeProductionList> list) {
		this.list = list;
	}

	public LCMeProduction(String errorCode, String errorMessage,
			String total_count, List<LCMeProductionList> list) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.total_count = total_count;
		this.list = list;
	}

	public LCMeProduction() {
		super();
	}

}
