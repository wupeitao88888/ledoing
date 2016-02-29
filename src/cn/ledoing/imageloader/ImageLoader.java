package cn.ledoing.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import cn.ledoing.activity.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.webkit.URLUtil;
import android.widget.ImageView;

public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	MemoryCache localCache = new MemoryCache();
	private ImageFileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	Executor executorService;
	/** 默认加载图片大小 */
	private int defaultWidth;
	/**
	 * 防止重复下载
	 * */
	private HashSet<String> downUrlMap;

	private Context context;
	// 滑动是否加载图片
	private boolean isScroll = false;

	public void setScroll(boolean isScroll) {
		this.isScroll = isScroll;
	}

	private ImageLoader(Context context) {
		this.context = context;
		defaultWidth = context.getResources().getDisplayMetrics().widthPixels / 4;
		executorService = Executors
				.newCachedThreadPool(new DefaultThreadFactory(4, "xiaoneng-iml"));
		downUrlMap = new HashSet<String>();
	}

	public static synchronized ImageLoader getInstance(Context context) {
		if (loader == null) {
			loader = new ImageLoader(context);
		}
		return loader;
	}

	private boolean wifiLoader = true;

	public void setWifiLoader(boolean wifiLoader) {
		this.wifiLoader = wifiLoader;
	}

	// 当进入listview时默认的图片，可换成你自己的默认图片
	final int stub_id = R.drawable.image_loading;
	private int fail = R.drawable.image_error;

	private static ImageLoader loader;

	/**
	 * 加载图片方法
	 * 
	 * @param url
	 *            网络或者本地地址
	 * @param imageView
	 * 
	 * 
	 */
	public void DisplayImage(String url, ImageView imageView) {
		DisplayImage(url, imageView, stub_id, defaultWidth, fail);
	}

	/**
	 * 
	 * @param url
	 *            图片地址
	 * @param imageView
	 * @param resId
	 *            加载完成前显示的图片
	 */
	public void DisplayImage(String url, ImageView imageView, int resId) {
		DisplayImage(url, imageView, resId, defaultWidth, fail);
	}

	/**
	 * 
	 * @param url
	 *            图片地址
	 * @param imageView
	 * @param resId
	 *            加载完成前显示的图片
	 * @param failImage
	 *            加载完成前失败显示的图片
	 */
	public void DisplayImage(String url, ImageView imageView, int resId,
			int failImage) {
		DisplayImage(url, imageView, resId, defaultWidth, failImage);
	}

	public void DisplayImage(String url, ImageView imageView, int resId,
			int failImage, ImageLoaderType type) {
		DisplayImage(url, imageView, resId, defaultWidth, failImage, type);
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 *            加载的图片地址
	 * @param imageView
	 * @param resId
	 *            加载完成前显示的图片
	 * @param requiredSize
	 *            加载的大小
	 * @param failImage
	 *            加载失败的图片
	 */
	public void DisplayImage(String url, ImageView imageView, int resId,
			int requiredSize, int failImage) {
		DisplayImage(url, imageView, resId, requiredSize, failImage,
				ImageLoaderType.Normal);
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 *            加载的图片地址
	 * @param imageView
	 * @param resId
	 *            加载完成前显示的图片
	 * @param requiredSize
	 *            加载的大小
	 * @param failImage
	 *            加载失败的图片
	 * @param type
	 *            标记图片类型
	 */
	public void DisplayImage(String url, ImageView imageView, int resId,
			int requiredSize, int failImage, ImageLoaderType type) {
		// 把图片和imageview绑定，以免造成图片错位
		imageViews.put(imageView, url);
		Bitmap bitmap = null;
		// 从内存中取出图片
		if (type != ImageLoaderType.Local) {
			bitmap = memoryCache.get(url);
		} else {
			bitmap = localCache.get(url);
		}
		// 从内存中取出，如果有，直接set然后return
		if (bitmap != null) {
			if (imageView != null) {
				imageView.setImageBitmap(bitmap);
				return;
			}
		}
		// 判断是否在滑动，如果在滑动，直接设置默认显示图，并return；
		if (isScroll) {// 滑动中不加载图片
			imageView.setImageResource(resId);
			return;
		}
		if (wifiLoader) {
			queuePhoto(url, imageView, requiredSize, failImage, type);
		}
	}

	/**
	 * 加载图片
	 * 
	 * @param url
	 * @param imageView
	 * @param failImage
	 * @param failImage2
	 */
	private void queuePhoto(String url, ImageView imageView, int requiredSize,
			int failImage, ImageLoaderType type) {
		PhotoToLoad p = new PhotoToLoad(url, imageView, requiredSize, failImage);
		executorService.execute(new PhotosLoader(p, type));
	}

	public Bitmap getBitmap(String url, int wight, int height) {
		// 默认路径
		return getBitmap(url, wight, height, ImageLoaderType.Normal);
	}

	/**
	 * Bitmap生成。本地直接生成，网络下载后生成
	 * 
	 * @param url
	 *            图片地址
	 * @param wight
	 *            要生成的bitmap 宽度
	 * @param height
	 *            要生成的bitmap 高度
	 * @return bitmap
	 */
	public Bitmap getBitmap(String url, int wight, int height,
			ImageLoaderType type) {
		Bitmap bitmap = null;
		// 判断是不是网络路径
		boolean isLoacl = URLUtil.isNetworkUrl(url);
		if (!isLoacl) {
			// 本地路径
			try {
				File f = new File(url);
				// 把流转化为Bitmap图片
				return bitmap = decodeFile(f, wight, height);
			} catch (Exception e) {
				return bitmap = null;
			}
		} else {
			fileCache = new ImageFileCache(context);
			File f = fileCache.getFile(url);
			// 先从文件缓存中查找是否有
			Bitmap b = decodeFile(f, wight, height);
			if (b != null)
				return b;
			// 最后从指定的url中下载图片
			OutputStream os = null;
			try {
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl
						.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					InputStream is = conn.getInputStream();
					os = new FileOutputStream(f);
					CopyStream(is, os);
					os.close();
					bitmap = decodeFile(f, wight, height);
				}
				return bitmap;
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			} finally {
				if (os != null) {
					try {
						os.close();
						os.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	// decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
	public static Bitmap decodeFile(File f, int reqWidth, int reqHeight) {
		BitmapFactory.Options o2 = null;
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			o.inPreferredConfig = Bitmap.Config.RGB_565;
			o.inPurgeable = true;
			o.inInputShareable = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			o2 = new BitmapFactory.Options();
			o2.inSampleSize = calculateInSampleSize(o, reqWidth, reqHeight);
			Bitmap bitmap = null;
			try {
				o2.inPreferredConfig = Bitmap.Config.RGB_565;
				o2.inPurgeable = true;
				o2.inInputShareable = true;
				bitmap = BitmapFactory.decodeStream(new FileInputStream(f),
						null, o2);
			} catch (Throwable e) {
				if (e instanceof OutOfMemoryError) {
					o2.inSampleSize *= 2;
					bitmap = BitmapFactory.decodeStream(new FileInputStream(f),
							null, o2);
				}
			}
			if (bitmap == null) {
				return null;
			}
			int degree = readPictureDegree(f.getAbsolutePath());
			if (degree != 0) {
				bitmap = rotateBitmap(bitmap, degree);
			}
			return bitmap;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	// Task for the queue
	private class PhotoToLoad {
		private int requiredSize;
		public String url;
		public ImageView imageView;
		public int failImage;

		public PhotoToLoad(String url, ImageView imageView, int requiredSize,
				int failImage) {
			this.url = url;
			this.imageView = imageView;
			this.failImage = failImage;
			this.requiredSize = requiredSize;
		}
	}

	/**
	 * 线程下载网络图片，并且展示图片
	 * 
	 * @author zhangll
	 * 
	 */
	public class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;
		ImageLoaderType type;

		PhotosLoader(PhotoToLoad photoToLoad, ImageLoaderType type) {
			this.photoToLoad = photoToLoad;
			this.type = type;
		}

		@Override
		public void run() {
			Bitmap bmp;
			if (imageViewReused(photoToLoad))
				return;
			// 这里做个控制,避免重复下载。
			if (downUrlMap.contains(photoToLoad.url)) {
				return;
			} else {
				synchronized (PhotosLoader.class) {
					if (downUrlMap.contains(photoToLoad.url)) {
						return;
					} else {
						downUrlMap.add(photoToLoad.url);
					}
				}
				bmp = getBitmap(photoToLoad.url, photoToLoad.requiredSize,
						photoToLoad.requiredSize, type);
				downUrlMap.remove(photoToLoad.url);
				if (bmp != null) {
					if (type == ImageLoaderType.Local) {
						localCache.put(photoToLoad.url, bmp);
					} else {
						memoryCache.put(photoToLoad.url, bmp);
					}
				}
			}
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			// 更新的操作放在UI线程中
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 防止图片错位
	 * 
	 * @param photoToLoad
	 * @return
	 */
	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// 用于在UI线程中更新界面
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				photoToLoad.imageView.setImageBitmap(bitmap);
			} else {
				// 下载失败设置图片损坏
				photoToLoad.imageView.setImageResource(photoToLoad.failImage);
			}
		}
	}

	// public void clearCache() {
	// memoryCache.clear();
	// fileCache.clear();
	// }

	/**
	 * 删除本地缓存，减少内存占用
	 */
	public void clearLocal() {
		localCache.clear();
	}

	/**
	 * 删除内存缓存，减少内存占用
	 */
	public void clearCache() {
		memoryCache.clear();
		localCache.clear();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		byte[] bytes = new byte[buffer_size];
		int length = 0;
		try {
			while ((length = is.read(bytes, 0, buffer_size)) != -1) {
				os.write(bytes, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算图片缩放比例
	 * 
	 * @param options
	 * @param reqWidth
	 *            宽
	 * @param reqHeight
	 *            高
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	/**
	 * 获取图片翻转度
	 * 
	 * @param path
	 *            图片地址
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 返回标准的图片（和图片翻转度一起使用）
	 * 
	 * @param bitmap
	 * @param rotate
	 * @return
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null)
			return null;
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		Bitmap createBitmap = null;
		try {
			mtx.postRotate(rotate);
			createBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		} catch (Throwable e) {
			mtx.setScale(0.5f, 0.5f);
			mtx.postRotate(rotate);
			createBitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
		}
		return createBitmap;
	}

	public MemoryCache getMemoryCache() {
		return memoryCache;
	}

	public Map<ImageView, String> getImageViews() {
		return imageViews;
	}

	private static class DefaultThreadFactory implements ThreadFactory {

		private static final AtomicInteger poolNumber = new AtomicInteger(1);

		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;
		private final int threadPriority;

		DefaultThreadFactory(int threadPriority, String threadNamePrefix) {
			this.threadPriority = threadPriority;
			SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread()
					.getThreadGroup();
			namePrefix = threadNamePrefix + poolNumber.getAndIncrement()
					+ "-thread-";
		}

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(group, r, namePrefix
					+ threadNumber.getAndIncrement(), 0);
			if (t.isDaemon())
				t.setDaemon(false);
			t.setPriority(threadPriority);
			return t;
		}
	}
}
