package cn.ledoing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.bean.Comment;
import cn.ledoing.bean.Sequence;
import cn.ledoing.utils.AbViewHolder;
import cn.ledoing.utils.LCUtils;

/**
 * 评论列表
 * Created by wupeitao on 15/11/4.
 */
public class SequenceAdapter extends BaseAdapter {
    private Context context;
    private List<Sequence> list;

    public SequenceAdapter(Context context, List<Sequence> list) {
        this.context = context;
        this.list = list;
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
        final Sequence comment = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_sequebce_item, null);
        }

        TextView content = AbViewHolder.get(convertView, R.id.content);
        final RelativeLayout content_bg = AbViewHolder.get(convertView, R.id.content_bg);

        LCUtils.setTitle(content, comment.getContent());
        if ("1".equals(comment.getStatus())) {
            content_bg.setBackgroundColor(context.getResources().getColor(R.color.gray_bg));
        } else {
            content_bg.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        content_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset(position);
                comment.setStatus("1");
                notifyDataSetChanged();
            }
        });


        return convertView;
    }

    private void reset(int p) {
        for (int i = 0; i < list.size(); i++) {
            if (p != i) {
                Sequence sequence = list.get(i);
                sequence.setStatus("0");
            }
        }
    }
    public void resetAll() {
        for (int i = 0; i < list.size(); i++) {
                Sequence sequence = list.get(i);
                sequence.setStatus("0");
        }
        notifyDataSetChanged();
    }


    public Sequence getSequence() {
        for (int i = 0; i < list.size(); i++) {

            Sequence sequence = list.get(i);
            if ("1".equals(sequence.getStatus())) {

                return sequence;
            }

        }
        return null;
    }

}
