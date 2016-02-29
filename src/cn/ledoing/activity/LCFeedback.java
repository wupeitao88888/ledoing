package cn.ledoing.activity;

import android.content.Context;
import android.hardware.input.InputManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ledoing.bean.SingleHttpBean;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpResponseListener;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;

/**
 * wpt
 * 2015/9/15  14:23
 * 意见反馈
 */

public class LCFeedback extends LCActivitySupport {
    private LCTitleBar lc_feedback_title;
    private EditText lc_feedback_idea,//意见
            lc_feedback_ideaAuthor; //手机号
    private TextView lc_feedback_residueIdea;//剩余个数
    private Button lc_feedback_submint;
    private AbHttpUtil mAbHttpUtil = null;
    private RelativeLayout lc_feedback_goodIdea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lc_feedback);
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        initView();
    }

    private void initView() {
        lc_feedback_title = (LCTitleBar) findViewById(R.id.lc_feedback_title);
        lc_feedback_title.setCenterTitle(mString(R.string.feedback));
//        lc_feedback_title.setRightTitle(mString(R.string.submit));
//        lc_feedback_title.setRightTitleListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showToast("成功");
//                closeInput();
//            }
//        });
        lc_feedback_title.setOnclickBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInput();
                finish();
            }
        });
        lc_feedback_goodIdea=(RelativeLayout)findViewById(R.id.lc_feedback_goodIdea);
        lc_feedback_goodIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lc_feedback_idea.requestFocus();
                InputMethodManager imm=(InputMethodManager)lc_feedback_idea.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
            }
        });
        lc_feedback_idea = (EditText) findViewById(R.id.lc_feedback_idea);
        lc_feedback_ideaAuthor = (EditText) findViewById(R.id.lc_feedback_ideaAuthor);
        lc_feedback_residueIdea = (TextView) findViewById(R.id.lc_feedback_residueIdea);
        lc_feedback_submint = (Button) findViewById(R.id.lc_feedback_submint);
        lc_feedback_idea.addTextChangedListener(mTextWatcher);
        lc_feedback_submint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback(lc_feedback_idea.getText().toString(), lc_feedback_ideaAuthor.getText().toString());
            }
        });
        lc_feedback_submint.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_select));
        lc_feedback_submint.setEnabled(false);
    }


    public void sendFeedback(String idea, String ideaPhone) {
        AbRequestParams params = new AbRequestParams();
        params.put("reback", idea);
        params.put("phone_number", ideaPhone);
        params.put("model", android.os.Build.MODEL.toLowerCase().replace(" ", ""));
        params.put("sys_version", android.os.Build.VERSION.RELEASE.toLowerCase().replace(" ", ""));
        mAbHttpUtil.postUrl(LCConstant.RE_USER_REBACK, params, new AbStringHttpResponseListener() {
            @Override
            public void onStart() {
                LCUtils.startProgressDialog(context, mString(R.string.submiting));
            }

            @Override
            public void onFinish() {
                LCUtils.stopProgressDialog(context);
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                showToast(content);
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                SingleHttpBean userHoursOver = JSONUtils.getInstatce().getUserFeedBack(content, context);
                if (userHoursOver != null) {
                    if ("0".equals(userHoursOver.getErrorCode())) {
                        showToast(R.string.thank_you_idea);
                        finish();
                    } else {
                        showToast(userHoursOver.getErrorMessage());
                    }
                }
            }
        });
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            lc_feedback_residueIdea.setText("还可以输入" + (Integer) (200 - temp.length()) + "字");
            if (temp.length() > 0) {
                lc_feedback_submint.setBackgroundDrawable(getResources().getDrawable(R.drawable.blue_btn_selector));
                lc_feedback_submint.setEnabled(true);
            } else {
                lc_feedback_submint.setBackgroundDrawable(getResources().getDrawable(R.drawable.no_select));
                lc_feedback_submint.setEnabled(false);
            }
        }
    };


}
