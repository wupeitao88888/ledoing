package cn.ledoing.utils;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;

public class LCPlayUtils implements OnCompletionListener, OnErrorListener,
		OnBufferingUpdateListener, OnPreparedListener {

	String mp3url;

	public LCPlayUtils(String mp3url) {
		super();
		this.mp3url = mp3url;
		PlayMp3(mp3url);
	}

	private MediaPlayer mediaPlayer;

	public void PlayMp3(String url) {
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnErrorListener(this);
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnPreparedListener(this);
		try {
			/**
			 * ����setDataSource();��������������Ҫ���ŵ���Ƶ�ļ���HTTPλ�á�
			 */
			Log.e("LCPlayUtils", url);
			mediaPlayer.setDataSource(url);
		} catch (Exception e) {

		}
		/**
		 * ����prepareAsync�����������ں�̨��ʼ������Ƶ�ļ������ء�
		 */
		mediaPlayer.prepareAsync();
		try {
			mediaPlayer.prepare();
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}

	}

	/**
	 * ��MediaPlayer���ڻ���ʱ�������ø�Activity��onBufferingUpdate������
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		Log.i("LCPlayUtils", percent + "");
	}

	/**
	 * �����prepareAsync����ʱ��������onPrepared������������Ƶ׼�����š�
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		try {
			mediaPlayer.start();
		} catch (Exception e) {

		}
	}

	/**
	 * ��MediaPlayer��ɲ�����Ƶ�ļ�ʱ��������onCompletion������
	 * ��ʱ���á����š���ť�ɵ��������ͣ����ť�����������ʾ�����ٴβ��ţ���
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		mediaPlayer = null;
	}

	/**
	 * ���MediaPlayer���ִ��󣬽�����onError������
	 */
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			Log.i("LCPlayUtils",
					"MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK" + extra);
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			Log.i("LCPlayUtils", "MEDIA_ERROR_SERVER_DIED" + extra);
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			Log.i("LCPlayUtils", "MEDIA_ERROR_UNKNOWN");
			break;
		default:
			break;
		}
		return false;
	}

	public void Stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer = null;
		}
	}
}
