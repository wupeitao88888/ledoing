package cn.ledoing.view;

/**
 * Created by wupeitao on 15/11/19.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 能够兼容ViewPager的ScrollView
 *
 * @Description: 解决了ViewPager在ScrollView中的滑动反弹问题
 * @File: ScrollViewExtend.java
 * @Package com.image.indicator.control
 * @Author Hanyonglu
 * @Date 2012-6-18 下午01:34:50
 * @Version V1.0
 */
public class ScrollViewExtend extends ScrollView {
    private GestureDetector mGestureDetector;

    public ScrollViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
    }
}