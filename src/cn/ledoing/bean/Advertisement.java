package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wpt on 2015/9/25.
 */
public class Advertisement implements Serializable {
    private String errorCode;
    private String errorMessage;
    private String total_count;
    private List<AdvertisementList> list;

    public Advertisement() {
    }

    public Advertisement(String total_count) {
        this.total_count = total_count;
    }

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

    public List<AdvertisementList> getList() {
        return list;
    }

    public void setList(List<AdvertisementList> list) {
        this.list = list;
    }
}
