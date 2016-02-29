package cn.ledoing.view;



import cn.ledoing.activity.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <pre>
 * 业务名:
 * 功能说明: 
 * 编写日期:	2014-6-9
 * 编写人员:	 吴培涛
 * 
 * 历史记录
 * 1、修改日期:
 *    修改人:
 *    修改内容:
 * </pre>
 */
public class LCCustomProgressDialog extends Dialog {
	private Context context = null;
	private static LCCustomProgressDialog customProgressDialog = null;

	public LCCustomProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public LCCustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static LCCustomProgressDialog createDialog(Context context) {
		customProgressDialog = new LCCustomProgressDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.lc_customprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.loadingImageView);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getBackground();
		animationDrawable.start();
	}

	/**
	 * 
	 * [Summary] setTitile 标题
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public LCCustomProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage 提示内容
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public LCCustomProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return customProgressDialog;
	}

}
