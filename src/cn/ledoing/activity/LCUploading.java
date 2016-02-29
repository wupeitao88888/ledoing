package cn.ledoing.activity;

import java.io.File;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.imageloader.ImageLoader;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.BitmapUtil;
import cn.ledoing.utils.JXBURIUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCTitleBar;


public class LCUploading extends LCActivitySupport implements OnClickListener {
    private LCTitleBar lc_uploading_title;
    private ImageView lc_uploading_imagedefault,// 要上传的图片
            login;
    private ImageView lc_uploading_bt;// 上传的按钮
    private RelativeLayout lc_uploading_photo, lc_uploading_creame,
            lc_uploading_quit, lc_uploading_chose, lc_uploading_dialog, lc_uploading_up;
    private File filec;
    private final int L_PHOTO = 1001;
    private final int L_CAMERA = 1002;
    private final int R_PHOTO = 1003;
    private final int R_CAMERA = 1004;
    private String leftPath;
    private AbHttpUtil mAbHttpUtil = null;
    private TextView numberText, maxText;
    // ProgressBar进度控制
    // 最大100
    private int max = 100;
    private int progress = 0;
    String stringExtra;
    public boolean isUploadingSuccess = false;
    public static CallBackMe callBackMe;
    private boolean isback = false;
    private boolean isbackIndex = true;

    public static void setCallBackMe(CallBackMe callBackMe) {
        LCUploading.callBackMe = callBackMe;
    }

    public interface CallBackMe {
        void mIntent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_lc_uploading);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub

        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(10000);
        lc_uploading_title = (LCTitleBar) findViewById(R.id.lc_uploading_title);
        lc_uploading_title.setCenterTitle(mString(R.string.uploading));
        lc_uploading_imagedefault = (ImageView) findViewById(R.id.lc_uploading_imagedefault);
        login = (ImageView) findViewById(R.id.login);
        lc_uploading_bt = (ImageView) findViewById(R.id.lc_uploading_bt);
        lc_uploading_photo = (RelativeLayout) findViewById(R.id.lc_uploading_photo);
        lc_uploading_up = (RelativeLayout) findViewById(R.id.lc_uploading_up);
        lc_uploading_creame = (RelativeLayout) findViewById(R.id.lc_uploading_creame);
        lc_uploading_quit = (RelativeLayout) findViewById(R.id.lc_uploading_quit);
        lc_uploading_chose = (RelativeLayout) findViewById(R.id.lc_uploading_chose);
        lc_uploading_dialog = (RelativeLayout) findViewById(R.id.lc_uploading_dialog);
        lc_uploading_quit.setOnClickListener(this);
        lc_uploading_creame.setOnClickListener(this);
        lc_uploading_photo.setOnClickListener(this);
        lc_uploading_bt.setOnClickListener(this);
        lc_uploading_imagedefault.setOnClickListener(this);
        lc_uploading_chose.setOnClickListener(this);

        Intent intent = getIntent();
        stringExtra = intent.getStringExtra("pst");
        String videof = intent.getStringExtra("video");
        if (TextUtils.isEmpty(videof)) {
            isbackIndex = true;
        }
        int intExtra = 1;
        try {
            intExtra = intent.getIntExtra("ing", 1);
            isback = intent.getBooleanExtra("isback", false);
        } catch (Exception e) {
            intExtra = 1;
            isback = false;
        }

        if (intExtra == 1) {
//            netTask();
        }
    }

    private void netTask() {
        // TODO Auto-generated method stub

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("courseid", stringExtra);
        params.put("userid", LCConstant.userinfo.getUserid());
        params.put("uuid", getOnly());
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_FINISH_TASK, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // setTitle(content);
                        String loadConvert = loadConvert(content);
                        try {
                            JSONObject jo = new JSONObject(loadConvert);
                            String errorCode = jo.optString("errorCode", "1");
                            String errorMessage = jo.optString("errorMessage",
                                    "1");
                            if ("0".equals(errorCode)) {

                            } else {
                                LCUtils.ReLogin(errorCode, context, errorMessage);
                                finish();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            finish();
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {


                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        // Intent intent = new Intent(context,
                        // LCNONetWork.class);
                        // startActivityForResult(intent, 101);
                        showToast(content);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {


                    }

                    ;
                });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.lc_uploading_imagedefault:
                break;
            case R.id.lc_uploading_bt:
                lc_uploading_chose.setVisibility(View.VISIBLE);
/***
 *
 * 跳转
 */
//                Intent intent=new Intent(LCUploading.this,CameraActivity.class);
//                startActivity(intent);
                break;
            case R.id.lc_uploading_photo:
                lc_uploading_chose.setVisibility(View.GONE);
                openPhoto(L_PHOTO);
                break;
            case R.id.lc_uploading_creame:
                lc_uploading_chose.setVisibility(View.GONE);
                takePic(L_CAMERA);
                break;
            case R.id.lc_uploading_quit:
                lc_uploading_chose.setVisibility(View.GONE);
                break;
            case R.id.lc_uploading_chose:
                lc_uploading_chose.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePic(int p) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String filename = Environment.getExternalStorageDirectory()
                    + "/lccache/" + System.currentTimeMillis() + "c.jpg";

            String path = Environment.getExternalStorageDirectory()
                    + "/lccache/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            filec = new File(filename);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filec));
            List<ResolveInfo> infos = context.getPackageManager()
                    .queryIntentActivities(intent,
                            PackageManager.MATCH_DEFAULT_ONLY);

            if (infos == null || infos.size() == 0) {
                Toast.makeText(context, "没有找到支持的应用", Toast.LENGTH_SHORT).show();
            } else {
                startActivityForResult(intent, p);
            }
        } else {
            Toast.makeText(context, "sd卡不存在", Toast.LENGTH_SHORT).show();
        }
    }

    private void openPhoto(int p) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(LCUploading.this, SelectPicActivity.class);
        intent.setType("image/*");
        startActivityForResult(intent, p);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case L_PHOTO:// 相册
                if (data != null) {
                    String stringExtra = data.getStringExtra("path");
                    File file = new File(stringExtra);
                    Uri uri = Uri.fromFile(file);
                    // cropPicture(mActivity, uri);
                    // jxb_boundbankcard_imageleft
                    // .setImageBitmap(getBitmapFromUri(uri));
                    // mImageloader(uri, jxb_boundbankcard_imageleft);
                    leftPath = JXBURIUtils.getImageAbsolutePath(LCUploading.this,
                            uri);
                    try {
                        // String smallImage = BitmapUtil.saveImg(
                        // PictureUtil.getSmallBitmap(leftPath), "leftPath");
                        // String rote = BitmapUtil.getStringURL(leftPath,
                        // "leftPath");
                        // String smallImage = BitmapUtil.saveImg(
                        // PictureUtil.getSmallBitmap(rote), "leftPath");
                        // ImageLoader.getInstance(context).DisplayImage(smallImage,
                        // // lc_uploading_imagedefault);
                        String rote = BitmapUtil.getStringURL(leftPath);
                        UpLoading(rote);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        showToast("取消");
                    }
                } else {
                    showToast("取消");
                }
                break;
            case L_CAMERA:
                if (filec.exists()) {
                    if (filec.length() > 100) {
                        Uri uri = Uri.fromFile(filec);
                        leftPath = JXBURIUtils.getImageAbsolutePath(
                                LCUploading.this, uri);

                        try {
//						 String smallImage = BitmapUtil.saveImg(
//						 PictureUtil.getSmallBitmap(leftPath),
//						 "leftPath");
                            String rote = BitmapUtil.getStringURL(leftPath);
                            // String smallImage = BitmapUtil.saveImg(
                            // PictureUtil.getSmallBitmap(leftPath), "leftPath");
                            // ImageLoader.getInstance(context).DisplayImage(
                            // smallImage, lc_uploading_imagedefault);
                            UpLoading(rote);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            showToast("取消");
                        }
                    } else {
                        showToast("取消");
                    }
                } else {
                    showToast("取消");
                }
                break;

        }
    }

    private DialogFragment mAlertDialog = null;

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (!isback & !isbackIndex) {
            if (callBackMe != null) {
                callBackMe.mIntent();
            }
        }
        releaseImageView(lc_uploading_imagedefault);
        releaseImageView(lc_uploading_bt);
        releaseImageView(login);
        releaseRelativeLayout(lc_uploading_up);
    }

    // 文件上传
    public void UpLoading(String path) {

        // 已经在后台上传
        if (mAlertDialog != null) {
            mAlertDialog.show(getFragmentManager(), "dialog");
            return;
        }
        AbRequestParams params = new AbRequestParams();
        try {
            // 参数随便加，在sd卡根目录放图片
            File file1 = new File(path);
            params.put("userpic", file1);
            params.put("courseid", stringExtra);
            params.put("uuid", LCUtils.getOnly(getContext()));


            mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_WORKS_UPLOAD, params,
                    new AbStringHttpResponseListener() {

                        @Override
                        public void onSuccess(int statusCode, String content) {

                            String loadConvert = loadConvert(content);
                            isbackIndex = false;
                            try {
                                JSONObject jo = new JSONObject(loadConvert);
                                String errorCode = jo.optString("errorCode", "1");
                                String errorMessage = jo.optString("errorMessage",
                                        "1");
                                if ("0".equals(errorCode)) {
                                    AbToastUtil.showToast(context, "上传成功！");
                                    isUploadingSuccess = true;
                                    finish();
                                } else {
//                                AbToastUtil.showToast(context, errorMessage);
                                    LCUtils.ReLogin(errorCode, context, errorMessage);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                AbToastUtil.showToast(context, "数据异常");
                            }

                        }

                        // 开始执行前
                        @Override
                        public void onStart() {
                            // 打开进度框
                            lc_uploading_dialog.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(int statusCode, String content,
                                              Throwable error) {
                            AbToastUtil.showToast(context, error.getMessage());
                        }

                        // 进度
                        @Override
                        public void onProgress(int bytesWritten, int totalSize) {
                        }

                        // 完成后调用，失败，成功，都要调用
                        public void onFinish() {
                            lc_uploading_dialog.setVisibility(View.GONE);
                        }
                    });
        } catch (Exception e) {
            showToast("文件不存在！");
        }
    }

}
