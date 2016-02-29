package cn.ledoing.imageloader;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class ImageFileCache {

	private File cacheDir;

	public ImageFileCache(Context context) {
		// 如果有SD卡则在SD卡中建一个目录存放缓存的图片
		// 没有SD卡就放在系统的缓存目录中
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/lccache/.cache");
		} else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	/**
	 * 将Url地址转换成hashCode
	 * 
	 * @param url
	 * @return
	 */
	public File getFile(String url) {
		// 将url的hashCode作为缓存的文件名
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);
		return f;
	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}
