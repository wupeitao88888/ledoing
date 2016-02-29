package cn.ledoing.bean;

import com.baidu.cyberplayer.utils.T;

import java.util.List;

/**
 * Created by lc-lvfulong on 2015/9/5.
 */
public class LDChangeEorror {

    private String errorCode;
    private String errorMessage;
    private String total_count;
    private List<LDChange> ldChange;

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

    public List<LDChange> getLdChange() {
        return ldChange;
    }

    public void setLdChange(List<LDChange> ldChange) {
        this.ldChange = ldChange;
    }
}
