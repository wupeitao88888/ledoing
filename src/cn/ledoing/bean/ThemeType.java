package cn.ledoing.bean;

import java.io.Serializable;

public class ThemeType implements Serializable {


    private String agegroupid;
    private String agegroupname;
    private String coursename;
    private String courseid;
    private String courseimg;
    private String lock;
    private String price;
    private String isbuy;
    private String oldPrice;


    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getAgegroupid() {
        return agegroupid;
    }

    public void setAgegroupid(String agegroupid) {
        this.agegroupid = agegroupid;
    }

    public String getAgegroupname() {
        return agegroupname;
    }

    public void setAgegroupname(String agegroupname) {
        this.agegroupname = agegroupname;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getCourseimg() {
        return courseimg;
    }

    public void setCourseimg(String courseimg) {
        this.courseimg = courseimg;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getIsbuy() {
        return isbuy;
    }

    public void setIsbuy(String isbuy) {
        this.isbuy = isbuy;
    }

    public ThemeType(String agegroupid, String agegroupname, String coursename, String courseid, String courseimg, String lock) {
        this.agegroupid = agegroupid;
        this.agegroupname = agegroupname;
        this.coursename = coursename;
        this.courseid = courseid;
        this.courseimg = courseimg;
        this.lock = lock;
    }

    public ThemeType() {
        super();
    }

}
