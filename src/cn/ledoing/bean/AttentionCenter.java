package cn.ledoing.bean;

import java.util.List;

/**
 * 中心
 * Created by wpt on 2015/11/2.
 */
public class AttentionCenter extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private int total_count;
        private List<DataList> list;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public List<DataList> getList() {
            return list;
        }

        public void setList(List<DataList> list) {
            this.list = list;
        }
        public class DataList {
            private String ins_id;
            private String face_pic;
            private String ins_addr;
            private String score;
            private String ins_name;
            private String comment_num;
            private String distance;
            private int report_num;
            private List<String> teacher_list;

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

            public String getComment_num() {
                return comment_num;
            }

            public void setComment_num(String comment_num) {
                this.comment_num = comment_num;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public int getReport_num() {
                return report_num;
            }

            public void setReport_num(int report_num) {
                this.report_num = report_num;
            }

            public List<String> getTeacher_list() {
                return teacher_list;
            }

            public void setTeacher_list(List<String> teacher_list) {
                this.teacher_list = teacher_list;
            }
        }
    }


}
