package cn.ledoing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.bean.ActivityBean;
import cn.ledoing.http.AbHttpUtil;

public class LCActivityListAdapter extends BaseAdapter {
    private Context context;
    private List<ActivityBean> list;
    int status;
    // 图片下载类

    private AbHttpUtil mAbHttpUtil = null;
//	private ImageLoaderListView mImageLoader;

    public LCActivityListAdapter(Context context, List<ActivityBean> list2,int status) {
        super();
        this.context = context;
        this.list = list2;
        this.status = status;
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
        ActivityBean activity = list.get(position);

        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_activity_list_item, null);
            vh = new ViewHolder();
            vh.title = (TextView) convertView
                    .findViewById(R.id.title);
            vh.address = (TextView) convertView
                    .findViewById(R.id.address);
            vh.phone = (TextView) convertView
                    .findViewById(R.id.phone);
            vh.centerStatus = (TextView) convertView
                    .findViewById(R.id.center_type);
            vh.isCourse = (ImageView) convertView
                    .findViewById(R.id.imageView2);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        setTitle(vh.title, activity.getInstitution_name());
        setTitle(vh.phone, activity.getPhone_number());
        setTitle(vh.address, activity.getAddress());
        if(activity.getHas_class() == 1){//当前中心有课程时
            setTitle(vh.address, activity.getAddress());
            vh.isCourse.setImageResource(R.drawable.center_select);
        }else if (activity.getHas_class() == 0){//无课程
            setTitle(vh.address, "该中心暂无本主题课程");
            vh.isCourse.setImageResource(R.drawable.center_selecter);
        }
        if(status == 1){//第一个是历史中心
            if(position == 0){
                vh.centerStatus.setText("历史中心：");
                vh.centerStatus.setVisibility(View.VISIBLE);
            }
            if(position == 1){
                vh.centerStatus.setText("可选中心：");
                vh.centerStatus.setVisibility(View.VISIBLE);
            }
        }else if(status == 0){//没有历史中心
            vh.centerStatus.setVisibility(View.GONE);
        }
        return convertView;
    }


    private void setTitle(TextView lc_class_title, String str) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(str)) {
            lc_class_title.setText(str);
        } else {
            lc_class_title.setText("");
        }
    }


    class ViewHolder {
        private TextView title, address, phone,centerStatus;
        private ImageView isCourse;
    }


}
