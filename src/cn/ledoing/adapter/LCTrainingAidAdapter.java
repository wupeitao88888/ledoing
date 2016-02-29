package cn.ledoing.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.ledoing.activity.R;
import cn.ledoing.bean.TrainingAid;
import cn.ledoing.utils.L;

public class LCTrainingAidAdapter extends BaseAdapter {

	private List<TrainingAid> datas;
	private Context context;
	/**
	 * CheckBox 是否选择的存储集合,key 是 position , value 是该position是否选中
	 */
	private Map<Integer, Boolean> isCheckMap = new HashMap<Integer, Boolean>();

	public LCTrainingAidAdapter(List<TrainingAid> list, Context context) {
		super();
		this.datas = list;
		this.context = context;
		// 初始化,默认都没有选中
		configCheckMap(false);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas == null ? 0 : datas.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TrainingAid trainingAid = datas.get(position);
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.lc_trainingaidadapter_item, null);
			vh = new ViewHolder();
			vh.lc_trainingaid_item_checkBox = (CheckBox) convertView
					.findViewById(R.id.lc_trainingaid_item_checkBox);
			vh.lc_trainingaid_item_content = (TextView) convertView
					.findViewById(R.id.lc_trainingaid_item_content);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.lc_trainingaid_item_content.setText(trainingAid.getAidsname());
		/*
		 * 设置单选按钮的选中
		 */
		unSelect(vh.lc_trainingaid_item_checkBox, position);

		if ("1".equals(trainingAid.getSelected())) {
			vh.lc_trainingaid_item_checkBox.setChecked(true);
		} else {
			vh.lc_trainingaid_item_checkBox.setChecked(false);
		}

		// if ("1".equals(trainingAid.getSelected())) {
		// if (position == 0) {
		// selectfalse();
		// }
		// }
		// if (vh.lc_trainingaid_item_checkBox.isChecked()) {
		// TrainingAid trainingAid2 = datas.get(position);
		// trainingAid2.setSelected(1 + "");
		// } else {
		// TrainingAid trainingAid2 = datas.get(position);
		// trainingAid2.setSelected(0 + "");
		// }
		L.e(trainingAid.getAidsid() + "/" + trainingAid.getAidsname());

		return convertView;
	}

	public void unSelect(final CheckBox lc_trainingaid_item_checkBox,
			final int position) {
		lc_trainingaid_item_checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (position != 0) {
							if (lc_trainingaid_item_checkBox.isChecked()) {
								TrainingAid trainingAid2 = datas.get(position);
								trainingAid2.setSelected(1 + "");
								TrainingAid trainingAid = datas.get(0);
								trainingAid.setSelected(0 + "");
							} else {
								TrainingAid trainingAid2 = datas.get(position);
								trainingAid2.setSelected(0 + "");
							}
						} else {
							if (lc_trainingaid_item_checkBox.isChecked()) {
								selectfalse();
								TrainingAid trainingAid2 = datas.get(0);
								trainingAid2.setSelected(1 + "");
							} else {
								TrainingAid trainingAid2 = datas.get(0);
								trainingAid2.setSelected(0 + "");
							}
						}
						notifyDataSetChanged();
					}
				});

	}

	class ViewHolder {
		private CheckBox lc_trainingaid_item_checkBox;
		private TextView lc_trainingaid_item_content;
	}

	/**
	 * 首先,默认情况下,所有项目都是没有选中的.这里进行初始化
	 */
	public void configCheckMap(boolean bool) {
		for (int i = 0; i < datas.size(); i++) {
			isCheckMap.put(i, bool);
		}
	}

	public void selectfalse() {

		for (int i = 0; i < datas.size(); i++) {
			TrainingAid trainingAid = datas.get(i);
			trainingAid.setSelected("0");
		}
	}

	public Map<Integer, Boolean> getCheckMap() {
		return this.isCheckMap;
	}

	public String getText() {
		String send = "";
		for (int i = 0; i < datas.size(); i++) {
			TrainingAid boolean1 = datas.get(i);
			if ("1".equals(boolean1.getSelected())) {
				send = send + boolean1.getAidsid() + ",";
			}
		}
		return send;
	}
}
