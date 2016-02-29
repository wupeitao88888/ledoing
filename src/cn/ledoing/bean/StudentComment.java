package cn.ledoing.bean;

import android.database.DatabaseUtils;

import java.util.List;

/**
 * Created by lc-php1 on 2015/12/5.
 */
public class StudentComment extends BaseBean{

    private Date data;
    private String timestamp;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public class Date{
        private String ins_name;
        private String ins_id;
        private String start_time;
        private String end_time;
        private String base_name;
        private String base_id;
        private String teacher_name;
        private String teacher_id;
        private List<Zhujiao> zhujiao;
        private String ins_score;
        private String teacher_score;
        private String comment;

        public String getIns_score() {
            return ins_score;
        }

        public void setIns_score(String ins_score) {
            this.ins_score = ins_score;
        }

        public String getTeacher_score() {
            return teacher_score;
        }

        public void setTeacher_score(String teacher_score) {
            this.teacher_score = teacher_score;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getIns_name() {
            return ins_name;
        }

        public void setIns_name(String ins_name) {
            this.ins_name = ins_name;
        }

        public String getIns_id() {
            return ins_id;
        }

        public void setIns_id(String ins_id) {
            this.ins_id = ins_id;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getBase_name() {
            return base_name;
        }

        public void setBase_name(String base_name) {
            this.base_name = base_name;
        }

        public String getBase_id() {
            return base_id;
        }

        public void setBase_id(String base_id) {
            this.base_id = base_id;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public List<Zhujiao> getZhujiao() {
            return zhujiao;
        }

        public void setZhujiao(List<Zhujiao> zhujiao) {
            this.zhujiao = zhujiao;
        }

        public class Zhujiao{
            private String teacher_id;
            private String teacher_name;
            private String teacher_score;

            public String getTeacher_score() {
                return teacher_score;
            }

            public void setTeacher_score(String teacher_score) {
                this.teacher_score = teacher_score;
            }

            public String getTeacher_id() {
                return teacher_id;
            }

            public void setTeacher_id(String teacher_id) {
                this.teacher_id = teacher_id;
            }

            public String getTeacher_name() {
                return teacher_name;
            }

            public void setTeacher_name(String teacher_name) {
                this.teacher_name = teacher_name;
            }
        }
    }
}
