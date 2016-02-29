package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.ledoing.adapter.CommentAdapter;
import cn.ledoing.bean.Comment;
import cn.ledoing.bean.TeacherHome;
import cn.ledoing.bean.TracherHomeNews;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.BoundScrollView;
import cn.ledoing.view.LCRatingBar;
import cn.ledoing.view.LCTitleBar;
import cn.ledoing.view.NoScrollListView;

/**
 * 教师主页
 * Created by wupeitao on 15/11/4.
 */
public class TeacherHomePage extends LCActivitySupport implements View.OnClickListener, BoundScrollView.OverScrollTinyListener {

    private NoScrollListView comment_on;
    private ImageView teacher;
    private CommentAdapter commentAdapter;
    private BoundScrollView b_scrollview;
    private RelativeLayout oneself_trends, arrowhead,//箭头
            appointment_class;//预约课程
    private LinearLayout star_li;
    private LCRatingBar teacher_ratingBar;
    private ImageView oneselftrends_1,//动态1
            oneselftrends_2,
            oneselftrends_3,
            oneselftrends_4,
            teacherpic;

    private TextView teacher_name,//教师名称
            teacher_address,//地址
            surprised_mark,//惊喜
            satisfied_mark,//满意
            ordinary_mark,//一般
            barely_mark,//极差
            oneself,//自我介绍
            lose_mark,//失败
            introduce_briefly;//个人签名

    private ProgressBar surprised,//惊喜
            ordinary,//一般
            lose,//失败
            barely,//极差
            satisfied;//满意
    private ImageView teacher_df;//教师头像

    private AbHttpUtil mAbHttpUtil;
    public TextView give_marks;
    private TextView count_person;
    private String teacher_id;
    private LCTitleBar title_t;

    private TeacherHome teacherHome;
    private String ins_id;
    private TextView trends_count;
    private RelativeLayout re_footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teacher_home_page);
        ins_id = getIntent().getStringExtra("ins_id");
        initView();
    }

    private void initView() {
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        title_t = (LCTitleBar) findViewById(R.id.title_t);
        title_t.setBackGbAlpha(0.1f);
        title_t.setCenterTitle("教师主页");
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_judge_footview, null);
        re_footer=(RelativeLayout)inflate.findViewById(R.id.re_footer);
        comment_on = (NoScrollListView) findViewById(R.id.comment_on);
        comment_on.addFooterView(inflate);
        comment_on.setDivider(null);
        oneself_trends = (RelativeLayout) findViewById(R.id.oneself_trends);
        b_scrollview = (BoundScrollView) findViewById(R.id.b_scrollview);
        appointment_class = (RelativeLayout) findViewById(R.id.appointment_class);
        arrowhead = (RelativeLayout) findViewById(R.id.arrowhead);
        star_li = (LinearLayout) findViewById(R.id.star_li);
        oneselftrends_1 = (ImageView) findViewById(R.id.oneselftrends_1);
        oneselftrends_2 = (ImageView) findViewById(R.id.oneselftrends_2);
        oneselftrends_3 = (ImageView) findViewById(R.id.oneselftrends_3);
        oneselftrends_4 = (ImageView) findViewById(R.id.oneselftrends_4);
        teacherpic = (ImageView) findViewById(R.id.arrowhead_image);
        teacher_df = (ImageView) findViewById(R.id.teacher_df);
        teacher_name = (TextView) findViewById(R.id.teacher_name);
        teacher_address = (TextView) findViewById(R.id.teacher_address);
        surprised_mark = (TextView) findViewById(R.id.surprised_mark);
        satisfied_mark = (TextView) findViewById(R.id.satisfied_mark);
        ordinary_mark = (TextView) findViewById(R.id.ordinary_mark);
        barely_mark = (TextView) findViewById(R.id.barely_mark);
        give_marks = (TextView) findViewById(R.id.give_marks);
        lose_mark = (TextView) findViewById(R.id.lose_mark);
        count_person = (TextView) findViewById(R.id.count_person);
        oneself = (TextView) findViewById(R.id.oneself);
        barely_mark = (TextView) findViewById(R.id.barely_mark);
        introduce_briefly = (TextView) findViewById(R.id.introduce_briefly);
        trends_count = (TextView) findViewById(R.id.trends_count);
        surprised = (ProgressBar) findViewById(R.id.surprised);
        ordinary = (ProgressBar) findViewById(R.id.ordinary);
        lose = (ProgressBar) findViewById(R.id.lose);
        barely = (ProgressBar) findViewById(R.id.barely);
        satisfied = (ProgressBar) findViewById(R.id.satisfied);

        teacher_ratingBar = (LCRatingBar) findViewById(R.id.teacher_ratingBar);

        teacher_ratingBar.setMark(4.5f);
        b_scrollview.setOverScrollTinyListener(this);
        oneself_trends.setOnClickListener(this);
        appointment_class.setOnClickListener(this);
        arrowhead.setOnClickListener(this);
        star_li.setVisibility(View.GONE);
        re_footer.setOnClickListener(this);

        Intent intent = getIntent();
        teacher_id = intent.getStringExtra("teacher_id");
        netTeacherinfo(teacher_id);
        netTrends(teacher_id);
        netComment(teacher_id);
        b_scrollview.setOnScrollListener(new BoundScrollView.OnScrollListener() {
            @Override
            public void onScroll(int l, int t, int oldl, int oldt) {
                float pull = t;
                float all = 100;
                float me = pull / all;
                title_t.setBackGbAlpha(me);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.oneself_trends:
                Intent intent = new Intent(TeacherHomePage.this, TrendsActivity.class);
                intent.putExtra("teacher_id", teacher_id);
                startActivity(intent);
                break;
            case R.id.appointment_class:
                if(null == teacherHome){
                    showToast("网络较慢，稍后点击");
                    return;
                }
                if (!LCConstant.islogin) {
                    Intent login_intent = new Intent(this,
                            LCUserLoginAndRegister.class);
                    login_intent.putExtra("mIntent", 11);
                    login_intent.putExtra("ins_id", ins_id);
                    login_intent.putExtra("teacher_id", teacher_id);
                    login_intent.putExtra("teacher_name", teacherHome.getData().getTeacher_name());
                    startActivity(login_intent);
                }else{
                    Intent about_intent = new Intent(context, LCActivityDetailsListActivity.class);
                    about_intent.putExtra("institution_id", ins_id);
                    about_intent.putExtra("teacher_id", teacher_id);
                    about_intent.putExtra("teacher_name", teacherHome.getData().getTeacher_name());
                    startActivity(about_intent);
                }
                break;
            case R.id.arrowhead:
                if (star_li.getVisibility() == View.GONE) {
                    star_li.setVisibility(View.VISIBLE);
                    teacherpic.setBackground(context.getResources().getDrawable(R.drawable.text_right_selcet));
                } else {
                    star_li.setVisibility(View.GONE);
                    teacherpic.setBackground(context.getResources().getDrawable(R.drawable.text_right));
                }
                break;
            case R.id.re_footer:
                Intent intent_c = new Intent(TeacherHomePage.this, CommentAllActivity.class);
                intent_c.putExtra("teacher_id", teacher_id);
                startActivity(intent_c);
                break;
        }
    }

    @Override
    public void scrollDistance(int tinyDistance, int totalDistance) {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) AbViewUtil.dip2px(context, 293));
        float pull = Math.abs((int) AbViewUtil.dip2px(context, totalDistance));
        float all = Math.abs((int) AbViewUtil.dip2px(context, 173));
        float top = pull / all;
        int top_h = (int) (top * (int) AbViewUtil.dip2px(context, 25)) - (int) AbViewUtil.dip2px(context, 25);
        lp.setMargins(0, top_h, 0, 0);
        teacher_df.setLayoutParams(lp);


    }

    @Override
    public void scrollLoosen() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) AbViewUtil.dip2px(context, 293));
        lp.setMargins(0, (int) AbViewUtil.dip2px(context, -25), 0, 0);
        teacher_df.setLayoutParams(lp);
    }

    //教师信息
    private void netTeacherinfo(String teacherid) {
        // TODO Auto-generated method stub

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("teacherid", teacherid + "");
        mAbHttpUtil.post(LCConstant.TEACHER_HOME, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e(content);
                        teacherHome = JSON.parseObject(content, TeacherHome.class);
                        if ("0".equals(teacherHome.getErrorCode())) {
                            LCUtils.mImageloader(teacherHome.getData().getHead_pic(), teacher_df, context);
                            L.e("头像：" + teacherHome.getData().getHead_pic());
                            LCUtils.setTitle(teacher_name, teacherHome.getData().getTeacher_name());
                            LCUtils.setTitle(introduce_briefly, teacherHome.getData().getTeacher_sign());
                            LCUtils.setTitle(oneself, teacherHome.getData().getTeacher_desc());
                            LCUtils.setTitle(teacher_address, teacherHome.getData().getTeacher_addr());
                            LCUtils.setTitle(give_marks, teacherHome.getData().getScore());
                            LCUtils.setTitle(count_person, teacherHome.getData().getScore_num() + "人");

                            try {
                                LCUtils.setTitle(surprised_mark, teacherHome.getData().getScore_list().get(0) + "%");
                                LCUtils.setTitle(satisfied_mark, teacherHome.getData().getScore_list().get(1) + "%");
                                LCUtils.setTitle(ordinary_mark, teacherHome.getData().getScore_list().get(2) + "%");
                                LCUtils.setTitle(lose_mark, teacherHome.getData().getScore_list().get(3) + "%");
                                LCUtils.setTitle(barely_mark, teacherHome.getData().getScore_list().get(4) + "%");
                                teacher_ratingBar.setMark(Float.parseFloat(teacherHome.getData().getScore()));
                                surprised.setProgress(Integer.parseInt(teacherHome.getData().getScore_list().get(0)));
                                satisfied.setProgress(Integer.parseInt(teacherHome.getData().getScore_list().get(1)));
                                ordinary.setProgress(Integer.parseInt(teacherHome.getData().getScore_list().get(2)));
                                lose.setProgress(Integer.parseInt(teacherHome.getData().getScore_list().get(3)));
                                barely.setProgress(Integer.parseInt(teacherHome.getData().getScore_list().get(4)));
                            } catch (Exception e) {
                                LCUtils.setTitle(surprised_mark, "0%");
                                LCUtils.setTitle(satisfied_mark, "0%");
                                LCUtils.setTitle(ordinary_mark, "0%");
                                LCUtils.setTitle(lose_mark, "0%");
                                LCUtils.setTitle(barely_mark, "0%");
                                teacher_ratingBar.setMark(0);
                                surprised.setProgress(0);
                                satisfied.setProgress(0);
                                ordinary.setProgress(0);
                                lose.setProgress(0);
                                barely.setProgress(0);
                            }
                        } else {
                            LCUtils.ReLogin(teacherHome.getErrorCode(), context, teacherHome.getErrorMessage());
                        }
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框

                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e(statusCode + "++++++++++++++++++++++++" + content);
                        showToast("网络请求失败");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框

                    }
                });
    }

    //教师动态
    private void netTrends(String teacherid) {
        // TODO Auto-generated method stub

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("teacherid", teacherid + "");
        params.put("page", 1 + "");
        params.put("pagesize", 15 + "");
        mAbHttpUtil.post(LCConstant.TEACHER_HOME_NEWS, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("动态：" + content);
                        TracherHomeNews tracherHomeNews = JSON.parseObject(content, TracherHomeNews.class);
                        if ("0".equals(tracherHomeNews.getErrorCode())) {
                            String count = tracherHomeNews.getData().getTotal_count() + "";
                            LCUtils.setTitle(trends_count, count);
                            if (tracherHomeNews.getData().getTotal_count() > 0) {

                                try {
                                    LCUtils.mImageloader(tracherHomeNews.getData().getList().get(0).getNews_pic(), oneselftrends_1, context);
                                } catch (Exception e) {
                                    oneselftrends_1.setVisibility(View.GONE);
                                }
                                try {
                                    LCUtils.mImageloader(tracherHomeNews.getData().getList().get(1).getNews_pic(), oneselftrends_2, context);
                                    oneselftrends_2.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    oneselftrends_2.setVisibility(View.GONE);
                                }
                                try {
                                    LCUtils.mImageloader(tracherHomeNews.getData().getList().get(2).getNews_pic(), oneselftrends_3, context);
                                    oneselftrends_3.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    oneselftrends_3.setVisibility(View.GONE);
                                }
                                try {
                                    LCUtils.mImageloader(tracherHomeNews.getData().getList().get(3).getNews_pic(), oneselftrends_4, context);
                                    oneselftrends_4.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    oneselftrends_4.setVisibility(View.GONE);
                                }

                            }
                        } else {
                            LCUtils.ReLogin(tracherHomeNews.getErrorCode(), context, tracherHomeNews.getErrorMessage());
                        }

                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框

                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        showToast("网络请求失败");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框

                    }
                });


    }

    //教师评论
    private void netComment(String teacherid) {
        // TODO Auto-generated method stub

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("teacherid", teacherid + "");
        params.put("page", 1 + "");
        params.put("pagesize", 15 + "");
        mAbHttpUtil.post(LCConstant.TEACHER_HOME_COMMENTS, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        Comment comment = JSON.parseObject(content, Comment.class);
                        if ("0".equals(comment.getErrorCode())) {
                            List<Comment.Data.ListData> list = comment.getData().getList();
                            commentAdapter = new CommentAdapter(context, list);
                            comment_on.setAdapter(commentAdapter);
                            //解决scrollview嵌套listview不置顶
                            b_scrollview.smoothScrollTo(0, 20);
                        } else {
                            LCUtils.ReLogin(comment.getErrorCode(), context, comment.getErrorMessage());
                        }
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框

                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        showToast("网络请求失败");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框

                    }
                });


    }
}
