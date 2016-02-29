package cn.ledoing.bean;

import com.alibaba.fastjson.JSON;

import org.json.JSONObject;

/**
 * Created by cheers on 2015/9/15.
 */
public class LCTeacherBean {
    String teacherName;
    String teacherID;

    public LCTeacherBean(JSONObject jsonObject) {
        teacherName = jsonObject.optString("teacher_name");
        teacherID = jsonObject.optString("teacher_id");
    }

    public LCTeacherBean(String teacherID, String teacherName) {
        this.teacherID = teacherID;
        this.teacherName = teacherName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }
}
