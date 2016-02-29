package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.adapter.TrendsAdapter;
import cn.ledoing.bean.Trends;

import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;

/**
 * 动态
 * Created by wupeitao on 15/11/4.
 */
public class TrendsActivity extends LCActivitySupport {
    private ListView trends_ListView;
    private AbPullToRefreshView mAbPullToRefreshView = null;
    private TrendsAdapter trendsAdapter;
    private LCTitleBar trends_title;
    private AbHttpUtil mAbHttpUtil;
    private String teacher_id;
    private int page = 1;
    public boolean isRefresh = true;
    private Trends trends;
    private  List<Trends.Data.DataList> list = new ArrayList<>();
    private ImageView no_deta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trends);
        initView();
    }

    private void initView() {
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        trends_title = (LCTitleBar) findViewById(R.id.trends_title);
        trends_title.setCenterTitle("动态列表");
        trends_ListView = (ListView) findViewById(R.id.trends_ListView);
        no_deta = (ImageView) findViewById(R.id.no_deta);
        trends_ListView.setDivider(null);
        mAbPullToRefreshView = (AbPullToRefreshView) findViewById(R.id.trends_AbPullToRefreshView);
        // 设置进度条的样式
        mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));

        mAbPullToRefreshView
                .setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

                    @Override
                    public void onFooterLoad(AbPullToRefreshView view) {
                        isRefresh=false;
                        netComment();
                    }
                });

        mAbPullToRefreshView
                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                        isRefresh = true;
                        netComment();
                    }
                });
        Intent intent = getIntent();
        teacher_id = intent.getStringExtra("teacher_id");
        trendsAdapter = new TrendsAdapter(context, list);
        trends_ListView.setAdapter(trendsAdapter);
        trends_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, TrendsInfo.class);
                intent.putExtra("open",false);
                intent.putExtra("news_id",list.get(position).getNews_id());
                startActivity(intent);
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
            if (trends.getData().getTotal_count() >= list.size()) {
                showToast("暂无更多数据！");
                mAbPullToRefreshView.onFooterLoadFinish();
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
        mAbHttpUtil.post(LCConstant.TEACHER_NEWS, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        trends= JSON.parseObject(content, Trends.class);
                        if("0".equals(trends.getErrorCode())){

                            if(isRefresh){
                                if(trends.getData().getList().size()==0){
                                    no_deta.setVisibility(View.VISIBLE);
                                }
                                list.clear();
                            }
                            list.addAll(trends.getData().getList());
                            trendsAdapter.notifyDataSetChanged();
                        }else{
                            LCUtils.ReLogin(trends.getErrorCode(),context,trends.getErrorMessage());
                            no_deta.setVisibility(View.VISIBLE);
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
                        mAbPullToRefreshView.onHeaderRefreshFinish();
                        mAbPullToRefreshView.onFooterLoadFinish();
                    }
                });


    }
}
