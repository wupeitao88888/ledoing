package cn.ledoing.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ledoing.activity.R;
import cn.ledoing.bean.Video;


public class LCStudiedAdapter extends BaseAdapter {
	private Context context;
	private List<Video> list;

	// 图片下载类

	public LCStudiedAdapter(Context context, List<Video> list) {
		super();
		this.context = context;
		this.list = list;

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
		Video classList = list.get(position);

		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.lc_studied_item, null);
			vh = new ViewHolder();
			vh.studied_all = (RelativeLayout) convertView
					.findViewById(R.id.studied_all);
			vh.lc_studied_icon = (ImageView) convertView
					.findViewById(R.id.lc_studied_icon);
			vh.lc_studied_title = (TextView) convertView
					.findViewById(R.id.lc_studied_title);
			vh.lc_studied_time = (TextView) convertView
					.findViewById(R.id.lc_studied_time);
			vh.lc_studied_info = (TextView) convertView
					.findViewById(R.id.lc_studied_info);
			vh.lc_videoinfo_note = (RelativeLayout) convertView
					.findViewById(R.id.lc_videoinfo_note);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		if ("true".equals(classList.getPlaying())) {
			vh.studied_all.setBackgroundColor(context.getResources().getColor(
					R.color.isplay));
			vh.lc_studied_title.setTextColor(context.getResources().getColor(
					R.color.lightblack));
		} else {
			vh.studied_all.setBackgroundColor(context.getResources().getColor(
					R.color.white));
			vh.lc_studied_title.setTextColor(context.getResources().getColor(
					R.color.lightblack));
		}
		setIcon(vh.lc_studied_icon, position,vh.lc_videoinfo_note);
		setTitle(vh.lc_studied_title, classList.getVideoname());
		setTitle(vh.lc_studied_time, classList.getVideotime());
		// setTitle(vh.lc_studied_info, classList.getVideo_intro());

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

	private void setIcon(ImageView lc_class_icon, int p,RelativeLayout lc_videoinfo_note) {
		// TODO Auto-generated method stub
		// if (p == 1) {
		// lc_class_icon.setImageResource(R.drawable.play_one);
		// } else if (p == 2) {
		// lc_class_icon.setImageResource(R.drawable.play_two);
		// } else if (p == 3) {
		// lc_class_icon.setImageResource(R.drawable.play_three);
		// } else if (p == 4) {
		// lc_class_icon.setImageResource(R.drawable.play_four);
		// }

		switch (p) {
		case 0:
//			lc_class_icon.setImageResource(R.drawable.play_one);
            lc_videoinfo_note.setVisibility(View.VISIBLE);
			break;
		case 1:
//			lc_class_icon.setImageResource(R.drawable.play_two);
			break;
		case 2:
//			lc_class_icon.setImageResource(R.drawable.play_four);
			break;
		case 3:
//			lc_class_icon.setImageResource(R.drawable.play_three);
			break;
		default:
//			lc_class_icon.setImageResource(R.drawable.play_three);
			break;
		}
	}

	class ViewHolder {
		private ImageView lc_studied_icon;
		private TextView lc_studied_title, lc_studied_time, lc_studied_info;
		private RelativeLayout studied_all,lc_videoinfo_note;
	}
}
