package cn.ledoing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.adapter.TrendsCommentAllAdapter;
import cn.ledoing.adapter.TrendsImageAdapter;
import cn.ledoing.bean.AllBean;

import cn.ledoing.bean.NewsComment;
import cn.ledoing.bean.Trends_Info;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;
import cn.ledoing.view.NoScrollGridView;
import cn.ledoing.view.NoScrollListView;

/**
 * Created by wupeitao on 15/11/6.
 */
public class TrendsInfo extends LCActivitySupport implements View.OnClickListener {
    private LCTitleBar trendsinfo_title;
    private TextView trendsinfo_content;
    private NoScrollGridView trendsinfo_image;
    private NoScrollListView trends_ListView;
    private TextView trendsinfo_time, comment_onCount, supportCount;
    private TrendsImageAdapter trendsImageAdapter;
    private TrendsCommentAllAdapter commentAdapter;
    private PopupWindow popWindow;
    private RelativeLayout all_re;
    private RelativeLayout bottom;
    private Handler handler = new Handler();
    private boolean open = false;
    private AbHttpUtil mAbHttpUtil;
    private String news_id;
    private ImageView support;
    private Button send_comment;
    private List<NewsComment> news_comment;
    private EditText comment_ed;
    private LinearLayout praise_li;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        open = intent.getBooleanExtra("open", false);
        news_id = intent.getStringExtra("news_id");
        L.e(news_id + "++++++++++++++++++++++");
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        setContentView(R.layout.layout_trendsinfo);
        initView();
    }

    private void initView() {
        all_re = (RelativeLayout) findViewById(R.id.all_re);
        bottom = (RelativeLayout) findViewById(R.id.bottom);
        trendsinfo_title = (LCTitleBar) findViewById(R.id.trendsinfo_title);
        trendsinfo_title.setCenterTitle("动态详情");
        trendsinfo_content = (TextView) findViewById(R.id.trendsinfo_content);
        trendsinfo_time = (TextView) findViewById(R.id.trendsinfo_time);
        comment_onCount = (TextView) findViewById(R.id.comment_onCount);
        supportCount = (TextView) findViewById(R.id.supportCount);
        praise_li = (LinearLayout) findViewById(R.id.praise_li);
        support = (ImageView) findViewById(R.id.ispraise);
        trendsinfo_image = (NoScrollGridView) findViewById(R.id.trendsinfo_image);
        trends_ListView = (NoScrollListView) findViewById(R.id.trends_ListView);
        send_comment = (Button) findViewById(R.id.send_comment);
        comment_ed=(EditText)findViewById(R.id.comment_ed);
        trends_ListView.setDivider(null);
        comment_onCount.setOnClickListener(this);
        send_comment.setOnClickListener(this);

        if (open) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(200);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                openInput();

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        netGetInfo();
    }


    public void openInput() {
        InputMethodManager mInputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }


    //导致TextView异常换行的原因：安卓默认数字、字母不能为第一行以后每行的开头字符，因为数字、字母为半角字符
    //所以我们只需要将半角字符转换为全角字符即可，方法如下
//    public String ToSBC(String input) {
//        char c[] = input.toCharArray();
//        for (int i = 0; i < c.length; i++) {
//            if (c[i] == ' ') {
//                c[i] = '\u3000';
//            } else if (c[i] < '\177') {
//                c[i] = (char) (c[i] + 65248);
//            }
//        }
//        return new String(c);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.comment_onCount:
                openInput();
                break;
            case R.id.send_comment:
                if (!TextUtils.isEmpty(comment_ed.getText().toString())) {
                    netComment(comment_ed.getText().toString());
                    comment_ed.setText("");
                } else {
                    AbToastUtil.showToast(context, "请输入内容！");
                }
                break;
        }
    }


    private void netGetInfo() {
        // TODO Auto-generated method stub

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("newsid", news_id);
        mAbHttpUtil.post(LCConstant.TEACHER_SHOW, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        final Trends_Info tInfo = JSON.parseObject(content, Trends_Info.class);
                        if ("0".equals(tInfo.getErrorCode())) {
                            LCUtils.setTitle(trendsinfo_content, tInfo.getData().getNews_content());
                            LCUtils.setTitle(comment_onCount, tInfo.getData().getComment_num());
                            LCUtils.setTitle(supportCount, tInfo.getData().getPraise_num());
                            LCUtils.setTitle(trendsinfo_time, AbDateUtil.getStringByFormat(tInfo.getData().getPublish_time(), AbDateUtil.dateFormatYMDHM_HAN));
                            LCUtils.setTitle(supportCount, tInfo.getData().getPraise_num());
                            if (tInfo.getData().getIspraise() == 1) {
                                support.setBackgroundResource(R.drawable.price_select);
                                praise_li.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AbToastUtil.showToast(context, "你已经点过赞了！");
                                    }
                                });

                            } else {
                                support.setBackgroundResource(R.drawable.price_normal);
                                praise_li.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        netPrise(tInfo.getData());
                                    }
                                });
                            }
                            trendsImageAdapter = new TrendsImageAdapter(context, tInfo.getData().getNews_img());
                            trendsinfo_image.setAdapter(trendsImageAdapter);
                            news_comment = tInfo.getData().getNews_comment();
                            commentAdapter = new TrendsCommentAllAdapter(context, news_comment);
                            trends_ListView.setAdapter(commentAdapter);
                            trendsinfo_image.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Intent intent = new Intent(TrendsInfo.this, ImagePagerActivity.class);
                                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
                                    ArrayList<String> news_img = (ArrayList<String>) tInfo.getData().getNews_img();
                                    intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, news_img);
                                    startActivity(intent);
                                }
                            });

                        } else {
                            LCUtils.ReLogin(tInfo.getErrorCode(), context, tInfo.getErrorMessage());
                        }
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
//                        LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        AbToastUtil.showToast(context, "获取失败");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框
//                        LCUtils.stopProgressDialog(context);
                    }
                });


    }

    //点赞
    private void netPrise(Trends_Info.Data data) {
        // TODO Auto-generated method stub

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("newsid", news_id);
        mAbHttpUtil.post(LCConstant.TEACHER_PRICE, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        AllBean allBean = JSON.parseObject(content, AllBean.class);
                        if ("0".equals(allBean.getErrorCode())) {
                            support.setBackgroundResource(R.drawable.price_select);
                            supportCount.setText(Integer.parseInt(supportCount.getText().toString())+1+"");
//                            news_comment.add()
                        } else {
                            LCUtils.ReLogin(allBean.getErrorCode(), context, allBean.getErrorMessage());
                        }
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        AbToastUtil.showToast(context, "点赞失败");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框
                        LCUtils.stopProgressDialog(context);
                    }
                });


    }

    //提交评论
    private void netComment(final String contenttext) {
        // TODO Auto-generated method stub

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("newsid", news_id);
        params.put("content", contenttext);
        mAbHttpUtil.post(LCConstant.TEACHER_COMMENT, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        AllBean allBean = JSON.parseObject(content, AllBean.class);
                        if ("0".equals(allBean.getErrorCode())) {
                            NewsComment newsComment = new NewsComment(Integer.parseInt(LCConstant.userinfo.getUserid()), contenttext, System.currentTimeMillis(), LCConstant.userinfo.getUserpic(), LCConstant.userinfo.getUsername());
                            news_comment.add(newsComment);
                            commentAdapter.notifyDataSetChanged();
                            closeInput();
                        } else {
                            LCUtils.ReLogin(allBean.getErrorCode(), context, allBean.getErrorMessage());
                        }
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        AbToastUtil.showToast(context, "评论失败");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框
                        LCUtils.stopProgressDialog(context);
                    }
                });


    }

}
