package cn.ledoing.view;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.ledoing.activity.R;
import cn.ledoing.utils.AbViewUtil;

/**
 * 我的大星星and小星星
 *
 * @author wpt
 */
public class LCRatingBarFace extends FrameLayout implements OnClickListener{

    private Context mContext;
    private ImageView star1, star2, star3, star4, star5;
    private LinearLayout startall;
    private View inflate;
    private float mark = 0.0f;

    public int getMark() {

        return (int)mark;
    }

    public LCRatingBarFace(Context context) {
        super(context);
        init(context);
    }

    public LCRatingBarFace(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;

        inflate = View.inflate(context, R.layout.layout_lcratingbar_face, null);
        startall = (LinearLayout) inflate.findViewById(R.id.startall);
        star1 = (ImageView) inflate.findViewById(R.id.star1);
        star2 = (ImageView) inflate.findViewById(R.id.star2);
        star3 = (ImageView) inflate.findViewById(R.id.star3);
        star4 = (ImageView) inflate.findViewById(R.id.star4);
        star5 = (ImageView) inflate.findViewById(R.id.star5);
        addView(inflate);
    }

    public void setOnClic(){
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);
    }



    /***
     * 设置分数
     */
    public void setMark(float mark) {
        this.mark = mark;
        if (mark == 0.0f) {
            star1.setBackgroundResource(R.drawable.big_face_normal);
            star2.setBackgroundResource(R.drawable.big_face_normal);
            star3.setBackgroundResource(R.drawable.big_face_normal);
            star4.setBackgroundResource(R.drawable.big_face_normal);
            star5.setBackgroundResource(R.drawable.big_face_normal);
        } else if (mark == 1.0f) {
            star1.setBackgroundResource(R.drawable.big_face_black);
            star2.setBackgroundResource(R.drawable.big_face_normal);
            star3.setBackgroundResource(R.drawable.big_face_normal);
            star4.setBackgroundResource(R.drawable.big_face_normal);
            star5.setBackgroundResource(R.drawable.big_face_normal);
        } else if (mark == 2.0f) {
            star1.setBackgroundResource(R.drawable.big_face_black);
            star2.setBackgroundResource(R.drawable.big_face_black);
            star3.setBackgroundResource(R.drawable.big_face_normal);
            star4.setBackgroundResource(R.drawable.big_face_normal);
            star5.setBackgroundResource(R.drawable.big_face_normal);
        } else if (mark == 3.0f) {
            star1.setBackgroundResource(R.drawable.big_face_yellow);
            star2.setBackgroundResource(R.drawable.big_face_yellow);
            star3.setBackgroundResource(R.drawable.big_face_yellow);
            star4.setBackgroundResource(R.drawable.big_face_normal);
            star5.setBackgroundResource(R.drawable.big_face_normal);
        } else if (mark == 4.0f) {
            star1.setBackgroundResource(R.drawable.big_face_red);
            star2.setBackgroundResource(R.drawable.big_face_red);
            star3.setBackgroundResource(R.drawable.big_face_red);
            star4.setBackgroundResource(R.drawable.big_face_red);
            star5.setBackgroundResource(R.drawable.big_face_normal);
        } else if (mark == 5.0f) {
            star1.setBackgroundResource(R.drawable.big_face_red);
            star2.setBackgroundResource(R.drawable.big_face_red);
            star3.setBackgroundResource(R.drawable.big_face_red);
            star4.setBackgroundResource(R.drawable.big_face_red);
            star5.setBackgroundResource(R.drawable.big_face_red);
        }
        if(clickListaner != null){
            clickListaner.getStarNum((int)mark);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.star1:
                setMark(1.0f);
                break;
            case R.id.star2:
                setMark(2.0f);
                break;
            case R.id.star3:
                setMark(3.0f);
                break;
            case R.id.star4:
                setMark(4.0f);
                break;
            case R.id.star5:
                setMark(5.0f);
                break;
            default:
                setMark(0.0f);
                break;
        }
    }

    public interface OnStarClickListaner{
        void getStarNum(int num);
    }
    private OnStarClickListaner clickListaner = null;
    public void setOnStarClickListener(OnStarClickListaner listener){
        this.clickListaner = listener;
    }

}
