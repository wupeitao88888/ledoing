package cn.ledoing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.activity.R;

import cn.ledoing.utils.AbViewHolder;
import cn.ledoing.utils.GlideCircleTransform;

/**
 *
 *老师的动态
 * Created by wupeitao on 15/11/6.
 */
public class TrendsImageAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<String> gridItemList = new ArrayList<>();
    Context mContext;
    private boolean isAdd = false;

    public TrendsImageAdapter(Context mContext, List<String> list) {
        this.mContext = mContext;
        this.gridItemList = list;
    }

    public int getCount() {
        return gridItemList.size();
    }

    public Object getItem(int position) {
        return gridItemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_trendspic, null);
        }
        String trendsPic = gridItemList.get(position);

        ImageView trends_pic= AbViewHolder.get(convertView,R.id.trends_pic);
        setmImage(trends_pic, trendsPic);
        return convertView;
    }

    private void setmImage(ImageView lc_index_threeimage, String courseimg) {
        if (!TextUtils.isEmpty(courseimg)) {
            Glide.with(mContext)
                    .load(courseimg)
//                    .transform(new GlideCircleTransform(mContext))
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .centerCrop()
//                    .placeholder(R.drawable.image_loading)
                    .error(R.color.gray)
//                    .crossFade()
                    .into(lc_index_threeimage);
        } else {
            lc_index_threeimage.setImageResource(R.color.gray);
        }
    }

}
