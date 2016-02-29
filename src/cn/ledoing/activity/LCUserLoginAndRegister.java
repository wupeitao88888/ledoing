package cn.ledoing.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import cn.ledoing.bean.LCLogin;
import cn.ledoing.bean.TimAll;
import cn.ledoing.global.AbAppConfig;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbFileHttpResponseListener;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AutoInstall;
import cn.ledoing.utils.DialogUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCDialog;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCSlideShowView;
import cn.ledoing.view.LCTitleBar;
import cn.ledoing.view.LCViewPageSelectListener;
import de.greenrobot.event.EventBus;

public class LCUserLoginAndRegister extends LCActivitySupport implements
        LCViewPageSelectListener, OnClickListener {
    private LCSlideShowView lc_slideshowview_loginAndRegister;// 标签选项卡
    private Button lc_userlogin, lc_userregister;
    private LCTitleBar lc_loginAndRegister_title;
    private AbHttpUtil mAbHttpUtil = null;
    LCDialog showDialog;
    /**
     * 注册界面
     */
    private EditText lc_phone_number,// 手机号码
            lc_phone_checknumber,// 验证码
            lc_phone_pw,// 密码
            lc_phone_repw;// 确认密码
    private Button // 获取验证码
            lc_phone_submit;// 提交密码
    private TextView lc_phone_getchecknumber;

    /**
     * 登陆界面
     */
    private Button lc_login_submint;// 提交用户名密码
    private EditText lc_login_number,// 手机号码
            lc_login_pw// 验证码
                    ;
    private RelativeLayout lc_login_forget;
    private LCSharedPreferencesHelper lcSharedPreferencesHelper;
    private LCNoNetWork lc_slideshowview_nonet;
    private RelativeLayout lc_loginAndRegister_content, lc_login_dialog;
    private ImageView login_bg;
    private TextView lc_login_downtext;
    private int M_INTENT;

    public static LoginCallBack loginCallBack;
    public static UserInfoCallBack userInfoCallBack;
    private String ins_id;
    private String teacher_id;
    private String teacher_name;

    public static void setLoginCallBack(LoginCallBack loginCallBack) {
        LCUserLoginAndRegister.loginCallBack = loginCallBack;
    }

    public static void setUserInfoCallBack(UserInfoCallBack userInfoCallBack) {
        LCUserLoginAndRegister.userInfoCallBack = userInfoCallBack;
    }

    public interface LoginCallBack {
        void onSuccess();
    }


    public interface UserInfoCallBack {
        void onSuccess();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        initView();
    }

    @SuppressLint("InlinedApi")
    private void initView() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        M_INTENT = intent.getIntExtra("mIntent", 6);
        ins_id = intent.getStringExtra("ins_id");
        teacher_id = intent.getStringExtra("teacher_id");
        teacher_name = intent.getStringExtra("teacher_name");
        setContentView(R.layout.acticity_lc_userloginandregister);
        lcSharedPreferencesHelper = new LCSharedPreferencesHelper(context,
                "isfrist");
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        LayoutInflater from = LayoutInflater.from(context);

        lc_loginAndRegister_title = (LCTitleBar) findViewById(R.id.lc_loginAndRegister_title);
        lc_loginAndRegister_content = (RelativeLayout) findViewById(R.id.lc_loginAndRegister_content);
        lc_login_dialog = (RelativeLayout) findViewById(R.id.lc_login_dialog);
        login_bg = (ImageView) findViewById(R.id.login_bg);
        lc_login_downtext = (TextView) findViewById(R.id.lc_login_downtext);
        lc_slideshowview_nonet = (LCNoNetWork) findViewById(R.id.lc_slideshowview_nonet);
        lc_slideshowview_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });
        lc_loginAndRegister_title.isCenterVisibility(false);
        lc_loginAndRegister_title.setOnclickBackListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cloaseInput();
                finish();
            }
        });
        View lc_layout_userlogin = from.inflate(R.layout.lc_layout_userlogin,
                null);
        View lc_layout_userregister = from.inflate(
                R.layout.lc_layout_userregister, null);
        initUserRegister(lc_layout_userregister);
        initUserLogin(lc_layout_userlogin);
        lc_slideshowview_loginAndRegister = (LCSlideShowView) findViewById(R.id.lc_slideshowview_loginAndRegister);
        lc_userlogin = (Button) findViewById(R.id.lc_userlogin);
        lc_userregister = (Button) findViewById(R.id.lc_userregister);
        lc_userlogin.setBackgroundResource(R.drawable.select_title);
        lc_userlogin.setTextColor(this.getResources().getColor(
                R.color.selet_title));
        lc_userregister.setBackgroundResource(R.drawable.title);
        lc_userregister.setTextColor(this.getResources().getColor(
                R.color.unselect));
        List<View> imageUris = new ArrayList<View>();
        imageUris.add(lc_layout_userlogin);
        imageUris.add(lc_layout_userregister);
        lc_slideshowview_loginAndRegister.setImageUris(imageUris);
        lc_userregister.setOnClickListener(this);
        lc_userlogin.setOnClickListener(this);
        lc_slideshowview_loginAndRegister.setJXBViewPageSelectListener(this);
        isNoNet();
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
        lc_slideshowview_nonet.setVisibility(View.GONE);
        lc_loginAndRegister_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_slideshowview_nonet.setVisibility(View.VISIBLE);
        lc_loginAndRegister_content.setVisibility(View.GONE);
    }

    private void initUserLogin(View v) {
        // TODO Auto-generated method stub
        lc_login_submint = (Button) v.findViewById(R.id.lc_login_submint);
        lc_login_number = (EditText) v.findViewById(R.id.lc_login_number);
        lc_login_pw = (EditText) v.findViewById(R.id.lc_login_pw);
        lc_login_forget = (RelativeLayout) v.findViewById(R.id.lc_login_forget);
        lc_login_submint.setOnClickListener(this);
        lc_login_forget.setOnClickListener(this);
    }

    protected void cloaseInput() {
        // TODO Auto-generated method stub
        /** 隐藏软键盘 **/
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void netLogin(final String lc_login_number, String lc_login_pw) {
        // TODO Auto-generated method stub
        LCConstant.islogin = false;
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("username", lc_login_number);
        params.put("password", lc_login_pw);
        params.put("uuid", getOnly());
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_LOGIN, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("=======4.4====statusCode==" + statusCode + "================" + loadConvert(content));
                        setLogon(content, lc_login_number);
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        DialogUtil.startDialogLoading(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.4====statusCode==" + statusCode + "================" + content);
                        // AbToastUtil.showToast(context, error.getMessage());
                        setNotNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {

                        // 移除进度框
//                        AbDialogUtil.removeDialog(context);

                    }

                    ;

                });
    }

    protected void setLogon(String content, String lc_login_number) {
        // TODO Auto-generated method stub
        LCConstant.userinfo = JSONUtils.getInstatce().getLogin(content,
                lc_login_number);
        if ("0".equals(LCConstant.userinfo.getErrorCode())) {
//            startActivity(new Intent(this, LCHomeFragmentHost.class));
            LCConstant.token = LCConstant.userinfo.getToken();
            LCConstant.islogin = true;
            getNewVestion();
        } else {
            showDialog(LCConstant.userinfo.getErrorMessage(), "取消");
            LCConstant.userinfo = null;
            LCConstant.islogin = false;
            DialogUtil.stopDialogLoading(context);
        }

    }

    private void mIntent(int m_intent) {
        if (loginCallBack != null) {
            loginCallBack.onSuccess();
        }
        if (userInfoCallBack != null) {
            userInfoCallBack.onSuccess();
        }
        switch (m_intent) {
            case 0://用户信息
                Intent intent = new Intent(context, LCUserInfo.class);
                startActivity(intent);
                break;
            case 1://上课记录
                startActivity(new Intent(context,
                        LCMeCourse.class));
                break;
            case 2://学习记录
                startActivity(new Intent(context,
                        LCStudyRecord.class));
                break;
            case 3://关于我们
                startActivity(new Intent(context, LCAboutMe.class));
                break;
            case 4://我的二维码
                startActivity(new Intent(context, LCMeCode.class));
                break;
            case 5://我的乐豆
                startActivity(new Intent(context, LCMeLcBean.class));
                break;
            case 6:
                break;
            //预约课程
            case 7:
                break;
            //播放页
            case 8:
                EventBus.getDefault().post(AbAppConfig.CENTER_FRAGMENT_FINISH);
                break;
            // 家 中心点评
            case 9:
                Intent comment_intent = new Intent(context, CommentActivity.class);
                comment_intent.putExtra("ins_id", ins_id);
                startActivity(comment_intent);
                break;
            // 家 中心约课
            case 10:
                Intent detail_intent = new Intent(context, LCActivityDetailsListActivity.class);
                detail_intent.putExtra("institution_id", ins_id);
                startActivity(detail_intent);
                break;
            // 家 老师约课
            case 11:
                Intent about_intent = new Intent(context, LCActivityDetailsListActivity.class);
                about_intent.putExtra("institution_id", ins_id);
                about_intent.putExtra("teacher_id", teacher_id);
                about_intent.putExtra("teacher_name", teacher_name);
                startActivity(about_intent);
                break;
        }

    }

    @Override
    public void succeedCallBack(int p) {
        // TODO Auto-generated method stub
        switch (p) {
            case 0:
                lc_userlogin.setTextColor(this.getResources().getColor(
                        R.color.selet_title));
                lc_userlogin.setBackgroundResource(R.drawable.select_title);

                lc_userregister.setTextColor(this.getResources().getColor(
                        R.color.unselect));
                lc_userregister.setBackgroundResource(R.drawable.title);
                break;
            case 1:
                lc_userlogin.setBackgroundResource(R.drawable.title);
                lc_userlogin.setTextColor(this.getResources().getColor(
                        R.color.unselect));
                lc_userregister.setBackgroundResource(R.drawable.select_title);
                lc_userregister.setTextColor(this.getResources().getColor(
                        R.color.selet_title));
                break;
        }
    }

    @Override
    public void succeedEndCallBack(int p) {

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.lc_userlogin:
                lc_slideshowview_loginAndRegister.setCurrentItemMy(0);
                break;
            case R.id.lc_userregister:
                lc_slideshowview_loginAndRegister.setCurrentItemMy(1);
                break;
            case R.id.lc_phone_getchecknumber:
                String register_number = lc_phone_number.getText().toString();
                if (!TextUtils.isEmpty(register_number)) {
                    if (register_number.length() == 11) {
                        lc_phone_getchecknumber.setEnabled(false);
                        if (isMobileNO(register_number)) {

                            netCode(register_number);
                            mTime();
                        } else {
                            showToast("手机号码格式不正确！");
                        }
                    } else {
                        showToast("手机号码格式不正确！");
                    }
                } else {
                    showToast("手机号码不能为空！");
                }
                break;
            case R.id.lc_phone_submit:
                cloaseInput();
                register();
                break;
            case R.id.lc_login_submint:
                cloaseInput();
                String login_number = lc_login_number.getText().toString();
                String login_pw = lc_login_pw.getText().toString();
                if (!TextUtils.isEmpty(login_number)) {
                    if (!TextUtils.isEmpty(login_pw)) {
                        netLogin(login_number, login_pw);
                    } else {
                        showToast("密码不能为空");
                    }
                } else {
                    showToast("用户名不能为空");
                }
                break;
            case R.id.lc_login_forget:
                startActivity(new Intent(this, LCResetPW.class));
                break;
        }
    }

    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int s = (Integer) msg.obj;
                    if (s > 0) {
                        lc_phone_getchecknumber.setText(s + "秒");
                    } else {
                        lc_phone_getchecknumber.setText("获取验证码");
                        timer.cancel();
                        timer = null;
                        lc_phone_getchecknumber.setEnabled(true);
                    }
                    break;
            }
        }

    };
    Timer timer;

    public void mTime() {
        TimerTask task = new TimerTask() {
            int i = 30;

            public void run() {
                i--;
                Message message = new Message();
                message.what = 1;
                message.obj = i;
                handler.sendMessage(message);
            }
        };

        timer = new Timer(true);
        timer.schedule(task, 1000, 1000); // 延时1000ms后执行，1000ms执行一次
    }

    //发送验证码
    private void netCode(String phone) {
        // TODO Auto-generated method stub
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("username", phone);
        params.put("uuid", getOnly());
        params.put("action", LCConstant.VERIFY_TYPE_REGISTER + "");

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_CODE, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // 移除进度框
                        DialogUtil.stopDialogLoading(context);
                        if (!TextUtils.isEmpty(content)) {
                            String loadConvert = loadConvert(content);
                            try {
                                JSONObject jo = new JSONObject(loadConvert);
                                String errorCode = jo.optString("errorCode",
                                        "1");
                                String errorMessage = jo.optString(
                                        "errorMessage", "0");
                                if ("0".equals(errorCode)) {
                                    showToast("发送成功");

                                } else {
                                    showToast(errorMessage);
                                    LCUtils.ReLogin(errorCode, context, errorMessage);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                showToast("数据异常");
                            }
                        } else {
                            showToast("数据为空");
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        DialogUtil.startDialogLoading(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        // AbToastUtil.showToast(context, error.getMessage());
                        setNotNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {

                        // 移除进度框
                        DialogUtil.stopDialogLoading(context);
                    }

                    ;

                });
    }

    //初始话祖册的view
    private void initUserRegister(View v) {
        // TODO Auto-generated method stub
        lc_phone_getchecknumber = (TextView) v
                .findViewById(R.id.lc_phone_getchecknumber);
        lc_phone_submit = (Button) v.findViewById(R.id.lc_phone_submit);
        lc_phone_number = (EditText) v.findViewById(R.id.lc_phone_number);
        lc_phone_checknumber = (EditText) v
                .findViewById(R.id.lc_phone_checknumber);
        lc_phone_pw = (EditText) v.findViewById(R.id.lc_phone_pw);
        lc_phone_repw = (EditText) v.findViewById(R.id.lc_phone_repw);
        lc_phone_getchecknumber.setOnClickListener(this);
        lc_phone_submit.setOnClickListener(this);

    }

    //验证
    private void register() {
        // TODO Auto-generated method stub
        String phone_number = lc_phone_number.getText().toString();
        String phone_checknumber = lc_phone_checknumber.getText().toString();
        String phone_pw = lc_phone_pw.getText().toString();
        String phone_repw = lc_phone_repw.getText().toString();
        if (!TextUtils.isEmpty(phone_number)) {
            if (!TextUtils.isEmpty(phone_checknumber)) {
                if (!TextUtils.isEmpty(phone_pw)) {
                    if (!TextUtils.isEmpty(phone_repw)) {
                        if (phone_pw.equals(phone_repw)) {
                            if (phone_pw.length() >= 6 & phone_pw.length() <= 16) {

                                netRegister(phone_number, phone_checknumber,
                                        phone_pw);
                            } else {
                                showToast("请输入6到16位密码");
                            }
                        } else {
                            showToast("密码不一致");
                        }
                    } else {
                        showToast("重复密码不能为空");
                    }
                } else {
                    showToast("密码不能为空");
                }
            } else {
                showToast("验证不能为空");
            }
        } else {
            showToast("用户名不能为空");
        }
    }

    //注册
    private void netRegister(final String phone_number,
                             String phone_checknumber, String phone_pw) {


        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", getOnly());
        params.put("username", phone_number);
        params.put("password", phone_pw);
        params.put("code", phone_checknumber);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_REGISTER, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e(".." + content);
                        setRegister(content, phone_number);
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        DialogUtil.startDialogLoading(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        // AbToastUtil.showToast(context, error.getMessage());
                        L.e("=======4.4====statusCode==" + statusCode + "================" + content);
                        setNotNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {

                        // 移除进度框
                        DialogUtil.stopDialogLoading(context);
                    }

                    ;

                });
    }

    protected void setRegister(String content, String lc_login_number) {
        // TODO Auto-generated method stub
        LCConstant.userinfo = JSONUtils.getInstatce().getLogin(content,
                lc_login_number);
        if ("0".equals(LCConstant.userinfo.getErrorCode())) {
//            startActivity(new Intent(this, LCHomeFragmentHost.class));
            LCConstant.islogin = true;
            LCConstant.token = LCConstant.userinfo.getToken();
            savaLogin(LCConstant.userinfo);
        } else {
//            showDialog(LCConstant.userinfo.getErrorMessage(), "取消");
            LCUtils.ReLogin(LCConstant.userinfo.getErrorCode(), context, LCConstant.userinfo.getErrorMessage());
            LCConstant.userinfo = null;
            LCConstant.islogin = false;
        }
    }

    private void savaLogin(LCLogin userinfo) {
        // TODO Auto-generated method stub
        lcSharedPreferencesHelper.putValue("userid", userinfo.getUserid());
        lcSharedPreferencesHelper.putValue("username", userinfo.getUsername());
        lcSharedPreferencesHelper.putValue("userpic", userinfo.getUserpic());
        lcSharedPreferencesHelper.putValue("realname", userinfo.getRealname());
        lcSharedPreferencesHelper.putValue("birthday", userinfo.getBirthday());
        lcSharedPreferencesHelper.putValue("sex", userinfo.getSex());
        lcSharedPreferencesHelper.putValue("provincial",
                userinfo.getProvincial().getName());
        lcSharedPreferencesHelper.putValue("city", userinfo.getCity().getName());
        lcSharedPreferencesHelper.putValue("district", userinfo.getDistrict().getName());
        lcSharedPreferencesHelper.putValue("mobile", userinfo.getMobile());
        lcSharedPreferencesHelper.putValue("email", userinfo.getEmail());
        lcSharedPreferencesHelper.putValue("token", userinfo.getToken());
        lcSharedPreferencesHelper.putValue(LCSharedPreferencesHelper.PROVINCIALID, userinfo.getProvincial().getId());
        lcSharedPreferencesHelper.putValue(LCSharedPreferencesHelper.CITYID, userinfo.getCity().getId());
        lcSharedPreferencesHelper.putValue(LCSharedPreferencesHelper.DISTRICTID, userinfo.getDistrict().getId());
        lcSharedPreferencesHelper.putValue(LCSharedPreferencesHelper.IS_MEMBER, userinfo.getIs_member());
        MobclickAgent.onProfileSignIn(userinfo.getUserid());
        mIntent(M_INTENT);
        finish();


    }

    public void getNewVestion() {
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", LCUtils.getOnly(context));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.VERSION_VERSIONUPDATE, params, new AbStringHttpResponseListener() {

            // 获取数据成功会调用这里
            @Override
            public void onSuccess(int statusCode, String content) {
                String loadConvert = loadConvert(content);
                L.e(loadConvert);
                try {
                    JSONObject jo = new JSONObject(loadConvert);
                    String errorCode = jo.optString("errorCode", "1");
                    String errorMessage = jo.optString("errorMessage",
                            "已是最新版本！");
                    if ("0".equals(errorCode)) {
                        JSONObject jsonObject = jo.getJSONObject("data");
                        final String url = jsonObject.optString("url");
                        String version = jsonObject.optString("version");
                        String status = jsonObject.optString("status");
//                        String status = "0";
                        if ("0".equals(status)) {
                            if (getIsUpdate(LCUtils.getVersionName(context),
                                    version)) {
                                showDialog = showDialogTwoButton(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        showDialog.cancel();
                                        DownLoad(url);
                                        lc_login_dialog
                                                .setVisibility(View.VISIBLE);
                                    }
                                }, new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showDialog.cancel();
                                        savaLogin(LCConstant.userinfo);
                                    }
                                }, "检测有最新版本，是否更新？", "确定", "取消");
                                showDialog.setIsback(true);
                                showDialog.setCancelable(false);
                            } else {
                                savaLogin(LCConstant.userinfo);
                            }
                        } else {
                            if (getIsUpdate(LCUtils.getVersionName(context),
                                    version)) {
                                DownLoad(url);
                                lc_login_dialog
                                        .setVisibility(View.VISIBLE);
                            } else {
                                savaLogin(LCConstant.userinfo);
                            }
                        }
                    } else {
                        savaLogin(LCConstant.userinfo);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    savaLogin(LCConstant.userinfo);
                }
            }

            // 开始执行前
            @Override
            public void onStart() {
            }

            // 失败，调用
            @Override
            public void onFailure(int statusCode, String content,
                                  Throwable error) {
                savaLogin(LCConstant.userinfo);
            }

            // 完成后调用，失败，成功
            @Override
            public void onFinish() {
                LCUtils.stopProgressDialog(context);
            }
            ;
        });
    }

    public static boolean getIsUpdate(String oldv, String newv) {
        if (TextUtils.isEmpty(oldv)) {
            return true;
        }
        if (TextUtils.isEmpty(newv)) {
            return true;
        }
        if (newv.equals(oldv)) {
            return false;
        } else {

            int oldVerstionLeft = Integer.parseInt(oldv.substring(0,
                    oldv.indexOf(".")));
            int newVerStionLeft = Integer.parseInt(newv.substring(0,
                    newv.indexOf(".")));

            int oldVerstionCenter = Integer.parseInt(oldv.substring(
                    oldv.indexOf(".") + 1, oldv.lastIndexOf(".")));
            int newVerStionCenter = Integer.parseInt(newv.substring(
                    newv.indexOf(".") + 1, newv.lastIndexOf(".")));

            int oldVerstionRight = Integer.parseInt(oldv.substring(
                    oldv.lastIndexOf(".") + 1, oldv.length()));
            int newVerStionRight = Integer.parseInt(newv.substring(
                    newv.lastIndexOf(".") + 1, newv.length()));

            if (oldVerstionLeft >= newVerStionLeft) {
                if (oldVerstionLeft == newVerStionLeft) {
                    if (oldVerstionCenter >= newVerStionCenter) {
                        if (oldVerstionCenter == newVerStionCenter) {
                            if (oldVerstionRight >= newVerStionRight) {
                                if (oldVerstionRight == newVerStionRight) {
                                    return false;
                                } else {
                                    return false;
                                }
                            } else {
                                return true;
                            }

                        } else {
                            return false;
                        }

                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                return true;
            }

        }

    }

    public void DownLoad(String url) {

        mAbHttpUtil.get(url, new AbFileHttpResponseListener(url) {

            // 获取数据成功会调用这里
            @Override
            public void onSuccess(int statusCode, File file) {
                AutoInstall.setUrl(file.getPath());
                AutoInstall.install(context);
            }

            // 开始执行前
            @Override
            public void onStart() {

            }

            // 失败，调用
            @Override
            public void onFailure(int statusCode, String content,
                                  Throwable error) {
                savaLogin(LCConstant.userinfo);
            }

            // 下载进度
            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                lc_login_downtext.setText("正在更新：" + bytesWritten
                        / (totalSize / 100) + "/" + 100 + "%");
            }

            // 完成后调用，失败，成功
            public void onFinish() {
//                lc_login_dialog.setVisibility(View.GONE);
//                savaLogin(LCConstant.userinfo);
                isExit();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(login_bg);
        if(LCConstant.islogin==true){
            EventBus.getDefault().post(1001);
        }

    }
}
