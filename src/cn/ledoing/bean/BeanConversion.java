package cn.ledoing.bean;

/**
 * Created by wpt on 2015/8/18.
 */
public class BeanConversion {
    private String stautstype;
    private String money;
    private String expiration;
    private String vouchername;
    private String cambistld;

    public String getStautstype() {
        return stautstype;
    }

    public void setStautstype(String stautstype) {
        this.stautstype = stautstype;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getVouchername() {
        return vouchername;
    }

    public void setVouchername(String vouchername) {
        this.vouchername = vouchername;
    }

    public String getCambistld() {
        return cambistld;
    }

    public void setCambistld(String cambistld) {
        this.cambistld = cambistld;
    }

    public BeanConversion(String stautstype, String money, String expiration, String vouchername, String cambistld) {
        this.stautstype = stautstype;
        this.money = money;
        this.expiration = expiration;
        this.vouchername = vouchername;
        this.cambistld = cambistld;
    }
}
