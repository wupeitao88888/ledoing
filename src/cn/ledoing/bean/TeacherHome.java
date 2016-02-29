package cn.ledoing.bean;

import java.util.List;

/**
 * Created by wupeitao on 15/11/24.
 */
public class TeacherHome extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private String teacher_id;
        private String teacher_name;
        private String head_pic;
        private String teacher_sign;
        private String teacher_desc;
        private String score;
        private String score_num;
        private String teacher_addr;
        private List<String> score_list;

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

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getTeacher_sign() {
            return teacher_sign;
        }

        public void setTeacher_sign(String teacher_sign) {
            this.teacher_sign = teacher_sign;
        }

        public String getTeacher_desc() {
            return teacher_desc;
        }

        public void setTeacher_desc(String teacher_desc) {
            this.teacher_desc = teacher_desc;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getScore_num() {
            return score_num;
        }

        public void setScore_num(String score_num) {
            this.score_num = score_num;
        }

        public String getTeacher_addr() {
            return teacher_addr;
        }

        public void setTeacher_addr(String teacher_addr) {
            this.teacher_addr = teacher_addr;
        }

        public List<String> getScore_list() {
            return score_list;
        }

        public void setScore_list(List<String> score_list) {
            this.score_list = score_list;
        }
    }
}
