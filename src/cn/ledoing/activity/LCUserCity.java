package cn.ledoing.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ledoing.bean.City;
import cn.ledoing.bean.CityModel;
import cn.ledoing.bean.DistrictModel;
import cn.ledoing.bean.ProvinceModel;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

import com.lc.widget.OnWheelChangedListener;
import com.lc.widget.WheelView;
import com.lc.widget.adapters.ArrayWheelAdapter;

public class LCUserCity extends LCActivitySupport implements OnClickListener,
        OnWheelChangedListener {
    private AbHttpUtil mAbHttpUtil = null;
    private WheelView mViewProvince;
    private WheelView mViewCity;
    private WheelView mViewDistrict;
    /**
     * 所有省
     */
    protected String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;
    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";
    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";
    Handler handler;
    City city;
    private TextView lc_city;
    private LCTitleBar lc_updateusercity_title;
    List<ProvinceModel> provinceList = null;
    City city2;
    private RelativeLayout lc_updatecity_content;
    private LCNoNetWork lc_updateusercity_nonet;
    private String provincialU;
    private String cityU;
    private String districtU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_upodateusercity);
        mViewProvince = (WheelView) findViewById(R.id.id_province);
        mViewCity = (WheelView) findViewById(R.id.id_city);
        mViewDistrict = (WheelView) findViewById(R.id.id_district);
        lc_city = (TextView) findViewById(R.id.lc_city);

        lc_updateusercity_title = (LCTitleBar) findViewById(R.id.lc_updateusercity_title);
        lc_updateusercity_title.setCenterTitle("地区");
        lc_updateusercity_title.setRightTitle("保存");
        lc_updateusercity_title.setRightTitleListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    String name = lc_city.getText().toString()
                            .replaceAll(" ", "");
                    String oldAddress = provincialU + "：省   " + cityU + "：市   "
                            + districtU + "：区  ";
                    if (oldAddress.replaceAll(" ", "").equals(name)) {
                        finish();
                        return;
                    }
                    netInit(name);
                } catch (Exception e) {
                    showToast("保存失败！");
                }
            }
        });
        lc_updatecity_content = (RelativeLayout) findViewById(R.id.lc_updatecity_content);
        lc_updateusercity_nonet = (LCNoNetWork) findViewById(R.id.lc_updateusercity_nonet);

        lc_updateusercity_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });

        // 添加change事件
        mViewProvince.addChangingListener(this);
        // 添加change事件
        mViewCity.addChangingListener(this);
        // 添加change事件
        mViewDistrict.addChangingListener(this);
        Intent intent = getIntent();
        city2 = (City) intent.getSerializableExtra("usercity");
        provincialU = intent.getStringExtra("provincial");
        cityU = intent.getStringExtra("city");
        districtU = intent.getStringExtra("district");
        setContent(city2);
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
        lc_updateusercity_nonet.setVisibility(View.GONE);
        lc_updatecity_content.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_updateusercity_nonet.setVisibility(View.VISIBLE);
        lc_updatecity_content.setVisibility(View.GONE);
    }

    private void netInit(String name) {
        // TODO Auto-generated method stub
        if (TextUtils.isEmpty(name)) {
            showToast("修改为空");
            return;
        }

        final String pro = name.substring(0, name.indexOf("：省"));
        final String substring = name.substring(name.indexOf("：省") + 2,
                name.indexOf("：市"));
        final String substring2 = name.substring(name.indexOf("：市") + 2,
                name.indexOf("：区"));
        Bundle bd = getProvince(pro, substring, substring2);
        final String provincialid = bd.getString("Provinceid");
        final String cityid = bd.getString("cityid");
        final String districtid = bd.getString("Districtid");
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("provincial", provincialid);
        params.put("city", cityid);
        params.put("district", districtid);
        params.put("uuid", getOnly());

        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.UPDATE_USER_INFO, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // 移除进度框
                        AbDialogUtil.removeDialog(context);
                        String loadConvert = loadConvert(content);
                        try {
                            JSONObject jo = new JSONObject(loadConvert);
                            String errorCode = jo.optString("errorCode", "1");
                            String errorMessage = jo.optString("errorMessage",
                                    "保存失败！");
                            if ("0".equals(errorCode)) {
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
                                lcSharedPreferencesLogin.putValue(LCSharedPreferencesHelper.PROVINCIALID,
                                        provincialid);
                                lcSharedPreferencesLogin.putValue(LCSharedPreferencesHelper.CITYID,
                                        cityid);
                                lcSharedPreferencesLogin.putValue(LCSharedPreferencesHelper.DISTRICTID,
                                        districtid);
//                                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.PROVINCIALID,
//                                        provincialid);
//                                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.CITYID,
//                                        cityid);
//                                sharedPreferencesHelper.putValue(LCSharedPreferencesHelper.DISTRICTID,
//                                        districtid);
                                showToast("保存成功！");
                                finish();
                            } else {
//								showToast(errorMessage);
                                LCUtils.ReLogin(errorCode, context, errorMessage);
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
                        isNoNet();
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

    protected Bundle getProvince(String pro, String ct, String dc) {
        // TODO Auto-generated method stub
        String Provinceid = null;
        String cityid = null;
        String Districtid = null;
        for (int i = 0; i < city2.getList().size(); i++) {
            if (pro.equals(city2.getList().get(i).getName())) {
                Provinceid = city2.getList().get(i).getId();

                for (int y = 0; y < city2.getList().get(i).getCityList().size(); y++) {
                    if (ct.equals(city2.getList().get(i).getCityList().get(y)
                            .getName())) {
                        cityid = city2.getList().get(i).getCityList().get(y)
                                .getId();
                        for (int u = 0; u < city2.getList().get(i)
                                .getCityList().get(y).getDistrictList().size(); u++) {
                            if (dc.equals(city2.getList().get(i).getCityList()
                                    .get(y).getDistrictList().get(u).getName())) {
                                Districtid = city2.getList().get(i)
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

    protected void setContent(City city) {
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

        getUserCity();

    }

    private void getUserCity() {
        // TODO Auto-generated method stub
        if (TextUtils.isEmpty(provincialU) & TextUtils.isEmpty(cityU)
                & TextUtils.isEmpty(districtU)) {
            setCity(0, 0, 0);
            return;
        }
        getProstion();
    }

    private void getProstion() {
        // TODO Auto-generated method stub
        int p = 0;
        int c = 0;
        int d = 0;
        for (int i = 0; i < provinceList.size(); i++) {
            if (provincialU.equals(provinceList.get(i).getName())) {
                p = i;
                for (int k = 0; k < provinceList.get(i).getCityList().size(); k++) {
                    if (cityU.equals(provinceList.get(i).getCityList().get(k)
                            .getName())) {
                        c = k;
                        for (int h = 0; h < provinceList.get(i).getCityList()
                                .get(k).getDistrictList().size(); h++) {
                            if (districtU.equals(provinceList.get(i)
                                    .getCityList().get(k).getDistrictList()
                                    .get(h).getName())) {
                                d = h;
                            }
                        }
                    }
                }

            }
        }
        setCity(p, c, d);
    }

    // 从零开始
    public void setCity(int p, int c, int d) {
        mViewProvince.setCurrentItem(p);
        mViewCity.setCurrentItem(c);
        mViewDistrict.setCurrentItem(d);
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
            lc_city.setText(mCurrentProviceName + "：省   " + mCurrentCityName
                    + "：市   " + string + "：区  ");
        } catch (Exception e) {

        }
    }

    // 提交数据的时候提交选中数据
    public String getId(List<ProvinceModel> provinceList, String mp, String mc,
                        String md) {

        for (int i = 0; i < provinceList.size(); i++) {
            if (mp.equals(provinceList.get(i).getName())) {
                for (int p = 0; p < provinceList.get(i).getCityList().size(); p++) {
                    if (mc.equals(provinceList.get(i).getCityList().get(p)
                            .getName())) {
                        for (int y = 0; y < provinceList.get(i).getCityList()
                                .get(p).getDistrictList().size(); y++) {
                            if (md.equals(provinceList.get(i).getCityList()
                                    .get(p).getDistrictList().get(y).getName())) {

                            }
                        }
                    }
                }
            }
        }

        return mCurrentCityName;

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
            L.e(mCurrentDistrictName);
            lc_city.setText(mCurrentProviceName + "：省   " + mCurrentCityName
                    + "：市   " + mCurrentDistrictName + "：区  ");
        }
        // lc_city.setText(mCurrentProviceName + "：省   " + mCurrentCityName
        // + "：市   " + mCurrentDistrictName + "：区  ");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

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
}
