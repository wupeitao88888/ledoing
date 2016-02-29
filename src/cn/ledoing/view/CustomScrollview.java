package cn.ledoing.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollview extends ScrollView {
	private ScrollInterface web;
	private int oldY = 0;
	private boolean isScrollStop;
	
	public boolean isScrollStop() {
		return isScrollStop;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			isScrollStop = false;
			if (msg.what == 0) {
				oldY = getScrollY();
				handler.sendEmptyMessageDelayed(1, 100);
			}else if (msg.what == 1) {
				if (oldY != getScrollY()) {
					oldY = getScrollY();
					handler.sendEmptyMessageDelayed(1, 50);
				}else{
					isScrollStop = true;
				}
			} 
			if (web != null) {
				web.onSChanged(isScrollStop);
			}
		};
	};

	public CustomScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_MOVE:
			if (web != null) {
				web.onSChanged(false);
			}
			break;
		case MotionEvent.ACTION_UP:
			handler.sendEmptyMessage(0);
			break;
		}
		return super.onTouchEvent(ev);
	}
	
	public void setOnCustomScroolChangeListener(ScrollInterface t){       
		this.web=t;    
	}    
	/**     
	* 自定义滑动接口     
	* @param t     
	*/    
	public interface ScrollInterface {    
		public void onSChanged(boolean isScrollStop);
	};    
	
}
