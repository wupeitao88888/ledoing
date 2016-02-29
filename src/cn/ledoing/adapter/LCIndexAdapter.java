package cn.ledoing.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.ledoing.activity.LCActivityListActivity;
import cn.ledoing.activity.LCActivitySupport;
import cn.ledoing.activity.LCSetUserInfoActivity;
import cn.ledoing.activity.LCUserLoginAndRegister;
import cn.ledoing.activity.MemberActivity;
import cn.ledoing.activity.R;
import cn.ledoing.activity.VideoActivity;
import cn.ledoing.bean.SingleHttpBean;
import cn.ledoing.bean.ThemeGroup;
import cn.ledoing.bean.ThemeType;
import cn.ledoing.bean.VideoAll;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCSharedPreferencesHelper;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCDialog;

/**
 * @author wpt
 *         课程首页适配器
 */
public class LCIndexAdapter extends BaseAdapter {

    // 上下文对象
    private Context mContext;
    private List<ThemeGroup> list;
    private AbHttpUtil mAbHttpUtil = null;
    private LCSharedPreferencesHelper lcSharedPreferencesLogin;
    private Dialog dialog;
    private LCDialog lcdialog;

    public LCIndexAdapter(Context mContext, List<ThemeGroup> strList) {
        this.mContext = mContext;
        this.list = strList;
        lcSharedPreferencesLogin = new LCSharedPreferencesHelper(mContext,
                "isfrist");
        mAbHttpUtil = AbHttpUtil.getInstance(mContext);
        mAbHttpUtil.setTimeout(5000);
    }

    /**
     * wpt
     * 2015/9/8 15
     * 请求视频信息传送到VideoActivity
     */
    protected void netVideoinfo(final ThemeType themeType, final int position, final int index) {
        // TODO Auto-generated method stub
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        if (LCConstant.userinfo != null) {
            params.put("userid", LCConstant.userinfo.getUserid());
        } else {
            params.put("userid", 0 + "");
        }
        params.put("courseid", themeType.getCourseid());
        params.put("page", 1 + "");
        params.put("pagesize", 20 + "");
        params.put("uuid", LCUtils.getOnly(mContext));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.COURSE_TASK_LIST, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        // 移除进度框
                        L.e(content + "-------");
                        VideoAll videoAll = JSONUtils.getInstatce()
                                .getVideoAll(content);
                        if ("0".equals(videoAll.getErrorCode())) {
                            if (videoAll.getList() != null
                                    & videoAll.getList().size() != 0) {
//                                Intent intent = new Intent(mContext,
//                                        VideoActivity.class);
//                                intent.putExtra("themeType", themeType);
//                                intent.putExtra("videoAll", videoAll);
                                Bundle bd = new Bundle();
                                bd.putInt("position", position);
                                bd.putInt("index", index);
                                bd.putInt("isShowPull", 0);
//                                intent.putExtra("bundle", bd);
//                                mContext.startActivity(intent);
                                initIsQustion(themeType, videoAll, bd);
                            } else {
                                AbToastUtil.showToast(mContext, "无视频");
                                LCUtils.ReLogin(videoAll.getErrorCode(),
                                        mContext, videoAll.getErrorMessage());
                            }
                        } else {
                            AbToastUtil.showToast(mContext,
                                    videoAll.getErrorMessage());
                        }
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        LCUtils.startProgressDialog(mContext);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        AbToastUtil.showToast(mContext, "网络异常");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
//                        LCUtils.stopProgressDialog(mContext);

                    }

                });
    }


    /**
     * 获取问题状态
     *
     * @param
     */
    private void initIsQustion(final ThemeType themeType, final VideoAll videoAll, final Bundle bd) {
//        String url = "check-task-question-answer";
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("courseid", themeType.getCourseid());
        params.put("uuid", LCUtils.getOnly(mContext));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.CHECK_TASK_QUAESTION_ANSWER, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {


                        String loadConvert = JSONUtils.getInstatce().loadConvert(content);
                        SingleHttpBean singleHttpBean;
                        ArrayList<String> qList = new ArrayList<String>();

                        try {
                            JSONObject jsonObject = new JSONObject(loadConvert);
                            String errorCode = jsonObject.optString("errorCode", "1");
                            String errorMessage = jsonObject.optString("errorMessage", "");
                            if ("0".equals(errorCode)) {
                                JSONObject jObject = jsonObject.getJSONObject("data");
                                JSONArray ja = jObject.optJSONArray("list");
                                for (int i = 0; i < ja.length(); i++) {
                                    qList.add(ja.get(i).toString());
                                }
                                Intent intent = new Intent(mContext,
                                        VideoActivity.class);
                                intent.putExtra("themeType", themeType);
                                intent.putExtra("videoAll", videoAll);
                                intent.putStringArrayListExtra("questionid", qList);
                                intent.putExtra("bundle", bd);
                                mContext.startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext,
                                        VideoActivity.class);
                                intent.putExtra("themeType", themeType);
                                intent.putExtra("videoAll", videoAll);
                                intent.putStringArrayListExtra("questionid", qList);
                                intent.putExtra("bundle", bd);
                                mContext.startActivity(intent);
                            }
                        } catch (Exception e) {
                            Intent intent = new Intent(mContext,
                                    VideoActivity.class);
                            intent.putExtra("themeType", themeType);
                            intent.putExtra("videoAll", videoAll);
                            intent.putStringArrayListExtra("questionid", qList);
                            intent.putExtra("bundle", bd);
                            mContext.startActivity(intent);
                        }
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
//                        AbDialogUtil.showProgressDialog(mContext, 0, "正在查询...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        AbToastUtil.showToast(mContext, error.getMessage());
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框
                        AbDialogUtil.removeDialog(mContext);

                    }
                });

    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ThemeGroup classList = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.lc_index_item, null);
            vh = new ViewHolder();
            vh.lc_index_content = (RelativeLayout) convertView.findViewById(R.id.lc_index_content);
            vh.lc_index_fiveall = (RelativeLayout) convertView.findViewById(R.id.lc_index_fiveall);
            vh.lc_index_threeall = (RelativeLayout) convertView.findViewById(R.id.lc_index_threeall);
            vh.lc_index_fourall = (RelativeLayout) convertView.findViewById(R.id.lc_index_fourall);
            vh.lc_index_sixall = (RelativeLayout) convertView.findViewById(R.id.lc_index_sixall);
            vh.lc_index_fuceng = (RelativeLayout) convertView.findViewById(R.id.lc_index_fuceng);
            vh.lc_index_groupname = (TextView) convertView.findViewById(R.id.lc_index_groupname);
            vh.lc_index_threename = (TextView) convertView.findViewById(R.id.lc_index_threename);
            vh.lc_index_threeiconname = (TextView) convertView.findViewById(R.id.lc_index_threeiconname);
            vh.lc_index_fouriconname = (TextView) convertView.findViewById(R.id.lc_index_fouriconname);
            vh.lc_index_fourname = (TextView) convertView.findViewById(R.id.lc_index_fourname);
            vh.lc_index_fivename = (TextView) convertView.findViewById(R.id.lc_index_fivename);
            vh.lc_index_fiveiconname = (TextView) convertView.findViewById(R.id.lc_index_fiveiconname);
            vh.lc_index_sixname = (TextView) convertView.findViewById(R.id.lc_index_sixname);
            vh.lc_index_sixiconname = (TextView) convertView.findViewById(R.id.lc_index_sixiconname);
            vh.lc_index_groupimage = (ImageView) convertView.findViewById(R.id.lc_index_groupimage);
            vh.lc_index_threeimage = (ImageView) convertView.findViewById(R.id.lc_index_threeimage);
            vh.lc_index_fourimage = (ImageView) convertView.findViewById(R.id.lc_index_fourimage);
            vh.lc_index_fiveimage = (ImageView) convertView.findViewById(R.id.lc_index_fiveimage);
            vh.lc_index_siximage = (ImageView) convertView.findViewById(R.id.lc_index_siximage);
            vh.lc_content_button = (View) convertView.findViewById(R.id.lc_content_button);
            vh.lc_index_lock = (ImageView) convertView.findViewById(R.id.lc_index_lock);
            vh.order_cource = (Button) convertView.findViewById(R.id.order_course);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        setmText(vh.lc_index_groupname, classList.getName());
        setImageTitle(vh.lc_index_groupimage, classList.getBaseimg());
        try {
            setmText(vh.lc_index_threename, classList.getList().get(0).getCoursename());
            setmText(vh.lc_index_threeiconname, classList.getList().get(0).getAgegroupname());
            setmImage(vh.lc_index_threeimage, classList.getList().get(0).getCourseimg());
            setmText(vh.lc_index_fourname, classList.getList().get(1).getCoursename());
            setmText(vh.lc_index_fouriconname, classList.getList().get(1).getAgegroupname());
            setmImage(vh.lc_index_fourimage, classList.getList().get(1).getCourseimg());
            setmText(vh.lc_index_fivename, classList.getList().get(2).getCoursename());
            setmText(vh.lc_index_fiveiconname, classList.getList().get(2).getAgegroupname());
            setmImage(vh.lc_index_fiveimage, classList.getList().get(2).getCourseimg());
            setmText(vh.lc_index_sixname, classList.getList().get(3).getCoursename());
            setmText(vh.lc_index_sixiconname, classList.getList().get(3).getAgegroupname());
            setmImage(vh.lc_index_siximage, classList.getList().get(3).getCourseimg());
            setmOclick(vh.lc_index_threeall, vh.lc_index_fourall, vh.lc_index_fiveall, vh.lc_index_sixall, classList, position);
            setmColor(vh.lc_index_content, vh.lc_content_button, classList.getMclolor(), vh.lc_index_fuceng);
            vh.order_cource.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!LCConstant.islogin) {
                        Intent intent = new Intent(mContext,
                                LCUserLoginAndRegister.class);
                        intent.putExtra("mIntent", 7);
                        mContext.startActivity(intent);
                    } else {
                        getMyCourse(classList);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setJimu(vh.lc_index_fuceng, classList.getGrouplock(), vh.lc_index_lock);



        /**
         * wpt
         *2015/9/8 15
         *设置回调，如果用户在看videoactivity里购买成功回调次接口
         *
         */
        VideoActivity.setBuyCallBack(new VideoActivity.BuyCallBack() {
            @Override
            public void onSuccess(Bundle bu) {
                mNotifyDate(bu);
            }
        });
        return convertView;
    }

    public boolean isSetUserInfo() {
        String realname = lcSharedPreferencesLogin.getValue("realname");
        String birthday = lcSharedPreferencesLogin.getValue("birthday");
        String sex = lcSharedPreferencesLogin.getValue("sex");
        if (!TextUtils.isEmpty(realname) && !TextUtils.isEmpty(birthday) && !TextUtils.isEmpty(sex)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * wpt
     * 2015/9/8 15
     * 设置购买成功，并且刷新列表
     */
    public void mNotifyDate(Bundle bu) {
        int postion = bu.getInt("position");
        int index = bu.getInt("index");
        list.get(postion).getList().get(index).setIsbuy("1");
        notifyDataSetChanged();
    }

//    private void setJimu(RelativeLayout lc_index_fuceng, String grouplock, ImageView lc_index_lock) {
//        if (TextUtils.isEmpty(grouplock)) {
//            Glide.with(mContext).load(R.drawable.lc_lock).crossFade().into(lc_index_lock);
//            lc_index_fuceng.setVisibility(View.VISIBLE);
//            lc_index_fuceng.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                }
//            });
//            return;
//        }
//        if ("0".equals(grouplock)) {
//            lc_index_fuceng.setVisibility(View.GONE);
//            lc_index_lock.setImageDrawable(null);
//        } else {
//            Glide.with(mContext).load(R.drawable.lc_lock).crossFade().into(lc_index_lock);
//            lc_index_fuceng.setVisibility(View.VISIBLE);
//            lc_index_fuceng.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext,
//                            LCUserLoginAndRegister.class);
//                    intent.putExtra("mIntent", 7);
//                    mContext.startActivity(intent);
//                }
//            });
//        }
//    }

    private void setmColor(RelativeLayout lc_index_content, View lc_content_button, int mColor, RelativeLayout lc_index_fuceng) {
        switch (mColor) {
            case 1:
                lc_content_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.jimu1));
                lc_index_content.setBackgroundColor(mContext.getResources().getColor(R.color.index1));
                lc_index_fuceng.setBackgroundColor(mContext.getResources().getColor(R.color.index91));
                break;
            case 2:
                lc_content_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.jimu2));
                lc_index_content.setBackgroundColor(mContext.getResources().getColor(R.color.index2));
                lc_index_fuceng.setBackgroundColor(mContext.getResources().getColor(R.color.index92));
                break;
            case 3:
                lc_content_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.jimu3));
                lc_index_content.setBackgroundColor(mContext.getResources().getColor(R.color.index3));
                lc_index_fuceng.setBackgroundColor(mContext.getResources().getColor(R.color.index93));
                break;
            case 4:
                lc_content_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.jimu4));
                lc_index_content.setBackgroundColor(mContext.getResources().getColor(R.color.index4));
                lc_index_fuceng.setBackgroundColor(mContext.getResources().getColor(R.color.index94));
                break;
            case 5:
                lc_content_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.jimu5));
                lc_index_content.setBackgroundColor(mContext.getResources().getColor(R.color.index5));
                lc_index_fuceng.setBackgroundColor(mContext.getResources().getColor(R.color.index95));
                break;
            case 6:
                lc_content_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.jimu6));
                lc_index_content.setBackgroundColor(mContext.getResources().getColor(R.color.index6));
                lc_index_fuceng.setBackgroundColor(mContext.getResources().getColor(R.color.index96));
                break;

            case 7:
                lc_content_button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.jimu7));
                lc_index_content.setBackgroundColor(mContext.getResources().getColor(R.color.index7));
                lc_index_fuceng.setBackgroundColor(mContext.getResources().getColor(R.color.index97));
                break;
        }

    }


    private void setmOclick(RelativeLayout lc_index_threeall, RelativeLayout lc_index_fourall, RelativeLayout lc_index_fiveall, RelativeLayout lc_index_sixall, final ThemeGroup classList, final int position) {
        lc_index_threeall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netVideoinfo(classList.getList().get(0), position, 0);
//
            }
        });
        lc_index_fourall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netVideoinfo(classList.getList().get(1), position, 1);

            }
        });
        lc_index_fiveall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netVideoinfo(classList.getList().get(2), position, 2);
            }
        });
        lc_index_sixall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                netVideoinfo(classList.getList().get(3), position, 3);
            }
        });
    }


    private void setImageTitle(ImageView lc_index_threeimage, String courseimg) {

        if (!TextUtils.isEmpty(courseimg)) {
            Glide.with(mContext)
                    .load(courseimg)
                    .override(1080, 460)
                    .centerCrop()
//                    .placeholder(R.drawable.image_loading)
//                    .error(R.drawable.image_error)
                    .crossFade()
                    .into(lc_index_threeimage);
        } else {

            lc_index_threeimage.setImageResource(R.drawable.white_title);
        }
    }

    private void setmImage(ImageView lc_index_threeimage, String courseimg) {
        if (!TextUtils.isEmpty(courseimg)) {
            Glide.with(mContext)
                    .load(courseimg)
                    .override(200, 200)
                    .centerCrop()
//                    .placeholder(R.drawable.image_loading)
//                    .error(R.drawable.image_error)
                    .crossFade()
                    .into(lc_index_threeimage);
        } else {
            lc_index_threeimage.setImageResource(R.drawable.white_title);
        }
    }


    private void setmText(TextView lc_index_groupname, String name) {
        if (lc_index_groupname == null) {
            return;
        }
        if (!TextUtils.isEmpty(name)) {
            lc_index_groupname.setText(name);
        } else {
            lc_index_groupname.setText("");
        }
    }

    private void getMyCourse(final ThemeGroup classList) {
//        AbRequestParams params = new AbRequestParams();
//        params.put("platform", "android");
//        params.put("time", LCUtils.gettime().substring(0, 10));
//        params.put("uuid", LCUtils.getOnly(mContext));
//        String url = "/api/v2/re-get-user-hours";
//        mAbHttpUtil.post(LCConstant.URL + url, params,
//                new AbStringHttpResponseListener() {
//                    @Override
//                    public void onStart() {
//                        LCUtils.startProgressDialog(mContext);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        LCUtils.stopProgressDialog(mContext);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, String content, Throwable error) {
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, String content) {
//                        L.e("=======4.1====statusCode==" + statusCode + "================" + content);
//                        SingleHttpBean singlehttp = JSONUtils.getInstatce().getUserHoursOver(content, mContext);
//                        if ("1".equals(singlehttp.getIs_mumber())) {//是会员
                            Intent intent = new Intent();
                            if (isSetUserInfo()) {
                                LCActivitySupport.categoryid = classList.getCategoryid();
                                LCActivitySupport.categoryname = classList.getName();
                                intent.setClass(mContext, LCActivityListActivity.class);
                            } else {
                                intent.setClass(mContext, LCSetUserInfoActivity.class);
                            }
                            mContext.startActivity(intent);
//                        } else {//否则不是会员
//
//                            lcdialog= AbDialogUtil.showDialogTwoButton(mContext, null, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(mContext, MemberActivity.class);
//                                    mContext.startActivity(intent);
//                                    /**
//                                     * 2015 10/22 添加取消对话框
//                                     */
//                                    lcdialog.cancel();
//                                }
//                            }, "您还不是会员，请充值成为会员！", "取消", "确认");
//                        }
//
//                    }
//                });

    }


    public class ViewHolder {

        public RelativeLayout lc_index_threeall, lc_index_fourall, lc_index_fiveall, lc_index_sixall, lc_index_content, lc_index_fuceng;
        public TextView lc_index_groupname, lc_index_threename, lc_index_threeiconname, lc_index_fourname, lc_index_fouriconname,
                lc_index_fivename, lc_index_fiveiconname, lc_index_sixname, lc_index_sixiconname;
        private ImageView lc_index_groupimage, lc_index_threeimage, lc_index_fourimage, lc_index_fiveimage, lc_index_siximage, lc_index_lock;
        private View lc_content_button;
        private Button order_cource;
    }
}
