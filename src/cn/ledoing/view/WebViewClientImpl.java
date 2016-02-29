package cn.ledoing.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import cn.ledoing.utils.AbToastUtil;

public class WebViewClientImpl extends WebViewClient {

	ProgressBar pb;
	Context context;

	public WebViewClientImpl(ProgressBar pb,Context context) {
		this.pb = pb;
		this.context = context;
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		pb.setVisibility(View.GONE);
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		try {
			AbToastUtil.showToast(context,"网页加载出错");
//			alertDialog.setTitle("ERROR");
//			alertDialog.setMessage(description);
//			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface dialog, int which) {
//					alertDialog.dismiss();
//				}
//			});
//			alertDialog.show();
			pb.setVisibility(View.GONE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
