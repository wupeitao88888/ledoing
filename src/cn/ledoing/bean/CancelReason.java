package cn.ledoing.bean;

import com.baidu.cyberplayer.utils.T;

import java.util.List;

/**
 * Created by lc-php1 on 2015/9/25.
 */
public class CancelReason {

    private String errorCode ;
    private String errorMessage ;
    private List<CancelReasonList> cancelReasonList;

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

    public List<CancelReasonList> getCancelReasonList() {
        return cancelReasonList;
    }

    public void setCancelReasonList(List<CancelReasonList> cancelReasonList) {
        this.cancelReasonList = cancelReasonList;
    }
}
