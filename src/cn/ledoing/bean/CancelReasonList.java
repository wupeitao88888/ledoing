package cn.ledoing.bean;

/**
 * Created by lc-php1 on 2015/9/25.
 */
public class CancelReasonList {

    private String id ;
    private String reason ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CancelReasonList(String id, String reason) {
        this.id = id ;
        this.reason = reason ;
    }
    public CancelReasonList() {
    }
}
