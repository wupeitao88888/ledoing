package cn.ledoing.bean;

import java.util.List;

/**
 * Created by wupeitao on 15/11/23.
 */
public class FCenterAll {
    private int all_total_count;
    private List<All_list> all_list;

    public int getAll_total_count() {
        return all_total_count;
    }

    public void setAll_total_count(int all_total_count) {
        this.all_total_count = all_total_count;
    }

    public List<All_list> getAll_list() {
        return all_list;
    }

    public void setAll_list(List<All_list> all_list) {
        this.all_list = all_list;
    }

    public class All_list {
        private String ins_id;
        private String face_pic;
        private String ins_addr;
        private String score;
        private String ins_name;
        private int comment_num;
        private String distance;

        public String getIns_id() {
            return ins_id;
        }

        public void setIns_id(String ins_id) {
            this.ins_id = ins_id;
        }

        public String getFace_pic() {
            return face_pic;
        }

        public void setFace_pic(String face_pic) {
            this.face_pic = face_pic;
        }

        public String getIns_addr() {
            return ins_addr;
        }

        public void setIns_addr(String ins_addr) {
            this.ins_addr = ins_addr;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getIns_name() {
            return ins_name;
        }

        public void setIns_name(String ins_name) {
            this.ins_name = ins_name;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }
    }

}
