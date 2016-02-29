package cn.ledoing.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.ledoing.activity.LCAdWebViewActiviy;
import cn.ledoing.activity.R;
import cn.ledoing.adapter.LCFindAllAdapter;
import cn.ledoing.bean.Advertisement;
import cn.ledoing.bean.AdvertisementList;
import cn.ledoing.bean.FindAll;
import cn.ledoing.bean.FindList;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.CustomGridView;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCSlideShowView;
import cn.ledoing.view.LCViewPageSelectListener;
import cn.ledoing.view.PullScrollView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

@SuppressLint("NewApi")
public class LCFindContentFragment extends AbFragment implements
        LCViewPageSelectListener, PullScrollView.OnPullListener {

    // private MyApplication application;
    private Activity mActivity = null;
    private List<FindList> mList = null;
    //    private PullToRefreshView mAbPullToRefreshView = null;
    private CustomGridView mListView = null;
    private int currentPage = 1;
    // private ArticleListAdapter myListViewAdapter = null;
    private int total = 50;
    private int pageSize = 5;
    //    private int inPage = 1;
    private AbHttpUtil mAbHttpUtil = null;
    private LCFindAllAdapter findAllAdapter;
    private View lc_find_refresh, lc_find_carousel;
    private LCNoNetWork lc_fcontent_nonet;
    private LCSlideShowView lc_slideshowview_carousel;
    private LinearLayout lc_slideshowview_select;
    private PullScrollView scroll;


    private ImageView[] dots;
    List<AdvertisementList> listurl;

    @Override
    public View onCreateContentView(LayoutInflater inflater,
                                    ViewGroup container, Bundle savedInstanceState) {
        mActivity = this.getActivity();
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(getActivity());
        mAbHttpUtil.setTimeout(5000);
        View view = inflater.inflate(R.layout.lc_layout_gridview, null);
        listurl = new ArrayList<AdvertisementList>();
        // 获取ListView对象
        scroll = (PullScrollView) view
                .findViewById(R.id.scroll);
        View contentLayout = inflater.inflate(R.layout.lc_layout_customgridview, null);
        scroll.addBodyView(contentLayout);
        scroll.setOnPullListener(this);
        scroll.setfooterViewReset();
        scroll.setOverScrollMode(scroll.OVER_SCROLL_NEVER);
        mListView = (CustomGridView) contentLayout.findViewById(R.id.lc_find_gridview);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lc_find_carousel = (View) contentLayout.findViewById(R.id.lc_find_carousel);
        lc_find_refresh = (View) view.findViewById(R.id.lc_find_refresh);
        lc_find_refresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                refreshTask();
            }
        });
        lc_fcontent_nonet = (LCNoNetWork) view
                .findViewById(R.id.lc_fcontent_nonet);
        lc_fcontent_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
                refreshTask();
            }
        });
        // ListView数据
        mList = new ArrayList<FindList>();
        isNoNet();
        refreshTask();
        return view;
    }

    private void initCarousel(final List<AdvertisementList> list) {
        lc_slideshowview_carousel = (LCSlideShowView) lc_find_carousel.findViewById(R.id.lc_slideshowview_carousel);
        lc_slideshowview_carousel.setCirculation(true);
        lc_slideshowview_select = (LinearLayout) lc_find_carousel.findViewById(R.id.lc_slideshowview_select);
        List<View> imageUris = new ArrayList<View>();
        listurl = list;
        lc_slideshowview_carousel.clear();
        lc_slideshowview_select.removeAllViews();
        dots = new ImageView[listurl.size()];
        for (int i = 0; i < listurl.size(); i++) {
            ImageView imageView = new ImageView(mActivity);
            final AdvertisementList advertisementList = listurl.get(i);
            LCUtils.mImageloader(advertisementList.getMedia_url(), imageView, mActivity);
            RelativeLayout re = new RelativeLayout(mActivity);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imageParams);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) AbViewUtil.dip2px(mActivity, 201));
            re.setLayoutParams(params);
            re.addView(imageView);
            re.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!TextUtils.isEmpty(advertisementList.getAds_url())){
                        Intent intent = new Intent(mActivity, LCAdWebViewActiviy.class);
                        intent.putExtra("url", advertisementList.getAds_url());
                        startActivity(intent);
                    }else{
                        AbToastUtil.showToast(mActivity,"没什么好看的！");
                    }
                }
            });
            imageUris.add(re);
            ImageView imageSelct = new ImageView(mActivity);
            imageSelct.setImageDrawable(this.getResources().getDrawable(R.drawable.ad_selector));
            dots[i] = imageSelct;
            int padding = (int) AbViewUtil.dip2px(mActivity, 3);
            int h = (int) AbViewUtil.dip2px(mActivity, 10);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(h, h);
            layoutParams.setMargins(padding, padding, padding, padding);
            imageSelct.setLayoutParams(layoutParams);
            lc_slideshowview_select.addView(imageSelct);
        }
        lc_slideshowview_carousel.setImageUris(imageUris);
        lc_slideshowview_carousel.setJXBViewPageSelectListener(this);
        if(listurl.size()>0)
        {
            dots[0].setSelected(true);
        }
    }



    private void isNoNet() {
        // TODO Auto-generated method stub
        if (LCUtils.isNetworkAvailable(getActivity())) {
            setNotNetBack();
        } else {
            setNotNet();
        }
    }

    public void setNotNetBack() {
        lc_fcontent_nonet.setVisibility(View.GONE);
//        mAbPullToRefreshView.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_fcontent_nonet.setVisibility(View.VISIBLE);
//        mAbPullToRefreshView.setVisibility(View.GONE);
    }

    protected void loadMoreTask() {
        // TODO Auto-generated method stub
        currentPage++;
        AbRequestParams params = new AbRequestParams();
//        params.put("categoryid", inPage + "");
        params.put("page", currentPage + "");
        params.put("pagesize", "10");
        params.put("uuid", LCUtils.getOnly(getActivity()));

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.TEAM_WORK_LIST, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        setLoadContent(content);
                        // showContentView();

                    }

                    // 开始执行前
                    @Override
                    public void onStart() {

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
                        scroll.setfooterViewReset();
                        scroll.setfooterViewGone();
//                        mAbPullToRefreshView.onFooterLoadFinish();
                    }

                    ;
                });
    }

    @Override
    public void setResource() {

    }


    /**
     * 下载数据
     */
    public void refreshTask() {
        currentPage = 1;
        AbRequestParams params = new AbRequestParams();
        params.put("page", currentPage + "");
        params.put("pagesize", "10");
        params.put("uuid", LCUtils.getOnly(getActivity()));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.TEAM_WORK_LIST, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {

                        setCintent(content);
                        // showContentView();

                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // showProgress();
                        LCUtils.startProgressDialog(mActivity);
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
                        // stopProgress();
//                        mAbPullToRefreshView.onHeaderRefreshFinish();
                        LCUtils.stopProgressDialog(mActivity);
                        scroll.setheaderViewReset();
                        advertisement();
                    }
                });
    }

    public void advertisement() {
        AbRequestParams params = new AbRequestParams();
        mAbHttpUtil.post(LCConstant.URL + LCConstant.COMMON_GETAD, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {

                        Advertisement advertisement = JSONUtils.getInstatce().getAdvertisement(content, mActivity);
                        if ("0".equals(advertisement.getErrorCode())) {
                            if (mList.size() > 0 || advertisement.getList().size() > 0) {
                                lc_find_refresh.setVisibility(View.GONE);
                            } else {
                                lc_find_refresh.setVisibility(View.VISIBLE);
                            }
                            if(advertisement.getList().size()>0){
                                lc_find_carousel.setVisibility(View.VISIBLE);
                            }else{
                                lc_find_carousel.setVisibility(View.GONE);
                            }
                            initCarousel(advertisement.getList());
                        } else {
                            AbToastUtil.showToast(mActivity, advertisement.getErrorMessage());
                        }
                        // showContentView();
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // showProgress();
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
                        // stopProgress();
//                        mAbPullToRefreshView.onHeaderRefreshFinish();
//                        LCUtils.stopProgressDialog(mActivity);
                    }

                    ;
                });
    }

    protected void setCintent(String content) {
        // TODO Auto-generated method stub
        FindAll findAll = JSONUtils.getInstatce().getFindAll(content);
        if ("0".equals(findAll.getErrorCode())) {
            mList = findAll.getList();
//            if (mList.size() > 0) {
//                lc_find_refresh.setVisibility(View.GONE);
//            } else {
//                lc_find_refresh.setVisibility(View.VISIBLE);
//            }
            initContent(mList);
        } else {
            LCUtils.ReLogin(findAll.getErrorCode(), getActivity(), findAll.getErrorMessage());
        }
    }

    protected void setLoadContent(String content) {
        // TODO Auto-generated method stub
        FindAll findAll = JSONUtils.getInstatce().getFindAll(content);

        if ("0".equals(findAll.getErrorCode())) {
            if (mList.size() < Integer.parseInt(findAll.getTotal_count())) {
                mList.addAll(findAll.getList());
                initContent(mList);
            } else {
                AbToastUtil.showToast(getActivity(), "暂无数据");
            }
        } else {

            LCUtils.ReLogin(findAll.getErrorCode(), getActivity(), findAll.getErrorMessage());
        }
    }

    private void initContent(List<FindList> list) {
        // TODO Auto-generated method stub

        L.e("数据：" + mList.size());
        findAllAdapter = new LCFindAllAdapter(mActivity, mList);
        mListView.setAdapter(findAllAdapter);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String ss = data.getStringExtra("isRefresh");
        if ("1".equals(ss)) {
            // 刷新
            refreshTask();
        } else {
            // 不刷新
            AbDialogUtil.removeDialog(getActivity());
        }

    }

    @Override
    public void succeedCallBack(int p) {
        changeDots(p);
    }

    @Override
    public void succeedEndCallBack(int p) {
//        if(){
//
//        }
    }

    public void changeDots(int position) {

        for (int i = 0; i < dots.length; i++) {
            dots[i].setSelected(position == i);
        }
    }


    @Override
    public void refresh() {
        refreshTask();
    }

    @Override
    public void loadMore() {
        loadMoreTask();
    }


}
