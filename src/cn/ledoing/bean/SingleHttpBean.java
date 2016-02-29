package cn.ledoing.bean;

/**
 * Created by lvfl on 2015/9/6.
 * 此类用于单个解析网络请求
 */
public class SingleHttpBean {

    private String errorCode;
    private String errorMessage;
    /**
     * 我的乐豆界面
     */
    private String reward; // 乐豆余额

    /**
     * 我的课程界面
     */
    private String course_hours = ""; // 用户充值课时
    private String give_course_hours = ""; // 用户赠送课时数
    private String money_amount = ""; // 用户充值金额
    private String give_money_amount = ""; // 用户赠送金额
    private String is_mumber = ""; // 判断几组数据

    public String getIs_mumber() {
        return is_mumber;
    }

    public void setIs_mumber(String is_mumber) {
        this.is_mumber = is_mumber;
    }

    public String getCourse_hours() {
        return course_hours;
    }

    public void setCourse_hours(String course_hours) {
        this.course_hours = course_hours;
    }

    public String getGive_course_hours() {
        return give_course_hours;
    }

    public void setGive_course_hours(String give_course_hours) {
        this.give_course_hours = give_course_hours;
    }

    public String getMoney_amount() {
        return money_amount;
    }

    public void setMoney_amount(String money_amount) {
        this.money_amount = money_amount;
    }

    public String getGive_money_amount() {
        return give_money_amount;
    }

    public void setGive_money_amount(String give_money_amount) {
        this.give_money_amount = give_money_amount;
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

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
