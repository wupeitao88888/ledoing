package cn.ledoing.bean;

/**
 * Created by lvfl on 2015/9/5.
 */
public class LDChange {

    private int id;
    private String code;
    private String state;
    private String name;
    private String begin_name;
    private String end_time;
    private String reward;

    public LDChange(int id, String code, String state, String name, String begin_name, String end_time, String reward) {
        this.id = id;
        this.code = code;
        this.state = state;
        this.name = name;
        this.begin_name = begin_name;
        this.end_time = end_time;
        this.reward = reward;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBegin_name() {
        return begin_name;
    }

    public void setBegin_name(String begin_name) {
        this.begin_name = begin_name;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
