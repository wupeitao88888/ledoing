package cn.ledoing.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ledoing.bean.CityModel;
import cn.ledoing.bean.DistrictModel;
import cn.ledoing.bean.ProvinceModel;
import cn.ledoing.bean.TimAll;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

import com.lc.widget.OnWheelChangedListener;
import com.lc.widget.WheelView;
import com.lc.widget.adapters.ArrayWheelAdapter;

public class LCUpdateBrithday extends LCActivitySupport implements
		OnWheelChangedListener
// , DatePicker.OnDateChangedListener
{
	private LCTitleBar lc_update_title;
	private Context mContext;


	private Calendar mCalendar;
	private int year;
	private int monthOfYear;
	private int dayOfMonth;
	private boolean mTitleNeedsUpdate = true;
	private RelativeLayout lc_brithday_content;
	public static String date = null;
	private TextView lc_day;
	private AbHttpUtil mAbHttpUtil = null;
	private LCNoNetWork lc_brithday_nonet;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	protected String[] mProvinceDatas;
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
	protected String mCurrentProviceName;
	protected String mCurrentCityName;
	/** 当前区的名称 */
	protected String mCurrentDistrictName = "";
	/** 当前区的邮政编码 */
	protected String mCurrentZipCode = "";
	private Handler handler;
	private TextView lc_city;
	private LCTitleBar lc_updateusercity_title;
	private List<ProvinceModel> provinceList = null;
	private TimAll timeall;
	private String brithday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_upodatebrithday);
		mContext = this;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lc_update_title = (LCTitleBar) findViewById(R.id.lc_updatebrithday_title);
		lc_update_title.setCenterTitle(mString(R.string.lc_new_brithday_title));
		lc_update_title.setRightTitle("保存");
		lc_update_title.setRightTitleListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateInit();
			}
		});
		lc_brithday_nonet = (LCNoNetWork) findViewById(R.id.lc_brithday_nonet);
		lc_brithday_content = (RelativeLayout) findViewById(R.id.lc_brithday_content);
		lc_brithday_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoNet();
            }
        });

		lc_day = (TextView) findViewById(R.id.lc_day);
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
		brithday = intent.getStringExtra("brithday");
		setContent(timeall);
		isNoNet();
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
		if (!TextUtils.isEmpty(brithday)) {
			if (brithday.equals(mCurrentProviceName + "-" + monthUpdateString
					+ "-" + dayUpdateString)) {
				finish();
				return;
			}
		}
		netInit(mCurrentProviceName + "-" + monthUpdateString + "-"
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
		mCurrentCityName = monthOfYear + "";
		mCurrentDistrictName = (dayOfMonth) + "";
		lc_day.setText(mCurrentProviceName + "-" + mCurrentCityName + "-"
				+ mCurrentDistrictName + "");
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
		getBrithDay(brithday);
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

        String[]  cities;
        //判断是否是当前年，如果是则月份是当前月，如果不是则是12个月
        if(mCurrentProviceName.equals(year+"")){
            cities =new String[monthOfYear+1];
            for(int i=0;i<monthOfYear+1;i++){
                cities[i]=""+(Integer)(i+1);
            }
        }else{
            cities =new String[12];
            for(int i=0;i<cities.length;i++){
                cities[i]=""+(Integer)(i+1);
            }
        }
		if (cities == null) {
			cities = new String[] { "" };
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
           //判断是否是当前年当前月如果是则显示到当前日期
           if(mCurrentProviceName.equals(year+"")&mCurrentCityName.equals((Integer)(monthOfYear+1)+"")){
               areas = new String[dayOfMonth];
               for(int i=0;i<dayOfMonth;i++){
                   areas[i]=(Integer)(i+1)+"";
               }
           }else{
               // 是否闰年
               if (!isLeapYear(Integer.parseInt(mCurrentProviceName))
                       & mCurrentCityName.equals("2")) {
                   areas = getLeapDay();
               } else {
                   areas = mDistrictDatasMap.get(mCurrentCityName);
               }
               if (areas == null) {
                   areas = new String[] { "" };
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
			lc_day.setText(mCurrentProviceName + "-" + mCurrentCityName + "-"
					+ mCurrentDistrictName + "");
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

	private void isNoNet() {
		// TODO Auto-generated method stub
		if (LCUtils.isNetworkAvailable(this)) {
			setNotNetBack();
		} else {
			setNotNet();
		}
	}

	public void setNotNetBack() {
		lc_brithday_nonet.setVisibility(View.GONE);
		lc_brithday_content.setVisibility(View.VISIBLE);
	}

	public void setNotNet() {
		lc_brithday_nonet.setVisibility(View.VISIBLE);
		lc_brithday_content.setVisibility(View.GONE);
	}

	private void netInit(final String realname) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(realname)) {
			showToast("修改生日为空");
			return;
		}
		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.setTimeout(5000);
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("birthday", realname);
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
								sharedPreferencesHelper.putValue("birthday",
										realname);
								lcSharedPreferencesLogin.putValue("birthday",
										realname);
								showToast("保存成功！");
								finish();
							} else {
								showToast(errorMessage);
								LCUtils.ReLogin(errorCode, context,errorMessage);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							showToast("保存失败！");
						}
					};

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
					};

				});
	}
}
