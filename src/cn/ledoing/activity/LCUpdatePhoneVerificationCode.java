package cn.ledoing.activity;

import java.util.Timer;
import java.util.TimerTask;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ledoing.activity.R.string;
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

public class LCUpdatePhoneVerificationCode extends LCActivitySupport {
	private LCTitleBar lc_updatephoneverificationcode_title;
	private EditText lc_uodatephoneverification_edittext;
	private Button lc_submit_verification;
	private TextView lc_uodatephoneverification_unreceive;
	private AbHttpUtil mAbHttpUtil = null;
	private RelativeLayout lc_updatephoneverificationcode_content;
	private LCNoNetWork lc_uodatephoneverification_nonet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_updatephoneverificationcode);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);
		lc_updatephoneverificationcode_title = (LCTitleBar) findViewById(R.id.lc_updatephoneverificationcode_title);
		lc_updatephoneverificationcode_title
				.setCenterTitle(mString(string.lc_updateuserphone));
		lc_uodatephoneverification_edittext = (EditText) findViewById(R.id.lc_uodatephoneverification_edittext);
		lc_submit_verification = (Button) findViewById(R.id.lc_submit_verification);
		lc_uodatephoneverification_unreceive = (TextView) findViewById(R.id.lc_uodatephoneverification_unreceive);
		lc_updatephoneverificationcode_content = (RelativeLayout) findViewById(R.id.lc_updatephoneverificationcode_content);
		lc_uodatephoneverification_nonet = (LCNoNetWork) findViewById(R.id.lc_uodatephoneverification_nonet);
		lc_uodatephoneverification_nonet
				.setRetryOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isNoNet();
                    }
                });
		Intent intent = getIntent();
		final String stringExtra = intent.getStringExtra("phone");
		mTime();
		lc_submit_verification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String string = lc_uodatephoneverification_edittext.getText()
						.toString();
				if (!TextUtils.isEmpty(string)) {
					netUpdateUsername(stringExtra, string);
				} else {
					showToast("验证码为空！");
				}
			}
		});

		lc_uodatephoneverification_unreceive
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						lc_uodatephoneverification_unreceive.setEnabled(false);
						netCode(stringExtra);
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
		lc_uodatephoneverification_nonet.setVisibility(View.GONE);
		lc_updatephoneverificationcode_content.setVisibility(View.VISIBLE);
	}

	public void setNotNet() {
		lc_uodatephoneverification_nonet.setVisibility(View.VISIBLE);
		lc_updatephoneverificationcode_content.setVisibility(View.GONE);
	}

	private void netCode(final String phone) {
		// TODO Auto-generated method stub
		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("username", phone);
		params.put("uuid", getOnly());
		params.put("action", LCConstant.VERIFY_TYPE_USERNAME + "");

		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_CODE, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						// 移除进度框
						AbDialogUtil.removeDialog(context);
						if (!TextUtils.isEmpty(content)) {

							String loadConvert = loadConvert(content);
							try {
								JSONObject jo = new JSONObject(loadConvert);
								String errorCode = jo.optString("errorCode",
										"1");
								String errorMessage = jo.optString(
										"errorMessage", "0");
								if ("0".equals(errorCode)) {
									mTime();
									showToast("发送成功");
								} else {
//									showToast(errorMessage);
									LCUtils.ReLogin(errorCode, context,errorMessage);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								showToast("数据异常");
							}
						} else {
							showToast("数据为空");
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
						setNotNet();
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {

						// 移除进度框
						AbDialogUtil.removeDialog(context);
					};

				});
	}

	private void netUpdateUsername(final String phone_number,
			String phone_checknumber) {
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("uuid", getOnly());
		params.put("username", phone_number);
		params.put("code", phone_checknumber);

		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_CHANGE_MOBILE, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						if (!TextUtils.isEmpty(content)) {

							String loadConvert = loadConvert(content);
							try {
								JSONObject jo = new JSONObject(loadConvert);
								String errorCode = jo.optString("errorCode",
										"1");
								String errorMessage = jo.optString(
										"errorMessage", "0");
								if ("0".equals(errorCode)) {
									sharedPreferencesHelper.putValue("mobile",
											phone_number);
									lcSharedPreferencesLogin.putValue(
											"username", phone_number);
									showToast("成功");
									finish();
								} else {
//									showToast(errorMessage);
									LCUtils.ReLogin(errorCode, context,errorMessage);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								showToast("数据异常");
							}
						} else {
							showToast("数据为空");
						}
					};

					// 开始执行前
					@Override
					public void onStart() {
						// 显示进度框
						AbDialogUtil.showProgressDialog(context, 0, "正在注册...");
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

						// 移除进度框
						AbDialogUtil.removeDialog(context);
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
					lc_uodatephoneverification_unreceive.setText(s + "秒");

				} else {
					lc_uodatephoneverification_unreceive.setText("没收到？重新获取验证码");
					timer.cancel();
					timer = null;
					lc_uodatephoneverification_unreceive.setEnabled(true);
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
}
