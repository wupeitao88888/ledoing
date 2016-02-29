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

import cn.ledoing.activity.R;
import cn.ledoing.bean.Courselist;
import cn.ledoing.bean.Student;
import cn.ledoing.global.LCConstant;
import cn.ledoing.utils.GlideCircleTransform;

public class GridViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<Student> gridItemList = new ArrayList<Student>();
    Context mContext;
    private boolean isAdd = false;

    public GridViewAdapter(Context mContext, ArrayList<Student> list) {
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
        GHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gridview_item, null);
            viewHolder = new GHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GHolder) convertView.getTag();
        }
        setmImage(viewHolder.img, gridItemList.get(position).getAvatar());
        viewHolder.name.setText(gridItemList.get(position).getName());
        if(gridItemList.get(position).getAvatar().equals(LCConstant.HAND_URL)){
            viewHolder.img2.setVisibility(View.GONE);
            viewHolder.img3.setVisibility(View.GONE);
        }else{
            viewHolder.img2.setVisibility(View.VISIBLE);
            viewHolder.img3.setVisibility(View.VISIBLE);
        }
        //返回convertView
        return convertView;
    }

    private void setmImage(ImageView lc_index_threeimage, String courseimg) {
        if (!TextUtils.isEmpty(courseimg)) {
            Glide.with(mContext)
                    .load(courseimg).transform(new GlideCircleTransform(mContext))
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .centerCrop()
//                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.add_me)
//                    .crossFade()
                    .into(lc_index_threeimage);
        } else {
            lc_index_threeimage.setImageResource(R.drawable.hand);
        }
    }

    public class GHolder {
        ImageView img, img2, img3;
        TextView name;

        GHolder(View v) {
            img = (ImageView) v.findViewById(R.id.img);
            img2 = (ImageView) v.findViewById(R.id.imageView5);
            img3 = (ImageView) v.findViewById(R.id.imageView4);
            name = (TextView) v.findViewById(R.id.name);
        }
    }
}
