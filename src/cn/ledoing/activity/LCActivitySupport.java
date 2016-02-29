package cn.ledoing.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cn.ledoing.activity.R.string;
import cn.ledoing.global.LCApplication;
import cn.ledoing.global.LCConstant;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.view.LCCustomProgressDialog;
import cn.ledoing.view.LCDialog;

/**
 * Actity  wptddf
 *
 * @author wpt
 */
@SuppressLint("NewApi")
public class LCActivitySupport extends FragmentActivity implements
        LCIActivitySupport {

    public static String categoryid = "";//从首页点击预约暂时保存的id 和 name 需要在中心列表及后面的页面显示
    public static String categoryname = "";
    protected Context context = null;
    protected LCApplication jxbApplication;
    protected ProgressDialog pg = null;
    private String TAG = "ActivitySupport";
    protected LCSharedPreferencesHelper sharedPreferencesHelper = null;
    protected LCSharedPreferencesHelper lcSharedPreferencesLogin;
    private LCCustomProgressDialog pd;
    View dialogView;
    public LCDialog dialog;
    private Toast mToast;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeInput();
    }


    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        context = this;
        pg = new ProgressDialog(context);
        jxbApplication = (LCApplication) getApplication();
        jxbApplication.addActivity(this);
        sharedPreferencesHelper = new LCSharedPreferencesHelper(context,
                "userinfo");
        lcSharedPreferencesLogin = new LCSharedPreferencesHelper(context,
                "isfrist");
        dialogView = LayoutInflater.from(context).inflate(R.layout.lc_dialog,
                null);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        // FT = true;
        // L.e(TAG, "onResume");
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();

    }

    @Override
    public ProgressDialog getProgressDialog() {
        return pg;
    }

    /**
     * 描述：�??出提�?
     *
     * @param
     */
    @Override
    public void isExit() {

        jxbApplication.exit();

    }

    @Override
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
        return false;
    }

    @Override
    public boolean hasLocationNetWork() {
        LocationManager manager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void checkMemoryCard() {
        //
        // new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
        // .setTitle("警告")
        // .setMessage("请检查内存卡")
        // .setPositiveButton("设置",
        // new DialogInterface.OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog,
        // int which) {
        // dialog.cancel();
        // Intent intent = new Intent(
        // Settings.ACTION_SETTINGS);
        // context.startActivity(intent);
        // }
        // })
        // .setNegativeButton("�?�?",
        // new DialogInterface.OnClickListener() {
        // @Override
        // public void onClick(DialogInterface dialog,
        // int which) {
        // dialog.cancel();
        // jxbApplication.exit();
        // }
        // }).create().show();

        // }
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            showDialog(new OnClickListener() {

                           @Override
                           public void onClick(View v) {
                               // TODO Auto-generated method stub
                               dialog.cancel();
                               jxbApplication.exit();
                           }
                       }, new OnClickListener() {

                           @Override
                           public void onClick(View v) {
                               // TODO Auto-generated method stub
                               Intent intent = new Intent(Settings.ACTION_SETTINGS);
                               context.startActivity(intent);
                               dialog.cancel();
                           }
                       }, mString(string.check_sd), mString(string.cancel),
                    mString(string.checking));
        }
    }

    public void openWirelessSet() {
        showDialog(new OnClickListener() {

                       @Override
                       public void onClick(View v) {
                           // TODO Auto-generated method stub
                           dialog.cancel();
                           jxbApplication.exit();
                           Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                           context.startActivity(intent);
                           dialog.cancel();
                       }
                   }, new OnClickListener() {

                       @Override
                       public void onClick(View v) {
                           // TODO Auto-generated method stub
                           // Intent intent = new
                           // Intent(Settings.ACTION_WIRELESS_SETTINGS);
                           // context.startActivity(intent);
                           // dialog.cancel();
                       }
                   }, mString(string.check_network), mString(string.checking),
                mString(string.checking));

    }

    /**
     * 显示toast
     *
     * @param text
     * @param longint
     * @author wpt
     * @update 2012-6-28 下午3:46:18
     */
    public void showToast(String text, int longint) {
        Toast.makeText(context, text, longint).show();
    }

    @Override
    public void showToast(String text) {
        if(mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public LCDialog showDialog(OnClickListener cancel, OnClickListener affim,
                               String msg, String textcancel, String textaffim) {
        dialogView = LayoutInflater.from(context).inflate(R.layout.lc_dialog,
                null);
        dialog = new LCDialog(LCActivitySupport.this, R.style.MyDialog, dialogView);
        dialog.show();
        TextView tv = (TextView) dialogView.findViewById(R.id.jxb_dialog_title);
        tv.setText(msg);
        Button dialog_button_cancel = (Button) dialogView
                .findViewById(R.id.dialog_button_cancel);
        dialog_button_cancel.setText(textcancel);
        Button dialog_button_affim = (Button) dialogView
                .findViewById(R.id.dialog_button_affim);
        dialog_button_affim.setVisibility(View.GONE);
        dialog_button_affim.setText(textaffim);

        if (affim == null) {
            dialog_button_affim.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

        } else {
            dialog_button_affim.setOnClickListener(affim);
        }

        if (cancel == null) {
            dialog_button_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        } else {
            dialog_button_cancel.setOnClickListener(cancel);

        }
        return dialog;
    }

    public LCDialog showDialogTwoButton(OnClickListener cancel, OnClickListener affim,
                                        String msg, String textcancel, String textaffim) {
        dialogView = LayoutInflater.from(context).inflate(R.layout.lc_dialog,
                null);
        dialog = new LCDialog(context, R.style.MyDialog, dialogView);
        dialog.show();
        TextView tv = (TextView) dialogView.findViewById(R.id.jxb_dialog_title);
        tv.setText(msg);
        Button dialog_button_cancel = (Button) dialogView
                .findViewById(R.id.dialog_button_cancel);
        dialog_button_cancel.setText(textcancel);
        Button dialog_button_affim = (Button) dialogView
                .findViewById(R.id.dialog_button_affim);

        dialog_button_affim.setText(textaffim);

        if (affim == null) {
            dialog_button_affim.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });

        } else {
            dialog_button_affim.setOnClickListener(affim);
        }

        if (cancel == null) {
            dialog_button_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        } else {
            dialog_button_cancel.setOnClickListener(cancel);


        }
        return dialog;
    }

    public LCDialog showDialog(String msg, String bottonMsg) {
        dialogView = LayoutInflater.from(context).inflate(R.layout.lc_dialog,
                null);
        dialog = new LCDialog(context, R.style.MyDialog, dialogView);
        dialog.show();
        TextView tv = (TextView) dialogView.findViewById(R.id.jxb_dialog_title);
        tv.setText(msg);
        Button dialog_button_affim = (Button) dialogView
                .findViewById(R.id.dialog_button_affim);
        dialog_button_affim.setVisibility(View.GONE);
        Button dialog_button_cancel = (Button) dialogView
                .findViewById(R.id.dialog_button_cancel);
        dialog_button_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        return dialog;
    }

    public LCDialog showDialog(View view) {

        final LCDialog dialog = new LCDialog(context, R.style.MyDialog, view);
        dialog.show();
        return dialog;
    }

    /**
     * 描述：Toast提示文本.
     *
     * @param resId 文本的资源ID
     */
    public void showToast(int resId) {
        Toast.makeText(this, "" + this.getResources().getText(resId),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 关闭键盘事件
     *
     * @author wpt
     * @update 2012-7-4 下午2:34:34
     */
    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public LCApplication getJXBApplication() {
        return jxbApplication;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.jxb.activity.IActivitySupport#mString(int)
     */
    @Override
    public String mString(int string) {
        // TODO Auto-generated method stub
        return getResources().getString(string);
    }

    public void startProgressDialog() {
        if (pd == null) {
            pd = LCCustomProgressDialog.createDialog(this);
            // pd.setMessage("正在加载�?...");
        }

        pd.show();
    }

    public void stopProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    public LCDialog showDialogIsLoading(OnClickListener cancel,
                                        OnClickListener affim, String msg, String textcancel,
                                        String textaffim) {
        View dialogLoading = LayoutInflater.from(context).inflate(
                R.layout.lc_download_dialog, null);
        final LCDialog dialogLoad = new LCDialog(context, R.style.MyDialog,
                dialogLoading);
        dialogLoad.show();
        TextView tv = (TextView) dialogLoading
                .findViewById(R.id.jxb_dialog_title);
        tv.setText(msg);
        Button dialog_button_cancel = (Button) dialogLoading
                .findViewById(R.id.dialog_button_cancel);
        dialog_button_cancel.setText(textcancel);
        Button dialog_button_affim = (Button) dialogLoading
                .findViewById(R.id.dialog_button_affim);
        dialog_button_affim.setText(textaffim);
        if (affim == null) {
            dialog_button_affim.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialogLoad.cancel();
                }
            });
        } else {
            dialog_button_affim.setOnClickListener(affim);
        }
        if (cancel == null) {
            dialog_button_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialogLoad.cancel();
                }
            });
        } else {
            dialog_button_cancel.setOnClickListener(cancel);
        }
        return dialogLoad;
    }

    // 转换unicode
    public String loadConvert(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    private static String key = "a6U&1$Ip[Jr/sed]Rfvn=O>Mz+}lXN*%-gLcGD|0";

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    // SHA1 加密实例
    public static String encryptToSHA(String info) {
        byte[] digesta = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将摘要转为字符串a
        String rs = byte2hex(digesta);
        return rs + key;
    }

    // 获取唯一标示
    public String getOnly() {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        LCSharedPreferencesHelper lc = new LCSharedPreferencesHelper(context,
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

    public String getUUID() {

        String uuidStr = UUID.randomUUID().toString();
        uuidStr = uuidStr.substring(0, 8) + uuidStr.substring(9, 13)
                + uuidStr.substring(14, 18) + uuidStr.substring(19, 23)
                + uuidStr.substring(24);

        return uuidStr;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    // 时间戳
    public String gettime() {

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

    public void releaseImageView(ImageView imageView) {
        Drawable d = imageView.getDrawable();
        if (d != null)
            d.setCallback(null);
        imageView.setImageDrawable(null);
        imageView.setBackgroundDrawable(null);
    }

    public void releaseRelativeLayout(RelativeLayout imageView) {
        Drawable d = imageView.getBackground();
        if (d != null)
            d.setCallback(null);
        imageView.setBackgroundDrawable(null);
    }
}
