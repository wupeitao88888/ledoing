package cn.ledoing.bean;

/**
 * Created by cheers on 2015/9/28.
 */
public class Provice {
    String name;
    String  id;

    public Provice(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
