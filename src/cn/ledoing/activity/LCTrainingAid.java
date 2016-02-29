package cn.ledoing.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import cn.ledoing.activity.R.string;
import cn.ledoing.adapter.LCTrainingAidAdapter;
import cn.ledoing.bean.TrainingAid;
import cn.ledoing.bean.TrainingAidAll;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

public class LCTrainingAid extends LCActivitySupport {
    private LCTitleBar lc_trainingaid_title;

    private ListView lc_trainingaid_mListView;
    private LCTrainingAidAdapter lcTrainingAidAdapter;
    TrainingAid trainingAid;
    List<TrainingAid> list;
    String send = "";
    private AbHttpUtil mAbHttpUtil = null;
    private LCNoNetWork lc_trainingaid_nonet;
    private RelativeLayout lc_trainingaid_content;
    private TrainingAidAll aidsperson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_trainingaid);
        Intent intent = getIntent();
        aidsperson = (TrainingAidAll) intent.getSerializableExtra("aidsperson");
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        lc_trainingaid_title = (LCTitleBar) findViewById(R.id.lc_trainingaid_title);
        lc_trainingaid_title
                .setCenterTitle(mString(string.lc_trainingaid_title));
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        lc_trainingaid_title.setRightTitle(mString(string.lc_save));
        lc_trainingaid_nonet = (LCNoNetWork) findViewById(R.id.lc_trainingaid_nonet);
        lc_trainingaid_content = (RelativeLayout) findViewById(R.id.lc_trainingaid_content);
        lc_trainingaid_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netAidspersonget();
                isNoNet();
            }
        });
        lc_trainingaid_title.setRightTitleListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                choose();
            }
        });
        lc_trainingaid_mListView = (ListView) findViewById(R.id.lc_trainingaid_mListView);
        list = new ArrayList<TrainingAid>();

        if (aidsperson != null) {
            if ("0".equals(aidsperson.getErrorCode())) {
                list = aidsperson.getList();
                lcTrainingAidAdapter = new LCTrainingAidAdapter(list, context);
                lc_trainingaid_mListView.setAdapter(lcTrainingAidAdapter);
            } else {
                LCUtils.ReLogin(aidsperson.getErrorCode(), context, aidsperson.getErrorMessage());
            }
        } else {
            netAidspersonget();
        }

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
        lc_trainingaid_nonet.setVisibility(View.GONE);
        lc_trainingaid_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_trainingaid_nonet.setVisibility(View.VISIBLE);
        lc_trainingaid_content.setVisibility(View.GONE);
    }

    protected void choose() {
        // TODO Auto-generated method stub

        if (lcTrainingAidAdapter == null) {
            showToast("未选中");
            return;
        }

        // int count = lcTrainingAidAdapter.getCount();
        send = lcTrainingAidAdapter.getText();
        if (!TextUtils.isEmpty(send)) {
            send = send.substring(0, send.length() - 1);
            netSendAidsperson(send);
        } else {
            showToast("未选中");
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
                        // 移除进度框
                        AbDialogUtil.removeDialog(context);
                        TrainingAidAll aidsperson = JSONUtils.getInstatce()
                                .getAidsperson(content);

                        if ("0".equals(aidsperson.getErrorCode())) {
                            list = aidsperson.getList();
                            lcTrainingAidAdapter = new LCTrainingAidAdapter(
                                    list, context);
                            lc_trainingaid_mListView
                                    .setAdapter(lcTrainingAidAdapter);
                        } else {
                            LCUtils.ReLogin(aidsperson.getErrorCode(), context, aidsperson.getErrorMessage());
//							showToast(aidsperson.getErrorMessage());
                        }

                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        AbDialogUtil.showProgressDialog(context, 0, "正在查询...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        // AbToastUtil.showToast(context, error.getMessage());
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

    private void netSendAidsperson(String send) {
        // TODO Auto-generated method stub
        String substring = send.substring(0, send.length());
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("aids", substring);
        params.put("uuid", getOnly());


        mAbHttpUtil.post(LCConstant.URL +LCConstant.URL_API +LCConstant.USER_UPDATE_AIDS, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // 移除进度框
                        AbDialogUtil.removeDialog(context);
                        if (!TextUtils.isEmpty(content)) {

                            String loadConvert = loadConvert(content);
                            try {
                                JSONObject jo = new JSONObject(loadConvert);
                                String errorCode = jo.optString("errorCode",
                                        "1");
                                String errorMessage = jo.optString(
                                        "errorMessage", "0");

                                if ("0".equals(errorCode)) {
                                    showToast("发送成功！");
                                    LCUserInfo.ISAIDS=true;
                                    finish();
                                } else {
                                    // showToast(errorMessage);
                                    LCUtils.ReLogin(errorCode, context, errorMessage);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                showToast("发送失败！");
                            }
                        } else {
                            showToast("发送失败！");
                        }

                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        AbDialogUtil.showProgressDialog(context, 0, "正在提交...");
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
                        AbDialogUtil.removeDialog(context);
                    }

                    ;

                });
    }
}
