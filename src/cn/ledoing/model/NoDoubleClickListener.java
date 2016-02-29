package cn.ledoing.model;


import android.view.View.OnClickListener;
import android.view.View;
import java.util.Calendar;

/**
 * Created by lc-php1 on 2015/10/20.
 */
public abstract class NoDoubleClickListener implements OnClickListener {
    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    public void onNoDoubleClick(View v) {

    }
}
