package cn.ledoing.activity;

import org.json.JSONException;
import org.json.JSONObject;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;

import cn.ledoing.utils.GlideCircleTransform;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

/**
 * 我的二维码
 */
public class LCMeCode extends LCActivitySupport implements OnClickListener {
    private LCTitleBar lc_mecode_title;
    private ImageView lc_mecode_image, lc_mecode_hand;
    private AbHttpUtil mAbHttpUtil = null;
    private RelativeLayout lc_mecode_chose, lc_mecode_change, lc_mecode_savephoto;
    private TextView lc_mecode_name;
    private String urlP;
    private RelativeLayout lc_mecode_all;
    private LCNoNetWork lc_mecode_nonetw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_mecode);
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(10000);
        lc_mecode_title = (LCTitleBar) findViewById(R.id.lc_mecode_title);
        lc_mecode_image = (ImageView) findViewById(R.id.lc_mecode_image);

        lc_mecode_change = (RelativeLayout) findViewById(R.id.lc_mecode_change);
        lc_mecode_chose = (RelativeLayout) findViewById(R.id.lc_mecode_chose);
        lc_mecode_savephoto = (RelativeLayout) findViewById(R.id.lc_mecode_savephoto);
        lc_mecode_all = (RelativeLayout) findViewById(R.id.lc_mecode_all);
        lc_mecode_nonetw = (LCNoNetWork) findViewById(R.id.lc_mecode_nonetw);
        lc_mecode_nonetw.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewVestion();
                isNoNet();
            }
        });
        lc_mecode_change.setOnClickListener(this);
        lc_mecode_chose.setOnClickListener(this);
        lc_mecode_savephoto.setOnClickListener(this);
        lc_mecode_name = (TextView) findViewById(R.id.lc_mecode_name);
        lc_mecode_hand = (ImageView) findViewById(R.id.lc_mecode_hand);
        lc_mecode_title.setRightTitleRes(R.drawable.lc_more);
        lc_mecode_title.setCenterTitle("我的二维码");
        lc_mecode_title.setCenterTitleColor(getResources().getColor(R.color.white));
        lc_mecode_title.setRightTitleImageListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lc_mecode_chose.setVisibility(View.VISIBLE);
            }
        });
        getNewVestion();
        if (!TextUtils.isEmpty(LCConstant.userinfo.getUserpic())) {
            Glide.with(context)
                    .load(LCConstant.userinfo.getUserpic()).transform(new GlideCircleTransform(context))
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .centerCrop()
////                    .placeholder(R.drawable.image_loading)
////                    .error(R.drawable.image_error)
//                    .crossFade()
                    .into(lc_mecode_hand);
        } else {
            Glide.with(context)
                    .load(R.drawable.hand)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .crossFade()
                    .into(lc_mecode_hand);
        }


        if (!TextUtils.isEmpty(LCConstant.userinfo.getRealname())) {
            lc_mecode_name.setText("" + LCConstant.userinfo.getRealname());
        } else {
            lc_mecode_name.setText("");
        }
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
        lc_mecode_nonetw.setVisibility(View.GONE);
        lc_mecode_all.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_mecode_nonetw.setVisibility(View.VISIBLE);
        lc_mecode_all.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.lc_mecode_change:
                lc_mecode_chose.setVisibility(View.GONE);
                getNewVestion();
                break;
            case R.id.lc_mecode_savephoto:
                lc_mecode_chose.setVisibility(View.GONE);
                downLoadImage(urlP);
                break;
            case R.id.lc_mecode_chose:
                lc_mecode_chose.setVisibility(View.GONE);
                break;

        }


    }


    public void downLoadImage(final String urlPath) {
        if (TextUtils.isEmpty(urlPath))
            return;
        new Thread() {
            public void run() {
                try {

                    URL url = new URL(urlPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(6 * 1000);  // 注意要设置超时，设置时间不要超过10秒，避免被android系统回收
                    if (conn.getResponseCode() != 200) throw new RuntimeException("请求url失败");
                    InputStream inSream = conn.getInputStream();
                    String path = Environment.getExternalStorageDirectory() + "/leDoingDownload/";
                    File dir = new File(path);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    //把图片保存到项目的根目录
                    readAsFile(inSream, new File(path + "meCode.jpg"));
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = path + "meCode.jpg";
                    handler.sendMessage(msg);
                } catch (Exception e) {

                }
            }

            ;
        }.start();
    }

    public void readAsFile(InputStream inSream, File file) throws Exception {
        FileOutputStream outStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inSream.close();
    }

    //创建Handler
    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            showToast(msg.obj.toString());
        }


    };

    public void getNewVestion() {

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        if (LCConstant.userinfo != null) {
            params.put("userid", LCConstant.userinfo.getUserid());
        }
        params.put("uuid", LCUtils.getOnly(context));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.VERSION_QRCODE, params, new AbStringHttpResponseListener() {

            // 获取数据成功会调用这里
            @Override
            public void onSuccess(int statusCode, String content) {
                String loadConvert = loadConvert(content);
                L.e(loadConvert);
                try {
                    JSONObject jo = new JSONObject(loadConvert);
                    String errorCode = jo.optString("errorCode", "1");
                    String errorMessage = jo.optString("errorMessage",
                            "");
                    if ("0".equals(errorCode)) {
                        JSONObject jsonObject = jo.getJSONObject("data");
                        urlP = jsonObject.optString("url", "null");
                        LCUtils.mImageloader(urlP, lc_mecode_image, context);
                    } else {
//                        showToast(errorMessage);
                        LCUtils.ReLogin(errorCode, context, errorMessage);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    showToast("已是最新版本！");
                }
            }


            // 开始执行前
            @Override
            public void onStart() {
                LCUtils.startProgressDialog(context);
            }

            // 失败，调用
            @Override
            public void onFailure(int statusCode, String content,
                                  Throwable error) {
                LCUtils.stopProgressDialog(context);
                isNoNet();
            }

            // 完成后调用，失败，成功
            @Override
            public void onFinish() {
                LCUtils.stopProgressDialog(context);
            }

            ;
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }

    public void release() {
        releaseImageView(lc_mecode_image);
        releaseImageView(lc_mecode_hand);
        releaseRelativeLayout(lc_mecode_all);
    }


}
