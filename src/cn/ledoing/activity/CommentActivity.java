package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import cn.ledoing.bean.BaseBean;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCRatingBarFace;
import cn.ledoing.view.LCTitleBar;

/**
 * Created by lc-php1 on 2015/11/19.
 */
public class CommentActivity extends LCActivitySupport implements TextWatcher{

    private LCRatingBarFace rating_face;
    private TextView status_pl;
    private EditText exit_content;
    private Button commit_btn;
    private AbHttpUtil mAbHttpUtil;
    private String ins_id;
    private LCTitleBar lc_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        ins_id = getIntent().getStringExtra("ins_id");
        initView();
    }

    private void initView() {
        rating_face = (LCRatingBarFace) findViewById(R.id.rating_face);
        lc_title = (LCTitleBar) findViewById(R.id.lc_title);
        rating_face.setOnClic();
        lc_title.setCenterTitle("点评中心");
        status_pl = (TextView) findViewById(R.id.status_pl);
        exit_content = (EditText) findViewById(R.id.exit_content);
        commit_btn = (Button) findViewById(R.id.commit_btn);
        commit_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(exit_content.getText().toString())){
                    Toast.makeText(CommentActivity.this,"请填写评价内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(rating_face.getMark()+"")){
                    Toast.makeText(CommentActivity.this,"请选择评分",Toast.LENGTH_SHORT).show();
                    return;
                }
                commit_btn.setEnabled(false);
                getInstitution();
            }
        });
        exit_content.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void getInstitution(){
        AbRequestParams params = new AbRequestParams();
        params.put("score", rating_face.getMark()+"");
        params.put("content", exit_content.getText().toString());
        params.put("insid", ins_id);
        Log.e("======ins_id=======", ins_id);
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.EVALUATION_INS, params,
                new AbStringHttpResponseListener() {

                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(CommentActivity.this);
                    }

                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(CommentActivity.this);
                        commit_btn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        //                        showToast("网络连接失败");
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        BaseBean basestring = JSON.parseObject(content,BaseBean.class);
                        if(basestring!=null){
                            if("0".equals(basestring.getErrorCode())){
                                Toast.makeText(CommentActivity.this,"点评成功",Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK, getIntent());
                                finish();
                            }else{
                                Toast.makeText(CommentActivity.this,basestring.getErrorMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }else{
                            Toast.makeText(CommentActivity.this,"点评失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
