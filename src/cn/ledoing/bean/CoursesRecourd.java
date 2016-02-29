package cn.ledoing.bean;


public class CoursesRecourd {
    private String lc_courses_day;
    private String lc_courses_week;
    private String lc_courses_time;
    private String lc_courses_timed;
    private String lc_courses_tname;
    private String lc_courses_prise;
    private String id;
    private String month;
    private String total_use;
    private String total_hour;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTotal_use() {
        return total_use;
    }

    public void setTotal_use(String total_use) {
        this.total_use = total_use;
    }

    public String getTotal_hour() {
        return total_hour;
    }

    public void setTotal_hour(String total_hour) {
        this.total_hour = total_hour;
    }

    public String getLc_courses_day() {
        return lc_courses_day;
    }

    public String getLc_courses_week() {
        return lc_courses_week;
    }

    public String getLc_courses_time() {
        return lc_courses_time;
    }

    public String getLc_courses_timed() {
        return lc_courses_timed;
    }

    public String getLc_courses_tname() {
        return lc_courses_tname;
    }

    public String getLc_courses_prise() {
        return lc_courses_prise;
    }

    public void setLc_courses_day(String lc_courses_day) {
        this.lc_courses_day = lc_courses_day;
    }

    public void setLc_courses_week(String lc_courses_week) {
        this.lc_courses_week = lc_courses_week;
    }

    public void setLc_courses_time(String lc_courses_time) {
        this.lc_courses_time = lc_courses_time;
    }

    public void setLc_courses_timed(String lc_courses_timed) {
        this.lc_courses_timed = lc_courses_timed;
    }

    public void setLc_courses_tname(String lc_courses_tname) {
        this.lc_courses_tname = lc_courses_tname;
    }

    public void setLc_courses_prise(String lc_courses_prise) {
        this.lc_courses_prise = lc_courses_prise;
    }


    public CoursesRecourd(String lc_courses_day, String lc_courses_week, String lc_courses_time, String lc_courses_timed, String lc_courses_tname, String lc_courses_prise, String id, String month, String total_use, String total_hour) {
        this.lc_courses_day = lc_courses_day;
        this.lc_courses_week = lc_courses_week;
        this.lc_courses_time = lc_courses_time;
        this.lc_courses_timed = lc_courses_timed;
        this.lc_courses_tname = lc_courses_tname;
        this.lc_courses_prise = lc_courses_prise;
        this.id = id;
        this.month = month;
        this.total_use = total_use;
        this.total_hour = total_hour;
    }

    public CoursesRecourd() {
    }
}
