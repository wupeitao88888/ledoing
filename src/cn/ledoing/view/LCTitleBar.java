package cn.ledoing.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import cn.ledoing.activity.R;

/**
 * 标题栏
 *
 * @author wpt
 */
public class LCTitleBar extends FrameLayout {

    private Context mContext;
    private TextView lc_left_menu, lc_center_menu, lc_right_menu;
    private ImageView lc_left_back, lc_right_image;
    private RelativeLayout lc_left_back_all, lc_center_all, lc_right_all, ll_title;
    private OnClickListener backListenetForUser;
    private OnClickListener backListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (backListenetForUser != null) {
                backListenetForUser.onClick(v);
            } else {
                ((Activity) mContext).onBackPressed();
            }
        }
    };

    public LCTitleBar(Context context) {
        super(context);
        init(context);
    }

    public LCTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View inflate = View.inflate(context, R.layout.lc_layout_title, null);
        lc_left_menu = (TextView) inflate.findViewById(R.id.lc_left_menu);
        lc_center_menu = (TextView) inflate.findViewById(R.id.title);
        lc_left_back = (ImageView) inflate.findViewById(R.id.lc_left_back);
        lc_left_back_all = (RelativeLayout) inflate
                .findViewById(R.id.lc_left_back_all);
        lc_center_all = (RelativeLayout) inflate
                .findViewById(R.id.lc_center_all);
        lc_right_all = (RelativeLayout) inflate.findViewById(R.id.lc_right_all);
        ll_title = (RelativeLayout) inflate.findViewById(R.id.ll_title);
        lc_right_menu = (TextView) inflate.findViewById(R.id.lc_right_menu);
        lc_right_image = (ImageView) inflate.findViewById(R.id.lc_right_image);
        lc_left_back_all.setOnClickListener(backListener);
        addView(inflate);
    }

    /**
     * 设置title背景颜色
     */
    public void setBackGb(int color) {
        ll_title.setBackgroundColor(color);
    }
    /**
     * 设置title背景颜色
     */
    public void setBackGbAlpha(float Alpha) {
        ll_title.setAlpha(Alpha);
    }

    /**
     * 设置”返回“是否隐藏
     */
    public void isLeftTitleVisibility(boolean isVisibit) {
        if (isVisibit)
            lc_left_menu.setVisibility(View.VISIBLE);
        else
            lc_left_menu.setVisibility(View.GONE);
    }

    /**
     * 设置返回监听
     */
    public void setOnclickBackListener(OnClickListener l) {
        lc_left_back_all.setOnClickListener(l);
    }

    /**
     * 设置左边的内容
     */
    public void setLeftTitle(String msg) {
        lc_left_menu.setText(msg);
    }

    /**
     * 设置左边的内容字体颜色
     */
    public void setLeftTitleColor(int msg) {
        lc_left_menu.setTextColor(msg);
    }


    /**
     * 设置左边的图片
     */
    public void setLeftImage(int draw) {
        lc_left_back.setBackgroundResource(draw);
    }

    /**
     * 旋转-90度
     */
    public void setAnimetion(final int draw) {



        AnimatorSet set = new AnimatorSet();
        set.playTogether(
//                ObjectAnimator.ofFloat(lc_left_back, "rotationX", 0, 360)
//                ObjectAnimator.ofFloat(lc_left_back, "rotationY", 0, 180)
                ObjectAnimator.ofFloat(lc_left_back, "rotation", 0, -90)
//                ObjectAnimator.ofFloat(myView, "translationX", 0, 90),
//                ObjectAnimator.ofFloat(myView, "translationY", 0, 90),
//                ObjectAnimator.ofFloat(myView, "scaleX", 1, 1.5f),
//                ObjectAnimator.ofFloat(myView, "scaleY", 1, 0.5f),
//                ObjectAnimator.ofFloat(myView, "alpha", 1, 0.25f, 1)
        );
        set.setDuration(500).start();
    }

    /**
     * 复位
     */
    public void setAnmin() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
//                ObjectAnimator.ofFloat(lc_left_back, "rotationX", 0, 360)
//                ObjectAnimator.ofFloat(lc_left_back, "rotationY", 0, 180)
                ObjectAnimator.ofFloat(lc_left_back, "rotation", -90, 0)
//                ObjectAnimator.ofFloat(myView, "translationX", 0, 90),
//                ObjectAnimator.ofFloat(myView, "translationY", 0, 90),
//                ObjectAnimator.ofFloat(myView, "scaleX", 1, 1.5f),
//                ObjectAnimator.ofFloat(myView, "scaleY", 1, 0.5f),
//                ObjectAnimator.ofFloat(myView, "alpha", 1, 0.25f, 1)
        );
        set.setDuration(500).start();

    }

    ;


    /**
     * 设置左边的是否显示
     */
    public void isLeftVisibility(boolean isVisibit) {
        if (isVisibit)
            lc_left_back_all.setVisibility(View.VISIBLE);
        else
            lc_left_back_all.setVisibility(View.GONE);
    }

    /**
     * 设置中间标题是否显示
     */
    public void isCenterVisibility(boolean isVisibit) {
        if (isVisibit)
            lc_center_all.setVisibility(View.VISIBLE);
        else
            lc_center_all.setVisibility(View.GONE);
    }

    /**
     * 设置中间标题内容
     */
    public void setCenterTitle(String msg) {
        lc_center_menu.setText(msg);
    }

    /**
     * 设置中间标题内容字体颜色
     */
    public void setCenterTitleColor(int msg) {
        lc_center_menu.setTextColor(msg);
    }


    /**
     * 设置右边标题内容
     */
    public void setRightTitle(String msg) {
        lc_right_menu.setText(msg);
        lc_right_menu.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边标题内容字体颜色
     */
    public void setRightTitle(int msg) {
        lc_right_menu.setTextColor(msg);
    }

    /**
     * 设置右边图片内容
     */
    public void setRightTitleRes(int msg) {
        lc_right_image.setBackgroundResource(msg);
        lc_right_image.setVisibility(View.VISIBLE);
    }

    /**
     * 设置右边标题内容
     */
    public void setRightTitleListener(OnClickListener l) {
        lc_right_all.setOnClickListener(l);
    }

    /**
     * 设置右边图片内容
     */
    public void setRightTitleImageListener(OnClickListener l) {
        lc_right_image.setOnClickListener(l);
    }
    public void isRightVisibility(int visibility){
        lc_right_all.setVisibility(visibility);
    }

}
