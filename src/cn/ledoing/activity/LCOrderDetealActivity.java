package cn.ledoing.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.ledoing.bean.OrderDetail;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;

/**
 * Created by cheers on 2015/9/22.
 */
public class LCOrderDetealActivity extends LCActivitySupport {
    private Context mContext;
    private Button orderCenter;
    private Button sure;
    private LCTitleBar lc_title;
    private TextView titleName;
    private TextView centerTitle;
    private TextView time;
    private TextView teacher;
    private TextView price;
    OrderDetail orderDetail;
    private ImageView lc_left_back;
    private TextView call;
    private AbHttpUtil mAbHttpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        mContext = this;
        initView();
    }

    private void initTitle() {
        lc_title = (LCTitleBar) findViewById(R.id.lc_title);
        lc_title.setCenterTitle("订单详情");
        lc_title.setCenterTitleColor(getResources().getColor(R.color.white));
        lc_title.setBackGb(getResources().getColor(R.color.titlebar_activity));
//        lc_title.setLeftImage(R.drawable.goback);
        lc_title.isLeftVisibility(false);
    }

    private void initView() {
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        initTitle();
        titleName = (TextView) findViewById(R.id.textView2);
        centerTitle = (TextView) findViewById(R.id.center_title);
        time = (TextView) findViewById(R.id.time);
        teacher = (TextView) findViewById(R.id.teacher);
        price = (TextView) findViewById(R.id.price);
        orderCenter = (Button) findViewById(R.id.order_center);
        call = (TextView) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = showDialogTwoButton(null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-0068-617"));
                        //通知activtity处理传入的call服务
                        startActivity(intent);
                        dialog.cancel();
                    }
                }, "确定拨打：400-0068-617", "取消", "拨打");
            }
        });
        sure = (Button) findViewById(R.id.sure);
        lc_left_back = (ImageView) findViewById(R.id.lc_left_back);
        lc_left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        orderCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(context, LCCoursesRecord.class);
//                intent.putExtra("isGoHome", 1);
//                startActivity(intent);
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrder();
            }
        });
        getOrderInfo();
    }

    public void setStr(TextView textView, String str) {
        if (TextUtils.isEmpty(str)) {
            textView.setText("");
        } else {
            textView.setText(str);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(context, LCHomeFragmentHost.class);
        startActivity(intent);
    }

    /**
     * 网络请求
     */
    public void getOrderInfo() {
        AbRequestParams params = new AbRequestParams();
        params.put("row_class_id", getIntent().getStringExtra("classid"));
        mAbHttpUtil.postUrl(LCConstant.RE_GET_ORDER_DETAIL, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("=======4.5====statusCode==" + statusCode + "================" + loadConvert(content));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(loadConvert(content));
                            if (jsonObject.optString("errorCode").equals("0")) {
                                orderDetail = new OrderDetail(jsonObject.optJSONObject("data"));
                                if (!TextUtils.isEmpty(orderDetail.getBase_name()) && orderDetail.getBase_name().length() > 3) {
                                    setStr(titleName, orderDetail.getBase_name().substring(4));
                                }
                                setStr(centerTitle, orderDetail.getInstitution());
                                setStr(time, AbDateUtil.getStringByFormat(orderDetail.getStart_time(),"yyyy-MM-dd HH:mm") + "-" + AbDateUtil.getStringByFormat(orderDetail.getEnd_time(), "HH:mm"));                                setStr(teacher, orderDetail.getMastar_teacher_name());
                                setStr(price, "¥" + orderDetail.getPrice());
                            } else {
                                showToast(jsonObject.optString("errorMessage"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context, "正在加载...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.5====statusCode==" + statusCode + "================" + content);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }
                });
    }

    /**
     * 网络请求
     */
    public void saveOrder() {
        AbRequestParams params = new AbRequestParams();
        params.put("classid", getIntent().getStringExtra("classid"));
        params.put("taskid", getIntent().getStringExtra("taskid"));
//        params.put("taskid", 28+"");

        mAbHttpUtil.postUrl(LCConstant.SAVE_USER_CLASS, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("=======4.5====statusCode==" + statusCode + "================" + loadConvert(content));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(loadConvert(content));
                            if (jsonObject.optString("errorCode").equals("0")) {
                                Intent intent = new Intent(context, LCOfflineCourses.class);
                                intent.putExtra("timeall", JSONUtils.getInstatce().getTimeAll());
                                intent.putExtra("isGoHome", 1);
                                startActivity(intent);
                                showToast("预约成功！");
                            } else {
                                showToast(jsonObject.optString("errorMessage"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context, "正在预约...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.5====statusCode==" + statusCode + "================" + content);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }
                });
    }
}
