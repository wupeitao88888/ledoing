package cn.ledoing.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.ledoing.activity.LCUploading;
import cn.ledoing.activity.R;
import cn.ledoing.activity.VideoActivity;
import cn.ledoing.bean.HistoryList;
import cn.ledoing.bean.SingleHttpBean;
import cn.ledoing.bean.ThemeType;
import cn.ledoing.bean.VideoAll;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;


public class LCStudiedRecordAdapter extends BaseAdapter {
	private Context context;
	private List<HistoryList> list;
	private AbHttpUtil mAbHttpUtil = null;
	// 图片下载类

	public LCStudiedRecordAdapter(Context context, List<HistoryList> list) {
		super();
		this.context = context;
		this.list = list;
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
		HistoryList classList = list.get(position);

		ViewHolder vh = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.lc_studiedrecord_item, null);
			vh = new ViewHolder();
			vh.history_left_icon = (ImageView) convertView
					.findViewById(R.id.history_left_icon);
			vh.history_right_icon = (ImageView) convertView
					.findViewById(R.id.history_right_icon);
			vh.history_left_title = (TextView) convertView
					.findViewById(R.id.history_left_title);
			vh.history_right_title = (TextView) convertView
					.findViewById(R.id.history_right_title);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		setTitle(vh.history_left_title, classList.getCoursename());
		setIcon(vh.history_left_icon, classList);
		setTask(vh.history_right_icon, vh.history_right_title, classList);
		return convertView;
	}

	private void setTask(ImageView history_right_icon,
			TextView history_right_title, final HistoryList classList) {
		// TODO Auto-generated method stub
		if ("3".equals(classList.getTaskstatus())) {
			history_right_icon.setImageResource(R.drawable.d);
			history_right_title.setText("");
            history_right_icon.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                }
            });
		} else {
			if ("2".equals(classList.getTaskstatus())) {
				history_right_title.setText("去完成");
				history_right_icon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, LCUploading.class);
						intent.putExtra("pst", classList.getCourseid());
						intent.putExtra("ing", 0);
                        intent.putExtra("isback",true);
						context.startActivity(intent);
					}
				});
			} else {
				history_right_title.setText("去领取");
				history_right_icon.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
//						Intent intent = new Intent(context, LCUploading.class);
//						intent.putExtra("pst", classList.getCourseid());
//                        intent.putExtra("isback",true);
//						context.startActivity(intent);
						ThemeType tt=new ThemeType();
						tt.setCourseid(classList.getCourseid());
						tt.setCoursename(classList.getCoursename());
						tt.setIsbuy(1+"");
						netVideoinfo(tt, 0, 0);

					}
				});
			}
			history_right_icon.setImageResource(R.drawable.qu);

		}
	}

	private void setTitle(TextView lc_class_title, String videoclassname) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(videoclassname)) {
			lc_class_title.setText(videoclassname);
		} else {
			lc_class_title.setText("");
		}
	}

	private void setIcon(ImageView lc_class_icon, HistoryList classList) {
		// TODO Auto-generated method stub
		try {
			if (!TextUtils.isEmpty(classList.getVideothumbnail())) {

				// mAbImageLoader.display(lc_class_icon, LCConstant.URL + "/"
				// + classList.getVideothumbnail());
				LCUtils.mImageloader(classList.getVideothumbnail(),
						lc_class_icon, context);
				// lc_class_icon.setImageResource(R.drawable.xuexijilu);
			} else {
				lc_class_icon.setImageResource(R.drawable.image_error);
			}
		} catch (Exception e) {

		}
	}

	class ViewHolder {
		private ImageView history_left_icon, history_right_icon;
		private TextView history_left_title, history_right_title;

	}


	/**
	 * wpt
	 * 2015/9/8 15
	 * 请求视频信息传送到VideoActivity
	 */
	protected void netVideoinfo(final ThemeType themeType, final int position, final int index) {
		// TODO Auto-generated method stub
		mAbHttpUtil = AbHttpUtil.getInstance(context);
		mAbHttpUtil.setTimeout(5000);
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		if (LCConstant.userinfo != null) {
			params.put("userid", LCConstant.userinfo.getUserid());
		} else {
			params.put("userid", 0 + "");
		}
		params.put("courseid", themeType.getCourseid());
		params.put("page", 1 + "");
		params.put("pagesize", 20 + "");
		params.put("uuid", LCUtils.getOnly(context));
		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.COURSE_TASK_LIST, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						// 移除进度框
						L.e(content + "-------");
						VideoAll videoAll = JSONUtils.getInstatce()
								.getVideoAll(content);
						if ("0".equals(videoAll.getErrorCode())) {
							if (videoAll.getList() != null
									& videoAll.getList().size() != 0) {
//                                Intent intent = new Intent(mContext,
//                                        VideoActivity.class);
//                                intent.putExtra("themeType", themeType);
//                                intent.putExtra("videoAll", videoAll);
								Bundle bd = new Bundle();
								bd.putInt("position", position);
								bd.putInt("index", index);
								bd.putInt("isShowPull", 1);
//                                intent.putExtra("bundle", bd);
//                                mContext.startActivity(intent);
								initIsQustion(themeType, videoAll, bd);
							} else {
								AbToastUtil.showToast(context, "无视频");
								LCUtils.ReLogin(videoAll.getErrorCode(),
										context, videoAll.getErrorMessage());
							}
						} else {
							AbToastUtil.showToast(context,
									videoAll.getErrorMessage());
						}
					}

					// 开始执行前
					@Override
					public void onStart() {
						// 显示进度框
						LCUtils.startProgressDialog(context);
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
										  Throwable error) {
						AbToastUtil.showToast(context, "网络异常");
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {
//                        LCUtils.stopProgressDialog(mContext);

					}

				});
	}


	/**
	 * 获取问题状态
	 *
	 * @param
	 */
	private void initIsQustion(final ThemeType themeType, final VideoAll videoAll, final Bundle bd) {
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("courseid", themeType.getCourseid());
		params.put("uuid", LCUtils.getOnly(context));
		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.CHECK_TASK_QUAESTION_ANSWER, params,
				new AbStringHttpResponseListener() {
					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						String loadConvert = JSONUtils.getInstatce().loadConvert(content);
						SingleHttpBean singleHttpBean;
						ArrayList<String> qList = new ArrayList<String>();

						try {
							JSONObject jsonObject = new JSONObject(loadConvert);
							String errorCode = jsonObject.optString("errorCode", "1");
							String errorMessage = jsonObject.optString("errorMessage", "");
							if ("0".equals(errorCode)) {
								JSONObject jObject = jsonObject.getJSONObject("data");
								JSONArray ja = jObject.optJSONArray("list");
								for (int i = 0; i < ja.length(); i++) {
									qList.add(ja.get(i).toString());
								}
								Intent intent = new Intent(context,
										VideoActivity.class);
								intent.putExtra("themeType", themeType);
								intent.putExtra("videoAll", videoAll);
								intent.putStringArrayListExtra("questionid", qList);
								intent.putExtra("bundle", bd);
								context.startActivity(intent);

							} else {
								Intent intent = new Intent(context,
										VideoActivity.class);
								intent.putExtra("themeType", themeType);
								intent.putExtra("videoAll", videoAll);
								intent.putStringArrayListExtra("questionid", qList);

								intent.putExtra("bundle", bd);
								context.startActivity(intent);
							}
						} catch (Exception e) {
							Intent intent = new Intent(context,
									VideoActivity.class);
							intent.putExtra("themeType", themeType);
							intent.putExtra("videoAll", videoAll);
							intent.putStringArrayListExtra("questionid", qList);
							intent.putExtra("bundle", bd);
							context.startActivity(intent);
						}
					}

					// 开始执行前
					@Override
					public void onStart() {
						// 显示进度框
//                        AbDialogUtil.showProgressDialog(mContext, 0, "正在查询...");
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
										  Throwable error) {
						AbToastUtil.showToast(context, error.getMessage());
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {
						// 移除进度框
						AbDialogUtil.removeDialog(context);

					}
				});

	}


}
