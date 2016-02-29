package cn.ledoing.bean;

import org.json.JSONObject;

/**
 * Created by cheers on 2015/9/8.
 */
public class ActivityDetailsListBean {

    String class_id;
    String start_time;
    String end_time;
    String master_teacher;
    int max_number;
    int number;
    String is_re;
    int status;
    String end_reservation_time;
    String member_price;
    String prime_price;
    String base_id;
    String base_name;

    public String getBase_id() {
        return base_id;
    }

    public void setBase_id(String base_id) {
        this.base_id = base_id;
    }

    public String getBase_name() {
        return base_name;
    }

    public void setBase_name(String base_name) {
        this.base_name = base_name;
    }

    long rest_time;

    public ActivityDetailsListBean(JSONObject jsonObject) {
        class_id = jsonObject.optString("class_id");
        start_time = jsonObject.optString("start_time");
        end_time = jsonObject.optString("end_time");
        master_teacher = jsonObject.optString("master_teacher");
        max_number = jsonObject.optInt("max_number");
        number = jsonObject.optInt("number");
        is_re = jsonObject.optString("is_re");
        status = jsonObject.optInt("status");
        end_reservation_time = jsonObject.optString("end_reservation_time");
        member_price = jsonObject.optString("member_price");
        prime_price = jsonObject.optString("prime_price");
        base_id = jsonObject.optString("base_id");
        base_name = jsonObject.optString("base_name");
        rest_time = jsonObject.optLong("rest_time");
    }

    public void setRest_time(long rest_time) {
        this.rest_time = rest_time;
    }

    public long getRest_time() {

        return rest_time;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setMaster_teacher(String master_teacher) {
        this.master_teacher = master_teacher;
    }

    public void setMax_number(int max_number) {
        this.max_number = max_number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setIs_re(String is_re) {
        this.is_re = is_re;
    }


    public void setEnd_reservation_time(String end_reservation_time) {
        this.end_reservation_time = end_reservation_time;
    }

    public void setMember_price(String member_price) {
        this.member_price = member_price;
    }

    public void setPrime_price(String prime_price) {
        this.prime_price = prime_price;
    }

    public ActivityDetailsListBean() {
    }

    public String getClass_id() {
        return class_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getMaster_teacher() {
        return master_teacher;
    }

    public int getMax_number() {
        return max_number;
    }

    public int getNumber() {
        return number;
    }

    public String getIs_re() {
        return is_re;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {

        return status;
    }

    public String getEnd_reservation_time() {
        return end_reservation_time;
    }

    public String getMember_price() {
        return member_price;
    }

    public String getPrime_price() {
        return prime_price;
    }

}
