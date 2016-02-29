package cn.ledoing.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ledoing.activity.LCHomeFragmentHost;
import cn.ledoing.activity.LCUserLoginAndRegister;
import cn.ledoing.activity.R;
import cn.ledoing.global.LCApplication;
import cn.ledoing.global.LCConstant;
import cn.ledoing.view.LCCustomProgressDialog;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;


public class LCUtils {
    private static LCCustomProgressDialog pd;

    public static void startProgressDialog(Context context) {
        // 显示进度框
        DialogUtil.startDialogLoading(context);
    }
    public static void startProgressDialog(Context context,String content) {
        // 显示进度框
        DialogUtil.startDialogLoading(context);
    }
    public static void stopProgressDialog(Context context) {

        DialogUtil.stopDialogLoading(context);
    }

    public static String getOnly(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) LCApplication.context
                .getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) LCApplication.context
                .getSystemService(Context.WIFI_SERVICE);
        LCSharedPreferencesHelper lc = new LCSharedPreferencesHelper(LCApplication.context,
                "1");
        WifiInfo info = wifi.getConnectionInfo();
        String IMEI = 0 + "";
        try {

            IMEI = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(IMEI)) {
                IMEI = info.getMacAddress();
            }
        } catch (Exception e) {
            if (TextUtils.isEmpty(lc.getValue("uuid"))) {
                lc.putValue("uuid", getUUID());
            } else {
                IMEI = lc.getValue("uuid");
            }
        }
        return IMEI.toLowerCase();
    }

    public static String getUUID() {

        String uuidStr = UUID.randomUUID().toString();
        uuidStr = uuidStr.substring(0, 8) + uuidStr.substring(9, 13)
                + uuidStr.substring(14, 18) + uuidStr.substring(19, 23)
                + uuidStr.substring(24);

        return uuidStr;
    }

    // 时间戳
    public static String gettime() {

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式

        String hehe = dateFormat.format(now);
        Date date;
        try {
            date = dateFormat.parse(hehe);
            return date.getTime() + "";
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            return System.currentTimeMillis() + "";
        }

    }

    public static void mImageloader(String url, final ImageView image,
                                    Context context) {
        // TODO Auto-generated method stub

        Glide.with(context)
                .load(url)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                .placeholder(R.drawable.image_loading)
//                .error(R.drawable.image_error)
                .crossFade()
                .into(image);
    }

    public static void ReLogin(String code, Context context,String errormsg) {
        if (TextUtils.isEmpty(code))
            return;

                if(20004==Integer.parseInt(code)){
                LCConstant.islogin = false;
                Intent intent = new Intent(context, LCUserLoginAndRegister.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                LCConstant.token = LCConstant.reToken;
                AbToastUtil.showToast(context,errormsg);
                }else{
                    AbToastUtil.showToast(context,errormsg);
                }

    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    System.out.println(i + "===状态==="
                            + networkInfo[i].getState());
                    System.out.println(i + "===类型==="
                            + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    // 版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
    public static void setTitle(TextView lc_class_title, String videoclassname) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(videoclassname)) {
            lc_class_title.setText(videoclassname);
        } else {
            lc_class_title.setText("");
        }
    }
}
