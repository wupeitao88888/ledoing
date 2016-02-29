package cn.ledoing.bean;

import java.util.List;

/**
 * 评论
 * Created by wupeitao on 15/11/4.
 */
public class Comment extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private int total_count;
        private List<ListData> list;

        public int getTotal_count() {
            return total_count;
        }

        public void setTotal_count(int total_count) {
            this.total_count = total_count;
        }

        public List<ListData> getList() {
            return list;
        }

        public void setList(List<ListData> list) {
            this.list = list;
        }

        public class ListData {
            private String user_id;
            private String head_pic;
            private String user_name;
            private String comment_score;
            private String comment_time;
            private String comment_text;
            private String task_name;

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getHead_pic() {
                return head_pic;
            }

            public void setHead_pic(String head_pic) {
                this.head_pic = head_pic;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getComment_score() {
                return comment_score;
            }

            public void setComment_score(String comment_score) {
                this.comment_score = comment_score;
            }

            public String getComment_time() {
                return comment_time;
            }

            public void setComment_time(String comment_time) {
                this.comment_time = comment_time;
            }

            public String getComment_text() {
                return comment_text;
            }

            public void setComment_text(String comment_text) {
                this.comment_text = comment_text;
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

