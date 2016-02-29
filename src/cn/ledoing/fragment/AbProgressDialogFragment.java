package cn.ledoing.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ledoing.activity.R;
import cn.ledoing.view.LCDialog;

/**
 * © 2012 amsoft.cn
 * 名称：AbLoadDialogFragment.java 
 * 描述：弹出的进度框
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-29 上午9:00:52
 */
public class AbProgressDialogFragment extends DialogFragment {
	
	int mIndeterminateDrawable;
	String mMessage;
	static View mContentView;
	
	/**
	 * Create a new instance of AbProgressDialogFragment.
	 */
	public static AbProgressDialogFragment newInstance(int indeterminateDrawable,String message) {
		AbProgressDialogFragment f = new AbProgressDialogFragment();
		Bundle args = new Bundle();
		args.putInt("indeterminateDrawable", indeterminateDrawable);
		args.putString("message", message);
		f.setArguments(args);

		return f;
	}
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIndeterminateDrawable = getArguments().getInt("indeterminateDrawable");
		mMessage = getArguments().getString("message");
		View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.progress_dialog,
				null);
		LCDialog dialog = new LCDialog(getActivity(), R.style.PgDialog, dialogView);

	    return dialog;
	}
}
