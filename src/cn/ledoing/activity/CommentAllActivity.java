package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.ledoing.adapter.CommentALlAdapter;
import cn.ledoing.adapter.CommentAdapter;
import cn.ledoing.bean.ActivityBean;
import cn.ledoing.bean.Comment;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;

/**
 * 教师所有评论
 * Created by wupeitao on 15/11/26.
 */
public class CommentAllActivity extends LCActivitySupport {
    private LCTitleBar trends_title;
    private AbPullToRefreshView comment_AbPullToRefreshView;
    private ListView comment_ListView;
    private AbHttpUtil mAbHttpUtil;
    private CommentALlAdapter commentAdapter;
    private int page = 1;
    public boolean isRefresh = true;
    private Comment comment;
    private List<Comment.Data.ListData> list;
    private String teacher_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teacher_commentall);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        teacher_id=intent.getStringExtra("teacher_id");
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        trends_title = (LCTitleBar) findViewById(R.id.comment_title);
        trends_title.setCenterTitle("评论");
        comment_ListView = (ListView) findViewById(R.id.comment_ListView);
        comment_ListView.setDivider(null);
        comment_AbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.comment_AbPullToRefreshView);
        comment_AbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        comment_AbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));

        comment_AbPullToRefreshView
                .setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

                    @Override
                    public void onFooterLoad(AbPullToRefreshView view) {
                        isRefresh = false;
                        netComment();
                    }
                });
        comment_AbPullToRefreshView
                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                        isRefresh = true;
                        netComment();
                    }
                });
        netComment();

    }

    //教师评论
    private void netComment() {
        // TODO Auto-generated method stub
        if (isRefresh) {
            page = 1;
        } else {
            if (comment.getData().getTotal_count() >= list.size()) {
                showToast("暂无更多数据！");
                comment_AbPullToRefreshView.onFooterLoadFinish();
                return;
            } else {
                page++;
            }
        }
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("teacherid", teacher_id + "");
        params.put("page", page + "");
        params.put("pagesize", 15 + "");
        mAbHttpUtil.post(LCConstant.TEACHER_HOME_COMMENTS, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        comment = JSON.parseObject(content, Comment.class);
                        if ("0".equals(comment.getErrorCode())) {
                            if (isRefresh) {
                                list = comment.getData().getList();
                                commentAdapter = new CommentALlAdapter(context, list);
                                comment_ListView.setAdapter(commentAdapter);
                            } else {
                                list.addAll(comment.getData().getList());
                                commentAdapter.notifyDataSetChanged();
                            }
                        } else {
                            LCUtils.ReLogin(comment.getErrorCode(), context, comment.getErrorMessage());
                        }
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框

                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        showToast("网络请求失败");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框
                        comment_AbPullToRefreshView.onFooterLoadFinish();;

                        comment_AbPullToRefreshView.onHeaderRefreshFinish();
                    }
                });
    }
}
