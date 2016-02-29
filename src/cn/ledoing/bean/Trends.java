package cn.ledoing.bean;

import java.util.List;

/**
 * 动态
 * Created by wupeitao on 15/11/4.
 */
public class Trends extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private int total_count;
        public List<DataList> list;

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
            private String news_id;
            private String news_content;
            private List<String> news_img;
            private int ispraise;
            private int praise_num;
            private int comment_num;
            private long publish_time;
            private String news_pic;
            private String news_name;
            private String news_address;

            public String getNews_pic() {
                return news_pic;
            }

            public void setNews_pic(String news_pic) {
                this.news_pic = news_pic;
            }

            public String getNews_name() {
                return news_name;
            }

            public void setNews_name(String news_name) {
                this.news_name = news_name;
            }

            public String getNews_address() {
                return news_address;
            }

            public void setNews_address(String news_address) {
                this.news_address = news_address;
            }

            public String getNews_id() {
                return news_id;
            }

            public void setNews_id(String news_id) {
                this.news_id = news_id;
            }

            public String getNews_content() {
                return news_content;
            }

            public void setNews_content(String news_content) {
                this.news_content = news_content;
            }

            public List<String> getNews_img() {
                return news_img;
            }

            public void setNews_img(List<String> news_img) {
                this.news_img = news_img;
            }

            public int getIspraise() {
                return ispraise;
            }

            public void setIspraise(int ispraise) {
                this.ispraise = ispraise;
            }

            public int getPraise_num() {
                return praise_num;
            }

            public void setPraise_num(int praise_num) {
                this.praise_num = praise_num;
            }

            public int getComment_num() {
                return comment_num;
            }

            public void setComment_num(int comment_num) {
                this.comment_num = comment_num;
            }

            public long getPublish_time() {
                return publish_time;
            }

            public void setPublish_time(long publish_time) {
                this.publish_time = publish_time;
            }
        }
    }

}
