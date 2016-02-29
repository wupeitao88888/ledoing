package cn.ledoing.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import cn.ledoing.activity.LCFindInfoPraise;
import cn.ledoing.activity.LCUserLoginAndRegister;
import cn.ledoing.activity.R;
import cn.ledoing.bean.FindList;
import cn.ledoing.bean.Praise;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.imageloader.ImageLoaderListView;
import cn.ledoing.imageloader.ImageLoaderListView.Type;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.FindRefresh;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;

public class LCFindAllAdapter extends BaseAdapter {
	private Context context;
	private List<FindList> list;
	// 图片下载类

	private AbHttpUtil mAbHttpUtil = null;
//	private ImageLoaderListView mImageLoader;

	public LCFindAllAdapter(Context context, List<FindList> list2) {
		super();
		this.context = context;
		this.list = list2;
		mAbHttpUtil = AbHttpUtil.getInstance(context);
		mAbHttpUtil.setTimeout(5000);
//		// 图片的下载
//		mImageLoader = ImageLoaderListView.getInstance(3, Type.LIFO);
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
		FindList classList = list.get(position);

		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.lc_findall_item, null);
			vh = new ViewHolder();
			vh.lc_find_icon = (ImageView) convertView
					.findViewById(R.id.lc_find_icon);
			vh.lc_find_praiseall = (LinearLayout) convertView
					.findViewById(R.id.lc_find_praiseall);
			vh.lc_find_name = (TextView) convertView
					.findViewById(R.id.lc_find_name);
			vh.lc_find_praisetext = (TextView) convertView
					.findViewById(R.id.lc_find_praisetext);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		setIcon(vh.lc_find_icon, classList,position);
		setTitle(vh.lc_find_name, classList.getName());
		setPraise(vh.lc_find_praiseall, classList);
		setPraiseCount(vh.lc_find_praisetext, classList.getPraise());
        LCFindInfoPraise.setFindRefresh(new FindRefresh() {
            @Override
            public void refresh(int position) {
            int praise=Integer.parseInt(list.get(position).getPraise());
                list.get(position).setPraise(praise+1+"");
                notifyDataSetChanged();
            }
        });
		return convertView;
	}

	private void setPraise(LinearLayout lc_find_praise, final FindList classList) {
		// TODO Auto-generated method stub
		lc_find_praise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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

	private void setPraiseCount(TextView lc_class_title, String videoclassname) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(videoclassname)) {
			lc_class_title.setText(videoclassname);
		} else {
			lc_class_title.setText("");
		}
	}

	private void setIcon(ImageView lc_class_icon, final FindList classList,final int position) {
		// TODO Auto-generated method stub
		try {
			if (!TextUtils.isEmpty(classList.getGroupimg())) {
                
//                mImageLoader.loadImage(classList.getGroupimg(), lc_class_icon,
//						true);
                Glide.with(context)
                        .load(classList.getGroupimg())
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .error(R.color.gray)
                        .crossFade()
                        .into(lc_class_icon);

			} else {
				lc_class_icon.setImageResource(R.drawable.image_error);
			}
		} catch (Exception e) {
		}

		lc_class_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, LCFindInfoPraise.class);

				intent.putExtra("findList", classList);
                intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
	}

	class ViewHolder {
		private ImageView lc_find_icon;
		private TextView lc_find_name, lc_find_praisetext;
		private LinearLayout lc_find_praiseall;
	}
}
