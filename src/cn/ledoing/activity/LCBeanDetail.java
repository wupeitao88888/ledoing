package cn.ledoing.activity;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.adapter.LCBeanDetailAdapter;
import cn.ledoing.bean.BeanDetail;
import cn.ledoing.bean.ErrorCodeBeanDetail;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;


public class LCBeanDetail extends LCActivitySupport implements View.OnClickListener {
    private LCNoNetWork lc_beandetail_nonet;
    private RelativeLayout lc_beandetail_content,//所有布局内容
            lc_beandetail_all,//全部
            lc_beandetail_in,//收入
            lc_beandetail_out,//支出
            lc_beandetail_nodate;//暂无数据
    private AbPullToRefreshView lc_beandetail_pullto;
    private ListView lc_beandetail_ListView;
    private View lc_beandetail_refresh;
    private LCTitleBar lc_beandetail_title;
    private TextView lc_beandetail_textall,
            lc_beandetail_textin,
            lc_beandetail_textout;
    private AbHttpUtil mAbHttpUtil;
    private List<BeanDetail> listDetail;
    private LCBeanDetailAdapter beanDetailAdapter;
    private int mType = 0;//（1：收入  2：支出）  0为全部查看保持不变
    private int mPage = 0;
    private ErrorCodeBeanDetail beanDetail;
private ImageView detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcbean_detail);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        initView();
    }

    private void initView() {
        lc_beandetail_nonet = (LCNoNetWork) findViewById(R.id.lc_beandetail_nonet);
        lc_beandetail_nonet.setVisibility(View.GONE);
        lc_beandetail_nonet.setRetryOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });
        lc_beandetail_content = (RelativeLayout) findViewById(R.id.lc_beandetail_content);
        lc_beandetail_pullto = (AbPullToRefreshView) findViewById(R.id.lc_beandetail_pullto);
        lc_beandetail_ListView = (ListView) findViewById(R.id.lc_beandetail_ListView);
        lc_beandetail_refresh = (View) findViewById(R.id.lc_beandetail_refresh);
        lc_beandetail_title = (LCTitleBar) findViewById(R.id.lc_beandetail_title);
        lc_beandetail_title.setCenterTitle(getResources().getString(R.string.balancedetail));
        lc_beandetail_all = (RelativeLayout) findViewById(R.id.lc_beandetail_all);
        lc_beandetail_in = (RelativeLayout) findViewById(R.id.lc_beandetail_in);
        lc_beandetail_out = (RelativeLayout) findViewById(R.id.lc_beandetail_out);
        lc_beandetail_nodate= (RelativeLayout) findViewById(R.id.lc_beandetail_nodate);
        lc_beandetail_textall = (TextView) findViewById(R.id.lc_beandetail_textall);
        lc_beandetail_textin = (TextView) findViewById(R.id.lc_beandetail_textin);
        lc_beandetail_textout = (TextView) findViewById(R.id.lc_beandetail_textout);
        detail=(ImageView)findViewById(R.id.detail);
        lc_beandetail_all.setOnClickListener(this);
        lc_beandetail_in.setOnClickListener(this);
        lc_beandetail_out.setOnClickListener(this);

        lc_beandetail_ListView.setDivider(getResources().getDrawable(R.drawable.jxb_userinfo_normal));
        listDetail = new ArrayList<BeanDetail>();
        //初始化数据
        getLeBeanDteail(mType, mPage);
        lc_beandetail_nodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLeBeanDteail(mType, mPage);
            }
        });
        // //设置监听器
        lc_beandetail_pullto
                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                        lc_beandetail_pullto.onHeaderRefreshFinish();
                        getLeBeanDteail(mType, mPage);
                    }
                });
        lc_beandetail_pullto
                .setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

                    @Override
                    public void onFooterLoad(AbPullToRefreshView view) {
                        lc_beandetail_pullto.onFooterLoadFinish();

                        getLoadBeanDteail(mType, mPage);
                    }
                });

        // 设置进度条的样式
        lc_beandetail_pullto.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        lc_beandetail_pullto.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        lc_beandetail_all.setSelected(true);
        lc_beandetail_in.setSelected(false);
        lc_beandetail_out.setSelected(false);
        isNoNet();
    }

    public void getLeBeanDteail(int type, int page) {
        AbRequestParams params = new AbRequestParams();
        params.put("status", type + "");
        params.put("page", page + "");
        params.put("pagesize", 20 + "");
        params.put("platform", "android");
        params.put("time", LCUtils.gettime().substring(0, 10));
        params.put("uuid", LCUtils.getOnly(this));


        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.LEDOU_USER_ORDER_LIST, params,
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
                        L.e("ADD----------------------------------", content);
                        isNoNet();
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("ADD----------------------------------", loadConvert(content));
                        beanDetail = JSONUtils.getInstatce().getBeanDetail(content, context);
                        setBeanDetail(beanDetail);
                    }
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
        lc_beandetail_nonet.setVisibility(View.GONE);
        lc_beandetail_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_beandetail_nonet.setVisibility(View.VISIBLE);
        lc_beandetail_content.setVisibility(View.GONE);
    }


    public void getLoadBeanDteail(int type, int page) {
        if (TextUtils.isEmpty(beanDetail.getTotal_count())) {
            return;
        }
        if (Integer.parseInt(beanDetail.getTotal_count()) > listDetail.size()) {
            page++;
            AbRequestParams params = new AbRequestParams();
            params.put("status", type + "");
            params.put("uuid", LCUtils.getOnly(this));

            mAbHttpUtil.post(LCConstant.URL+LCConstant.URL_API + LCConstant.LEDOU_USER_ORDER_LIST, params,
                    new AbStringHttpResponseListener() {
                        @Override
                        public void onStart() {
//                        LCUtils.startProgressDialog(context);
                        }

                        @Override
                        public void onFinish() {
//                        LCUtils.stopProgressDialog(context);
                        }

                        @Override
                        public void onFailure(int statusCode, String content, Throwable error) {
                            L.e("ADD----------------------------------", content);
                            isNoNet();
                        }

                        @Override
                        public void onSuccess(int statusCode, String content) {
                            L.e("ADD----------------------------------", loadConvert(content));
                            ErrorCodeBeanDetail beanDetail = JSONUtils.getInstatce().getBeanDetail(content, context);
                            setLoadBeanDetail(beanDetail);
                        }
                    });
        } else {
            showToast(mString(R.string.no_date));
        }
    }

    private void setLoadBeanDetail(ErrorCodeBeanDetail beanDetail) {
        if ("0".equals(beanDetail.getErrorCode())) {
            listDetail.addAll(beanDetail.getList());
            beanDetailAdapter.notifyDataSetChanged();

        } else {
            showToast(beanDetail.getErrorMessage());
            LCUtils.ReLogin(beanDetail.getErrorCode(),context,beanDetail.getErrorMessage());
        }
    }

    private void setBeanDetail(ErrorCodeBeanDetail beanDetail) {
        if ("0".equals(beanDetail.getErrorCode())) {
            if(beanDetail.getList().size()==0){
                lc_beandetail_nodate.setVisibility(View.VISIBLE);
            }else{
                lc_beandetail_nodate.setVisibility(View.GONE);
                listDetail = beanDetail.getList();
                beanDetailAdapter = new LCBeanDetailAdapter(context, listDetail);
                lc_beandetail_ListView.setAdapter(beanDetailAdapter);
            }
        } else {
            showToast(beanDetail.getErrorMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lc_beandetail_all:
                lc_beandetail_all.setSelected(true);
                lc_beandetail_in.setSelected(false);
                lc_beandetail_out.setSelected(false);
                mType = 0;
                mPage=0;
                getLeBeanDteail(mType, mPage);
                break;
            case R.id.lc_beandetail_in:
                lc_beandetail_all.setSelected(false);
                lc_beandetail_in.setSelected(true);
                lc_beandetail_out.setSelected(false);
                mType = 1;
                mPage=0;
                getLeBeanDteail(mType, mPage);
                break;
            case R.id.lc_beandetail_out:
                lc_beandetail_all.setSelected(false);
                lc_beandetail_in.setSelected(false);
                lc_beandetail_out.setSelected(true);
                mType = 2;
                mPage=0;
                getLeBeanDteail(mType, mPage);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(detail);
    }
}
