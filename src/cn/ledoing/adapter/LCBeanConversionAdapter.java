package cn.ledoing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.bean.BeanConversion;
import cn.ledoing.bean.BeanTask;
import cn.ledoing.bean.FindList;
import cn.ledoing.bean.LDChange;
import cn.ledoing.bean.Praise;
import cn.ledoing.bean.SingleHttpBean;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.utils.AbStrUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;

public class LCBeanConversionAdapter extends BaseAdapter {
    private Context context;
    private List<LDChange> list;

    private AbHttpUtil mAbHttpUtil = null;
    private String format = "yyyy年MM月dd日";

    public LCBeanConversionAdapter(Context context, List<LDChange> list2) {
        super();
        this.context = context;
        this.list = list2;
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LDChange classList = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.lc_beanconversion_item, null);
            vh = new ViewHolder();
            vh.lc_stutstype = (TextView) convertView
                    .findViewById(R.id.lc_stutstype);
            vh.lc_money = (TextView) convertView
                    .findViewById(R.id.lc_money);
            vh.vouchername = (TextView) convertView
                    .findViewById(R.id.vouchername);
            vh.lc_expirationdate = (TextView) convertView
                    .findViewById(R.id.lc_expirationdate);
            vh.lc_beanconversion_left = (RelativeLayout) convertView
                    .findViewById(R.id.lc_beanconversion_left);
            vh.lc_stuts_type = (Button) convertView
                    .findViewById(R.id.lc_stuts_type);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        setType(vh.lc_stutstype, classList.getState(), vh.lc_beanconversion_left,vh.lc_stuts_type);
        setTitle(vh.vouchername, classList.getName());
        setMoney(vh.lc_money, classList.getReward());
        L.e(classList.getBegin_name()+"-------------------"+classList.getEnd_time());
        setTitle(vh.lc_expirationdate, AbDateUtil.getStringByFormat(classList.getBegin_name(), format) +
                "至" + AbDateUtil.getStringByFormat(classList.getEnd_time(), format));
        setPraise(vh.lc_stuts_type,classList,vh.lc_stutstype);
        return convertView;
    }

    private void setMoney(TextView lc_money, String money) {
        if (TextUtils.isEmpty(money)) {
            lc_money.setText("");
            return;
        }
//        if (money.length() == 3) {
//            lc_money.setText(money);
//            lc_money.setTextSize(AbViewUtil.dip2px(context, 10));
//        }else if(money.length() == 4){
//            lc_money.setText(money);
//            lc_money.setTextSize(AbViewUtil.dip2px(context, 8));
//        }else if(money.length() == 5){
//            lc_money.setText(money);
//            lc_money.setTextSize(AbViewUtil.dip2px(context, 4));
//        }else
//        {
//            lc_money.setText(money);
//            lc_money.setTextSize(AbViewUtil.dip2px(context, 17));
//        }
    }

    private void setType(TextView lc_stutstype, String stautstype, RelativeLayout lc_beanconversion_left,Button lc_stuts_type) {
        if (TextUtils.isEmpty(stautstype)) {
            lc_stutstype.setText("");
            lc_beanconversion_left.setBackgroundColor(context.getResources().getColor(R.color.gray));
            return;
        }
        switch (Integer.parseInt(stautstype)) {
            case 3:
                lc_stutstype.setText("未生效");
                lc_beanconversion_left.setBackgroundColor(context.getResources().getColor(R.color.Cambridgeblue));
                lc_stuts_type.setSelected(true);
                break;
            case 1:
                lc_stutstype.setText("未兑换");
                lc_beanconversion_left.setBackgroundColor(context.getResources().getColor(R.color.Cambridgeblue));
                lc_stuts_type.setSelected(true);
                break;
            case 2:
                lc_stutstype.setText("已兑换");
                lc_beanconversion_left.setBackgroundColor(context.getResources().getColor(R.color.gray));
                lc_stuts_type.setSelected(false);
                break;
            case 4:
                lc_stutstype.setText("已失效");
                lc_beanconversion_left.setBackgroundColor(context.getResources().getColor(R.color.gray));
                lc_stuts_type.setSelected(false);
                break;
        }
    }

    private void setTitle(TextView lc_class_title, String videoclassname) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(videoclassname)) {
            lc_class_title.setText(videoclassname);
        } else {
            lc_class_title.setText("");
        }
    }

    private void setPraise(final Button lc_stuts_type, final LDChange classList,final TextView lc_stutstype) {
        // TODO Auto-generated method stub
        lc_stuts_type.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!"1".equals(classList.getState())){
                    AbToastUtil.showToast(context,"此兑换券"+ lc_stutstype.getText().toString());
                    return;
                }
                clickPraise(classList);

            }
        });
    }

    /**
     * 兑换乐豆
     */
    public void clickPraise(final LDChange classList) {

        AbRequestParams params = new AbRequestParams();

        params.put("uuid", LCUtils.getOnly(context));
        params.put("code", classList.getCode());
        mAbHttpUtil.post(LCConstant.URL + LCConstant.URL_API+LCConstant.LEDOU_EXCHANGE, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        SingleHttpBean singlehttp = JSONUtils.getInstatce().getLDVoucherOver(
                                content, context);
                        if ( !"20050".equals(singlehttp.getErrorCode()) ) {
                            classList.setState("2");
                        }
                        AbToastUtil.showToast(context,singlehttp.getErrorMessage());
                        notifyDataSetChanged();
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        AbToastUtil.showToast(context, "兑换失败");

                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }

                });
    }


    class ViewHolder {
        private TextView lc_stutstype, lc_money, vouchername, lc_expirationdate;
        private RelativeLayout lc_beanconversion_left;
        private Button lc_stuts_type;
    }
}
