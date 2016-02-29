package cn.ledoing.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-3-23
 * 编写人员:	 吴培涛
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class LCSharedPreferencesHelper {

	public static String TAG = "SharedPreferencesHelper";
	public static String IS_FIRST_LOCATION = "is_first_location";//是否首次定位
	public static String PROVINCIALID = "provincialid";//保存的省份ID
	public static String CITYID = "cityid";//保存的城市ID
	public static String DISTRICTID = "districtid";//保存的区域ID
	public static String PROVINCIALNAME = "provincialname";//保存的省份名字
	public static String CITYNAME  = "cityname";//保存的城市名字
	public static String DISTRICTNAME = "districtname";//保存的区域ID
	public static String IS_MEMBER = "is_member";//是否是会员  0不是会员   1是会员
	SharedPreferences sp;
	SharedPreferences.Editor editor;

	Context mContext;
	private static LCSharedPreferencesHelper sh;

	public static synchronized LCSharedPreferencesHelper instance(
			Context context, String pName) {
		if (sh == null) {
			sh = new LCSharedPreferencesHelper(context, pName);
		}
		return sh;
	}

	public LCSharedPreferencesHelper(Context context, String pName) {
		this.mContext = context;
		sp = mContext.getSharedPreferences(pName, 0);
		editor = sp.edit();
	}

	public void putValue(String key, String value) {
		editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void putBoolean(String key, boolean value) {
		editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public String getValue(String key) {
		return sp.getString(key, null);
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public void moveValue(String key) {
		editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}
}
