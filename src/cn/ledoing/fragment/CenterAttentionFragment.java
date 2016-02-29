package cn.ledoing.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.activity.HomeCenterDetalActivity;
import cn.ledoing.activity.R;
import cn.ledoing.adapter.CenterAdapter;
import cn.ledoing.bean.AttentionCenter;
import cn.ledoing.bean.ConcernAll;
import cn.ledoing.global.LCApplication;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.NoScrollListView;

/**
 * 我关注的中心
 * Created by wupeitao on 15/11/19.
 */
public class CenterAttentionFragment extends Fragment {
    private View rootView;// 缓存Fragment view
    private Activity mActivity;
    private AbHttpUtil mAbHttpUtil = null;
    private ListView home_attention_listview;
    private CenterAdapter centerAttentionAdapter;
    private int page = 1;
    private List<AttentionCenter.Data.DataList> list;
    private AttentionCenter attentionCenter;
    private AbPullToRefreshView home_attention;
    public boolean isRefresh = true;
    private TextView nodate;
    private double latitude;
    private double longitude;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    public void setRefase() {
        if (LCApplication.mInstance.lbs) {
            //定位成功
            L.e("定位成功");
            latitude = LCApplication.mInstance.latitude;
            longitude = LCApplication.mInstance.longitude;
            getConcern(longitude + "", latitude + "");
        } else {
            //定位失败
            L.e("定位失败");
            AbToastUtil.showToast(mActivity, "");
            getConcern(null, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = this.getActivity();
        if (rootView == null) {
            rootView = initView(inflater);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }


        return initView(inflater);
    }

    private View initView(LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.layout_centerattention, null);
        initAttentionCenter(v);


        return v;
    }

    private void initAttentionCenter(View v) {
        if (!LCConstant.FRIST_NO_LOAD) {
            LCConstant.FRIST_NO_LOAD = true;
            return;
        }
        mAbHttpUtil = AbHttpUtil.getInstance(mActivity);
        mAbHttpUtil.setTimeout(5000);
        home_attention_listview = (ListView) v.findViewById(R.id.home_attention_listview);
        home_attention = (AbPullToRefreshView) v.findViewById(R.id.home_attention);
        nodate = (TextView) v.findViewById(R.id.nodate);

        nodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRefresh = true;
                setRefase();
            }
        });
        list = new ArrayList<>();
        centerAttentionAdapter = new CenterAdapter(mActivity, list);
        home_attention_listview.setAdapter(centerAttentionAdapter);
        home_attention_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, HomeCenterDetalActivity.class);
                intent.putExtra("center_id", list.get(position).getIns_id());
                startActivity(intent);
            }
        });
        home_attention
                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                        // refreshTask();
                        isRefresh = true;
                        setRefase();
                    }
                });

        // 设置进度条的样式
        home_attention.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        home_attention.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));

        home_attention
                .setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

                    @Override
                    public void onFooterLoad(AbPullToRefreshView view) {
                        isRefresh = false;
                        setRefase();
                    }
                });
        if (LCConstant.islogin) {
            isRefresh = true;
            setRefase();
        }
    }

    /**
     * 获得关注机构列表
     */
    private void getConcern(String x, String y) {
        if (isRefresh) {
            page = 1;
        } else {
            if (attentionCenter != null) {

                if (list.size() >= attentionCenter.getData().getTotal_count()) {
                    AbToastUtil.showToast(mActivity, "没有更多数据");
                    //不加载
                    home_attention.onFooterLoadFinish();
                    home_attention.onHeaderRefreshFinish();
                    LCUtils.stopProgressDialog(mActivity);
                    return;
                } else {
                    page++;
                }
            }
        }
        AbRequestParams params = new AbRequestParams();
        params.put("page", page + "");
        params.put("pagesize", "15");
        params.put("x", x + "");
        params.put("y", y + "");
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.GUANZHU_HOME, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(mActivity);
                    }

                    @Override
                    public void onFinish() {
                        home_attention.onFooterLoadFinish();
                        home_attention.onHeaderRefreshFinish();
                        LCUtils.stopProgressDialog(mActivity);
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        //                        showToast("网络连接失败");
//                        isNoNet();
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        setConcernAllOver(content);

                    }
                });
    }

    /**
     *  刷新关注机构列表
     */
    public void getConcern() {
        if(null != list && list.size() > 0){
            return;
        }
        page = 1;
        AbRequestParams params = new AbRequestParams();
        params.put("page", page + "");
        params.put("pagesize", "15");
        params.put("x", longitude + "");
        params.put("y", latitude + "");
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.GUANZHU_HOME, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(mActivity);
                    }

                    @Override
                    public void onFinish() {
                        home_attention.onFooterLoadFinish();
                        home_attention.onHeaderRefreshFinish();
                        LCUtils.stopProgressDialog(mActivity);
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        //                        showToast("网络连接失败");
//                        isNoNet();
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        setConcernAllOver(content);
                    }
                });
    }

    private void setConcernAllOver(String content) {
        attentionCenter = JSON.parseObject(content, AttentionCenter.class);
        if ("0".equals(attentionCenter.getErrorCode())) {
            if (page > 1) {
                list.addAll(attentionCenter.getData().getList());
            } else {
                list.clear();
                if (attentionCenter.getData().getTotal_count() == 0) {
                    nodate.setVisibility(View.VISIBLE);
                }else{
                    nodate.setVisibility(View.GONE);
                    list.addAll(attentionCenter.getData().getList());
                }
            }
            centerAttentionAdapter.notifyDataSetChanged();
        } else {
            AbToastUtil.showToast(mActivity, "数据获取失败！");
        }
    }
}
