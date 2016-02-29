package cn.ledoing.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ledoing.activity.LCUserLoginAndRegister;
import cn.ledoing.activity.R;
import cn.ledoing.adapter.FragmentAdapter;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.FragmentEvent;
import cn.ledoing.utils.L;
import cn.ledoing.utils.MSG;
import de.greenrobot.event.EventBus;


/*
 * 
 * 
 * 家
 */
public class HomeFragment extends Fragment implements OnClickListener, ViewPager.OnPageChangeListener, FragmentEvent.OnEventListener {
    private View rootView;// 缓存Fragment view
    private Activity mActivity;

    private LayoutInflater inflater;
    private TextView centerAttention_text, centerAll_text;
    private RelativeLayout centerAttention_re,//我关注的
            centerAll_re;//全部中心
    private View left, right;
    private AbHttpUtil mAbHttpUtil = null;
    private CenterAttentionFragment centerAttentionFragment;
    private CenterAll centerAll;
    private Fragment[] fragments = new Fragment[2];
    private boolean[] fragmentsUpdateFlag = {false, false};
    private FragmentAdapter adapter;


    Handler mainHandler = new Handler() {

        /*
         * （非 Javadoc）
         *
         * @see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg) {
            // TODO 自动生成的方法存根
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG.INTO_05:
                    fragments[2] = centerAll;
                    fragmentsUpdateFlag[2] = true;
                    adapter.notifyDataSetChanged();
                    break;
                default:
            }
        }
    };
    private ViewPager mechange_viewpager;

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        mActivity = this.getActivity();
        if (rootView == null) {
            rootView = initView(inflater);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // register
        EventBus.getDefault().register(this);
    }

    public void onEvent(Object event) {
        L.e("刷新");
        if((int)event==1001){
            centerAttentionFragment.setRefase();
            setConcernText();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Unregister
        EventBus.getDefault().unregister(this);
    }

    /**
     * <pre>
     * 方法说明：
     *  编写日期: 2015-4-7
     *  编写人员: 吴培涛
     * </pre>
     *
     * @param inflater
     * @return
     */
    private View initView(LayoutInflater inflater) {
        // TODO Auto-generated method stub
        mActivity = getActivity();
        mAbHttpUtil = AbHttpUtil.getInstance(mActivity);
        mAbHttpUtil.setTimeout(5000);
        View v = inflater.inflate(R.layout.layout_home, null);

        centerAttention_text = (TextView) v.findViewById(R.id.centerAttention_text);
        centerAll_text = (TextView) v.findViewById(R.id.centerAll_text);

        centerAttention_re = (RelativeLayout) v.findViewById(R.id.centerAttention_re);
        centerAll_re = (RelativeLayout) v.findViewById(R.id.centerAll_re);

        left = (View) v.findViewById(R.id.left);
        right = (View) v.findViewById(R.id.right);

        centerAttention_re.setOnClickListener(this);
        centerAll_re.setOnClickListener(this);
        mechange_viewpager = (ViewPager) v.findViewById(R.id.mechange_viewpager);
        centerAttentionFragment = new CenterAttentionFragment();
        centerAll = new CenterAll();
        fragments[0] = centerAttentionFragment;
        fragments[1] = centerAll;
        adapter = new FragmentAdapter(getFragmentManager(), fragments, fragmentsUpdateFlag);
        mechange_viewpager.setAdapter(adapter);
        mechange_viewpager.setOnPageChangeListener(this);
        mechange_viewpager.setCurrentItem(0);

        if (LCConstant.islogin) {
            setConcernText();
        } else {
            setCenterAllText();
        }
        return v;
    }


    public void setConcernText() {
        centerAttention_text.setTextColor(getResources().getColor(R.color.blue));
        centerAll_text.setTextColor(getResources().getColor(R.color.gray_e1));
        left.setBackgroundColor(getResources().getColor(R.color.blue));
        right.setBackgroundColor(getResources().getColor(R.color.gray_e1));
        RelativeLayout.LayoutParams lpl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) AbViewUtil.dip2px(mActivity, 2));
        lpl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
        left.setLayoutParams(lpl);
        RelativeLayout.LayoutParams lpr = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) AbViewUtil.dip2px(mActivity, 1));
        lpr.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
        right.setLayoutParams(lpr);
        mechange_viewpager.setCurrentItem(0);
    }

    public void setCenterAllText() {
        centerAttention_text.setTextColor(getResources().getColor(R.color.gray_e1));
        centerAll_text.setTextColor(getResources().getColor(R.color.blue));
        left.setBackgroundColor(getResources().getColor(R.color.gray_e1));
        right.setBackgroundColor(getResources().getColor(R.color.blue));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) AbViewUtil.dip2px(mActivity, 1));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
        left.setLayoutParams(lp);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) AbViewUtil.dip2px(mActivity, 2));
        rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
        right.setLayoutParams(rp);
        mechange_viewpager.setCurrentItem(1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.centerAttention_re:
                if (LCConstant.islogin) {
                    setConcernText();
                    centerAttentionFragment.getConcern();
                } else {
                    Intent intent = new Intent(mActivity, LCUserLoginAndRegister.class);
                    intent.putExtra("mIntent", 6);
                    startActivity(intent);
                }
                break;
            case R.id.centerAll_re:
                setCenterAllText();
                centerAll.getConcern();
                break;
        }
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onEvent(int what, Bundle data, Object object) {
        mainHandler.sendEmptyMessage(what);
    }

}