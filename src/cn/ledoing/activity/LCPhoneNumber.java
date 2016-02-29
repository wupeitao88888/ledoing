package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.ledoing.view.LCTitleBar;

public class LCPhoneNumber extends LCActivitySupport {
	private LCTitleBar lc_phonenumber_title;
	private TextView lc_phonenumber_content;
	private Button lc_phonenumber_update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.avtivity_lc_phonenumber);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		lc_phonenumber_title = (LCTitleBar) findViewById(R.id.lc_phonenumber_title);
		lc_phonenumber_title.setCenterTitle("手机号");
		lc_phonenumber_content = (TextView) findViewById(R.id.lc_phonenumber_content);
		lc_phonenumber_update = (Button) findViewById(R.id.lc_phonenumber_update);
		lc_phonenumber_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LCPhoneNumber.this,
						LCUpdatePhone.class));
			}
		});
	}

}
