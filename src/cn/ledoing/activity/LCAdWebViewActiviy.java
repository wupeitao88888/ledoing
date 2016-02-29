package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCNoNetWork;
import cn.ledoing.view.LCTitleBar;
import cn.ledoing.view.WebViewClientImpl;

/**
 * Created by lvfl on 2015/9/10.
 */
public class LCAdWebViewActiviy extends LCActivitySupport implements View.OnClickListener {

    private ProgressBar pb;
    private WebView wvContent;
    private AbHttpUtil mAbHttpUtil;
    private LCTitleBar lc_mebean_title;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ab_layout);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        initView();
    }

    private void initView() {
        pb = (ProgressBar) findViewById(R.id.progressbar);
        lc_mebean_title = (LCTitleBar) findViewById(R.id.lc_mebean_title);
        wvContent = (WebView) findViewById(R.id.webview);
        lc_mebean_title.setOnclickBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        LinearLayout original_bottom_ll = (LinearLayout) findViewById(R.id.original_bottom_ll);
        for (int i = 0; i < original_bottom_ll.getChildCount(); i++) {
            original_bottom_ll.getChildAt(i).setOnClickListener(this);
        }
        loadWebView(url);
    }

    private void loadWebView( String url ){
        wvContent.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                // Activity和Webview根据加载程度决定进度条的进度大小
                // 当加载到100%的时候 进度条自动消失
                pb.setProgress(progress);
                if (progress == 100)
                    pb.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                lc_mebean_title.setCenterTitle(title);
            }
        });
        wvContent.loadUrl(url);
        wvContent.getSettings().setJavaScriptEnabled(true);
        wvContent.getSettings().setSupportZoom(true);
        wvContent.getSettings().setBuiltInZoomControls(true);
        wvContent.getSettings().setUseWideViewPort(true);
        wvContent.getSettings().setLoadWithOverviewMode(true);
        // 设置视图客户端
        wvContent
                .setWebViewClient(new WebViewClientImpl(pb, this));
    }






    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.original_close_ll:
                this.finish();

                break;
            case R.id.original_refresh_ll:
                pb.setProgress(0);
                pb.setVisibility(View.VISIBLE);
                wvContent.loadUrl(url);
                break;
            case R.id.original_last_ll:
                if (wvContent.canGoBack()) {
                    wvContent.goBack();
                }else{
                    Toast.makeText(this, "已经是第一页了", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.original_next_ll:
                if (wvContent.canGoForward()) {
                    wvContent.goForward();
                }else{
                   Toast.makeText(this, "已经是最后一页了", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
