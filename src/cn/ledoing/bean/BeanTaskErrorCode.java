package cn.ledoing.bean;

import com.baidu.cyberplayer.utils.T;

import java.util.List;

/**
 * Created by Administrator on 2015/9/4.
 */
public class BeanTaskErrorCode {
    public String errorCode;
    public String errorMessage;
    public String total_count;
    public List<BeanTask> list;


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

    public List<BeanTask> getList() {
        return list;
    }

    public void setList(List<BeanTask> list) {
        this.list = list;
    }

    public BeanTaskErrorCode(String errorCode, String errorMessage, String total_count, List<BeanTask> list) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.total_count = total_count;
        this.list = list;
    }

    public BeanTaskErrorCode() {
    }


}
