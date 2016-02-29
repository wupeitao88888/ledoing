package cn.ledoing.bean;

import java.util.List;

/**
 * Created by lc-php1 on 2015/11/24.
 */
public class InstitutionComment extends BaseBean {

    private Datas data;

    public Datas getData() {
        return data;
    }

    public void setData(Datas data) {
        this.data = data;
    }

    public class Datas {
        private String total_count;
        private List<ListData> list;

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

        public class ListData{

            private String id;
            private String user_id;
            private String user_name;
            private String head_pic;
            private String teacher_name;
            private String teacher_id;
            private String comment_text;
            private String comment_score;
            private String publish_time;
            private Long comment_time;
            private String task_name;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
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

            public String getTeacher_id() {
                return teacher_id;
            }

            public void setTeacher_id(String teacher_id) {
                this.teacher_id = teacher_id;
            }

            public String getComment_text() {
                return comment_text;
            }

            public void setComment_text(String comment_text) {
                this.comment_text = comment_text;
            }

            public String getComment_score() {
                return comment_score;
            }

            public void setComment_score(String comment_score) {
                this.comment_score = comment_score;
            }

            public String getPublish_time() {
                return publish_time;
            }

            public void setPublish_time(String publish_time) {
                this.publish_time = publish_time;
            }

            public Long getComment_time() {
                return comment_time;
            }

            public void setComment_time(Long comment_time) {
                this.comment_time = comment_time;
            }

            public String getTask_name() {
                return task_name;
            }

            public void setTask_name(String task_name) {
                this.task_name = task_name;
            }
        }
    }
}
