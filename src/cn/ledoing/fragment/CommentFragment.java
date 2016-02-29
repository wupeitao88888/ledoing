package cn.ledoing.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;

import cn.ledoing.activity.R;
import cn.ledoing.adapter.HomeCenterAdapter;
import cn.ledoing.adapter.HomeCommentAdapter;
import cn.ledoing.bean.InstitutionComment;
import cn.ledoing.bean.InstitutionTeacher;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;

public class CommentFragment extends Fragment {

	private ArrayList<InstitutionComment.Datas.ListData> comment_list;
	private String ins_id;
	private HomeCommentAdapter comment_adapter;
	private AbHttpUtil mAbHttpUtil;
	private InstitutionComment ins_comment;
	private ListView listView;
	private RelativeLayout no_data_rl;

	public static CommentFragment instantiation(String position) {
		CommentFragment fragment = new CommentFragment();
		Bundle args = new Bundle();
		args.putString("position", position);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_comment, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		listView = (ListView) view.findViewById(R.id.listView);
		no_data_rl = (RelativeLayout) view.findViewById(R.id.no_data_rl);
		listView.setFocusable(false);
		ins_id = getArguments().getString("position");

		comment_list = new ArrayList<InstitutionComment.Datas.ListData>();
		comment_adapter = new HomeCommentAdapter(getActivity(),comment_list);
		listView.setAdapter(comment_adapter);
		no_data_rl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(ins_id)) {
					getInstitutionTeacher();
				}
			}
		});
		if(!TextUtils.isEmpty(ins_id)){
			getInstitutionTeacher();
		}
	}

	private void getInstitutionTeacher(){
		mAbHttpUtil = AbHttpUtil.getInstance(getActivity());
		mAbHttpUtil.setTimeout(5000);
		AbRequestParams params = new AbRequestParams();
		params.put("insid", ins_id);
		mAbHttpUtil.post(LCConstant.GET_INSTITUTION_COMMENTS, params,
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
						ins_comment = JSON.parseObject(content, InstitutionComment.class);

						if (null != ins_comment) {
							try {
								if(ins_comment.getData().getList().size() <= 0){
									no_data_rl.setVisibility(View.VISIBLE);
									return;
								}
								comment_list.addAll(ins_comment.getData().getList());
								comment_adapter.notifyDataSetChanged();
								setListViewHeightBasedOnChildren(listView);
							} catch (NumberFormatException e) {
								Toast.makeText(getActivity(), ins_comment.getErrorMessage() + "", Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
						} else {
							Toast.makeText(getActivity(), content + "", Toast.LENGTH_SHORT).show();
						}
					}
				});
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


	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
//		if (this.getView() != null)
//			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
//		Log.e("11111111111111111111111","");
		if (this.getView() != null){
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
			if(menuVisible){
				getInstitutionTeacher();
			}
		}
	}
}
