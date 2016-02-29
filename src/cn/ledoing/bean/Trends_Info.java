package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 动态内容中得图片
 * Created by wupeitao on 15/11/4.
 */
public class Trends_Info extends BaseBean {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private int news_id;
        private String news_content;
        private List<String> news_img;
        private List<NewsComment> news_comment;
        private long publish_time;
        private String praise_num;
        private String comment_num;
        private int ispraise;

        public long getPublish_time() {
            return publish_time;
        }

        public void setPublish_time(long publish_time) {
            this.publish_time = publish_time;
        }

        public String getPraise_num() {
            return praise_num;
        }

        public void setPraise_num(String praise_num) {
            this.praise_num = praise_num;
        }

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
        }

        public int getIspraise() {
            return ispraise;
        }

        public void setIspraise(int ispraise) {
            this.ispraise = ispraise;
        }

        public int getNews_id() {
            return news_id;
        }

        public void setNews_id(int news_id) {
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

        public List<NewsComment> getNews_comment() {
            return news_comment;
        }

        public void setNews_comment(List<NewsComment> news_comment) {
            this.news_comment = news_comment;
        }


    }
}
