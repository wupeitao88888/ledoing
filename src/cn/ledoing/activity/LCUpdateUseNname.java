package cn.ledoing.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import cn.ledoing.activity.R.string;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

public class LCUpdateUseNname extends LCActivitySupport implements
		OnClickListener {
	private LCTitleBar lc_updateusername_title;
	private EditText lc_update_username;
	private Button lc_update_submit;
	private AbHttpUtil mAbHttpUtil = null;
	public static NameCallBack nameCallBack = null;
	private RelativeLayout lc_uodateusername_content;
	private LCNoNetWork lc_updateusername_nonet;
	private LCSharedPreferencesHelper lcSharedPreferencesLogin;
	private String realname;

	public static void setNameCallBack(NameCallBack nameCallBack) {
		LCUpdateUseNname.nameCallBack = nameCallBack;
	}

	public static interface NameCallBack {
		void onSuccess(String name);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitvity_lc_updateusername);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lc_updateusername_title = (LCTitleBar) findViewById(R.id.lc_updateusername_title);
		lc_update_username = (EditText) findViewById(R.id.lc_update_username);
		lc_updateusername_title.setOnclickBackListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cloaseInput();
				finish();
			}
		});
		lcSharedPreferencesLogin = new LCSharedPreferencesHelper(context,
				"isfrist");
		lc_update_submit = (Button) findViewById(R.id.lc_update_submit);
		lc_updateusername_title
				.setCenterTitle(mString(string.lc_updateusername));
		lc_update_submit.setOnClickListener(this);
		lc_uodateusername_content = (RelativeLayout) findViewById(R.id.lc_uodateusername_content);
		lc_updateusername_nonet = (LCNoNetWork) findViewById(R.id.lc_updateusername_nonet);
		lc_updateusername_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });
		isNoNet();

		Intent intent = getIntent();
		realname = intent.getStringExtra("realname");
		if (!TextUtils.isEmpty(realname))
			lc_update_username.setText(realname);
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
		lc_updateusername_nonet.setVisibility(View.GONE);
		lc_uodateusername_content.setVisibility(View.VISIBLE);
	}

	public void setNotNet() {
		lc_updateusername_nonet.setVisibility(View.VISIBLE);
		lc_uodateusername_content.setVisibility(View.GONE);
	}

	protected void cloaseInput() {
		// TODO Auto-generated method stub
		/** 隐藏软键盘 **/
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			cloaseInput();
			finish();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.lc_update_submit:
			if (!TextUtils.isEmpty(realname)) {
				if (realname.equals(lc_update_username.getText().toString())) {
					finish();
					return;
				}
			}
			netInit(lc_update_username.getText().toString());
			break;
		}
	}

	private void netInit(final String realname) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(realname)) {
			showToast("修改姓名为空");
			return;
		}
		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("realname", realname);
		params.put("uuid", getOnly());

		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.UPDATE_USER_INFO, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						// 移除进度框
						AbDialogUtil.removeDialog(context);
						String loadConvert = loadConvert(content);
						try {
							JSONObject jo = new JSONObject(loadConvert);
							String errorCode = jo.optString("errorCode", "1");
							String errorMessage = jo.optString("errorMessage",
									"保存失败！");
							if ("0".equals(errorCode)) {
								showToast("保存成功！");
								sharedPreferencesHelper.putValue("realname",
										realname);
								lcSharedPreferencesLogin.putValue("realname",
										realname);
								LCConstant.userinfo.setRealname(realname);
								if (nameCallBack != null) {
									nameCallBack.onSuccess(realname);
								}
								finish();
							} else {
								LCUtils.ReLogin(errorCode, context,errorMessage);
//								showToast(errorMessage);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							showToast("保存失败！");
						}
						cloaseInput();
					};

					// 开始执行前
					@Override
					public void onStart() {
						// 显示进度框
						AbDialogUtil.showProgressDialog(context, 0, "正在保存...");
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

						// 移除进度框
						AbDialogUtil.removeDialog(context);
					};

				});
	}

}
