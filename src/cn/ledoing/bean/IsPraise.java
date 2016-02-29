package cn.ledoing.bean;

import java.util.List;

/**
 * Created by wpt on 2015/8/31.
 */
public class IsPraise {
    private String groupsid;
    private String prise;
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


    public String getGroupsid() {
        return groupsid;
    }

    public void setGroupsid(String groupsid) {
        this.groupsid = groupsid;
    }

    public String getPrise() {
        return prise;
    }

    public void setPrise(String prise) {
        this.groupsid = prise;
    }

    public IsPraise(String prise, String groupsid, String userid) {
        super();
        this.prise = prise;
        this.groupsid = groupsid;
        this.userid = userid;
    }

    public IsPraise() {
        super();
    }

}
