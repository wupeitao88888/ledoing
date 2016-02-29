package cn.ledoing.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Environment;

public class BitmapUtil {
	public static Bitmap getRoundedCornerBitmap(Bitmap bmpSrc, float rx,
			float ry) {
		if (null == bmpSrc) {
			return null;
		}

		int bmpSrcWidth = bmpSrc.getWidth();
		int bmpSrcHeight = bmpSrc.getHeight();

		Bitmap bmpDest = Bitmap.createBitmap(bmpSrcWidth, bmpSrcHeight,
				Config.ARGB_8888);
		if (null != bmpDest) {
			Canvas canvas = new Canvas(bmpDest);
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bmpSrcWidth, bmpSrcHeight);
			final RectF rectF = new RectF(rect);

			// Setting or clearing the ANTI_ALIAS_FLAG bit AntiAliasing smooth
			// out
			// the edges of what is being drawn, but is has no impact on the
			// interior of the shape.
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, rx, ry, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bmpSrc, rect, rect, paint);
		}

		return bmpDest;
	}

	public static Bitmap duplicateBitmap(Bitmap bmpSrc) {
		if (null == bmpSrc) {
			return null;
		}

		int bmpSrcWidth = bmpSrc.getWidth();
		int bmpSrcHeight = bmpSrc.getHeight();

		Bitmap bmpDest = Bitmap.createBitmap(bmpSrcWidth, bmpSrcHeight,
				Config.ARGB_8888);
		if (null != bmpDest) {
			Canvas canvas = new Canvas(bmpDest);
			final Rect rect = new Rect(0, 0, bmpSrcWidth, bmpSrcHeight);

			canvas.drawBitmap(bmpSrc, rect, rect, null);
		}

		return bmpDest;
	}

	public static Bitmap getScaleBitmap(Bitmap bitmap, float wScale,
			float hScale) {
		Matrix matrix = new Matrix();
		matrix.postScale(wScale, hScale);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);

		return bmp;
	}

	public static Bitmap getSizedBitmap(Bitmap bitmap, int dstWidth,
			int dstHeight) {
		if (null != bitmap) {
			Bitmap result = Bitmap.createScaledBitmap(bitmap, dstWidth,
					dstHeight, false);
			return result;
		}

		return null;
	}

	public static Bitmap getFullScreenBitmap(Bitmap bitmap, int wScale,
			int hScale) {
		int dstWidth = bitmap.getWidth() * wScale;
		int dstHeight = bitmap.getHeight() * hScale;
		Bitmap result = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight,
				false);
		return result;
	}

	public static Bitmap byteArrayToBitmap(byte[] array) {
		if (null == array) {
			return null;
		}

		return BitmapFactory.decodeByteArray(array, 0, array.length);
	}

	public static byte[] bitampToByteArray(Bitmap bitmap) {
		byte[] array = null;
		try {
			if (null != bitmap) {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
				array = os.toByteArray();
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return array;
	}

	public static void saveBitmapToFile(Context context, Bitmap bmp, String name) {
		if (null != context && null != bmp && null != name && name.length() > 0) {
			try {
				FileOutputStream fos = context.openFileOutput(name,
						Context.MODE_WORLD_WRITEABLE);
				bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
				fos.flush();
				fos.close();
				fos = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Bitmap loadBitmapFromFile(Context context, String name) {
		Bitmap bmp = null;

		try {
			if (null != context && null != name && name.length() > 0) {
				FileInputStream fis = context.openFileInput(name);
				bmp = BitmapFactory.decodeStream(fis);
				fis.close();
				fis = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bmp;
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (null == drawable) {
			return null;
		}

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Config config = (drawable.getOpacity() != PixelFormat.OPAQUE) ? Config.ARGB_8888
				: Config.RGB_565;

		Bitmap bitmap = Bitmap.createBitmap(width, height, config);

		if (null != bitmap) {
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, width, height);
			drawable.draw(canvas);
		}

		return bitmap;
	}

	public static void saveBitmapToSDCard(Bitmap bmp, String strPath) {
		if (null != bmp && null != strPath && !strPath.equalsIgnoreCase("")) {
			try {
				File file = new File(strPath);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = BitmapUtil.bitampToByteArray(bmp);

				fos.write(buffer);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void saveBitmapToSDCard(Drawable bmp, String strPath)
			throws Exception {
		if (null != bmp && null != strPath && !strPath.equalsIgnoreCase("")) {

			File file = new File(strPath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();

			}
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = BitmapUtil.bitampToByteArray(drawableToBitmap(bmp));
			fos.write(buffer);
			fos.close();

		}
	}

	public static Bitmap loadBitmapFromSDCard(String strPath) {
		File file = new File(strPath);
		try {
			FileInputStream fis = new FileInputStream(file);

			Bitmap bmp = BitmapFactory.decodeStream(fis);
			return bmp;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public static Drawable getDrawable(String path) throws Exception {
		return Drawable.createFromStream(new FileInputStream(new File(path)),
				"iamgeSync");
	}

	public static String saveImg(Bitmap b) throws Exception {

		String path = Environment.getExternalStorageDirectory().getPath()
				+ File.separator + "/lccache/";

		File mediaFile = new File(path + File.separator
				+ System.currentTimeMillis() + ".jpg");
		if (mediaFile.exists()) {
			mediaFile.delete();
		}
		if (!new File(path).exists()) {
			new File(path).mkdirs();
		}
		mediaFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(mediaFile);
		b.compress(Bitmap.CompressFormat.JPEG, 70, fos);
		fos.flush();
		fos.close();
		// b.recycle();
		// b = null;
		// System.gc();

		return mediaFile.getPath();
	}

	public static String getStringURL(String picurl) throws Exception {
//		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//		bitmapOptions.inSampleSize = PictureUtil.calculateInSampleSize(
//				bitmapOptions, 480, 800);
		File file = new File(picurl);
		/**
		 * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
		 */
		int degree = readPictureDegree(file.getAbsolutePath());
		Bitmap rotaingImageView = rotaingImageView(degree,
				PictureUtil.getSmallBitmap(picurl));
		String saveImg = saveImg(rotaingImageView);
		if(rotaingImageView!=null){
			rotaingImageView.recycle();
		}
		return saveImg;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
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
}
