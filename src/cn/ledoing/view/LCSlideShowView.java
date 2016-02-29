package cn.ledoing.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.ledoing.activity.R;
import cn.ledoing.utils.L;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * viewpager
 *
 * @author wpt
 */

public class LCSlideShowView extends FrameLayout {

    private boolean isAutoPlay = false;
    private boolean circulation = false;
    private List<View> imageUris;
    private List<View> imageViewsList;
    private List<ImageView> dotViewsList;
    private LinearLayout mLinearLayout;
    private LCViewPage mViewPager;
    private int currentItem = 0;
    private ScheduledExecutorService scheduledExecutorService;
    private LCViewPageSelectListener jxbViewPageSelectListener;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            mViewPager.setCurrentItem(currentItem);
        }
    };

    public void clear() {
        imageUris.clear();
        imageViewsList.clear();
        dotViewsList.clear();
    }

    public void setCurrentItemMy(int currentItem) {
        mViewPager.setCurrentItem(currentItem);
    }

    public LCSlideShowView(Context context) {
        this(context, null);
    }

    public LCSlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LCSlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub

        initUI(context);
        if (!(imageUris.size() <= 0)) {

            setImageUris(imageUris);
        }
    }

    public void setIsAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        if (isAutoPlay) {
            startPlay();
        }
    }

    public void setCirculation(boolean circulation) {
        this.circulation = circulation;
    }

    private void initUI(Context context) {
        imageViewsList = new ArrayList<View>();
        dotViewsList = new ArrayList<ImageView>();
        imageUris = new ArrayList<View>();

        LayoutInflater.from(context).inflate(R.layout.lc_layout_slideshow,
                this, true);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        mViewPager = (LCViewPage) findViewById(R.id.viewPager);

    }

    public void setImageUris(List<View> imageuris) {

        for (int i = 0; i < imageuris.size(); i++) {
            imageUris.add(imageuris.get(i));
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 0, 0, 0);
        for (int i = 0; i < imageUris.size(); i++) {
            imageViewsList.add(imageuris.get(i));
            ImageView viewDot = new ImageView(getContext());
//			if (i == 0) {
//				viewDot.setBackgroundResource(R.drawable.jxb_title_white_select);
//			} else {
//				viewDot.setBackgroundResource(R.drawable.jxb_title_white);
//			}

            viewDot.setLayoutParams(lp);
            viewDot.setVisibility(View.GONE);
            dotViewsList.add(viewDot);
            mLinearLayout.addView(viewDot);
        }
        mViewPager.setFocusable(true);
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
                TimeUnit.SECONDS);
    }

    @SuppressWarnings("unused")
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    public void setJXBViewPageSelectListener(
            LCViewPageSelectListener jxbViewPageSelectListener) {
        this.jxbViewPageSelectListener = jxbViewPageSelectListener;
    }

//	private void setImageBackground(int selectItems) {
//		for (int i = 0; i < dotViewsList.size(); i++) {
//			if (i == selectItems) {
//				dotViewsList.get(i).setBackgroundResource(
//						R.drawable.jxb_title_white_select);
//			} else {
//				dotViewsList.get(i).setBackgroundResource(
//						R.drawable.jxb_title_white);
//			}
//		}
//	}

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            // ((ViewPag.er)container).removeView((View)object);
            if(circulation==false)
            ((ViewPager) container).removeView(imageViewsList.get(position));

        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;

        }

        //        @Override
//        public Object instantiateItem(View container, int position) {
//            // TODO Auto-generated method stub
//            ((ViewPager) container).addView(imageViewsList.get(position));
//            return imageViewsList.get(position);
//        }
        @Override
        public Object instantiateItem(ViewGroup view, int position) // 实例化Item

        {
            if(circulation){
                //对ViewPager页号求模取出View列表中要显示的项
                position %= imageViewsList.size();
                if (position < 0) {
                    position = imageViewsList.size() + position;
                }
                View v = imageViewsList.get(position);
                //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
                ViewParent vp = v.getParent();
                if (vp != null) {
                    ViewGroup parent = (ViewGroup) vp;
                    parent.removeView(v);
                }
                view.addView(v);
                //add listeners here if necessary
                return v;
            }else{
                ((ViewPager) view).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
            }
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
//            return Integer.MAX_VALUE;
            if(circulation==true)
                return (imageViewsList == null || imageViewsList.size() == 0) ? 0 : Integer.MAX_VALUE;
            else
                return imageViewsList.size();

//
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }


    public class MyPageChangeListener implements OnPageChangeListener {

        public MyPageChangeListener() {
            super();


        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            L.e("onPageScrollStateChanged+++++++++++++++++" + arg0);
            if (jxbViewPageSelectListener != null) {
                jxbViewPageSelectListener.succeedEndCallBack(arg0);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub
            L.e("onPageScrolled+++++++++++++++++" + pos);

            if (jxbViewPageSelectListener != null) {
                jxbViewPageSelectListener.succeedCallBack(pos % imageViewsList.size());
            }
        }

    }

    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (mViewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

}
