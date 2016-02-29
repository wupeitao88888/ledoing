package cn.ledoing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;
import java.util.Map;

import cn.ledoing.activity.R;
import cn.ledoing.bean.CancelReasonList;
import cn.ledoing.model.LCCheckChangeCallBack;
import cn.ledoing.utils.AbViewHolder;

/**
 * Created by wpt on 2015/9/21.
 */
public class LCReasonAdapter extends BaseAdapter {
    private Context context;
    private List<CancelReasonList> list;
    private Map<String, String> choose;
    private LCCheckChangeCallBack checkChangeCallBack;

    public LCReasonAdapter(Context context, List<CancelReasonList> list, Map<String, String> choose) {
        this.context = context;
        this.list = list;
        this.choose = choose;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String checkText = list.get(position).getReason();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lc_layout_reason, null);
        }
        CheckBox dialog_reason = AbViewHolder.get(convertView, R.id.dialog_reason);
        dialog_reason.setText(checkText);
        dialog_reason.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    choose.put("" + position, checkText);
                } else {
                    choose.remove("" + position);
                }
                if(checkChangeCallBack!=null){
                    checkChangeCallBack.onChange(choose);
                }
            }
        });
        return convertView;
    }

    public void setCheckChangeListener(LCCheckChangeCallBack checkChangeCallBack) {
        this.checkChangeCallBack = checkChangeCallBack;
    }

}
