package cn.ledoing.bean;

import java.util.List;

public class Theme {
	private String errorCode;
	private String errorMessage;
	private String total_count;
	private List<ThemeGroup> list;

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

    public List<ThemeGroup> getList() {
        return list;
    }

    public void setList(List<ThemeGroup> list) {
        this.list = list;
    }

    public Theme(String errorCode, String errorMessage, String total_count, List<ThemeGroup> list) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.total_count = total_count;
        this.list = list;
    }

    public Theme() {
		super();
	}

}
