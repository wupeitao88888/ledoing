package cn.ledoing.bean;

/**
 * Created by wupeitao on 15/11/17.
 */
public class Sequence {
    private String content;
    public String id;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Sequence(String content, String status) {
        this.content = content;
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Sequence() {
    }
}
