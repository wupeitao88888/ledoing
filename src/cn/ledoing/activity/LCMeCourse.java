package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ledoing.bean.SingleHttpBean;
import cn.ledoing.bean.TimAll;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;

/**
 * wpt
 * 2015/9/15  14:40
 * 我的课程
 */
public class LCMeCourse extends LCActivitySupport implements View.OnClickListener {
    private ViewStub lc_mecourse_money,//j金额
            lc_mecourse_course,lc_mecourse_dddd;//课程
    private RelativeLayout Lc_mecourse_onlineCourse,//线上课程
            Lc_mecourse_offCourse,//线下课程
            Lc_mecourse_record,// 销课记录
            Lc_mecourse_residueMoney,//剩余金额
            Lc_mecourse_giveMoney,//赠送金额
            Lc_mecourse_giveCourse,//赠送课程
            Lc_mecourse_residueCourse;//剩余课程
    private LCTitleBar lc_mecourse_title;
    private AbHttpUtil mAbHttpUtil;
    private TextView Lc_mecourse_leftResidueMoney; // 剩余金额
    private TextView Lc_mecourse_rightGiveMoney; // 赠送金额
    private TextView Lc_mecourse_leftResidueCourse; // 剩余课时
    private TextView Lc_mecourse_rightGiveCourse; // 赠送课时
    public static boolean isRefresh; // 是否刷新课时
    private TimAll timeall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_mecourse);
        initView();
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        getMyCourse();    }

    private void initView() {
        lc_mecourse_title=(LCTitleBar)findViewById(R.id.lc_mecourse_title);
        lc_mecourse_title.setCenterTitle(mString(R.string.allclass));

        lc_mecourse_dddd= (ViewStub) findViewById(R.id.lc_mecourse_dddd);
        lc_mecourse_dddd.inflate();
        lc_mecourse_money = (ViewStub) findViewById(R.id.lc_mecourse_money);
        lc_mecourse_course = (ViewStub) findViewById(R.id.lc_mecourse_course);
        Lc_mecourse_onlineCourse = (RelativeLayout) findViewById(R.id.Lc_mecourse_onlineCourse);
        Lc_mecourse_offCourse = (RelativeLayout) findViewById(R.id.Lc_mecourse_offCourse);
        Lc_mecourse_record = (RelativeLayout) findViewById(R.id.Lc_mecourse_record);
        Lc_mecourse_onlineCourse.setOnClickListener(this);
        Lc_mecourse_offCourse.setOnClickListener(this);
        Lc_mecourse_record.setOnClickListener(this);
    }

    /**
     * 加载金额ViewStub
     */
    private void loadMoneyView() {
        try {
            lc_mecourse_money.inflate();
            Lc_mecourse_residueMoney = (RelativeLayout) findViewById(R.id.Lc_mecourse_residueMoney);
            Lc_mecourse_giveMoney = (RelativeLayout) findViewById(R.id.Lc_mecourse_giveMoney);
            Lc_mecourse_leftResidueMoney = (TextView) findViewById(R.id.Lc_mecourse_leftResidueMoney);
            Lc_mecourse_rightGiveMoney = (TextView) findViewById(R.id.Lc_mecourse_rightGiveMoney);
            Lc_mecourse_giveMoney.setOnClickListener(this);
            Lc_mecourse_residueMoney.setOnClickListener(this);
        }catch (Exception e){

        }
    }

    /**
     * 加载课程ViewStub
     */
    private void loadCourseView() {
        try {
            lc_mecourse_course.inflate();
            Lc_mecourse_giveCourse = (RelativeLayout) findViewById(R.id.Lc_mecourse_giveCourse);
            Lc_mecourse_residueCourse = (RelativeLayout) findViewById(R.id.Lc_mecourse_residueCourse);
            Lc_mecourse_leftResidueCourse = (TextView) findViewById(R.id.Lc_mecourse_leftResidueCourse);
            Lc_mecourse_rightGiveCourse = (TextView) findViewById(R.id.Lc_mecourse_rightGiveCourse);
            Lc_mecourse_giveCourse.setOnClickListener(this);
            Lc_mecourse_residueCourse.setOnClickListener(this);
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Lc_mecourse_onlineCourse:
                Intent iStudyRecord = new Intent(context, LCStudyRecord.class);
                startActivity(iStudyRecord);
                break;
            case R.id.Lc_mecourse_offCourse:
                if( timeall == null ){
                    timeall = JSONUtils.getInstatce().getTimeAll();
                }
                Intent offlineIntent = new Intent(context, LCOfflineCourses.class);
                offlineIntent.putExtra("timeall", timeall);
                startActivityForResult(offlineIntent, 1);
                break;
            case R.id.Lc_mecourse_record:
                    if( timeall == null ){
                        timeall = JSONUtils.getInstatce().getTimeAll();
                    }
                    Intent recordIntent = new Intent(context, LCCoursesRecord.class);
                    recordIntent.putExtra("timeall", timeall);
                    startActivityForResult(recordIntent, 1);
                break;
            case R.id.Lc_mecourse_residueMoney:
                break;
            case R.id.Lc_mecourse_giveMoney:
                break;
            case R.id.Lc_mecourse_giveCourse:
                break;
            case R.id.Lc_mecourse_residueCourse:
                break;
        }
    }

    private void getMyCourse() {
        AbRequestParams params = new AbRequestParams();
        params.put("platform", "android");
        params.put("time", LCUtils.gettime().substring(0, 10));
        params.put("uuid", LCUtils.getOnly(this));
        String url = "/api/v2/re-get-user-hours";
        mAbHttpUtil.post(LCConstant.URL + url, params,
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
                        showToast("网络连接失败");
//                        isNoNet();
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("=======4.1====statusCode==" + statusCode + "================" + content);
                        SingleHttpBean singlehttp = JSONUtils.getInstatce().getUserHoursOver(content, LCMeCourse.this);
//                        if ( !TextUtils.isEmpty(singlehttp.getMoney_amount()) && (0 < Float.parseFloat(singlehttp.getMoney_amount()) || 0 < Float.parseFloat(singlehttp.getGive_money_amount())) ) {
                            loadMoneyView();
                            Lc_mecourse_leftResidueMoney.setText(singlehttp.getMoney_amount()); // 剩余金额
                            Lc_mecourse_rightGiveMoney.setText(singlehttp.getGive_money_amount()); // 赠送金额
//                        }
                        if ( !TextUtils.isEmpty(singlehttp.getCourse_hours()) && (0 < Float.parseFloat(singlehttp.getCourse_hours()) || 0 < Float.parseFloat(singlehttp.getGive_course_hours())) ) {
                            loadCourseView();
                            Lc_mecourse_leftResidueCourse.setText(singlehttp.getCourse_hours()); // 剩余课时
                            Lc_mecourse_rightGiveCourse.setText(singlehttp.getGive_course_hours()); // 赠送课时
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if(isRefresh){
            getMyCourse();
            isRefresh = false;
        }
    }
}
