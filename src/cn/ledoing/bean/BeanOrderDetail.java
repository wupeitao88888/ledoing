package cn.ledoing.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheers on 2015/9/9.
 */
public class BeanOrderDetail {

    String coursename;
    String schooltime;
    String endtime;
    String teacher;
    String centername;
    ArrayList<Courselist> courselist;

    public BeanOrderDetail(JSONObject jsonObject) {
        this.coursename = jsonObject.optString("coursename");
        this.schooltime = jsonObject.optString("schooltime");
        this.endtime = jsonObject.optString("endtime");
        this.teacher = jsonObject.optString("teacher");
        this.centername = jsonObject.optString("centername");
        JSONArray jsonArray = jsonObject.optJSONArray("courselist");
        courselist = new ArrayList<Courselist>();
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                courselist.add(new Courselist(jsonArray.optJSONObject(i)));
            }
        }
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setSchooltime(String schooltime) {
        this.schooltime = schooltime;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setCentername(String centername) {
        this.centername = centername;
    }

    public void setCourselist(ArrayList<Courselist> courselist) {
        this.courselist = courselist;
    }

    public String getCoursename() {

        return coursename;
    }

    public String getSchooltime() {
        return schooltime;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getCentername() {
        return centername;
    }

    public ArrayList<Courselist> getCourselist() {
        return courselist;
    }
}
