package cn.ledoing.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cheers on 2015/9/23.
 */
public class CenterCity {

    private String errorCode;
    private String errorMessage;
    private List<ProvinceBean> list;

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

    public List<ProvinceBean> getList() {
        return list;
    }

    public void setList(List<ProvinceBean> list) {
        this.list = list;
    }

    public CenterCity(String errorCode, String errorMessage, List<ProvinceBean> list) {
        super();
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.list = list;
    }

    public CenterCity() {
        super();
    }
}
