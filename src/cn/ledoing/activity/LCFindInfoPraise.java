package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ledoing.bean.FindList;
import cn.ledoing.bean.IsPraise;
import cn.ledoing.bean.Praise;
import cn.ledoing.db.Dbhelper;
import cn.ledoing.db.LCDbHelper;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.FindRefresh;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

public class LCFindInfoPraise extends LCActivitySupport implements
        OnClickListener {
    private LCTitleBar lc_findinfo_title;
    private ImageView lc_findinfo_icon;

    private TextView lc_findinfo_name, lc_findinfo_praisecount;
    private RelativeLayout lc_findinfo_clickpraise;
    private FindList findList;//上层数据
    private int position;//上层数据源
    private AbHttpUtil mAbHttpUtil = null;
    private LinearLayout lc_findinfo_imageone, lc_findinfo_imagetwo,
            lc_findinfo_imagethree, lc_findinfo_imagefour,
            lc_findinfo_imagefive;
            private RelativeLayout lc_findinfo_content;
    private LCNoNetWork lc_findinfo_nonet;
    public static FindRefresh findRefresh = null;
    Dbhelper dbhelper;

    public static void setFindRefresh(FindRefresh findRefreshin) {
        findRefresh = findRefreshin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_findinfopraise);
        LCDbHelper lcDbHelper = new LCDbHelper(context);
        dbhelper = new Dbhelper(lcDbHelper);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        lc_findinfo_title = (LCTitleBar) findViewById(R.id.lc_findinfo_title);
        lc_findinfo_title.setCenterTitle("作品详情");
        lc_findinfo_icon = (ImageView) findViewById(R.id.lc_findinfo_icon);
        lc_findinfo_name = (TextView) findViewById(R.id.lc_findinfo_name);
        lc_findinfo_praisecount = (TextView) findViewById(R.id.lc_findinfo_praisecount);
        lc_findinfo_clickpraise = (RelativeLayout) findViewById(R.id.lc_findinfo_clickpraise);
        lc_findinfo_imageone = (LinearLayout) findViewById(R.id.lc_findinfo_imageone);
        lc_findinfo_imagetwo = (LinearLayout) findViewById(R.id.lc_findinfo_imagetwo);
        lc_findinfo_imagethree = (LinearLayout) findViewById(R.id.lc_findinfo_imagethree);
        lc_findinfo_imagefour = (LinearLayout) findViewById(R.id.lc_findinfo_imagefour);
        lc_findinfo_imagefive = (LinearLayout) findViewById(R.id.lc_findinfo_imagefive);
        lc_findinfo_content = (RelativeLayout) findViewById(R.id.lc_findinfo_content);
        lc_findinfo_nonet = (LCNoNetWork) findViewById(R.id.lc_findinfo_nonet);
        lc_findinfo_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });
        lc_findinfo_imageone.setOnClickListener(this);
        lc_findinfo_imagetwo.setOnClickListener(this);
        lc_findinfo_imagethree.setOnClickListener(this);
        lc_findinfo_imagefour.setOnClickListener(this);
        lc_findinfo_imagefive.setOnClickListener(this);

        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        Intent intent = getIntent();
        findList = (FindList) intent.getSerializableExtra("findList");
        position = intent.getIntExtra("position", 0);
        setPraise(lc_findinfo_clickpraise, findList);
        setIcon(lc_findinfo_icon, findList.getGroupimg());
        setTitle(lc_findinfo_name, findList.getName());
        setTitle(lc_findinfo_praisecount, findList.getPraise());

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
        lc_findinfo_nonet.setVisibility(View.GONE);
        lc_findinfo_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_findinfo_nonet.setVisibility(View.VISIBLE);
        lc_findinfo_content.setVisibility(View.GONE);
    }

    private void setPraise(RelativeLayout lc_find_praise,
                           final FindList classList) {
        // TODO Auto-generated method stub
        lc_find_praise.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (LCConstant.islogin) {
                    if(!dbhelper.checkedPraise(new IsPraise("1",classList.getGroupsid(),LCConstant.userinfo.getUserid()))){
                        clickPraise(classList);
                    }else{
                        showToast("点过赞了！");
                    }
                } else {
                    Intent intent = new Intent(context,
                            LCUserLoginAndRegister.class);
                    intent.putExtra("mIntent",7);

                    context.startActivity(intent);
                }
            }
        });
    }

    /**
     * 点赞d
     */
    public void clickPraise(final FindList classList) {
        AbRequestParams params = new AbRequestParams();
        params.put("userid", LCConstant.userinfo.getUserid() + "");
        params.put("teamid", classList.getGroupsid());
        params.put("uuid", LCUtils.getOnly(context));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.TEAM_WORK_SHOW_PRAISE, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        Praise praise = JSONUtils.getInstatce().getPraise(
                                content);
                        if ("0".equals(praise.getErrorCode())) {
                            setTitle(lc_findinfo_praisecount,
                                    praise.getNumber());
                            try {
                                dbhelper.add(new IsPraise("1",classList.getGroupsid(),LCConstant.userinfo.getUserid()));
                                AbToastUtil.showToast(context, "+1");
                                if (findRefresh != null) {
                                    findRefresh.refresh(position);
                                }
                            } catch (Exception e) {
                              showToast("点过赞了！");
                            }

                        } else {
                            AbToastUtil.showToast(context,
                                    praise.getErrorMessage());
                            LCUtils.ReLogin(praise.getErrorCode(), context,praise.getErrorMessage());
                        }

                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        // AbToastUtil.showToast(context, content);
                        L.e(content+statusCode+"---------onFailure-------------");
                        isNoNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(getContext());
                    }

                });
    }

    private void setTitle(TextView lc_class_title, String videoclassname) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(videoclassname)) {
            lc_class_title.setText(videoclassname);
        } else {
            lc_class_title.setText("0");
        }
    }

    private void setIcon(ImageView lc_class_icon, String videoclassimg) {
        // TODO Auto-generated method stub
        try {
            if (!TextUtils.isEmpty(videoclassimg)) {

                LCUtils.mImageloader(videoclassimg, lc_class_icon, context);
            } else {
                lc_class_icon.setImageResource(R.drawable.image_error);
            }
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        releaseImageView(lc_findinfo_icon);

    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.lc_findinfo_imageone:
                Intent intent = new Intent(LCFindInfoPraise.this, LCFindInfo.class);
                intent.putExtra("findlist", findList);
                intent.putExtra("select", 1);
                intent.putExtra("position", position);
                startActivity(intent);
                break;
            case R.id.lc_findinfo_imagetwo:
                Intent intent1 = new Intent(LCFindInfoPraise.this, LCFindInfo.class);
                intent1.putExtra("findlist", findList);
                intent1.putExtra("select", 2);
                intent1.putExtra("position", position);
                startActivity(intent1);

                break;
            case R.id.lc_findinfo_imagethree:
                Intent intent2 = new Intent(LCFindInfoPraise.this, LCFindInfo.class);
                intent2.putExtra("findlist", findList);
                intent2.putExtra("select", 3);
                intent2.putExtra("position", position);
                startActivity(intent2);
                break;
            case R.id.lc_findinfo_imagefour:
                Intent intent3 = new Intent(LCFindInfoPraise.this, LCFindInfo.class);
                intent3.putExtra("findlist", findList);
                intent3.putExtra("select", 4);
                intent3.putExtra("position", position);
                startActivity(intent3);
                break;
            case R.id.lc_findinfo_imagefive:
                Intent intent4 = new Intent(LCFindInfoPraise.this, LCFindInfo.class);
                intent4.putExtra("findlist", findList);
                intent4.putExtra("select", 5);
                intent4.putExtra("position", position);
                startActivity(intent4);
                break;
        }
        finish();
    }

}
