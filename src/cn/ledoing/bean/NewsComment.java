package cn.ledoing.bean;

/**
 * Created by wupeitao on 15/11/25.
 */
public class NewsComment {
    private int user_id;
    private String comment_text;
    private long comment_time;
    private String head_pic;
    private String user_name;

    public NewsComment(int user_id, String comment_text, long comment_time, String head_pic, String user_name) {
        this.user_id = user_id;
        this.comment_text = comment_text;
        this.comment_time = comment_time;
        this.head_pic = head_pic;
        this.user_name = user_name;
    }

    public NewsComment() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
    }

    public long getComment_time() {
        return comment_time;
    }

    public void setComment_time(long comment_time) {
        this.comment_time = comment_time;
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
}
