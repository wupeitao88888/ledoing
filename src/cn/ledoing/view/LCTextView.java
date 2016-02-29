package cn.ledoing.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.ledoing.activity.R;
import cn.ledoing.bean.LyricBean;
import cn.ledoing.view.ShuoMClickableSpan.ItemCallBack;

/**
 * <pre>
 * 业务名:
 * 功能说明:
 * 编写日期:	2015年5月29日
 * 编写人员:	 吴培涛
 *
 * 历史记录
 * 1、修改日期：
 *    修改人：
 *    修改内容：
 * </pre>
 */
public class LCTextView extends FrameLayout implements ItemCallBack {
    private Context mContext;
    private TextView textView,lc_note_item;
    private List<LyricBean> list;
    public MyCallBack mCallBack;
    private boolean isF=true;
    public interface MyCallBack {
        void mOnClick(Object obj);
    }

    public void setMyCallBack(MyCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * @param context
     */
    public LCTextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        list = new ArrayList<LyricBean>();

        init(context);
    }

    public LCTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        list = new ArrayList<LyricBean>();

        init(context);
    }

    /**
     * <pre>
     * 方法说明：
     * 编写日期:	2015年5月29日
     * 编写人员:   吴培涛
     *
     * </pre>
     *
     * @param context
     */
    private void init(Context context) {
        // TODO Auto-generated method stub
        this.mContext = context;

        View inflate = View.inflate(context, R.layout.m_textview, null);
        textView = (TextView) inflate.findViewById(R.id.item);
        lc_note_item= (TextView) inflate.findViewById(R.id.lc_note_item);

        for (int i = 0; i < list.size(); i++) {
            SpannableString spanttt = new SpannableString(list.get(i).getText());
            ClickableSpan clickttt = new ShuoMClickableSpan(list.get(i)
                    .getText(), context, i, list, textView);
            ShuoMClickableSpan.setItemCallBack(this);
            ForegroundColorSpan span = new ForegroundColorSpan(getResources()
                    .getColor(R.color.lyc_text));
            spanttt.setSpan(span, 0, list.get(i).getText().length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanttt.setSpan(clickttt, 0, list.get(i).getText().length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textView.append(spanttt);
        }
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        addView(inflate);
    }


    public void setNote(String text){

        if(isF){
            lc_note_item.setText(text);
        }

    }
    /*
     *
     *
     * (non-Javadoc)
     *
     * @see
     * com.example.lctextview.ShuoMClickableSpan.ItemCallBack#onSuccess(java;
     * .util.List, int)
     */
    @Override
    public void onSuccess(List<LyricBean> list, int p) {
        // TODO Auto-generated method stub

        if (mCallBack != null)
            mCallBack.mOnClick(list.get(p));

    }

    public void setSelection(int p) {
        textView.setText("");

        for (int i = 0; i < list.size(); i++) {
            SpannableString spanttt = new SpannableString(list.get(i).getText());
            ClickableSpan clickttt = new ShuoMClickableSpan(list.get(i)
                    .getText(), mContext, i, list, textView);
            ShuoMClickableSpan.setItemCallBack(this);
            ForegroundColorSpan span = null;
            if (!TextUtils.isEmpty(list.get(i).getTime())) {
                if (p > Integer.parseInt(list.get(i).getTime())
                        & p < Integer.parseInt(list.get(i).getEndtime())) {
                    span = new ForegroundColorSpan(getResources().getColor(
                            R.color.selet_title));
                } else {
                    span = new ForegroundColorSpan(getResources().getColor(
                            R.color.lyc_text));
                }
                spanttt.setSpan(span, 0, list.get(i).getText().length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanttt.setSpan(clickttt, 0, list.get(i).getText().length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                textView.append(spanttt);
            }
        }
    }

    public void setListAll(List<LyricBean> lb) {
        list.clear();
        if (lb != null) {
            if(lb.size()!=0){
                lc_note_item.setText("");
                isF=false;
            }else{
                isF=true;
            }
            list.addAll(lb);
        }

    }
}
