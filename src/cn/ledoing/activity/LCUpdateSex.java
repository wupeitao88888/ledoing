package cn.ledoing.activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

/**
 * Created by lvfl on 2015/9/6.
 */
public class LCUpdateSex extends LCActivitySupport implements OnClickListener {

    private LCTitleBar lc_updatesex_title;
    private CheckBox lc_men_checkBox;
    private CheckBox lc_woman_checkBox;
    private Button lc_update_submit;
    private int isMan;
    private AbHttpUtil mAbHttpUtil;
    private RelativeLayout lc_updatesex_content;
    private LCNoNetWork lc_updatesex_nonet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lc_update_sex_layout);
        Intent intent = getIntent();
        try {
            isMan = Integer.parseInt(intent.getStringExtra("realsex"));
        } catch (Exception e) {
            isMan = 0;
        }
        initView();

    }

    private void initView() {

        lc_updatesex_title = (LCTitleBar) findViewById(R.id.lc_updatesex_title);
        lc_men_checkBox = (CheckBox) findViewById(R.id.lc_men_checkBox);
        lc_woman_checkBox = (CheckBox) findViewById(R.id.lc_woman_checkBox);
        lc_update_submit = (Button) findViewById(R.id.lc_update_submit);
        lc_updatesex_content = (RelativeLayout) findViewById(R.id.lc_updatesex_content);
        lc_updatesex_nonet = (LCNoNetWork) findViewById(R.id.lc_updatesex_nonet);
        lc_men_checkBox.setOnClickListener(this);
        lc_woman_checkBox.setOnClickListener(this);
        lc_update_submit.setOnClickListener(this);
        lc_updatesex_title.setCenterTitle("修改性别");
        checkBoxView();
    }


    private void checkBoxView(){
        if( isMan == 1 ){
            lc_men_checkBox.setChecked(true);
        } else {
            lc_woman_checkBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lc_men_checkBox:
                lc_woman_checkBox.setChecked(false);
                lc_men_checkBox.setChecked(true);
                isMan = 1;
                break;
            case R.id.lc_woman_checkBox:
                lc_woman_checkBox.setChecked(true);
                lc_men_checkBox.setChecked(false);
                isMan = 0;
                break;
            case R.id.lc_update_submit:
                netInit(String.valueOf(isMan));
                break;
        }

    }


    private void netInit(final String isMan) {
        // TODO Auto-generated method stub
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("sex", isMan);
        params.put("uuid", getOnly());

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.UPDATE_USER_INFO, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // 移除进度框
                        AbDialogUtil.removeDialog(context);
                        String loadConvert = loadConvert(content);
                        try {
                            JSONObject jo = new JSONObject(loadConvert);
                            String errorCode = jo.optString("errorCode", "1");
                            String errorMessage = jo.optString("errorMessage",
                                    "保存失败！");
                            if ("0".equals(errorCode)) {
                                showToast("保存成功！");
                                sharedPreferencesHelper.putValue("sex", isMan);
                                finish();
                            } else {
                                LCUtils.ReLogin(errorCode, context, errorMessage);
                                showToast(errorMessage);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            showToast("保存失败！");
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        AbDialogUtil.showProgressDialog(context, 0, "正在保存...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        isNoNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {

                        // 移除进度框
                        AbDialogUtil.removeDialog(context);
                    }

                    ;

                });
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
        lc_updatesex_nonet.setVisibility(View.GONE);
        lc_updatesex_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_updatesex_nonet.setVisibility(View.VISIBLE);
        lc_updatesex_content.setVisibility(View.GONE);
    }

}
