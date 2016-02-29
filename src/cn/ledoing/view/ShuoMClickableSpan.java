package cn.ledoing.view;

import java.util.List;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import cn.ledoing.bean.LyricBean;

public class ShuoMClickableSpan extends ClickableSpan {

	String string;
	Context context;
	private int i;
	List<LyricBean> list;
	private static ItemCallBack itemCallBack;
	TextPaint ds;
	TextView mtextView;

	public static void setItemCallBack(ItemCallBack itemCallBack2) {
		itemCallBack = itemCallBack2;
	}

	public interface ItemCallBack {
		void onSuccess(List<LyricBean> list, int p);
	}

	public ShuoMClickableSpan(String str, Context context, int i,
			List<LyricBean> list, TextView mtextView) {
		super();
		this.string = str;
		this.context = context;
		this.i = i;
		this.list = list;
		this.mtextView = mtextView;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		this.ds = ds;
		// ds.setColor(Color.BLUE);

	}

	@Override
	public void onClick(View widget) {
		mtextView.invalidate();
		if (itemCallBack != null) {
			itemCallBack.onSuccess(list, i);
		}
	}

}
