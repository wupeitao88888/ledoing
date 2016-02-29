package cn.ledoing.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheers on 2015/9/22.
 */
public class Courselist {
    String taskname;
    int taskid;
    String courseimg;
    int status;
    int max_number;
    int number;
    ArrayList<Student> studentlist;

    public Courselist(JSONObject jsonObject) {
        this.max_number = jsonObject.optInt("max_number");
        this.number = jsonObject.optInt("number");
        this.taskname = jsonObject.optString("taskname");
        this.taskid = jsonObject.optInt("taskid");
        this.courseimg = jsonObject.optString("courseimg");
        this.status = jsonObject.optInt("status");
        JSONArray jsonArray = jsonObject.optJSONArray("studentlist");
        studentlist = new ArrayList<Student>();
        if (jsonArray != null && jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                studentlist.add(new Student(jsonArray.optJSONObject(i)));
            }
        }
    }

    public void setMax_number(int max_number) {
        this.max_number = max_number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMax_number() {
        return max_number;
    }

    public int getNumber() {
        return number;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public void setCourseimg(String courseimg) {
        this.courseimg = courseimg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStudentlist(ArrayList<Student> studentlist) {
        this.studentlist = studentlist;
    }

    public String getTaskname() {
        return taskname;
    }

    public int getTaskid() {
        return taskid;
    }

    public String getCourseimg() {
        return courseimg;
    }

    public int getStatus() {
        return status;
    }

    public ArrayList<Student> getStudentlist() {
        return studentlist;
    }
}
