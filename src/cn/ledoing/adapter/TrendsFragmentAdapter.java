package cn.ledoing.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.ledoing.activity.R;
import cn.ledoing.activity.TrendsInfo;
import cn.ledoing.bean.AllBean;
import cn.ledoing.bean.Trends;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.utils.AbToastUtil;
import cn.ledoing.utils.AbViewHolder;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;

/**
 * 动态
 * Created by wupeitao on 15/11/4.
 */
public class TrendsFragmentAdapter extends BaseAdapter {
    private Context context;
    private List<Trends.Data.DataList> list;
    private AbHttpUtil mAbHttpUtil;

    public TrendsFragmentAdapter(Context context, List<Trends.Data.DataList> list) {
        this.context = context;
        this.list = list;
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
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
        final Trends.Data.DataList comment = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_trends_fragment_item, null);
        }

        ImageView teacher_image = AbViewHolder.get(convertView, R.id.teacher_image);
        TextView center_text = AbViewHolder.get(convertView, R.id.center_text);
        TextView center_addr = AbViewHolder.get(convertView, R.id.center_addr);
        TextView trends_content = AbViewHolder.get(convertView, R.id.trends_content);
        TextView picCount = AbViewHolder.get(convertView, R.id.picCount);
        TextView trends_time = AbViewHolder.get(convertView, R.id.trends_time);
        TextView comment_onCount = AbViewHolder.get(convertView, R.id.comment_onCount);
        TextView supportCount = AbViewHolder.get(convertView, R.id.supportCount);
        ImageView ispraise = AbViewHolder.get(convertView, R.id.ispraise);
        ImageView comment_onCounticon = AbViewHolder.get(convertView, R.id.comment_onCounticon);
        ImageView trends_pic1 = AbViewHolder.get(convertView, R.id.trends_pic1);
        ImageView trends_pic2 = AbViewHolder.get(convertView, R.id.trends_pic2);
        ImageView trends_pic3 = AbViewHolder.get(convertView, R.id.trends_pic3);
        RelativeLayout count = AbViewHolder.get(convertView, R.id.count);
        LinearLayout praise_li = AbViewHolder.get(convertView, R.id.praise_li);
        LinearLayout comment_li = AbViewHolder.get(convertView, R.id.comment_li);

        LCUtils.setTitle(picCount, comment.getNews_img().size()+"");
        LCUtils.setTitle(trends_content, comment.getNews_content());
        LCUtils.setTitle(trends_time, AbDateUtil.getStringByFormat(comment.getPublish_time(), AbDateUtil.dateFormatYMDHM_HAN));
        LCUtils.setTitle(center_text, comment.getNews_name());
        LCUtils.setTitle(center_addr, comment.getNews_address());
        LCUtils.setTitle(comment_onCount, comment.getComment_num()+"");
        LCUtils.setTitle(supportCount, comment.getPraise_num()+"");
        LCUtils.mImageloader(comment.getNews_pic(), teacher_image, context);
        if (comment.getNews_img().size() >= 3) {
            LCUtils.mImageloader(comment.getNews_img().get(0), trends_pic1, context);
            LCUtils.mImageloader(comment.getNews_img().get(1), trends_pic2, context);
            LCUtils.mImageloader(comment.getNews_img().get(2), trends_pic3, context);
        } else {
            try {
                LCUtils.mImageloader(comment.getNews_img().get(0), trends_pic1, context);
            } catch (Exception e) {
                trends_pic1.setVisibility(View.GONE);
            }
            try {
                LCUtils.mImageloader(comment.getNews_img().get(1), trends_pic2, context);
            } catch (Exception e) {
                trends_pic2.setVisibility(View.GONE);
            }
            trends_pic3.setVisibility(View.GONE);
            count.setVisibility(View.GONE);
        }

        if (comment.getIspraise() == 1) {
            ispraise.setBackgroundResource(R.drawable.price_select);
            praise_li.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AbToastUtil.showToast(context, "你已经点过赞了！");
                }
            });
        } else {
            ispraise.setBackgroundResource(R.drawable.price_normal);
            praise_li.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    netComment(comment);
                }
            });
        }

        comment_li.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TrendsInfo.class);
                intent.putExtra("open",true);
                intent.putExtra("news_id",comment.getNews_id());
                L.e(comment.getNews_id() + "+++comment.getNews_id()++++++++++++");
                context.startActivity(intent);
            }
        });



        return convertView;
    }

    private void netComment(final Trends.Data.DataList comment) {
        // TODO Auto-generated method stub

        // 绑定参数
        AbRequestParams params = new AbRequestParams();
        params.put("newsid", comment.getNews_id() + "");
        mAbHttpUtil.post(LCConstant.TEACHER_PRICE, params,
                new AbStringHttpResponseListener() {

                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        AllBean allBean = JSON.parseObject(content, AllBean.class);
                        if("0".equals(allBean.getErrorCode())){
                            comment.setIspraise(1);
                            comment.setPraise_num(comment.getPraise_num() + 1);
                            notifyDataSetChanged();
                        }else{
                            LCUtils.ReLogin(allBean.getErrorCode(),context,allBean.getErrorMessage());
                        }
                    }


                    // 开始执行前
                    @Override
                    public void onStart() {
                        // 显示进度框
                        LCUtils.startProgressDialog(context);
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {

                        AbToastUtil.showToast(context, "点赞失败");
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        // 移除进度框
                        LCUtils.stopProgressDialog(context);
                    }
                });


    }


    //导致TextView异常换行的原因：安卓默认数字、字母不能为第一行以后每行的开头字符，因为数字、字母为半角字符
    //所以我们只需要将半角字符转换为全角字符即可，方法如下
//    public String ToSBC(String input) {
//        char c[] = input.toCharArray();
//        for (int i = 0; i < c.length; i++) {
//            if (c[i] == ' ') {
//                c[i] = '\u3000';
//            } else if (c[i] < '\177') {
//                c[i] = (char) (c[i] + 65248);
//            }
//        }
//        return new String(c);
//    }
}
