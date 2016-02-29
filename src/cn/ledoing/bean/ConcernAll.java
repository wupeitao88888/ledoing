package cn.ledoing.bean;

import java.util.List;

/**
 * Created by lc-php1 on 2015/11/18.
 */
public class ConcernAll {

    private String errorCode;
    private String errorMessage;
    private String total_count;
    private List<AttentionCenter> list;

    public ConcernAll() {
    }

    public ConcernAll(String errorCode, String errorMessage, String total_count, List<AttentionCenter> list) {

        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.total_count = total_count;
        this.list = list;
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

    public List<AttentionCenter> getList() {
        return list;
    }

    public void setList(List<AttentionCenter> list) {
        this.list = list;
    }
}
