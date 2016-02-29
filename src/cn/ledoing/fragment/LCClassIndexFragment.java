package cn.ledoing.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.ledoing.activity.LCUserLoginAndRegister;
import cn.ledoing.activity.R;
import cn.ledoing.activity.VideoActivity;
import cn.ledoing.adapter.LCIndexAdapter;
import cn.ledoing.bean.Theme;
import cn.ledoing.bean.ThemeGroup;
import cn.ledoing.bean.ThemeType;
import cn.ledoing.bean.VideoAll;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCDialog;
import cn.ledoing.view.LCNoNetWork;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.bumptech.glide.Glide;

/*
 * 
 * 
 * 信用评分
 */
public class LCClassIndexFragment extends Fragment {
	private View rootView;// 缓存Fragment view
	private Activity mActivity = null;
	private ListView mListView;
	// 只是用来模拟异步获取数据
	private Handler handler;
	private List<ThemeGroup> strList;
	private LCIndexAdapter listViewAdapter;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbHttpUtil mAbHttpUtil = null;
	private int page = 1;
	Theme historyAll;
	View lc_index_refresh;
	private LCNoNetWork lc_index_nonet;
	LCDialog showDialog;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub6222 0202 0010 8705 811
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = initView(inflater);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		return rootView;
	}

	private void isNoNet() {
		// TODO Auto-generated method stub
		if (LCUtils.isNetworkAvailable(mActivity)) {
			setNotNetBack();
		} else {
			setNotNet();
		}
	}

	/**
	 * 方法说明： 编写日期: 2015-4-1 编写人员: 吴培涛
	 * 
	 */
	private View initView(LayoutInflater inflater) {
		mActivity = this.getActivity();
		View v = inflater.inflate(R.layout.lc_tab_indexclass, null);
		lc_index_refresh = (View) v.findViewById(R.id.lc_index_refresh);
		mAbPullToRefreshView = (AbPullToRefreshView) v
				.findViewById(R.id.lc_index_pullto);


		mListView = (ListView) v.findViewById(R.id.xListView);
        mListView.setDivider(getResources().getDrawable(R.drawable.jxb_userinfo_normal));
		lc_index_nonet = (LCNoNetWork) v.findViewById(R.id.lc_index_nonet);
		isNoNet();
		handler = new Handler();

		lc_index_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netInit();
                isNoNet();
            }
        });
		// 获取ListView对象
		mListView.setDivider(null);

		lc_index_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				netInit();
			}
		});
		strList = new ArrayList<ThemeGroup>();
		mAbPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						// refreshTask();
						netInit();
					}
				});

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		mAbPullToRefreshView
				.setOnFooterLoadListener(new OnFooterLoadListener() {
					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						netload();
					}
				});
        LCUserLoginAndRegister.setLoginCallBack(new LCUserLoginAndRegister.LoginCallBack() {
            @Override
            public void onSuccess() {
                netInit();
            }
        });
		netInit();
		return v;
	}

	public void setNotNetBack() {
		lc_index_nonet.setVisibility(View.GONE);
		mAbPullToRefreshView.setVisibility(View.VISIBLE);
	}

	public void setNotNet() {
		lc_index_nonet.setVisibility(View.VISIBLE);
		mAbPullToRefreshView.setVisibility(View.GONE);
	}



	public LCDialog showDialog(OnClickListener cancel, OnClickListener affim,
			String msg, String textcancel, String textaffim) {
		View dialogView = LayoutInflater.from(getActivity()).inflate(
				R.layout.lc_dialog, null);
		final LCDialog dialog = new LCDialog(getActivity(), R.style.MyDialog,
				dialogView);
		dialog.show();
		TextView tv = (TextView) dialogView.findViewById(R.id.jxb_dialog_title);
		tv.setText(msg);
		Button dialog_button_cancel = (Button) dialogView
				.findViewById(R.id.dialog_button_cancel);
		dialog_button_cancel.setText(textcancel);
		Button dialog_button_affim = (Button) dialogView
				.findViewById(R.id.dialog_button_affim);
		dialog_button_affim.setVisibility(View.VISIBLE);
		dialog_button_affim.setText(textaffim);
		if (affim == null) {
			dialog_button_affim.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
		} else {
			dialog_button_affim.setOnClickListener(affim);
		}

		if (cancel == null) {
			dialog_button_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
				}
			});
		} else {
			dialog_button_cancel.setOnClickListener(cancel);

		}
		return dialog;
	}

	private void netload() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(historyAll.getTotal_count())) {
			mAbPullToRefreshView.onFooterLoadFinish();
			return;
		}
		// 获取Http工具类
		if ( strList.size()< Integer.parseInt(historyAll.getTotal_count())) {
			page++;


			L.e(page + "/" + Integer.parseInt(historyAll.getTotal_count()));
            mAbHttpUtil = AbHttpUtil.getInstance(mActivity);
            mAbHttpUtil.setTimeout(5000);

			// 绑定参数
			AbRequestParams params = new AbRequestParams();
			params.put("page", page + "");
			params.put("pagesize", 10 + "");
			params.put("uuid", LCUtils.getOnly(mActivity));
			mAbHttpUtil.post(LCConstant.URL +LCConstant.URL_API+ LCConstant.COURSE_BASE_LIST, params,
					new AbStringHttpResponseListener() {

						// 获取数据成功会调用这里
						@Override
						public void onSuccess(int statusCode, String content) {
							// 移除进度框
							// mAbPullToRefreshView.onFooterLoadFinish();
							// AbDialogUtil.removeDialog(context);
							historyAll = JSONUtils.getInstatce().getTheme(
									content);
							setLoad(historyAll);
						};

						// 开始执行前
						@Override
						public void onStart() {
							// 显示进度框
							// AbDialogUtil.showProgressDialog(LCUserInfo.this,
							// 0,
							// "加载中...");
							// LCUtils.startProgressDialog(mActivity);

						}

						// 失败，调用
						@Override
						public void onFailure(int statusCode, String content,
								Throwable error) {
                            isNoNet();
						}

						// 完成后调用，失败，成功
						@Override
						public void onFinish() {
							// LCUtils.stopProgressDialog(getActivity());
							mAbPullToRefreshView.onFooterLoadFinish();
						};

					});
		} else {
			Toast.makeText(getActivity(), "暂无数据！", Toast.LENGTH_SHORT).show();
			mAbPullToRefreshView.onFooterLoadFinish();
		}

	}

	protected void setLoad(Theme historyAll) {
		// TODO Auto-generated method stub
		if ("0".equals(historyAll.getErrorCode())) {
			// if (strList.size() <
			// Integer.parseInt(historyAll.getTotal_count())) {

			strList.addAll(historyAll.getList());
			listViewAdapter.notifyDataSetChanged();

            // } else {
			// Toast.makeText(mActivity, "没有更多数据", 1).show();
			// }
		} else {
			Toast.makeText(mActivity, historyAll.getErrorMessage(), Toast.LENGTH_SHORT).show();
			LCUtils.ReLogin(historyAll.getErrorCode(), mActivity,historyAll.getErrorMessage());
		}
	}

	private void netInit() {
		// TODO Auto-generated method stub
			page = 1;
		mAbHttpUtil = AbHttpUtil.getInstance(mActivity);
		mAbHttpUtil.setTimeout(5000);
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("page", page + "");
		params.put("pagesize", 10 + "");
		params.put("uuid", LCUtils.getOnly(mActivity));
		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.COURSE_BASE_LIST, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						// 移除进度框

						// AbDialogUtil.removeDialog(context);
						historyAll = JSONUtils.getInstatce().getTheme(content);
						setDate(historyAll);
					};

					// 开始执行前
					@Override
					public void onStart() {
						// 显示进度框
						// AbDialogUtil.showProgressDialog(LCUserInfo.this, 0,
						// "加载中...");
						// LCUtils.startProgressDialog(mActivity);
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
                        isNoNet();
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {
						// LCUtils.stopProgressDialog(getActivity());
						mAbPullToRefreshView.onHeaderRefreshFinish();
						mAbPullToRefreshView.onFooterLoadFinish();
					};

				});
	}





	protected void setDate(Theme historyAll) {
		// TODO Auto-generated method stub
		if ("0".equals(historyAll.getErrorCode())) {
			strList = historyAll.getList();
			if (strList.size() > 0) {
				lc_index_refresh.setVisibility(View.GONE);
			} else {
				lc_index_refresh.setVisibility(View.INVISIBLE);
			}
			listViewAdapter = new LCIndexAdapter(mActivity, strList);
			mListView.setAdapter(listViewAdapter);
		} else {
			Toast.makeText(mActivity, historyAll.getErrorMessage(), Toast.LENGTH_SHORT).show();
			LCUtils.ReLogin(historyAll.getErrorCode(), mActivity,historyAll.getErrorMessage());
		}
	}
}
