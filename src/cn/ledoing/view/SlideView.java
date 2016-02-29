package cn.ledoing.view;





import cn.ledoing.activity.R;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * @author Taolin
 * @date Dec 03, 2013
 * @since v1.0
 */

public class SlideView extends View {

	public interface SlideListener {
		void onDone();
	}

	public Context context;
	private static final int MSG_REDRAW = 1;
	private static final int DRAW_INTERVAL = 50;
	private static final int STEP_LENGTH = 5;

	private Paint mPaint;
	private VelocityTracker mVelocityTracker;
	private int mMaxVelocity;
	private LinearGradient mGradient;
	private int[] mGradientColors;
	private int mGradientIndex;
	private Interpolator mInterpolator;
	private SlideListener mSlideListener;
	private float mDensity;
	private Matrix mMatrix;
	private ValueAnimator mValueAnimator;

	private String mText;
	private int mTextSize;
	private int mTextLeft;
	private int mTextTop;

	private int mSlider;
	private Bitmap mSliderBitmap;
	private Bitmap endbutton;
	private int mSliderLeft;
	private int mSliderTop;
	private Rect mSliderRect;
	private int mSlidableLength; // SlidableLength = BackgroundWidth -
									// LeftMagins - RightMagins - SliderWidth
	private int mEffectiveLength; // Suggested length is 20pixels shorter than
									// SlidableLength
	private float mEffectiveVelocity;

	private float mStartX;
	private float mStartY;
	private float mLastY;
	private float mLastX;
	private float mMoveY;
	private boolean isVisibilty = true;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_REDRAW:
				mMatrix.setTranslate(mGradientIndex, 0);
				mGradient.setLocalMatrix(mMatrix);
				invalidate();
				mGradientIndex += STEP_LENGTH * mDensity;
				if (mGradientIndex > mSlidableLength) {
					mGradientIndex = 0;
				}
				mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
				break;
			}
		}
	};

	public SlideView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		ViewConfiguration configuration = ViewConfiguration.get(context);
		mMaxVelocity = configuration.getScaledMaximumFlingVelocity();
		mInterpolator = new AccelerateDecelerateInterpolator();
		mDensity = getResources().getDisplayMetrics().density;
		setClickable(true);
		setFocusable(true);
		setFocusableInTouchMode(true);

		TypedArray typeArray = context.obtainStyledAttributes(attrs,
				R.styleable.SlideView);
		mText = typeArray.getString(R.styleable.SlideView_maskText);
		mTextSize = typeArray.getDimensionPixelSize(
				R.styleable.SlideView_maskTextSize, R.dimen.mask_text_size);
		mTextLeft = typeArray.getDimensionPixelSize(
				R.styleable.SlideView_maskTextMarginLeft,
				R.dimen.mask_text_margin_left);
		mTextTop = typeArray.getDimensionPixelSize(
				R.styleable.SlideView_maskTextMarginTop,
				R.dimen.mask_text_margin_top);

		mSlider = typeArray.getResourceId(R.styleable.SlideView_slider,
				R.drawable.ic_launcher);
		mSliderLeft = typeArray.getDimensionPixelSize(
				R.styleable.SlideView_sliderMarginLeft,
				R.dimen.slider_margin_left);
		mSliderTop = typeArray.getDimensionPixelSize(
				R.styleable.SlideView_sliderMarginTop,
				R.dimen.slider_margin_top);
		mSliderBitmap = BitmapFactory.decodeResource(getResources(), mSlider);
		mSliderRect = new Rect(mSliderLeft, mSliderTop, mSliderLeft
				+ mSliderBitmap.getWidth(), mSliderTop
				+ mSliderBitmap.getHeight());

		mSlidableLength = typeArray.getDimensionPixelSize(
				R.styleable.SlideView_slidableLength, R.dimen.slidable_length);
		mEffectiveLength = typeArray
				.getDimensionPixelSize(R.styleable.SlideView_effectiveLength,
						R.dimen.effective_length);
		mEffectiveVelocity = typeArray.getDimensionPixelSize(
				R.styleable.SlideView_effectiveVelocity,
				R.dimen.effective_velocity);

		typeArray.recycle();

		mGradientColors = new int[] { Color.argb(255, 120, 120, 120),
				Color.argb(255, 120, 120, 120), Color.argb(255, 255, 255, 255) };
		mGradient = new LinearGradient(0, 0, 100 * mDensity, 0,
				mGradientColors, new float[] { 0, 0.7f, 1 }, TileMode.MIRROR);
		mGradientIndex = 0;
		mPaint = new Paint();
		mMatrix = new Matrix();
		mPaint.setTextSize(mTextSize);
		mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
	}

	public void setSlideListener(SlideListener slideListener) {
		mSlideListener = slideListener;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setShader(mGradient);
		// canvas.drawText(mText, mTextLeft, mTextTop, mPaint);
		// canvas.drawBitmap(mSliderBitmap, mTextLeft, mTextTop + mMoveY, null);
		// Slider's moving rely on the mMoveX.
		Resources res = getResources();

		// canvas.drawBitmap(BitmapFactory.decodeResource(res,
		// R.drawable.xuxian),
		// 8, mSlidableLength, null);
		// canvas.drawBitmap(
		// BitmapFactory.decodeResource(res, R.drawable.circlexu),
		// mSliderTop, mSlidableLength, null);

		// canvas.drawBitmap(
		// BitmapFactory.decodeResource(res, R.drawable.circlexu),
		// mSliderTop, mSlidableLength, null);
		drawImage(canvas,
				BitmapFactory.decodeResource(res, R.drawable.add),
				dip2px(0), dip2px(101), dip2px(72), dip2px(72), dip2px(0),
				dip2px(0));
		float f = mSliderLeft + mMoveY;

		drawImage(canvas, mSliderBitmap, mSliderTop, (int) f, dip2px(74),
				dip2px(74), dip2px(0), dip2px(0));
		if (isVisibilty) {
			drawImage(canvas,
					BitmapFactory.decodeResource(res, R.drawable.xuxian),
					dip2px(37), dip2px(74), dip2px(1), dip2px(25), dip2px(0),
					dip2px(0));
		}
	}

	/*---------------------------------  
	 * 绘制图片  
	 * @param       x屏幕上的x坐标  
	 * @param       y屏幕上的y坐标  
	 * @param       w要绘制的图片的宽度  
	 * @param       h要绘制的图片的高度  
	 * @param       bx图片上的x坐标  
	 * @param       by图片上的y坐标  
	 *   
	 * @return      null  
	 ------------------------------------*/

	public static void drawImage(Canvas canvas, Bitmap blt, int x, int y,
			int w, int h, int bx, int by) {
		Rect src = new Rect();// 图片 >>原矩形
		Rect dst = new Rect();// 屏幕 >>目标矩形

		src.left = bx;
		src.top = by;
		src.right = bx + w;
		src.bottom = by + h;

		dst.left = x;
		dst.top = y;
		dst.right = x + w;
		dst.bottom = y + h;
		// 画出指定的位图，位图将自动--》缩放/自动转换，以填补目标矩形
		// 这个方法的意思就像 将一个位图按照需求重画一遍，画后的位图就是我们需要的了
		canvas.drawBitmap(blt, null, dst, null);
		src = null;
		dst = null;
	}

	/**
	 * 绘制一个Bitmap
	 * 
	 * @param canvas
	 *            画布
	 * @param bitmap
	 *            图片
	 * @param x
	 *            屏幕上的x坐标
	 * @param y
	 *            屏幕上的y坐标
	 */

	public static void drawImage(Canvas canvas, Bitmap bitmap, int x, int y) {
		// 绘制图像 将bitmap对象显示在坐标 x,y上
		canvas.drawBitmap(bitmap, x, y, null);
	}

	public void reset() {
		if (mValueAnimator != null) {
			mValueAnimator.cancel();
		}
		mMoveY = 0;
		mPaint.setAlpha(255);
		mHandler.removeMessages(MSG_REDRAW);
		mHandler.sendEmptyMessage(MSG_REDRAW);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public int dip2px(float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public int px2dip(float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// If the start point is not on the slider, moving slider will not be
		// executed.
		if (event.getAction() != MotionEvent.ACTION_DOWN
				&& !mSliderRect.contains((int) mStartX, (int) mStartY)) {
			if (event.getAction() == MotionEvent.ACTION_UP
					|| event.getAction() == MotionEvent.ACTION_CANCEL) {
				mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
			}
			return super.onTouchEvent(event);
		}
		acquireVelocityTrackerAndAddMovement(event);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mStartX = event.getX();
			mStartY = event.getY();
			mLastY = mStartY;
			mHandler.removeMessages(MSG_REDRAW);
			isVisibilty = true;
			break;
		case MotionEvent.ACTION_MOVE:
			mLastY = event.getY();
			if (mLastY > mStartY) { // Can not exceed the left boundary,
									// otherwise, mMoveX will get a minimum
									// value.
				// The transparency of text will be changed along with moving
				// slider
				// int alpha = (int) (255 - (mLastY - mStartY) * 3 / mDensity);
				// if (alpha > 1) {
				// mPaint.setAlpha(alpha);
				// } else {
				// mPaint.setAlpha(0);
				// }
				// Can not exceed the right boundary, otherwise, mMoveX will get
				// a maximum value.

				if (mLastY - mStartY >= mSlidableLength - 5) {
					mLastY = mStartY + mSlidableLength;
					mMoveY = mSlidableLength - 12;

				} else {
					mMoveY = (int) (mLastY - mStartY);
				}
			} else {
				mLastY = mStartY;
				mMoveY = 0;
			}
			invalidate();
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
			float velocityX = mVelocityTracker.getXVelocity();
			if (mLastY - mStartY >= mEffectiveLength
					|| velocityX >= mEffectiveVelocity) {
				startAnimator(mLastY - mStartY, mSlidableLength, velocityX,
						true);
			} else {
				startAnimator(mLastY - mStartY, 0, velocityX, false);
				mHandler.sendEmptyMessageDelayed(MSG_REDRAW, DRAW_INTERVAL);
			}
			releaseVelocityTracker();
			break;
		}
		return super.onTouchEvent(event);
	}

	private void startAnimator(float start, float end, float velocity,
			boolean isRightMoving) {
		if (velocity < mEffectiveVelocity) {
			velocity = mEffectiveVelocity;
		}
		int duration = (int) (Math.abs(end - start) * 1000 / velocity);
		mValueAnimator = ValueAnimator.ofFloat(start, end);
		mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				mMoveY = (Float) animation.getAnimatedValue();
				// int alpha = (int) (255 - (mMoveY) * 3 / mDensity);
				// if (alpha > 1) {
				// mPaint.setAlpha(alpha);
				// } else {
				// mPaint.setAlpha(0);
				// }
				invalidate();
			}
		});
		mValueAnimator.setDuration(duration);
		mValueAnimator.setInterpolator(mInterpolator);
		if (isRightMoving) {
			mValueAnimator.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					if (mSlideListener != null) {
						mSlideListener.onDone();
						isVisibilty = false;
					}
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}
			});
		}
		mValueAnimator.start();
	}

	private void acquireVelocityTrackerAndAddMovement(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
	}

	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(dip2px(75), dip2px(180));
		// setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

}
