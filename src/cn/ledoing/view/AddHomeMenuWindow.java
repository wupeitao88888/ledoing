package cn.ledoing.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.lc.widget.OnWheelChangedListener;
import com.lc.widget.WheelView;
import com.lc.widget.adapters.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ledoing.activity.R;
import cn.ledoing.adapter.PopListAdapter;
import cn.ledoing.adapter.SequenceAdapter;
import cn.ledoing.bean.CenterCity;
import cn.ledoing.bean.City;
import cn.ledoing.bean.CityBean;
import cn.ledoing.bean.DistrictBean;
import cn.ledoing.bean.DistrictModel;
import cn.ledoing.bean.LCTeacherBean;
import cn.ledoing.bean.ProvinceBean;
import cn.ledoing.bean.ProvinceModel;
import cn.ledoing.bean.Sequence;
import cn.ledoing.global.LCApplication;
import cn.ledoing.model.AddHomeMenuListener;
import cn.ledoing.model.ApplicationLocationListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCSharedPreferencesHelper;

/**
 * 自定义popupWindow
 *
 * @author wpt
 */
public class AddHomeMenuWindow extends PopupWindow implements View.OnClickListener, OnWheelChangedListener {
    private View conentView;
    private RelativeLayout endView;

    private RelativeLayout city_choose;
    private RelativeLayout scope_choose;
    private RelativeLayout sort_choose;
    private WheelView id_province;
    private WheelView id_city;
    private WheelView districts;
    private ListView sort;
    private Context context;
    private String[] mProvinceDatas;
    private CenterCity cityString;
    List<ProvinceBean> provinceList = null;
    protected String mCurrentProviceName;
    protected String mCurrentCityName;
    protected String mCurrentDistrictName = "";

    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    private String provincialid;
    private String cityid;
    private String districtid;
    private int provicePosition = 0;
    private int cityPosition = 0;
    private int centerStatus = 0;
    private SequenceAdapter sequenceAdapter;
    private AddHomeMenuListener addHomeMenuListener;



    public AddHomeMenuWindow(final Activity context, CenterCity cityString) {
        this.context = context;
        this.cityString = cityString;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.layout_menu, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
//        this.setOutsideTouchable(false);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
//         点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationPreview);

        endView = (RelativeLayout) conentView.findViewById(R.id.endview);

        city_choose = (RelativeLayout) conentView.findViewById(R.id.city_choose);
        scope_choose = (RelativeLayout) conentView.findViewById(R.id.scope_choose);
        sort_choose = (RelativeLayout) conentView.findViewById(R.id.sort_choose);

        id_province = (WheelView) conentView.findViewById(R.id.id_province);
        id_city = (WheelView) conentView.findViewById(R.id.id_city);
        districts = (WheelView) conentView.findViewById(R.id.districts);

        sort = (ListView) conentView.findViewById(R.id.sort);


        endView.setOnClickListener(this);
        id_province.addChangingListener(this);
        id_city.addChangingListener(this);
        districts.addChangingListener(this);
        setContent(cityString);
        List<Sequence> sequenceList = new ArrayList<>();
        Sequence sequence = new Sequence("离我最近", "0");
        sequenceList.add(sequence);
        Sequence sequence2 = new Sequence("好评优先", "0");
        sequenceList.add(sequence2);
        sequenceAdapter = new SequenceAdapter(context, sequenceList);
        sort.setAdapter(sequenceAdapter);


        LCApplication.mInstance.setOnReceiveLocation(new ApplicationLocationListener() {

            @Override
            public void onReceiveLocation(BDLocation var1) {
                if ("161".equals(var1.getLocType())) {
                    //定位成功

                    if (addHomeMenuListener != null) {
                        addHomeMenuListener.onLBS(var1.getLatitude(), var1.getLongitude(), var1.getStreet());
                    }
                } else {
                    //定位失败
                    AbToastUtil.showToast(context, "定位失败！");
                }
            }

            @Override
            public void fild() {
                //定位失败
                AbToastUtil.showToast(context, "定位失败！");
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }

    //显示和选择城市
    public void setCity() {
        if (city_choose.getVisibility() == View.VISIBLE) {
            city_choose.setVisibility(View.GONE);
            getProviceId();
            getCityId();
            if (addHomeMenuListener != null) {
                addHomeMenuListener.onCityCall(mCurrentCityName, provincialid, cityid);
            }

        } else {
            city_choose.setVisibility(View.VISIBLE);

        }
        if (scope_choose.getVisibility() == View.VISIBLE) {
            districts.setCurrentItem(0);
        }
        scope_choose.setVisibility(View.GONE);
        sort_choose.setVisibility(View.GONE);
    }

    //显示和选择区
    public void setScope() {
        if (city_choose.getVisibility() == View.VISIBLE) {
            city_choose.setVisibility(View.GONE);
            getProviceId();
            getCityId();
            if (addHomeMenuListener != null) {
                addHomeMenuListener.onCityCall(mCurrentCityName, provincialid, cityid);
            }

        } else {
            city_choose.setVisibility(View.VISIBLE);

        }
        if (scope_choose.getVisibility() == View.VISIBLE) {
            districts.setCurrentItem(0);
        }
        scope_choose.setVisibility(View.GONE);
        sort_choose.setVisibility(View.GONE);
    }
    //显示和选择优先级
    public void setOrder() {
        if (sort_choose.getVisibility() == View.VISIBLE) {
            sort_choose.setVisibility(View.GONE);
            if (addHomeMenuListener != null) {
                addHomeMenuListener.onOrder(sequenceAdapter.getSequence().getContent());
            }
        } else {
            sort_choose.setVisibility(View.VISIBLE);
        }
        if (city_choose.getVisibility() == View.VISIBLE) {
            city_choose.setVisibility(View.GONE);
            id_province.setCurrentItem(0);
            id_city.setCurrentItem(0);
        }
        if (scope_choose.getVisibility() == View.VISIBLE) {
            districts.setCurrentItem(0);
        }
        city_choose.setVisibility(View.GONE);
        scope_choose.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.endview:
                if (!this.isShowing()) {
                } else {
                    this.dismiss();
                }
                break;
        }
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

    protected void setContent(CenterCity city) {
        // TODO Auto-generated method stub
        initProvinceDatas(city);
        id_province.setViewAdapter(new ArrayWheelAdapter<String>(context,
                mProvinceDatas));
        // 设置可见条目数量
        id_province.setVisibleItems(7);
        id_city.setVisibleItems(7);
        districts.setVisibleItems(7);
        updateCities();
        updateAreas();
    }

    public void setAddHomeMenuListener(AddHomeMenuListener addHomeMenuListener) {

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

            districts.setViewAdapter(new ArrayWheelAdapter<String>(context,
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
        id_city.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
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
            e.printStackTrace();
        } finally {

        }
    }
}
