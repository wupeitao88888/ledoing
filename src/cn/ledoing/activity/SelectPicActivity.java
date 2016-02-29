package cn.ledoing.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import cn.ledoing.adapter.SelectPicGroupAdapter;
import cn.ledoing.bean.SelectPicImageBean;
import cn.ledoing.view.LCTitleBar;

/*
 * 
 * 相册
 */
@SuppressLint("HandlerLeak")
public class SelectPicActivity extends Activity {
	private HashMap<String, ArrayList<String>> mGruopMap = new HashMap<String, ArrayList<String>>();
	private List<SelectPicImageBean> list = new ArrayList<SelectPicImageBean>();
	private final static int SCAN_OK = 1;
	private SelectPicGroupAdapter adapter;
	private GridView mGroupGridView;
	private LCTitleBar lc_selecte_title;
	/** 相册 */
	public static final int ALBUM = 0;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			list = subGroupOfImage(mGruopMap);
			switch (msg.what) {
			case SCAN_OK:
				adapter = new SelectPicGroupAdapter(SelectPicActivity.this,
						list);
				mGroupGridView.setAdapter(adapter);
				break;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_activity_selectpic);
		initView();
		initData();
	}

	private void initView() {
		lc_selecte_title = (LCTitleBar) findViewById(R.id.lc_selecte_title);
		lc_selecte_title.setCenterTitle("选择图片");
		mGroupGridView = (GridView) findViewById(R.id.main_grid);
		mGroupGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent mIntent = new Intent(SelectPicActivity.this,
						SelectPicShowImageActivity.class);
				mIntent.putExtra("folderName", list.get(position)
						.getFolderName());
				ArrayList<String> childList = mGruopMap.get(list.get(position)
						.getFolderName());
				Collections.reverse(childList);// 把集合反转
				mIntent.putStringArrayListExtra("data", childList);
				startActivityForResult(mIntent, 1);
			}
		});
	}

	private void initData() {
		getImages();
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "内存卡不存在", Toast.LENGTH_SHORT).show();
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = SelectPicActivity.this
						.getContentResolver();

				// 只查询jpeg、jpg、gif、bmp和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png", "image/jpg",
								"image/gif", "image/bmp", "image/x-ms-bmp" },
						MediaStore.Images.Media.DATE_MODIFIED);

				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					if (new File(path).length() < 100) {// 如果图片大小小于100字节.不予显示
						continue;
					}
					// 获取该图片的父路径名
					String parentName = new File(path).getParentFile()
							.getName();

					// 根据父路径名将图片放入到mGruopMap中
					if (!mGruopMap.containsKey(parentName)) {
						ArrayList<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(parentName, chileList);
					} else {
						mGruopMap.get(parentName).add(path);
					}
				}
				mCursor.close();
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_OK);
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1 && data != null) {
			setResult(RESULT_OK, data);
			finish();
		} else {
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void finish() {
		super.finish();
	}

	/**
	 * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中 所以需要遍历HashMap将数据组装成List
	 * 
	 * @param mGruopMap
	 * @return
	 */
	private List<SelectPicImageBean> subGroupOfImage(
			HashMap<String, ArrayList<String>> mGruopMap) {
		if (mGruopMap.size() == 0) {
			return null;
		}
		List<SelectPicImageBean> list = new ArrayList<SelectPicImageBean>();

		Iterator<Map.Entry<String, ArrayList<String>>> it = mGruopMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<String>> entry = it.next();
			SelectPicImageBean mImageBean = new SelectPicImageBean();
			String key = entry.getKey();
			List<String> value = entry.getValue();

			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			String path = value.get(value.size() - 1);
			mImageBean.setTopImagePath(path);// 获取该组的第一张图片
			list.add(mImageBean);
		}
		return list;

	}

}
