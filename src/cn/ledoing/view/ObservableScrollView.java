package cn.ledoing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if (mCallbacks != null) {
			mCallbacks.onScrollChanged(t);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mCallbacks.onDownMotionEvent();
				break;
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_CANCEL:
				mCallbacks.onUpOrCancelMotionEvent();
				break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	
	@Override
	protected int computeVerticalScrollRange() {
		// TODO Auto-generated method stub
		return super.computeVerticalScrollRange();
	}

	public static interface Callbacks {
		public void onScrollChanged(int scrollY);

		public void onDownMotionEvent();

		public void onUpOrCancelMotionEvent();
	}

	private Callbacks mCallbacks;

	public void setCallbacks(Callbacks listener) {
		mCallbacks = listener;
	}

}
