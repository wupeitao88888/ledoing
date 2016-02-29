package cn.ledoing.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import cn.ledoing.activity.R;
import cn.ledoing.utils.AbViewUtil;

/**
 * 我的大星星and小星星
 *
 * @author wpt
 */
public class LCRatingBar extends FrameLayout {

    private Context mContext;
    private ImageView star1, star2, star3, star4, star5;
    private LinearLayout startall;
    private View inflate;

    public LCRatingBar(Context context) {
        super(context);
        init(context);
    }

    public LCRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;

        inflate = View.inflate(context, R.layout.layout_lcratingbar, null);
        startall = (LinearLayout) inflate.findViewById(R.id.startall);
        star1 = (ImageView) inflate.findViewById(R.id.star1);
        star2 = (ImageView) inflate.findViewById(R.id.star2);
        star3 = (ImageView) inflate.findViewById(R.id.star3);
        star4 = (ImageView) inflate.findViewById(R.id.star4);
        star5 = (ImageView) inflate.findViewById(R.id.star5);
        addView(inflate);
    }

    /***
     * 设置分数
     */
    public void setMark(float mark) {
        if (mark == 0.0f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star2.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star3.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star4.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 0.5f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_ban));
            star2.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star3.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star4.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 1.0f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star3.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star4.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 1.5f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_ban));
            star3.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star4.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 2.0f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_select));
            star3.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star4.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 2.5f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_select));
            star3.setBackground(getResources().getDrawable(R.drawable.start_ban));
            star4.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 3.0f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_select));
            star3.setBackground(getResources().getDrawable(R.drawable.start_select));
            star4.setBackground(getResources().getDrawable(R.drawable.start_normal));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 3.5f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_select));
            star3.setBackground(getResources().getDrawable(R.drawable.start_select));
            star4.setBackground(getResources().getDrawable(R.drawable.start_ban));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 4.0f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_select));
            star3.setBackground(getResources().getDrawable(R.drawable.start_select));
            star4.setBackground(getResources().getDrawable(R.drawable.start_select));
            star5.setBackground(getResources().getDrawable(R.drawable.start_normal));
        } else if (mark == 4.5f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_select));
            star3.setBackground(getResources().getDrawable(R.drawable.start_select));
            star4.setBackground(getResources().getDrawable(R.drawable.start_select));
            star5.setBackground(getResources().getDrawable(R.drawable.start_ban));
        } else if (mark == 5.0f) {
            star1.setBackground(getResources().getDrawable(R.drawable.start_select));
            star2.setBackground(getResources().getDrawable(R.drawable.start_select));
            star3.setBackground(getResources().getDrawable(R.drawable.start_select));
            star4.setBackground(getResources().getDrawable(R.drawable.start_select));
            star5.setBackground(getResources().getDrawable(R.drawable.start_select));
        }
    }

    public void setSmail(boolean isSmail) {
        if (isSmail) {
            for (int i = 0; i < startall.getChildCount(); i++) {
                LinearLayout.LayoutParams linearParams1 = (LinearLayout.LayoutParams) startall.getChildAt(i)
                        .getLayoutParams();
                linearParams1.height = (int) AbViewUtil.dip2px(mContext, 11.0f);
                linearParams1.width = (int) AbViewUtil.dip2px(mContext, 10.0f);
                startall.getChildAt(i).setLayoutParams(linearParams1);
            }
        } else {
            for (int i = 0; i < startall.getChildCount(); i++) {
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) startall.getChildAt(i)
                        .getLayoutParams();
                linearParams.height = (int) AbViewUtil.dip2px(mContext, 19.0f);
                linearParams.width = (int) AbViewUtil.dip2px(mContext, 18.0f);
                startall.getChildAt(i).setLayoutParams(linearParams);
            }

        }

    }


}
