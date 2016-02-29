package cn.ledoing.view;

import cn.ledoing.activity.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

/**
 * 标题栏
 * 
 * @author wpt
 * 
 */
public class LCNoNetWork extends FrameLayout {

	private Context mContext;
	private Button retry;
	private OnClickListener backListenetForUser;

	// private OnClickListener backListener = new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// if (backListenetForUser != null) {
	// backListenetForUser.onClick(v);
	// } else {
	// ((Activity) mContext).onBackPressed();
	// }
	// }
	// };

	public LCNoNetWork(Context context) {
		super(context);
		init(context);
	}

	public LCNoNetWork(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.mContext = context;
		View inflate = View.inflate(context, R.layout.lc_nonetwork, null);
		retry = (Button) inflate.findViewById(R.id.retry);

		addView(inflate);
	}

	public void setRetryOnClickListener(OnClickListener retryOnClickListener) {
		retry.setOnClickListener(retryOnClickListener);
	}
}
