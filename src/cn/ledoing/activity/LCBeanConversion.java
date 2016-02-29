package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.adapter.LCBeanConversionAdapter;
import cn.ledoing.bean.LDChange;
import cn.ledoing.bean.LDChangeEorror;
import cn.ledoing.bean.SingleHttpBean;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;


public class LCBeanConversion extends LCActivitySupport implements View.OnClickListener {
    private LCTitleBar lc_beanconversion_title;
    private RelativeLayout lc_beanconversion_content;
    private EditText lc_beanconversion_code;
    private Button lc_beanconversion_conversion;
    private TextView lc_conversion_remind;
    private ListView lc_conversion_ListView;
    private LCNoNetWork lc_mebeantask_nonet;
    private LCBeanConversionAdapter beanConversionAdapter;
    private List<LDChange> listbean;
    private AbHttpUtil mAbHttpUtil;
    private RelativeLayout rl_no_data_tip; // 无数据展示
    private ImageView bg_no_data_tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcbean_conversion);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        initView();
        getLeBeanChange();
    }

    private void initView() {
        lc_beanconversion_title = (LCTitleBar) findViewById(R.id.lc_beanconversion_title);
        lc_beanconversion_title.setCenterTitle(getResources().getString(R.string.balanceconversion));
        lc_beanconversion_content = (RelativeLayout) findViewById(R.id.lc_beanconversion_content);
        lc_beanconversion_code = (EditText) findViewById(R.id.lc_beanconversion_code);
        lc_beanconversion_conversion = (Button) findViewById(R.id.lc_beanconversion_conversion);
        lc_conversion_remind = (TextView) findViewById(R.id.lc_conversion_remind);
        lc_conversion_ListView = (ListView) findViewById(R.id.lc_conversion_ListView);
        lc_mebeantask_nonet = (LCNoNetWork) findViewById(R.id.lc_mebeantask_nonet);
        rl_no_data_tip = (RelativeLayout) findViewById(R.id.rl_no_data_tip);
        bg_no_data_tip=(ImageView)findViewById(R.id.bg_no_data_tip);
        lc_mebeantask_nonet.setVisibility(View.GONE);
        rl_no_data_tip.setOnClickListener(this);
        lc_beanconversion_conversion.setOnClickListener(this);
        listbean = new ArrayList<LDChange>();
        beanConversionAdapter = new LCBeanConversionAdapter(context, listbean);
        lc_conversion_ListView.setAdapter(beanConversionAdapter);
        lc_beanconversion_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    lc_conversion_remind.setVisibility(View.GONE);
                }
            }
        });
        lc_beanconversion_title.setOnclickBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

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
        lc_mebeantask_nonet.setVisibility(View.GONE);
        lc_beanconversion_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_mebeantask_nonet.setVisibility(View.VISIBLE);
        lc_beanconversion_content.setVisibility(View.GONE);
    }

    private void getLeBeanChange() {
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", LCUtils.getOnly(this));

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.LEDOU_USER_COUPON_LIST, params, new AbStringHttpResponseListener() {
            @Override
            public void onSuccess(int statusCode, String content) {
                L.e("-------------onSuccess---------------------", content);
                LDChangeEorror beanTask = JSONUtils.getInstatce().getLDChangeOver(content, context);
                if ( beanTask.getLdChange() != null && beanTask.getLdChange().size() > 0 ){
                    listbean.addAll(beanTask.getLdChange());
                    beanConversionAdapter.notifyDataSetChanged();
                } else {
                    rl_no_data_tip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStart() {
                LCUtils.startProgressDialog(context);
            }

            @Override
            public void onFinish() {
                LCUtils.stopProgressDialog(context);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                L.e(statusCode + "---------------onFailure-------------------", content);

                if (statusCode == 404) {
                    isNoNet();
                } else {
                    showToast(content);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_no_data_tip:
                getLeBeanChange();
                rl_no_data_tip.setVisibility(View.GONE);
                break;
            case R.id.lc_beanconversion_conversion:
                if (TextUtils.isEmpty(lc_beanconversion_code.getText().toString())) {
                    showToast("请输入兑换码");
                    return;
                }
                clickPraise(lc_beanconversion_code.getText().toString());
                break;

        }
    }

    /**
     * 兑换乐豆
     */
    public void clickPraise(String code) {
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", LCUtils.getOnly(context));
        params.put("code", code);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.LEDOU_EXCHANGE, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        SingleHttpBean singlehttp = JSONUtils.getInstatce().getLDVoucherOver(
                                content, context);
                        if ("20050".equals(singlehttp.getErrorCode())) {
                            lc_conversion_remind.setVisibility(View.VISIBLE);
                        }
                        AbToastUtil.showToast(context, singlehttp.getErrorMessage());
                    }

                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context);
                    }

                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        lc_conversion_remind.setVisibility(View.VISIBLE);
                        AbToastUtil.showToast(context, "兑换失败");

                    }

                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }

                });
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode ==  event.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyUp(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(bg_no_data_tip);
    }
}
