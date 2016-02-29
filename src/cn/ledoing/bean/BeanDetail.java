package cn.ledoing.bean;

/**
 * Created by wpt on 2015/8/18.
 */
public class BeanDetail {
   private String  balance;//明细发生后余额
    private String  time;
    private String addlecbean;//乐斗金额
    private String typename;//明细名称
    private String note;//明细备注
    private String status;//明细类型（2减少，1增加）

    public BeanDetail(String balance, String time, String addlecbean, String typename, String note, String status) {
        this.balance = balance;
        this.time = time;
        this.addlecbean = addlecbean;
        this.typename = typename;
        this.note = note;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddlecbean() {
        return addlecbean;
    }

    public void setAddlecbean(String addlecbean) {
        this.addlecbean = addlecbean;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BeanDetail(String balance, String time, String addlecbean, String typename, String note) {
        this.balance = balance;
        this.time = time;
        this.addlecbean = addlecbean;
        this.typename = typename;
        this.note = note;
    }
}
