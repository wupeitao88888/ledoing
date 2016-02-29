package cn.ledoing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import cn.ledoing.activity.R;
import cn.ledoing.bean.BeanTask;
import cn.ledoing.bean.FindList;
import cn.ledoing.bean.Praise;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;

public class LCBeanTaskAdapter extends BaseAdapter {
	private Context context;
	private List<BeanTask> list;

	private AbHttpUtil mAbHttpUtil = null;
	public LCBeanTaskAdapter(Context context, List<BeanTask> list2) {
		super();
		this.context = context;
		this.list = list2;
		mAbHttpUtil = AbHttpUtil.getInstance(context);
		mAbHttpUtil.setTimeout(5000);

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
        BeanTask classList = list.get(position);
		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.lc_beantask_item, null);
			vh = new ViewHolder();
			vh.tasktype = (TextView) convertView
					.findViewById(R.id.tasktype);
            vh.addlecbean = (TextView) convertView
                    .findViewById(R.id.addlecbean);
            vh.taskname = (TextView) convertView
                    .findViewById(R.id.taskname);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
        setTitle(vh.taskname, classList.getTaskname());
        setTitle(vh.addlecbean, classList.getAddlecbean());

		return convertView;
	}

	private void setPraise(LinearLayout lc_find_praise, final BeanTask classList) {
		// TODO Auto-generated method stub
		lc_find_praise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub


//					clickPraise(classList);

			}
		});
	}



	private void setTitle(TextView lc_class_title, String videoclassname) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(videoclassname)) {
			lc_class_title.setText(videoclassname);
		} else {
			lc_class_title.setText("");
		}
	}





	class ViewHolder {

		private TextView tasktype, addlecbean,taskname;

	}
}
