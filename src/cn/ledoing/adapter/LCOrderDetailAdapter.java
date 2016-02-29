package cn.ledoing.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import cn.ledoing.activity.R;
import cn.ledoing.bean.Courselist;
import cn.ledoing.bean.Student;
import cn.ledoing.global.LCConstant;
import cn.ledoing.view.NoScrollGridView;

public class LCOrderDetailAdapter extends BaseAdapter {

    Context context;
    private ArrayList<Courselist> lists;
    private int curp;
    private boolean isAdd = false;
    private int listPosition;
    private int gridPosition;


    public LCOrderDetailAdapter(Context activity, ArrayList<Courselist> lists) {
        super();
        this.lists = lists;
        this.context = activity;
    }

    public void selectPosition(int position) {
        this.curp = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int parentposition, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.list_order_detail_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Courselist orderDetail = lists.get(parentposition);
        setStr(holder.title, orderDetail.getTaskname());
        if (orderDetail.getNumber() == orderDetail.getMax_number()) {
            setStr(holder.personOfAll, "已满" + "/" + orderDetail.getMax_number());
        } else {
            setStr(holder.personOfAll, orderDetail.getNumber() + "/" + orderDetail.getMax_number());
        }
//        setStr(holder.name1, orderDetail.getName1());
//        setStr(holder.name2, orderDetail.getName2());
        setmImage(holder.titleImg, orderDetail.getCourseimg());
        if (orderDetail.getStudentlist() != null && orderDetail.getStudentlist().size() > 0) {
            final GridViewAdapter gridViewAdapter = new GridViewAdapter(context, orderDetail.getStudentlist());
            holder.gridview.setAdapter(gridViewAdapter);
            holder.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!isAdd) {
                        if (position == orderDetail.getStudentlist().size() - 1) {
                            orderDetail.getStudentlist().set(position, new Student("我", LCConstant.userinfo.getUserpic()));
                            notifyDataSetChanged();
                            listPosition = parentposition;
                            gridPosition = position;
                            isAdd = true;
//                    if(LCConstant.userinfo == null){
//                        holder.img.setImageResource(R.drawable.hand);
//                    }else if(TextUtils.isEmpty(LCConstant.userinfo.getUserpic())){
//                        holder.img.setImageResource(R.drawable.hand);
//                    }else {
//                        setmImage(holder.img, LCConstant.userinfo.getUserpic());
//                    }
                        }
                    }else{
                        lists.get(listPosition).getStudentlist().set(gridPosition,new Student("可加入","http://s.lechome.com/mobile/icon/add_me.png"));

                        orderDetail.getStudentlist().set(position,new Student("我",LCConstant.userinfo.getUserpic()));
                        notifyDataSetChanged();
                        listPosition = parentposition;
                        gridPosition = position;
                        notifyDataSetChanged();
                    }
                }
            });
        } else {
            holder.gridview.setVisibility(View.GONE);
        }
        return convertView;
        // TODO Auto-generated method stub
    }

    private void setmImage(ImageView lc_index_threeimage, String courseimg) {
        if (!TextUtils.isEmpty(courseimg)) {
            Glide.with(context)
                    .load(courseimg)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .centerCrop()
//                    .placeholder(R.drawable.image_loading)
//                    .error(R.drawable.image_error)
                    .crossFade()
                    .into(lc_index_threeimage);
        } else {
            lc_index_threeimage.setImageResource(R.drawable.white_title);
        }
    }

    private void setStr(TextView lc_class_title, String videoclassname) {
        // TODO Auto-generated method stub
        if (!TextUtils.isEmpty(videoclassname)) {
            lc_class_title.setText(videoclassname);
        } else {
            lc_class_title.setText("");
        }
    }

    public class ViewHolder {

        TextView title, personOfAll;
        ImageView titleImg;
        NoScrollGridView gridview;

        public ViewHolder(View v) {

            title = (TextView) v.findViewById(R.id.title);
            titleImg = (ImageView) v.findViewById(R.id.title_img);
            gridview = (NoScrollGridView) v.findViewById(R.id.gridView2);
            personOfAll = (TextView) v.findViewById(R.id.textView14);
        }

    }

}
