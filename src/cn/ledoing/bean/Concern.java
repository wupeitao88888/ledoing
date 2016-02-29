package cn.ledoing.bean;

/**
 * Created by lc-php1 on 2015/11/18.
 */
public class Concern {

    private String ins_id;
    private String ins_name;
    private String face_pic;
    private String score;
    private String ins_addr;
    private String distance;
    private String comment_num;
    private String report_num;

    public Concern() {
    }

    public Concern(String ins_id, String ins_name, String face_pic, String score, String ins_addr, String distance, String comment_num, String report_num) {
        this.ins_id = ins_id;
        this.ins_name = ins_name;
        this.face_pic = face_pic;
        this.score = score;
        this.ins_addr = ins_addr;
        this.distance = distance;
        this.comment_num = comment_num;
        this.report_num = report_num;
    }

    public String getIns_id() {

        return ins_id;
    }

    public void setIns_id(String ins_id) {
        this.ins_id = ins_id;
    }

    public String getIns_name() {
        return ins_name;
    }

    public void setIns_name(String ins_name) {
        this.ins_name = ins_name;
    }

    public String getFace_pic() {
        return face_pic;
    }

    public void setFace_pic(String face_pic) {
        this.face_pic = face_pic;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIns_addr() {
        return ins_addr;
    }

    public void setIns_addr(String ins_addr) {
        this.ins_addr = ins_addr;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getReport_num() {
        return report_num;
    }

    public void setReport_num(String report_num) {
        this.report_num = report_num;
    }
}
