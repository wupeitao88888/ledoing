package cn.ledoing.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import cn.ledoing.adapter.SelectPicChildAdapter;
import cn.ledoing.view.LCTitleBar;

/**
 * 
 * @author 039
 * 
 */
public class SelectPicShowImageActivity extends Activity {

	/** 图片展示的GridView */
	private GridView mGridView;

	/** 存储路径的list */
	private ArrayList<String> list;
	/** 适配器 */
	private SelectPicChildAdapter adapter;
	/** 相册 */
	public static final int ALBUM = 0;
	private LCTitleBar lc_selecteimage_title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_activity_selectpic_show_image);
		mGridView = (GridView) findViewById(R.id.child_grid);
		initData();

	}

	private void initData() {
		lc_selecteimage_title = (LCTitleBar) findViewById(R.id.lc_selecteimage_title);
		lc_selecteimage_title.setCenterTitle("相册");
		// 相册选择
		String folderName = getIntent().getStringExtra("folderName");
		Intent intent = getIntent();
		list = intent.getStringArrayListExtra("data");
		adapter = new SelectPicChildAdapter(this, list);
		mGridView.setAdapter(adapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("path", list.get(position));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}
