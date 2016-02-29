package cn.ledoing.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import cn.ledoing.bean.NoVIP;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDialogUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCDialog;
import cn.ledoing.view.LCTitleBar;

/**
 * Created by lc-php1 on 2015/9/24.
 */
public class MemberActivity extends LCActivitySupport {

    private LCTitleBar lcTitleBar;
    //    private TextView lc_vip_phone;
    private TextView no_vip_text;
    private TextView center_contact;
    private TextView center_traffic;
    private TextView center_time;
    private TextView center_name;
    private ImageView novip;
    private ImageView explain;
    private RelativeLayout in_center;
    private AbHttpUtil mAbHttpUtil = null;
    private String ins_id;
    private boolean ISVIP = false;
    private NoVIP.Result data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_layout);
        lcTitleBar = (LCTitleBar) findViewById(R.id.lc_coures_title);
        lcTitleBar.setCenterTitle("会员&充值");
        // 获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        Intent intent = getIntent();
        ins_id = intent.getStringExtra("ins_id");

//        lc_vip_phone = (TextView) findViewById(R.id.lc_vip_phone);
//        lc_vip_phone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //传入服务， parse（）解析号码
//                dialog = showDialogTwoButton(null, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-0068-617"));
//                        //通知activtity处理传入的call服务
//                        startActivity(intent);
//                        dialog.cancel();
//                    }
//                }, "确定拨打：400-0068-617", "拨打", "取消");
//            }
//        });
        no_vip_text = (TextView) findViewById(R.id.no_vip_text);
        center_contact = (TextView) findViewById(R.id.center_contact);
        center_traffic = (TextView) findViewById(R.id.center_traffic);
        center_time = (TextView) findViewById(R.id.center_time);
        center_name = (TextView) findViewById(R.id.center_name);
        no_vip_text = (TextView) findViewById(R.id.no_vip_text);
        novip = (ImageView) findViewById(R.id.novip);
        explain = (ImageView) findViewById(R.id.explain);
        in_center = (RelativeLayout) findViewById(R.id.in_center);

        if (!ISVIP) {
            Glide.with(context)
                    .load(R.drawable.no_vip)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .crossFade()
                    .into(novip);
            Glide.with(context)
                    .load(R.drawable.explain)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .crossFade()
                    .into(explain);
        } else {
            Glide.with(context)
                    .load(R.drawable.no_meney)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .crossFade()
                    .into(novip);
            Glide.with(context)
                    .load(R.drawable.top_up)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .crossFade()
                    .into(explain);
        }

        in_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HomeCenterDetalActivity.class);
                intent.putExtra("center_id",data.getIns_id());
                startActivity(intent);
            }
        });
        net();
    }


    private void net() {
        // TODO Auto-generated method stub
        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("insid", ins_id);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.INFORMETION_CENTER, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("=======4.4====statusCode==" + statusCode + "================" + loadConvert(content));
                        NoVIP noVIP = JSON.parseObject(content, NoVIP.class);
                        if ("0".equals(noVIP.getErrorCode())) {
                            data = noVIP.getData();
                            if (!ISVIP) {
                                String con="您还不是<font color=#4A90E2>"+data.getIns_name()+"</font>的会员";
                                no_vip_text.setText(Html.fromHtml(con));
                            }else{
                                String con="您在<font color=#4A90E2>"+data.getIns_name()+"</font>的余额不足";
                                no_vip_text.setText(Html.fromHtml(con));
                            }
                            LCUtils.setTitle(center_contact,data.getContact_info());
                            LCUtils.setTitle(center_traffic,data.getIns_addr());
                            LCUtils.setTitle(center_time,data.getBusiness_hours());
                            LCUtils.setTitle(center_name,data.getIns_name());
                        } else {
                            LCUtils.ReLogin(noVIP.getErrorCode(),context,noVIP.getErrorMessage());
                        }

                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
//                        AbDialogUtil.showProgressDialog(context, );
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.4====statusCode==" + statusCode + "================" + content);
                        AbToastUtil.showToast(context, error.getMessage());
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {

                        // 移除进度框
//                        AbDialogUtil.removeDialog(context);
                    }

                    ;

                });
    }
}
