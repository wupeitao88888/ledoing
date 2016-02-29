package cn.ledoing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.bean.AttentionCenter;
import cn.ledoing.bean.FCenterAll;
import cn.ledoing.utils.AbViewHolder;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCRatingBar;

/**
 * Created by wpt on 2015/11/2.
 */
public class CenterAllAdapter extends BaseAdapter {
    private Context context;
    private List<FCenterAll.All_list> list;

    public CenterAllAdapter(Context context, List<FCenterAll.All_list> list) {
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
        FCenterAll.All_list attentionCenter = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_centerall_item, null);
        }

        ImageView centerIcon = AbViewHolder.get(convertView, R.id.centerIcon);
        TextView centerName = AbViewHolder.get(convertView, R.id.centerName);
        TextView grade = AbViewHolder.get(convertView, R.id.grade);
        TextView centerAddress = AbViewHolder.get(convertView, R.id.centerAddress);
        TextView distance = AbViewHolder.get(convertView, R.id.distance);
        LCRatingBar grade_ratingBar = AbViewHolder.get(convertView, R.id.grade_ratingBar);
        grade_ratingBar.setSmail(true);

        LCUtils.mImageloader(attentionCenter.getFace_pic(), centerIcon, context);
        LCUtils.setTitle(centerName, attentionCenter.getIns_name());
        LCUtils.setTitle(grade, attentionCenter.getScore());
        LCUtils.setTitle(centerAddress, attentionCenter.getIns_addr());
        LCUtils.setTitle(distance, attentionCenter.getDistance());
        grade_ratingBar.setMark(Float.parseFloat(attentionCenter.getScore()));

        return convertView;
    }
}
