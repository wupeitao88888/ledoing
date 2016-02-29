package cn.ledoing.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ledoing.bean.FindList;
import cn.ledoing.bean.IsPraise;
import cn.ledoing.bean.Praise;
import cn.ledoing.bean.TeamShow;
import cn.ledoing.db.Dbhelper;
import cn.ledoing.db.LCDbHelper;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class LCFindInfo extends LCActivitySupport implements OnClickListener {
	FindList findList;

	private AbHttpUtil mAbHttpUtil = null;
	private LCTitleBar lc_findinfo_title, lc_find_title;
	public ImageView lc_smallshow;// 拼图小图
	public ImageView show_image;// 显示大图
	public ImageView lc_bigshow;// 拼图大图
	private Animator mCurrentAnimator;

	private int mShortAnimationDuration = 300;

	private Context mContext;
	LayoutInflater inflater;

	// References to our images in res > drawable
	public int[] mThumbIds = { R.drawable.ic_launcher };
	float startScale;
	RelativeLayout viewPager;
	Rect startBounds;
	float startScaleFinal;
	public int SELECT = 1;
	public LinearLayout lc_big_imageone, lc_big_imagetwo, lc_big_imagethree,
			lc_big_imagefour, lc_big_imagefive;
	TeamShow teamShow;
	private TextView lc_findinfo_author, lc_findinfo_name, lc_find_name,
			lc_find_praisecount;
	private RelativeLayout lc_findinfo_praiseall, lc_findinfo_clickpraise;
	private LCNoNetWork lc_findinfo_nonet;
	private FrameLayout container;
    private Dbhelper dbhelper;
    private int position;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lc_findinfo);
		Intent intent = getIntent();
		findList = (FindList) intent.getSerializableExtra("findlist");
		SELECT = intent.getIntExtra("select", 1);
        position = intent.getIntExtra("position", 0);
		initView();
		neiInit();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (teamShow != null) {
			zoo();
			switch (v.getId()) {
			case R.id.lc_big_imageone:
				showImage(1, teamShow);
				break;
			case R.id.lc_big_imagetwo:
				showImage(2, teamShow);
				break;
			case R.id.lc_big_imagethree:
				showImage(3, teamShow);
				break;
			case R.id.lc_big_imagefour:
				showImage(4, teamShow);
				break;
			case R.id.lc_big_imagefive:
				showImage(5, teamShow);
				break;
			}
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
        LCDbHelper lcDbHelper = new LCDbHelper(context);
        dbhelper = new Dbhelper(lcDbHelper);
		lc_findinfo_title = (LCTitleBar) findViewById(R.id.lc_findinfo_title);
		lc_findinfo_title.setCenterTitle("作品详情");
		lc_find_title = (LCTitleBar) findViewById(R.id.lc_find_title);
		lc_find_title.setCenterTitle("点赞");
		lc_smallshow = (ImageView) findViewById(R.id.lc_smallshow);
		lc_findinfo_nonet = (LCNoNetWork) findViewById(R.id.lc_findinfo_nonet);
		container = (FrameLayout) findViewById(R.id.container);
		lc_findinfo_nonet.setRetryOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                neiInit();
                isNoNet();
            }
        });
		show_image = (ImageView) findViewById(R.id.show_image);
		lc_bigshow = (ImageView) findViewById(R.id.lc_bigshow);
		lc_big_imageone = (LinearLayout) findViewById(R.id.lc_big_imageone);
		lc_big_imagetwo = (LinearLayout) findViewById(R.id.lc_big_imagetwo);
		lc_big_imagethree = (LinearLayout) findViewById(R.id.lc_big_imagethree);
		lc_big_imagefour = (LinearLayout) findViewById(R.id.lc_big_imagefour);
		lc_big_imagefive = (LinearLayout) findViewById(R.id.lc_big_imagefive);
		lc_findinfo_author = (TextView) findViewById(R.id.lc_findinfo_author);
		lc_findinfo_name = (TextView) findViewById(R.id.lc_findinfo_name);
		lc_find_name = (TextView) findViewById(R.id.lc_find_name);
		lc_find_praisecount = (TextView) findViewById(R.id.lc_find_praisecount);
		lc_findinfo_praiseall = (RelativeLayout) findViewById(R.id.lc_findinfo_praiseall);
		lc_findinfo_clickpraise = (RelativeLayout) findViewById(R.id.lc_findinfo_clickpraise);
		lc_findinfo_clickpraise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (LCConstant.islogin) {
                    if(!dbhelper.checkedPraise(new IsPraise("1",findList.getGroupsid(),LCConstant.userinfo.getUserid()))){
					clickPraise(findList);
                }else{
                    showToast("点过赞了！");
                }
				} else {
					Intent intent = new Intent(context,
							LCUserLoginAndRegister.class);
					context.startActivity(intent);
				}
			}
		});
		lc_big_imageone.setOnClickListener(this);
		lc_big_imagetwo.setOnClickListener(this);
		lc_big_imagethree.setOnClickListener(this);
		lc_big_imagefour.setOnClickListener(this);
		lc_big_imagefive.setOnClickListener(this);

		// gridView.setAdapter(new ImageAdapter(this));
		viewPager = (RelativeLayout) findViewById(R.id.expanded_image);

		lc_smallshow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (viewPager.getVisibility() == View.INVISIBLE) {
					zoomImageFromThumb(lc_smallshow, 0);
				}
				if (viewPager.getVisibility() == View.GONE) {
					zoomImageFromThumb(lc_smallshow, 0);
				}
			}
		});
		isNoNet();
	}

	private void isNoNet() {
		// TODO Auto-generated method stub
		if (LCUtils.isNetworkAvailable(this)) {
			setNotNetBack();
		} else {
			setNotNet();
		}
	}

	public void setNotNetBack() {
		lc_findinfo_nonet.setVisibility(View.GONE);
		container.setVisibility(View.VISIBLE);
	}

	public void setNotNet() {
		lc_findinfo_nonet.setVisibility(View.VISIBLE);
		container.setVisibility(View.GONE);
	}

	private void showImage(int i, TeamShow teamShow) {
		// TODO Auto-generated method stub

		// mAbImageLoader.display(show_image, teamShow.getList().get(i - 1)
		// .getWorkimg());
		if (teamShow.getList().size() == 5) {
			LCUtils.mImageloader(teamShow.getList().get(i - 1).getWorkimg(),
					show_image, context);
			lc_findinfo_author.setText(teamShow.getList().get(i - 1)
					.getUsername()
					+ "");
		} else {
			showDialog(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			}, new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					neiInit();
				}
			}, "暂数据！", "返回", "刷新");

		}
	}

	/**
	 * 点赞
	 */
	public void clickPraise(final FindList classList) {
		AbRequestParams params = new AbRequestParams();
		params.put("userid", LCConstant.userinfo.getUserid() + "");
		params.put("teamid", classList.getGroupsid());
		params.put("uuid", LCUtils.getOnly(context));
		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.TEAM_WORK_SHOW_PRAISE, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						Praise praise = JSONUtils.getInstatce().getPraise(
								content);
						if ("0".equals(praise.getErrorCode())) {
							setTitle(lc_find_praisecount, praise.getNumber());
                            try {
                                dbhelper.add(new IsPraise("1",classList.getGroupsid(),LCConstant.userinfo.getUserid()));
                                AbToastUtil.showToast(context, "+1");
                                if (LCFindInfoPraise.findRefresh != null) {
                                    LCFindInfoPraise.findRefresh.refresh(position);
                                }
                            } catch (Exception e) {
                                showToast("点过赞了！");
                            }
						} else {
							AbToastUtil.showToast(context,
									praise.getErrorMessage());
						}

					};

					// 开始执行前
					@Override
					public void onStart() {
						LCUtils.startProgressDialog(context);
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						// AbToastUtil.showToast(context, content);
                        isNoNet();
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {
						LCUtils.stopProgressDialog(getContext());
					};
				});
	}

	private void setTitle(TextView lc_class_title, String videoclassname) {
		// TODO Auto-generated method stub
		if (!TextUtils.isEmpty(videoclassname)) {
			lc_class_title.setText(videoclassname);
		} else {
			lc_class_title.setText("");
		}
	}

	private void neiInit() {
		// TODO Auto-generated method stub
		mAbHttpUtil = AbHttpUtil.getInstance(context);
		mAbHttpUtil.setTimeout(5000);
		AbRequestParams params = new AbRequestParams();
		if (LCConstant.userinfo != null) {

			params.put("userid", LCConstant.userinfo.getUserid() + "");
		} else {
			params.put("userid", "");
		}
		params.put("groupsid", findList.getGroupsid());
		params.put("uuid", LCUtils.getOnly(context));

		mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.TEAM_WORK_SHOW, params,
				new AbStringHttpResponseListener() {

					// 获取数据成功会调用这里
					@Override
					public void onSuccess(int statusCode, String content) {
						teamShow = JSONUtils.getInstatce().getTeamShow(content);
						setDate(teamShow);
					};

					// 开始执行前
					@Override
					public void onStart() {
						// LCUtils.startProgressDialog(context);
					}

					// 失败，调用
					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						Intent intent = new Intent(context, LCNONetWork.class);
						startActivityForResult(intent, 101);
					}

					// 完成后调用，失败，成功
					@Override
					public void onFinish() {
						// LCUtils.stopProgressDialog();
					};
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String ss = data.getStringExtra("isRefresh");
		if ("1".equals(ss)) {
			// 刷新
			neiInit();
		} else {
			// 不刷新
			AbDialogUtil.removeDialog(context);
		}

	}

	protected void setDate(TeamShow teamShow) {
		// TODO Auto-generated method stub
		if ("0".equals(teamShow.getErrorCode())) {
			showImage(SELECT, teamShow);
			LCUtils.mImageloader(findList.getGroupimg(), lc_bigshow, context);
			// ImageLoader.getInstance(context).DisplayImage(
			// findList.getGroupimg(), lc_bigshow);

			String setSallimage = setSallimage(findList.getGroupimg());
			LCUtils.mImageloader(setSallimage, lc_smallshow, context);

			if (!TextUtils.isEmpty(findList.getName())) {
				lc_findinfo_name.setText(findList.getName() + "");
			} else {
				lc_findinfo_name.setText("");
			}
			if (!TextUtils.isEmpty(findList.getName())) {
				lc_find_name.setText(findList.getName() + "");
			} else {
				lc_find_name.setText("");
			}
			if (!TextUtils.isEmpty(findList.getName())) {
				lc_find_praisecount.setText(findList.getPraise() + "");
			} else {
				lc_find_praisecount.setText("0");
			}
		} else {
			showToast(teamShow.getErrorMessage());
			LCUtils.ReLogin(teamShow.getErrorCode(), mContext,teamShow.getErrorMessage());
		}

	}

	private String setSallimage(String groupimg) {
		if (TextUtils.isEmpty(groupimg))
			return "";
		String start = groupimg.substring(0, groupimg.lastIndexOf("."));
		String end = groupimg.substring(groupimg.lastIndexOf("."),
				groupimg.length());
		String substring = start + "_0" + end;
		L.e(substring);
		return substring;
	}

	private void zoomImageFromThumb(View thumbView, int position) {
		// If there's an animation in progress, cancel it immediately and
		// proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		// viewPager.setImageResource(R.drawable.guidance_two);

		// viewPager.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// zoo();
		// }
		// });
		// viewPager.setAdapter(new SamplePagerAdapter(mThumbIds, mContext));
		// viewPager.setCurrentItem(position);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step
		// involves lots of math. Yay, math.
		startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the
		// final bounds are the global visible rectangle of the container view.
		// Also
		// set the container view's offset as the origin for the bounds, since
		// that's
		// the origin for the positioning animation properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);

		findViewById(R.id.container).getGlobalVisibleRect(finalBounds,
				globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the
		// "center crop" technique. This prevents undesirable stretching during
		// the animation.
		// Also calculate the start scaling factor (the end scaling factor is
		// always 1.0).

		if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
				.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// show the zoomed-in view. When the animation
		// begins,
		// it will position the zoomed-in view in the place of the thumbnail.
		viewPager.setVisibility(View.VISIBLE);
		// Set the pivot point for SCALE_X and SCALE_Y transformations to the
		// top-left corner of
		// the zoomed-in view (the default is the center of the view).

		// 璁剧疆鍔ㄧ敾寮�濮嬬殑SCALE_X
		// 鍜孲CALE_Y鍙樺寲鐨勫乏涓婅鍘熺偣,濡傛灉涓嶈缃殑璇濋粯璁や粠View鐨刢enter寮�濮�
		// viewPager.setPivotX(0f);
		// viewPager.setPivotY(0f);
		//
		AnimatorSet animSet = new AnimatorSet();
		animSet.setDuration(1);
		animSet.play(ObjectAnimator.ofFloat(viewPager, "pivotX", 0f))
				.with(ObjectAnimator.ofFloat(viewPager, "pivotY", 0f))
				.with(ObjectAnimator.ofFloat(viewPager, "alpha", 1.0f));
		animSet.start();

		// Construct and run the parallel animation of the four translation and
		// scale properties
		// (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(lc_smallshow,
				"alpha", 1.0f, 0.f);
		ObjectAnimator animatorX = ObjectAnimator.ofFloat(viewPager, "x",
				startBounds.left, finalBounds.left);
		ObjectAnimator animatorY = ObjectAnimator.ofFloat(viewPager, "y",
				startBounds.top, finalBounds.top);
		ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(viewPager,
				"scaleX", startScale, 1f);
		ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(viewPager,
				"scaleY", startScale, 1f);

		set.play(alphaAnimator).with(animatorX).with(animatorY)
				.with(animatorScaleX).with(animatorScaleY);
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {

			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down to the
		// original bounds
		// and show the thumbnail instead of the expanded image.
		startScaleFinal = startScale;

	}


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseImageView(lc_smallshow);
        releaseImageView(show_image);
        releaseImageView(lc_bigshow);
    }

    public void zoo() {
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}
		// photoView.clearZoom();
		boolean scaleResult = true;
		// Animate the four positioning/sizing properties in
		// parallel,
		// back to their
		// original values.
		AnimatorSet as = new AnimatorSet();
		ObjectAnimator containAlphaAnimator = ObjectAnimator.ofFloat(
				lc_smallshow, "alpha", 0.f, 1.0f);
		if (scaleResult) {
			ObjectAnimator animatorX = ObjectAnimator.ofFloat(viewPager, "x",
					startBounds.left);
			ObjectAnimator animatorY = ObjectAnimator.ofFloat(viewPager, "y",
					startBounds.top);
			ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(viewPager,
					"scaleX", startScaleFinal);
			ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(viewPager,
					"scaleY", startScaleFinal);

			as.play(containAlphaAnimator).with(animatorX).with(animatorY)
					.with(animatorScaleX).with(animatorScaleY);
		} else {
			// the selected photoview is beyond the mobile
			// screen display
			// so it just fade out
			ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(viewPager,
					"alpha", 0.1f);
			as.play(alphaAnimator).with(containAlphaAnimator);
		}
		as.setDuration(mShortAnimationDuration);
		as.setInterpolator(new DecelerateInterpolator());
		as.addListener(new AnimatorListenerAdapter() {

			@Override
			public void onAnimationEnd(Animator animation) {
				viewPager.clearAnimation();
				viewPager.setVisibility(View.GONE);
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				viewPager.clearAnimation();
				viewPager.setVisibility(View.GONE);
				mCurrentAnimator = null;
			}
		});
		as.start();
		mCurrentAnimator = as;
	}

}
