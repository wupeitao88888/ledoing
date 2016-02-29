package cn.ledoing.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ledoing.activity.R;
import cn.ledoing.bean.LCTeacherBean;

public class PopListAdapter extends BaseAdapter {

	Context context;
	Activity activity;
	private ArrayList<LCTeacherBean> lists;
	private int curp = -1;
	

	public PopListAdapter(Activity activity, ArrayList<LCTeacherBean> lists) {
		super();
		this.lists = lists;
		this.activity = activity;
	}

	public void selectPosition(int position) {
		this.curp = position;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.pop_list_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String str = lists.get(position).getTeacherName();
		if(curp == position){
			holder.tv_text.setTextColor(activity.getResources().getColor(R.color.header_selected));
		}else{
			holder.tv_text.setTextColor(activity.getResources().getColor(R.color.header));
		}
		//
		holder.tv_text.setText(str);
		return convertView;
		// TODO Auto-generated method stub
	}

	public class ViewHolder {
		
		TextView tv_text;

		public ViewHolder(View v) {
			tv_text = (TextView) v.findViewById(R.id.listview_textview);
		}

	}

}
