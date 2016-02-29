package cn.ledoing.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ledoing.adapter.LCOfflineCoursesAdapter;
import cn.ledoing.bean.CancelReason;
import cn.ledoing.bean.CancelReasonList;
import cn.ledoing.bean.CityModel;
import cn.ledoing.bean.OfflineCourses;
import cn.ledoing.bean.OfflineCoursesAll;
import cn.ledoing.bean.DistrictModel;
import cn.ledoing.bean.ProvinceModel;
import cn.ledoing.bean.TimAll;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;

import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lc.widget.OnWheelChangedListener;
import com.lc.widget.WheelView;
import com.lc.widget.adapters.ArrayWheelAdapter;

//wupeitao
public class LCOfflineCourses extends LCActivitySupport implements
        View.OnClickListener, OnWheelChangedListener {
    private Calendar mCalendar;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private boolean mTitleNeedsUpdate = true;
    //    public static String date = null;
    private LCTitleBar lc_coures_title;
    private LinearLayout lc_coures_time;
    private TextView lc_courses_year;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName = "";
    protected String mCurrentZipCode = "";
    private List<ProvinceModel> provinceList = null;
    private TimAll timeall;
    private AbPullToRefreshView lc_courses_pullto;
    private ListView lc_courses_ListView;
    private AbHttpUtil mAbHttpUtil = null;
    private LCOfflineCoursesAdapter offlineCoursesAdapter;
    private int page = 1;
    private int pagesize = 20;
    private List<OfflineCourses> list;
    private OfflineCoursesAll coursesRecordAll;
    private RelativeLayout lc_coures_refresh;
    private LCNoNetWork lc_coures_nonet;
    private RelativeLayout lc_coures_content;
    private LinearLayout lc_coures_filter;
    private View popView;
    private PopupWindow popupWindow;
    private WheelView mViewDistrict;
    private LinearLayout layoutCity;
    private TextView sure;
    private int isSet=2;
    private String filter[] = {"全部", "待上课", "已上课", "已过期"};
    private LinearLayout close;
    private TextView lc_courses_filter;
    private List<CancelReasonList> listReason = new ArrayList<CancelReasonList>();
    private ImageView lc_left_back;
    private String stastFilter = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_offlinecourse);
        initView();
        initPopupWindow();
    }

    @Override
    public void onBackPressed() {
        backPress();
        super.onBackPressed();
    }

    public void backPress(){
        if(1 == getIntent().getIntExtra("isGoHome",0) ){//若是从预约入口进到此界面，返回后调整到首页
            finish();
            Intent intent = new Intent(context, LCHomeFragmentHost.class);
            startActivity(intent);
        }else{
            finish();
        }
    }

    private void initView() {
        // TODO Auto-generated method stub
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        lc_left_back = (ImageView)findViewById(R.id.lc_left_back);
        lc_left_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPress();
            }
        });
        mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        monthOfYear = mCalendar.get(Calendar.MONTH);
        dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        list = new ArrayList<OfflineCourses>();
        lc_coures_refresh = (RelativeLayout) findViewById(R.id.lc_coures_refresh_rl);
        lc_coures_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netInit(year + "-" + monthOfYear, 1, stastFilter);
            }
        });
        lc_coures_nonet = (LCNoNetWork) findViewById(R.id.lc_coures_nonet);
        lc_coures_nonet.setRetryOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netInit(year + "-" + monthOfYear, 1, stastFilter);
                isNoNet();
            }
        });
        lc_coures_content = (RelativeLayout) findViewById(R.id.lc_coures_content);
        lc_coures_title = (LCTitleBar) findViewById(R.id.lc_coures_title);
        lc_coures_title.setCenterTitle("预约课程");
        lc_coures_time = (LinearLayout) findViewById(R.id.lc_coures_time);
        lc_courses_year = (TextView) findViewById(R.id.lc_courses_year);
        lc_coures_filter = (LinearLayout) findViewById(R.id.lc_coures_filter);
        lc_courses_filter = (TextView) findViewById(R.id.lc_courses_filter);
        lc_courses_filter.setText(filter[0] + " ");
        lc_coures_time.setOnClickListener(this);
        lc_coures_filter.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        monthOfYear = mCalendar.get(Calendar.MONTH);
        dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        Intent intent = getIntent();
        timeall = (TimAll) intent.getSerializableExtra("timeall");
        timeall.getYear().add(new ProvinceModel(year+1+"", year+1+"", "", "", "", "", "", "", "", "", "", "", null));
        lc_courses_year.setText(year + "年" +(monthOfYear + 1) + "月 ");
        lc_courses_ListView = (ListView) findViewById(R.id.lc_courses_ListView);
        lc_courses_pullto = (AbPullToRefreshView) findViewById(R.id.lc_courses_pullto);
        // 设置进度条的样式
        lc_courses_pullto.getHeaderView().setHeaderProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        lc_courses_pullto.getFooterView().setFooterProgressBarDrawable(
                this.getResources().getDrawable(R.drawable.progress_circular));
        lc_courses_pullto
                .setOnHeaderRefreshListener(new AbPullToRefreshView.OnHeaderRefreshListener() {

                    @Override
                    public void onHeaderRefresh(AbPullToRefreshView view) {
                        netInit(year + "-" + monthOfYear, 1, stastFilter);
                    }
                });
        lc_courses_pullto
                .setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

                    @Override
                    public void onFooterLoad(AbPullToRefreshView view) {
                        LoadInit(year + "-" + monthOfYear);
                    }
                });

        netInit(year + "-" + (monthOfYear + 1), 1, stastFilter);
        isNoNet();
    }


    /**
     * 初始化popupWindow
     */
    private void initPopupWindow() {
        popView = getLayoutInflater().inflate(R.layout.popupwindow, null);
        popView.setAlpha(1);
        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        initLocalData();
    }

    //以下方法是城市选择器需要的方法
    private void initLocalData() {
        layoutCity = (LinearLayout) popView.findViewById(R.id.select_lc_city);
        mViewProvince = (WheelView) popView.findViewById(R.id.id_province);
        mViewCity = (WheelView) popView.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popView.findViewById(R.id.id_district);
        sure = (TextView) popView.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSet == 2) {//选择日期
                    year = Integer.parseInt(mCurrentProviceName);
                    monthOfYear = Integer.parseInt(mCurrentCityName);
                    lc_courses_year.setText(mCurrentProviceName + "年" + mCurrentCityName + "月 ");
                    netInit(year + "-" + monthOfYear, 1, stastFilter);
                } else if (isSet == 1) {//选择
                    int pCurrent = mViewProvince.getCurrentItem();
                    mCurrentProviceName = filter[pCurrent];
                    // 添加stast方法
                    stastFilter = getStastFilter(mCurrentProviceName);
                    lc_courses_filter.setText(mCurrentProviceName+" ");
                    netInit(year + "-" + monthOfYear, 1, stastFilter);
                }
                popupWindow.dismiss();
            }
        });
        close = (LinearLayout) popView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        if (isSet == 2) {//选择生日
            monthOfYear = monthOfYear+1;
            setInitDate(year,monthOfYear,0);
        }
    }

    private String getStastFilter(String filters){
        String filter = "0";
        switch (filters){
            case "全部":
                filter = "0";
            break;
            case "待上课":
                filter = "1";
            break;
            case "已上课":
                filter = "2";
            break;
            case "已过期":
                filter = "5";
            break;
            case "已完成":
                filter = "6";
            break;
        }
        return filter;
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
        lc_coures_nonet.setVisibility(View.GONE);
        lc_coures_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_coures_nonet.setVisibility(View.VISIBLE);
        lc_coures_content.setVisibility(View.GONE);
    }

    private void netInit(String starttime ,final int page, String stastFilter) {
        // TODO Auto-generated method stub
        // 获取Http工具类
        this.page = page;
        AbRequestParams params = new AbRequestParams();
        params.put("time_dur", starttime);
        params.put("status", stastFilter);
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        mAbHttpUtil.postUrl(LCConstant.RE_GET_USER_ORDER_LIST, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {

                        L.e("=======4.1====statusCode==" + statusCode + "================" + content);
                        coursesRecordAll = JSONUtils.getInstatce().getOfflineCoursesAll(content);
                        if (page > 1) {
                            setLoadDate(coursesRecordAll);
                        } else {
                            setDate(coursesRecordAll);
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
                        L.e("=======4.1====statusCode==" + statusCode + "================" + content);
                        isNoNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        if( page > 1 ){
                            lc_courses_pullto.onFooterLoadFinish();
                        } else {
                            LCUtils.stopProgressDialog(context);
                            lc_courses_pullto.onHeaderRefreshFinish();
                        }
                        if( offlineCoursesAdapter != null && listReason.size() == 0 ) {
                            getCancelReason();
                        }
                    }
                });
    }


    private void getCancelReason() {
        AbRequestParams params = new AbRequestParams();
        mAbHttpUtil.postUrl(LCConstant.RE_GET_CANCEL_REASON, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        CancelReason cancelReason = JSONUtils.getInstatce().getCancelReasonOver(content, LCOfflineCourses.this);
                        listReason = cancelReason.getCancelReasonList();
                        if( listReason.size() > 0 ){
                            offlineCoursesAdapter.setListReason(listReason);
                        }
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.1====statusCode==" + statusCode + "================" + content);
                        isNoNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        lc_courses_pullto.onHeaderRefreshFinish();
                    }
                });
    }

    public void setDate(OfflineCoursesAll date) {
        if ("0".equals(date.getErrorCode())) {

            list = date.getList();
            if (list.size() == 0) {
                lc_coures_refresh.setVisibility(View.VISIBLE);
            } else {
                lc_coures_refresh.setVisibility(View.GONE);
                offlineCoursesAdapter = new LCOfflineCoursesAdapter(LCOfflineCourses.this, list);
                lc_courses_ListView.setAdapter(offlineCoursesAdapter);
            }
        } else {
//            showToast(date.getErrorMessage());
            LCUtils.ReLogin(date.getErrorCode(), context, date.getErrorMessage());
        }
    }


    private void LoadInit(String starttime) {
        // TODO Auto-generated method stub
        // 获取Http工具类

        if (list.size() < Integer.parseInt(coursesRecordAll.getTotal_count())) {
            page++;
        } else {
            showToast("没有更多数据！");
            lc_courses_pullto.onFooterLoadFinish();
            return;
        }
        netInit(starttime, page, stastFilter);
    }

    public void setLoadDate(OfflineCoursesAll date) {
        if ("0".equals(date.getErrorCode())) {
            list.addAll(date.getList());
            offlineCoursesAdapter.notifyDataSetChanged();
        } else {
            showToast(date.getErrorMessage());
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.lc_coures_time:
                isSet = 2;
                setContent(timeall);
                //为popWindow添加动画效果
                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                // 点击弹出泡泡窗口
                popupWindow.showAtLocation((RelativeLayout) findViewById(R.id.parent_rl), Gravity.BOTTOM, 0, 0);
                setInitDate(year,monthOfYear,0);
                break;
            case R.id.lc_coures_filter:
                isSet = 1;
                setContentStast();
                //为popWindow添加动画效果
                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                // 点击弹出泡泡窗口
                popupWindow.showAtLocation((RelativeLayout) findViewById(R.id.parent_rl), Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    protected void setContentStast() {
        // TODO Auto-generated method stub

        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
                filter));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewProvince.setCurrentItem(0);
        mViewCity.setVisibility(View.GONE);
        mViewDistrict.setVisibility(View.GONE);
    }



    public void setNoDate() {
        int y = year - 2000;
        setInitDate(y, monthOfYear, dayOfMonth - 1);
        mCurrentProviceName = year + "";
        mCurrentCityName = monthOfYear + 1 + "";
        mCurrentDistrictName = (dayOfMonth) + "";
    }

    protected void setContent(TimAll city) {
        // TODO Auto-generated method stub

        initProvinceDatas(city);
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
                mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibility(View.GONE);
        mViewCity.setVisibility(View.VISIBLE);
        updateCities();
        updateAreas();
    }

    public void setInitDate(int y, int m, int d) {
        mViewProvince.setCurrentItem(y-2000);
        mViewCity.setCurrentItem(m - 1);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = null;
//        if(mCurrentProviceName.equals(String.valueOf(mCalendar.get(Calendar.YEAR)))){
//            cities = new String[mCalendar.get(Calendar.MONTH)+1];
//            for(int i = 0; i < mCalendar.get(Calendar.MONTH)+1; i++){
//                cities[i] = (i+1)+"";
//            }
//
//        } else {
            cities = new String[12];
            for(int i = 0; i < 12; i++){
                cities[i] = (i+1)+"";
            }
//        }
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        try {
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
            String[] areas = null;
            // 是否闰年，如果是闰年是21天
            if (!isLeapYear(Integer.parseInt(mCurrentProviceName))
                    & mCurrentCityName.equals("2")) {
                areas = getLeapDay();
            } else {
                areas = mDistrictDatasMap.get(mCurrentCityName);
            }
            if (areas == null) {
                areas = new String[]{""};
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince && isSet != 1) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        }
    }

    protected void initProvinceDatas(TimAll city2) {

        try {

            // 获取解析出来的数据
            provinceList = new ArrayList<ProvinceModel>();
            provinceList.addAll(city2.getYear());
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();

                List<CityModel> cityList = city2.getMonth();
                if (cityList != null && !cityList.isEmpty()) {

                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = city2.getDay();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getXingzhengdaima();
                }
            }

            // */
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = city2.getMonth();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();

                    List<DistrictModel> districtList = getDate(
                            Integer.parseInt(provinceList.get(i).getName()),
                            Integer.parseInt(cityList.get(j).getName()));
                    String[] distrinctNameArray = new String[districtList
                            .size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList
                            .size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DistrictModel districtModel = districtList.get(k);
                        mZipcodeDatasMap.put(districtList.get(k).getName(),
                                districtList.get(k).getXingzhengdaima());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

    public List<DistrictModel> getDate(int year, int month) {
        List<DistrictModel> day = new ArrayList<DistrictModel>();

        int lastDay = getMonth(year, month);
        for (int q = 1; q < lastDay; q++) {
            String districtId = q + "";
            String districtName = q + "";
            String districtPinyin = "";
            String districtDivision_type = "";
            String districtParent_id = "";
            String districtXingzhengdaima = "";
            String districtShow_order = "";
            String districtStatus = "";
            String districtUpdate_time = "";
            String districtAlias = "";
            String districtCreate_time = "";
            String districtInitials = "";
            DistrictModel dm = new DistrictModel(districtName, districtId,
                    districtPinyin, districtDivision_type, districtParent_id,
                    districtXingzhengdaima, districtShow_order, districtStatus,
                    districtUpdate_time, districtAlias, districtCreate_time,
                    districtInitials);
            day.add(dm);
        }
        return day;
    }

    private int getMonth(int i, int p) {
        // TODO Auto-generated method stub

        int m = 31;
        if (p == 1 | p == 3 | p == 5 | p == 7 | p == 8 | p == 10 | p == 3
                | p == 12) {

            m = 32;
        }
        if (p == 2) {

            return m = 30;

        }

        return m;
    }

    public boolean isLeapYear(int i) {
        if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static String[] getLeapDay() {
        String[] distrinctNameArray = new String[28];
        for (int i = 0; i < 28; i++) {
            distrinctNameArray[i] = (i + 1) + "";
            System.out.println((i + 1) + "");
        }
        return distrinctNameArray;
    }

}
