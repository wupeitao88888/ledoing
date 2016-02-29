package cn.ledoing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import cn.ledoing.activity.R;
import cn.ledoing.bean.AttentionCenter;
import cn.ledoing.utils.AbViewHolder;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCRatingBar;

/**
 * Created by wpt on 2015/11/2.
 */
public class CenterAdapter extends BaseAdapter {
    private Context context;
    private List<AttentionCenter.Data.DataList> list;

    public CenterAdapter(Context context, List<AttentionCenter.Data.DataList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AttentionCenter.Data.DataList attentionCenter = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_center_item, null);
        }

        ImageView centerIcon = AbViewHolder.get(convertView, R.id.centerIcon);
        TextView centerName = AbViewHolder.get(convertView, R.id.centerName);
        TextView grade = AbViewHolder.get(convertView, R.id.grade);
        TextView centerAddress = AbViewHolder.get(convertView, R.id.centerAddress);
        TextView distance = AbViewHolder.get(convertView, R.id.distance);
        LinearLayout layout_lin = AbViewHolder.get(convertView, R.id.layout_lin);
        ImageView msg = AbViewHolder.get(convertView, R.id.msg);
        LCRatingBar grade_ratingBar = AbViewHolder.get(convertView, R.id.grade_ratingBar);
        grade_ratingBar.setSmail(true);
        grade_ratingBar.setMark(Float.parseFloat(attentionCenter.getScore()));
        LCUtils.mImageloader(attentionCenter.getFace_pic(), centerIcon, context);
        LCUtils.setTitle(centerName, attentionCenter.getIns_name());
        LCUtils.setTitle(grade, attentionCenter.getScore());
        LCUtils.setTitle(centerAddress, attentionCenter.getIns_addr());
        LCUtils.setTitle(distance, attentionCenter.getDistance());
        for (int i = 0 ;i< layout_lin.getChildCount(); i++) {
            ImageView childAt = (ImageView) layout_lin.getChildAt(i);
            try {
                LCUtils.mImageloader(attentionCenter.getTeacher_list().get(i), childAt, context);
            }catch(Exception e){
                childAt.setVisibility(View.GONE);
            }
        }
        if(attentionCenter.getReport_num()>0){
            msg.setVisibility(View.VISIBLE);
        }else{
            msg.setVisibility(View.GONE);
        }

        return convertView;
    }
}
