package cn.ledoing.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import cn.ledoing.activity.R.string;
import cn.ledoing.adapter.LCStudiedRecordAdapter;
import cn.ledoing.bean.HistoryAll;
import cn.ledoing.bean.HistoryList;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

/**
 *wpt
 * 2015/9/21 15:15
 * 学习记录
 */
public class LCStudyRecord extends LCActivitySupport {
	private LCTitleBar lc_studycalss_title;
	private ListView lc_studyrecored_ListView;
	private AbHttpUtil mAbHttpUtil = null;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private LCStudiedRecordAdapter studiedRecordAdapter;
	private List<HistoryList> mList;
	private int page = 1;
	private View lc_studyrecored_refresh;
	private LCNoNetWork lc_study_nonet;
    private ImageView lc_studyrecored_nodate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_studyrecored);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lc_studycalss_title = (LCTitleBar) findViewById(R.id.lc_studycalss_title);
		lc_studycalss_title.setCenterTitle(mString(string.studyrecord));
		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.lc_history_pullto);
		lc_studyrecored_ListView = (ListView) findViewById(R.id.lc_studyrecored_ListView);
		lc_studyrecored_ListView.setSelector(new ColorDrawable(
				Color.TRANSPARENT));
        lc_studyrecored_nodate=(ImageView)findViewById(R.id.lc_studyrecored_nodate);
		lc_studyrecored_refresh = (View) findViewById(R.id.lc_studyrecored_refresh);
		lc_study_nonet = (LCNoNetWork) findViewById(R.id.lc_study_nonet);
		lc_study_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netInit();
                isNoNet();
            }
        });
		lc_studyrecored_refresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				netInit();
			}
		});
		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView
				.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

					@Override
					public void onHeaderRefresh(AbPullToRefreshView view) {
						// refreshTask();
						netInit();
					}
				});
		mAbPullToRefreshView
				.setOnFooterLoadListener(new OnFooterLoadListener() {

					@Override
					public void onFooterLoad(AbPullToRefreshView view) {
						// loadMoreTask();
						netLoad();
					}
				});
		mList = new ArrayList<HistoryList>();
		isNoNet();
		netInit();
	}

	private void isNoNet() {
		// TODO Auto-generated method stub
		if (LCUtils.isNetworkAvailable(this)) {
			setNotNetBack();
		} else {
			setNotNet();
		}
	}

	public void setNotNetBack() {
		lc_study_nonet.setVisibility(View.GONE);
		mAbPullToRefreshView.setVisibility(View.VISIBLE);
	}

	public void setNotNet() {
		lc_study_nonet.setVisibility(View.VISIBLE);
		mAbPullToRefreshView.setVisibility(View.GONE);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		netInit();
	}

	private void netInit() {
		// TODO Auto-generated method stub
		// 获取Http工具类
		page = 1;
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("userid", LCConstant.userinfo.getUserid());
		params.put("page", page + "");
		params.put("pagesize", 20 + "");
		params.put("uuid", getOnly());
		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_TASK_LIST, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						// 移除进度框
						mAbPullToRefreshView.onHeaderRefreshFinish();
						// AbDialogUtil.removeDialog(context);
						HistoryAll historyAll = JSONUtils.getInstatce()
								.getHistoryAll(content);
						setDate(historyAll);
					};

					// 开始执行前
					@Override
					public void onStart() {
						// 显示进度框
						// AbDialogUtil.showProgressDialog(LCUserInfo.this, 0,
						// "加载中...");
//						LCUtils.startProgressDialog(context);
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						// AbToastUtil.showToast(context, error.getMessage());
						setNotNet();
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {
//						LCUtils.stopProgressDialog(context);
					};

				});
	}

	protected void setDate(HistoryAll historyAll) {
		// TODO Auto-generated method stub
		if ("0".equals(historyAll.getErrorCode())) {
			mList = historyAll.getList();
			if (mList.size() > 0) {
				lc_studyrecored_refresh.setVisibility(View.GONE);
			} else {
				lc_studyrecored_refresh.setVisibility(View.VISIBLE);
			}
			studiedRecordAdapter = new LCStudiedRecordAdapter(context, mList);
			lc_studyrecored_ListView.setAdapter(studiedRecordAdapter);

		} else {
			showToast(historyAll.getErrorMessage());
			LCUtils.ReLogin(historyAll.getErrorCode(), context,historyAll.getErrorMessage());
		}
	}

	private void netLoad() {
		// TODO Auto-generated method stub
		// 获取Http工具类
		page++;
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);

		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("userid", LCConstant.userinfo.getUserid());
		params.put("page", page + "");
		params.put("pagesize", 20 + "");
		params.put("uuid", getOnly());

		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_TASK_LIST, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						// 移除进度框
						mAbPullToRefreshView.onFooterLoadFinish();
						// AbDialogUtil.removeDialog(context);
						HistoryAll historyAll = JSONUtils.getInstatce()
								.getHistoryAll(content);
						setLoad(historyAll);
					};

					// 开始执行前
					@Override
					public void onStart() {
						// 显示进度框
						// AbDialogUtil.showProgressDialog(LCUserInfo.this, 0,
						// "加载中...");
//						LCUtils.startProgressDialog(context);
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						// AbToastUtil.showToast(context, error.getMessage());
						setNotNet();
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {
//						LCUtils.stopProgressDialog(context);
					};
				});
	}

	protected void setLoad(HistoryAll historyAll) {
		// TODO Auto-generated method stub
		if ("0".equals(historyAll.getErrorCode())) {
			if (Integer.parseInt(historyAll.getTotal_count()) < historyAll
					.getList().size()) {

				mList.addAll(historyAll.getList());
				studiedRecordAdapter.notifyDataSetChanged();
			} else {
				showToast("没有更多数据了");
			}
		} else {
			LCUtils.ReLogin(historyAll.getErrorCode(), context,historyAll.getErrorMessage());
			showToast(historyAll.getErrorMessage());
		}
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(lc_studyrecored_nodate);
    }
}
