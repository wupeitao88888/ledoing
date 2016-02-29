package cn.ledoing.bean;

import java.io.Serializable;

/**
 * Created by wpt on 2015/9/25.
 */
public class AdvertisementList implements Serializable {
    private  String ads_name;
    private String  ads_type;
    private String ads_url;
    private String media_url;
    private String ads_content;
    private String start_time;
    private String end_time;

    public String getAds_name() {
        return ads_name;
    }

    public void setAds_name(String ads_name) {
        this.ads_name = ads_name;
    }

    public String getAds_type() {
        return ads_type;
    }

    public void setAds_type(String ads_type) {
        this.ads_type = ads_type;
    }

    public String getAds_url() {
        return ads_url;
    }

    public void setAds_url(String ads_url) {
        this.ads_url = ads_url;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getAds_content() {
        return ads_content;
    }

    public void setAds_content(String ads_content) {
        this.ads_content = ads_content;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public AdvertisementList(String ads_name, String ads_type, String ads_url, String media_url, String ads_content, String start_time, String end_time) {
        this.ads_name = ads_name;
        this.ads_type = ads_type;
        this.ads_url = ads_url;
        this.media_url = media_url;
        this.ads_content = ads_content;
        this.start_time = start_time;
        this.end_time = end_time;
    }
}
