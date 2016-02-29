package cn.ledoing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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

public class LCUpdatePhone extends LCActivitySupport {
	private LCTitleBar lc_update_title;
	private EditText lc_new_edphone;
	private TextView tost, send_text;
	private RelativeLayout lc_cancel, lc_submit, lc_send_dialog;
	private AbHttpUtil mAbHttpUtil = null;
	private RelativeLayout lc_phone_content;
	private LCNoNetWork lc_phone_nonet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_updatephone);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);
		lc_update_title = (LCTitleBar) findViewById(R.id.lc_phone_title);
		lc_update_title.setCenterTitle(mString(R.string.lc_updateuserphone));
		lc_update_title.setRightTitle(mString(string.lc_next));
		lc_update_title.setRightTitleListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cloaseInput();

			}
		});
		Intent intent = getIntent();
		String stringExtra = intent.getStringExtra("phone");
		lc_new_edphone = (EditText) findViewById(R.id.lc_new_edphone);
		send_text = (TextView) findViewById(R.id.send_text);
		tost = (TextView) findViewById(R.id.tost);
		tost.setText("更换手机后，下次登录可使用新手机号登录。当前手机号：" + stringExtra);
		lc_cancel = (RelativeLayout) findViewById(R.id.lc_cancel);
		lc_submit = (RelativeLayout) findViewById(R.id.lc_submit);
		lc_send_dialog = (RelativeLayout) findViewById(R.id.lc_send_dialog);
		lc_phone_content = (RelativeLayout) findViewById(R.id.lc_phone_content);
		lc_phone_nonet = (LCNoNetWork) findViewById(R.id.lc_phone_nonet);
		lc_phone_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });
		lc_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lc_send_dialog.setVisibility(View.GONE);
			}
		});
		lc_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				netCode(lc_new_edphone.getText().toString());
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
		lc_phone_nonet.setVisibility(View.GONE);
		lc_phone_content.setVisibility(View.VISIBLE);
	}

	public void setNotNet() {
		lc_phone_nonet.setVisibility(View.VISIBLE);
		lc_phone_content.setVisibility(View.GONE);
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
									Intent intent = new Intent(
											LCUpdatePhone.this,
											LCUpdatePhoneVerificationCode.class);
									intent.putExtra("phone", phone);
									startActivity(intent);
									finish();
									showToast("发送成功");

								} else {
									LCUtils.ReLogin(errorCode, context,errorMessage);
									showToast(errorMessage);
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

	protected void cloaseInput() {
		// TODO Auto-generated method stub
		/** 隐藏软键盘 **/
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}

		String string = lc_new_edphone.getText().toString();
		if (lc_send_dialog.getVisibility() == View.GONE) {
			if (!TextUtils.isEmpty(string)) {
				lc_send_dialog.setVisibility(View.VISIBLE);
				send_text.setText("我们将发送验证码短信到这个号码：" + string);
			} else {
				showToast("号码不能为空！");
			}
		} else {
			lc_send_dialog.setVisibility(View.GONE);
		}
	}
}
