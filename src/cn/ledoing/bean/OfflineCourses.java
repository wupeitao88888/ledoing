package cn.ledoing.bean;

import android.text.TextUtils;

import java.util.List;


public class OfflineCourses {
    private String id;
    private String create_at;
    private String price;
    private String status;
    private String teacher_name;
    private String base_name;
    private String start_time;
    private String institution;
    private String cancelable;
    private String course_name;

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        if(TextUtils.isEmpty(status)){
            return "1";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getBase_name() {
        if(TextUtils.isEmpty(base_name)){
            return "";
        }
        return base_name;
    }

    public void setBase_name(String base_name) {
        this.base_name = base_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getCancelable() {
        return cancelable;
    }

    public void setCancelable(String cancelable) {
        this.cancelable = cancelable;
    }

    public OfflineCourses(String id, String create_at, String price, String status, String teacher_name, String base_name, String start_time, String institution, String cancelable, String course_name) {
        this.id = id;
        this.create_at = create_at;
        this.price = price;
        this.status = status;
        this.teacher_name = teacher_name;
        this.base_name = base_name;
        this.start_time = start_time;
        this.institution = institution;
        this.cancelable = cancelable;
        this.course_name = course_name;
    }

    public OfflineCourses() {

    }
}
