package cn.ledoing.activity;

import android.os.Bundle;
import cn.ledoing.view.LCTitleBar;

public class LCCompanyInfo extends LCActivitySupport {
	private LCTitleBar lc_lccompany_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lccompany_info);
		lc_lccompany_title = (LCTitleBar) findViewById(R.id.lc_lccompany_title);
		lc_lccompany_title.setCenterTitle("公司简介");
	}

}
