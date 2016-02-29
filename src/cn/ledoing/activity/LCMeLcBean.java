package cn.ledoing.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

/**
 *
 * 我的乐豆
 */
public class LCMeLcBean extends LCActivitySupport implements View.OnClickListener {

    private LCTitleBar lc_mebean_title;
    private RelativeLayout lc_mebean_task;//乐豆任务
    private RelativeLayout lc_mebean_conversion;//乐豆兑换
    private RelativeLayout lc_mebean_detail;//乐豆明细
    private RelativeLayout lc_mebean_content;//没有网络
    private LCNoNetWork lc_mebean_nonet;//内容布局
    private AbHttpUtil mAbHttpUtil;
    private TextView lc_mebean_balance;
    public static final int CHANGE_RESULT = 100;
    private RelativeLayout lc_mebean_gift,lc_mebean_bg;//乐豆换礼物
    private ImageView lc_mebean_image,lc_mebean_giftimage,lc_mebean_arrow1,lc_mebean_taskimage,lc_mebean_arrow2,lc_mebean_conversionimage,lc_mebean_arrow3,lc_mebean_detailimage,lc_mebean_arrow4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcme_lc_bean);
        initView();
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        getLeBeanBalance();

    }

    private void initView() {
        lc_mebean_title = (LCTitleBar) findViewById(R.id.lc_mebean_title);
        lc_mebean_title.setCenterTitle(getResources().getString(R.string.title_activity_lcme_lc_bean));
        lc_mebean_nonet = (LCNoNetWork) findViewById(R.id.lc_mebean_nonet);
        lc_mebean_nonet.setRetryOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isNoNet();
                getLeBeanBalance();
            }
        });

        lc_mebean_image=(ImageView)findViewById(R.id.lc_mebean_image);
        lc_mebean_giftimage=(ImageView)findViewById(R.id.lc_mebean_giftimage);
        lc_mebean_arrow1=(ImageView)findViewById(R.id.lc_mebean_arrow1);
        lc_mebean_taskimage=(ImageView)findViewById(R.id.lc_mebean_taskimage);
        lc_mebean_arrow2=(ImageView)findViewById(R.id.lc_mebean_arrow2);
        lc_mebean_conversionimage=(ImageView)findViewById(R.id.lc_mebean_conversionimage);
        lc_mebean_arrow3=(ImageView)findViewById(R.id.lc_mebean_arrow3);
        lc_mebean_detailimage=(ImageView)findViewById(R.id.lc_mebean_detailimage);
        lc_mebean_arrow4=(ImageView)findViewById(R.id.lc_mebean_arrow4);

        lc_mebean_content = (RelativeLayout) findViewById(R.id.lc_mebean_content);
        lc_mebean_gift = (RelativeLayout) findViewById(R.id.lc_mebean_gift);
        lc_mebean_task = (RelativeLayout) findViewById(R.id.lc_mebean_task);
        lc_mebean_bg= (RelativeLayout) findViewById(R.id.lc_mebean_bg);
        lc_mebean_conversion = (RelativeLayout) findViewById(R.id.lc_mebean_conversion);
        lc_mebean_detail = (RelativeLayout) findViewById(R.id.lc_mebean_detail);
        lc_mebean_balance = (TextView) findViewById(R.id.lc_mebean_balance);
        lc_mebean_gift.setOnClickListener(this);
        lc_mebean_task.setOnClickListener(this);
        lc_mebean_conversion.setOnClickListener(this);
        lc_mebean_detail.setOnClickListener(this);
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
        lc_mebean_nonet.setVisibility(View.GONE);
        lc_mebean_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_mebean_nonet.setVisibility(View.VISIBLE);
        lc_mebean_content.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lc_mebean_gift:
                startActivityForResult(new Intent(LCMeLcBean.this, LCBeanGift.class), CHANGE_RESULT);
                break;
            case R.id.lc_mebean_task:
                startActivity(new Intent(LCMeLcBean.this, LCMelcBeanTask.class));
                break;
            case R.id.lc_mebean_conversion:
                startActivityForResult(new Intent(LCMeLcBean.this, LCBeanConversion.class), CHANGE_RESULT);
                break;
            case R.id.lc_mebean_detail:
                startActivity(new Intent(LCMeLcBean.this, LCBeanDetail.class));
                break;
        }
    }

    public void getLeBeanBalance() {
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", LCUtils.getOnly(this));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.LEDOU_USER_BALANCE, params,
                new AbStringHttpResponseListener() {
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
//                        showToast("网络连接失败");
                        isNoNet();
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        String balance = JSONUtils.getInstatce().getLeBeanCountOver(content, LCMeLcBean.this);
                        lc_mebean_balance.setText(balance);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHANGE_RESULT:
                getLeBeanBalance();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(lc_mebean_image);
        releaseRelativeLayout(lc_mebean_bg);
        releaseImageView(lc_mebean_giftimage);
        releaseImageView(lc_mebean_arrow1);
        releaseImageView(lc_mebean_taskimage);
        releaseImageView(lc_mebean_arrow2);
        releaseImageView(lc_mebean_conversionimage);
        releaseImageView(lc_mebean_arrow3);
        releaseImageView(lc_mebean_detailimage);
        releaseImageView(lc_mebean_arrow4);
    }
}
