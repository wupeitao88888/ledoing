package cn.ledoing.activity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

public class LCResetPW extends LCActivitySupport implements OnClickListener {
	private LCTitleBar lc_resetpw_title;
	private Button lc_reset_getchecknumber,// 获取验证码
			lc_reset_submit;// 提交
	private EditText lc_reset_number,// 收机号
			lc_reset_pw,// 设置密码
			lc_reset_repw,// 重复输入
			lc_reset_checknumber;// 获取验证码
	private AbHttpUtil mAbHttpUtil = null;
	private LCNoNetWork lc_reset_nonet;
	private LinearLayout lc_resetpw_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_resetpw);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);
		lc_resetpw_title = (LCTitleBar) findViewById(R.id.lc_resetpw_title);
		lc_resetpw_title.setCenterTitle("重置登录密码");
		lc_reset_getchecknumber = (Button) findViewById(R.id.lc_reset_getchecknumber);
		lc_reset_submit = (Button) findViewById(R.id.lc_reset_submit);
		lc_reset_getchecknumber.setOnClickListener(this);
		lc_reset_submit.setOnClickListener(this);
		lc_reset_number = (EditText) findViewById(R.id.lc_reset_number);
		lc_reset_pw = (EditText) findViewById(R.id.lc_reset_pw);
		lc_reset_repw = (EditText) findViewById(R.id.lc_reset_repw);
		lc_reset_checknumber = (EditText) findViewById(R.id.lc_reset_checknumber);
		lc_reset_getchecknumber.setOnClickListener(this);
		lc_reset_nonet = (LCNoNetWork) findViewById(R.id.lc_reset_nonet);
		lc_resetpw_content = (LinearLayout) findViewById(R.id.lc_resetpw_content);
		lc_reset_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });
		isNoNet();
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
		lc_reset_nonet.setVisibility(View.GONE);
		lc_resetpw_content.setVisibility(View.VISIBLE);
	}

	public void setNotNet() {
		lc_reset_nonet.setVisibility(View.VISIBLE);
		lc_resetpw_content.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lc_reset_getchecknumber:
			if (!TextUtils.isEmpty(lc_reset_number.getText().toString())) {
				if (lc_reset_number.getText().toString().length() == 11) {

					if (isMobileNO(lc_reset_number.getText().toString())) {

						netCode(lc_reset_number.getText().toString());
					} else {
						showToast("手机号格式错误");
					}
				} else {
					showToast("手机号格式错误");
				}

			} else {
				showToast("手机号不能为空");
			}
			break;
		case R.id.lc_reset_submit:
			if (!TextUtils.isEmpty(lc_reset_number.getText().toString())) {
				if (!TextUtils.isEmpty(lc_reset_checknumber.getText()
						.toString())) {
					if (!TextUtils.isEmpty(lc_reset_pw.getText().toString())) {
						if (lc_reset_pw.length() >= 6
								& lc_reset_pw.length() <= 16) {

							netSetPw();
						} else {
							showToast("请输入6到16位密码");
						}

					} else {
						showToast("密码不能为空");
					}
				} else {
					showToast("验证码不能为空");
				}
			} else {
				showToast("手机号不能为空");
			}
			break;
		}
	}

	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	private void netSetPw() {
		// TODO Auto-generated method stub

		String username = lc_reset_number.getText().toString();
		String pw = lc_reset_pw.getText().toString();
		String code = lc_reset_checknumber.getText().toString();
		// 绑定参数
		AbRequestParams params = new AbRequestParams();

		params.put("username", username.trim());
		params.put("code", code.trim());
		params.put("password", pw.trim() + "");
		params.put("uuid", getOnly());
		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_RESET_PASSWORD, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						if (!TextUtils.isEmpty(content)) {

							String loadConvert = loadConvert(content);
							try {
								JSONObject jo = new JSONObject(loadConvert);
								String errorCode = jo.optString("errorCode");
								String errorMessage = jo.optString(
										"errorMessage", "0");
								if ("0".equals(errorCode)) {
									showToast("修改成功");
									finish();
								} else {
									showToast(errorMessage);
									LCUtils.ReLogin(errorCode, context,errorMessage);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								showToast("修改失败");
							}
						} else {
							showToast("修改失败");
						}
					};

					// 开始执行前
					@Override
					public void onStart() {
						LCUtils.startProgressDialog(context);
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						// Intent intent = new Intent(context,
						// LCNONetWork.class);
						// startActivityForResult(intent, 101);
                        isNoNet();
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {
						LCUtils.stopProgressDialog(getContext());
					};
				});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				int s = (Integer) msg.obj;
				if (s > 0) {
					lc_reset_getchecknumber.setText(s + "秒");
				} else {
					lc_reset_getchecknumber.setText("获取验证码");
					timer.cancel();
					timer = null;
					lc_reset_getchecknumber.setEnabled(true);
				}
				break;
			}
		}

	};
	Timer timer;

	public void mTime() {
		TimerTask task = new TimerTask() {
			int i = 30;

			public void run() {
				i--;
				Message message = new Message();
				message.what = 1;
				message.obj = i;
				handler.sendMessage(message);
			}
		};

		timer = new Timer(true);
		timer.schedule(task, 1000, 1000); // 延时1000ms后执行，1000ms执行一次
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String ss = data.getStringExtra("isRefresh");
		if ("1".equals(ss)) {
			// 刷新
			netSetPw();
		} else {
			// 不刷新
			AbDialogUtil.removeDialog(context);
		}

	}

	private void netCode(String phone) {
		// TODO Auto-generated method stub
		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("username", phone);
		params.put("uuid", getOnly());
		params.put("action", LCConstant.VERIFY_TYPE_FINDPASSWD + "");
		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_CODE, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						// 移除进度框
						AbDialogUtil.removeDialog(context);
						if (!TextUtils.isEmpty(content)) {

							String loadConvert = loadConvert(content);
							L.e(loadConvert);
							try {
								JSONObject jo = new JSONObject(loadConvert);
								String errorCode = jo.optString("errorCode");

								String errorMessage = jo.optString(
										"errorMessage", "");
								if ("0".equals(errorCode + "")) {
									mTime();
									showToast("发送成功");
								} else {
									showToast(errorMessage);
									LCUtils.ReLogin(errorCode, context,errorMessage);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								showToast("发送成功");
							}
						} else {
							showToast("发送成功");
						}
					};

					// 开始执行前
					@Override
					public void onStart() {
						// 显示进度框
						AbDialogUtil.showProgressDialog(context, 0, "发送中...");
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						// AbToastUtil.showToast(context, error.getMessage());
                        isNoNet();
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {

						// 移除进度框
						AbDialogUtil.removeDialog(context);
					};

				});
	}
}
