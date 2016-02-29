package cn.ledoing.activity;




import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.http.impl.io.ContentLengthInputStream;

import cn.ledoing.camera.CameraGrid;
import cn.ledoing.camera.CameraHelper;
import cn.ledoing.camera.CameraManager;
import cn.ledoing.camera.Crop;
import cn.ledoing.camera.DistanceUtil;
import cn.ledoing.camera.FileUtils;
import cn.ledoing.camera.IOUtil;
import cn.ledoing.camera.ImageUtils;
import cn.ledoing.camera.PhotoItem;
import cn.ledoing.camera.StringUtils;
import cn.ledoing.global.LCApplication;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.LCUtils;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 相机界面 Created by sky on 15/7/6.
 */
public class CameraActivity extends LCActivitySupport {

	private CameraHelper mCameraHelper;
	private Camera.Parameters parameters = null;
	private Camera cameraInst = null;
	private Bundle bundle = null;
	private int photoWidth = DistanceUtil.getCameraPhotoWidth();
	private int photoNumber = 4;
	private int photoMargin = LCApplication.getApp().dp2px(1);
	private float pointX, pointY;
	static final int FOCUS = 1; // 聚焦
	static final int ZOOM = 2; // 缩放
	private int mode; // 0是聚焦 1是放大
	private float dist;
	private int PHOTO_SIZE = 2000;
	private int mCurrentCameraId = 0; // 1是前置 0是后置
	private Handler handler = new Handler();

	CameraGrid cameraGrid;
	View takePhotoPanel;
	View takePicture;
	ImageView flashBtn;
	ImageView changeBtn;
	ImageView backBtn;
	ImageView galaryBtn;
	View focusIndex;
	SurfaceView surfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		mCameraHelper = new CameraHelper(this);

		initView();
		initEvent();
	}

	private void initView() {
		cameraGrid = (CameraGrid) findViewById(R.id.masking);
		takePhotoPanel = (View) findViewById(R.id.panel_take_photo);
		takePicture = (View) findViewById(R.id.takepicture);
		flashBtn = (ImageView) findViewById(R.id.flashBtn);
		changeBtn = (ImageView) findViewById(R.id.change);
		backBtn = (ImageView) findViewById(R.id.back);
		galaryBtn = (ImageView) findViewById(R.id.next);
		focusIndex = (View) findViewById(R.id.focus_index);
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.setKeepScreenOn(true);
		surfaceView.setFocusable(true);
		surfaceView.setBackgroundColor(TRIM_MEMORY_BACKGROUND);
		surfaceView.getHolder().addCallback(new SurfaceCallback());// 为SurfaceView的句柄添加一个回调函数

	}

	private void initEvent() {
        //拍照
        takePicture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				  try {
		                cameraInst.takePicture(null, null, new MyPictureCallback());
		            } catch (Throwable t) {
		                t.printStackTrace();
		                Toast.makeText(CameraActivity.this, "拍照失败，请重试！",  Toast.LENGTH_LONG).show();
		                try {
		                    cameraInst.startPreview();
		                } catch (Throwable e) {

		                }
		            }
			}
        }

        );
        //闪光灯
        flashBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				turnLight(cameraInst); 
			}
		});
        //前后置摄像头切换
        boolean canSwitch = false;
        try {
            canSwitch = mCameraHelper.hasFrontCamera() && mCameraHelper.hasBackCamera();
        } catch (Exception e) {
            //获取相机信息失败
        }
        if (!canSwitch) {
            changeBtn.setVisibility(View.GONE);
        } else {
            changeBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switchCamera();
				}
			});
        }
        //跳转相册
//        galaryBtn.setOnClickListener(v -> startActivity(new Intent(CameraActivity.this, AlbumActivity.class)));
        //返回按钮
        backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        surfaceView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				 switch (event.getAction() & MotionEvent.ACTION_MASK) {
	                // 主点按下
	                case MotionEvent.ACTION_DOWN:
	                    pointX = event.getX();
	                    pointY = event.getY();
	                    mode = FOCUS;
	                    break;
	                // 副点按下
	                case MotionEvent.ACTION_POINTER_DOWN:
	                    dist = spacing(event);
	                    // 如果连续两点距离大于10，则判定为多点模式
	                    if (spacing(event) > 10f) {
	                        mode = ZOOM;
	                    }
	                    break;
	                case MotionEvent.ACTION_UP:
	                case MotionEvent.ACTION_POINTER_UP:
	                    mode = FOCUS;
	                    break;
	                case MotionEvent.ACTION_MOVE:
	                    if (mode == FOCUS) {
	                        //pointFocus((int) event.getRawX(), (int) event.getRawY());
	                    } else if (mode == ZOOM) {
	                        float newDist = spacing(event);
	                        if (newDist > 10f) {
	                            float tScale = (newDist - dist) / dist;
	                            if (tScale < 0) {
	                                tScale = tScale * 10;
	                            }
	                            addZoomIn((int) tScale);
	                        }
	                    }
	                    break;
	            }
	            return false;
			}
		});


        surfaceView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
	                pointFocus((int) pointX, (int) pointY);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	            RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(focusIndex.getLayoutParams());
	            layout.setMargins((int) pointX - 60, (int) pointY - 60, 0, 0);
	            focusIndex.setLayoutParams(layout);
	            focusIndex.setVisibility(View.VISIBLE);
	            ScaleAnimation sa = new ScaleAnimation(3f, 1f, 3f, 1f,
	                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
	            sa.setDuration(800);
	            focusIndex.startAnimation(sa);
	            handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						 focusIndex.setVisibility(View.INVISIBLE);
					}
				}, 800);
			}
		});

        takePhotoPanel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//doNothing 防止聚焦框出现在拍照区域
			}
		});

    }

	@Override
	protected void onActivityResult(final int requestCode,
			final int resultCode, final Intent result) {
		if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
			CameraManager.getInst().processPhotoItem(
				CameraActivity.this,
					new PhotoItem(result.getData().getPath(), System
							.currentTimeMillis()));
		} else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
//			Intent newIntent = new Intent(this, PhotoProcessActivity.class);
//			newIntent.setData(result.getData());
//			startActivity(newIntent);
		}
	}

	/**
	 * 两点的距离
	 */
	private float spacing(MotionEvent event) {
		if (event == null) {
			return 0;
		}
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 放大缩小
	int curZoomValue = 0;

	private void addZoomIn(int delta) {

		try {
			Camera.Parameters params = cameraInst.getParameters();
			Log.d("Camera", "Is support Zoom " + params.isZoomSupported());
			if (!params.isZoomSupported()) {
				return;
			}
			curZoomValue += delta;
			if (curZoomValue < 0) {
				curZoomValue = 0;
			} else if (curZoomValue > params.getMaxZoom()) {
				curZoomValue = params.getMaxZoom();
			}

			if (!params.isSmoothZoomSupported()) {
				params.setZoom(curZoomValue);
				cameraInst.setParameters(params);
				return;
			} else {
				cameraInst.startSmoothZoom(curZoomValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 定点对焦的代码
	private void pointFocus(int x, int y) {
		cameraInst.cancelAutoFocus();
		parameters = cameraInst.getParameters();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			showPoint(x, y);
		}
		cameraInst.setParameters(parameters);
		autoFocus();
	}


	private void showPoint(int x, int y) {
		if (parameters.getMaxNumMeteringAreas() > 0) {
			List<Camera.Area> areas = new ArrayList<Camera.Area>();
			// xy变换了
			int rectY = -x * 2000 / LCApplication.getApp().getScreenWidth() + 1000;
			int rectX = y * 2000 / LCApplication.getApp().getScreenHeight() - 1000;

			int left = rectX < -900 ? -1000 : rectX - 100;
			int top = rectY < -900 ? -1000 : rectY - 100;
			int right = rectX > 900 ? 1000 : rectX + 100;
			int bottom = rectY > 900 ? 1000 : rectY + 100;
			Rect area1 = new Rect(left, top, right, bottom);
			areas.add(new Camera.Area(area1, 800));
			parameters.setMeteringAreas(areas);
		}

		parameters
				.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
	}

	private final class MyPictureCallback implements Camera.PictureCallback {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			bundle = new Bundle();
			bundle.putByteArray("bytes", data); // 将图片字节数据保存在bundle当中，实现数据交换
			new SavePicTask(data).execute();
			camera.startPreview(); // 拍完照后，重新开始预览
		}
	}

	private class SavePicTask extends AsyncTask<Void, Void, String> {
		private byte[] data;

		protected void onPreExecute() {
//			showProgressDialog("处理中");
			LCUtils.startProgressDialog(context);
		}


		SavePicTask(byte[] data) {
			this.data = data;
		}

		@Override
		protected String doInBackground(Void... params) {
			try {
				return saveToSDCard(data);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (StringUtils.isNotEmpty(result)) {
//				dismissProgressDialog();
				LCUtils.stopProgressDialog(context);
				CameraManager.getInst().processPhotoItem(CameraActivity.this,
						new PhotoItem(result, System.currentTimeMillis()));
//				galaryBtn.
				LCUtils.mImageloader(result,galaryBtn,context);

			} else {
				AbToastUtil.showToast(context,"拍照失败，请稍后重试！");
			}
		}
	}

	/* SurfaceCallback */
	private final class SurfaceCallback implements SurfaceHolder.Callback {

		public void surfaceDestroyed(SurfaceHolder holder) {
			try {
				if (cameraInst != null) {
					cameraInst.stopPreview();
					cameraInst.release();
					cameraInst = null;
				}
			} catch (Exception e) {
				// 相机已经关了
				AbToastUtil.showToast(context,"相机已经关了");
			}

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if (null == cameraInst) {
				try {
					cameraInst = Camera.open();
					cameraInst.setPreviewDisplay(holder);
					initCamera();
					cameraInst.startPreview();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			autoFocus();
		}
	}

	// 实现自动对焦
	private void autoFocus() {
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (cameraInst == null) {
					return;
				}
				cameraInst.autoFocus(new Camera.AutoFocusCallback() {
					@Override
					public void onAutoFocus(boolean success, Camera camera) {
						if (success) {
							initCamera();// 实现相机的参数初始化
						}
					}
				});
			}
		};
	}

	private Camera.Size adapterSize = null;
	private Camera.Size previewSize = null;

	private void initCamera() {
		parameters = cameraInst.getParameters();
		parameters.setPictureFormat(PixelFormat.JPEG);
		// if (adapterSize == null) {
		setUpPicSize(parameters);
		setUpPreviewSize(parameters);
		// }
		if (adapterSize != null) {
			parameters.setPictureSize(adapterSize.width, adapterSize.height);
		}
		if (previewSize != null) {
			parameters.setPreviewSize(previewSize.width, previewSize.height);
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			parameters
					.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 1连续对焦
		} else {
			parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		}
		setDispaly(parameters, cameraInst);
		try {
			cameraInst.setParameters(parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cameraInst.startPreview();
		cameraInst.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
	}

	private void setUpPicSize(Camera.Parameters parameters) {

		if (adapterSize != null) {
			return;
		} else {
			adapterSize = findBestPictureResolution();
			return;
		}
	}

	private void setUpPreviewSize(Camera.Parameters parameters) {

		if (previewSize != null) {
			return;
		} else {
			previewSize = findBestPreviewResolution();
		}
	}

	/**
	 * 最小预览界面的分辨率
	 */
	private static final int MIN_PREVIEW_PIXELS = 480 * 320;
	/**
	 * 最大宽高比差
	 */
	private static final double MAX_ASPECT_DISTORTION = 0.15;
	private static final String TAG = "Camera";

	/**
	 * 找出最适合的预览界面分辨率
	 *
	 * @return
	 */
	private Camera.Size findBestPreviewResolution() {
		Camera.Parameters cameraParameters = cameraInst.getParameters();
		Camera.Size defaultPreviewResolution = cameraParameters
				.getPreviewSize();

		List<Camera.Size> rawSupportedSizes = cameraParameters
				.getSupportedPreviewSizes();
		if (rawSupportedSizes == null) {
			return defaultPreviewResolution;
		}

		// 按照分辨率从大到小排序
		List<Camera.Size> supportedPreviewResolutions = new ArrayList<Camera.Size>(
				rawSupportedSizes);
		Collections.sort(supportedPreviewResolutions,
				new Comparator<Camera.Size>() {
					@Override
					public int compare(Camera.Size a, Camera.Size b) {
						int aPixels = a.height * a.width;
						int bPixels = b.height * b.width;
						if (bPixels < aPixels) {
							return -1;
						}
						if (bPixels > aPixels) {
							return 1;
						}
						return 0;
					}
				});

		StringBuilder previewResolutionSb = new StringBuilder();
		for (Camera.Size supportedPreviewResolution : supportedPreviewResolutions) {
			previewResolutionSb.append(supportedPreviewResolution.width)
					.append('x').append(supportedPreviewResolution.height)
					.append(' ');
		}
		Log.v(TAG, "Supported preview resolutions: " + previewResolutionSb);

		// 移除不符合条件的分辨率
		double screenAspectRatio = (double) LCApplication.getApp().getScreenWidth()
				/ (double) LCApplication.getApp().getScreenHeight();
		Iterator<Camera.Size> it = supportedPreviewResolutions.iterator();
		while (it.hasNext()) {
			Camera.Size supportedPreviewResolution = it.next();
			int width = supportedPreviewResolution.width;
			int height = supportedPreviewResolution.height;

			// 移除低于下限的分辨率，尽可能取高分辨率
			if (width * height < MIN_PREVIEW_PIXELS) {
				it.remove();
				continue;
			}

			// 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
			// 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
			// 因此这里要先交换然preview宽高比后在比较
			boolean isCandidatePortrait = width > height;
			int maybeFlippedWidth = isCandidatePortrait ? height : width;
			int maybeFlippedHeight = isCandidatePortrait ? width : height;
			double aspectRatio = (double) maybeFlippedWidth
					/ (double) maybeFlippedHeight;
			double distortion = Math.abs(aspectRatio - screenAspectRatio);
			if (distortion > MAX_ASPECT_DISTORTION) {
				it.remove();
				continue;
			}

			// 找到与屏幕分辨率完全匹配的预览界面分辨率直接返回
			if (maybeFlippedWidth == LCApplication.getApp().getScreenWidth()
					&& maybeFlippedHeight == LCApplication.getApp().getScreenHeight()) {
				return supportedPreviewResolution;
			}
		}

		// 如果没有找到合适的，并且还有候选的像素，则设置其中最大比例的，对于配置比较低的机器不太合适
		if (!supportedPreviewResolutions.isEmpty()) {
			Camera.Size largestPreview = supportedPreviewResolutions.get(0);
			return largestPreview;
		}

		// 没有找到合适的，就返回默认的

		return defaultPreviewResolution;
	}

	private Camera.Size findBestPictureResolution() {
		Camera.Parameters cameraParameters = cameraInst.getParameters();
		List<Camera.Size> supportedPicResolutions = cameraParameters
				.getSupportedPictureSizes(); // 至少会返回一个值

		StringBuilder picResolutionSb = new StringBuilder();
		for (Camera.Size supportedPicResolution : supportedPicResolutions) {
			picResolutionSb.append(supportedPicResolution.width).append('x')
					.append(supportedPicResolution.height).append(" ");
		}
		Log.d(TAG, "Supported picture resolutions: " + picResolutionSb);

		Camera.Size defaultPictureResolution = cameraParameters
				.getPictureSize();
		Log.d(TAG, "default picture resolution "
				+ defaultPictureResolution.width + "x"
				+ defaultPictureResolution.height);

		// 排序
		List<Camera.Size> sortedSupportedPicResolutions = new ArrayList<Camera.Size>(
				supportedPicResolutions);
		Collections.sort(sortedSupportedPicResolutions,
				new Comparator<Camera.Size>() {
					@Override
					public int compare(Camera.Size a, Camera.Size b) {
						int aPixels = a.height * a.width;
						int bPixels = b.height * b.width;
						if (bPixels < aPixels) {
							return -1;
						}
						if (bPixels > aPixels) {
							return 1;
						}
						return 0;
					}
				});

		// 移除不符合条件的分辨率
		double screenAspectRatio = (double) LCApplication.getApp().getScreenWidth()
				/ (double) LCApplication.getApp().getScreenHeight();
		Iterator<Camera.Size> it = sortedSupportedPicResolutions.iterator();
		while (it.hasNext()) {
			Camera.Size supportedPreviewResolution = it.next();
			int width = supportedPreviewResolution.width;
			int height = supportedPreviewResolution.height;

			// 在camera分辨率与屏幕分辨率宽高比不相等的情况下，找出差距最小的一组分辨率
			// 由于camera的分辨率是width>height，我们设置的portrait模式中，width<height
			// 因此这里要先交换然后在比较宽高比
			boolean isCandidatePortrait = width > height;
			int maybeFlippedWidth = isCandidatePortrait ? height : width;
			int maybeFlippedHeight = isCandidatePortrait ? width : height;
			double aspectRatio = (double) maybeFlippedWidth
					/ (double) maybeFlippedHeight;
			double distortion = Math.abs(aspectRatio - screenAspectRatio);
			if (distortion > MAX_ASPECT_DISTORTION) {
				it.remove();
				continue;
			}
		}

		// 如果没有找到合适的，并且还有候选的像素，对于照片，则取其中最大比例的，而不是选择与屏幕分辨率相同的
		if (!sortedSupportedPicResolutions.isEmpty()) {
			return sortedSupportedPicResolutions.get(0);
		}

		// 没有找到合适的，就返回默认的
		return defaultPictureResolution;
	}

	// 控制图像的正确显示方向
	private void setDispaly(Camera.Parameters parameters, Camera camera) {
		if (Build.VERSION.SDK_INT >= 8) {
			setDisplayOrientation(camera, 90);
		} else {
			parameters.setRotation(90);
		}
	}

	// 实现的图像的正确显示
	private void setDisplayOrientation(Camera camera, int i) {
		Method downPolymorphic;
		try {
			downPolymorphic = camera.getClass().getMethod(
					"setDisplayOrientation", new Class[] { int.class });
			if (downPolymorphic != null) {
				downPolymorphic.invoke(camera, new Object[] { i });
			}
		} catch (Exception e) {
			Log.e("Came_e", "图像出错");
		}
	}

	/**
	 * 将拍下来的照片存放在SD卡中
	 *
	 * @param data
	 * @throws IOException
	 */
	public String saveToSDCard(byte[] data) throws IOException {
		Bitmap croppedImage;

		// 获得图片大小
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		PHOTO_SIZE = options.outHeight > options.outWidth ? options.outWidth
				: options.outHeight;
		int height = options.outHeight > options.outWidth ? options.outHeight
				: options.outWidth;
		options.inJustDecodeBounds = false;
		Rect r;
		if (mCurrentCameraId == 1) {
			r = new Rect(height - PHOTO_SIZE, 0, height, PHOTO_SIZE);
		} else {
			r = new Rect(0, 0, PHOTO_SIZE, PHOTO_SIZE);
		}
		try {
			croppedImage = decodeRegionCrop(data, r);
		} catch (Exception e) {
			return null;
		}
		String imagePath = ImageUtils.saveToFile(FileUtils.getInst()
				.getSystemPhotoPath(), true, croppedImage);
		croppedImage.recycle();
		return imagePath;
	}

	private Bitmap decodeRegionCrop(byte[] data, Rect rect) {

		InputStream is = null;
		System.gc();
		Bitmap croppedImage = null;
		try {
			is = new ByteArrayInputStream(data);
			BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is,
					false);

			try {
				croppedImage = decoder.decodeRegion(rect,
						new BitmapFactory.Options());
			} catch (IllegalArgumentException e) {
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeStream(is);
		}
		Matrix m = new Matrix();
		m.setRotate(90, PHOTO_SIZE / 2, PHOTO_SIZE / 2);
		if (mCurrentCameraId == 1) {
			m.postScale(1, -1);
		}
		Bitmap rotatedImage = Bitmap.createBitmap(croppedImage, 0, 0,
				PHOTO_SIZE, PHOTO_SIZE, m, true);
		if (rotatedImage != croppedImage)
			croppedImage.recycle();
		return rotatedImage;
	}

	/**
	 * 闪光灯开关 开->关->自动
	 *
	 * @param mCamera
	 */
	private void turnLight(Camera mCamera) {
		if (mCamera == null || mCamera.getParameters() == null
				|| mCamera.getParameters().getSupportedFlashModes() == null) {
			return;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		String flashMode = mCamera.getParameters().getFlashMode();
		List<String> supportedModes = mCamera.getParameters()
				.getSupportedFlashModes();
		if (Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)
				&& supportedModes.contains(Camera.Parameters.FLASH_MODE_ON)) {// 关闭状态
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			mCamera.setParameters(parameters);
			flashBtn.setImageResource(R.drawable.camera_flash_on);
		} else if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {// 开启状态
			if (supportedModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				flashBtn.setImageResource(R.drawable.camera_flash_auto);
				mCamera.setParameters(parameters);
			} else if (supportedModes
					.contains(Camera.Parameters.FLASH_MODE_OFF)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				flashBtn.setImageResource(R.drawable.camera_flash_off);
				mCamera.setParameters(parameters);
			}
		} else if (Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)
				&& supportedModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
			parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(parameters);
			flashBtn.setImageResource(R.drawable.camera_flash_off);
		}
	}

	// 切换前后置摄像头
	private void switchCamera() {
		mCurrentCameraId = (mCurrentCameraId + 1)
				% mCameraHelper.getNumberOfCameras();
		releaseCamera();
		Log.d("DDDD", "DDDD----mCurrentCameraId" + mCurrentCameraId);
		setUpCamera(mCurrentCameraId);
	}

	private void releaseCamera() {
		if (cameraInst != null) {
			cameraInst.setPreviewCallback(null);
			cameraInst.release();
			cameraInst = null;
		}
		adapterSize = null;
		previewSize = null;
	}

	/**
	 * @param mCurrentCameraId2
	 */
	private void setUpCamera(int mCurrentCameraId2) {
		cameraInst = getCameraInstance(mCurrentCameraId2);
		if (cameraInst != null) {
			try {
				cameraInst.setPreviewDisplay(surfaceView.getHolder());
				initCamera();
				cameraInst.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			 Toast.makeText(CameraActivity.this, "切换失败，请重试！",  Toast.LENGTH_LONG).show();
		}
	}

	private Camera getCameraInstance(final int id) {
		Camera c = null;
		try {
			c = mCameraHelper.openCamera(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
}
