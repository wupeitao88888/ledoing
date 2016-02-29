/*******************************************************************************
 * Copyright (c) 2015 by ehoo Corporation all right reserved.
 * 2015-4-21 
 * 
 *******************************************************************************/
package cn.ledoing.view;



import cn.ledoing.activity.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2015-4-21
 * 编写人员:	 吴培涛
 * 
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class LCQuestionDialog extends Dialog {
	Context context;
	private View view;
	private boolean isback = false;

	public LCQuestionDialog(Context context, View view) {
		super(context); // TODO Auto-generated constructor stub
		this.context = context;
	}

	public LCQuestionDialog(Context context, int theme, View view) {
		super(context, theme);
		this.context = context;
		this.view = view;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) { // TODO Auto-generated

		super.onCreate(savedInstanceState);
		this.setContentView(view);
		this.setCancelable(false);// 设置点击屏幕Dialog不消失
		Window window = getWindow();
		// window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mystyle); // 添加动画

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}

	public void setIsback(boolean isback) {
		this.isback = isback;
	}
}
