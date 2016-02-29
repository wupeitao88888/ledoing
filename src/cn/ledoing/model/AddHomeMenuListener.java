package cn.ledoing.model;

import com.baidu.location.BDLocation;

/**
 * Created by wupeitao on 15/11/17.
 */
public interface AddHomeMenuListener {
    void onCityCall(String city, String pid, String cid);

    void onScope(String scope, String id);

    void onOrder(String order);

    void onLBS(double latitude, double longitude,String  street);
}
