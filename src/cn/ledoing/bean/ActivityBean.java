package cn.ledoing.bean;

import org.json.JSONObject;

/**
 * Created by cheers on 2015/9/8.
 */
public class ActivityBean {

    String institution_id;
    String address;
    String institution_name;
    String phone_number;
    int has_class;


    public ActivityBean() {
    }

    public ActivityBean(JSONObject jsonObject) {
        this.has_class = jsonObject.optInt("has_class");
        this.address = jsonObject.optString("address");
        this.phone_number = jsonObject.optString("phone_number");
        this.institution_id = jsonObject.optString("institution_id");
        this.institution_name = jsonObject.optString("institution_name");
    }

    public void setHas_class(int has_class) {
        this.has_class = has_class;
    }

    public int getHas_class() {

        return has_class;
    }

    public void setInstitution_id(String institution_id) {
        this.institution_id = institution_id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInstitution_name(String institution_name) {
        this.institution_name = institution_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getInstitution_id() {
        return institution_id;
    }

    public String getAddress() {
        return address;
    }

    public String getInstitution_name() {
        return institution_name;
    }

    public String getPhone_number() {
        return phone_number;
    }
}
