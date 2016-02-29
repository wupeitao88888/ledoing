package cn.ledoing.fragment;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.app.SharedElementCallback;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.Transition;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

/**
 * © 2012 amsoft.cn 名称：AbLoadDialogFragment.java 描述：弹出加载框
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-30 下午16:00:52
 */
@SuppressLint("NewApi")
public class AbFragmentTwo extends Fragment {

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	}

	@Override
	public void setInitialSavedState(SavedState state) {
		// TODO Auto-generated method stub
		super.setInitialSavedState(state);
	}

	@Override
	public void setTargetFragment(Fragment fragment, int requestCode) {
		// TODO Auto-generated method stub
		super.setTargetFragment(fragment, requestCode);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
	}

	@Override
	public void setRetainInstance(boolean retain) {
		// TODO Auto-generated method stub
		super.setRetainInstance(retain);
	}

	@Override
	public void setHasOptionsMenu(boolean hasMenu) {
		// TODO Auto-generated method stub
		super.setHasOptionsMenu(hasMenu);
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public boolean getUserVisibleHint() {
		// TODO Auto-generated method stub
		return super.getUserVisibleHint();
	}

	@Override
	public LoaderManager getLoaderManager() {
		// TODO Auto-generated method stub
		return super.getLoaderManager();
	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		super.startActivity(intent);
	}

	@Override
	public void startActivity(Intent intent, Bundle options) {
		// TODO Auto-generated method stub
		super.startActivity(intent, options);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode,
			Bundle options) {
		// TODO Auto-generated method stub
		super.startActivityForResult(intent, requestCode, options);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onInflate(AttributeSet attrs, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInflate(attrs, savedInstanceState);
	}

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onInflate(activity, attrs, savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
		return super.onCreateAnimator(transit, enter, nextAnim);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return super.getView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onDestroyOptionsMenu() {
		// TODO Auto-generated method stub
		super.onDestroyOptionsMenu();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// TODO Auto-generated method stub
		super.onOptionsMenuClosed(menu);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public void registerForContextMenu(View view) {
		// TODO Auto-generated method stub
		super.registerForContextMenu(view);
	}

	@Override
	public void unregisterForContextMenu(View view) {
		// TODO Auto-generated method stub
		super.unregisterForContextMenu(view);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}

	@Override
	public void setEnterSharedElementCallback(SharedElementCallback callback) {
		// TODO Auto-generated method stub
		super.setEnterSharedElementCallback(callback);
	}

	@Override
	public void setExitSharedElementCallback(SharedElementCallback callback) {
		// TODO Auto-generated method stub
		super.setExitSharedElementCallback(callback);
	}

	@Override
	public void setEnterTransition(Transition transition) {
		// TODO Auto-generated method stub
		super.setEnterTransition(transition);
	}

	@Override
	public Transition getEnterTransition() {
		// TODO Auto-generated method stub
		return super.getEnterTransition();
	}

	@Override
	public void setReturnTransition(Transition transition) {
		// TODO Auto-generated method stub
		super.setReturnTransition(transition);
	}

	@Override
	public Transition getReturnTransition() {
		// TODO Auto-generated method stub
		return super.getReturnTransition();
	}

	@Override
	public void setExitTransition(Transition transition) {
		// TODO Auto-generated method stub
		super.setExitTransition(transition);
	}

	@Override
	public Transition getExitTransition() {
		// TODO Auto-generated method stub
		return super.getExitTransition();
	}

	@Override
	public void setReenterTransition(Transition transition) {
		// TODO Auto-generated method stub
		super.setReenterTransition(transition);
	}

	@Override
	public Transition getReenterTransition() {
		// TODO Auto-generated method stub
		return super.getReenterTransition();
	}

	@Override
	public void setSharedElementEnterTransition(Transition transition) {
		// TODO Auto-generated method stub
		super.setSharedElementEnterTransition(transition);
	}

	@Override
	public Transition getSharedElementEnterTransition() {
		// TODO Auto-generated method stub
		return super.getSharedElementEnterTransition();
	}

	@Override
	public void setSharedElementReturnTransition(Transition transition) {
		// TODO Auto-generated method stub
		super.setSharedElementReturnTransition(transition);
	}

	@Override
	public Transition getSharedElementReturnTransition() {
		// TODO Auto-generated method stub
		return super.getSharedElementReturnTransition();
	}

	@Override
	public void setAllowEnterTransitionOverlap(boolean allow) {
		// TODO Auto-generated method stub
		super.setAllowEnterTransitionOverlap(allow);
	}

	@Override
	public boolean getAllowEnterTransitionOverlap() {
		// TODO Auto-generated method stub
		return super.getAllowEnterTransitionOverlap();
	}

	@Override
	public void setAllowReturnTransitionOverlap(boolean allow) {
		// TODO Auto-generated method stub
		super.setAllowReturnTransitionOverlap(allow);
	}

	@Override
	public boolean getAllowReturnTransitionOverlap() {
		// TODO Auto-generated method stub
		return super.getAllowReturnTransitionOverlap();
	}

	@Override
	public void dump(String prefix, FileDescriptor fd, PrintWriter writer,
			String[] args) {
		// TODO Auto-generated method stub
		super.dump(prefix, fd, writer, args);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

}
