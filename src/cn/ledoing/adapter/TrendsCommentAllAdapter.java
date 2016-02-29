package cn.ledoing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.bean.Comment;
import cn.ledoing.bean.NewsComment;
import cn.ledoing.bean.Trends_Info;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.utils.AbViewHolder;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCRatingBarFace;

/**
 * 评论列表
 * Created by wupeitao on 15/11/4.
 */
public class TrendsCommentAllAdapter extends BaseAdapter {
    private Context context;
    private List<NewsComment> list;

    public TrendsCommentAllAdapter(Context context, List<NewsComment> list) {
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
       NewsComment comment = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_comment_all, null);
        }

        ImageView comment_pic = AbViewHolder.get(convertView, R.id.comment_pic);
        TextView comment_name = AbViewHolder.get(convertView, R.id.comment_name);
        TextView comment_content = AbViewHolder.get(convertView, R.id.comment_content);
        TextView comment_time = AbViewHolder.get(convertView, R.id.comment_time);


        LCUtils.setTitle(comment_name, comment.getUser_name());
        LCUtils.setTitle(comment_content, comment.getComment_text());
        LCUtils.setTitle(comment_time, AbDateUtil.getStringByFormat(comment.getComment_time(), AbDateUtil.dateFormatYMDHM_HAN));
        LCUtils.mImageloader(comment.getHead_pic(), comment_pic, context);

        return convertView;
    }




}
