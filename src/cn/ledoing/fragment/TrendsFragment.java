package cn.ledoing.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ab.view.pullview.AbListViewFooter;
import com.ab.view.pullview.AbPullToRefreshView;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.activity.TrendsInfo;
import cn.ledoing.adapter.HomeCenterAdapter;
import cn.ledoing.adapter.TrendsAdapter;
import cn.ledoing.adapter.TrendsFragmentAdapter;
import cn.ledoing.bean.Institution;
import cn.ledoing.bean.InstitutionHomeNews;
import cn.ledoing.bean.Trends;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;

public class TrendsFragment extends Fragment {

	private ListView trends_ListView;
	private TrendsFragmentAdapter trendsFragmentAdapter;
	private AbHttpUtil mAbHttpUtil;
	private String ins_id;
	private ArrayList<Trends.Data.DataList> list = new ArrayList<Trends.Data.DataList>();
	private int page = 1;
	private LinearLayout mLoadingLayout;
	private AbListViewFooter lode_footer;
	private boolean isadd;
	private RelativeLayout no_data_rl;

	public static TrendsFragment instantiation(String position) {
		TrendsFragment fragment = new TrendsFragment();
		Bundle args = new Bundle();
		args.putString("position", position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mAbHttpUtil = AbHttpUtil.getInstance(getActivity());
		mAbHttpUtil.setTimeout(5000);
		return inflater.inflate(R.layout.trends_layout_fragment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ins_id = getArguments().getString("position");
		trends_ListView = (ListView) view.findViewById(R.id.trends_ListView);
		no_data_rl = (RelativeLayout) view.findViewById(R.id.no_data_rl);
		trends_ListView.setDivider(null);
		trends_ListView.setFocusable(false);
		trendsFragmentAdapter = new TrendsFragmentAdapter(getActivity(), list);
		trends_ListView.setAdapter(trendsFragmentAdapter);
		mLoadingLayout = (LinearLayout) View.inflate(getActivity(), R.layout.footer_lode_layout, null);
		lode_footer = (AbListViewFooter)mLoadingLayout.findViewById(R.id.lode_footer);
		lode_footer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				page++;
				getInstitutionHome();
			}
		});
		trends_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), TrendsInfo.class);
				intent.putExtra("open", false);
				intent.putExtra("news_id", list.get(position).getNews_id());
				startActivity(intent);
			}
		});
		no_data_rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				list.clear();
				getInstitutionHome();
			}
		});
		getInstitutionHome();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


	private void getInstitutionHome(){
		AbRequestParams params = new AbRequestParams();
		params.put("insid", ins_id);
		params.put("page", page+"");
		params.put("pagesize", "15");
		L.e("=======page=========", page + "");
		mAbHttpUtil.post(LCConstant.GET_INSTITUTION_CENTER_NEWS, params,
				new AbStringHttpResponseListener() {

					@Override
					public void onStart() {
						LCUtils.startProgressDialog(getActivity());
					}

					@Override
					public void onFinish() {
						LCUtils.stopProgressDialog(getActivity());
					}

					@Override
					public void onFailure(int statusCode, String content, Throwable error) {
						//                        showToast("网络连接失败");
//						isNoNet();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						Log.e("===content====", content);
						Trends trend = JSON.parseObject(content,Trends.class);
						if(page ==1 && trend.getData().getList().size() <= 0){
							no_data_rl.setVisibility(View.VISIBLE);
							return;
						}
						no_data_rl.setVisibility(View.GONE);
						list.addAll(trend.getData().getList());
						if(isadd){
							trends_ListView.removeFooterView(mLoadingLayout);
						}
						trendsFragmentAdapter.notifyDataSetChanged();
						if(trend.getData().getList().size() >= 15){
							isadd = true;
							trends_ListView.addFooterView(mLoadingLayout);
						}
						setListViewHeightBasedOnChildren(trends_ListView);
					}
				});
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
//		if (this.getView() != null)
//			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
		if (this.getView() != null){
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
			if(menuVisible){
				list.clear();
				getInstitutionHome();
			}
		}
	}
}
