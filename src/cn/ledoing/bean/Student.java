package cn.ledoing.bean;

import org.json.JSONObject;

/**
 * Created by cheers on 2015/9/25.
 */
public class Student {

    String name;
    String avatar;

    public Student(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public Student(JSONObject jsonObject) {
        this.name = jsonObject.optString("name");
        this.avatar = jsonObject.optString("avatar");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }
}
