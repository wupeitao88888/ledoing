package cn.ledoing.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by cheers on 2015/9/28.
 */
public class OrderDetail implements Serializable {

    String start_time;//开始时间
    String institution;//活动中心
    String mastar_teacher_name;//老师
    String price;//价格
    String base_name;//课程名称
    String end_time;



    public OrderDetail(JSONObject jsonObject) {
        this.start_time = jsonObject.optString("start_time");
        this.institution = jsonObject.optString("institution");
        this.mastar_teacher_name = jsonObject.optString("mastar_teacher_name");
        this.price = jsonObject.optString("price");
        this.base_name = jsonObject.optString("base_name");
        this.end_time = jsonObject.optString("end_time");
    }

    public void setBase_name(String base_name) {
        this.base_name = base_name;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getBase_name() {

        return base_name;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setMastar_teacher_name(String mastar_teacher_name) {
        this.mastar_teacher_name = mastar_teacher_name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getInstitution() {
        return institution;
    }

    public String getMastar_teacher_name() {
        return mastar_teacher_name;
    }

    public String getPrice() {
        return price;
    }

    public OrderDetail(String start_time, String institution, String mastar_teacher_name, String price, String base_name, String end_time) {
        this.start_time = start_time;
        this.institution = institution;
        this.mastar_teacher_name = mastar_teacher_name;
        this.price = price;
        this.base_name = base_name;
        this.end_time = end_time;
    }
}
