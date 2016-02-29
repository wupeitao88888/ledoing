package cn.ledoing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.bean.Comment;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.utils.AbViewHolder;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCRatingBarFace;

/**
 * 评论列表
 * Created by wupeitao on 15/11/4.
 */
public class CommentALlAdapter extends BaseAdapter {
    private Context context;
    private List<Comment.Data.ListData> list;

    public CommentALlAdapter(Context context, List<Comment.Data.ListData> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment.Data.ListData comment = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_comment, null);
        }

        ImageView comment_pic = AbViewHolder.get(convertView, R.id.comment_pic);
        TextView comment_name = AbViewHolder.get(convertView, R.id.comment_name);
        LCRatingBarFace comment_mark = AbViewHolder.get(convertView, R.id.comment_mark);
        TextView comment_content = AbViewHolder.get(convertView, R.id.comment_content);
        TextView comment_time = AbViewHolder.get(convertView, R.id.comment_time);
        TextView comment_class = AbViewHolder.get(convertView, R.id.comment_class);
        View line = AbViewHolder.get(convertView, R.id.line);


        line.setBackgroundColor(context.getResources().getColor(R.color.line));

        LCUtils.setTitle(comment_name, comment.getUser_name());
        LCUtils.setTitle(comment_content, comment.getComment_text());
        LCUtils.setTitle(comment_time, AbDateUtil.getStringByFormat(Long.parseLong(comment.getComment_time()), AbDateUtil.dateFormatYMDHM_HAN));
        LCUtils.setTitle(comment_class, comment.getTask_name());
        LCUtils.mImageloader(comment.getHead_pic(), comment_pic, context);
        comment_mark.setMark(Float.parseFloat(comment.getComment_score()));
        comment_mark.setSmail(true);

        return convertView;
    }


    public int getP() {
        return list == null ? 0 : (list.size() >= 3 ? 2 : list.size() - 1);
    }


}
