package cn.ledoing.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import cn.ledoing.activity.LCUploading.CallBackMe;
import cn.ledoing.activity.R.string;

import cn.ledoing.fragment.HomeFragment;
import cn.ledoing.fragment.LCClassIndexFragment;
import cn.ledoing.fragment.LCFindFragment;
import cn.ledoing.fragment.LCLeftMenuFragment;
import cn.ledoing.fragment.LCMeFragment;
import cn.ledoing.fragment.LCMeFragment.TaskCallBack;
import cn.ledoing.global.LCConstant;
import cn.ledoing.utils.L;
import cn.ledoing.view.LCTitleBar;

import com.ab.view.slidingmenu.SlidingMenu;

import java.util.ArrayList;

public class LCHomeFragmentHost extends LCActivitySupport implements
        OnCheckedChangeListener {
    private FragmentTabHost mTabHost;
    private RadioGroup radioGroup;
    private long exitTime = 0;
    // private SlidingMenu mMenu;
    private LCTitleBar layout_top;
    private SlidingMenu menu;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        setContentView(R.layout.activity_lc_homefragmenthost);
        // mMenu = (SlidingMenu) findViewById(R.id.id_menu);
        layout_top = (LCTitleBar) findViewById(R.id.layout_top);
        layout_top.setCenterTitle(mString(string.jxb_tab_credit));
        layout_top.setCenterTitleColor(getResources().getColor(R.color.white));
        layout_top.setBackGb(getResources().getColor(R.color.green));
        // layout_top.isLeftVisibility(false);
        layout_top.setLeftImage(R.drawable.menu);
        layout_top.isLeftTitleVisibility(false);
        layout_top.setOnclickBackListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (menu.isMenuShowing()) {
                    L.e("关闭++++++++++++");
                    menu.showContent();
                } else {
                    L.e("打开++++++++++++");
//                    layout_top.setAnimetion(R.drawable.x_menu);
                    menu.showMenu();
                }
            }
        });
        LCMeFragment.setTaskBack(new TaskCallBack() {

            @Override
            public void success(int obj) {
                // TODO Auto-generated method stub
                // if (obj == 0) {
                //
                // // mTabHost.setCurrentTab(2);
                // // ((RadioButton) findViewById(R.id.radio_me))
                // // .setChecked(true);
                // // LCConstant.isRef = true;
                // } else {
                mTabHost.setCurrentTab(1);
                ((RadioButton) findViewById(R.id.radio_classindex))
                        .setChecked(true);
                // }

            }
        });

        LCUploading.setCallBackMe(new CallBackMe() {

            @Override
            public void mIntent() {
                // TODO Auto-generated method stub
                mTabHost.setCurrentTab(3);
                ((RadioButton) findViewById(R.id.radio_me))
                        .setChecked(true);
            }
        });
        // SlidingMenu的配置
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);

        // slidingmenu的事件模式，如果里面有可以滑动的请用TOUCHMODE_MARGIN
        // 可解决事件冲突问题
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.0f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        // menu视图的Fragment添加
        menu.setMenu(R.layout.sliding_menu_menu);

        menu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {

                layout_top.setAnmin();
            }
        });

        menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {

                layout_top.setAnimetion(R.drawable.menu);
            }
        });

        getFragmentManager().beginTransaction()
                .replace(R.id.menu_frame, new LCLeftMenuFragment()).commit();
        InitView();

    }

    private void InitView() {
        // TODO Auto-generated method stub

        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(),
                android.R.id.tabcontent);
        mTabHost.getTabWidget().setVisibility(View.GONE); //

        mTabHost.addTab(mTabHost.newTabSpec("find").setIndicator("find"),
                LCFindFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("classindex").setIndicator("classindex"),
                LCClassIndexFragment.class, null);


        mTabHost.addTab(mTabHost.newTabSpec("home").setIndicator("home"),
                HomeFragment.class, null);


        mTabHost.addTab(mTabHost.newTabSpec("me").setIndicator("me"),
                LCMeFragment.class, null);

        mTabHost.setCurrentTabByTag("classindex");
        ((RadioButton) findViewById(R.id.radio_classindex)).setChecked(true);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }
        return false;
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            showToast(string.exit);
            exitTime = System.currentTimeMillis();
        } else {
            isExit();
        }
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentManager fm = getSupportFragmentManager();
        LCClassIndexFragment index = (LCClassIndexFragment) fm
                .findFragmentByTag("credit");
        LCFindFragment monitor = (LCFindFragment) fm
                .findFragmentByTag("contact");
        HomeFragment home = (HomeFragment) fm
                .findFragmentByTag("home");
        LCMeFragment expert = (LCMeFragment) fm.findFragmentByTag("me");

        FragmentTransaction ft = fm.beginTransaction();
        L.e(checkedId + "+++++++++++");
        // ** Detaches the androidfragment if exists */
        if (index != null)
            ft.detach(index);
        if (monitor != null)
            ft.detach(monitor);
        if (home != null)
            ft.detach(home);
        if (expert != null)
            ft.detach(expert);

        switch (checkedId) {
            case R.id.radio_classindex:
                layout_top.isRightVisibility(View.GONE);
                if (index == null) {
                    ft.add(android.R.id.tabcontent, new LCClassIndexFragment(),
                            "classindex");
                } else {
                    ft.attach(index);
                }
                layout_top.setCenterTitle(mString(string.jxb_tab_credit));
                mTabHost.setCurrentTabByTag("classindex");
                break;
            case R.id.radio_home:
                layout_top.isRightVisibility(View.GONE);
                if (home == null) {
                    ft.add(android.R.id.tabcontent, new HomeFragment(), "home");
                } else {
                    ft.attach(home);
                }
                layout_top.setCenterTitle(mString(string.jxb_tab_home));
                mTabHost.setCurrentTabByTag("home");
                break;
            case R.id.radio_find:
                layout_top.setRightTitle("发贴");
                layout_top.isRightVisibility(View.VISIBLE);
                layout_top.setRightTitleListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Posts.class);
                        startActivity(intent);
                    }
                });
                if (monitor == null) {
                    ft.add(android.R.id.tabcontent, new LCFindFragment(), "find");
                } else {
                    ft.attach(monitor);
                }
                layout_top.setCenterTitle(mString(string.jxb_tab_contact));
                mTabHost.setCurrentTabByTag("find");

                break;

            case R.id.radio_me:
                layout_top.isRightVisibility(View.GONE);
                if (expert == null) {
                    // ft.add(R.id.realtabcontent, new QLJ_Query_Fragment(),
                    // "expert");
                } else {
                    ft.attach(expert);
                }
                layout_top.setCenterTitle(mString(string.jxb_tab_me));
                mTabHost.setCurrentTabByTag("me");
                break;
        }
    }
}
