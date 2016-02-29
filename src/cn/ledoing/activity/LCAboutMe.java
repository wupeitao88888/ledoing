package cn.ledoing.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ledoing.activity.R.string;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbFileHttpResponseListener;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AutoInstall;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCDialog;
import cn.ledoing.view.LCTitleBar;

// 添加注释dd
public class LCAboutMe extends LCActivitySupport {
    private LCTitleBar lc_aboutme_title;
    private RelativeLayout lc_aboutme_conmponyInfo, lc_aboutme_logo, lc_aboutme_version,
            lc_downloading_dialog;
    private AbHttpUtil mAbHttpUtil = null;
    private TextView lc_downloading;
    LCDialog showDialog;
    private int i = 0;
    private ImageView imageView1, login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_aboutme);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(imageView1);
        releaseImageView(login);
    }

    private void initView() {
        // TODO Auto-generated method stub
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(10000);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        login=(ImageView)findViewById(R.id.login);
        lc_aboutme_title = (LCTitleBar) findViewById(R.id.lc_aboutme_title);
        lc_aboutme_title.setCenterTitle(mString(string.aboutme));
        lc_aboutme_conmponyInfo = (RelativeLayout) findViewById(R.id.lc_aboutme_conmponyInfo);
        lc_aboutme_version = (RelativeLayout) findViewById(R.id.lc_aboutme_version);
        lc_downloading_dialog = (RelativeLayout) findViewById(R.id.lc_downloading_dialog);
        lc_downloading = (TextView) findViewById(R.id.lc_downloading);
        lc_aboutme_logo = (RelativeLayout) findViewById(R.id.lc_aboutme_logo);
        lc_aboutme_logo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                if (i == 5) {
                    showToast(LCConstant.URL);
                    i = 0;
                }
            }
        });
        lc_downloading_dialog.setVisibility(View.GONE);
        lc_aboutme_conmponyInfo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                startActivity(new Intent(LCAboutMe.this, LCCompanyInfo.class));
            }
        });
        lc_aboutme_version.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                getNewVestion();
            }
        });
    }

    public void getNewVestion() {
        // 绑定参数

        AbRequestParams params = new AbRequestParams();
        params.put("uuid", LCUtils.getOnly(context));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.VERSION_VERSIONUPDATE, params, new AbStringHttpResponseListener() {

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
                        if (getIsUpdate(LCUtils.getVersionName(context),
                                version)) {
                            showDialog = showDialog(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    showDialog.cancel();
                                    DownLoad(url);
                                    lc_downloading_dialog
                                            .setVisibility(View.VISIBLE);
                                }
                            }, null, "检测有最新版本，是否更新？", "确定", "");
                        } else {
                            showToast("已是最新版本！");

						}
					} else {
						showToast(errorMessage);
						LCUtils.ReLogin(errorCode, context,errorMessage);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					showToast("已是最新版本！");
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

			}

			// 完成后调用，失败，成功
			@Override
			public void onFinish() {
				LCUtils.stopProgressDialog(context);
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

			}

			// 下载进度
			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				lc_downloading.setText("正在更新：" + bytesWritten
						/ (totalSize / 100) + "/" + 100 + "%");
			}

			// 完成后调用，失败，成功
			public void onFinish() {
				lc_downloading_dialog.setVisibility(View.GONE);
			};
		});
	}


}
