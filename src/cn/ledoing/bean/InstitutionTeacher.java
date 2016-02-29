package cn.ledoing.bean;

import java.util.List;

/**
 * Created by lc-php1 on 2015/11/24.
 */
public class InstitutionTeacher extends BaseBean {

    private List<Datas> data;

    public List<Datas> getData() {
        return data;
    }

    public void setData(List<Datas> data) {
        this.data = data;
    }

    public class Datas {

        private String teacher_id;
        private String head_pic;
        private String teacher_name;
        private String teacher_score;
        private String teacher_desc;

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public String getHead_pic() {
            return head_pic;
        }

        public void setHead_pic(String head_pic) {
            this.head_pic = head_pic;
        }

        public String getTeacher_name() {
            return teacher_name;
        }

        public void setTeacher_name(String teacher_name) {
            this.teacher_name = teacher_name;
        }

        public String getTeacher_score() {
            return teacher_score;
        }

        public void setTeacher_score(String teacher_score) {
            this.teacher_score = teacher_score;
        }

        public String getTeacher_desc() {
            return teacher_desc;
        }

        public void setTeacher_desc(String teacher_desc) {
            this.teacher_desc = teacher_desc;
        }
    }

}
