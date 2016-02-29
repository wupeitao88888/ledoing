package cn.ledoing.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ledoing.activity.LCMeCourse;
import cn.ledoing.activity.R;
import cn.ledoing.bean.CancelReasonList;
import cn.ledoing.bean.OfflineCourses;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.model.LCCheckChangeCallBack;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCDialog;

public class LCOfflineCoursesAdapter extends BaseAdapter {
    private Context context;
    private List<OfflineCourses> list;
    private AbHttpUtil mAbHttpUtil = null;
    private List<CancelReasonList> listReason = new ArrayList<CancelReasonList>();
    public CharSequence temp;

    public LCOfflineCoursesAdapter(Context context, List<OfflineCourses> list2) {
        super();
        this.context = context;
        this.list = list2;
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
    }

    public void setListReason(List<CancelReasonList> listReason) {
        this.listReason = listReason;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        OfflineCourses classList = list.get(position);

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.lc_coursesrecord_items, null);
            vh = new ViewHolder();
            vh.class_address_text = (TextView) convertView
                    .findViewById(R.id.class_address_text);
            vh.class_state_text = (TextView) convertView
                    .findViewById(R.id.class_state_text); // stast
            vh.teacher_name_text = (TextView) convertView
                    .findViewById(R.id.teacher_name_text);
            vh.class_time_text = (TextView) convertView
                    .findViewById(R.id.class_time_text);
            vh.class_name_text = (TextView) convertView
                    .findViewById(R.id.class_name_text);
//            vh.class_start_time_text = (TextView) convertView
//                    .findViewById(R.id.class_start_time_text);
            vh.booking_time_text = (TextView) convertView
                    .findViewById(R.id.booking_time_text);
            vh.lc_courses_prise = (Button) convertView.findViewById(R.id.lc_courses_prise);
            vh.lc_courses_cancel = (Button) convertView.findViewById(R.id.lc_courses_cancel);
            vh.stast_tip_bg = (View) convertView.findViewById(R.id.stast_tip_bg);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        setCourses(vh.class_address_text, classList.getInstitution());
        setCourses(vh.teacher_name_text, classList.getTeacher_name());
        setCourses(vh.class_time_text, classList.getPrice());
        setCourses(vh.class_name_text, classList.getBase_name()+" "+classList.getCourse_name());
//        setCourses(vh.class_start_time_text, "预约时间："+classList.getCreate_at());
        setCourses(vh.booking_time_text, "上课时间：" + AbDateUtil.getStringByFormat(classList.getStart_time(), "yyyy-MM-dd HH:mm"));
        setCoursesOnClick(vh.lc_courses_prise, classList.getId(), classList, position);
        setCoursesOnClick(vh.lc_courses_cancel, classList.getId(), classList, position);
        switch (Integer.parseInt(classList.getStatus())){
            case 1:
                vh.lc_courses_prise.setVisibility(View.GONE);
                vh.lc_courses_cancel.setVisibility(View.VISIBLE);
                vh.class_state_text.setVisibility(View.VISIBLE);
                vh.stast_tip_bg.setVisibility(View.GONE);
                setCourses(vh.class_state_text, "待上课");
                break;
            case 2:
                vh.lc_courses_prise.setVisibility(View.GONE);
                vh.lc_courses_cancel.setVisibility(View.GONE);
                vh.class_state_text.setVisibility(View.VISIBLE);
                vh.stast_tip_bg.setVisibility(View.GONE);
                setCourses(vh.class_state_text, "已上课");
                break;
            /*case 3:
                vh.lc_courses_prise.setVisibility(View.GONE);
                vh.lc_courses_cancel.setVisibility(View.GONE);
                vh.class_state_text.setVisibility(View.GONE);
                vh.stast_tip_bg.setVisibility(View.VISIBLE);
                vh.stast_tip_bg.setBackgroundResource(R.drawable.course_stast_cancelled);
                break;*/
            case 6:
                vh.lc_courses_prise.setVisibility(View.GONE);
                vh.lc_courses_cancel.setVisibility(View.GONE);
                vh.class_state_text.setVisibility(View.GONE);
                vh.stast_tip_bg.setVisibility(View.VISIBLE);
                vh.stast_tip_bg.setBackgroundResource(R.drawable.course_stast_completed);
                break;
            case 5:
                vh.lc_courses_prise.setVisibility(View.GONE);
                vh.lc_courses_cancel.setVisibility(View.GONE);
                vh.class_state_text.setVisibility(View.GONE);
                vh.stast_tip_bg.setVisibility(View.VISIBLE);
                vh.stast_tip_bg.setBackgroundResource(R.drawable.course_stast_expired);
                break;
        }
        return convertView;
    }

    private void setCoursesOnClick(final Button lc_courses_prise, final String id, final OfflineCourses classList, final int position) {
        lc_courses_prise.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(classList.getStatus())) {
                    if( "2".equals(classList.getCancelable()) ){
                        // 显示高亮，提示要扣乐豆。
                        Toast.makeText(context,"距离上课时间少于12小时,不能取消",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showDialog(classList,position);
                } else if ("2".equals(classList.getStatus())) {
                    clickPraise(id, classList);
                }
            }
        });
    }

    private void setCourses(final TextView lc_courses_day, final String lc_courses_day1) {

        if (TextUtils.isEmpty(lc_courses_day1)) {
            lc_courses_day.setText("");
            return;
        }
        lc_courses_day.setText(lc_courses_day1);
    }

    /**
     * 销课
     */
    private void clickPraise(String id, final OfflineCourses classList) {
        // 绑定参数

        AbRequestParams params = new AbRequestParams();
        params.put("reservation_id", id);
        mAbHttpUtil.postUrl(LCConstant.VERSION_UPDATERECORD, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("-------" + statusCode + "---------" + content + "---------------");
                        String praise = JSONUtils.getInstatce().loadConvert(content);
                        L.e(praise);
                        try {
                            JSONObject jo = new JSONObject(praise);
                            String errorCode = jo.optString("errorCode", "");
                            String errorMessage = jo.optString("errorMessage", "");
                            if ("0".equals(errorCode)) {
                                classList.setStatus("6");
                                notifyDataSetChanged();
                            } else {
//                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                LCUtils.ReLogin(errorCode, context, errorMessage);
                            }
                        } catch (JSONException e) {
//                            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
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
//                        AbToastUtil.showToast(context, statusCode);
                        L.e("" + statusCode + "::" + content);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {

                        LCUtils.stopProgressDialog(context);
                    }


                });
    }

    private void showDialog(final OfflineCourses classList, final int position) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.lc_abolish_dialog,
                null);
        final LCDialog dialog = new LCDialog(context, R.style.MyDialog, dialogView);
        dialog.show();
        final Map<String, String> choose = new HashMap<String, String>();
        ImageView lc_dialog_cancel = (ImageView) dialogView.findViewById(R.id.lc_dialog_cancel);
        ListView Lc_dialog_reasonChose = (ListView) dialogView.findViewById(R.id.Lc_dialog_reasonChose);
        Lc_dialog_reasonChose.setOverScrollMode(View.OVER_SCROLL_NEVER);
        View dialogV = LayoutInflater.from(context).inflate(R.layout.lc_layout_footview,
                null);
        EditText lc_dialog_reasonOther = (EditText) dialogV.findViewById(R.id.lc_dialog_reasonOther);
        final Button lc_dialog_confirmCancel = (Button) dialogV.findViewById(R.id.lc_dialog_confirmCancel);
        Lc_dialog_reasonChose.addFooterView(dialogV);
        LCReasonAdapter reasonAdapter=new LCReasonAdapter(context,listReason,choose);
        Lc_dialog_reasonChose.setAdapter(reasonAdapter);
        reasonAdapter.setCheckChangeListener(new LCCheckChangeCallBack() {
            @Override
            public void onChange(Map<String, String> choose) {
                if (choose.size() > 0) {
                    lc_dialog_confirmCancel.setEnabled(true);
                } else {
                    lc_dialog_confirmCancel.setEnabled(false);
                }
            }
        });
        lc_dialog_reasonOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (temp.length() > 0) {
                    choose.put("five", temp.toString());
                } else {
                    choose.remove("five");
                }
                if (choose.size() > 0) {
                    lc_dialog_confirmCancel.setEnabled(true);
                } else {
                    lc_dialog_confirmCancel.setEnabled(false);
                }
            }
        });
        lc_dialog_confirmCancel.setEnabled(false);
        lc_dialog_confirmCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer reason = new StringBuffer();
                reason.append("{\"reason\":[");
                for (int i = 0; i < listReason.size(); i++) {
                    if (!TextUtils.isEmpty(choose.get(i + ""))) {
                        reason.append("\"" + choose.get(i + "") + "\"" + ",");
                    }
                }
                reason.append("]").append("\"otherReason\":").append("\"" + choose.get("five") + "\"").append("}");

                AbRequestParams params = new AbRequestParams();
                params.put("reservation_id", classList.getId());
                params.put("reason", reason.toString());
                AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(context);
                mAbHttpUtil.setTimeout(5000);
                mAbHttpUtil.postUrl(LCConstant.RE_CANCEL, params,
                        new AbStringHttpResponseListener() {

                            // 获取数据成功会调用这里
                            @Override
                            public void onSuccess(int statusCode, String content) {
                                L.e("-------" + statusCode + "---------" + content + "---------------");
                                String praise = JSONUtils.getInstatce().loadConvert(content);
                                L.e(praise);
                                try {
                                    JSONObject jo = new JSONObject(praise);
                                    String errorCode = jo.optString("errorCode", "");
                                    String errorMessage = jo.optString("errorMessage", "");
                                    if ("0".equals(errorCode)) {
                                        list.remove(position);
                                        LCMeCourse.isRefresh = true;
                                        notifyDataSetChanged();
                                        Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT).show();
                                    } else {
                                        LCUtils.ReLogin(errorCode, context, errorMessage);
                                    }
                                } catch (JSONException e) {
//                            Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
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
                                L.e("" + statusCode + "::" + content);
                                Toast.makeText(context,"取消失败",Toast.LENGTH_SHORT).show();
                            }

                            // 完成后调用，失败，成功
                            @Override
                            public void onFinish() {

                                LCUtils.stopProgressDialog(context);
                            }


                        });

                dialog.cancel();
            }
        });
        lc_dialog_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    class ViewHolder {
        private TextView class_address_text, class_state_text, teacher_name_text, class_time_text, class_name_text,
                booking_time_text;
        private Button lc_courses_prise, lc_courses_cancel;
        private View stast_tip_bg;

    }
}
