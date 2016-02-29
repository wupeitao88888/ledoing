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
import cn.ledoing.bean.BeanDetail;
import cn.ledoing.bean.BeanTask;
import cn.ledoing.bean.FindList;
import cn.ledoing.bean.Praise;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.AbViewUtil;
import cn.ledoing.utils.JSONUtils;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.utils.MD5Util;

public class LCBeanDetailAdapter extends BaseAdapter {
    private Context context;
    private List<BeanDetail> list;

    public LCBeanDetailAdapter(Context context, List<BeanDetail> list2) {
        super();
        this.context = context;
        this.list = list2;
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
        BeanDetail classList = list.get(position);
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.lc_detail_item, null);
            vh = new ViewHolder();
            vh.detail_typename = (TextView) convertView
                    .findViewById(R.id.detail_typename);
            vh.detail_balance = (TextView) convertView
                    .findViewById(R.id.detail_balance);
            vh.detail_addlecbean = (TextView) convertView
                    .findViewById(R.id.detail_addlecbean);
            vh.detail_time = (TextView) convertView
                    .findViewById(R.id.detail_time);
            vh.detail_note = (TextView) convertView
                    .findViewById(R.id.detail_note);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        setTitle(vh.detail_typename, classList.getTypename());
        setTitle(vh.detail_balance, classList.getBalance());
        setTitleText(vh.detail_addlecbean, classList.getAddlecbean(),classList.getStatus());
        setTitle(vh.detail_time, classList.getTime());
        setTitle(vh.detail_note, classList.getNote());

        return convertView;
    }

    private void setTitle(TextView lc_class_title, String videoclassname) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(videoclassname)) {
            lc_class_title.setText(videoclassname);
        } else {
            lc_class_title.setText("");
        }
    }

    private void setTitleText(TextView lc_class_title, String videoclassname, String status) {
        // TODO Auto-generated method stub

        if (TextUtils.isEmpty(status)) {
            return;
        }
        switch (Integer.parseInt(status)) {
            case 2:
                if (!TextUtils.isEmpty(videoclassname)) {
                    lc_class_title.setText("-"+videoclassname);
                } else {
                    lc_class_title.setText("");
                }
                lc_class_title.setTextColor(context.getResources().getColor(R.color.subtract));
                break;
            case 1:
                if (!TextUtils.isEmpty(videoclassname)) {
                    lc_class_title.setText("+"+videoclassname);
                } else {
                    lc_class_title.setText("");
                }
                lc_class_title.setTextColor(context.getResources().getColor(R.color.Cambridgeblue));
                break;
        }
    }


    class ViewHolder {
        private TextView detail_typename, detail_balance, detail_addlecbean, detail_time, detail_note;
    }
}
