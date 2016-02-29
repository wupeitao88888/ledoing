package cn.ledoing.camera;


import cn.ledoing.global.LCApplication;

public class DistanceUtil {

    public static int getCameraAlbumWidth() {
        return (LCApplication.getApp().getScreenWidth() - LCApplication.getApp().dp2px(10)) / 4 - LCApplication.getApp().dp2px(4);
    }
    
    // 相机照片列表高度计算 
    public static int getCameraPhotoAreaHeight() {
        return getCameraPhotoWidth() + LCApplication.getApp().dp2px(4);
    }
    
    public static int getCameraPhotoWidth() {
        return LCApplication.getApp().getScreenWidth() / 4 - LCApplication.getApp().dp2px(2);
    }

    //活动标签页grid图片高度
    public static int getActivityHeight() {
        return (LCApplication.getApp().getScreenWidth() - LCApplication.getApp().dp2px(24)) / 3;
    }
}
