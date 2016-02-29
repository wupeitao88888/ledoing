package cn.ledoing.utils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FragmentEvent extends Fragment {

	public interface OnEventListener {
		public void onEvent(int what, Bundle data, Object object);
	}

	protected OnEventListener onEventListener = null;

	public FragmentEvent() {
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onEventListener = (OnEventListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ "must implement " + OnEventListener.class);
		}
	}
}