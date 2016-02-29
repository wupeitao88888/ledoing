package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.ledoing.view.LCTitleBar;

public class LCNONetWork extends LCActivitySupport {
	private LCTitleBar lc_nonetwork_title;
	private Button retry;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_nonetwork);
		lc_nonetwork_title = (LCTitleBar) findViewById(R.id.lc_nonetwork_title);
		lc_nonetwork_title.setCenterTitle("无网络连接");
		lc_nonetwork_title.setOnclickBackListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent();
				in.putExtra("isRefresh", "0");
				// -1为RESULT_OK, 1为RESULT_CANCEL..
				// in 则是回调的Activity内OnActivityResult那个方法内处理
				setResult(-1, in);
				finish();
			}
		});
		retry = (Button) findViewById(R.id.retry);
		retry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent();
				in.putExtra("isRefresh", "1");
				// -1为RESULT_OK, 1为RESULT_CANCEL..
				// in 则是回调的Activity内OnActivityResult那个方法内处理
				setResult(-1, in);
				finish();
			}
		});

	}

}
