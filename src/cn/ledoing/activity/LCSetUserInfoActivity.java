package cn.ledoing.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lc.widget.OnWheelChangedListener;
import com.lc.widget.WheelView;
import com.lc.widget.adapters.ArrayWheelAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ledoing.bean.City;
import cn.ledoing.bean.CityModel;
import cn.ledoing.bean.DistrictModel;
import cn.ledoing.bean.ProvinceModel;
import cn.ledoing.bean.TimAll;
import cn.ledoing.global.LCApplication;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;

/**
 * Created by cheers on 2015/9/8.
 */
public class LCSetUserInfoActivity extends LCActivitySupport implements OnWheelChangedListener, View.OnClickListener {

    private LCTitleBar titleName;
    private Button submit;
    private Context mContext;

    /**
     * 以下变量是城市选择器需要的变量
     */
    protected String[] mProvinceDatas;
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName = "";
    protected String mCurrentZipCode = "";
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    List<ProvinceModel> provinceList = null;
    private LinearLayout layoutCity;
    private TextView chooseCity;
    private PopupWindow popupWindow;
    private View popView;
    private TextView sure;
    private TextView chooseBirthDay;
    public int isSet = 0;

    private TextView chooseName;
    private TextView chooseSex;
    private City city;
    private TimAll timeall;
    String sex[] = {"男", "女"};
    private LinearLayout close;
    private LCTitleBar lc_title;
    private Calendar mCalendar;
    private int year;
    private int monthOfYear;
    private int dayOfMonth;
    private AbHttpUtil mAbHttpUtil;
    int sexNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_userinfo);
        mContext = this;
        initView();
    }

    private void initView() {
        initTitle();
        String fromAssets = getFromAssets("city.txt");
        city = JSONUtils.getInstatce().getCity(fromAssets);
        mCalendar = Calendar.getInstance();
        year = mCalendar.get(Calendar.YEAR);
        monthOfYear = mCalendar.get(Calendar.MONTH);
        dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
        timeall = JSONUtils.getInstatce().getTimeAll();
        chooseName = (TextView) findViewById(R.id.name);
        chooseCity = (TextView) findViewById(R.id.choose_city);
        chooseBirthDay = (TextView) findViewById(R.id.choose_birthday);
        chooseSex = (TextView) findViewById(R.id.choose_sex);
        if (!TextUtils.isEmpty(sharedPreferencesHelper.getValue("sex"))) {
            if ("1".equals(sharedPreferencesHelper.getValue("sex")))
                chooseSex.setText("男");
            else{
                chooseSex.setText("女");
            }
        }
        if (!TextUtils.isEmpty(sharedPreferencesHelper.getValue("realname"))) {
            chooseName.setText(sharedPreferencesHelper.getValue("realname"));
        }
        if (!TextUtils.isEmpty(sharedPreferencesHelper.getValue("provincial"))) {
            chooseCity.setText(sharedPreferencesHelper.getValue("provincial") + "：省   " + sharedPreferencesHelper.getValue("city") + "：市   " + sharedPreferencesHelper.getValue("district"));
        }
        if (!TextUtils.isEmpty(sharedPreferencesHelper.getValue("birthday"))) {
            chooseBirthDay.setText(sharedPreferencesHelper.getValue("birthday"));
        }
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseName.getText().toString().isEmpty()) {
                    showToast("请输入名字");
                    return;
                } else if (chooseSex.getText().toString().isEmpty()) {
                    showToast("请选择性别");
                    return;
                } else if (chooseBirthDay.getText().toString().isEmpty()) {
                    showToast("请选择生日");
                    return;
                } else if (chooseBirthDay.getText().toString().isEmpty()) {
                    showToast("请选择地区");
                    return;
                } else {
                    updateUserInfo();
                }
            }
        });
        chooseCity.setOnClickListener(this);
        chooseBirthDay.setOnClickListener(this);
        chooseSex.setOnClickListener(this);
        initPopupWindow();
    }

    private void initTitle() {
        lc_title = (LCTitleBar) findViewById(R.id.lc_title);
        lc_title.setCenterTitle("个人信息");
        lc_title.setCenterTitleColor(getResources().getColor(R.color.white));
        lc_title.setBackGb(getResources().getColor(R.color.titlebar_userinfo));
        lc_title.setLeftImage(R.drawable.goback);
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
                if (isSet == 3) {//选择城市
                    chooseCity.setText(mCurrentProviceName + "：省   " + mCurrentCityName
                            + "：市   " + mCurrentDistrictName + "：区  ");
                } else if (isSet == 2) {//选择生日
                    chooseBirthDay.setText(mCurrentProviceName + "-" + mCurrentCityName + "-"
                            + mCurrentDistrictName + "");
                } else if (isSet == 1) {//选择性别
                    int pCurrent = mViewProvince.getCurrentItem();
                    mCurrentProviceName = sex[pCurrent];
                    chooseSex.setText(mCurrentProviceName);
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
    }

    public String getFromAssets(String fileName) {
        String Result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            return Result = "{\"errorCode\":1,\"errorMessage\":[\"错误\"],\"data\":[]}";
        }
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mViewProvince && isSet != 1) {
            if (isSet != 2) {//不是生日更新
                updateCities();
            } else {
                updateMonth();
            }
        } else if (wheel == mViewCity) {
            if (isSet != 2) {//不是生日更新
                updateAreas();
            } else {
                updateDay();
            }
        } else if (wheel == mViewDistrict) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
            mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
            L.e(mCurrentDistrictName);
        }
    }

    protected void setContent(City city) {
        // TODO Auto-generated method stub

        initProvinceDatas(city);
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
                mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibility(View.VISIBLE);
        mViewDistrict.setVisibility(View.VISIBLE);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        mViewProvince.setCurrentItem(0);
        mViewCity.setCurrentItem(0);
        mViewDistrict.setCurrentItem(0);

    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateDay() {
        int pCurrent = mViewCity.getCurrentItem();
        try {
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
            String[] areas = null;
            //判断是否是当前年当前月如果是则显示到当前日期
            if (mCurrentProviceName.equals(year + "") & mCurrentCityName.equals((Integer) (monthOfYear + 1) + "")) {
                areas = new String[dayOfMonth];
                for (int i = 0; i < dayOfMonth; i++) {
                    areas[i] = (Integer) (i + 1) + "";
                }
            } else {
                // 是否闰年
                if (!isLeapYear(Integer.parseInt(mCurrentProviceName))
                        & mCurrentCityName.equals("2")) {
                    areas = getLeapDay();
                } else {
                    areas = mDistrictDatasMap.get(mCurrentCityName);
                }
                if (areas == null) {
                    areas = new String[]{""};
                }
            }


            mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this,
                    areas));
            mViewDistrict.setCurrentItem(0);
            int currentItem = mViewDistrict.getCurrentItem();
            String string = areas[currentItem];

        } catch (Exception e) {

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

    public boolean isLeapYear(int i) {
        if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateMonth() {
        int pCurrent = mViewProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];

        String[] cities;
        //判断是否是当前年，如果是则月份是当前月，如果不是则是12个月
        if (mCurrentProviceName.equals(year + "")) {
            cities = new String[monthOfYear + 1];
            for (int i = 0; i < monthOfYear + 1; i++) {
                cities[i] = "" + (Integer) (i + 1);
            }
        } else {
            cities = new String[12];
            for (int i = 0; i < cities.length; i++) {
                cities[i] = "" + (Integer) (i + 1);
            }
        }
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
            String[] areas = mDistrictDatasMap.get(mCurrentCityName);

            if (areas == null) {
                areas = new String[]{""};
            }
            mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(this,
                    areas));
            mViewDistrict.setCurrentItem(0);
            int currentItem = mViewDistrict.getCurrentItem();
            String string = areas[currentItem];
//            lc_city.setText(mCurrentProviceName + "：省   " + mCurrentCityName
//                    + "：市   " + string + "：区  ");
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

    protected void initProvinceDatas(City city2) {

        try {

            // 获取解析出来的数据
            provinceList = new ArrayList<ProvinceModel>();

            provinceList.addAll(city2.getList());
            // */ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProviceName = provinceList.get(0).getName();

                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0)
                            .getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getXingzhengdaima();
                }
            }

            // */
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j)
                            .getDistrictList();
                    String[] distrinctNameArray = new String[districtList
                            .size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList
                            .size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        DistrictModel districtModel = districtList.get(k);
                        // 遍历市下面所有区/县的数据
                        // DistrictModel districtModel = new DistrictModel(name,
                        // id, pinyin, division_type, parent_id,
                        // xingzhengdaima, show_order, status,
                        // update_time, alias, create_time, initials)
                        //
                        // ;
                        // 区/县对于的邮编，保存到mZipcodeDatasMap
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

    protected void setContent(TimAll city) {
        // TODO Auto-generated method stub

        initProvinceDatas(city);
        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
                mProvinceDatas));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewCity.setVisibility(View.VISIBLE);
        mViewDistrict.setVisibility(View.VISIBLE);
        mViewCity.setVisibleItems(7);
        mViewDistrict.setVisibleItems(7);
        mViewProvince.setCurrentItem(year - 2000);
        mViewCity.setCurrentItem(monthOfYear);
        mViewDistrict.setCurrentItem(dayOfMonth - 1);
    }

    protected void setContentSex() {
        // TODO Auto-generated method stub

        mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
                sex));
        // 设置可见条目数量
        mViewProvince.setVisibleItems(7);
        mViewProvince.setCurrentItem(0);
        mViewCity.setVisibility(View.GONE);
        mViewDistrict.setVisibility(View.GONE);
//        mViewCity.setVisibleItems(7);
//        mViewDistrict.setVisibleItems(7);
//        updateCities();
//        updateAreas();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.choose_city:
                isSet = 3;
                setContent(city);
                //为popWindow添加动画效果
                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                // 点击弹出泡泡窗口
                popupWindow.showAtLocation((LinearLayout) findViewById(R.id.parent), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.choose_birthday:
                isSet = 2;
                setContent(timeall);
                //为popWindow添加动画效果
                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                // 点击弹出泡泡窗口
                popupWindow.showAtLocation((LinearLayout) findViewById(R.id.parent), Gravity.BOTTOM, 0, 0);
                break;
            case R.id.choose_sex:
                isSet = 1;
                setContentSex();
                //为popWindow添加动画效果
                popupWindow.setAnimationStyle(R.style.popWindow_animation);
                // 点击弹出泡泡窗口
                popupWindow.showAtLocation((LinearLayout) findViewById(R.id.parent), Gravity.BOTTOM, 0, 0);
                break;
            default:
        }
    }

    protected Bundle getProvince(String pro, String ct, String dc) {
        // TODO Auto-generated method stub
        String Provinceid = null;
        String cityid = null;
        String Districtid = null;
        for (int i = 0; i < city.getList().size(); i++) {
            if (pro.equals(city.getList().get(i).getName())) {
                Provinceid = city.getList().get(i).getId();

                for (int y = 0; y < city.getList().get(i).getCityList().size(); y++) {
                    if (ct.equals(city.getList().get(i).getCityList().get(y)
                            .getName())) {
                        cityid = city.getList().get(i).getCityList().get(y)
                                .getId();
                        for (int u = 0; u < city.getList().get(i)
                                .getCityList().get(y).getDistrictList().size(); u++) {
                            if (dc.equals(city.getList().get(i).getCityList()
                                    .get(y).getDistrictList().get(u).getName())) {
                                Districtid = city.getList().get(i)
                                        .getCityList().get(y).getDistrictList()
                                        .get(u).getId();
                                break;
                            }
                        }
                    }
                }

            }
        }
        Bundle bd = new Bundle();
        bd.putString("Provinceid", Provinceid);
        bd.putString("cityid", cityid);
        bd.putString("Districtid", Districtid);
        return bd;
    }

    private void updateUserInfo() {
        // TODO Auto-generated method stub
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        String name = chooseCity.getText().toString();
        final String pro = name.substring(0, name.indexOf("：省"));
        final String substring = name.substring(name.indexOf("：省") + 2,
                name.indexOf("：市")).replace(" ", "");
        final String substring2 = name.substring(name.indexOf("：市") + 2,
                name.indexOf("：区")).replace(" ", "");
        Bundle bd = getProvince(pro, substring, substring2);
        String provincialid = bd.getString("Provinceid");
        String cityid = bd.getString("cityid");
        String districtid = bd.getString("Districtid");
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("realname", chooseName.getText().toString());
        params.put("birthday", chooseBirthDay.getText().toString());
        params.put("province", provincialid);
        params.put("city", cityid);
        params.put("district", districtid);

        if (chooseBirthDay.getText().toString().equals("男")) {
            sexNum = 0;
            params.put("sex", sexNum + "");
        } else {
            sexNum = 1;
            params.put("sex", sexNum + "");
        }
        params.put("uuid", getOnly());

        mAbHttpUtil.postUrl(LCConstant.UPDATE_USER_INFO, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // 移除进度框
                        AbDialogUtil.removeDialog(context);
                        String loadConvert = loadConvert(content);
                        L.d("hy", loadConvert);
                        try {
                            JSONObject jo = new JSONObject(loadConvert);
                            String errorCode = jo.optString("errorCode", "1");
                            String errorMessage = jo.optString("errorMessage",
                                    "保存失败！");
                            if ("0".equals(errorCode)) {
                                showToast("保存成功！");
                                sharedPreferencesHelper.putValue("realname",
                                        chooseName.getText().toString());
                                sharedPreferencesHelper.putValue("birthday",
                                        chooseBirthDay.getText().toString());
                                sharedPreferencesHelper.putValue("sex",
                                        sexNum + "");
                                lcSharedPreferencesLogin.putValue("realname",
                                        chooseName.getText().toString());
                                lcSharedPreferencesLogin.putValue("birthday",
                                        chooseBirthDay.getText().toString());
                                lcSharedPreferencesLogin.putValue("sex",
                                        sexNum + "");
                                sharedPreferencesHelper.putValue("provincial",
                                        pro);
                                sharedPreferencesHelper.putValue("city",
                                        substring);
                                sharedPreferencesHelper.putValue("district",
                                        substring2);
                                lcSharedPreferencesLogin.putValue("provincial",
                                        pro);
                                lcSharedPreferencesLogin.putValue("city",
                                        substring);
                                lcSharedPreferencesLogin.putValue("district",
                                        substring2);
                                LCSetUserInfoActivity.this.finish();
                                Intent intent = new Intent(mContext, LCActivityListActivity.class);
                                startActivity(intent);

                            } else {
//                                LCUtils.ReLogin(errorCode, context, errorMessage);
                                showToast(errorMessage);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            showToast("保存失败！");
                        }
                    }

                    ;

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        AbDialogUtil.showProgressDialog(context, 0, "正在保存...");
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
                    }

                    ;

                });
    }

}
