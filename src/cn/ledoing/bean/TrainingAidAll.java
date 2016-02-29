package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

public class TrainingAidAll implements Serializable {
	private String errorCode;
	private String errorMessage;

	private List<TrainingAid> list;

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

	public List<TrainingAid> getList() {
		return list;
	}

	public void setList(List<TrainingAid> list) {
		this.list = list;
	}

	public TrainingAidAll(String errorCode, String errorMessage,
			List<TrainingAid> list) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;

		this.list = list;
	}

	public TrainingAidAll() {
		super();
	}

}
