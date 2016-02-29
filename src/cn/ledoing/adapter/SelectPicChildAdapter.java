package cn.ledoing.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import cn.ledoing.activity.R;
import cn.ledoing.imageloader.ImageLoader;

public class SelectPicChildAdapter extends BaseAdapter {
	private ArrayList<String> list;
	protected LayoutInflater mInflater;
	private Context context;

	public SelectPicChildAdapter(Context context, ArrayList<String> list) {
		this.list = list;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public String getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path1 = list.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.square_item_grid_selectpic_child, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (ImageView) convertView
					.findViewById(R.id.child_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance(context).DisplayImage(path1,
				viewHolder.mImageView);

		return convertView;
	}

	public static class ViewHolder {
		public ImageView mImageView;
	}

}
