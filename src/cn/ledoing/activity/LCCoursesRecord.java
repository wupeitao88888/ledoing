package cn.ledoing.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ledoing.adapter.LCCoursesRecordAdapter;
import cn.ledoing.bean.CityModel;
import cn.ledoing.bean.CoursesRecourd;
import cn.ledoing.bean.CoursesRecourdAll;
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
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lc.widget.OnWheelChangedListener;
import com.lc.widget.WheelView;
import com.lc.widget.adapters.ArrayWheelAdapter;

//wupeitao
public class LCCoursesRecord extends LCActivitySupport implements
        View.OnClickListener, OnWheelChangedListener {
    private Calendar mCalendar;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private boolean mTitleNeedsUpdate = true;
    //    public static String date = null;
    private LCTitleBar lc_coures_title;
    private LinearLayout lc_coures_time, lc_coures_selector;
    private TextView lc_courses_year, lc_courses_month, lc_coures_classtimed,
            lc_coures_residuetimed, lc_coures_submit;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
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
    private LCCoursesRecordAdapter coursesRecordAdapter;
    private int page = 1;
    private int pagesize = 20;
    private List<CoursesRecourd> list;
    private CoursesRecourdAll coursesRecordAll;
    private View lc_coures_refresh;
    private LCNoNetWork lc_coures_nonet;
    private RelativeLayout lc_coures_content;
    private TextView lc_coures_cancel;
    private RelativeLayout toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_coursesrecord);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        monthOfYear = mCalendar.get(Calendar.MONTH);
        dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        list = new ArrayList<CoursesRecourd>();
        lc_coures_refresh = (View) findViewById(R.id.lc_coures_refresh);
        lc_coures_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netInit(mCurrentProviceName + "-" + mCurrentCityName);
            }
        });
        lc_coures_nonet = (LCNoNetWork) findViewById(R.id.lc_coures_nonet);
        lc_coures_nonet.setRetryOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netInit(mCurrentProviceName + "-" + mCurrentCityName);
                isNoNet();
            }
        });
        toast= (RelativeLayout) findViewById(R.id.toast);
        lc_coures_content = (RelativeLayout) findViewById(R.id.lc_coures_content);
        lc_coures_title = (LCTitleBar) findViewById(R.id.lc_coures_title);
        lc_coures_title.setCenterTitle("销课记录");
        lc_coures_time = (LinearLayout) findViewById(R.id.lc_coures_time);
        lc_coures_selector = (LinearLayout) findViewById(R.id.lc_coures_selector);
        lc_courses_year = (TextView) findViewById(R.id.lc_courses_year);
        lc_courses_month = (TextView) findViewById(R.id.lc_courses_month);
        lc_coures_classtimed = (TextView) findViewById(R.id.lc_coures_classtimed);
        lc_coures_residuetimed = (TextView) findViewById(R.id.lc_coures_residuetimed);
        lc_coures_submit = (TextView) findViewById(R.id.lc_coures_submit);
        lc_coures_cancel = (TextView) findViewById(R.id.lc_coures_cancel);
        lc_coures_time.setOnClickListener(this);
        lc_coures_cancel.setOnClickListener(this);
        lc_coures_submit.setOnClickListener(this);
        lc_coures_selector.setVisibility(View.GONE);
        mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        monthOfYear = mCalendar.get(Calendar.MONTH);
        dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        Intent intent = getIntent();
        timeall = (TimAll) intent.getSerializableExtra("timeall");
        timeall.getYear().add(new ProvinceModel(year+1+"", year+1+"", "", "", "", "", "", "", "", "", "", "", null));
        setContent(timeall);
        lc_courses_year.setText(year + "");
        lc_courses_month.setText((monthOfYear + 1) + "月");

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
                        netInit(mCurrentProviceName + "-" + mCurrentCityName);
                    }
                });
        lc_courses_pullto
                .setOnFooterLoadListener(new AbPullToRefreshView.OnFooterLoadListener() {

                    @Override
                    public void onFooterLoad(AbPullToRefreshView view) {
                        LoadInit(mCurrentProviceName + "-" + mCurrentCityName);
                    }
                });

        netInit(mCurrentProviceName + "-" + (Integer) (monthOfYear + 1));
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
        lc_coures_nonet.setVisibility(View.GONE);
        lc_coures_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_coures_nonet.setVisibility(View.VISIBLE);
        lc_coures_content.setVisibility(View.GONE);
    }

    private void netInit(String starttime) {
        // TODO Auto-generated method stub
        // 获取Http工具类
        page = 1;
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        String p = "platform=android&time=" + gettime().substring(0, 10)
                + "&uuid=" + getOnly() + LCConstant.token;
        String url = "/version/userhour";
        AbRequestParams params = new AbRequestParams();
        params.put("userid", LCConstant.userinfo.getUserid());
        params.put("starttime", starttime);
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        params.put("platform", "android");
        params.put("time", gettime().substring(0, 10));
        params.put("uuid", getOnly());
        params.put("sign", MD5Util.md5(p));
        mAbHttpUtil.post(LCConstant.URL+url, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {

                        coursesRecordAll = JSONUtils.getInstatce().getCoursesRecordAll(content);
                        setDate(coursesRecordAll);
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
                        isNoNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                        lc_courses_pullto.onHeaderRefreshFinish();
                    }
                });
    }

    public void setDate(CoursesRecourdAll date) {
        if ("0".equals(date.getErrorCode())) {

            list = date.getList();
            if (list.size() == 0) {
                lc_coures_refresh.setVisibility(View.VISIBLE);
                lc_coures_classtimed.setText(date.getTotal_use());
                lc_coures_residuetimed.setText(date.getTotal_hour());
                toast.setVisibility(View.GONE);

            } else {
                lc_coures_refresh.setVisibility(View.GONE);
                lc_coures_classtimed.setText(date.getTotal_use());
                lc_coures_residuetimed.setText(date.getTotal_hour());
                coursesRecordAdapter = new LCCoursesRecordAdapter(LCCoursesRecord.this, list,toast);
                lc_courses_ListView.setAdapter(coursesRecordAdapter);
            }
        } else {
//            showToast(date.getErrorMessage());
            LCUtils.ReLogin(date.getErrorCode(),context,date.getErrorMessage());
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

        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        String p = "platform=android&time=" + gettime().substring(0, 10)
                + "&uuid=" + getOnly() + LCConstant.token;
        String url = "/version/userhour";
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("userid", LCConstant.userinfo.getUserid());
        params.put("starttime", starttime);
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        params.put("platform", "android");
        params.put("time", gettime().substring(0, 10));
        params.put("uuid", getOnly());
        params.put("sign", MD5Util.md5(p));
        mAbHttpUtil.post(LCConstant.URL+url, params,

                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // 移除进度框
                        coursesRecordAll = JSONUtils.getInstatce().getCoursesRecordAll(content);
                        setLoadDate(coursesRecordAll);

                    }

                    // 开始执行前
                    @Override
                    public void onStart() {

                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        isNoNet();
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        lc_courses_pullto.onFooterLoadFinish();
                    }
                });
    }

    public void setLoadDate(CoursesRecourdAll date) {
        if ("0".equals(date.getErrorCode())) {
            list.addAll(date.getList());
            coursesRecordAdapter.notifyDataSetChanged();
        } else {
            showToast(date.getErrorMessage());
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.lc_coures_submit:
                lc_coures_selector.setVisibility(View.GONE);
                L.e("年" + mCurrentProviceName + "月" + mCurrentCityName);
                lc_courses_year.setText("  " + mCurrentProviceName + "  ");
                lc_courses_month.setText(mCurrentCityName + "月");
                netInit(mCurrentProviceName + "-" + mCurrentCityName);
                break;
            case R.id.lc_coures_time:
                lc_coures_selector.setVisibility(View.VISIBLE);
                break;
            case R.id.lc_coures_cancel:
                lc_coures_selector.setVisibility(View.GONE);
                break;
        }
    }

    protected void DateInit() {
        // TODO Auto-generated method stub
        int monthUpdate = Integer.parseInt(mCurrentCityName);
        String monthUpdateString;
        if (monthUpdate < 10) {
            monthUpdateString = "0" + monthUpdate;
        } else {
            monthUpdateString = monthUpdate + "";
        }
        int dayUpdate = Integer.parseInt(mCurrentDistrictName);
        String dayUpdateString;
        if (dayUpdate < 10) {
            dayUpdateString = "0" + dayUpdate;
        } else {
            dayUpdateString = dayUpdate + "";
        }
        L.e(mCurrentProviceName + "/" + monthUpdateString + "/"
                + dayUpdateString);
    }

    public void getBrithDay(String bd) {
        if (TextUtils.isEmpty(bd)) {
            setNoDate();
            return;
        }
        try {
            int yearU = Integer.parseInt(bd.substring(0, bd.indexOf("-")));
            int monthU = Integer.parseInt(bd.substring(bd.indexOf("-") + 1,
                    bd.lastIndexOf("-")));
            int dayU = Integer.parseInt(bd.substring(bd.lastIndexOf("-") + 1,
                    bd.length()));
            int y = yearU - 2000;
            L.e(monthU + "/" + dayU + "/" + mCitisDatasMap.size());
            setInitDate(y, monthU - 1, dayU - 1);

        } catch (Exception e) {
            setNoDate();
        }

    }

    public void setNoDate() {
        int y = year - 2000;
        setInitDate(y, monthOfYear, dayOfMonth - 1);
        mCurrentProviceName = year + "";
        mCurrentCityName = monthOfYear + 1 + "";
        mCurrentDistrictName = (dayOfMonth) + "";
        // lc_day.setText(mCurrentProviceName + "-" + mCurrentCityName + "-"
        // + mCurrentDistrictName + "-"+);
    }

    protected void setContent(TimAll city) {
        // TODO Auto-generated method stub

        initProvinceDatas(city);
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
                mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        updateCities();
        updateAreas();
        getBrithDay(null);
    }

    public void setInitDate(int y, int m, int d) {
        mViewProvince.setCurrentItem(y);
        mViewCity.setCurrentItem(m);
        mViewDistrict.setCurrentItem(d);

    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = null;
//        if(mCurrentProviceName.equals(String.valueOf(year))){
//            cities = new String[monthOfYear+1];
//            for(int i = 0; i < monthOfYear+1; i++){
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
            mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this,
                    areas));
            mViewDistrict.setCurrentItem(0);
            int currentItem = mViewDistrict.getCurrentItem();
            String string = areas[currentItem];

        } catch (Exception e) {

        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        // TODO Auto-generated method stub
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
        try {
            // lc_day.setText(mCurrentProviceName + "-" + mCurrentCityName + "-"
            // + mCurrentDistrictName + "");
        } catch (Exception e) {

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
