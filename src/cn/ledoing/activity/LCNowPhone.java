package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.ledoing.view.LCTitleBar;

public class LCNowPhone extends LCActivitySupport {
	private LCTitleBar lc_lcnowphone_title;
	private TextView lc_right_phone;
	private Button lc_update_phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lcnow_phone);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lc_lcnowphone_title = (LCTitleBar) findViewById(R.id.lc_lcnowphone_title);
		lc_lcnowphone_title.setCenterTitle("手机号");
		lc_right_phone = (TextView) findViewById(R.id.lc_right_phone);
		lc_update_phone = (Button) findViewById(R.id.lc_update_phone);
		Intent intent = getIntent();
		final String stringExtra = intent.getStringExtra("phone");
		lc_right_phone.setText(stringExtra);
		lc_update_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(LCNowPhone.this,
						LCUpdatePhone.class);
				intent2.putExtra("phone", stringExtra);
				startActivity(intent2);
				finish();
			}
		});
	}

}
