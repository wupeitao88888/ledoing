package cn.ledoing.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.view.pullview.AbPullToRefreshView;
import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.lc.widget.OnWheelChangedListener;
import com.lc.widget.WheelView;
import com.lc.widget.adapters.ArrayWheelAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ledoing.activity.HomeCenterDetalActivity;
import cn.ledoing.activity.LCHomeFragmentHost;
import cn.ledoing.activity.R;
import cn.ledoing.adapter.CenterAdapter;
import cn.ledoing.adapter.CenterAllAdapter;
import cn.ledoing.adapter.SequenceAdapter;
import cn.ledoing.bean.AttentionCenter;
import cn.ledoing.bean.CenterCity;
import cn.ledoing.bean.CityBean;
import cn.ledoing.bean.ConcernAll;
import cn.ledoing.bean.DistrictBean;
import cn.ledoing.bean.DistrictModel;
import cn.ledoing.bean.FCenterAll;
import cn.ledoing.bean.FCenterAllList;
import cn.ledoing.bean.ProvinceBean;
import cn.ledoing.bean.Sequence;
import cn.ledoing.global.LCApplication;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.model.AddHomeMenuListener;
import cn.ledoing.model.ApplicationLocationListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.AddHomeMenuWindow;

/**
 * Created by wupeitao on 15/11/19.
 */
public class CenterAll extends Fragment implements Animator.AnimatorListener, View.OnClickListener, OnWheelChangedListener {
    private View rootView;// 缓存Fragment view
    private Activity mActivity;
    private ListView home_attentionall_listview;
    private CenterAdapter centerAttentionAdapter;

    private LinearLayout mLayout;
    private LinearLayout layout_center;
    private AbHttpUtil mAbHttpUtil = null;
    private CenterCity centerCity;

    private TextView home_address;//定位的街道
    private TextView home_refresh;//刷新定位
    private RelativeLayout city_re;//城市
    private RelativeLayout scope_re;//区
    private RelativeLayout order_re;//循序

    private TextView cityt;//城市(选择的)
    private TextView scopet;//区(选择的)
    private TextView ordert;//排序(选择的)

    private RelativeLayout city_choose;
    private RelativeLayout scope_choose;
    private RelativeLayout sort_choose;
    private WheelView id_province;
    private WheelView id_city;
    private WheelView districts;
    private ListView sort;
    private RelativeLayout endView;
    private LinearLayout layout_li;
    private int index;
    private boolean mIsScrollToUp = false;  //是否向上滚动
    public int i = 0;
    private ImageView order_text_right;
    private ImageView scope_text_right;
    private ImageView city_text_right;

    private String[] mProvinceDatas;
    private CenterCity cityString;
    List<ProvinceBean> provinceList = null;
    protected String mCurrentProviceName = "北京";
    protected String mCurrentCityName = "北京";
    protected String mCurrentDistrictName = "朝阳区";

    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    private String provincialid;
    private String cityid;
    private String districtid;
    public String sortid;
    private int provicePosition = 0;
    private int cityPosition = 0;
    private int centerStatus = 0;
    private SequenceAdapter sequenceAdapter;
    private int page = 1;


    private LinearLayout secondary;

    private int visibleLastIndex = 0;   //最后的可视项索引
    public int visibleListItemCount;       // 当前窗口可见项总数

    private List<FCenterAll.All_list> all_list;

    private CenterAllAdapter centerAllAdapter;
    private View footview;
    public boolean isLoad = false;//判断是否正在加载
    private String lastX;
    private String lastY;
    private String lastSort;
    private String lastpid;
    private String lastdid;
    private String lastcid;
    private AbPullToRefreshView mAbPullToRefreshView;
    private FCenterAllList fCenterAllList;
    private boolean isReface = true;
    private TextView nodate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = this.getActivity();
        if (rootView == null) {
            rootView = initView(inflater);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    private View initView(LayoutInflater inflater) {
        View v = inflater.inflate(R.layout.layout_centerall, null);
        mAbHttpUtil = AbHttpUtil.getInstance(getActivity());
        mAbHttpUtil.setTimeout(5000);

        home_address = (TextView) v.findViewById(R.id.home_address);
        home_refresh = (TextView) v.findViewById(R.id.home_refresh);
        cityt = (TextView) v.findViewById(R.id.city);
        scopet = (TextView) v.findViewById(R.id.scope);
        ordert = (TextView) v.findViewById(R.id.order);
        city_re = (RelativeLayout) v.findViewById(R.id.city_re);
        scope_re = (RelativeLayout) v.findViewById(R.id.scope_re);
        order_re = (RelativeLayout) v.findViewById(R.id.order_re);
        secondary = (LinearLayout) v.findViewById(R.id.secondary);

        layout_li = (LinearLayout) v.findViewById(R.id.layout_li);
        endView = (RelativeLayout) v.findViewById(R.id.endview);

        city_choose = (RelativeLayout) v.findViewById(R.id.city_choose);
        scope_choose = (RelativeLayout) v.findViewById(R.id.scope_choose);
        sort_choose = (RelativeLayout) v.findViewById(R.id.sort_choose);

        id_province = (WheelView) v.findViewById(R.id.id_province);
        id_city = (WheelView) v.findViewById(R.id.id_city);
        districts = (WheelView) v.findViewById(R.id.districts);

        sort = (ListView) v.findViewById(R.id.sort);

        order_text_right = (ImageView) v.findViewById(R.id.order_text_right);
        scope_text_right = (ImageView) v.findViewById(R.id.scope_text_right);
        city_text_right = (ImageView) v.findViewById(R.id.city_text_right);
        nodate = (TextView) v.findViewById(R.id.nodate);
        mAbPullToRefreshView = (AbPullToRefreshView) v
                .findViewById(R.id.home_attentionall_rV);

        mAbPullToRefreshView
                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                        // refreshTask();
                        isReface = true;
                        getConcern(lastX, lastY, lastSort, lastpid, lastcid, lastdid);
                    }
                });

        // 设置进度条的样式
        mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));

        mAbPullToRefreshView
                .setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

                    @Override
                    public void onFooterLoad(AbPullToRefreshView view) {
                        // loadMoreTask();
                        isReface = false;
                        getConcern(lastX, lastY, lastSort, lastpid, lastcid, lastdid);
                    }
                });


        getCityDataList();//获取城市数据
        layout_center = (LinearLayout) v.findViewById(R.id.layout_center);
        mLayout = (LinearLayout) v.findViewById(R.id.mLayout);
        home_attentionall_listview = (ListView) v.findViewById(R.id.home_attentionall_listview);
//        footview = LayoutInflater.from(mActivity).inflate(R.layout.pull_footer, null);
//        home_attentionall_listview.addFooterView(footview);
        endView.setOnClickListener(this);
        id_province.addChangingListener(this);
        id_city.addChangingListener(this);
        districts.addChangingListener(this);

        List<Sequence> sequenceList = new ArrayList<>();
        Sequence sequence = new Sequence("离我最近", "0");
        sequence.setId("2");
        sequenceList.add(sequence);
        Sequence sequence2 = new Sequence("好评优先", "0");
        sequence2.setId("1");
        sequenceList.add(sequence2);

        sequenceAdapter = new SequenceAdapter(mActivity, sequenceList);
        sort.setAdapter(sequenceAdapter);


        city_re.setOnClickListener(this);
        scope_re.setOnClickListener(this);
        order_re.setOnClickListener(this);
        home_refresh.setOnClickListener(this);

        if (LCApplication.mInstance.lbs) {
            home_address.setText(LCApplication.mInstance.street);
            cityt.setText(LCApplication.mInstance.city);
            scopet.setText(LCApplication.mInstance.district);
            LCUtils.setTitle(ordert, "离我最近");
            isReface = true;
            getConcern(LCApplication.mInstance.longitude + "", LCApplication.mInstance.latitude + "", "", "", "", "");
        } else {
            home_address.setText("定位失败！");
            getConcern("", "", sortid, "356", "1", "512");
        }
        initAttentionCenter(v);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_refresh:
                LCApplication.mInstance.startLBS();
                break;
            case R.id.city_re:
                layout_li.setVisibility(View.VISIBLE);
                setCity();
                break;
            case R.id.scope_re:
                layout_li.setVisibility(View.VISIBLE);
                setScope();
                break;
            case R.id.order_re:
                layout_li.setVisibility(View.VISIBLE);
                setOrder();
                break;
            case R.id.endview:
                layout_li.setVisibility(View.GONE);
                break;
        }
    }

    //显示和选择城市
    public void setCity() {
        if (city_choose.getVisibility() == View.VISIBLE) {
            city_choose.setVisibility(View.GONE);
            getProviceId();
            getCityId();
            city_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
            layout_li.setVisibility(View.GONE);
            cityt.setText(mCurrentProviceName + "/" + mCurrentCityName);
            getConcern("", "", sortid, provincialid, cityid, districtid);
        } else {
            city_text_right.setBackground(getResources().getDrawable(R.drawable.text_right_selcet));
            city_choose.setVisibility(View.VISIBLE);

        }
        if (scope_choose.getVisibility() == View.VISIBLE) {
            scope_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
            districts.setCurrentItem(0);
            scope_choose.setVisibility(View.GONE);
            AbToastUtil.showToast(mActivity, "未选中区县");
        }

        if (sort_choose.getVisibility() == View.VISIBLE) {
            sort_choose.setVisibility(View.GONE);
            AbToastUtil.showToast(mActivity, "未选择排序规则");
            order_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
            sequenceAdapter.resetAll();
        }
    }

    //显示和选择区
    public void setScope() {
        if (scope_choose.getVisibility() == View.VISIBLE) {
            scope_choose.setVisibility(View.GONE);
            scope_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
            layout_li.setVisibility(View.GONE);
            getDistrictId();
            scopet.setText(mCurrentDistrictName);
            getConcern("", "", sortid, provincialid, cityid, districtid);
        } else {
            scope_choose.setVisibility(View.VISIBLE);
            scope_text_right.setBackground(getResources().getDrawable(R.drawable.text_right_selcet));
        }
        if (city_choose.getVisibility() == View.VISIBLE) {
            city_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
            city_choose.setVisibility(View.GONE);
            id_province.setCurrentItem(0);
            id_city.setCurrentItem(0);
            AbToastUtil.showToast(mActivity, "未选中城市");
        }
        if (sort_choose.getVisibility() == View.VISIBLE) {
            sort_choose.setVisibility(View.GONE);
            AbToastUtil.showToast(mActivity, "未选择排序规则");
            order_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
            sequenceAdapter.resetAll();
        }
    }

    //显示和选择优先级
    public void setOrder() {
        if (sort_choose.getVisibility() == View.VISIBLE) {
            sort_choose.setVisibility(View.GONE);
            layout_li.setVisibility(View.GONE);
            try {
                LCUtils.setTitle(ordert, sequenceAdapter.getSequence().getContent());
                sortid = sequenceAdapter.getSequence().getId();
                getConcern("", "", sortid, provincialid, cityid, districtid);
            } catch (Exception e) {

            }
            order_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
        } else {
            sort_choose.setVisibility(View.VISIBLE);
            order_text_right.setBackground(getResources().getDrawable(R.drawable.text_right_selcet));
        }
        if (city_choose.getVisibility() == View.VISIBLE) {
            AbToastUtil.showToast(mActivity, "未选中城市");
            city_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
            city_choose.setVisibility(View.GONE);
            id_province.setCurrentItem(0);
            id_city.setCurrentItem(0);
        }
        if (scope_choose.getVisibility() == View.VISIBLE) {
            scope_text_right.setBackground(getResources().getDrawable(R.drawable.text_right));
            districts.setCurrentItem(0);
            scope_choose.setVisibility(View.GONE);
            AbToastUtil.showToast(mActivity, "未选中区县");
        }
    }


    private void initAttentionCenter(View v) {

        LCApplication.mInstance.setOnReceiveLocation(new ApplicationLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation var1) {
                cityt.setText(var1.getCity());
                scopet.setText(var1.getDistrict());
                LCUtils.setTitle(ordert, "离我最近");
                isReface = true;
                getConcern(var1.getLongitude() + "", var1.getLatitude() + "", null, null, null, null);
            }

            @Override
            public void fild() {
                home_address.setText("定位失败！");
                isReface = true;
                getConcern("", "", sortid, "356", "1", "512");
            }
        });
        home_attentionall_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mActivity, HomeCenterDetalActivity.class);
                intent.putExtra("center_id",all_list.get(position).getIns_id());
                startActivity(intent);
            }
        });
        all_list = new ArrayList<>();
        centerAllAdapter = new CenterAllAdapter(mActivity, all_list);
        home_attentionall_listview.setAdapter(centerAllAdapter);
        home_attentionall_listview.setOnScrollListener(new AbsListView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        index = view.getLastVisiblePosition();
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        int scrolled = view.getLastVisiblePosition();
                        if (scrolled > index) {
                            if (!mIsScrollToUp) {
                                //收起
                                mIsScrollToUp = true;
                                float[] f = new float[2];
                                f[0] = 0.0F;
                                f[1] = -mLayout.getHeight();
                                L.e(mLayout.getHeight() + "///////");
                                ObjectAnimator animator1 = ObjectAnimator.ofFloat(secondary, "translationY", f);
                                animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                                animator1.setDuration(200);
                                animator1.start();
                                animator1.addListener(CenterAll.this);

//                                RelativeLayout.LayoutParams para = (RelativeLayout.LayoutParams) home_attentionall_listview.getLayoutParams();
//                                para.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                                para.height = layout_center.getHeight();
//
//                                home_attentionall_listview.setLayoutParams(para);

                            }

                        } else {
//                            弹出
                            // 判断滚动到顶部
                            if (home_attentionall_listview.getFirstVisiblePosition() == 0) {
                                if (mIsScrollToUp) {
                                    mLayout.setVisibility(View.VISIBLE);
                                    mIsScrollToUp = false;
                                    float[] f = new float[2];
                                    f[0] = -mLayout.getHeight();
                                    f[1] = 0F;
                                    ObjectAnimator animator1 = ObjectAnimator.ofFloat(secondary, "translationY", f);
                                    animator1.setDuration(200);
                                    animator1.setInterpolator(new AccelerateDecelerateInterpolator());
                                    animator1.start();
                                    animator1.addListener(CenterAll.this);

                                }
                            }
                        }
                        break;
                }
                int itemsLastIndex = all_list.size() - 1;    //数据集最后一项的索引
                int lastIndex = itemsLastIndex + 1;             //加上底部的loadMoreView项
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
                    //如果是自动加载,可以在这里放置异步加载数据的代码
                    L.i("LOADMORE", "loading...");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                CenterAll.this.visibleListItemCount = visibleItemCount;
                visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });


    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if (mIsScrollToUp)
            mLayout.setVisibility(View.GONE);

        L.e("++++++++++++++++++++++++++++>>>>>>>>>");
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    //得到城市列表
    public void getCityDataList() {
        i = 1;

        AbRequestParams params = new AbRequestParams();
//        params.put("page", "");
//        params.put("pagesize", 10 + "");
        mAbHttpUtil.postUrl(LCConstant.GET_CITY_LIST, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        cityString = JSONUtils.getInstatce().getCenterCityOnline(content);
                        L.e("=======4.8====statusCode==" + cityString.getList().size() + "================");
                        setContent(cityString);
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.8====statusCode==" + statusCode + "================"+content+"1111111111111"+error.toString());
                        AbToastUtil.showToast(mActivity, "城市数据正在初始化失败！");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // LCUtils.stopProgressDialog();
                        i = 0;
                    }
                });
    }


    public void getProviceId() {
        for (int i = 0; i < cityString.getList().size(); i++) {
            if (mCurrentProviceName.equals(cityString.getList().get(i).getName())) {
                provincialid = cityString.getList().get(i).getId();
                provicePosition = i;
            }
        }
    }

    public void getCityId() {
        for (int i = 0; i < cityString.getList().get(provicePosition).getCityList().size(); i++) {
            if (mCurrentCityName.equals(cityString.getList().get(provicePosition).getCityList().get(i).getName())) {
                cityid = cityString.getList().get(provicePosition).getCityList().get(i).getId();
                cityPosition = i;
            }
        }
    }

    public void getDistrictId() {
        for (int i = 0; i < cityString.getList().get(provicePosition).getCityList().get(cityPosition).getDistrictList().size(); i++) {
            if (mCurrentDistrictName.equals(cityString.getList().get(provicePosition).getCityList().get(cityPosition).getDistrictList().get(i).getName())) {
                districtid = cityString.getList().get(provicePosition).getCityList().get(cityPosition).getDistrictList().get(i).getId();
            }
        }
    }

    //滑动数据切换
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == id_province) {
            updateCities();
        } else if (wheel == id_city) {
            updateAreas();
        } else if (wheel == districts) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
        }
    }

    protected void setContent(CenterCity cityS) {
        // TODO Auto-generated method stub
        L.e("=======4.7====statusCode==" + cityS.getList().size() + "================");
        initProvinceDatas(cityS);
        id_province.setViewAdapter(new ArrayWheelAdapter<String>(mActivity,
                mProvinceDatas));
        // 设置可见条目数量
        id_province.setVisibleItems(7);
        id_city.setVisibleItems(7);
        districts.setVisibleItems(7);
        updateCities();
        updateAreas();
    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = id_city.getCurrentItem();
        try {
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];

            String[] areas = mDistrictDatasMap.get(mCurrentCityName);

            if (areas == null) {
                areas = new String[]{""};
            }
            for (int i = 0; i < areas.length; i++) {
                String area = areas[i];
                L.e(area);
            }

            districts.setViewAdapter(new ArrayWheelAdapter<String>(mActivity,
                    areas));
            districts.setCurrentItem(0);
            int currentItem = districts.getCurrentItem();
        } catch (Exception e) {

        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = id_province.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        id_city.setViewAdapter(new ArrayWheelAdapter<String>(mActivity, cities));
        id_city.setCurrentItem(0);
        updateAreas();
    }

    //得到省市区数据
    protected void initProvinceDatas(CenterCity city2) {

        try {

            // 获取解析出来的数据
            provinceList = new ArrayList<ProvinceBean>();
            provinceList.addAll(city2.getList());

            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();

                List<CityBean> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictBean> districtList = cityList.get(0)
                            .getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                }
            }

            // */
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityBean> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictBean> districtList = cityList.get(j)
                            .getDistrictList();
                    String[] distrinctNameArray = new String[districtList
                            .size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList
                            .size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DistrictBean districtModel = districtList.get(k);
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);

                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {

        } finally {

        }
    }


    /**
     * 获得关注机构列表
     */
    private void getConcern(String x, String y, String sort, String pid, String cid, String did) {
        if (isReface) {
            page = 1;
        } else {
            if (fCenterAllList != null) {
                if (fCenterAllList.getData().getAll_total_count() <= all_list.size()) {
                    AbToastUtil.showToast(mActivity, "暂无数据！");
                    mAbPullToRefreshView.onFooterLoadFinish();
                    return;
                } else
                    page++;
            }
        }


        this.lastX = x;
        this.lastY = y;
        this.lastpid = pid;
        this.lastcid = cid;
        this.lastdid = did;
        this.lastSort = sort;
        AbRequestParams params = new AbRequestParams();
        params.put("page", page + "");
        params.put("pagesize", "15");
        params.put("x", x + "");
        params.put("y", y + "");
        params.put("sort", sort + "");
        params.put("pid", pid + "");
        params.put("cid", cid + "");
        params.put("did", did + "");

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.CENTER_LIST, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                        isLoad = true;
                        LCUtils.startProgressDialog(mActivity);
                    }

                    @Override
                    public void onFinish() {
                        mAbPullToRefreshView.onFooterLoadFinish();
                        mAbPullToRefreshView.onHeaderRefreshFinish();
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        //                        showToast("网络连接失败");
//                        isNoNet();
                        isLoad = false;
                        AbToastUtil.showToast(mActivity, "网络连接失败");

                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        fCenterAllList = JSON.parseObject(content, FCenterAllList.class);
                        LCUtils.stopProgressDialog(mActivity);
                        if(null != fCenterAllList && "0".equals(fCenterAllList.getErrorCode())){
                            if (isReface) {
                                if(fCenterAllList.getData().getAll_total_count()==0) {
                                    nodate.setVisibility(View.VISIBLE);
                                }else {
                                    nodate.setVisibility(View.GONE);
                                    all_list.clear();
                                    all_list.addAll(fCenterAllList.getData().getAll_list());
                                }
                            } else {
                                all_list.addAll(fCenterAllList.getData().getAll_list());
                            }
                            centerAllAdapter.notifyDataSetChanged();
                        }else{
                            AbToastUtil.showToast(mActivity, "数据获取失败");
                        }
                    }
                });
    }
    /**
     * 刷新关注机构列表
     */
    public void getConcern() {
        if(null != all_list && all_list.size()>0){
            return;
        }
        page = 1;
        AbRequestParams params = new AbRequestParams();
        params.put("page", page + "");
        params.put("pagesize", "15");
        params.put("x", lastX + "");
        params.put("y", lastY + "");
        params.put("sort", lastSort + "");
        params.put("pid", lastpid + "");
        params.put("cid", lastcid + "");
        params.put("did", lastdid + "");

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.CENTER_LIST, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                        isLoad = true;
                        LCUtils.startProgressDialog(mActivity);
                    }

                    @Override
                    public void onFinish() {
                        mAbPullToRefreshView.onFooterLoadFinish();
                        mAbPullToRefreshView.onHeaderRefreshFinish();
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        //                        showToast("网络连接失败");
//                        isNoNet();
                        isLoad = false;
                        AbToastUtil.showToast(mActivity, "网络连接失败");

                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        fCenterAllList = JSON.parseObject(content, FCenterAllList.class);
                        LCUtils.stopProgressDialog(mActivity);
                        if(null != fCenterAllList && "0".equals(fCenterAllList.getErrorCode())){
                            if(fCenterAllList.getData().getAll_total_count()==0) {
                                nodate.setVisibility(View.VISIBLE);
                            }else {
                                nodate.setVisibility(View.GONE);
                                all_list.clear();
                                all_list.addAll(fCenterAllList.getData().getAll_list());
                            }
                            centerAllAdapter.notifyDataSetChanged();
                        }else{
                            AbToastUtil.showToast(mActivity, "数据获取失败");
                        }
                    }
                });
    }

}
