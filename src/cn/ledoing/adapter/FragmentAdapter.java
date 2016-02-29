package cn.ledoing.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

/**
 * Created by wupeitao on 15/11/5.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;
    private Fragment[] fragments;
    private boolean[] fragmentsUpdateFlag;
    public FragmentAdapter(FragmentManager fm, Fragment[] fragments, boolean[] fragmentsUpdateFlag) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
        this.fragmentsUpdateFlag=fragmentsUpdateFlag;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments[position % fragments.length];
        return fragments[position % fragments.length];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //重载该方法，防止其它视图被销毁，防止加载视图卡顿
        // super.destroyItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Fragment fragment = (Fragment) super.instantiateItem(container,
                position);

        String fragmentTag = fragment.getTag();

        if (fragmentsUpdateFlag[position % fragmentsUpdateFlag.length]) {


            FragmentTransaction ft = fm.beginTransaction();

            ft.remove(fragment);
            fragment = fragments[position % fragments.length];

            ft.add(container.getId(), fragment, fragmentTag);
            ft.attach(fragment);
            ft.commit();

            fragmentsUpdateFlag[position % fragmentsUpdateFlag.length] = false;
        }

        return fragment;
    }
}

