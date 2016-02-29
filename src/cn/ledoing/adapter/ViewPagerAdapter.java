package cn.ledoing.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter {
	private Object object;
	private List<View> views;

	public ViewPagerAdapter(Object object, List<View> views) {
		super();
		this.object = object;
		this.views = views;
	}
	
	@Override
	public int getCount() { // 获得size
		return views==null?0:views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View view, int position, Object object) // 销毁Item
	{
		((ViewPager) view).removeView(views.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;

	}

	@Override
	public Object instantiateItem(View view, int position) // 实例化Item

	{
		if (views == null) {
			return super.instantiateItem(view, position);
		}
		((ViewPager) view).removeView(views.get(position));
		((ViewPager) view).addView(views.get(position));

		return views.get(position);
	}
}