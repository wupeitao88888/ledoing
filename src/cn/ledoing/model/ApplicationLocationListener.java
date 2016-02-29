package cn.ledoing.model;

import com.baidu.location.BDLocation;

/**
 * Created by wupeitao on 15/11/17.
 */
public interface ApplicationLocationListener {
    void onReceiveLocation(BDLocation var1);
    void fild();
}
