package cn.ledoing.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.view.pullview.AbPullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.adapter.LCBeanTaskAdapter;
import cn.ledoing.bean.BeanTask;
import cn.ledoing.bean.BeanTaskErrorCode;
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


public class LCMelcBeanTask extends LCActivitySupport  {

    private LCTitleBar lc_mebeantask_title;
    private RelativeLayout lc_mebeantask_content;
    //    private AbPullToRefreshView lc_mebeantask_pullto;
    private ListView lc_mebeantask_ListView;
    private View lc_mebeantask_refresh;
    private LCNoNetWork lc_mebeantask_nonet;
    private List<BeanTask> beanlist;
    private LCBeanTaskAdapter beanTaskAdapter;
    private AbHttpUtil mAbHttpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lcmelc_bean_task);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        initView();

    }

    private void initView() {
        beanlist = new ArrayList<BeanTask>();
        lc_mebeantask_title = (LCTitleBar) findViewById(R.id.lc_mebeantask_title);
        lc_mebeantask_title.setCenterTitle(getResources().getString(R.string.balancetask));
        lc_mebeantask_content = (RelativeLayout) findViewById(R.id.lc_mebeantask_content);
//        lc_mebeantask_pullto=(AbPullToRefreshView)findViewById(R.id.lc_mebeantask_pullto);
        lc_mebeantask_ListView = (ListView) findViewById(R.id.lc_mebeantask_ListView);
        lc_mebeantask_refresh = (View) findViewById(R.id.lc_mebeantask_refresh);
        lc_mebeantask_nonet = (LCNoNetWork) findViewById(R.id.lc_mebeantask_nonet);
        lc_mebeantask_refresh.setVisibility(View.GONE);
        lc_mebeantask_nonet.setVisibility(View.GONE);
        lc_mebeantask_nonet.setRetryOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });
        // //设置监听器
//        lc_mebeantask_pullto
//                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {
//
//                    @Override
//                    public void onHeaderRefresh(AbPullToRefreshView view) {
//                        lc_mebeantask_pullto.onHeaderRefreshFinish();
//                        getLeBeanTask();
//                    }
//                });
//        lc_mebeantask_pullto
//                .setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {
//
//                    @Override
//                    public void onFooterLoad(AbPullToRefreshView view) {
//                        showToast("暂无数据");
//                        lc_mebeantask_pullto.onFooterLoadFinish();
//                    }
//                });

        getLeBeanTask();
        // 设置进度条的样式
//        lc_mebeantask_pullto.getHeaderView().setHeaderProgressBarDrawable(
//                this.getResources().getDrawable(R.drawable.progress_circular));
//        lc_mebeantask_pullto.getFooterView().setFooterProgressBarDrawable(
//                this.getResources().getDrawable(R.drawable.progress_circular));
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
        lc_mebeantask_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_mebeantask_nonet.setVisibility(View.VISIBLE);
        lc_mebeantask_content.setVisibility(View.GONE);
    }

    public void getLeBeanTask() {
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", LCUtils.getOnly(this));

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.LEDOU_USER_TASK_LIST, params,
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
//                        showToast(statusCode);
                        isNoNet();
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
//                        String convert = loadConvert(content);
//                        beanTaskAdapter.notifyDataSetChanged();
                        L.e(content);
                        BeanTaskErrorCode beanTask = JSONUtils.getInstatce().getBeanTask(content, context);
                        setBeanTask(beanTask);

                    }


                });
    }

    private void setBeanTask(BeanTaskErrorCode beanTask) {
        if (beanTask.getErrorCode().equals("0")) {
            beanlist = beanTask.getList();
            L.e("+++++++++++listsize：" + beanlist.size());
            beanTaskAdapter = new LCBeanTaskAdapter(context, beanlist);
            lc_mebeantask_ListView.setAdapter(beanTaskAdapter);
        } else {
//            showToast("暂无数据");
            LCUtils.ReLogin(beanTask.getErrorCode(),context,beanTask.getErrorMessage());
        }

    }

}
