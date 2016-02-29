package cn.ledoing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by lc-php1 on 2015/11/18.
 */
public class CustomListV extends ListView {

    public CustomListV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListV(Context context) {
        super(context);
    }

    public CustomListV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyOnTouchListener listener : onTouchListeners) {
            listener.dispatchTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.add(myOnTouchListener);
    }

    public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
        onTouchListeners.remove(myOnTouchListener);
    }

    public interface MyOnTouchListener {
        public boolean dispatchTouchEvent(MotionEvent ev);
    }
}
