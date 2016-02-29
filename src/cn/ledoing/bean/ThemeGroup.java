package cn.ledoing.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wpt on 2015/8/20.
 */
public class ThemeGroup implements Serializable {
    private String categoryid;
    private String name;
    private String baseimg;
    private String grouplock;
    private int mclolor;
    private List<ThemeType> list;

    public List<ThemeType> getList() {
        return list;
    }

    public void setList(List<ThemeType> list) {
        this.list = list;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBaseimg() {
        return baseimg;
    }

    public void setBaseimg(String baseimg) {
        this.baseimg = baseimg;
    }

    public String getGrouplock() {
        return grouplock;
    }

    public void setGrouplock(String grouplock) {
        this.grouplock = grouplock;
    }

    public int getMclolor() {
        return mclolor;
    }

    public void setMclolor(int mclolor) {
        this.mclolor = mclolor;
    }

    public ThemeGroup(String categoryid, String name, String baseimg, String grouplock, int mclolor, List<ThemeType> list) {
        this.categoryid = categoryid;
        this.name = name;
        this.baseimg = baseimg;
        this.grouplock = grouplock;
        this.mclolor = mclolor;
        this.list = list;
    }

    public ThemeGroup() {
    }
}
