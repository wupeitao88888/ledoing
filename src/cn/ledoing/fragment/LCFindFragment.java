package cn.ledoing.fragment;

import cn.ledoing.activity.LCFindInfoPraise;
import cn.ledoing.activity.R;
import cn.ledoing.utils.FindRefresh;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * 
 * 
 * 发现
 */
public class LCFindFragment extends Fragment {
    private View rootView;// 缓存Fragment view

    private Activity mActivity = null;



    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = initView(inflater);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    /**
     * <pre>
     * 方法说明：
     * 编写日期:	2015-4-7
     * 编写人员:   吴培涛
     * </pre>
     */
    @SuppressLint("NewApi")
    private View initView(LayoutInflater inflater) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.lc_tab_contact, null);
        mActivity = this.getActivity();

        // 主视图的Fragment添加
        mActivity.getFragmentManager().beginTransaction()
                .replace(R.id.lc_find_content, new LCFindContentFragment())
                .commit();



        return v;
    }
}
