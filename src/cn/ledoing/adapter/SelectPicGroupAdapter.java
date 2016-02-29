package cn.ledoing.adapter;



import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ledoing.activity.R;
import cn.ledoing.bean.SelectPicImageBean;
import cn.ledoing.imageloader.ImageLoader;


public class SelectPicGroupAdapter extends BaseAdapter {
	private List<SelectPicImageBean> list;
	protected LayoutInflater mInflater;
	private Context context;

	@Override
	public int getCount() {
		if (list != null) {
			return list.size() > 0 ? list.size() : 0;
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public SelectPicGroupAdapter(Context context, List<SelectPicImageBean> list) {
		this.list = list;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		SelectPicImageBean mImageBean = list.get(position);
		String path = mImageBean.getTopImagePath();
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.square_item_grid_selectpic_group, null);
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.group_image);
			viewHolder.mTextViewTitle = (TextView) convertView
					.findViewById(R.id.group_title);
			viewHolder.mTextViewCounts = (TextView) convertView
					.findViewById(R.id.group_count);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
		viewHolder.mTextViewCounts.setText(Integer.toString(mImageBean
				.getImageCounts()));
		ImageLoader.getInstance(context).DisplayImage(path,
				viewHolder.mImageView);

		return convertView;
	}

	public static class ViewHolder {
		public ImageView mImageView;
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
	}

}
