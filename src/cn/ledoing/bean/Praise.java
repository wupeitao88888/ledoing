package cn.ledoing.bean;

public class Praise {
	private String errorCode;
	private String errorMessage;
	private String number;

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

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Praise(String errorCode, String errorMessage, String number) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.number = number;
	}

	public Praise() {
		super();
	}

}
