package cn.ledoing.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ActionBar.LayoutParams;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import cn.ledoing.bean.BaseBean;
import cn.ledoing.bean.Institution;
import cn.ledoing.bean.StudentComment;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCRatingBarFace;
import cn.ledoing.view.LCTitleBar;

/**
 * Created by lc-php1 on 2015/11/2.
 */
public class StudentCommentActivity extends LCActivitySupport implements OnClickListener{

    private TextView address_text;
    private TextView time_text;
    private TextView them_text;
    private LCRatingBarFace center_ratingbar;
    private TextView fan_text;
    private LCRatingBarFace teacher_ratingbar;
    private TextView teacher_name;
    private TextView teacher_fan_text;
    private EditText edit_text;
    private TextView ld_text;
    private Button submit_btn;
    private LCTitleBar lc_comment_title;
    private ImageView star1;
    private AbHttpUtil mAbHttpUtil;
    private String hour_record_id;
    private String state;
    private StudentComment studentComment;
    private LinearLayout ass_teacher_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_comment_layout);
        initView();
        Intent intent = getIntent();
        hour_record_id = intent.getStringExtra("hour_record_id");
        state = intent.getStringExtra("state");
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        if(state.equals("2")){
            getCommentData();
        }else if(state.equals("3")){
            getCommentDataShow();
        }
    }

    private void initView() {

        lc_comment_title = (LCTitleBar) findViewById(R.id.lc_comment_title);
        lc_comment_title.setCenterTitle("学员评价");

        address_text = (TextView) findViewById(R.id.address_text);
        time_text = (TextView) findViewById(R.id.time_text);
        them_text = (TextView) findViewById(R.id.them_text);

        center_ratingbar = (LCRatingBarFace) findViewById(R.id.center_ratingbar);
        fan_text = (TextView) findViewById(R.id.fan_text);
        center_ratingbar.setOnStarClickListener(new LCRatingBarFace.OnStarClickListaner() {
            @Override
            public void getStarNum(int num) {
                fan_text.setText(setMartFan(num+""));
            }
        });



        teacher_ratingbar = (LCRatingBarFace) findViewById(R.id.teacher_ratingbar);
        teacher_name = (TextView) findViewById(R.id.teacher_name);
        teacher_fan_text = (TextView) findViewById(R.id.teacher_fan_text);
        teacher_ratingbar.setOnStarClickListener(new LCRatingBarFace.OnStarClickListaner() {
            @Override
            public void getStarNum(int num) {
                teacher_fan_text.setText(setMartFan(num + ""));
            }
        });

        edit_text = (EditText) findViewById(R.id.edit_text);

        ass_teacher_rl = (LinearLayout) findViewById(R.id.ass_teacher_rl);

        ld_text = (TextView) findViewById(R.id.ld_text);
        ld_text.setText("评价后会获得10乐豆");
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edit_text.getText().toString())) {
                    AbToastUtil.showToast(StudentCommentActivity.this, "评价内容不能为空");
                    return;
                }
                if ("3".equals(state)) {
                    AbToastUtil.showToast(StudentCommentActivity.this, "已评价，不能重复评价");
                    return;
                }
                if(null == studentComment){
                    AbToastUtil.showToast(StudentCommentActivity.this, "数据加载中...");
                    return;
                }
                getCommentState();
                submit_btn.setEnabled(false);
            }
        });

    }

    private View addView(StudentComment.Date.Zhujiao zhujiao) {
        // TODO 动态添加布局(xml方式)
        View view = null;
        try {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            LayoutInflater inflater3 = LayoutInflater.from(this);
            view = inflater3.inflate(R.layout.student_comment_item_layout, null);
            TextView ass_teacher_name = (TextView) view.findViewById(R.id.ass_teacher_name);
            LCRatingBarFace ass_teacher_ratingbar = (LCRatingBarFace) view.findViewById(R.id.ass_teacher_ratingbar);
            final TextView ass_teacher_fan_text = (TextView) view.findViewById(R.id.ass_teacher_fan_text);
            ass_teacher_name.setText(zhujiao.getTeacher_name());
            ass_teacher_ratingbar.setOnStarClickListener(new LCRatingBarFace.OnStarClickListaner() {
                @Override
                public void getStarNum(int num) {
                    ass_teacher_fan_text.setText(setMartFan(num + ""));
                }
            });
            ass_teacher_ratingbar.setMark(Float.parseFloat(zhujiao.getTeacher_score()));
            ass_teacher_fan_text.setText(setMartFan(zhujiao.getTeacher_score()));
            view.setLayoutParams(lp);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return view;

    }

    private String setMartFan(String mark){
        String marts = "";
        switch (mark){
            case "0":
                marts = "未评";
                return marts;
            case "1":
                marts = "极差";
                return marts;
            case "2":
                marts = "失望";
                return marts;
            case "3":
                marts = "一般";
                return marts;
            case "4":
                marts = "满意";
                return marts;
            case "5":
                marts = "惊喜";
                return marts;
        }
        return marts;
    }

    @Override
    public void onClick(View v) {

    }

    private void getCommentState(){
        String assistant_teacher = "";
        if(studentComment.getData().getZhujiao().size()>0){
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("[");
            for(int i =0;i<studentComment.getData().getZhujiao().size();i++){
                stringBuffer.append("{\"teacher_id\":").append(studentComment.getData().getZhujiao().get(i).getTeacher_id()).append(",\"score\":")
                        .append(studentComment.getData().getZhujiao().get(i).getTeacher_score()).append("},");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append("]");
            assistant_teacher = stringBuffer.toString();
            L.e("-------------------"+assistant_teacher);
        }
        AbRequestParams params = new AbRequestParams();
        params.put("hour_record_id", hour_record_id);
        params.put("ins_evaluate_score", center_ratingbar.getMark()+"");
        params.put("master_teacher_id", studentComment.getData().getTeacher_id());
        params.put("master_teacher_score", teacher_ratingbar.getMark()+"");
        L.e(teacher_ratingbar.getMark()+"1111111111111");
        params.put("master_teacher_comment", edit_text.getText().toString());
        params.put("assistant_teacher_evaluate", assistant_teacher);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.STUDENT_EVALUATE, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context);
                    }

                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                        submit_btn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                           showToast(content);
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        BaseBean baseBean = JSON.parseObject(content,BaseBean.class);
                        if(null!=baseBean){
                            if("0".equals(baseBean.getErrorCode())){
                                showToast("评价成功");
                                finish();
                            }else{
                                showToast(baseBean.getErrorMessage());
                            }
                        }
                        L.e("--------------------"+content);
                    }
                });
    }

    private void getCommentData(){
        AbRequestParams params = new AbRequestParams();
        params.put("hourid", hour_record_id);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.GET_COMMENT_RECORD_SHOW, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context);
                    }

                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        L.e("--------------------"+content+"   statusCode  "+statusCode);
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("======"+content);
                        studentComment = JSON.parseObject(content, StudentComment.class);
                        if( null != studentComment ){
                            try {
                                if("0".equals(studentComment.getErrorCode())){
                                    CommentDataOver(studentComment);
                                } else {
                                    Toast.makeText(StudentCommentActivity.this, studentComment.getErrorMessage()+"", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(StudentCommentActivity.this,studentComment.getErrorMessage()+"",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(StudentCommentActivity.this,content+"",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 评价解析
     * @param Comment
     */
    private void CommentDataOver(final StudentComment Comment) {
        address_text.setText("地点:"+Comment.getData().getIns_name());
        time_text.setText("时间:"+Comment.getData().getStart_time());
        them_text.setText("主题:"+Comment.getData().getBase_name());
        teacher_name.setText(Comment.getData().getTeacher_name());
        center_ratingbar.setOnClic();
        teacher_ratingbar.setOnClic();
        for(int i=0;i<Comment.getData().getZhujiao().size();i++){
            final int j = i;
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
            LayoutInflater inflater3 = LayoutInflater.from(StudentCommentActivity.this);
            View view = inflater3.inflate(R.layout.student_comment_item_layout, null);
            TextView ass_teacher_name = (TextView) view.findViewById(R.id.ass_teacher_name);
            LCRatingBarFace ass_teacher_ratingbar = (LCRatingBarFace) view.findViewById(R.id.ass_teacher_ratingbar);
            final TextView ass_teacher_fan_text = (TextView) view.findViewById(R.id.ass_teacher_fan_text);
            ass_teacher_name.setText(Comment.getData().getZhujiao().get(i).getTeacher_name());
            ass_teacher_ratingbar.setOnStarClickListener(new LCRatingBarFace.OnStarClickListaner() {
                @Override
                public void getStarNum(int num) {
                    ass_teacher_fan_text.setText(setMartFan(num + ""));
                    Comment.getData().getZhujiao().get(j).setTeacher_score(num + "");
                }
            });
            ass_teacher_ratingbar.setOnClic();
            view.setLayoutParams(lp);
            ass_teacher_rl.addView(view);
        }
    }

    private void getCommentDataShow(){
        AbRequestParams params = new AbRequestParams();
        params.put("hour_record_id", hour_record_id);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.GET_COMMENT_EVALUATE_INFO, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context);
                    }

                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        L.e("--------------------"+content+"   statusCode  "+statusCode);
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("======" + content);
                        studentComment = JSON.parseObject(content, StudentComment.class);
                        if (null != studentComment) {
                            try {
                                if ("0".equals(studentComment.getErrorCode())) {
                                    CommentDataShowOver(studentComment);
                                } else {
                                    Toast.makeText(StudentCommentActivity.this, studentComment.getErrorMessage() + "", Toast.LENGTH_SHORT).show();
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(StudentCommentActivity.this, studentComment.getErrorMessage() + "", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(StudentCommentActivity.this, content + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void CommentDataShowOver(StudentComment Comment) {
        address_text.setText("地点:" + Comment.getData().getIns_name());
        time_text.setText("时间:" + Comment.getData().getStart_time());
        them_text.setText("主题:" + Comment.getData().getBase_name());
        teacher_name.setText(Comment.getData().getTeacher_name());
        center_ratingbar.setMark(Float.parseFloat(Comment.getData().getIns_score()));
        teacher_ratingbar.setMark(Float.parseFloat(Comment.getData().getTeacher_score()));
        fan_text.setText(setMartFan(Comment.getData().getIns_score()));
        teacher_fan_text.setText(setMartFan(Comment.getData().getTeacher_score()));
        edit_text.setFocusable(false);
        edit_text.setEnabled(false);
        edit_text.setText(Comment.getData().getComment());
        for (int i = 0; i < Comment.getData().getZhujiao().size(); i++) {
            ass_teacher_rl.addView(addView(Comment.getData().getZhujiao().get(i)));
        }
    }
}
