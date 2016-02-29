package cn.ledoing.bean;

import java.io.Serializable;

/**
 * Created by wupeitao on 15/11/23.
 */
public class BaseBean implements Serializable{
    private  String errorCode;
    private String errorMessage;

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

}
