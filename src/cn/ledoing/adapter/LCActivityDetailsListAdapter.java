package cn.ledoing.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.ledoing.activity.LCOrderActivity;
import cn.ledoing.activity.R;
import cn.ledoing.bean.ActivityDetailsListBean;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.utils.AbDateUtil;

public class LCActivityDetailsListAdapter extends BaseAdapter {
    private Context context;
    private List<ActivityDetailsListBean> list;
    // 图片下载类

    private AbHttpUtil mAbHttpUtil = null;
//	private ImageLoaderListView mImageLoader;

    public LCActivityDetailsListAdapter(Context context, List<ActivityDetailsListBean> list2) {
        super();
        this.context = context;
        this.list = list2;
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
        ActivityDetailsListBean activity = list.get(position);

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_activity_deatils_list_item, null);
            vh = new ViewHolder();
            vh.distance = (TextView) convertView
                    .findViewById(R.id.distance);
            vh.starttime = (TextView) convertView
                    .findViewById(R.id.start_time);
            vh.name = (TextView) convertView
                    .findViewById(R.id.name);
            vh.person = (TextView) convertView
                    .findViewById(R.id.person);
            vh.prime_price = (TextView) convertView
                    .findViewById(R.id.textView3);
            vh.member_price = (TextView) convertView
                    .findViewById(R.id.textView15);
            vh.time = (TextView) convertView
                    .findViewById(R.id.time);
            vh.order = (ImageView) convertView.findViewById(R.id.order);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        setTitle(vh.distance, AbDateUtil.formatTime(activity.getRest_time() * 1000)+"后预约截止");
        setTitle(vh.starttime, AbDateUtil.getStringByFormat(activity.getStart_time(), "HH:mm") + "上课" + AbDateUtil.getStringByFormat(activity.getEnd_time(), "HH:mm") + "下课");
        setTitle(vh.name, activity.getMaster_teacher() + "老师");
        setTitle(vh.person,"剩余"+( activity.getMax_number()-activity.getNumber())+"空位");
        setTitle(vh.prime_price, "¥"+activity.getPrime_price());
        vh.prime_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 删除线
        setTitle(vh.member_price, "¥"+activity.getMember_price());
        setTitle(vh.time, activity.getBase_name()+"  "+AbDateUtil.getStringByFormat(activity.getStart_time(), "yyyy-MM-dd    EEEE"));

        if(activity.getStatus() == 1){//正常
            vh.order.setVisibility(View.GONE);
        }else if(activity.getStatus() == 2){//预约已满
            vh.order.setVisibility(View.VISIBLE);
            vh.order.setImageResource(R.drawable.person_over);
        }else if(activity.getStatus() == 3){//时间冲突
            vh.order.setVisibility(View.VISIBLE);
            vh.order.setImageResource(R.drawable.time_onflict);
        }

        if(activity.getIs_re().equals("1")){//是否已预约
            vh.order.setVisibility(View.VISIBLE);
            vh.order.setImageResource(R.drawable.yi_order);
        }
        return convertView;
    }


    private void setTitle(TextView lc_class_title, String videoclassname) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(videoclassname)) {
            lc_class_title.setText(videoclassname);
        } else {
            lc_class_title.setText("");
        }
    }


    class ViewHolder {
        private TextView distance, starttime, name, prime_price,member_price, time, person;
        private ImageView order;
    }

}
