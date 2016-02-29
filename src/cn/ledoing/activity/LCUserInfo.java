package cn.ledoing.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cn.ledoing.activity.R.string;
import cn.ledoing.adapter.LCTrainingAidAdapter;
import cn.ledoing.bean.City;
import cn.ledoing.bean.TimAll;
import cn.ledoing.bean.TrainingAidAll;
import cn.ledoing.bean.UserInfo;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.imageloader.ImageLoader;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.BitmapUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

public class LCUserInfo extends LCActivitySupport implements OnClickListener {
    private RelativeLayout lc_usericon,// 用户头像布局
            lc_username,// 用户名
            lc_sex,//性别
            lc_birthday,// 生日
            lc_region,// 地区
            lc_trainingAid,// 教具
            lc_userphone,// 用户手机
            lc_usertime;// 注册日期
    private LCTitleBar lc_userinfo_title;
    private ImageView lc_userinfo_usericon,image;// 头像
    private TextView lc_userinfo_username,// 用户名
            lc_userinfo_sex,// 性别
            lc_userinfo_birthday,// // 生日
            lc_userinfo_region,// 地区
            lc_userinfo_userphone,// 用户手机
            lc_userinfo_usertime,// 注册日期
            lc_userinfo_trainingAid;//教具名称
    private LCNoNetWork lc_userinfo_nonet;
    private AbHttpUtil mAbHttpUtil = null;
    private ScrollView lc_userinfo_content;
    // 图片下载类
    private Button lc_userinfo_submint;
    private RelativeLayout lc_userinfo_chose, lc_userinfo_photo,
            lc_userinfo_creame, lc_userinfo_quit;
    private final int PHOTO = 100;
    private final int TAILOR = 101;
    private final int UpLoad = 102;
    private final int CAMERA = 103;
    private final int INITINFO = 104;
    private final int INITUPdate = 105;
    private final int SEX_RESOUT = 106;
    private File filec;
    private UserInfo userInfo;
    private TrainingAidAll aidsperson;
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    Intent intent = new Intent(LCUserInfo.this, LCUserCity.class);
                    City usercity = (City) msg.obj;
                    intent.putExtra("usercity", usercity);
                    intent.putExtra("provincial", userInfo.getProvincial());
                    intent.putExtra("city", userInfo.getCity());
                    intent.putExtra("district", userInfo.getDistrict());
                    AbDialogUtil.removeDialog(LCUserInfo.this);
                    startActivity(intent);
                    break;
                case 200:
                    Intent intent2 = new Intent(LCUserInfo.this,
                            LCUpdateBrithday.class);
                    TimAll timeall = (TimAll) msg.obj;
                    intent2.putExtra("timeall", timeall);
                    intent2.putExtra("brithday", userInfo.getBirthday());
                    AbDialogUtil.removeDialog(LCUserInfo.this);
                    startActivity(intent2);
                    break;
            }
        }
    };
    private boolean isRefresh = true;
    public static boolean ISAIDS = true;
    public static UserPic userPic;
    private LCSharedPreferencesHelper lcSharedPreferencesLogin;

    public static void setUserPic(UserPic userPic) {
        LCUserInfo.userPic = userPic;
    }

    public interface UserPic {
        void success(String url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_userinfo);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        lc_usericon = (RelativeLayout) findViewById(R.id.lc_usericon);
        lc_username = (RelativeLayout) findViewById(R.id.lc_username);
        lc_sex = (RelativeLayout) findViewById(R.id.lc_sex);
        lc_birthday = (RelativeLayout) findViewById(R.id.lc_birthday);
        lc_region = (RelativeLayout) findViewById(R.id.lc_region);
        lc_trainingAid = (RelativeLayout) findViewById(R.id.lc_trainingAid);
        lc_userphone = (RelativeLayout) findViewById(R.id.lc_userphone);
        lc_usertime = (RelativeLayout) findViewById(R.id.lc_usertime);
        lc_userinfo_title = (LCTitleBar) findViewById(R.id.lc_userinfo_title);
        lc_userinfo_title.setCenterTitle(mString(string.lc_personalInfo));
        lc_userinfo_usericon = (ImageView) findViewById(R.id.lc_userinfo_usericon);
        image= (ImageView) findViewById(R.id.image);
        lc_userinfo_username = (TextView) findViewById(R.id.lc_userinfo_username);
        lc_userinfo_sex = (TextView) findViewById(R.id.lc_userinfo_sex);
        lc_userinfo_birthday = (TextView) findViewById(R.id.lc_userinfo_birthday);
        lc_userinfo_region = (TextView) findViewById(R.id.lc_userinfo_region);
        lc_userinfo_userphone = (TextView) findViewById(R.id.lc_userinfo_userphone);
        lc_userinfo_usertime = (TextView) findViewById(R.id.lc_userinfo_usertime);
        lc_userinfo_trainingAid = (TextView) findViewById(R.id.lc_userinfo_trainingAid);
        lc_userinfo_submint = (Button) findViewById(R.id.lc_userinfo_submint);
        lc_userinfo_content = (ScrollView) findViewById(R.id.lc_userinfo_content);
        lc_userinfo_chose = (RelativeLayout) findViewById(R.id.lc_userinfo_chose);
        lc_userinfo_photo = (RelativeLayout) findViewById(R.id.lc_userinfo_photo);
        lc_userinfo_creame = (RelativeLayout) findViewById(R.id.lc_userinfo_creame);
        lc_userinfo_quit = (RelativeLayout) findViewById(R.id.lc_userinfo_quit);
        lc_userinfo_nonet = (LCNoNetWork) findViewById(R.id.lc_userinfo_nonet);
        lc_userinfo_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });
        lcSharedPreferencesLogin = new LCSharedPreferencesHelper(context,
                "isfrist");
        lc_userinfo_chose.setOnClickListener(this);
        lc_userinfo_photo.setOnClickListener(this);
        lc_userinfo_creame.setOnClickListener(this);
        lc_userinfo_quit.setOnClickListener(this);
        // 图片的下载
        lc_usericon.setOnClickListener(this);
        lc_username.setOnClickListener(this);
        lc_sex.setOnClickListener(this);
        lc_birthday.setOnClickListener(this);
        lc_region.setOnClickListener(this);
        lc_trainingAid.setOnClickListener(this);
        lc_userphone.setOnClickListener(this);
        lc_usertime.setOnClickListener(this);
        lc_userinfo_submint.setOnClickListener(this);
        isNoNet();
        isLoadUserInfo();
        isLoadAdds();
    }


    //判断是否加载用户信息
    private void isLoadUserInfo() {
        if (LCConstant.ISFRIST&&LCConstant.userinfo!=null) {
            netInit();
            LCConstant.ISFRIST = false;
        } else {
            if (!TextUtils.isEmpty(sharedPreferencesHelper.getValue("userid"))) {
                String userid = sharedPreferencesHelper.getValue("userid");
                String username = sharedPreferencesHelper.getValue("username");
                String realname = sharedPreferencesHelper.getValue("realname");
                String birthday = sharedPreferencesHelper.getValue("birthday");
                String mobile = sharedPreferencesHelper.getValue("mobile");
                String sex = sharedPreferencesHelper.getValue("sex");
                String provincial = sharedPreferencesHelper
                        .getValue("provincial");
                String city = sharedPreferencesHelper.getValue("city");
                String district = sharedPreferencesHelper.getValue("district");
                String userpic = lcSharedPreferencesLogin.getValue("userpic");
                String did = lcSharedPreferencesLogin.getValue(LCSharedPreferencesHelper.DISTRICTID);
                String cid = lcSharedPreferencesLogin.getValue(LCSharedPreferencesHelper.CITYID);
                String pid = lcSharedPreferencesLogin.getValue(LCSharedPreferencesHelper.PROVINCIALID);
                L.e("userpic"+userpic);
                String adddate = sharedPreferencesHelper.getValue("adddate");
                userInfo = new UserInfo("0", "", userid, username, realname,
                        birthday, mobile, sex, provincial, city, district,
                        userpic, adddate, cid, did, pid);
                initDate(userpic, realname, birthday, username, adddate,
                        provincial + "/" + city + "/" + district, sex);
            } else {
                netInit();
            }
        }
    }

    private void isNoNet() {
        // TODO Auto-generated method stub
        if (LCUtils.isNetworkAvailable(this)) {
            setNotNetBack();
        } else {
            setNotNet();
        }
    }

    public void setNotNetBack() {
        lc_userinfo_nonet.setVisibility(View.GONE);
        lc_userinfo_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_userinfo_nonet.setVisibility(View.VISIBLE);
        lc_userinfo_content.setVisibility(View.GONE);
    }

    //判断是否加载教具列表
    private void isLoadAdds() {
        if (ISAIDS) {
            netAidspersonget();
        } else {
            String aidsName = sharedPreferencesHelper.getValue("aidsName");
            lc_userinfo_trainingAid.setText(aidsName);
        }
    }


    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        isLoadAdds();
        if (isRefresh&&LCConstant.userinfo!=null) {
            isLoadUserInfo();
        }
        isRefresh = true;
    }

    private void netInit() {
        // TODO Auto-generated method stub
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("userid", LCConstant.userinfo.getUserid());
        params.put("username", LCConstant.userinfo.getUsername());
        params.put("uuid", getOnly());

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_INFO, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {

                        setDate(content);
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        AbDialogUtil.showProgressDialog(context, 0,
                                "加载中...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        setNotNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框
                        AbDialogUtil.removeDialog(context);
                    }


                });
    }

    /**
     *
     * 注销登录
     */
    private void netLogout() {
        // TODO Auto-generated method stub
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", getOnly());
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_LOGOUT, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // 移除进度框
                        AbDialogUtil.removeDialog(LCUserInfo.this);
                        String loadConvert = loadConvert(content);
                        try {
                            JSONObject jo = new JSONObject(loadConvert);
                            String errorCode = jo.optString("errorCode", "1");
                            String errorMessage = jo.optString("errorMessage",
                                    "1");
                            if ("0".equals(errorCode)) {
                                LCConstant.islogin = false;
                                Intent intent = new Intent(context,
                                        LCHomeFragmentHost.class);
                                startActivity(intent);
                                finish();
                                LCConstant.userinfo = null;
                                LCConstant.token = LCConstant.reToken;
                                clearSharePreference();
                            } else {
                                LCUtils.ReLogin(errorCode, context, errorMessage);
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            AbToastUtil.showToast(context, "数据异常");
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        AbDialogUtil.showProgressDialog(LCUserInfo.this, 0,
                                "正在退出...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        // AbToastUtil.showToast(LCUserInfo.this,
                        // error.getMessage());
                        setNotNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {

                        // 移除进度框
                        AbDialogUtil.removeDialog(LCUserInfo.this);
                    }

                    ;

                });
    }

    protected void clearSharePreference() {
        // TODO Auto-generated method stub
        // 用户信息
        sharedPreferencesHelper.moveValue("userid");
        sharedPreferencesHelper.moveValue("username");
        sharedPreferencesHelper.moveValue("realname");
        sharedPreferencesHelper.moveValue("birthday");
        sharedPreferencesHelper.moveValue("mobile");
        sharedPreferencesHelper.moveValue("sex");
        sharedPreferencesHelper.moveValue("provincial");
        sharedPreferencesHelper.moveValue("city");
        sharedPreferencesHelper.moveValue("district");
        sharedPreferencesHelper.moveValue("userpic");
        sharedPreferencesHelper.moveValue("adddate");
        // 登录信息
        lcSharedPreferencesLogin.moveValue("userid");
        lcSharedPreferencesLogin.moveValue("username");
        lcSharedPreferencesLogin.moveValue("userpic");
        lcSharedPreferencesLogin.moveValue("realname");
        lcSharedPreferencesLogin.moveValue("birthday");
        lcSharedPreferencesLogin.moveValue("sex");
        lcSharedPreferencesLogin.moveValue("provincial");
        lcSharedPreferencesLogin.moveValue("city");
        lcSharedPreferencesLogin.moveValue("district");
        lcSharedPreferencesLogin.moveValue("mobile");
        lcSharedPreferencesLogin.moveValue("email");
        lcSharedPreferencesLogin.moveValue("token");
        LCConstant.ISFRIST = true;
    }

    protected void setDate(String content) {
        // TODO Auto-generated method stub
        userInfo = JSONUtils.getInstatce().getUserInfo(content);
        if ("0".equals(userInfo.getErrorCode())) {
            sharedPreferencesHelper.putValue("userid", userInfo.getUserid());
            sharedPreferencesHelper
                    .putValue("username", userInfo.getUsername());
            sharedPreferencesHelper
                    .putValue("realname", userInfo.getRealname());
            sharedPreferencesHelper
                    .putValue("birthday", userInfo.getBirthday());
            sharedPreferencesHelper.putValue("mobile", userInfo.getMobile());
            sharedPreferencesHelper.putValue("sex", userInfo.getSex());
            sharedPreferencesHelper.putValue("provincial",
                    userInfo.getProvincial());
            sharedPreferencesHelper.putValue("city", userInfo.getCity());
            sharedPreferencesHelper
                    .putValue("district", userInfo.getDistrict());
            sharedPreferencesHelper.putValue("userpic", userInfo.getUserpic());

            sharedPreferencesHelper.putValue("adddate", userInfo.getAdddate());
            sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.CITYID, userInfo.getCid());
            sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.DISTRICTID, userInfo.getDid());
            sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.PROVINCIALID, userInfo.getPid());
            // setUserIcon(lc_userinfo_usericon, userInfo.getUserpic());
            if (!TextUtils.isEmpty(userInfo.getUserpic())) {
                LCUtils.mImageloader(userInfo.getUserpic(),
                        lc_userinfo_usericon, context);
            }

            initDate(userInfo.getUserpic(), userInfo.getRealname(),
                    userInfo.getBirthday(), userInfo.getUsername(),
                    userInfo.getAdddate(), userInfo.getProvincial() + "/"
                            + userInfo.getCity() + "/" + userInfo.getDistrict(), userInfo.getSex());
        } else {
//            showToast(userInfo.getErrorMessage());
            LCUtils.ReLogin(userInfo.getErrorCode(), context, userInfo.getErrorMessage());
        }
    }

    public void initDate(String usericon, String realname, String birthday,
                         String username, String adddate, String region, String sex) {
        if (!TextUtils.isEmpty(usericon)) {
//            LCUtils.mImageloader(usericon, lc_userinfo_usericon, context);
            Glide.with(context)
                    .load(usericon)
                    .centerCrop()
//                    .placeholder(R.drawable.image_loading)
//                    .error(R.drawable.image_error)
                    .crossFade()
                    .into(lc_userinfo_usericon);
        }
        setUserName(lc_userinfo_username, realname);
        setBrithday(lc_userinfo_birthday, birthday);
        setRegisterPhone(lc_userinfo_userphone, username);
        setRegisterPhone(lc_userinfo_usertime, adddate);

        if ("1".equals(sex)) {
            lc_userinfo_sex.setText("男");
        } else if("0".equals(sex)){
            lc_userinfo_sex.setText("女");
        }else{
            lc_userinfo_sex.setText("未选择");
        }

        setRegion(lc_userinfo_region, region);
    }

    private void setRegion(TextView lc_userinfo_region, String region) {
        if (!"//".equals(region)) {
            if (!TextUtils.isEmpty(region)) {
                lc_userinfo_region.setText(region);
            } else {
                lc_userinfo_region.setText("未选择");
            }
        } else {
            lc_userinfo_region.setText("未选择");
        }

    }


    private void setRegisterPhone(TextView lc_userinfo_userphone2,
                                  String district) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(district)) {
            lc_userinfo_userphone2.setText(district);
        } else {
            lc_userinfo_userphone2.setText("");
        }
    }

    private void setBrithday(TextView lc_userinfo_birthday2, String birthday) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(birthday)) {
            lc_userinfo_birthday2.setText(birthday);
        } else {
            lc_userinfo_birthday2.setText("未选择");
        }
    }

    private void setUserName(TextView lc_userinfo_username2, String username) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(username)) {
            lc_userinfo_username2.setText(username);
        } else {
            lc_userinfo_username2.setText("未填写");
        }
    }

    private void setUserIcon(ImageView scaleView, String imageUrl) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(imageUrl)) {
            ImageLoader.getInstance(context).DisplayImage(imageUrl, scaleView);
        }
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.lc_usericon://头像
                lc_userinfo_chose.setVisibility(View.VISIBLE);
                break;
            case R.id.lc_username://用户名
                Intent intent = new Intent(this, LCUpdateUseNname.class);
                intent.putExtra("realname", userInfo.getRealname());
                startActivity(intent);
                break;
            case R.id.lc_sex://性别
                Intent intentsex = new Intent(this, LCUpdateSex.class);
                if ( TextUtils.isEmpty(userInfo.getSex()) ) {
                    userInfo.setSex("0");
                }
                intentsex.putExtra("realsex", userInfo.getSex());
                startActivityForResult(intentsex, SEX_RESOUT);
                break;
            case R.id.lc_birthday://生日

                new Thread() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        super.run();
                        AbDialogUtil.showProgressDialog(LCUserInfo.this, 0,
                                "加载中...");
                        TimAll timeall = JSONUtils.getInstatce().getTimeAll();
                        Message obtainMessage = mHandler
                                .obtainMessage(200, timeall);
                        mHandler.sendMessage(obtainMessage);
                    }

                }.start();

                break;
            case R.id.lc_region://地区
                // startActivity(new Intent(this, LCUserCity.class));
                new Thread() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        super.run();
                        AbDialogUtil.showProgressDialog(LCUserInfo.this, 0,
                                "加载中...");
                        String fromAssets = getFromAssets("city.txt");
                        City city2 = JSONUtils.getInstatce().getCity(fromAssets);
                        Message obtainMessage = mHandler.obtainMessage(100, city2);
                        mHandler.sendMessage(obtainMessage);
                    }

                }.start();
                break;
            case R.id.lc_trainingAid://教具
                Intent intent1 = new Intent(this, LCTrainingAid.class);
                intent1.putExtra("aidsperson", aidsperson);
                startActivity(intent1);
                break;
            case R.id.lc_userphone://更换手机号
                Intent intent2 = new Intent(this, LCNowPhone.class);
                intent2.putExtra("phone", lc_userinfo_userphone.getText()
                        .toString());
                startActivity(intent2);
                break;
            case R.id.lc_usertime:
                break;
            case R.id.lc_userinfo_submint://注销登录
                netLogout();
                break;
            case R.id.lc_userinfo_chose://关闭相机和相册的浮层
                lc_userinfo_chose.setVisibility(View.GONE);
                break;
            case R.id.lc_userinfo_photo://相册
                Intent intentp = new Intent(context, SelectPicActivity.class);
                intentp.setType("image/*");
                startActivityForResult(intentp, PHOTO);
                isRefresh = false;
                lc_userinfo_chose.setVisibility(View.GONE);
                break;
            case R.id.lc_userinfo_creame://相相机
                isRefresh = false;
                takePic();
                lc_userinfo_chose.setVisibility(View.GONE);
                break;
            case R.id.lc_userinfo_quit://退出浮层
                lc_userinfo_chose.setVisibility(View.GONE);
                break;
        }
    }

    public String getFromAssets(String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            return Result = "{\"errorCode\":1,\"errorMessage\":[\"错误\"],\"data\":[]}";
        }
    }

    /**
     * 拍照
     */
    private void takePic() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filename = Environment.getExternalStorageDirectory()
                    + "/lccache/" + System.currentTimeMillis() + "c.jpg";
            String path = Environment.getExternalStorageDirectory()
                    + "/lccache/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            filec = new File(filename);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filec));
            List<ResolveInfo> infos = context.getPackageManager()
                    .queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);

            if (infos == null || infos.size() == 0) {
                Toast.makeText(context, "没有找到支持的应用", Toast.LENGTH_LONG).show();
            } else {
                startActivityForResult(intent, CAMERA);
            }
        } else {
            Toast.makeText(context, "sd卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PHOTO:// 相册
                photoResult(requestCode,resultCode,data);
                break;
            case TAILOR://裁剪后
                cropPictureResult(requestCode,resultCode,data);
                break;
            case CAMERA://相机
                cameraResult(requestCode,resultCode,data);
                break;
        }

    }

    private void cameraResult(int requestCode, int resultCode, Intent data) {
        if (filec.exists()) {
            if (filec.length() > 100) {
                Uri uri = Uri.fromFile(filec);
                cropPicture(this, uri);
            } else {
                Toast.makeText(context, "取消", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "取消", Toast.LENGTH_LONG).show();
        }
    }

    private void cropPictureResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            AbToastUtil.showToast(context, "上传失败!");
            return;
        }
        Bitmap bitmap = data.getParcelableExtra("data");
        if (bitmap == null) {
            AbToastUtil.showToast(context, "上传失败!");
            return;
        }
        File temp = new File(Environment.getExternalStorageDirectory()
                .getPath() + "/lccache/");// 自已缓存文件夹
        if (!temp.exists()) {
            temp.mkdir();
        }
        File tempFile = new File(temp.getAbsolutePath() + "/"
                + Calendar.getInstance().getTimeInMillis() + ".jpg"); // 以时间秒为文件名


//                BitmapUtil.saveBitmapToFile();
        // 图像保存到文件中
        FileOutputStream foutput = null;
        try {
            foutput = new FileOutputStream(tempFile);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 70, foutput)) {
                // UploadImage(tempFile);
                foutput.flush();
                foutput.close();
                foutput = null;
                String murl = tempFile.getAbsolutePath();
                UpLoading(murl);
            }

            if (bitmap != null) {
                bitmap.recycle();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block

            AbToastUtil.showToast(context, "上传失败!");
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }

    private void photoResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String stringExtra = data.getStringExtra("path");
            File file = new File(stringExtra);
            Uri uri = Uri.fromFile(file);
            cropPicture(this, uri);
        } else {
            AbToastUtil.showToast(context, "取消");
        }
    }

    //裁剪图片
    public void cropPicture(Activity activity, Uri uri) {
        Intent innerIntent = new Intent("com.android.camera.action.CROP");
        innerIntent.setDataAndType(uri, "image/*");
        innerIntent.putExtra("crop", "true");// 才能出剪辑的小方框，不然没有剪辑功能，只能选取图片
        innerIntent.putExtra("aspectX", 1); // 放大缩小比例的X
        innerIntent.putExtra("aspectY", 1);// 放大缩小比例的X 这里的比例为： 1:1
        innerIntent.putExtra("outputX", 320); // 这个是限制输出图片大小
        innerIntent.putExtra("outputY", 320);
        innerIntent.putExtra("return-data", true);
        innerIntent.putExtra("scale", true);
        startActivityForResult(innerIntent, TAILOR);
    }

    private DialogFragment mAlertDialog = null;

    // 最大100
    private int max = 100;
    private int progress = 0;
    private TextView numberText, maxText;

    // 文件上传
    public void UpLoading(String path) {

        // 已经在后台上传
        if (mAlertDialog != null) {
            mAlertDialog.show(getFragmentManager(), "dialog");
            return;
        }
        AbRequestParams params = new AbRequestParams();
        try {
            // 参数随便加，在sd卡根目录放图片
            File file1 = new File(path);
            params.put("userpic", file1);
            params.put("uuid", LCUtils.getOnly(getContext()));
        } catch (Exception e) {
            showToast("文件不存在！");
        }

        mAbHttpUtil.post(LCConstant.URL +LCConstant.URL_API+ LCConstant.USER_UPLOAD_AVATAR, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onSuccess(int statusCode, String content) {


                        upLoadPic(content);
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 打开进度框
                        // View v = LayoutInflater.from(context).inflate(
                        // R.lc_update_sex_layout.progress_bar_horizontal, null, false);
                        // mAbProgressBar = (AbHorizontalProgressBar) v
                        // .findViewById(R.id.horizontalProgressBar);
                        // numberText = (TextView)
                        // v.findViewById(R.id.numberText);
                        // maxText = (TextView) v.findViewById(R.id.maxText);
                        // maxText.setText(progress + "/" +
                        // String.valueOf(max));
                        // mAbProgressBar.setMax(max);
                        // mAbProgressBar.setProgress(progress);
                        // mAlertDialog = AbDialogUtil.showAlertDialog("正在上传",
                        // v);

                    }

                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        // AbToastUtil.showToast(context, error.getMessage());
                        L.e("毛线："+statusCode+"/"+content);
                        setNotNet();
                    }

                    // 进度
                    @Override
                    public void onProgress(int bytesWritten, int totalSize) {
                        // maxText.setText(bytesWritten / (totalSize / max) +
                        // "/"
                        // + max);
                        // mAbProgressBar.setProgress(bytesWritten
                        // / (totalSize / max));
                    }

                    // 完成后调用，失败，成功，都要调用
                    public void onFinish() {
                        // Log.d(TAG, "onFinish");
                        // 下载完成取消进度框
                        if (mAlertDialog != null) {
                            mAlertDialog.dismiss();
                            mAlertDialog = null;
                        }
                        netInit();
                    }



                });
    }

    private void upLoadPic(String content) {
        String loadConvert = loadConvert(content);
            L.e(loadConvert);
        try {
            JSONObject jo = new JSONObject(loadConvert);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage",
                    "1");
            if ("0".equals(errorCode)) {
                JSONObject jsonObject = jo
                        .getJSONObject("data");
                String userpic = jsonObject
                        .optString("userpic");
                Glide.with(context)
                        .load(userpic)
                        .centerCrop()
                        .crossFade()
                        .into(lc_userinfo_usericon);
                sharedPreferencesHelper.putValue("userpic",
                        userpic);
                lcSharedPreferencesLogin.putValue("userpic",
                        userpic);
//                String  ss=sharedPreferencesHelper.getValue("userpic");
                if(LCConstant.userinfo!=null){
                    LCConstant.userinfo.setUserpic(userpic);
                }
                if (userPic != null) {
                    userPic.success(userpic);
                }
                AbToastUtil.showToast(context, "上传成功！");
                // finish();
            } else {
//                                AbToastUtil.showToast(context, errorMessage);
                LCUtils.ReLogin(errorCode, context, errorMessage);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            AbToastUtil.showToast(context, "上传失败！");
        }

    }


    private void netAidspersonget() {
        // TODO Auto-generated method stub
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", getOnly());
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_AIDS_LIST, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                    loadAids(content);
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
//                        AbDialogUtil.showProgressDialog(context, 0, "正在查询...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
//                        AbToastUtil.showToast(context, error.getMessage());
                        isNoNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框

                    }
                });
    }

    private void loadAids(String content) {
        aidsperson = JSONUtils.getInstatce()
                .getAidsperson(content);
        if ("0".equals(aidsperson.getErrorCode())) {
            if (aidsperson.getList().size() > 0) {
                String aidsName = getAidsName(aidsperson);
                lc_userinfo_trainingAid.setText(aidsName);
                sharedPreferencesHelper.putValue("aidsName", aidsName);
            } else {
                lc_userinfo_trainingAid.setText("未选择");
            }
            ISAIDS = false;
        } else {
            lc_userinfo_trainingAid.setText("未选择");
        }
    }

    private String getAidsName(TrainingAidAll aidsperson) {
        String aidsName = "未选择";
        for (int i = 0; i < aidsperson.getList().size(); i++) {
            if ("1".equals(aidsperson.getList().get(i).getSelected())) {
                aidsName = aidsperson.getList().get(i).getAidsname();
                break;
            }
        }
        return aidsName;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(lc_userinfo_usericon);
        releaseImageView(image);
    }
}
