package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lc-php1 on 2015/11/17.
 */
public class Institution implements Serializable {

    private String errorCode;
    private String errorMessage;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private Datas data;

    public class Datas implements Serializable{
        private String ins_id;
        private String ins_name;
        private String ins_addr;
        private String report_text;
        private String[] ins_album;
        private String face_pic;
        private String score;
        private String comment_num;
        private String ins_desc;
        private String business_hours;
        private String traffic_info;
        private String contact_info;
        private String isguanzhu;

        public String getFace_pic() {
            return face_pic;
        }

        public void setFace_pic(String face_pic) {
            this.face_pic = face_pic;
        }

        public String getIns_id() {
            return ins_id;
        }

        public String getIsguanzhu() {
            return isguanzhu;
        }

        public void setIsguanzhu(String isguanzhu) {
            this.isguanzhu = isguanzhu;
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

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public String getReport_text() {
            return report_text;
        }

        public void setReport_text(String report_text) {
            this.report_text = report_text;
        }

        public String[] getIns_album() {
            return ins_album;
        }

        public void setIns_album(String[] ins_album) {
            this.ins_album = ins_album;
        }

        public String getIns_desc() {

            return ins_desc;
        }

        public void setIns_desc(String ins_desc) {
            this.ins_desc = ins_desc;
        }

        public String getBusiness_hours() {
            return business_hours;
        }

        public void setBusiness_hours(String business_hours) {
            this.business_hours = business_hours;
        }

        public String getContact_info() {
            return contact_info;
        }

        public void setContact_info(String contact_info) {
            this.contact_info = contact_info;
        }

        public String getTraffic_info() {
            return traffic_info;
        }

        public void setTraffic_info(String traffic_info) {
            this.traffic_info = traffic_info;
        }

    }

    public Datas getData() {
        return data;
    }

    public void setData(Datas data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }


    public Institution() {
    }

    public Institution(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
