package cn.ledoing.adapter;

import android.content.Context;
import android.content.Intent;
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
import cn.ledoing.activity.TeacherHomePage;
import cn.ledoing.bean.InstitutionTeacher;
import cn.ledoing.view.LCRatingBar;

/**
 * Created by lc-php1 on 2015/11/4.
 */
public class HomeCenterAdapter extends BaseAdapter{

    private Context context;
    private List<InstitutionTeacher.Datas> list;
    private String ins_id;

    public HomeCenterAdapter(Context context, List<InstitutionTeacher.Datas> list,String ins_id){
        this.context = context;
        this.list = list;
        this.ins_id = ins_id;
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
        final InstitutionTeacher.Datas ins_teachaer = (InstitutionTeacher.Datas) getItem(position);
        ViewHoder holder;
        if(convertView == null){
            holder = new ViewHoder();
            convertView = LayoutInflater.from(context).inflate(R.layout.center_layout_item, null);
            holder.ratingBar = (LCRatingBar)convertView.findViewById(R.id.teacher_ratingBar);
            holder.teacher_name = (TextView)convertView.findViewById(R.id.teacher_name);
            holder.teacher_profile = (TextView)convertView.findViewById(R.id.teacher_profile);
            holder.teacher_image = (ImageView)convertView.findViewById(R.id.teacher_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHoder) convertView.getTag();
        }
        if(TextUtils.isEmpty(ins_teachaer.getTeacher_score())){
            ins_teachaer.setTeacher_score("0.0");
    }
        holder.ratingBar.setMark(Float.parseFloat(ins_teachaer.getTeacher_score()));
        holder.ratingBar.setSmail(true);
        holder.teacher_name.setText(ins_teachaer.getTeacher_name());
        holder.teacher_profile.setText(ins_teachaer.getTeacher_desc());
        if(!TextUtils.isEmpty(ins_teachaer.getHead_pic())){
            Glide.with(context).load(ins_teachaer.getHead_pic()).into(holder.teacher_image);
        } else {
            holder.teacher_image.setBackgroundResource(R.drawable.teacher_bg);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, TeacherHomePage.class);
                intent.putExtra("teacher_id",ins_teachaer.getTeacher_id());
                intent.putExtra("ins_id",ins_id);
                context.startActivity(intent);

            }
        });

        return convertView;
    }

    class ViewHoder {
        TextView text;
        TextView teacher_name;
        TextView teacher_profile;
        ImageView teacher_image;
        LCRatingBar ratingBar;
    }
}
