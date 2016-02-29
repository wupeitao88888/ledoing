package cn.ledoing.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.ledoing.adapter.LCActivityDetailsListAdapter;
import cn.ledoing.adapter.PopListAdapter;
import cn.ledoing.bean.ActivityDetailsListBean;
import cn.ledoing.bean.LCTeacherBean;
import cn.ledoing.bean.SingleHttpBean;
import cn.ledoing.bean.ThemeGroup;
import cn.ledoing.global.LCApplication;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCDialog;


/**
 * Created by cheers on 2015/9/8.
 */
public class LCActivityDetailsListActivity extends LCActivitySupport implements View.OnClickListener {

    private ListView xListView;
    private LCActivityDetailsListAdapter adapter;
    private ArrayList<ActivityDetailsListBean> arrayList = new ArrayList<ActivityDetailsListBean>();
    private Context mContext;
    private GridView weeklist;
    private TextView textweek, texttime, textteacher;
    private ArrayList<LCTeacherBean> arrayListWeek = new ArrayList<LCTeacherBean>();
    private ArrayList<LCTeacherBean> arrayListTime = new ArrayList<LCTeacherBean>();
    private ArrayList<LCTeacherBean> arrayListTeacher = new ArrayList<LCTeacherBean>();
    private AbHttpUtil mAbHttpUtil;
    private LinearLayout layoutweek;
    private LinearLayout layouttime;
    private LinearLayout layoutteacher;
    private View conentView;
    private boolean isThree;
    private ImageView back;
    private String institution_id;
    private int isShow = 0;
    private TextView weekInfo;
    private TextView timeInfo;
    private TextView teacherInfo;
    AddPopWindow addPopWindow;
    private String week_day = "0";
    private String time_dur = "0";
    private String master_teacher_id = "0";
    private TextView title;
    private TextView subtitle;
    private ActivityDetailsListBean bean;
    private Button theme_btn;
    private Button time_btn;
    private String order_by_name = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_details);
        mContext = this;
        initView();
        //机构id
        institution_id = getIntent().getStringExtra("institution_id");
        //教师id
        master_teacher_id = getIntent().getStringExtra("teacher_id");
        getOrderList();
        getTeacherList();
    }

    private void initView() {
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        initTitle();
        xListView = (ListView) findViewById(R.id.xListView);
        initArrayListData();
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bean = arrayList.get(position);
                checkMoney(bean.getClass_id());

            }
        });
        textweek = (TextView) findViewById(R.id.week);
        texttime = (TextView) findViewById(R.id.time);
        textteacher = (TextView) findViewById(R.id.teacher);
        weekInfo = (TextView) findViewById(R.id.week_info);
        timeInfo = (TextView) findViewById(R.id.time_info);
        teacherInfo = (TextView) findViewById(R.id.teacher_info);
        layoutweek = (LinearLayout) findViewById(R.id.layout_week);
        layouttime = (LinearLayout) findViewById(R.id.layout_time);
        layoutteacher = (LinearLayout) findViewById(R.id.layout_teacher);
        theme_btn = (Button) findViewById(R.id.theme_btn);
        time_btn = (Button) findViewById(R.id.time_btn);
        theme_btn.setSelected(true);
        layoutweek.setOnClickListener(this);
        layouttime.setOnClickListener(this);
        layoutteacher.setOnClickListener(this);
        theme_btn.setOnClickListener(this);
        time_btn.setOnClickListener(this);
        if (!TextUtils.isEmpty(getIntent().getStringExtra("teacher_name"))) {
            teacherInfo.setText(getIntent().getStringExtra("teacher_name"));
        }
    }

    private void initArrayListData() {
        arrayListWeek.add(new LCTeacherBean("0", "全部"));
        arrayListWeek.add(new LCTeacherBean("1", "星期一"));
        arrayListWeek.add(new LCTeacherBean("2", "星期二"));
        arrayListWeek.add(new LCTeacherBean("3", "星期三"));
        arrayListWeek.add(new LCTeacherBean("4", "星期四"));
        arrayListWeek.add(new LCTeacherBean("5", "星期五"));
        arrayListWeek.add(new LCTeacherBean("6", "星期六"));
        arrayListWeek.add(new LCTeacherBean("7", "星期日"));

        arrayListTime.add(new LCTeacherBean("0", "全部"));
        arrayListTime.add(new LCTeacherBean("8-10", "8:00-10:00"));
        arrayListTime.add(new LCTeacherBean("10-12", "10:00-12:00"));
        arrayListTime.add(new LCTeacherBean("12-14", "12:00-14:00"));
        arrayListTime.add(new LCTeacherBean("14-16", "14:00-16:00"));
        arrayListTime.add(new LCTeacherBean("16-18", "16:00-18:00"));
        arrayListTime.add(new LCTeacherBean("18-20", "18:00-20:00"));
    }

    private void initTitle() {
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        if (!TextUtils.isEmpty(categoryname))
            title.setText(categoryname.substring(4));
        else {
            title.setText("");
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("center_name")))
            subtitle.setText(getIntent().getStringExtra("center_name"));
        else {
            subtitle.setText("");
        }
        back = (ImageView) findViewById(R.id.lc_left_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showWeekList() {
        isThree = true;
        addPopWindow = new AddPopWindow(LCActivityDetailsListActivity.this, arrayListWeek, isThree);
        addPopWindow.showPopupWindow(layoutweek);
        addPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackground();
            }
        });
    }

    public void showTimeList() {
        isThree = false;
        addPopWindow = new AddPopWindow(LCActivityDetailsListActivity.this, arrayListTime, isThree);
        addPopWindow.showPopupWindow(layoutweek);
        addPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackground();
            }
        });
    }

    public void showTeacherList() {
        isThree = false;
        addPopWindow = new AddPopWindow(LCActivityDetailsListActivity.this, arrayListTeacher, isThree);
        addPopWindow.showPopupWindow(layoutweek);
        addPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackground();
            }
        });
    }


    @Override
    public void onClick(View v) {
        Drawable drawable = getResources().getDrawable(R.drawable.text_right_selcet);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        switch (v.getId()) {

            case R.id.layout_week:
                isShow = 1;
                setBackground();
                textweek.setTextColor(getResources().getColor(R.color.header_selected));
                textweek.setCompoundDrawables(null, null, drawable, null);
                showWeekList();
                break;
            case R.id.layout_time:
                isShow = 2;
                setBackground();
                texttime.setTextColor(getResources().getColor(R.color.header_selected));
                texttime.setCompoundDrawables(null, null, drawable, null);
                showTimeList();
                break;
            case R.id.layout_teacher:
                isShow = 3;
                setBackground();
                textteacher.setTextColor(getResources().getColor(R.color.header_selected));
                textteacher.setCompoundDrawables(null, null, drawable, null);
                showTeacherList();
                break;
            case R.id.theme_btn:
                order_by_name = "1";
                theme_btn.setSelected(true);
                if (time_btn.isSelected()) {
                    time_btn.setSelected(false);
                }
                getOrderList();
                break;
            case R.id.time_btn:
                order_by_name = "0";
                time_btn.setSelected(true);
                if (theme_btn.isSelected()) {
                    theme_btn.setSelected(false);
                }
                getOrderList();
                break;
        }
    }

    public void setBackground() {

        Drawable drawable = getResources().getDrawable(R.drawable.text_right);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        textweek.setCompoundDrawables(null, null, drawable, null);
        texttime.setCompoundDrawables(null, null, drawable, null);
        textteacher.setCompoundDrawables(null, null, drawable, null);
        textweek.setTextColor(getResources().getColor(R.color.header));
        texttime.setTextColor(getResources().getColor(R.color.header));
        textteacher.setTextColor(getResources().getColor(R.color.header));
    }


    public void getOrderList() {
        AbRequestParams params = new AbRequestParams();
        params.put("category_id", categoryid);
        params.put("institution_id", institution_id);
        params.put("week_day", week_day);
        params.put("time_dur", time_dur);
        params.put("master_teacher_id", master_teacher_id);
        params.put("order_by_name", order_by_name);
        mAbHttpUtil.postUrl(LCConstant.RE_GET_ONLINE_CLASS_LIST, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        try {
                            L.e("=======4.4====statusCode==" + statusCode + "================" + loadConvert(content));
                            JSONObject jsonObject = new JSONObject(loadConvert(content));
                            if (jsonObject.optString("errorCode").equals("0")) {
                                JSONArray jsonArray = jsonObject.optJSONObject("data").optJSONArray("list");
                                if (jsonArray != null & jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ActivityDetailsListBean activityBean = new ActivityDetailsListBean(jsonArray.optJSONObject(i));
                                        arrayList.add(activityBean);
                                    }
                                    setValue();
                                } else {
                                    showToast("无数据");
                                    if (adapter != null)
                                        adapter.notifyDataSetChanged();
                                }
                            } else {
                                showToast("无数据");
                                if (adapter != null)
                                    adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context);
                    }
                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.4====statusCode==" + statusCode + "================" + content);
                        Intent intent = new Intent(context, LCNONetWork.class);
                        startActivityForResult(intent, 101);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }
                });
    }

    public void getTeacherList() {
        AbRequestParams params = new AbRequestParams();
        params.put("institution_id", institution_id);
        params.put("category_id", categoryid);

        mAbHttpUtil.postUrl(LCConstant.RE_GET_TEACHER_NAME_LIST, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        try {
                            L.e("=======4.4====statusCode==" + statusCode + "================" + loadConvert(content));
                            JSONObject jsonObject = new JSONObject(loadConvert(content));
                            if (jsonObject.optString("errorCode").equals("0")) {
                                JSONArray jsonArrayTeacher = jsonObject.optJSONObject("data").optJSONArray("list");
                                arrayListTeacher.add(new LCTeacherBean("0", "全部"));
                                if (jsonArrayTeacher != null & jsonArrayTeacher.length() > 0) {
                                    for (int i = 0; i < jsonArrayTeacher.length(); i++) {
                                        LCTeacherBean teacher = new LCTeacherBean(jsonArrayTeacher.optJSONObject(i));
                                        arrayListTeacher.add(teacher);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.4====statusCode==" + statusCode + "================" + content);
                        Intent intent = new Intent(context, LCNONetWork.class);
                        startActivityForResult(intent, 101);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // LCUtils.stopProgressDialog();
                    }
                });
    }


    private void setValue() {
        adapter = new LCActivityDetailsListAdapter(context, arrayList);
        xListView.setAdapter(adapter);
    }

    //POPWINDOW  class
    public class AddPopWindow extends PopupWindow {
        private View conentView;
        GridView gridView;
        PopListAdapter adapter1;
        private LinearLayout gridviewBottom;

        public AddPopWindow(final Activity context, ArrayList<LCTeacherBean> list_data, boolean isSetThreeNum) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            conentView = inflater.inflate(R.layout.popwindow_gridview, null);
            int h = context.getWindowManager().getDefaultDisplay().getHeight();
            int w = context.getWindowManager().getDefaultDisplay().getWidth();
            // 设置SelectPicPopupWindow的View
            this.setContentView(conentView);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(w);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            this.setOutsideTouchable(true);
            // 刷新状态
            this.update();
            // 实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0000000000);
            // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
            this.setBackgroundDrawable(dw);
            // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
            // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationPreview);
            gridView = (GridView) conentView
                    .findViewById(R.id.gridView);
            gridviewBottom = (LinearLayout) conentView.findViewById(R.id.gridView_bottom);
            gridviewBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addPopWindow.dismiss();
                }
            });
            if (isSetThreeNum) {
                gridView.setNumColumns(3);
            } else {
                gridView.setNumColumns(2);
            }
            adapter1 = new PopListAdapter(context, list_data);
            gridView.setAdapter(adapter1);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter1.selectPosition(position);
                    addPopWindow.dismiss();
                    if (isShow == 1) {
                        weekInfo.setText(arrayListWeek.get(position).getTeacherName());
                        week_day = arrayListWeek.get(position).getTeacherID();
                    } else if (isShow == 2) {
                        timeInfo.setText(arrayListTime.get(position).getTeacherName());
                        time_dur = arrayListTime.get(position).getTeacherID();
                    } else if (isShow == 3) {
                        teacherInfo.setText(arrayListTeacher.get(position).getTeacherName());
                        master_teacher_id = arrayListTeacher.get(position).getTeacherID();
                    }
                    arrayList.clear();
                    getOrderList();
                }
            });
//            addPopWindow.setOnDismissListener(new OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    setBackground();
//                }
//            });
        }

        /**
         * 显示popupWindow
         *
         * @param parent
         */
        public void showPopupWindow(View parent) {
            if (!this.isShowing()) {
                this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 0);
            } else {
                this.dismiss();
            }
        }
    }

    //检查用户余额是否足够
    public void checkMoney(final String row_class_id) {
        AbRequestParams params = new AbRequestParams();
        params.put("row_class_id", row_class_id);
        mAbHttpUtil.postUrl(LCConstant.RE_CHECK_USER_HOUR_AMOUNT, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, final String content) {
                        try {
                            L.e("=======4.4====statusCode==" + statusCode + "================" + loadConvert(content));
                            JSONObject jsonObject = new JSONObject(loadConvert(content));
                            if (jsonObject.optString("errorCode").equals("0")) {
                                String amount_status = jsonObject.optJSONObject("data").optString("amount_status");
                                if ("0".equals(amount_status)) {
                                    if (!bean.getIs_re().equals("1")) {//是否已预约
                                        if (bean.getStatus() == 1) {//正常  可预约
                                            Intent intent = new Intent(context, LCOrderActivity.class);
                                            intent.putExtra("classid", row_class_id);
                                            startActivity(intent);
                                        } else if (bean.getStatus() == 2) {
                                            showToast("预约已满，请选择其他课程！");
                                        } else if (bean.getStatus() == 3) {
                                            showToast("时间冲突！请选择其他时间的课程！");
                                        }
                                    } else {
                                        showToast("您已预约该课程！");
                                    }
                                } else if ("1".equals(amount_status)) {
                                    //不是会员
                                    Intent intent = new Intent(context, MemberActivity.class);
                                    intent.putExtra("no_vip", false);
                                    intent.putExtra("ins_id", institution_id);
                                    startActivity(intent);

                                } else if ("2".equals(amount_status)) {
                                    //没有钱
                                    Intent intent = new Intent(context, MemberActivity.class);
                                    intent.putExtra("no_vip", true);
                                    intent.putExtra("ins_id", institution_id);
                                    startActivity(intent);

                                }

                            } else {
                                if (!TextUtils.isEmpty(jsonObject.optString("errorMessage"))) {
                                    showToast(jsonObject.optString("errorMessage"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context, "正在加载...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.4====statusCode==" + statusCode + "================" + content);
//                        Intent intent = new Intent(context, LCNONetWork.class);
//                        startActivityForResult(intent, 101);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }
                });
    }


    private void getMyCourse(String row_class_id) {
        AbRequestParams params = new AbRequestParams();
        params.put("row_class_id", row_class_id);
        mAbHttpUtil.post(LCConstant.RE_CHECK_USER_HOUR_AMOUNT, params,
                new AbStringHttpResponseListener() {
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(mContext);
                    }

                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(mContext);
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("=======4.1====statusCode==" + statusCode + "================" + content);
                        SingleHttpBean singlehttp = JSONUtils.getInstatce().getUserHoursOver(content, mContext);
                        if ("1".equals(singlehttp.getIs_mumber())) {//是会员


                        } else {//否则不是会员

                        }

                    }
                });

    }
}
