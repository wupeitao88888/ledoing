package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ledoing.view.LCTitleBar;
import cn.ledoing.view.album.MultiImageSelectorActivity;

/**
 * 发帖
 * Created by wupeitao on 15/12/2.
 */
public class Posts extends LCActivitySupport implements View.OnClickListener {
    private LCTitleBar posts_titlebar;
    private EditText post_title;//帖子标题
    private EditText post_content;//帖子内容
    private ImageView media;//相机相册
    private TextView post_lenth;//帖子长度
    private static final int REQUEST_IMAGE = 2;

    private TextView mResultText;
    private RadioGroup mChoiceMode, mShowCamera;
    private EditText mRequestNum;

    private ArrayList<String> mSelectPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_posts);
        posts_titlebar=(LCTitleBar)findViewById(R.id.posts_titlebar);
        posts_titlebar.setCenterTitle("发表帖子");
        post_title=(EditText)findViewById(R.id.post_title);
        post_content=(EditText)findViewById(R.id.post_content);
        media=(ImageView)findViewById(R.id.media);
        post_lenth=(TextView)findViewById(R.id.post_lenth);
        post_content.addTextChangedListener(mTextWatcher);
        media.setOnClickListener(this);
    }
    TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
            post_lenth.setText(s.length()+ "/140");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                StringBuilder sb = new StringBuilder();
                for(String p: mSelectPath){
                    sb.append(p);
                    sb.append("\n");
                }
                mResultText.setText(sb.toString());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.media:
                int selectedMode = MultiImageSelectorActivity.MODE_MULTI;

//                if(mChoiceMode.getCheckedRadioButtonId() == R.id.single){
//                    selectedMode = MultiImageSelectorActivity.MODE_SINGLE;//单选
//                }else{
//                    selectedMode = MultiImageSelectorActivity.MODE_MULTI;//多选
//                }

                boolean showCamera = true;

                int maxNum = 9;//多选的时候的最大值

                Intent intent = new Intent(Posts.this, MultiImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, showCamera);
                // 最大可选择图片数量
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
                // 选择模式
                intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
                // 默认选择
                if(mSelectPath != null && mSelectPath.size()>0){
                    intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }
                startActivityForResult(intent, REQUEST_IMAGE);
                break;
        }
    }
}
