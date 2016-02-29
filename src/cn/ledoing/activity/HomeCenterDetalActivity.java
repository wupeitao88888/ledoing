package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.bean.AdvertisementList;
import cn.ledoing.bean.Institution;
import cn.ledoing.fragment.CenterFragment;
import cn.ledoing.fragment.CommentFragment;
import cn.ledoing.fragment.TrendsFragment;
import cn.ledoing.global.AbAppConfig;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCRatingBar;
import cn.ledoing.view.LCSlideShowView;
import cn.ledoing.view.LCTitleBar;
import cn.ledoing.view.LCViewPageSelectListener;
import cn.ledoing.view.ObservableScrollView;
import de.greenrobot.event.EventBus;

/**
 * Created by lc-php1 on 2015/11/2.
 */
public class HomeCenterDetalActivity extends LCActivitySupport implements ObservableScrollView.Callbacks, OnClickListener,LCViewPageSelectListener {

    // 关注 1  未关注0
    private LCSlideShowView lc_hcenter_view;
    private ImageView center_home_image;
    private TextView center_home_title;
    private LCRatingBar home_rating_bar;
    private TextView user_count;
    private TextView center_home_add;
    private TextView center_introduction_text;
    private TextView home_dt_text;
    private TextView home_comment_text;
    private TextView home_appointment;
    private TextView center_comment;
    private View stopView;
    private ObservableScrollView scrollView;
    private ArrayList<View> views;
    private FrameLayout fragment;
    private LCTitleBar lc_title;
    private AbHttpUtil mAbHttpUtil;
    private LCNoNetWork lc_mebean_nonet;
    private FrameLayout framelayout_fl;
    private Institution institution;
    private LinearLayout lc_slideshowview_select;
    private ImageView[] dots;
    private String gz_status = "1";
    private TextView user_score;
    private String center_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_detal_layout);
        center_id = getIntent().getStringExtra("center_id");
        EventBus.getDefault().register(this);
        initView();
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        getInstitution();
    }

    private void initView() {
        lc_mebean_nonet = (LCNoNetWork) findViewById(R.id.lc_mebean_nonet);
        framelayout_fl = (FrameLayout) findViewById(R.id.framelayout_fl);
        lc_title = (LCTitleBar) findViewById(R.id.lc_title);
        lc_hcenter_view = (LCSlideShowView) findViewById(R.id.lc_hcenter_view);
        lc_slideshowview_select = (LinearLayout) findViewById(R.id.lc_slideshowview_select);
        center_home_image = (ImageView) findViewById(R.id.center_home_image);
        center_home_title = (TextView) findViewById(R.id.center_home_title);
        user_count = (TextView) findViewById(R.id.user_count);
        home_rating_bar = (LCRatingBar) findViewById(R.id.home_rating_bar);
        center_home_add = (TextView) findViewById(R.id.center_home_add);
        center_introduction_text = (TextView) findViewById(R.id.center_introduction_text);
        home_dt_text = (TextView) findViewById(R.id.home_dt_text);
        home_comment_text = (TextView) findViewById(R.id.home_comment_text);
        home_appointment = (TextView) findViewById(R.id.home_appointment);
        center_comment = (TextView) findViewById(R.id.center_comment);
        stopView = (View) findViewById(R.id.stopView);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        fragment = (FrameLayout) findViewById(R.id.fragment);
        user_score = (TextView) findViewById(R.id.user_score);
        scrollView.setCallbacks(this);
        center_introduction_text.setOnClickListener(this);
        home_dt_text.setOnClickListener(this);
        home_comment_text.setOnClickListener(this);
        center_comment.setOnClickListener(this);
        home_appointment.setOnClickListener(this);
        lc_title.setRightTitleListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getConcern();
            }
        });
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        onScrollChanged(scrollView.getScrollY());
                    }
                });
        // 滚动范围
        scrollView.scrollTo(0, 0);
        scrollView.smoothScrollTo(0, 0);//设置scrollView默认滚动到顶部
        setBgTtBg(true, false, false);
    }

    @Override
    public void onScrollChanged(int scrollY) {
        ((LinearLayout) this.findViewById(R.id.center_home_ll))
                .setTranslationY(Math.max(stopView.getTop(), scrollY));

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.center_introduction_text:
                setBgTtBg(true, false,false);
                Fragment center_fragment = (Fragment) mFragmentPagerAdapter
                        .instantiateItem(fragment, R.id.center_introduction_text);
                mFragmentPagerAdapter.setPrimaryItem(fragment, 0, center_fragment);
                mFragmentPagerAdapter.finishUpdate(fragment);
                break;
            case R.id.home_dt_text:
                setBgTtBg(false, true, false);
                Fragment trend_fragment = (Fragment) mFragmentPagerAdapter
                        .instantiateItem(fragment, R.id.home_dt_text);
                mFragmentPagerAdapter.setPrimaryItem(fragment, 0, trend_fragment);
                mFragmentPagerAdapter.finishUpdate(fragment);
                break;
            case R.id.home_comment_text:
                setBgTtBg(false, false,true);
                Fragment comment_fragment = (Fragment) mFragmentPagerAdapter
                        .instantiateItem(fragment, R.id.home_comment_text);
                mFragmentPagerAdapter.setPrimaryItem(fragment, 0, comment_fragment);
                mFragmentPagerAdapter.finishUpdate(fragment);
                break;
            case R.id.center_comment:
                if (!LCConstant.islogin) {
                    Intent intent = new Intent(this,
                            LCUserLoginAndRegister.class);
                    intent.putExtra("mIntent", 9);
                    intent.putExtra("ins_id", institution.getData().getIns_id());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this,CommentActivity.class);
                    Log.e("=============", institution.getData().getIns_id());
                    intent.putExtra("ins_id", institution.getData().getIns_id());
                    startActivityForResult(intent, AbAppConfig.COMMETN_CENTER_RESOUT);
                }
                break;
            case R.id.home_appointment:
                if (!LCConstant.islogin) {
                    Intent intent = new Intent(this,
                            LCUserLoginAndRegister.class);
                    intent.putExtra("mIntent", 10);
                    intent.putExtra("ins_id", institution.getData().getIns_id());
                    startActivity(intent);
                } else {
                    Intent about_intent = new Intent(context, LCActivityDetailsListActivity.class);
                    about_intent.putExtra("institution_id", institution.getData().getIns_id());
                    startActivity(about_intent);
                }
                break;
        }
    }

    private void getInstitution(){
        AbRequestParams params = new AbRequestParams();
        params.put("insid", center_id);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.GET_INSTITUTION_HOME, params,
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
                        institution = JSON.parseObject(content,Institution.class);

                        if( null != institution ){
                            try {
                                if("0".equals(institution.getErrorCode())){
                                    center_home_title.setText(institution.getData().getIns_name());
                                    Glide.with(context).load(institution.getData().getFace_pic()).into(center_home_image);
                                    String[] arry = institution.getData().getIns_album();
                                    initCarousel(arry);
                                    if(TextUtils.isEmpty(institution.getData().getScore())){
                                        institution.getData().setScore("0.0");
                                    }
                                    lc_title.setCenterTitle(institution.getData().getIns_name());
                                    home_rating_bar.setMark(Float.parseFloat(institution.getData().getScore()));
                                    user_score.setText(Float.parseFloat(institution.getData().getScore()) + "");
                                    center_home_add.setText(institution.getData().getIns_addr());
                                    user_count.setText(institution.getData().getComment_num()+"人");

                                    gz_status = institution.getData().getIsguanzhu();
                                    if(!TextUtils.isEmpty(institution.getData().getIsguanzhu()) && "1".equals(institution.getData().getIsguanzhu())){
                                        lc_title.setRightTitle("取消关注");
                                        gz_status = "2";
                                    }else if(!TextUtils.isEmpty(institution.getData().getIsguanzhu()) && "0".equals(institution.getData().getIsguanzhu())){
                                        lc_title.setRightTitle("关注");
                                        gz_status = "1";
                                    }
                                    Fragment center_fragment = (Fragment) mFragmentPagerAdapter
                                            .instantiateItem(fragment, R.id.center_introduction_text);
                                    mFragmentPagerAdapter.setPrimaryItem(fragment, 0, center_fragment);
                                    mFragmentPagerAdapter.finishUpdate(fragment);
                                } else {
                                    Toast.makeText(HomeCenterDetalActivity.this,institution.getErrorMessage()+"",Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(HomeCenterDetalActivity.this,institution.getErrorMessage()+"",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                                Toast.makeText(HomeCenterDetalActivity.this,content+"",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 关注（取消关注）
     */
    private void getConcern(){
        AbRequestParams params = new AbRequestParams();
        params.put("status", gz_status);
        params.put("insid", center_id);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_GUANZHU, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFinish() {
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                            showToast(content+"");
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        String sodeMessage = JSONUtils.getInstatce().getConcernOver(content, HomeCenterDetalActivity.this);
                        if(!TextUtils.isEmpty(sodeMessage) && "0".equals(sodeMessage)){
                            if("2".equals(gz_status)){
                                gz_status = "1";
                                lc_title.setRightTitle("关注");
                                Toast.makeText(context,"取消关注成功",Toast.LENGTH_SHORT).show();
                            } else {
                                gz_status = "2";
                                lc_title.setRightTitle("取消关注");
                                Toast.makeText(context,"关注成功",Toast.LENGTH_SHORT).show();
                            }
                        }
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
        lc_mebean_nonet.setVisibility(View.GONE);
        framelayout_fl.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_mebean_nonet.setVisibility(View.VISIBLE);
        framelayout_fl.setVisibility(View.GONE);
    }

    public void setBgTtBg(boolean centerSelected,boolean dtSelected,boolean comment){
        center_introduction_text.setSelected(centerSelected);
        home_dt_text.setSelected(dtSelected);
        home_comment_text.setSelected(comment);
    }

    private FragmentPagerAdapter mFragmentPagerAdapter = new FragmentPagerAdapter(
            getSupportFragmentManager()) {

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case R.id.home_comment_text:
                    return CommentFragment.instantiation(institution.getData().getIns_id());
                case R.id.home_dt_text:
                    return TrendsFragment.instantiation(institution.getData().getIns_id());
                case R.id.center_introduction_text:
                default:
                    return CenterFragment.instantiation(1,institution);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

    };
    private void initCarousel(final String[] list) {
        lc_hcenter_view.setCirculation(true);
        List<View> imageUris = new ArrayList<View>();
        lc_hcenter_view.clear();
        lc_slideshowview_select.removeAllViews();
        dots = new ImageView[list.length];
        for (int i = 0; i < list.length; i++) {
            ImageView imageView = new ImageView(HomeCenterDetalActivity.this);
            final String advertisementList = list[i];
            LCUtils.mImageloader(advertisementList, imageView, HomeCenterDetalActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            RelativeLayout re = new RelativeLayout(HomeCenterDetalActivity.this);
            RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(imageParams);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) AbViewUtil.dip2px(HomeCenterDetalActivity.this, 201));
            re.setLayoutParams(params);
            re.addView(imageView);
            imageUris.add(re);
            ImageView imageSelct = new ImageView(HomeCenterDetalActivity.this);
            imageSelct.setImageDrawable(this.getResources().getDrawable(R.drawable.ad_selector));
            dots[i] = imageSelct;
            int padding = (int) AbViewUtil.dip2px(HomeCenterDetalActivity.this, 3);
            int h = (int) AbViewUtil.dip2px(HomeCenterDetalActivity.this, 10);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(h, h);
            layoutParams.setMargins(padding, padding, padding, padding);
            imageSelct.setLayoutParams(layoutParams);
            lc_slideshowview_select.addView(imageSelct);
        }
        lc_hcenter_view.setImageUris(imageUris);
        lc_hcenter_view.setJXBViewPageSelectListener(this);
        if(list.length > 0)
        {
            dots[0].setSelected(true);
        }
    }

    @Override
    public void succeedCallBack(int p) {
        changeDots(p);
    }

    @Override
    public void succeedEndCallBack(int p) {

    }
    public void changeDots(int position) {

        for (int i = 0; i < dots.length; i++) {
            dots[i].setSelected(position == i);
        }
    }
    public void onEvent(Object object) {
        if(AbAppConfig.CENTER_FRAGMENT_POST.equals(object)){
            scrollView.scrollTo(0, 0);
            setBgTtBg(false, true, false);
            Fragment trend_fragment = (Fragment) mFragmentPagerAdapter
                    .instantiateItem(fragment, R.id.home_dt_text);
            mFragmentPagerAdapter.setPrimaryItem(fragment, 0, trend_fragment);
            mFragmentPagerAdapter.finishUpdate(fragment);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        super.onActivityResult(requestCode, resultCode, arg2);
        if(requestCode == AbAppConfig.COMMETN_CENTER_RESOUT){
            getInstitution();
        }
    }
}
