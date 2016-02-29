package cn.ledoing.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.ledoing.bean.CityUser;
import cn.ledoing.bean.DistrictUser;
import cn.ledoing.bean.LCLogin;
import cn.ledoing.bean.Provice;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbFileHttpResponseListener;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AutoInstall;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCDialog;
import cn.ledoing.view.LCQuestionDialog;

public class LCWelcome extends LCActivitySupport  {
	private ImageView lc_welcome,login;
	private LCSharedPreferencesHelper lcSharedPreferencesHelper;
	LCDialog myShowDialog;
	LCQuestionDialog myShowDialogConstraint;
//	// 您的ak
//	private String AK = "pWSMER9ANlTiwUnOYmk1fb1z";
//	// 您的sk前16位
//	private String SK = "KHkqaKckDim8BM2QUGc7oGSS9eKgvvxb";
	private AbHttpUtil mAbHttpUtil = null;
	private RelativeLayout lc_wlcome_dialog;
	private TextView lc_wlcome_downloading;
	private String  codeStaus="0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_lcwelcome);
		checkMemoryCard();
		showToast("建议在WiFi下观看视频，避免造成不必要的损失！");
		initView();
//		AbDialogUtil.showDialog(context);
//		startActivity(new Intent(LCWelcome.this,LCUploading.class));
		L.e(getDeviceInfo(context));
	}



	public static String getDeviceInfo(Context context) {
		try{
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if( TextUtils.isEmpty(device_id) ){
				device_id = mac;
			}

			if( TextUtils.isEmpty(device_id) ){
				device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (100 == arg0) {
			initView();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		lc_welcome = (ImageView) findViewById(R.id.lc_welcome);
        login=(ImageView)findViewById(R.id.login);
		lc_wlcome_dialog = (RelativeLayout) findViewById(R.id.lc_wlcome_dialog);
		lc_wlcome_downloading = (TextView) findViewById(R.id.lc_wlcome_downloading);
		lc_wlcome_dialog.setVisibility(View.GONE);
		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(10000);
		lcSharedPreferencesHelper = new LCSharedPreferencesHelper(context,
				"isfrist");
		AnimationSet an = new AnimationSet(true);
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
		alpha.setDuration(5000);
		lc_welcome.setAnimation(alpha);
		alpha.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				String token = lcSharedPreferencesHelper.getValue("token");
				String userid = lcSharedPreferencesHelper.getValue("userid");
				String username = lcSharedPreferencesHelper
						.getValue("username");
				String userpic = lcSharedPreferencesHelper.getValue("userpic");
				String realname = lcSharedPreferencesHelper
						.getValue("realname");
				String birthday = lcSharedPreferencesHelper
						.getValue("birthday");
				String provincial = lcSharedPreferencesHelper
						.getValue("provincial");
				String provincialid = lcSharedPreferencesHelper
						.getValue(LCSharedPreferencesHelper.PROVINCIALID);
				Provice provice = new Provice(provincial,provincialid);
				String city = lcSharedPreferencesHelper.getValue("city");
				String cityid = lcSharedPreferencesHelper.getValue(LCSharedPreferencesHelper.CITYID);
				CityUser cityUser = new CityUser(city,cityid);
				String district = lcSharedPreferencesHelper
						.getValue("district");
				String districtid = lcSharedPreferencesHelper
						.getValue(LCSharedPreferencesHelper.DISTRICTID);
				DistrictUser districtUser = new DistrictUser(district,districtid);
				String mobile = lcSharedPreferencesHelper.getValue("mobile");
				String email = lcSharedPreferencesHelper.getValue("email");
				String is_member = lcSharedPreferencesHelper.getValue(LCSharedPreferencesHelper.IS_MEMBER);
				if (!TextUtils.isEmpty(token)) {
					// LCConstant.userinfo=
					LCConstant.userinfo = new LCLogin("0", "", userid,
							username, userpic, realname, birthday, "0",
							provice, cityUser, districtUser, mobile, email, token,
							"0",is_member);
					LCConstant.islogin = true;
					LCConstant.token = token;
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (validateInternet()) {
					getNewVestion();
				}
//                mIntent();
			}
		});
	}


	public void mIntent() {
		String isfrist = lcSharedPreferencesHelper.getValue("frist");

		if (isfrist == null) {
			lcSharedPreferencesHelper.putValue("frist", "true");
			Intent intent = new Intent(context, LCGuidancePage.class);
			startActivity(intent);
			finish();
		} else {
			// Intent intent = new Intent(context,
			// LCUploading.class);
			// intent.putExtra("pst", 13);
			// startActivity(intent);
			Intent intent = new Intent(context, LCHomeFragmentHost.class);
			startActivity(intent);
//            Intent intent = new Intent(context, LCGuidancePage.class);
//            startActivity(intent);
			finish();
		}
	
	}

	public void getNewVestion() {
		String p = "platform=android&time="
				+ LCUtils.gettime().substring(0, 10) + "&uuid="
				+ LCUtils.getOnly(context) + LCConstant.token;
		String url = "/version/versionupdate";
		// 绑定参数

		AbRequestParams params = new AbRequestParams();
		params.put("uuid", LCUtils.getOnly(context));
		mAbHttpUtil.post(LCConstant.URL+LCConstant.VERSION_VERSIONUPDATE, params, new AbStringHttpResponseListener() {

			// 获取数据成功会调用这里
			@Override
			public void onSuccess(int statusCode, String content) {
				String loadConvert = loadConvert(content);
				L.e(loadConvert);
				try {
					JSONObject jo = new JSONObject(loadConvert);
					String errorCode = jo.optString("errorCode", "1");
					String errorMessage = jo.optString("errorMessage",
							"已是最新版本！");
					if ("0".equals(errorCode)) {
						JSONObject jsonObject = jo.getJSONObject("data");
						final String url = jsonObject.optString("url");
						String version = jsonObject.optString("version");
						String status = jsonObject.optString("status");
                        if(TextUtils.isEmpty(status)){
                            codeStaus=status;
                        }
						if (getIsUpdate(LCUtils.getVersionName(context),
								version)) {
                            IsConstraintUpdate(status, url);
						} else {
                            mIntent();
						}
					} else {
//						showToast(errorMessage);
                        if(20004==Integer.parseInt(errorCode)){
                            mIntent();
                        }else{
                            LCUtils.ReLogin(errorCode, context,errorMessage);
                        }
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					showToast("已是最新版本！");
                    mIntent();
				}
			};

			// 开始执行前
			@Override
			public void onStart() {
                AbDialogUtil.showProgressDialog(context, 0, "正在初始化数据，稍等哒！...");
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
//				if (validateInternet()) {
//					getNewVestion();
//				}
                mIntent();
			}

			// 完成后调用，失败，成功
			@Override
			public void onFinish() {
                AbDialogUtil.removeDialog(context);
			};
		});
	}

	public static boolean getIsUpdate(String oldv, String newv) {
		if (TextUtils.isEmpty(oldv)) {
			return true;
		}
		if (TextUtils.isEmpty(newv)) {
			return true;
		}

		if (newv.equals(oldv)) {
			return false;
		} else {
			try {
				int oldVerstionLeft = Integer.parseInt(oldv.substring(0,
						oldv.indexOf(".")));
				int newVerStionLeft = Integer.parseInt(newv.substring(0,
						newv.indexOf(".")));

				int oldVerstionCenter = Integer.parseInt(oldv.substring(
						oldv.indexOf(".") + 1, oldv.lastIndexOf(".")));
				int newVerStionCenter = Integer.parseInt(newv.substring(
						newv.indexOf(".") + 1, newv.lastIndexOf(".")));

				int oldVerstionRight = Integer.parseInt(oldv.substring(
						oldv.lastIndexOf(".") + 1, oldv.length()));
				int newVerStionRight = Integer.parseInt(newv.substring(
						newv.lastIndexOf(".") + 1, newv.length()));

				if (oldVerstionLeft >= newVerStionLeft) {
					if (oldVerstionLeft == newVerStionLeft) {
						if (oldVerstionCenter >= newVerStionCenter) {
							if (oldVerstionCenter == newVerStionCenter) {
								if (oldVerstionRight >= newVerStionRight) {
									if (oldVerstionRight == newVerStionRight) {
										return false;
									} else {
										return false;
									}
								} else {
									return true;
								}

							} else {
								return false;
							}

						} else {
							return true;
						}
					} else {
						return false;
					}
				} else {
					return true;
				}
			} catch (Exception e) {
				return true;
			}
		}

	}

	public void IsConstraintUpdate(String state, final String url) {


		if ("0".equals(state)) {
			myShowDialog = showDialogIsLoading(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myShowDialog.cancel();
					DownLoad(url);
					lc_wlcome_dialog.setVisibility(View.VISIBLE);
				}
			}, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					myShowDialog.cancel();
					mIntent();
				}
			}, "检测有最新版本，是否更新？", "确定", "取消");
		} else {
			myShowDialogConstraint = showDialogConstraint(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							myShowDialogConstraint.cancel();
							DownLoad(url);
							lc_wlcome_dialog.setVisibility(View.VISIBLE);
						}
					}, new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							myShowDialogConstraint.cancel();
							finish();
						}
					}, "检测有最新版本，是否更新？", "确定", "取消");
		}
	}

	public void DownLoad(String url) {

		mAbHttpUtil.get(url, new AbFileHttpResponseListener(url) {

			// 获取数据成功会调用这里
			@Override
			public void onSuccess(int statusCode, File file) {
				AutoInstall.setUrl(file.getPath());
				AutoInstall.install(context);
			}

			// 开始执行前
			@Override
			public void onStart() {

			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				showToast("更新失败！");
                if("0".equals(codeStaus)){
                    mIntent();
                }else{
                    finish();
                }
			}

			// 下载进度
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				lc_wlcome_downloading.setText("正在更新：" + bytesWritten
						/ (totalSize / 100) + "/" + 100 + "%");
			}

			// 完成后调用，失败，成功
			public void onFinish() {
				lc_wlcome_dialog.setVisibility(View.GONE);
			};

		});
	}

	public LCDialog showDialogIsLoading(OnClickListener cancel,
			OnClickListener affim, String msg, String textcancel,
			String textaffim) {
		View dialogLoading = LayoutInflater.from(context).inflate(
				R.layout.lc_download_dialog, null);
		final LCDialog dialogLoad = new LCDialog(context, R.style.MyDialog,
				dialogLoading);
		dialogLoad.show();
		TextView tv = (TextView) dialogLoading
				.findViewById(R.id.jxb_dialog_title);
		tv.setText(msg);
		Button dialog_button_cancel = (Button) dialogLoading
				.findViewById(R.id.dialog_button_cancel);
		dialog_button_cancel.setText(textcancel);
		Button dialog_button_affim = (Button) dialogLoading
				.findViewById(R.id.dialog_button_affim);

		dialog_button_affim.setText(textaffim);

		if (affim == null) {
			dialog_button_affim.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogLoad.cancel();
				}
			});

		} else {
			dialog_button_affim.setOnClickListener(affim);
		}

		if (cancel == null) {
			dialog_button_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogLoad.cancel();
				}
			});
		} else {
			dialog_button_cancel.setOnClickListener(cancel);

		}
		return dialogLoad;
	}

	public LCQuestionDialog showDialogConstraint(OnClickListener cancel,
			OnClickListener affim, String msg, String textcancel,
			String textaffim) {
		View dialogLoading = LayoutInflater.from(context).inflate(
				R.layout.lc_download_dialog, null);
		final LCQuestionDialog dialogLoad = new LCQuestionDialog(context,
				R.style.MyDialog, dialogLoading);
		dialogLoad.show();
		TextView tv = (TextView) dialogLoading
				.findViewById(R.id.jxb_dialog_title);
		tv.setText(msg);
		Button dialog_button_cancel = (Button) dialogLoading
				.findViewById(R.id.dialog_button_cancel);
		dialog_button_cancel.setText(textcancel);
		Button dialog_button_affim = (Button) dialogLoading
				.findViewById(R.id.dialog_button_affim);

		dialog_button_affim.setText(textaffim);

		if (affim == null) {
			dialog_button_affim.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogLoad.cancel();
				}
			});

		} else {
			dialog_button_affim.setOnClickListener(affim);
		}

		if (cancel == null) {
			dialog_button_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialogLoad.cancel();
				}
			});
		} else {
			dialog_button_cancel.setOnClickListener(cancel);

		}
		return dialogLoad;
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(lc_welcome);
        releaseImageView(login);
    }
}
