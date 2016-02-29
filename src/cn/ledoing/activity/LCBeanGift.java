package cn.ledoing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
public class LCBeanGift extends LCActivitySupport {

    private ProgressBar pb;
    private WebView wvContent;
    private AbHttpUtil mAbHttpUtil;
    private LCNoNetWork lc_mebean_nonet;
    private String ua;
    private TextView lc_center_menu;
    private LCTitleBar lc_mebean_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bean_gift_layout);
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setTimeout(5000);
        initView();
    }

    private void initView() {
        pb = (ProgressBar) findViewById(R.id.progressbar);
        lc_mebean_title = (LCTitleBar) findViewById(R.id.lc_mebean_title);
        wvContent = (WebView) findViewById(R.id.webview);
        lc_mebean_nonet = (LCNoNetWork)findViewById(R.id.lc_mebean_nonet);
        lc_center_menu = (TextView) findViewById(R.id.title);
        lc_mebean_title.setOnclickBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        getLeBeanBalance();
    }

    public void getLeBeanBalance() {
        AbRequestParams params = new AbRequestParams();
        params.put("uuid", LCUtils.getOnly(this));
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API + LCConstant.GET_DUIBA_URL, params,
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
                        isNoNet();
                    }

                    @Override
                    public void onSuccess(int statusCode, String content) {
                        String url = JSONUtils.getInstatce().getLeBeanGiftOver(content);
                        loadWebView(url);
                    }
                });
    }

    private void isNoNet() {
        // TODO Auto-generated method stub
        if (LCUtils.isNetworkAvailable(this)) {
            setNotNetBack();
        } else {
            setNotNet();
        }
    }

    public void setNotNetBack() {
        lc_mebean_nonet.setVisibility(View.GONE);
        wvContent.setVisibility(View.VISIBLE);
    }

    public void setNotNet() {
        lc_mebean_nonet.setVisibility(View.VISIBLE);
        wvContent.setVisibility(View.GONE);
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
                lc_center_menu.setText(title);
            }
        });
        if( ua == null ){
            ua = wvContent.getSettings().getUserAgentString()+" Duiba/1.0.5";
        }
        wvContent.getSettings().setUserAgentString(ua);
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
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode ==  event.KEYCODE_BACK){
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyUp(keyCode, event);

    }


}
