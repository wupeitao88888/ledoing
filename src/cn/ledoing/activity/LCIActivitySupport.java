package cn.ledoing.activity;

import android.app.ProgressDialog;
import android.content.Context;
import cn.ledoing.global.LCApplication;

/**
 * Activity帮助支持类接口.
 * 
 * @author wpt
 */
public interface LCIActivitySupport {
	/**
	 * 
	 * 获取EimApplication.
	 * 
	 * @author wupeitao
	 * @update 2012-7-6 上午9:05:51
	 */
	public abstract LCApplication getJXBApplication();

	/**
	 * 
	 * 校验网络-如果没有网络就弹出设置,并返回true.
	 * 
	 * @return
	 * @author wupeitao
	 * @update 2012-7-6 上午9:03:56
	 */
	public abstract boolean validateInternet();

	/**
	 * 
	 * 校验网络-如果没有网络就返回true.
	 * 
	 * @return
	 * @author wupeitao
	 * @update 2012-7-6 上午9:05:15
	 */
	public abstract boolean hasInternetConnected();

	/**
	 * 
	 * 退出应用.
	 * 
	 * @author wupeitao
	 * @update 2012-7-6 上午9:06:42
	 */
	public abstract void isExit();

	/**
	 * 
	 * 判断基站是否已经开启.
	 * 
	 * @return
	 * @author wupeitao
	 * @update 2012-7-6 上午9:07:34
	 */
	public abstract boolean hasLocationNetWork();

	/**
	 * 
	 * 检查内存卡.
	 * 
	 * @author wupeitao
	 * @update 2012-7-6 上午9:07:51
	 */
	public abstract void checkMemoryCard();

	/**
	 * 
	 * 显示toast.
	 * 
	 * @param text
	 *            内容
	 * @param longint
	 *            内容显示多长时间
	 * @author wupeitao
	 * @update 2012-7-6 上午9:12:02
	 */
	public abstract void showToast(String text, int longint);

	/**
	 * 
	 * 短时间显示toast.
	 * 
	 * @param text
	 * @author wupeitao
	 * @update 2012-7-6 上午9:12:46
	 */
	public abstract void showToast(String text);

	/**
	 * 
	 * 获取进度条.
	 * 
	 * @return
	 * @author wupeitao
	 * @update 2012-7-6 上午9:14:38
	 */
	public abstract ProgressDialog getProgressDialog();

	/**
	 * 
	 * 返回当前Activity上下文.
	 * 
	 * @return
	 * @author wupeitao
	 * @update 2012-7-6 上午9:19:54
	 */
	public abstract Context getContext();

	/** 转换string——>String */
	public String mString(int string);

}
