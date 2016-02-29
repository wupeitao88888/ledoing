package cn.ledoing.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.ledoing.activity.LCUserLoginAndRegister;
import cn.ledoing.activity.R;
import cn.ledoing.bean.LCMeProduction;
import cn.ledoing.bean.LCMeProductionList;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.model.NoDoubleClickListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;


/*
 * 
 * 
 * 我的
 */
public class LCMeFragment extends Fragment {
    private View rootView;// 缓存Fragment view
    private Activity mActivity;
    // 声明一个Gallery视图控件变量
    @SuppressWarnings("deprecation")
    private Gallery gallery;
    // 声明一个ImageSwitcher视图控件变量
    private ImageView imageSwitcher,//大图
            bg;//暂无内容背景
    List<LCMeProductionList> imageList;
    private AbHttpUtil mAbHttpUtil = null;
    public static TaskCallBack mtaskCallBack = null;
    public RelativeLayout share_onekey;
    private String picurl;
    private View lc_me_refresh;
    private AbPullToRefreshView lc_me_PullRefreshView;
    private LayoutInflater inflater;
    private final int FORRESULT = 100;
    private boolean INITDATE=true;
    private LCNoNetWork lc_me_nonet;


    public static void setTaskBack(TaskCallBack taskCallBack) {
        mtaskCallBack = taskCallBack;
    }

    public interface TaskCallBack {
        void success(int obj);
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        mActivity = this.getActivity();
        if (!LCConstant.islogin) {
            Intent intent = new Intent(getActivity(),
                    LCUserLoginAndRegister.class);
            intent.putExtra("mIntent", 6);
            startActivityForResult(intent, FORRESULT);
        }
        if (LCConstant.isRef) {
            mAbHttpUtil = AbHttpUtil.getInstance(mActivity);
            mAbHttpUtil.setTimeout(5000);
            netInit();
            LCConstant.isRef = false;
        }
        if (rootView == null) {
            rootView = initView(inflater);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        // 判断当前网络是否可用
        isNoNet();
        //在其他页面登录成功的时候返回页面是刷新一次数据
        if (LCConstant.islogin) {
            if(INITDATE==true){
                initDate();
                INITDATE=false;
            }
        }
        return rootView;
    }


    /**
     * <pre>
     * 方法说明：
     *  编写日期: 2015-4-7
     *  编写人员: 吴培涛
     * </pre>
     *
     * @param inflater
     * @return
     */
    private View initView(LayoutInflater inflater) {
        // TODO Auto-generated method stub

        View v = inflater.inflate(R.layout.lc_tab_me, null);
        mAbHttpUtil = AbHttpUtil.getInstance(mActivity);
        mAbHttpUtil.setTimeout(5000);
        lc_me_PullRefreshView = (AbPullToRefreshView) v
                .findViewById(R.id.lc_me_PullRefreshView);
        lc_me_nonet = (LCNoNetWork) v.findViewById(R.id.lc_me_nonet);
        share_onekey = (RelativeLayout) v.findViewById(R.id.share_onekey);
        lc_me_refresh = (View) v.findViewById(R.id.lc_me_refresh);
        // 获取视图控件对象
        gallery = (Gallery) v.findViewById(R.id.gallery);
        gallery.setUnselectedAlpha(0.3f);
        imageSwitcher = (ImageView) v.findViewById(R.id.image_switcher);
        bg= (ImageView) v.findViewById(R.id.bg);
        Glide.with(mActivity).load(R.drawable.me_bg).into(bg);
        if (LCConstant.islogin) {
            initDate();
        }
        return v;
    }

    private void initDate() {
        lc_me_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netInit();
            }
        });
        share_onekey.setOnClickListener(new NoDoubleClickListener() {

            @Override
            public void onNoDoubleClick(View v) {
                // TODO Auto-generated method stub
                if (isNetworkAvailable(getActivity())) {
//                    showShare(picurl);
                    downLoadImage(picurl);
                } else {
                    Toast.makeText(getActivity(), "当前无可用连接！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        lc_me_refresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                netInit();
            }
        });
        // 设置进度条的样式
        lc_me_PullRefreshView.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        lc_me_PullRefreshView.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        lc_me_PullRefreshView
                .setOnHeaderRefreshListener(new OnHeaderRefreshListener() {

                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                        // TODO Auto-generated method stub
                        netInit();
                    }
                });
        lc_me_PullRefreshView
                .setOnFooterLoadListener(new OnFooterLoadListener() {

                    @Override
                    public void onFooterLoad(AbPullToRefreshView view) {
                        // TODO Auto-generated method stub
                        netInit();
                    }
                });
        netInit();
    }

    private void isNoNet() {
        // TODO Auto-generated method stub
        if (isNetworkAvailable(mActivity)) {
            setNotNetWorkBack();
        } else {
            setNotNetWork();
        }
    }
    public void setNotNetWork() {
        lc_me_PullRefreshView.setVisibility(View.GONE);
        lc_me_nonet.setVisibility(View.VISIBLE);
    }

    public void setNotNetWorkBack() {
        lc_me_PullRefreshView.setVisibility(View.VISIBLE);
        lc_me_nonet.setVisibility(View.GONE);
    }

    /**
     * 检查当前网络是否可用
     *
     * @param
     * @return
     */

    public boolean isNetworkAvailable(Context activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //创建Handler
    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            AbDialogUtil.removeDialog(mActivity);
//            showToast(msg.obj.toString());
            Bundle bd=(Bundle)msg.obj;
            showShare(bd.getString("net"),bd.getString("localtion"));
        }
    };
    public void downLoadImage(final String urlPath) {

        if (TextUtils.isEmpty(urlPath)) {
            AbToastUtil.showToast(mActivity, "稍等哒！图片加载中！");
            return;
        }
        AbDialogUtil.showProgressDialog(mActivity, 0, "正在分享，稍等哒！");
        final String  pname= urlPath.substring(urlPath.lastIndexOf("/")+1, urlPath.lastIndexOf("."));
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
                    readAsFile(inSream, new File(path + pname+".jpg"));
                    Message msg = new Message();
                    msg.what = 0;
                    Bundle bd=new Bundle();
                    bd.putString("net",urlPath);
                    bd.putString("localtion",path + pname+".jpg");
                    msg.obj =bd;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                AbToastUtil.showToastInThread(mActivity, "分享失败");
                }
            }
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
    private void showShare(String url,String path) {
        if(TextUtils.isEmpty(url)){
            AbToastUtil.showToast(mActivity,"稍等哒！图片加载中！");
            return;
        }
//        String path = ImageLoader.getInstance().getDiscCache().get(url)
//                .getPath();
        L.e("url:"+url);
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        // oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("源自乐创家分享");
        // // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("源自乐创家分享个人作品！");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImageUrl(url);
        oks.setImagePath(path);
        // // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        // oks.setComment("我是测试评论文本");
        // // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // // oks.setShareFromQQAuthSupport(true);
        oks.setDialogMode();
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        // 启动分享GUI
        oks.show(getActivity());

    }

    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }

    private void netInit() {
        // TODO Auto-generated method stub
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        if (LCConstant.userinfo != null) {
            params.put("userid", LCConstant.userinfo.getUserid() + "");
        } else {
            params.put("userid", "");
        }
        params.put("uuid", LCUtils.getOnly(mActivity));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.USER_WORK_LIST, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        LCMeProduction lcMeProduction = JSONUtils.getInstatce()
                                .getLCMeProduction(content);

                        if ("0".equals(lcMeProduction.getErrorCode())) {
                            // 初始化一个MainGalleryAdapter对象
                            imageList = lcMeProduction.getList();
                            if (imageList.size() > 0) {
                                lc_me_refresh.setVisibility(View.GONE);
                                share_onekey.setEnabled(true);
                            } else {
                                lc_me_refresh.setVisibility(View.VISIBLE);
                                share_onekey.setEnabled(false);
                            }
                            MainGalleryAdapter adapter = new MainGalleryAdapter();

                            // 将适配器与gallery关联起来
                            gallery.setAdapter(adapter);

                            // 初始选中中间的图片
                            gallery.setSelection(imageList.size() / 2);
                            try {
                                picurl = imageList.get(imageList.size() / 2)
                                        .getWorkimg();
                                setIcon(imageSwitcher,
                                        imageList.get(imageList.size() / 2)
                                                .getWorkimg());
                            } catch (Exception e) {

                            }
                            gallery.setOnItemClickListener(new OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, int position, long id) {
                                    // 在ImageSwitcher中显示选中的图片
                                    picurl = imageList.get(position)
                                            .getWorkimg();
                                    L.e("path:" + picurl);
                                    setIcon(imageSwitcher, picurl);
                                    isNoNet();
                                }
                            });
                        } else {
                            LCUtils.ReLogin(lcMeProduction.getErrorCode(),
                                    mActivity,lcMeProduction.getErrorMessage());
                        }
                    }
                    // 开始执行前
                    @Override
                    public void onStart() {
                        // LCUtils.startProgressDialog(mActivity);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        // AbToastUtil.showToast(mActivity, content);
                        isNoNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(mActivity);
                        lc_me_PullRefreshView.onHeaderRefreshFinish();
                        lc_me_PullRefreshView.onFooterLoadFinish();
                    }

                    ;
                });
    }

    private String setSallimage(String groupimg) {
        if (TextUtils.isEmpty(groupimg))
            return "";
        String start = groupimg.substring(0, groupimg.lastIndexOf("."));
        String end = groupimg.substring(groupimg.lastIndexOf("."),
                groupimg.length());
        String substring = start + "_0" + end;
        L.e(substring);
        return substring;
    }

    private void setIcon(ImageView lc_class_icon, String lcMeProductionList) {
        // TODO Auto-generated method stub
        try {
            if (!TextUtils.isEmpty(lcMeProductionList)) {
//                LCUtils.mImageloader(lcMeProductionList, lc_class_icon,
//                        getActivity());
                Glide.with(getActivity())
                        .load(lcMeProductionList)
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .placeholder(R.drawable.image_loading)
                        .error(R.drawable.image_error)
                        .crossFade()
                        .into(lc_class_icon);
            } else {
                lc_class_icon.setImageResource(R.drawable.image_error);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 定义Gallery的数据适配器MainGalleryAdapter
     */

    class MainGalleryAdapter extends BaseAdapter {

        /**
         * 获得数量
         */
        @Override
        public int getCount() {
            return imageList.size();
        }

        /**
         * 获得当前选项
         */
        @Override
        public Object getItem(int position) {
            return imageList.get(position);
        }

        /**
         * 获得当前选项的id
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 获得当前选项的视图
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 初始化一个ImageView对象
            ImageView imageView = new ImageView(getActivity());
            // 设置缩放方式
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // 设置ImageView的宽高
            imageView.setLayoutParams(new Gallery.LayoutParams(getWidth(),
                    LayoutParams.MATCH_PARENT));

            // 设置IamgeView显示的图片
            // imageView.setImageResource(imageIds[position]);
            // setIcon2(imageView, imageList.get(position).getWorkimg());
            try {
                if (!TextUtils.isEmpty(imageList.get(position).getWorkimg())) {
                    Glide.with(getActivity())
                            .load(imageList.get(position).getWorkimg())
                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .placeholder(R.drawable.image_loading)
                            .error(R.drawable.image_error)
                            .crossFade()
                            .into(imageView);
                } else {
                    Glide.with(mActivity).load(R.drawable.image_error).into(imageView);
                }
            } catch (Exception e) {
            }
            /**
             * 设置ImageView背景，这里背景使用的是android提供的一种背景风格 在values/attr.xml文件中需要一下内容
             * <declare-styleable name="Gallery"> <attr
             * name="android:galleryItemBackground" /> </declare-styleable>
             */

            // 返回ImageView对象
            return imageView;
        }

    }

    public int getWidth() {
        // TODO Auto-generated method stub
        WindowManager wm = getActivity().getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int h = (int) AbViewUtil.dip2px(getActivity(), 73);
        if (height == 2048 & width == 1536) {
            h = (int) AbViewUtil.dip2px(getActivity(), 146);
        }
        if (height == 2560 & width == 1600) {
            h = (int) AbViewUtil.dip2px(getActivity(), 146);
        }
        return h;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == FORRESULT) {
            if (!LCConstant.islogin) {
                if (mtaskCallBack != null) {
                    mtaskCallBack.success(0);
                }
            } else {
                if (LCConstant.islogin) {
                    initDate();
                }
            }
        }
    }
}