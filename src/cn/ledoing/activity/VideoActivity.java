package cn.ledoing.activity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import cn.ledoing.adapter.LCStudiedAdapter;
import cn.ledoing.bean.LyricBean;
import cn.ledoing.bean.Praise;
import cn.ledoing.bean.Question;
import cn.ledoing.bean.ThemeType;
import cn.ledoing.bean.Video;
import cn.ledoing.bean.VideoAll;
import cn.ledoing.global.AbAppConfig;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCPlayUtils;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;

import cn.ledoing.view.LCDialog;
import cn.ledoing.view.LCQuestionDialog;
import cn.ledoing.view.LCTextView;
import cn.ledoing.view.LCTextView.MyCallBack;
import cn.ledoing.view.LCTitleBar;
import cn.ledoing.view.SlideView;
import de.greenrobot.event.EventBus;

import com.baidu.cyberplayer.core.BVideoView;
import com.baidu.cyberplayer.core.BVideoView.OnCompletionListener;
import com.baidu.cyberplayer.core.BVideoView.OnErrorListener;
import com.baidu.cyberplayer.core.BVideoView.OnInfoListener;
import com.baidu.cyberplayer.core.BVideoView.OnPlayingBufferCacheListener;
import com.baidu.cyberplayer.core.BVideoView.OnPreparedListener;

/**
 * wpt
 * 2015/9/8 15:24
 * <p/>
 * 视频播放 ——注释
 */
public class VideoActivity extends LCActivitySupport implements
        OnPreparedListener, OnCompletionListener, OnErrorListener,
        OnInfoListener, OnPlayingBufferCacheListener {

    /**
     * 百度key
     */
    private String AK = "pWSMER9ANlTiwUnOYmk1fb1z";
    private String SK = "KHkqaKckDim8BM2QUGc7oGSS9eKgvvxb";
    private String mVideoSource = null;
    private ImageView mPlaybtn = null;// 小播放按钮
    private ImageView mBigPlayBtn = null;// 大播放按钮
    private SeekBar mProgress = null;
    private TextView mDuration = null;
    private TextView mCurrPostion = null;
    private RelativeLayout videoviewholder;
    private RelativeLayout lc_ctrl_all;
    private TextView lc_srttext;//简介
    private ScrollView lc_scroll;
    private RelativeLayout lc_viedo_info;// title布局
    private TextView lc_videoinfo_text;// title
    private ImageView lc_videoinfo_arrow;// 箭头
    private boolean ONAUSE = false;// 按home键不发起新视频
    private ImageView lc_zoom;
    private final String TAG = "VideoViewPlayingActivity";
    private LCQuestionDialog dialog2;// 问题对话框
    private boolean PORTRAIT = true;
    private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
    private BVideoView mVV = null;
    private EventHandler mEventHandler;
    private HandlerThread mHandlerThread;
    private final Object SYNC_Playing = new Object();
    private WakeLock mWakeLock = null;
    private static final String POWER_LOCK = "VideoViewPlayingActivity";
    private boolean mIsHwDecode = true;// 软解硬解
    private final int EVENT_PLAY = 0;
    /**
     * 更新进度条
     */
    private final int UI_EVENT_UPDATE_CURRPOSITION = 1;
    private final int UI_ISGONE = 2;// 播放出错显示UI

    private final int UI_BUFFER = 3;// 缓冲；
    private final int UI_Completion = 4;// 播放完成
    private final int UI_PULL = 5;// 播放完成
    /**
     * 更新播放到最大时间,播放过程中不会变小
     */
    private final int UI_PLAYMAX = 6;

    private boolean isCallback = true;
    private Timer timer;
    private long START_V;
    private boolean NEWSTARTPLAY = false;// 网络连接失败，重试
    private VideoAll videoAll;
    private int PlayCount = 0;// 记录播放到第几个
    private ListView lc_studied_ListView;
    private LCStudiedAdapter adapter;
    private TextView lc_startplay_buffer,
            lc_video_tasklcbeanOld,//旧价格
            lc_video_tasklcbean;//乐豆价格
    private RelativeLayout lc_pull_view,//下拉刷新
            lc_video_lceBeanall,//兑换课程
            lc_video_listvideoview//video的list
                    ;
    private Button lc_video_lceBeanbt;//兑换课程按钮
    private String Select = "";
    private SlideView slideView;
    private LCTitleBar lc_video_title;
    private TextView lc_videoinfo_synopsis;//简介
    private ArrayList<String> qList;//已回答过的问题
    private int isShowPull = 0;//0是不顯示，1是現實
    /**
     * 修改人：wpt
     * 2015/9/8  14:42
     * <p/>
     * 添加购买回调，解决购买后不刷新的情况下，进入仍需要购买课程
     */
    public static BuyCallBack buyCallBack;

    public static void setBuyCallBack(BuyCallBack buyCallBack) {
        VideoActivity.buyCallBack = buyCallBack;
    }

    public interface BuyCallBack {
        void onSuccess(Bundle bu);
    }

    /**
     * 修改人：wpt
     * 2015/9/8  15:03
     * <p/>
     * s上一页的数据
     */
    Bundle bd;

    /**
     * 发起新回话时是否暂停
     */
    private boolean ISPAUSE = false;//
    /**
     * 避免对话框的重复加载
     */
    private boolean ISSHOWSECOND = false;//
    private ThemeType themeType;
    private ImageView lc_anim;
    private boolean ISAINM = true;
    private AbHttpUtil mAbHttpUtil = null;
    /**
     * 记录播放位置
     */
    private int mLastPos = 0, /**
     * 服务器返回上次播放位置
     */
    mLastPlayTime;
    /**
     * 本地播放最高位置
     */
    private LCDialog showDialogIsLoading,
            buyLcBean;//购买乐豆

    /**
     * 播放状态
     */
    private enum PLAYER_STATUS {
        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
    }

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // 解析播放
            analysisPlay(msg);
        }
    }

    Handler mUIHandler = new Handler() {
        public void handleMessage(Message msg) {
            // 解析msg
            analysisMessage(msg);
        }

    };

    // 网络连接失败
    private void netError(String msg) {
        // TODO Auto-generated method stub
        showToast(msg);
//        L.e("网络连接失败！");
        mBigPlayBtn.setVisibility(View.VISIBLE);
        NEWSTARTPLAY = true;
        mLastPos = mProgress.getProgress();
    }

    // 解析播放
    public void analysisPlay(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case EVENT_PLAY:
                uiPlay();
                break;
            default:
                break;
        }
    }

    // 播放
    public void uiPlay() {
        // TODO Auto-generated method stub
        /** 如果已经播放了，等待上一次播放结束 */
        if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
            synchronized (SYNC_Playing) {
                try {
                    SYNC_Playing.wait();
                    L.e(TAG, "wait player status to idle");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    L.e("播放器异常");
                }
            }
        }
        /** 设置播放url */
        mVV.setVideoPath(mVideoSource);
        /** 续播，如果需要如此 */
        if (mLastPos > 0) {
            mVV.seekTo(mLastPos);
            mLastPos = 0;
        }
        /** 显示或者隐藏缓冲提示 */
        mVV.showCacheInfo(true);
        START_V = System.currentTimeMillis();
        /** 开始播放 */
        mVV.start();
        mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
        L.e("开始播放");
        lc_startplay_buffer.setVisibility(View.INVISIBLE);
        // gif.setPaused(true);
    }

    protected void analysisMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            /**
             * 更新进度及时间
             */
            case UI_EVENT_UPDATE_CURRPOSITION:
                updateSeekBar();
                break;
            case UI_PLAYMAX:
                updatePlayMax();
                break;
            case UI_ISGONE:
                netError(msg.obj.toString());
                break;
            case UI_BUFFER:
                // int buffer = (Integer) msg.obj;
                // if (0 < buffer & buffer < 100) {
                //
                // } else {
                // lc_startplay_buffer.setVisibility(View.INVISIBLE);
                // // gif.setPaused(true);
                // gif.setVisibility(View.GONE);
                // }
                break;
            case UI_Completion:
                if (!ONAUSE) {
                    // 修改播放完成状态
                    L.e("修改用户状态");
                    playFinish();
                } else {
                    ONAUSE = false;
                }
                // }
                break;
            case UI_PULL:
                if (Integer.parseInt(msg.obj.toString()) < 6) {
                    playAndim(msg.obj.toString());
                } else {
                    mIntent();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 更新播放最大值
     */
    public void updatePlayMax() {
        int currPosition = mVV.getCurrentPosition();
        if (currPosition >= mLastPlayTime) {
            mLastPlayTime = currPosition;
        }
        mUIHandler.sendEmptyMessageDelayed(UI_PLAYMAX, 200);
    }


    /**
     * 更新进度条
     */
    protected void updateSeekBar() {
        // TODO Auto-generated method stub
        int currPosition = mVV.getCurrentPosition();
        int duration = mVV.getDuration();
        updateTextViewWithTimeFormat(mCurrPostion, currPosition);
        updateTextViewWithTimeFormat(mDuration, duration);
        mProgress.setMax(duration);
        mProgress.setProgress(currPosition);
        if (System.currentTimeMillis() - START_V > 5000) {
            lc_ctrl_all.setVisibility(View.INVISIBLE);
            mBigPlayBtn.setVisibility(View.INVISIBLE);
        } else {
            lc_ctrl_all.setVisibility(View.VISIBLE);
            mBigPlayBtn.setVisibility(View.VISIBLE);
        }
        mUIHandler.sendEmptyMessageDelayed(UI_EVENT_UPDATE_CURRPOSITION, 200);
    }

    // 课程观看完毕，可以领取任务
    private void netTask() {
        // TODO Auto-generated method stub
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("courseid", themeType.getCourseid());
        params.put("userid", LCConstant.userinfo.getUserid());
        params.put("uuid", getOnly());
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_RECEIVE_TASK, params,
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
//                                showToast(errorMessage);
                                LCUtils.ReLogin(errorCode, context, errorMessage);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            showToast("数据异常");
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        showToast(content);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }

                    ;
                });
    }

    // 播放完成UI操作
    protected void playFinish() {
        // TODO Auto-generated method stub

        updateVideoState(PlayCount);
        if (LCConstant.userinfo != null) {
            videoAll.getList().get(PlayCount).setViewstate("2");
        } else {
            videoAll.getList().get(PlayCount).setViewstate("0");
        }
        adapter.notifyDataSetChanged();
        PlayCount = isPlayed(PlayCount);
        PlayCount = mPause();
        if (isShowPull == 1) {
            PlayCount = videoAll.getList().size() + 1;
        }
        if (PlayCount < Integer.parseInt(videoAll.getTotal_count())) {
            mVideoSource = videoAll.getList().get(PlayCount).getVideourl();
            mVV.stopPlayback();
            startNewPlay();

            setSelection(PlayCount);
            isOnClick();
        } else {
            PlayCount = 0;
            mVideoSource = videoAll.getList().get(PlayCount).getVideourl();
            mVV.stopPlayback();
            startNewPlay();

            videoAll.getList().get(PlayCount).setViewstate("2");
            adapter.notifyDataSetChanged();
            /** 继续播放 */
            setSelection(PlayCount);
            isOnClick();
            ISPAUSE = true;
            if ("0".equals(themeType.getIsbuy())) {
                /**未购买*/
            } else {
                getTaskStatus();
            }
        }
    }

    /**
     * 发起一次新的播放任务
     */
    public void startNewPlay() {
        mEventHandler.removeMessages(EVENT_PLAY);
        mEventHandler.sendEmptyMessage(EVENT_PLAY);
    }

    /**
     * 领取任务
     */
    protected void mIntent() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(context, LCUploading.class);
        intent.putExtra("pst", themeType.getCourseid());
        intent.putExtra("video", "0");
        startActivity(intent);
        ISAINM = false;
        finish();
    }

    // 播放动画
    protected void playAndim(String string) {
        // TODO Auto-generated method stub
        switch (Integer.parseInt(string)) {
            case 1:
                lc_anim.setImageResource(R.drawable.anim_1);
                break;
            case 2:
                lc_anim.setImageResource(R.drawable.anim_2);
                break;
            case 3:
                lc_anim.setImageResource(R.drawable.anim_3);
                break;
            case 4:
                lc_anim.setImageResource(R.drawable.anim_4);
                break;
        }
    }

    // 校验任务状态
    protected void getTaskStatus() {
        // TODO Auto-generated method stub
        switch (Integer.parseInt(videoAll.getTaskstate())) {
            case 0:// 未看完 未领取
                if (LCConstant.userinfo != null) {
                    netTask();
                }
                lc_pull_view.setVisibility(View.VISIBLE);
                lc_scroll.setVisibility(View.GONE);
                lc_video_listvideoview.setVisibility(View.GONE);
                break;
            case 1:// 看完 未领取
                lc_pull_view.setVisibility(View.VISIBLE);
                lc_scroll.setVisibility(View.GONE);
                lc_video_listvideoview.setVisibility(View.GONE);
                break;
            case 2:// 看完 领取
                /**
                 * wpt
                 * 2015/9/21 15:11
                 * 如果看完了领取玩任务，就直接跳转到上传任务
                 *
                 */
                mVV.stopPlayback();
                Intent intent = new Intent(VideoActivity.this, LCUploading.class);
                intent.putExtra("pst", themeType.getCourseid());
                intent.putExtra("video", "0");
                intent.putExtra("ing", 0);
                startActivity(intent);
                ISAINM = false;
                finish();

//                lc_pull_view.setVisibility(View.VISIBLE);
//                lc_scroll.setVisibility(View.GONE);
//                lc_video_listvideoview.setVisibility(View.GONE);
                break;
            case 3:// 看完 完成
                lc_pull_view.setVisibility(View.GONE);
                lc_scroll.setVisibility(View.GONE);
                lc_video_listvideoview.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_video);
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        // 不锁屏
        mPower();
        // 获取视频数据，并且赋值URL
        getDate();

        initUI();
        isShowTask();
        // 开启后台事件处理线程
        startHT();


    }

    private void startHT() {
        // TODO Auto-generated method stub
        mHandlerThread = new HandlerThread("event handler thread",
                Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mEventHandler = new EventHandler(mHandlerThread.getLooper());
    }

    private void getDate() {
        // TODO Auto-generated method stub
        Intent intent = getIntent();
        themeType = (ThemeType) intent.getSerializableExtra("themeType");
        videoAll = (VideoAll) intent.getSerializableExtra("videoAll");
        bd = (Bundle) intent.getBundleExtra("bundle");
        isShowPull = bd.getInt("isShowPull");
        PlayCount = isPlay(PlayCount);
        mVideoSource = videoAll.getList().get(PlayCount).getVideourl();
        qList = intent.getStringArrayListExtra("questionid");
    }


    //判断是否购买0:未购买1：已购买
    private void isBuy(String isbuy) {
        if ("0".equals(isbuy)) {
            lc_video_lceBeanall.setVisibility(View.VISIBLE);
            lc_video_tasklcbean.setText(themeType.getPrice());
            lc_video_tasklcbeanOld.setText(themeType.getOldPrice());
        } else {
            lc_video_lceBeanall.setVisibility(View.GONE);
        }

    }

    private void mPower() {
        // TODO Auto-generated method stub
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ON_AFTER_RELEASE, POWER_LOCK);
    }


    private void isShowTask() {
        // ：0-未学完，未领取；1-学完，未领取；2-已学完，已领取，未完成；3-已完成
        if (LCConstant.userinfo != null) {
            if (!TextUtils.isEmpty(videoAll.getTaskstate())) {
                int parseInt = Integer.parseInt(videoAll.getTaskstate());
                switch (parseInt) {
                    case 0:
                        // lc_pull_view.setVisibility(View.VISIBLE);
                        // lc_scroll.setVisibility(View.GONE);
                        // lc_studied_ListView.setVisibility(View.GONE);
                        break;
                    case 1:
                        // lc_pull_view.setVisibility(View.VISIBLE);
                        // lc_scroll.setVisibility(View.GONE);
                        // lc_studied_ListView.setVisibility(View.GONE);
                        //2015/9/21  19:07  修改  区分来源显示
                        if (LCConstant.userinfo != null) {
//                            netTask();
                            if (isShowPull == 1) {
                                lc_pull_view.setVisibility(View.VISIBLE);
                                lc_scroll.setVisibility(View.GONE);
                                lc_studied_ListView.setVisibility(View.GONE);
                            }
                        }
                        break;
                    case 2:
                        // lc_pull_view.setVisibility(View.VISIBLE);
                        // lc_scroll.setVisibility(View.GONE);
                        // lc_studied_ListView.setVisibility(View.GONE);
                        break;
                    case 3:
                        // lc_pull_view.setVisibility(View.VISIBLE);
                        // lc_scroll.setVisibility(View.GONE);
                        // lc_studied_ListView.setVisibility(View.GONE);
                        break;
                }
            }
        }

    }

    protected int isPlayed(int k) {
        // TODO Auto-generated method stub
        if (LCConstant.userinfo != null) {
            for (int r = 0; r < videoAll.getList().size(); r++) {
                // 如果是未播放的
                if ("0".equals(videoAll.getList().get(r).getViewstate())) {
                    return r;
                    // 如果是播放中的
                } else if ("1".equals(videoAll.getList().get(r).getViewstate())) {
                    return r;
                }
            }
        }
        return k = k + 1;
    }

    protected int isPlay(int k) {
        // TODO Auto-generated method stub
        if ("0".equals(themeType.getIsbuy()) & LCConstant.islogin) {
            return k = 0;
        }
        for (int r = 0; r < videoAll.getList().size(); r++) {
            // 如果是未播放的
            if ("0".equals(videoAll.getList().get(r).getViewstate())) {
                return r;
                // 如果是播放中的
            } else if ("1".equals(videoAll.getList().get(r).getViewstate())) {
                return r;
            }
        }

        return k;
    }

    // 修改视频状态（播放结束）
    private void updateVideoState(int k) {
        // TODO Auto-generated method stub
        // 观看结束修改状态
        L.e("修改用户状态1" + videoAll.getList().get(k).getViewstate());
        if (!"2".equals(videoAll.getList().get(k).getViewstate())) {
            L.e("修改用户状态2");
            videoAll.getList().get(k).setLastplaydate(0 + "");
            recordVideoPlayTime(2 + "", 0 + "", videoAll.getList().get(k)
                    .getVideoid(), false);
        } else if ("2".equals(videoAll.getList().get(k).getViewstate())) {
            videoAll.getList().get(k).setLastplaydate(0 + "");
            recordVideoPlayTime(2 + "", 0 + "", videoAll.getList().get(k)
                    .getVideoid(), false);
        }
    }

    // 修改视频状态（返回键播放进行中）
    private void updateVideoStatePlaying(int k, String time) {
        if ("0".equals(videoAll.getList().get(k).getViewstate())) {
            recordVideoPlayTime(1 + "", time, videoAll.getList().get(k)
                    .getVideoid(), true);
        } else if ("1".equals(videoAll.getList().get(k).getViewstate())) {
            recordVideoPlayTime(1 + "", time, videoAll.getList().get(k)
                    .getVideoid(), true);
        } else if ("2".equals(videoAll.getList().get(k).getViewstate())) {
            recordVideoPlayTime(2 + "", time, videoAll.getList().get(k)
                    .getVideoid(), true);
        } else {
            finish();
        }
    }
    // 修改视频状态(切换视频)
    private void changeVideoStatePlaying(int k, String time, int position) {
        L.e("视频状态" + videoAll.getList().get(position).getViewstate());
        if ("0".equals(videoAll.getLock()) & LCConstant.userinfo == null) {
            mVV.pause();
            adapter.notifyDataSetChanged();
            isCallback = false;
            PlayCount = position;
            setSelection(position);
            mVideoSource = videoAll.getList().get(position).getVideourl();
            mVV.stopPlayback();

            /** 发起一次新的播放任务 */
            mEventHandler.removeMessages(EVENT_PLAY);
            mEventHandler.sendEmptyMessage(EVENT_PLAY);
            isOnClick();
        } else {
            if ("0".equals(videoAll.getList().get(position).getViewstate())) {
                 recordVideoTime(1 + "", time, videoAll.getList().get(k)
                 .getVideoid(), position);

            } else if ("1".equals(videoAll.getList().get(position)
                    .getViewstate())) {
                recordVideoTime(1 + "", time, videoAll.getList().get(k)
                        .getVideoid(), position);
            } else if ("2".equals(videoAll.getList().get(position)
                    .getViewstate())) {
                if (LCConstant.userinfo != null) {
                    recordVideoTime(2 + "", time, videoAll.getList().get(k)
                            .getVideoid(), position);
                    mVV.pause();
                }
            }
        }
    }

    // 修改视频状态（播放过程中切换视频）
    private void updateVideoStatePlaying(int k, String time, int position) {
        L.e("视频状态" + videoAll.getList().get(position).getViewstate());
        if ("0".equals(videoAll.getLock()) & LCConstant.userinfo == null) {
            mVV.pause();
            adapter.notifyDataSetChanged();
            isCallback = false;
            PlayCount = position;
            setSelection(position);
            mVideoSource = videoAll.getList().get(position).getVideourl();
            mVV.stopPlayback();

            /** 发起一次新的播放任务 */
            mEventHandler.removeMessages(EVENT_PLAY);
            mEventHandler.sendEmptyMessage(EVENT_PLAY);
            isOnClick();
        } else {
            if ("0".equals(videoAll.getList().get(position).getViewstate())) {
                // recordVideoTime(1 + "", time, videoAll.getList().get(k)
                // .getVideoid(), position);
                showToast("请按课程顺序播放该视频！");
            } else if ("1".equals(videoAll.getList().get(position)
                    .getViewstate())) {
                showToast("请按课程顺序播放该视频！");
            } else if ("2".equals(videoAll.getList().get(position)
                    .getViewstate())) {
                if (LCConstant.userinfo != null) {
                    recordVideoTime(2 + "", time, videoAll.getList().get(k)
                            .getVideoid(), position);
                    mVV.pause();
                }
            }
        }
    }

    // 用户观看视频记录接口（播放过程中切换视频）
    private void recordVideoTime(String viewstate, final String lastplaydate,
                                 String videoid, final int position) {
        if (LCConstant.userinfo != null) {
            AbRequestParams params = new AbRequestParams();
            params.put("viewstate", viewstate + "");
            params.put("lastplaytime", lastplaydate);
            params.put("videoid", videoid);
            if (LCConstant.userinfo != null) {
                params.put("userid", LCConstant.userinfo.getUserid());
            } else {
                params.put("userid", 0 + "");
            }
            params.put("uuid", getOnly());
            mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_PLAY_RECORD, params,
                    new AbStringHttpResponseListener() {

                        // 获取数据成功会调用这里
                        @Override
                        public void onSuccess(int statusCode, String content) {
                            String loadConvert = loadConvert(content);
                            L.e(loadConvert);
                            try {
                                JSONObject jo = new JSONObject(loadConvert);
                                String errorCode = jo.optString("errorCode",
                                        "1");
                                String errorMessage = jo.optString(
                                        "errorMessage", "1");
                                if ("0".equals(errorCode)) {

                                } else {
//                                    LCUtils.ReLogin(errorCode, context);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                // showToast("数据异常");
                            }
                        }


                        // 开始执行前
                        @Override
                        public void onStart() {
                            // 显示进度框
                            AbDialogUtil.showProgressDialog(context, 0,
                                    "正在保存视频状态...");

                        }

                        // 失败，调用
                        @Override
                        public void onFailure(int statusCode, String content,
                                              Throwable error) {
                        }

                        // 完成后调用，失败，成功
                        @Override
                        public void onFinish() {

                            // 移除进度框
                            AbDialogUtil.removeDialog(context);

                            videoAll.getList().get(PlayCount)
                                    .setLastplaydate(lastplaydate + "");
                            adapter.notifyDataSetChanged();
                            isCallback = false;
                            PlayCount = position;
                            setSelection(position);
                            mVideoSource = videoAll.getList().get(position)
                                    .getVideourl();
                            mVV.stopPlayback();

                            /**
                             * 发起一次新的播放任务
                             */
                            mEventHandler.removeMessages(EVENT_PLAY);
                            mEventHandler.sendEmptyMessage(EVENT_PLAY);
                            isOnClick();
                        }

                        ;

                    });
        }
    }

    // 用户观看视频记录接口
    private void recordVideoPlayTime(String viewstate, String lastplaydate,
                                     String videoid, final boolean isBack) {
        if (LCConstant.userinfo != null) {
            // 绑定参数
            AbRequestParams params = new AbRequestParams();
            params.put("viewstate", viewstate + "");
            params.put("lastplaytime", lastplaydate);
            params.put("videoid", videoid);
            if (LCConstant.userinfo != null) {
                params.put("userid", LCConstant.userinfo.getUserid());
            } else {
                params.put("userid", 0 + "");
            }
            params.put("uuid", getOnly());
            mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_PLAY_RECORD, params,
                    new AbStringHttpResponseListener() {

                        // 获取数据成功会调用这里
                        @Override
                        public void onSuccess(int statusCode, String content) {
                            String loadConvert = loadConvert(content);
                            L.e(loadConvert);
                            try {
                                JSONObject jo = new JSONObject(loadConvert);
                                String errorCode = jo.optString("errorCode",
                                        "1");
                                String errorMessage = jo.optString(
                                        "errorMessage", "1");
                                if ("0".equals(errorCode)) {

                                } else {
//                                    showToast(errorMessage);
//                                    LCUtils.ReLogin(errorCode, context);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                // showToast("数据异常");
                            }
                        }

                        // 开始执行前
                        @Override
                        public void onStart() {
                            // 显示进度框
                            if (isBack) {
                                AbDialogUtil.showProgressDialog(context, 0,
                                        "正在保存视频状态...");
                            }
                        }

                        // 失败，调用
                        @Override
                        public void onFailure(int statusCode, String content,
                                              Throwable error) {
//                            AbToastUtil.showToast(context, error.getMessage());
                        }

                        // 完成后调用，失败，成功
                        @Override
                        public void onFinish() {
                            if (isBack) {
                                // 移除进度框
                                AbDialogUtil.removeDialog(context);
                                finish();
                            }
                        }
                    });
        } else {
            if (isBack) {
                finish();
            }
        }
    }

    /**
     * 初始化界面
     */
    private void initUI() {

        mPlaybtn = (ImageView) findViewById(R.id.lc_pause);
        mBigPlayBtn = (ImageView) findViewById(R.id.lc_startplay);
        videoviewholder = (RelativeLayout) findViewById(R.id.videoviewholder);
        lc_video_listvideoview = (RelativeLayout) findViewById(R.id.lc_video_listvideoview);
        mProgress = (SeekBar) findViewById(R.id.media_progress);
        mDuration = (TextView) findViewById(R.id.lc_allplaytime);
        mCurrPostion = (TextView) findViewById(R.id.lc_playtime);
        lc_ctrl_all = (RelativeLayout) findViewById(R.id.lc_ctrl_all);
        lc_anim = (ImageView) findViewById(R.id.lc_anim);
        lc_videoinfo_synopsis = (TextView) findViewById(R.id.lc_videoinfo_synopsis);
        lc_srttext = (TextView) findViewById(R.id.lc_srttext);
        lc_zoom = (ImageView) findViewById(R.id.lc_zoom);
        lc_video_title = (LCTitleBar) findViewById(R.id.lc_video_title);
        lc_video_title.setCenterTitle(themeType.getCoursename() + "");
        lc_video_title.setBackGb(getResources().getColor(R.color.somber_title_bg));
        lc_video_title.setOnclickBackListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                synchronized (SYNC_Playing) {
                    SYNC_Playing.notify();
                }
                mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
                mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
                mUIHandler.removeMessages(UI_PLAYMAX);
                isCallback = false;
                mLastPos = mVV.getCurrentPosition();
                updateVideoStatePlaying(PlayCount, mLastPos + "");
            }
        });
        lc_zoom.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 强制为横屏
                }
            }
        });

        lc_scroll = (ScrollView) findViewById(R.id.lc_scroll);
        // videotitle
        lc_viedo_info = (RelativeLayout) findViewById(R.id.lc_viedo_info);
        lc_videoinfo_text = (TextView) findViewById(R.id.lc_videoinfo_text);
        lc_videoinfo_arrow = (ImageView) findViewById(R.id.lc_videoinfo_arrow);
        lc_studied_ListView = (ListView) findViewById(R.id.lc_studied_ListView);
        lc_startplay_buffer = (TextView) findViewById(R.id.lc_startplay_buffer);
        slideView = (SlideView) findViewById(R.id.pullsliderd);
        lc_pull_view = (RelativeLayout) findViewById(R.id.lc_pull_view);
        //乐豆购买
        lc_video_lceBeanall = (RelativeLayout) findViewById(R.id.lc_video_lceBeanall);
        lc_video_tasklcbean = (TextView) findViewById(R.id.lc_video_tasklcbean);
        lc_video_tasklcbeanOld = (TextView) findViewById(R.id.lc_video_tasklcbeanOld);
        lc_video_tasklcbeanOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        lc_video_lceBeanbt = (Button) findViewById(R.id.lc_video_lceBeanbt);
        lc_video_lceBeanall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        lc_video_lceBeanbt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LCConstant.islogin) {
                    //购买课程
                    setBuyLcBean();
                } else {
                    Intent intent = new Intent(context, LCUserLoginAndRegister.class);
                    intent.putExtra("mIntent", 8);
                    startActivity(intent);
                }

            }
        });

        lc_startplay_buffer.setVisibility(View.INVISIBLE);
        adapter = new LCStudiedAdapter(context, videoAll.getList());
        lc_studied_ListView.setAdapter(adapter);

        isOnClick();
        registerCallbackForControl();
        initPlay();
        isBuy(themeType.getIsbuy());
        // 设置正在播放中
        // videoAll.getList().get(0).setPlaying("true");
        setSelection(PlayCount);
        try {
            lc_srttext.setText(videoAll.getList().get(0).getNote());
        } catch (Exception e) {
        }

        lc_studied_ListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                int currentPosition = mVV.getCurrentPosition();
                changeVideoStatePlaying(PlayCount, currentPosition + "",
                        position);
            }
        });
    }

    /**
     * wpt
     * 2015/9/8 15:11
     * 购买乐豆
     */
    public void setBuyLcBean() {

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("courseid", themeType.getCourseid());
        params.put("uuid", LCUtils.getOnly(context));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.LEDOU_BUY_COURSE, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        buyLcBeanSuccess(content);
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
                        showToast("购买失败！");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(getContext());
                        if (buyLcBean != null) {
                            buyLcBean.cancel();
                        }
                    }

                });
    }

    /**
     * wpt
     * 2015/9/8 15:11
     * 购买返回成功
     */
    public void buyLcBeanSuccess(String content) {
        String loadConvert = loadConvert(content);
        L.e(loadConvert);
        try {
            JSONObject jo = new JSONObject(loadConvert);
            String errorCode = jo.optString("errorCode",
                    "1");
            String errorMessage = jo.optString(
                    "errorMessage", "1");
            if ("0".equals(errorCode)) {

                themeType.setIsbuy("1");
                lc_video_lceBeanall.setVisibility(View.GONE);
                /**
                 * wpt
                 * 2015/9/8 15:11
                 * 添加回调
                 *
                 */
                if (buyCallBack != null) {
                    buyCallBack.onSuccess(bd);
                }
                showToast("购买成功！");
            } else {
                if("20048".equals(errorCode)){
                    buyLcBean = showDialog(null, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buyLcBean.cancel();
                        }
                    }, "您的乐豆不足，\n不能解锁课程？", "取消", "确定");
                }else{
                    LCUtils.ReLogin(errorCode, context, errorMessage);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            showToast("数据异常");
        }
    }


    /**
     * 2015/9/8 14:25 修改添加当播放为课程动画的时候显示为“课程简介”
     */
    private void setmTitle() {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(videoAll.getList().get(PlayCount).getVideoname())) {
            L.e("长度：" + videoAll.getList().get(PlayCount).getVideoname().length());
            if (0 == PlayCount) {
                lc_videoinfo_synopsis.setVisibility(View.VISIBLE);
                lc_videoinfo_arrow.setVisibility(View.VISIBLE);
            } else {
                lc_videoinfo_synopsis.setVisibility(View.GONE);
                lc_videoinfo_arrow.setVisibility(View.GONE);
            }
            if (0 == PlayCount) {
                lc_videoinfo_text.setText("课程简介");
            } else {
                if (videoAll.getList().get(PlayCount).getVideoname().length() > 50) {
                    lc_videoinfo_text.setText(videoAll.getList().get(PlayCount)
                            .getVideoname().substring(0, 50)
                            + "...");
                } else {
                    lc_videoinfo_text.setText(videoAll.getList().get(PlayCount)
                            .getVideoname());
                }
            }
        } else {
            lc_videoinfo_text.setText(videoAll.getList().get(PlayCount)
                    .getVideoname());
        }
    }

    // 初始化播放器设置
    private void initPlay() {
        // TODO Auto-generated method stub
        /**
         * 设置ak及sk的前16位
         */
        BVideoView.setAKSK(AK, SK);
        /**
         * 获取BVideoView对象
         */
        mVV = (BVideoView) findViewById(R.id.video_view);
        /**
         * 注册listener
         */
        mVV.setOnPreparedListener(this);
        mVV.setOnCompletionListener(this);
        mVV.setOnErrorListener(this);
        mVV.setOnInfoListener(this);
        mVV.setOnPlayingBufferCacheListener(this);
        /**
         * 设置解码模式
         */
        mVV.setDecodeMode(mIsHwDecode ? BVideoView.DECODE_HW
                : BVideoView.DECODE_SW);
    }

    // 判断是否可点击
    public void isOnClick() {
        setmTitle();
        // 未登录状态下
        if ("0".equals(videoAll.getLock()) & LCConstant.userinfo == null) {
//            mProgress.setEnabled(true);
            if (!TextUtils.isEmpty(videoAll.getList().get(PlayCount)
                    .getLastplaydate())) {
                mLastPos = Integer.parseInt(videoAll.getList().get(PlayCount)
                        .getLastplaydate());
                setmLastPlayTime(mLastPos);
            }

        } else {
            // 登录状态下
            if (TextUtils.isEmpty(videoAll.getList().get(PlayCount)
                    .getViewstate())) {
//                mProgress.setEnabled(false);
            } else {
                int Videostate = Integer.parseInt(videoAll.getList()
                        .get(PlayCount).getViewstate());
                switch (Videostate) {
                    case 0:
//                        mProgress.setEnabled(false);
                        if (!TextUtils.isEmpty(videoAll.getList().get(PlayCount)
                                .getLastplaydate())) {
                            mLastPos = Integer.parseInt(videoAll.getList()
                                    .get(PlayCount).getLastplaydate());
                            setmLastPlayTime(mLastPos);
                        }

                        break;
                    case 1:
//                        mProgress.setEnabled(false);
                        if (!TextUtils.isEmpty(videoAll.getList().get(PlayCount)
                                .getLastplaydate())) {
                            mLastPos = Integer.parseInt(videoAll.getList()
                                    .get(PlayCount).getLastplaydate());
                            setmLastPlayTime(mLastPos);
                        }

                        break;
                    case 2:
//                        mProgress.setEnabled(true);
                        if (!TextUtils.isEmpty(videoAll.getList().get(PlayCount)
                                .getLastplaydate())) {
                            mLastPos = Integer.parseInt(videoAll.getList()
                                    .get(PlayCount).getLastplaydate());
                            setmLastPlayTime(mLastPos);
                        }

                        break;
                }
            }
        }
    }

    /**
     * <pre>
     * 方法说明：  设置添加最后-播放时间
     * 编写日期:	2015年5月21日
     * 编写人员:   吴培涛
     * </pre>
     */
    public void setmLastPlayTime(int mLP) {
        if (mLP > 0) {
            mLastPlayTime = mLP;
        } else {
            mLastPlayTime = 0;
        }
    }


    /**
     * mEventHandler.removeMessages(EVENT_PLAY); //
     * mEventHandler.sendEmptyMessage(EVENT_PLAY); 为控件注册回调处理函数
     */
    private void registerCallbackForControl() {
        mPlaybtn.setImageResource(R.drawable.pause);
        mBigPlayBtn.setImageResource(R.drawable.stopplay);
        mBigPlayBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setPlayStatus();
            }
        });
        mPlaybtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setPlayStatus();
            }
        });
        slideView.setSlideListener(new SlideView.SlideListener() {
            @Override
            public void onDone() {
                if (LCConstant.userinfo != null) {
                    slideView.setVisibility(View.GONE);
                    lc_anim.setVisibility(View.VISIBLE);
                    starPlayAnim();
                } else {
                    showDialogIsLoading = showDialogIsLoading(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    resetPull();
                                }
                            }, new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    Intent intent = new Intent(
                                            VideoActivity.this,
                                            LCUserLoginAndRegister.class);

                                    startActivity(intent);
                                    resetPull();
                                }
                            }, "请登录后再进行操作！", "取消", "确定");
                }
            }
        });
        videoviewholder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                START_V = System.currentTimeMillis();
            }
        });
        lc_viedo_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (PlayCount == 0) {
                    setVisible();
                }
            }
        });

        OnSeekBarChangeListener osbc1 = new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                if (isShowPull == 1) {
                    if (mVV.isPlaying()) {
                        mPlaybtn.setImageResource(R.drawable.stary_play);
                        mBigPlayBtn.setImageResource(R.drawable.startplay);
                        /** 暂停播放 */
                        mVV.pause();
                    }
                }
                updateTextViewWithTimeFormat(mCurrPostion, progress);

                isProChange(seekBar, progress);
                isPause();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                /** SeekBar开始seek时停止更新 */
                mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                isProGressDrag(seekBar);
            }
        };
        mProgress.setOnSeekBarChangeListener(osbc1);
    }

    /**
     * wpt
     * 2015/9/8 17:22
     * 判断是否能拖动
     *
     * @param seekBar
     */
    private void isProChange(SeekBar seekBar, int progress) {

        int Videostate = Integer.parseInt(videoAll.getList()
                .get(PlayCount).getViewstate());
        int iseekPos = seekBar.getProgress();
        switch (Videostate) {
            case 0://为观看
                L.e(iseekPos + "/w  " + mVV.getCurrentPosition());
//                    if (iseekPos <=  mVV.getCurrentPosition()) {
                isQusetion(mVV.getCurrentPosition());
//                    }
                break;
            case 1://观看中
                L.e(iseekPos + "/e   " + mVV.getCurrentPosition());
//                    if (iseekPos <=  mVV.getCurrentPosition()) {
                isQusetion(mVV.getCurrentPosition());
//                    }
                break;
            case 2://已观看
                break;
        }

    }

    /**
     * pwt
     * 2015/9/8 17:22
     * 判断是否能拖动
     *
     * @param seekBar
     */
    private void isProGressDrag(SeekBar seekBar) {
        if (!LCConstant.islogin) {
            ProGressDrag(seekBar);
        } else {
            int Videostate = Integer.parseInt(videoAll.getList()
                    .get(PlayCount).getViewstate());
            switch (Videostate) {
                case 0://为观看
                    unProGressDrag(seekBar);
                    break;
                case 1://观看中
                    unProGressDrag(seekBar);
                    break;
                case 2://已观看
                    ProGressDrag(seekBar);
                    break;
            }
        }
        /**
         * SeekBark完成seek时执行seekTo操作并更新界面
         */
        mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
    }

    /**
     * pwt
     * 2015/9/8 17:22
     * 能拖动
     *
     * @param seekBar
     */
    public void ProGressDrag(SeekBar seekBar) {
        int iseekPos = seekBar.getProgress();
        mVV.seekTo(iseekPos);
    }

    /**
     * pwt
     * 2015/9/8 17:22
     * 不能拖动
     *
     * @param seekBar
     */
    public void unProGressDrag(SeekBar seekBar) {
        int iseekPos = seekBar.getProgress();
//        if (iseekPos <= mLastPlayTime) {
            mVV.seekTo(iseekPos);
//        } else {
//            if (mVV.getCurrentPosition() != mLastPlayTime) {
//                mVV.seekTo(mLastPlayTime);
//            }
//        }
    }

    public void resetPull() {
        showDialogIsLoading.cancel();
        lc_pull_view.setVisibility(View.GONE);
        lc_scroll.setVisibility(View.GONE);
        lc_video_listvideoview.setVisibility(View.VISIBLE);
        slideView.reset();
    }

    // 设置播放状态
    protected void setPlayStatus() {
        // TODO Auto-generated method stub
        START_V = System.currentTimeMillis();
        if (!NEWSTARTPLAY) {
            if (mVV.isPlaying()) {
                mPlaybtn.setImageResource(R.drawable.stary_play);
                mBigPlayBtn.setImageResource(R.drawable.startplay);
                /** 暂停播放 */
                mVV.pause();
            } else {
                mPlaybtn.setImageResource(R.drawable.pause);
                mBigPlayBtn.setImageResource(R.drawable.stopplay);
                /** 继续播放 */
                mVV.resume();
            }
        } else {
            NEWSTARTPLAY = false;
            mEventHandler.removeMessages(EVENT_PLAY);
            mEventHandler.sendEmptyMessage(EVENT_PLAY);
            mBigPlayBtn.setVisibility(View.GONE);
        }
    }


    public int mPause() {
        if (TextUtils.isEmpty(themeType.getIsbuy())) {
            return PlayCount;
        }
        if (!LCConstant.islogin) {
            return PlayCount;
        }
        if ("0".equals(themeType.getIsbuy())) {
            return PlayCount = Integer.parseInt(videoAll.getTotal_count()) + 1;
        } else {
            return PlayCount;
        }
    }


    // 设置布局的状态
    protected void setVisible() {
        // TODO Auto-generated method stub
        if (lc_pull_view.getVisibility() == View.VISIBLE) {

        } else {
            if (lc_scroll.getVisibility() == View.GONE) {
                lc_scroll.setVisibility(View.VISIBLE);
                lc_videoinfo_arrow.setImageResource(R.drawable.arrowup);
                lc_video_listvideoview.setVisibility(View.GONE);
            } else {
                lc_scroll.setVisibility(View.GONE);
                lc_videoinfo_arrow.setImageResource(R.drawable.arrowdown);
                lc_video_listvideoview.setVisibility(View.VISIBLE);
            }
        }
    }

    // 启动动画
    protected void starPlayAnim() {
        // TODO Auto-generated method stub
        new Thread() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                super.run();
                int u = 0;
                int p = 0;
                while (ISAINM) {
                    u++;
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                    }
                    if (p > 2) {
                        u = 6;
                        p = 0;
                    }
                    Message obtainMessage = mUIHandler.obtainMessage(UI_PULL, u
                            + "");
                    mUIHandler.sendMessage(obtainMessage);
                    if (u > 4) {
                        u = 0;
                        p++;
                    }
                }
            }
        }.start();
    }

    // 是否显示问题对话框
    protected void isQusetion(int progress) {
        // TODO Auto-generated method stub
        if ("0".equals(videoAll.getLock()) & LCConstant.userinfo == null) {

            List<Question> listQuestion = videoAll.getList().get(PlayCount)
                    .getListQuestion();
            /**进入问题列表是否为空*/
            if (listQuestion != null) {
                Question showQuestion = isShowQuestion(progress);
                /**避免对话框重复加载*/
                if (!ISSHOWSECOND) {
                    /**判断问题对象是否为为空*/
                    if (showQuestion != null) {
                        if (isNotAnswer(showQuestion.getQuestionid())) {
                            /**显示问题，暂停视频*/
                            /** 暂停播放 */
                            mVV.pause();
                            mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
                            mUIHandler.removeMessages(UI_PLAYMAX);
                            LCPlayUtils lc = new LCPlayUtils(
                                    showQuestion.getMp3url());
                            Video video = videoAll.getList().get(PlayCount);
                            showQuestion(showQuestion, "确定", lc, video);
                            ISSHOWSECOND = true;
                        } else {
                            showQuestion.setQuestionstatus("1");
                        }
                    }
                }
            }

        } else {
            if ("0".equals(videoAll.getList().get(PlayCount).getViewstate())
                    || "1".equals(videoAll.getList().get(PlayCount)
                    .getViewstate())) {

                List<Question> listQuestion = videoAll.getList().get(PlayCount)
                        .getListQuestion();
                if (listQuestion != null) {
                    Question showQuestion = isShowQuestion(progress);
                    if (!ISSHOWSECOND) {
                        if (showQuestion != null) {
                            if (isNotAnswer(showQuestion.getQuestionid())) {
                                /** 暂停播放 */
                                mVV.pause();
                                LCPlayUtils lc = new LCPlayUtils(
                                        showQuestion.getMp3url());
                                Video video = videoAll.getList().get(PlayCount);
                                showQuestion(showQuestion, "确定", lc, video);

                                ISSHOWSECOND = true;
                            } else {
                                showQuestion.setQuestionstatus("1");
                            }
                        }
                    }
                }
            }
        }
    }


    public boolean isNotAnswer(String id) {
        boolean isnot = true;
        for (int i = 0; i < qList.size(); i++) {
            if (id.equals(qList.get(i))) {
                isnot = false;
                break;
            }
        }

        return isnot;

    }

    // 锁屏后进入程序或者是否停止
    protected void isPause() {
        // TODO Auto-generated method stub
        if (ISPAUSE) {
            if (mVV.isPlaying()) {
                mPlaybtn.setImageResource(R.drawable.stary_play);
                mBigPlayBtn.setImageResource(R.drawable.startplay);
                /**
                 * 暂停播放
                 */
                mVV.pause();
            }
            ISPAUSE = false;
        }
    }

    // 是否应该显示问题，显示那条问题
    public Question isShowQuestion(int t) {
        Question question = null;

        for (int y = 0; y < videoAll.getList().get(PlayCount).getListQuestion()
                .size(); y++) {

            if (t >= Integer.parseInt(videoAll.getList().get(PlayCount)
                    .getListQuestion().get(y).getStart())
                    & t < Integer.parseInt(videoAll.getList().get(PlayCount)
                    .getListQuestion().get(y).getEnd())) {

                if ("0".equals(videoAll.getList().get(PlayCount)
                        .getListQuestion().get(y).getQuestionstatus())) {
                    question = videoAll.getList().get(PlayCount)
                            .getListQuestion().get(y);
                    videoAll.getList().get(PlayCount).getListQuestion().get(y)
                            .setQuestionstatus(1 + "");
                }
                break;
            } else {

                if ("0".equals(videoAll.getLock())
                        & LCConstant.userinfo == null) {

                    if ("0".equals(videoAll.getList().get(PlayCount)
                            .getListQuestion().get(y).getQuestionstatus())) {

                        if (t > Integer.parseInt(videoAll.getList()
                                .get(PlayCount).getListQuestion().get(y)
                                .getStart())) {

                            if (t != mProgress.getMax()) {
                                mVV.seekTo(Integer.parseInt(videoAll.getList()
                                        .get(PlayCount).getListQuestion()
                                        .get(y).getStart()) - 5);
                                break;
                            }
                        }


                    }
                }
            }
        }
        return question;
    }

    public String getFromAssets(String fileName) {
        String text = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            text = Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    private void updateTextViewWithTimeFormat(TextView view, int second) {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = null;
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }
        view.setText(strTemp);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        /**
         * 在停止播放前 你可以先记录当前播放的位置,以便以后可以续播
         */
        if (mPlayerStatus == PLAYER_STATUS.PLAYER_PREPARED) {
            mLastPos = mVV.getCurrentPosition();
            if (!mVV.isPlaying()) {
                // 判断关闭时是否播放
                ISPAUSE = true;
            }
            mVV.stopPlayback();
            isCallback = false;
            ONAUSE = true;
        }
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        //
        isCallback = true;
        ISAINM = true;
        ONAUSE = false;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        if (null != mWakeLock && (!mWakeLock.isHeld())) {
            mWakeLock.acquire();
        }
        /**
         * 发起一次播放任务,当然您不一定要在这发起
         */
        mEventHandler.sendEmptyMessage(EVENT_PLAY);
    }

    private long mTouchTime;
    private boolean barShow = true;

    // 显示播放到那条
    public void setSelection(int i) {
        for (int u = 0; u < videoAll.getList().size(); u++) {
            if (i == u) {
                videoAll.getList().get(u).setPlaying(true + "");
            } else {
                videoAll.getList().get(u).setPlaying(false + "");
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    Handler mHandler = new Handler();

    @Override
    public boolean onInfo(int what, final int extra) {
        // TODO Auto-generated method stub
        switch (what) {
            /**
             * 开始缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_START:
                // mHandler.post(new Runnable() {
                //
                // @Override
                // public void run() {
                // // TODO Auto-generated method stub
                //
                // lc_startplay_buffer.setText("正在缓冲" + extra + "%");
                // lc_startplay_buffer.setVisibility(View.VISIBLE);
                // gif.setVisibility(View.VISIBLE);
                // }
                // });
                break;
            /**
             * 结束缓冲
             */
            case BVideoView.MEDIA_INFO_BUFFERING_END:
                // mHandler.post(new Runnable() {
                //
                // @Override
                // public void run() {
                // // TODO Auto-generated method stub
                // lc_startplay_buffer.setVisibility(View.INVISIBLE);
                // // gif.setPaused(true);
                // gif.setVisibility(View.GONE);
                // }
                // });
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 当前缓冲的百分比， 可以配合onInfo中的开始缓冲和结束缓冲来显示百分比到界面
     */
    @Override
    public void onPlayingBufferCache(int percent) {
        // TODO Auto-generated method stub
        Message obtainMessage = mUIHandler.obtainMessage(UI_BUFFER, percent);
        mUIHandler.sendMessage(obtainMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isCallback = false;
        ISAINM = false;
        /**
         * 退出后台事件处理线程
         */
        mHandlerThread.quit();
        EventBus.getDefault().unregister(this);
    }


    public void onEvent(Object event) {
        if((Integer) event== AbAppConfig.CENTER_FRAGMENT_FINISH){
            finish();
        }
    }

    /**
     * 播放出错
     */
    @Override
    public boolean onError(int what, int extra) {
        // TODO Auto-generated method stub
        L.e("error:" + what + "/" + extra);
        synchronized (SYNC_Playing) {
            SYNC_Playing.notify();
        }
        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
        mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        mUIHandler.removeMessages(UI_PLAYMAX);
        if (isCallback) {
            if (what != -38) {
                if (-1004 == extra) {
                    Message obtainMessage = mUIHandler.obtainMessage(UI_ISGONE, "url异常！");
                    mUIHandler.sendMessage(obtainMessage);
                } else {
                    Message obtainMessage = mUIHandler.obtainMessage(UI_ISGONE, "网络连接失败");
                    mUIHandler.sendMessage(obtainMessage);
                }
            }
        }
        isCallback = true;

        return true;
    }

    /**
     * 播放完成
     */
    @Override
    public void onCompletion() {
        // TODO Auto-generated method stub
        L.v(TAG, "onCompletion");
        synchronized (SYNC_Playing) {
            SYNC_Playing.notify();
        }
        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
        mUIHandler.removeMessages(UI_EVENT_UPDATE_CURRPOSITION);
        mUIHandler.removeMessages(UI_PLAYMAX);
        L.e("播放结束1");
        if (isCallback) {
            Message obtainMessage = mUIHandler.obtainMessage(UI_Completion, "");
            mUIHandler.sendMessage(obtainMessage);
            L.e("播放结束2");
        }
        isCallback = true;
    }

    /**
     * 准备播放就绪
     */
    @Override
    public void onPrepared() {
        // TODO Auto-generated method stub
        L.v(TAG, "onPrepared");
        mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
        mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
        mUIHandler.sendEmptyMessage(UI_PLAYMAX);

    }

    // 问题单选对话框
    public LCQuestionDialog showQuestion(final Question question, String bt,
                                         final LCPlayUtils lc, final Video video) {


        View questionDialogView = LayoutInflater.from(context).inflate(
                R.layout.lc_question_dialog, null);

        dialog2 = new LCQuestionDialog(context, R.style.MyQuestionDialog,
                questionDialogView);
        dialog2.show();
        // question.setQuestionstatus("1");
        final RelativeLayout lc_re_a = (RelativeLayout) questionDialogView
                .findViewById(R.id.lc_re_a);
        final RelativeLayout lc_re_b = (RelativeLayout) questionDialogView
                .findViewById(R.id.lc_re_b);
        final RelativeLayout lc_re_c = (RelativeLayout) questionDialogView
                .findViewById(R.id.lc_re_c);
        final RelativeLayout lc_re_d = (RelativeLayout) questionDialogView
                .findViewById(R.id.lc_re_d);
        ImageView lc_dialog_back = (ImageView) questionDialogView
                .findViewById(R.id.lc_dialog_back);
        ImageView lc_dialog_Aimage = (ImageView) questionDialogView
                .findViewById(R.id.lc_dialog_Aimage);
        ImageView lc_dialog_Bimage = (ImageView) questionDialogView
                .findViewById(R.id.lc_dialog_Bimage);
        ImageView lc_dialog_Cimage = (ImageView) questionDialogView
                .findViewById(R.id.lc_dialog_Cimage);
        ImageView lc_dialog_Dimage = (ImageView) questionDialogView
                .findViewById(R.id.lc_dialog_Dimage);
        TextView lc_dialog_questionText = (TextView) questionDialogView
                .findViewById(R.id.lc_dialog_questionText);
        TextView lc_dialog_Atext = (TextView) questionDialogView
                .findViewById(R.id.lc_dialog_Atext);
        TextView lc_dialog_Btext = (TextView) questionDialogView
                .findViewById(R.id.lc_dialog_Btext);
        TextView lc_dialog_Ctext = (TextView) questionDialogView
                .findViewById(R.id.lc_dialog_Ctext);
        TextView lc_dialog_Dtext = (TextView) questionDialogView
                .findViewById(R.id.lc_dialog_Dtext);
        Button dialog_button_affim = (Button) questionDialogView
                .findViewById(R.id.dialog_button_affim);
        Button dialog_button_cancel = (Button) questionDialogView
                .findViewById(R.id.dialog_button_cancel);
        dialog_button_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 提交
                if (TextUtils.isEmpty(Select)) {
                    showToast("选择不能为空");
                    return;
                }
                lc.Stop();

                if (LCConstant.userinfo != null) {
                    userQuestionAnswer(question.getQuestionid(),
                            themeType.getCourseid(), video.getVideoid(), Select
                                    + "", Integer.parseInt(question.getEnd()));
                } else {
                    mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
                    mPlaybtn.setImageResource(R.drawable.pause);
                    mBigPlayBtn.setImageResource(R.drawable.stopplay);
                    /**
                     * 继续播放
                     */
                    mVV.resume();
                    dialog2.cancel();
                    ISSHOWSECOND = false;
                }

            }
        });
        lc_dialog_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 返回
                lc.Stop();
                dialog2.cancel();
                mVV.seekTo(Integer.parseInt(question.getEnd()));
                mUIHandler.sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
                mPlaybtn.setImageResource(R.drawable.pause);
                mBigPlayBtn.setImageResource(R.drawable.stopplay);
                /**
                 * 继续播放
                 */
                mVV.resume();
            }
        });
        dialog_button_cancel.setText(bt);
        lc_dialog_questionText.setText(question.getQuestionname());

        try {
            lc_dialog_Atext.setText(question.getListOptions().get(0)
                    .getAnswername());
        } catch (Exception e) {
            lc_re_a.setVisibility(View.INVISIBLE);
            lc_re_a.setEnabled(false);
        }
        try {
            lc_dialog_Btext.setText(question.getListOptions().get(1)
                    .getAnswername());
        } catch (Exception e) {
            lc_re_b.setVisibility(View.INVISIBLE);
            lc_re_b.setEnabled(false);
        }
        try {
            lc_dialog_Ctext.setText(question.getListOptions().get(2)
                    .getAnswername());
        } catch (Exception e) {
            lc_re_c.setVisibility(View.INVISIBLE);
            lc_re_c.setEnabled(false);
        }
        try {
            lc_dialog_Dtext.setText(question.getListOptions().get(3)
                    .getAnswername());
        } catch (Exception e) {
            lc_re_d.setVisibility(View.INVISIBLE);
            lc_re_d.setEnabled(false);
        }
        lc_re_a.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lc_re_a.setBackgroundColor(getResources().getColor(
                        R.color.jxb_userinfo_selected));
                lc_re_b.setBackgroundColor(getResources().getColor(
                        R.color.white));
                lc_re_c.setBackgroundColor(getResources().getColor(
                        R.color.white));
                lc_re_d.setBackgroundColor(getResources().getColor(
                        R.color.white));

                Select = question.getListOptions().get(0).getAnswerid();
                lc_re_a.setEnabled(true);
                lc_re_b.setEnabled(true);
                lc_re_c.setEnabled(true);
                lc_re_d.setEnabled(true);
            }
        });
        lc_re_b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lc_re_b.setBackgroundColor(getResources().getColor(
                        R.color.jxb_userinfo_selected));
                lc_re_a.setBackgroundColor(getResources().getColor(
                        R.color.white));
                lc_re_c.setBackgroundColor(getResources().getColor(
                        R.color.white));
                lc_re_d.setBackgroundColor(getResources().getColor(
                        R.color.white));
                Select = question.getListOptions().get(1).getAnswerid();
                lc_re_a.setEnabled(true);
                lc_re_b.setEnabled(true);
                lc_re_c.setEnabled(true);
                lc_re_d.setEnabled(true);
            }
        });
        lc_re_c.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lc_re_c.setBackgroundColor(getResources().getColor(
                        R.color.jxb_userinfo_selected));
                lc_re_a.setBackgroundColor(getResources().getColor(
                        R.color.white));
                lc_re_b.setBackgroundColor(getResources().getColor(
                        R.color.white));
                lc_re_d.setBackgroundColor(getResources().getColor(
                        R.color.white));
                Select = question.getListOptions().get(2).getAnswerid();
                lc_re_a.setEnabled(true);
                lc_re_b.setEnabled(true);
                lc_re_c.setEnabled(true);
                lc_re_d.setEnabled(true);
            }
        });
        lc_re_d.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                lc_re_d.setBackgroundColor(getResources().getColor(
                        R.color.jxb_userinfo_selected));
                lc_re_a.setBackgroundColor(getResources().getColor(
                        R.color.white));
                lc_re_b.setBackgroundColor(getResources().getColor(
                        R.color.white));
                lc_re_c.setBackgroundColor(getResources().getColor(
                        R.color.white));
                Select = question.getListOptions().get(3).getAnswerid();
                lc_re_a.setEnabled(true);
                lc_re_b.setEnabled(true);
                lc_re_c.setEnabled(true);
                lc_re_d.setEnabled(true);
            }
        });
        try {
            LCUtils.mImageloader(question.getListOptions().get(0)
                    .getAnswerimg(), lc_dialog_Aimage, context);

        } catch (Exception e) {

        }
        try {
            LCUtils.mImageloader(question.getListOptions().get(1)
                    .getAnswerimg(), lc_dialog_Bimage, context);
        } catch (Exception e) {
        }
        try {
            LCUtils.mImageloader(question.getListOptions().get(2)
                    .getAnswerimg(), lc_dialog_Cimage, context);
        } catch (Exception e) {
        }
        try {
            LCUtils.mImageloader(question.getListOptions().get(3)
                    .getAnswerimg(), lc_dialog_Dimage, context);
        } catch (Exception e) {
        }

        return dialog2;
    }

    public void userQuestionAnswer(String questionid, String courseid,
                                   String videoid, String answerid, final int end) {

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("questionid", questionid);
        params.put("courseid", courseid);
        params.put("videoid", videoid);
        params.put("answerid", answerid);
        if (LCConstant.userinfo != null) {
            params.put("userid", LCConstant.userinfo.getUserid());
        } else {
            params.put("userid", 0 + "");
        }
        params.put("uuid", getOnly());
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.USER_QUESTION_ANSWER, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        try {
                            String loadConvert = loadConvert(content);
                            JSONObject jo = new JSONObject(loadConvert);
                            String errorCode = jo.optString("errorCode", "1");
                            String errorMessage = jo.optString("errorMessage",
                                    "1");
                            if ("0".equals(errorCode)) {
                                mUIHandler
                                        .sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
                                mPlaybtn.setImageResource(R.drawable.pause);
                                mBigPlayBtn
                                        .setImageResource(R.drawable.stopplay);
                                mVV.seekTo(end);
                                /**
                                 * 继续播放
                                 */
                                mVV.resume();
                                dialog2.cancel();
                                ISSHOWSECOND = false;
                            } else {
//                                showToast(errorMessage);
                                if ("20047".equals(errorCode)) {
                                    mUIHandler
                                            .sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
                                    mPlaybtn.setImageResource(R.drawable.pause);
                                    mBigPlayBtn
                                            .setImageResource(R.drawable.stopplay);
                                    /**
                                     * 继续播放
                                     */
                                    mVV.seekTo(end);
                                    mVV.resume();
                                    dialog2.cancel();
                                }
                                LCUtils.ReLogin(errorCode, context, errorMessage);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            // showToast("数据异常");
                            mUIHandler
                                    .sendEmptyMessage(UI_EVENT_UPDATE_CURRPOSITION);
                            mPlaybtn.setImageResource(R.drawable.pause);
                            mBigPlayBtn
                                    .setImageResource(R.drawable.stopplay);
                            /**
                             * 继续播放
                             */
                            mVV.seekTo(end);
                            mVV.resume();
                            dialog2.cancel();
                        }
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        AbDialogUtil.showProgressDialog(context, 0, "正在提交...");
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) videoviewholder
                    .getLayoutParams();
            linearParams.height = LayoutParams.MATCH_PARENT;
            linearParams.width = LayoutParams.MATCH_PARENT;
            videoviewholder.setLayoutParams(linearParams);
            lc_video_title.setVisibility(View.GONE);
            PORTRAIT = false;
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) videoviewholder
                    .getLayoutParams();
            linearParams.height = getHeight();
            linearParams.width = LayoutParams.MATCH_PARENT;
            videoviewholder.setLayoutParams(linearParams);
            lc_video_title.setVisibility(View.VISIBLE);
            PORTRAIT = true;
        }
    }

    private int getHeight() {
        // TODO Auto-generated method stub
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int h = (int) AbViewUtil.dip2px(context, 205);

        if (height == 2048 & width == 1536) {
            h = (int) AbViewUtil.dip2px(context, 405);
        }
//        if (height == 2560&) {
//            h = (int) AbViewUtil.dip2px(context, 405);
//            L.e("2HHHHHHHHHHHHHHHH:"+h);
//        }

        return h;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // do something...
            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.i("info", "landscape");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
            } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.i("info", "portrait");
                mLastPos = mVV.getCurrentPosition();
                updateVideoStatePlaying(PlayCount, mLastPos + "");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
