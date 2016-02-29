package cn.ledoing.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.bean.InstitutionComment;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.view.LCRatingBarFace;

/**
 * Created by lc-php1 on 2015/11/4.
 */
public class HomeCommentAdapter extends BaseAdapter{

    private Context context;
    private List<InstitutionComment.Datas.ListData> list;

    public HomeCommentAdapter(Context context, List<InstitutionComment.Datas.ListData> list){
        this.context = context;
        this.list = list;
    };
    @Override
    public int getCount() {
        return  list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        if (null != list && position < getCount()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstitutionComment.Datas.ListData listData = (InstitutionComment.Datas.ListData) getItem(position);
        ViewHoder holder;
        if(convertView == null){
            holder = new ViewHoder();
            convertView = LayoutInflater.from(context).inflate(R.layout.comment_layout_item, null);
            holder.title_comment = (TextView) convertView.findViewById(R.id.title_comment);
            holder.time_and_class = (TextView) convertView.findViewById(R.id.time_and_class);
            holder.teacher_image = (ImageView) convertView.findViewById(R.id.teacher_image);
            holder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
            holder.score_bar = (LCRatingBarFace) convertView.findViewById(R.id.score_bar);
            convertView.setTag(holder);
        }else{
            holder = (ViewHoder) convertView.getTag();
        }
        Glide.with(context).load(listData.getHead_pic()).into(holder.teacher_image);
        holder.title_comment.setText(Html.fromHtml("<font color=#696975>" + (TextUtils.isEmpty(listData.getUser_name()) ? "" : listData.getUser_name()) + "</font>"));
        holder.time_and_class.setText(Html.fromHtml("<font color=#BABABA>"+ AbDateUtil.getStringByFormat(listData.getComment_time(), "yyyy年MM月dd日 HH:mm")+"</font><font color=#696975>" + " " + (TextUtils.isEmpty(listData.getTask_name()) ? "" : listData.getTask_name()) + "</font>"));
        holder.comment_content.setText(listData.getComment_text());
        holder.score_bar.setSmail(true);
        if(TextUtils.isEmpty(listData.getComment_score())){
            listData.setComment_score("0.0");
        }
        holder.score_bar.setMark(Float.parseFloat(listData.getComment_score()));

        return convertView;
    }

    class ViewHoder {
        TextView title_comment;
        TextView time_and_class;
        TextView comment_content;
        ImageView teacher_image;
        LCRatingBarFace score_bar;
    }
}
