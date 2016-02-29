package cn.ledoing.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.activity.StudentCommentActivity;
import cn.ledoing.bean.CoursesRecourd;
import cn.ledoing.bean.FindList;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;

public class LCCoursesRecordAdapter extends BaseAdapter {
    private Context context;
    private List<CoursesRecourd> list;
    private AbHttpUtil mAbHttpUtil = null;
    private RelativeLayout toast;

    public LCCoursesRecordAdapter(Context context, List<CoursesRecourd> list2, RelativeLayout toast) {
        super();
        this.context = context;
        this.list = list2;
        this.toast = toast;
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        isToast();
    }


    public void  isToast(){
        if(isVisible(list,toast)){
            toast.setVisibility(View.VISIBLE);
            L.e("++++++++++++显示+++++++++++++");
        }else{
            toast.setVisibility(View.GONE);
            L.e("++++++++++++吟唱+++++++++++++");
        }

    }
    public boolean isVisible(List<CoursesRecourd> list2, RelativeLayout toast) {
        for (int i = 0; i < list2.size(); i++) {
            if ("1".equals(list2.get(i).getLc_courses_prise())) {

                return true;
            }
        }
        return false;
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
        CoursesRecourd classList = list.get(position);

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.lc_coursesrecord_item, null);
            vh = new ViewHolder();
            vh.lc_courses_day = (TextView) convertView
                    .findViewById(R.id.lc_courses_day);
            vh.lc_courses_week = (TextView) convertView
                    .findViewById(R.id.lc_courses_week);
            vh.lc_courses_time = (TextView) convertView
                    .findViewById(R.id.lc_courses_time);
            vh.lc_courses_timed = (TextView) convertView
                    .findViewById(R.id.lc_courses_timed);
            vh.lc_courses_tname = (TextView) convertView
                    .findViewById(R.id.lc_courses_tname);
            vh.lc_courses_prise = (Button) convertView.findViewById(R.id.lc_courses_prise);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        setCourses(vh.lc_courses_day, classList.getLc_courses_day());
        setCourses(vh.lc_courses_week, classList.getLc_courses_week());
        setCourses(vh.lc_courses_time, classList.getLc_courses_time());
        setCourses(vh.lc_courses_timed, classList.getLc_courses_timed());
        setCourses(vh.lc_courses_tname, classList.getLc_courses_tname());
        setCoursesOnClick(vh.lc_courses_prise, classList.getLc_courses_prise(), classList.getId(), classList);
        return convertView;
    }

    private void setCoursesOnClick(final Button lc_courses_prise, String prise, final String id, final CoursesRecourd classList) {
        if ("1".equals(prise)) {

            lc_courses_prise.setText("确认");
            lc_courses_prise.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPraise(id, classList);

                }
            });
        } else if("2".equals(prise)){
            lc_courses_prise.setText("评价");
            lc_courses_prise.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, StudentCommentActivity.class);
                    intent.putExtra("hour_record_id",id);
                    intent.putExtra("state","2");
                    context.startActivity(intent);

                }
            });
        } else if("3".equals(prise)){
            lc_courses_prise.setText("查看评价");
            lc_courses_prise.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, StudentCommentActivity.class);
                    intent.putExtra("hour_record_id",id);
                    intent.putExtra("state","3");
                    context.startActivity(intent);
                }
            });
        }

    }

    private void setCourses(TextView lc_courses_day, String lc_courses_day1) {

        if (TextUtils.isEmpty(lc_courses_day1)) {
            return;
        }
        lc_courses_day.setText(lc_courses_day1);
    }


    /**
     * 销课
     */
    public void clickPraise(String id, final CoursesRecourd classList) {

        // 绑定参数
        // TODO Auto-generated method stub
        String p = "platform=android&time="
                + LCUtils.gettime().substring(0, 10) + "&uuid="
                + LCUtils.getOnly(context) + LCConstant.token;
        String url = "/version/updaterecord";
        // 绑定参数

        AbRequestParams params = new AbRequestParams();
        params.put("userid", LCConstant.userinfo.getUserid() + "");
        params.put("id", id);
        params.put("platform", "android");
        params.put("time", LCUtils.gettime().substring(0, 10));
        params.put("uuid", LCUtils.getOnly(context));
        params.put("sign", MD5Util.md5(p));
        mAbHttpUtil.post(LCConstant.URL+url, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        String praise = JSONUtils.getInstatce().loadConvert(content);
                        L.e(praise);
                        try {
                            JSONObject jo = new JSONObject(praise);
                            String errorCode = jo.optString("errorCode", "");
                            String errorMessage = jo.optString("errorMessage", "");
                            if ("0".equals(errorCode)) {
                                classList.setLc_courses_prise("2");
                                notifyDataSetChanged();
                                isToast();
                            } else {
//                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                                LCUtils.ReLogin(errorCode,context,errorMessage);
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
                        L.e("" + statusCode);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {

                        LCUtils.stopProgressDialog(context);
                    }


                });
    }


    class ViewHolder {
        private TextView lc_courses_day, lc_courses_week, lc_courses_time, lc_courses_timed, lc_courses_tname;
        private Button lc_courses_prise;

    }

}
