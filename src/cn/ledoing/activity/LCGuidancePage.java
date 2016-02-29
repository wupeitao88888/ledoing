package cn.ledoing.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import cn.ledoing.view.LCSlideShowView;
import cn.ledoing.view.LCViewPageSelectListener;

/*
 * 
 * 引导页
 * 
 */
public class LCGuidancePage extends LCActivitySupport implements
        LCViewPageSelectListener {
    private LCSlideShowView mSlideShowView;
    private ImageView lc_first, lc_three, lc_two, lc_four;
    private Button lc_guidance_button;
    private TextView lc_title_original, lc_title_interaction, lc_title_team;
    private AnimatorSet set;
    private ImageView lc_gitdancepage_frist,lc_gitdancepage_two,lc_gitdancepage_three,lc_gitdancepage_four,lc_gitdancepage_hand,lc_gitdancepage_arm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_guidancepage);
        set = new AnimatorSet();
        lc_first = (ImageView) findViewById(R.id.lc_first);
        lc_two = (ImageView) findViewById(R.id.lc_two);
        lc_three = (ImageView) findViewById(R.id.lc_three);
        lc_four = (ImageView) findViewById(R.id.lc_four);
        lc_title_original = (TextView) findViewById(R.id.lc_title_original);
        lc_title_interaction = (TextView) findViewById(R.id.lc_title_interaction);
        lc_title_team = (TextView) findViewById(R.id.lc_title_team);


        View view1 = LayoutInflater.from(context).inflate(
                R.layout.lc_guidancepage_frist, null);
        lc_gitdancepage_frist=(ImageView)view1.findViewById(R.id.lc_gitdancepage_frist);
        lc_gitdancepage_hand=(ImageView)view1.findViewById(R.id.lc_gitdancepage_hand);


        View view2 = LayoutInflater.from(context).inflate(
                R.layout.lc_guidancepage_two, null);
        lc_gitdancepage_two=(ImageView)view2.findViewById(R.id.lc_gitdancepage_two);
        lc_gitdancepage_arm=(ImageView)view2.findViewById(R.id.lc_gitdancepage_arm);


        View view3 = LayoutInflater.from(context).inflate(
                R.layout.lc_guidancepage_three, null);
        lc_gitdancepage_three=(ImageView)view3.findViewById(R.id.lc_gitdancepage_three);

        View view4 = LayoutInflater.from(context).inflate(
                R.layout.lc_guidancepage_four, null);
        lc_gitdancepage_four=(ImageView)view4.findViewById(R.id.lc_gitdancepage_four);

        Glide.with(LCGuidancePage.this).load(R.drawable.guidance_pic1).into(lc_gitdancepage_frist);
        Glide.with(LCGuidancePage.this).load(R.drawable.guidance_pic2).into(lc_gitdancepage_two);
        Glide.with(LCGuidancePage.this).load(R.drawable.guidance_pic3).into(lc_gitdancepage_three);
        Glide.with(LCGuidancePage.this).load(R.drawable.guidance_pic4).into(lc_gitdancepage_four);
        Glide.with(LCGuidancePage.this).load(R.drawable.hand_action).into(lc_gitdancepage_hand);
        Glide.with(LCGuidancePage.this).load(R.drawable.arm).into(lc_gitdancepage_arm);

        lc_guidance_button = (Button) view4
                .findViewById(R.id.lc_guidance_button);
        List<View> imageUris = new ArrayList<View>();
        imageUris.add(view1);
        imageUris.add(view2);
        imageUris.add(view3);
        imageUris.add(view4);


        mSlideShowView = (LCSlideShowView) findViewById(R.id.slideshowView);
        mSlideShowView.setImageUris(imageUris);
        mSlideShowView.setJXBViewPageSelectListener(this);
        lc_first.setImageResource(R.drawable.white_title);
        setAinmalOut(lc_title_original);
        lc_title_interaction.setVisibility(View.GONE);
        lc_title_team.setVisibility(View.GONE);


        lc_guidance_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, LCHomeFragmentHost.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 淡出
     */
    public void setAinmalOut(TextView textView) {

        set.playTogether(
                ObjectAnimator.ofFloat(textView, "alpha", 0, 0.25f, 1)
        );
        set.setDuration(1000).start();
    }
    /**
     * 淡出
     */
    public void setAinmalOut(Button textView) {

        set.playTogether(
                ObjectAnimator.ofFloat(textView, "alpha", 0, 0.25f, 1),
                ObjectAnimator.ofFloat(textView, "translationY", 90, 0)
        );
        set.setDuration(1000).start();
    }
    /**
     * 淡入
     */
    public void setAinmalIn(TextView textView) {

        set.playTogether(
                ObjectAnimator.ofFloat(textView, "alpha", 1, 0.25f, 0)
        );
        set.setDuration(1000).start();
    }

    @Override
    public void succeedCallBack(int p) {
        // TODO Auto-generated method stub
        switch (p) {
            case 0:
                lc_first.setImageResource(R.drawable.white_title);
                lc_two.setImageResource(R.drawable.shallow);
                lc_three.setImageResource(R.drawable.shallow);
                lc_four.setImageResource(R.drawable.shallow);
                setAinmalOut(lc_title_original);
                lc_title_original.setVisibility(View.VISIBLE);
                lc_title_interaction.setVisibility(View.GONE);
                        lc_title_team.setVisibility(View.GONE);
                lc_gitdancepage_arm.setVisibility(View.VISIBLE);
                lc_gitdancepage_hand.setVisibility(View.VISIBLE);
                break;
            case 1:
                setAinmalIn(lc_title_original);
                setAinmalOut(lc_title_interaction);
                lc_gitdancepage_hand.setVisibility(View.GONE);
                lc_gitdancepage_arm.setVisibility(View.GONE);
                lc_title_original.setVisibility(View.GONE);
                lc_title_interaction.setVisibility(View.VISIBLE);
                lc_title_team.setVisibility(View.GONE);
                lc_first.setImageResource(R.drawable.shallow);
                lc_two.setImageResource(R.drawable.white_title);
                lc_three.setImageResource(R.drawable.shallow);
                lc_four.setImageResource(R.drawable.shallow);
                break;
            case 2:

                setAinmalIn(lc_title_interaction);
                setAinmalOut(lc_title_team);
                lc_title_original.setVisibility(View.GONE);
                lc_title_team.setVisibility(View.VISIBLE);
                lc_title_interaction.setVisibility(View.GONE);
                lc_first.setImageResource(R.drawable.shallow);
                lc_two.setImageResource(R.drawable.shallow);
                lc_three.setImageResource(R.drawable.white_title);
                lc_four.setImageResource(R.drawable.shallow);
                break;
            case 3:

                setAinmalIn(lc_title_team);
                setAinmalOut(lc_guidance_button);
                lc_title_original.setVisibility(View.GONE);
                lc_title_team.setVisibility(View.GONE);
                lc_title_interaction.setVisibility(View.GONE);
                lc_first.setImageResource(R.drawable.shallow);
                lc_two.setImageResource(R.drawable.shallow);
                lc_three.setImageResource(R.drawable.shallow);
                lc_four.setImageResource(R.drawable.white_title);
                break;
        }
    }

    @Override
    public void succeedEndCallBack(int p) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        release();
    }

    public void release(){
        releaseImageView(lc_gitdancepage_frist);
        releaseImageView(lc_gitdancepage_two);
        releaseImageView(lc_gitdancepage_three);
        releaseImageView(lc_gitdancepage_four);
        releaseImageView(lc_gitdancepage_hand);
        releaseImageView(lc_gitdancepage_arm);
    }


}
