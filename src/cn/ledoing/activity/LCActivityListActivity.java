package cn.ledoing.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ab.view.pullview.AbPullToRefreshView;
import com.lc.widget.OnWheelChangedListener;
import com.lc.widget.WheelView;
import com.lc.widget.adapters.ArrayWheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ledoing.adapter.LCActivityListAdapter;
import cn.ledoing.bean.ActivityBean;
import cn.ledoing.bean.CenterCity;
import cn.ledoing.bean.CityBean;
import cn.ledoing.bean.DistrictBean;
import cn.ledoing.bean.DistrictModel;
import cn.ledoing.bean.ProvinceBean;
import cn.ledoing.global.LCApplication;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;

/**
 * Created by cheers on 2015/9/8.
 */
public class LCActivityListActivity extends LCActivitySupport implements OnWheelChangedListener {

    private ListView xListView;
    private LCActivityListAdapter adapter;
    private ArrayList<ActivityBean> arrayList = new ArrayList<ActivityBean>();
    private TextView locationCity;
    private AbHttpUtil mAbHttpUtil = null;
    private TextView changeCity;
    private AbPullToRefreshView mAbPullToRefreshView;

    /**
     * 以下变量是城市选择器需要的变量
     */
    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName = "";
    protected String mCurrentZipCode = "";
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    List<ProvinceBean> provinceList = null;
    private PopupWindow popupWindow;
    private View popView;
    private LCTitleBar lc_title;
    private String provincialid;
    private String cityid;
    private String districtid;
    private CenterCity centerCity;
    private int provicePosition = 0;
    private int cityPosition = 0;
    private int centerStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acitivity_list);
        initView();
        getActivityList();
        getCityDataList();
    }


    //初始化titlebar
    private void initTitle() {
        lc_title = (LCTitleBar) findViewById(R.id.lc_title);
        if (!TextUtils.isEmpty(categoryname) && categoryname.length() > 3)
            lc_title.setCenterTitle(categoryname.substring(4));
        else {
            lc_title.setCenterTitle("");
        }
        lc_title.setCenterTitleColor(getResources().getColor(R.color.white));
        lc_title.setBackGb(getResources().getColor(R.color.titlebar_activity));
        lc_title.setLeftImage(R.drawable.goback);
    }

    //初始化
    private void initView() {
        initTitle();
        sharedPreferencesHelper = new LCSharedPreferencesHelper(context,
                "isfrist");
        xListView = (ListView) findViewById(R.id.xListView);
//        xListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.list_activity_list_header, null, false));
        initHeader();
        locationCity = (TextView) findViewById(R.id.address);
        if (TextUtils.isEmpty(sharedPreferencesHelper.getValue(LCSharedPreferencesHelper.PROVINCIALNAME))) {
            locationCity.setText(lcSharedPreferencesLogin.getValue("provincial") + "：省   " + lcSharedPreferencesLogin.getValue("city") + "：市   " + lcSharedPreferencesLogin.getValue("district"));
        } else {
            locationCity.setText(sharedPreferencesHelper.getValue(LCSharedPreferencesHelper.PROVINCIALNAME) + "：省   " + sharedPreferencesHelper.getValue(LCSharedPreferencesHelper.CITYNAME) + "：市   " + sharedPreferencesHelper.getValue(LCSharedPreferencesHelper.DISTRICTNAME));
        }

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (arrayList.get(position).getHas_class() == 1) {
                    Intent intent = new Intent(context, LCActivityDetailsListActivity.class);
                    intent.putExtra("institution_id", arrayList.get(position).getInstitution_id());
                    intent.putExtra("center_name", arrayList.get(position).getInstitution_name());
                    startActivity(intent);
                } else {
                    showToast("当前中心没有课程！");
                }
            }
        });
        initPopupWindow();
    }

    /**
     * 初始化popupWindow
     */
    private void initPopupWindow() {
        popView = getLayoutInflater().inflate(R.layout.popupwindow_and_location, null);
        popView.setAlpha(1);
        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        initLocalData();
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }


    private void initHeader() {
        changeCity = (TextView) findViewById(R.id.change_city);
        changeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopWindow();
            }
        });
    }

    //popwindow   show
    public void showPopWindow() {
        //为popWindow添加动画效果
        popupWindow.setAnimationStyle(R.style.popWindow_animation);
        // 点击弹出泡泡窗口
        popupWindow.showAtLocation((LinearLayout) findViewById(R.id.parent), Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.5f);
    }


    //以下方法是城市选择器需要的方法
    private void initLocalData() {
        mViewProvince = (WheelView) popView.findViewById(R.id.id_province);
        mViewCity = (WheelView) popView.findViewById(R.id.id_city);
        mViewDistrict = (WheelView) popView.findViewById(R.id.id_district);
        TextView sure = (TextView) popView.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                getProviceId();
                getCityId();
                getDistrictId();
                getActivityList();//如果选择了城市
                locationCity.setText(mCurrentProviceName + "：省   " + mCurrentCityName
                        + "：市   " + mCurrentDistrictName);
                popupWindow.dismiss();

            }
        });
        LinearLayout close = (LinearLayout) popView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        mViewProvince.addChangingListener(this);
        mViewCity.addChangingListener(this);
        mViewDistrict.addChangingListener(this);
    }

    public void getProviceId() {
        for (int i = 0; i < centerCity.getList().size(); i++) {
            if (mCurrentProviceName.equals(centerCity.getList().get(i).getName())) {
                provincialid = centerCity.getList().get(i).getId();
                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.PROVINCIALID,
                        provincialid + "");
                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.PROVINCIALNAME,
                        mCurrentProviceName + "");
                provicePosition = i;
            }
        }
    }

    public void getCityId() {
        for (int i = 0; i < centerCity.getList().get(provicePosition).getCityList().size(); i++) {
            if (mCurrentCityName.equals(centerCity.getList().get(provicePosition).getCityList().get(i).getName())) {
                cityid = centerCity.getList().get(provicePosition).getCityList().get(i).getId();
                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.CITYID,
                        cityid + "");
                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.CITYNAME,
                        mCurrentCityName + "");
                cityPosition = i;
            }
        }
    }

    public void getDistrictId() {
        for (int i = 0; i < centerCity.getList().get(provicePosition).getCityList().get(cityPosition).getDistrictList().size(); i++) {
            if (mCurrentDistrictName.equals(centerCity.getList().get(provicePosition).getCityList().get(cityPosition).getDistrictList().get(i).getName())) {
                districtid = centerCity.getList().get(provicePosition).getCityList().get(cityPosition).getDistrictList().get(i).getId();
                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.DISTRICTID,
                        districtid + "");
                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.DISTRICTNAME,
                        mCurrentDistrictName + "");
            }
        }
    }



    //滑动数据切换
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince) {
            updateCities();
        } else if (wheel == mViewCity) {
            updateAreas();
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            L.e(mCurrentDistrictName);
        }
    }

    protected void setContent(CenterCity city) {
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
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mViewCity.getCurrentItem();
        try {
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
            String[] areas = mDistrictDatasMap.get(mCurrentCityName);

            if (areas == null) {
                areas = new String[]{""};
            }
            mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this,
                    areas));
            mViewDistrict.setCurrentItem(0);
            int currentItem = mViewDistrict.getCurrentItem();
        } catch (Exception e) {

        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mViewCity.setCurrentItem(0);
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
                        // 遍历市下面所有区/县的数据
                        // DistrictModel districtModel = new DistrictModel(name,
                        // id, pinyin, division_type, parent_id,
                        // xingzhengdaima, show_order, status,
                        // update_time, alias, create_time, initials)
                        //
                        // ;
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
            e.printStackTrace();
        } finally {

        }
    }


    //得到活动列表
    public void getActivityList() {
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        AbRequestParams params = new AbRequestParams();
//        params.put("page", "");
//        params.put("pagesize", 10 + "");
        params.put("provincial", sharedPreferencesHelper.getValue(LCSharedPreferencesHelper.PROVINCIALID));
        params.put("city", sharedPreferencesHelper.getValue(LCSharedPreferencesHelper.CITYID));
        params.put("district", sharedPreferencesHelper.getValue(LCSharedPreferencesHelper.DISTRICTID));
        params.put("category_id", categoryid);
        mAbHttpUtil.postUrl(LCConstant.INSTITUTION_LIST, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        try {
                            L.e("=======4.7====statusCode==" + statusCode + "================" + loadConvert(content));
                            JSONObject jsonObject = new JSONObject(loadConvert(content));
                            String errorMessage = jsonObject.optString("errorMessage ");
                            if (jsonObject.optString("errorCode").equals("0")) {
                                JSONArray jsonArray = jsonObject.optJSONObject("data").optJSONArray("list");
                                centerStatus = jsonObject.optJSONObject("data").optInt("status");
                                if (jsonArray != null & jsonArray.length() > 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        ActivityBean activityBean = new ActivityBean(jsonArray.optJSONObject(i));
                                        arrayList.add(activityBean);
                                    }
                                    setValue();
                                } else {
                                    showToast("该地区没有开放的活动中心！");
                                    if (adapter != null)
                                        adapter.notifyDataSetChanged();
                                }
                            } else {
                                showToast(errorMessage);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context,"正在加载...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.7====statusCode==" + statusCode + "================" + content);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }
                });
    }

    private void setValue() {
        adapter = new LCActivityListAdapter(LCActivityListActivity.this, arrayList, centerStatus);
        xListView.setAdapter(adapter);
    }

    //得到城市列表
    public void getCityDataList() {
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        AbRequestParams params = new AbRequestParams();
        params.put("page", "");
        params.put("pagesize", 10 + "");
        mAbHttpUtil.postUrl(LCConstant.RE_GET_AREA_LIST, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("=======4.7====statusCode==" + statusCode + "================" + loadConvert(content));
                        centerCity = getCity(content);
                        setContent(centerCity);
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.7====statusCode==" + statusCode + "================" + content);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // LCUtils.stopProgressDialog();
                    }
                });
    }


    // 获取城市
    public CenterCity getCity(String theString) {
        CenterCity ag = null;
        if (TextUtils.isEmpty(theString)) {
            ag = new CenterCity();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }
        String readFrom = loadConvert(theString);

        try {
            JSONObject jo = new JSONObject(readFrom);
            String errorCode = jo.optString("errorCode", "1");
            String errorMessage = jo.optString("errorMessage", "");
            if ("0".equals(errorCode)) {
                JSONArray jsonArray = jo.getJSONArray("data");
                List<ProvinceBean> list = new ArrayList<ProvinceBean>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String id = jObject.optString("pid", "");
                    String name = jObject.optString("pname", "");
                    JSONArray cityChilds = jObject.getJSONArray("city_list");
                    List<CityBean> listCityModel = new ArrayList<CityBean>();
                    if (cityChilds.length() == 0) {

                        DistrictBean dm = new DistrictBean(name, id);
                        List<DistrictBean> listDistrictModel = new ArrayList<DistrictBean>();
                        listDistrictModel.add(dm);
                        CityBean cm = new CityBean(name, id, listDistrictModel);
                        listCityModel.add(cm);
                        ProvinceBean pm = new ProvinceBean(id, name, listCityModel);
                        list.add(pm);
                        continue;
                    }

                    for (int p = 0; p < cityChilds.length(); p++) {
                        JSONObject cityObject = cityChilds.getJSONObject(p);
                        String cityId = cityObject.optString("cid", "");
                        String cityName = cityObject.optString("cname",
                                "");
                        JSONArray districtChilds = cityObject
                                .getJSONArray("district_list");
                        List<DistrictBean> listDistrictModel = new ArrayList<DistrictBean>();
                        if (districtChilds.length() == 0) {
                            DistrictBean dm = new DistrictBean(cityName,
                                    cityId);
                            listDistrictModel.add(dm);
                            CityBean cm = new CityBean(cityName, cityId, listDistrictModel);
                            listCityModel.add(cm);
                            ProvinceBean pm = new ProvinceBean(cityId,
                                    cityName, listCityModel);
                            list.add(pm);
                            continue;
                        }

                        for (int q = 0; q < districtChilds.length(); q++) {
                            JSONObject districtObject = districtChilds
                                    .getJSONObject(q);
                            String districtId = districtObject.optString("did",
                                    "");
                            String districtName = districtObject.optString(
                                    "dname", "");
                            DistrictBean dm = new DistrictBean(districtName,
                                    districtId);
                            listDistrictModel.add(dm);
                        }

                        CityBean cm = new CityBean(cityName, cityId, listDistrictModel);
                        listCityModel.add(cm);
                    }

                    ProvinceBean pm = new ProvinceBean(id, name, listCityModel);
                    list.add(pm);
                }
                ag = new CenterCity(errorCode, errorMessage, list);
                return ag;
            } else {
                ag = new CenterCity();
                ag.setErrorCode(errorCode);
                ag.setErrorMessage(errorMessage);
                return ag;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            ag = new CenterCity();
            ag.setErrorCode("1");
            ag.setErrorMessage("数据为空");
            return ag;
        }

    }
}
