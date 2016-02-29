package cn.ledoing.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.ledoing.activity.LCAboutMe;
import cn.ledoing.activity.LCFeedback;
import cn.ledoing.activity.LCMeCode;
import cn.ledoing.activity.LCMeCourse;
import cn.ledoing.activity.LCMeLcBean;
import cn.ledoing.activity.LCUpdateUseNname;
import cn.ledoing.activity.LCUpdateUseNname.NameCallBack;
import cn.ledoing.activity.LCUserInfo;
import cn.ledoing.activity.LCUserInfo.UserPic;
import cn.ledoing.activity.LCUserLoginAndRegister;

import cn.ledoing.activity.R;
import cn.ledoing.global.LCConstant;
import cn.ledoing.utils.GlideCircleTransform;


@SuppressLint("NewApi")
public class LCLeftMenuFragment extends AbFragment implements OnClickListener {

    private View rootView;// 缓存Fragment view
    private Activity mActivity = null;
    private RelativeLayout lc_leftMenu_userinfo,// 用户信息
            lc_leftmenu_allclass,// 所有课程   2015/9/15 14:14 改为 我的课程
            lc_leftmenu_feedback,// 学习记录    2015/9/15 14:14 改为 意见反馈
            lc_leftmenu_set,// 设置
            lc_leftmenu_aboutme,// 关于我
            lc_leftmenu_mylcbean,//我的乐豆
            lc_leftmenu_mecode;// 我的二维码

    private ImageView lc_leftmenu_usericon;
    private TextView lc_leftmenu_username, lc_leftmenu_userage;


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = initView(inflater);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     * 方法说明： 编写日期: 2015-4-1 编写人员: 吴培涛
     */
    @SuppressLint("NewApi")
    private View initView(LayoutInflater inflater) {
        mActivity = getActivity();
        View v = inflater.inflate(R.layout.lc_layout_menu, null);
        lc_leftMenu_userinfo = (RelativeLayout) v
                .findViewById(R.id.lc_leftMenu_userinfo);
        lc_leftmenu_set = (RelativeLayout) v.findViewById(R.id.lc_leftmenu_set);
        lc_leftmenu_allclass = (RelativeLayout) v
                .findViewById(R.id.lc_leftmenu_allclass);
        lc_leftmenu_feedback = (RelativeLayout) v
                .findViewById(R.id.lc_leftmenu_feedback);
        lc_leftmenu_aboutme = (RelativeLayout) v
                .findViewById(R.id.lc_leftmenu_aboutme);
        lc_leftmenu_mecode = (RelativeLayout) v
                .findViewById(R.id.lc_leftmenu_mecode);
        lc_leftmenu_mylcbean = (RelativeLayout) v
                .findViewById(R.id.lc_leftmenu_mylcbean);
        lc_leftmenu_usericon = (ImageView) v
                .findViewById(R.id.lc_leftmenu_usericon);
        lc_leftmenu_username = (TextView) v
                .findViewById(R.id.lc_leftmenu_username);
        lc_leftmenu_userage = (TextView) v
                .findViewById(R.id.lc_leftmenu_userage);
        initDate();
        LCUserLoginAndRegister.setUserInfoCallBack(new LCUserLoginAndRegister.UserInfoCallBack() {
            @Override
            public void onSuccess() {
                initDate();
            }
        });
        LCUpdateUseNname.setNameCallBack(new NameCallBack() {

            @Override
            public void onSuccess(String name) {
                setMtext(name, lc_leftmenu_username);
            }
        });
        LCUserInfo.setUserPic(new UserPic() {

            @Override
            public void success(String url) {
                // TODO Auto-generated method stub
                Glide.with(mActivity)
                        .load(LCConstant.userinfo.getUserpic()).transform(new GlideCircleTransform(mActivity))
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .centerCrop()
////                    .placeholder(R.drawable.image_loading)
////                    .error(R.drawable.image_error)
//                    .crossFade()
                        .into(lc_leftmenu_usericon);
            }
        });
        lc_leftMenu_userinfo.setOnClickListener(this);
        lc_leftmenu_allclass.setOnClickListener(this);
        lc_leftmenu_feedback.setOnClickListener(this);
        lc_leftmenu_aboutme.setOnClickListener(this);
        lc_leftmenu_set.setOnClickListener(this);
        lc_leftmenu_mecode.setOnClickListener(this);
//        lc_leftmenu_mecode.setVisibility(View.GONE);
//        lc_leftmenu_allclass.setVisibility(View.GONE);
        lc_leftmenu_mylcbean.setOnClickListener(this);
//        lc_leftmenu_mylcbean.setVisibility(View.GONE);
        return v;
    }

    private void initDate() {
        if (LCConstant.islogin) {
            if (!TextUtils.isEmpty(LCConstant.userinfo.getUserpic())) {
//                LCUtils.mImageloader(LCConstant.userinfo.getUserpic(),
//                        lc_leftmenu_usericon, mActivity);
//                Glide.with(mActivity)
//                        .load(LCConstant.userinfo.getUserpic()).transform(new GlideCircleTransform(mActivity))
////                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
////                        .placeholder(R.drawable.image_loading)
////                        .error(R.drawable.image_error)
//                        .crossFade()
//                        .into(lc_leftmenu_usericon);
                Glide.with(mActivity)
                        .load(LCConstant.userinfo.getUserpic()).transform(new GlideCircleTransform(mActivity))
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .centerCrop()
////                    .placeholder(R.drawable.image_loading)
////                    .error(R.drawable.image_error)
//                    .crossFade()
                        .into(lc_leftmenu_usericon);

            }else{
                Glide.with(mActivity)
                        .load(R.drawable.hand)
//                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                        .placeholder(R.drawable.image_loading)
//                        .error(R.drawable.image_error)
                        .crossFade()
                        .into(lc_leftmenu_usericon);
            }
            if (!TextUtils.isEmpty(LCConstant.userinfo.getRealname())) {
                setMtext(LCConstant.userinfo.getRealname(), lc_leftmenu_username);
            } else {
                lc_leftmenu_username.setText("请填写");
            }
            if (!TextUtils.isEmpty(LCConstant.userinfo.getBirthday())) {
                // int year = Integer.parseInt(LCConstant.userinfo.getBirthday()
                // .substring(0, 4));
                // Date now = new Date();
                // SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
                // Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
                // String str = formatter.format(curDate);
                // int age = (int) (Integer.parseInt(str) - year);
                // lc_leftmenu_userage.setText(age);
            }
        }
    }

    public void setMtext(String text, TextView mTextView) {
        if (TextUtils.isEmpty(text)) {
            mTextView.setText("");
            return;
        }
        if (text.length() < 5) {
            mTextView.setText(text);
        } else {
            mTextView.setText(text.substring(0, 5) + "...");
        }


    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (LCConstant.islogin) {

            switch (v.getId()) {
                case R.id.lc_leftMenu_userinfo:
                    Intent intent = new Intent(mActivity, LCUserInfo.class);
                    mActivity.startActivity(intent);
                    break;
                case R.id.lc_leftmenu_allclass://2015/9/15 14:14 改为 我的课程
                    mActivity.startActivity(new Intent(mActivity,
                            LCMeCourse.class));
                    break;
                case R.id.lc_leftmenu_feedback://2015/9/15 14:14 改为 意见反馈
                    mActivity.startActivity(new Intent(mActivity,
                            LCFeedback.class));
                    break;
                case R.id.lc_leftmenu_aboutme:
                    mActivity.startActivity(new Intent(mActivity, LCAboutMe.class));
                    break;
                case R.id.lc_leftmenu_set:
//                    mActivity.startActivity(new Intent(mActivity, LCUserSet.class));
                    break;
                case R.id.lc_leftmenu_mecode:
                    mActivity.startActivity(new Intent(mActivity, LCMeCode.class));
                    break;
                case R.id.lc_leftmenu_mylcbean:
                    mActivity.startActivity(new Intent(mActivity, LCMeLcBean.class));
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.lc_leftMenu_userinfo:
                    Intent intent = new Intent(mActivity,
                            LCUserLoginAndRegister.class);
                    intent.putExtra("mIntent", 0);
                    mActivity.startActivity(intent);
                    break;
                case R.id.lc_leftmenu_allclass:
                    Intent coursesRecord = new Intent(mActivity,
                            LCUserLoginAndRegister.class);
                    coursesRecord.putExtra("mIntent", 1);
                    mActivity.startActivity(coursesRecord);

                    break;
                case R.id.lc_leftmenu_feedback://意见反馈
//                    2015/9/15 14:25   注，跳转到学习几轮
//                   Intent studyRecord= new Intent(mActivity,
//                            LCUserLoginAndRegister.class);
//                    studyRecord.putExtra("mIntent",2);
//                    mActivity.startActivity(studyRecord);

                    mActivity.startActivity(new Intent(mActivity,
                            LCFeedback.class));
                    break;
                case R.id.lc_leftmenu_aboutme:
                    Intent aboutMe = new Intent(mActivity, LCAboutMe.class);
                    aboutMe.putExtra("mIntent", 3);
                    mActivity.startActivity(aboutMe);
                    break;
                case R.id.lc_leftmenu_set:
//                    mActivity.startActivity(new Intent(mActivity, LCUserSet.class));
                    break;
                case R.id.lc_leftmenu_mecode:
                    Intent meCode = new Intent(mActivity,
                            LCUserLoginAndRegister.class);
                    meCode.putExtra("mIntent", 4);
                    mActivity.startActivity(meCode);
                    break;
                case R.id.lc_leftmenu_mylcbean:
                    Intent mylcbean = new Intent(mActivity,
                            LCUserLoginAndRegister.class);
                    mylcbean.putExtra("mIntent", 5);
                    mActivity.startActivity(mylcbean);
                    break;
                default:
                    break;
            }
        }
    }
}
