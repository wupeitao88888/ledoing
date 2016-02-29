package cn.ledoing.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.ledoing.adapter.GridViewAdapter;
import cn.ledoing.adapter.LCOrderDetailAdapter;
import cn.ledoing.bean.BeanOrderDetail;
import cn.ledoing.bean.Courselist;
import cn.ledoing.bean.LCTeacherBean;
import cn.ledoing.bean.OrderDetail;
import cn.ledoing.bean.Student;
import cn.ledoing.global.LCApplication;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.AbDateUtil;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import cn.ledoing.view.LCTitleBar;
import cn.ledoing.view.NoScrollGridView;

/**
 * Created by cheers on 2015/9/9.
 */
public class LCOrderActivity extends LCActivitySupport {

    private ListView xListView;
    private LCOrderDetailAdapter adapter;
    private Context mContext;
    private AbHttpUtil mAbHttpUtil;
    private LCTitleBar lc_title;
    private Button order;
    private TextView title;
    private TextView titleTime;
    private BeanOrderDetail orderDetail;
    private boolean isAdd = false;
    private int listPosition;
    private int gridPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        mContext = this;
        initView();
        getOrderList();
    }

    private void initView() {
        mAbHttpUtil = AbHttpUtil.getInstance(context);
        mAbHttpUtil.setTimeout(5000);
        initTitle();
        order = (Button) findViewById(R.id.order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAdd) {
                    Intent intent = new Intent(context,LCOrderDetealActivity.class);
                    intent.putExtra("classid",  getIntent().getStringExtra("classid"));
                    intent.putExtra("taskid",orderDetail.getCourselist().get(listPosition).getTaskid()+"");
                    startActivity(intent);
                } else {
                    showToast("请选择任务后再预约！");
                }
            }
        });
        xListView = (ListView) findViewById(R.id.xListView);
        xListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.order_detail_header, null, false));
        initHeader();
    }

    private void initHeader() {
        title = (TextView) findViewById(R.id.title_top_info);
        titleTime = (TextView) findViewById(R.id.time_info);
    }

    private void initTitle() {
        lc_title = (LCTitleBar) findViewById(R.id.lc_title);
        lc_title.setCenterTitle("选择任务");
        lc_title.setCenterTitleColor(getResources().getColor(R.color.white));
        lc_title.setBackGb(getResources().getColor(R.color.titlebar_activity));
        lc_title.setLeftImage(R.drawable.goback);
    }

    /**
     * 网络请求
     */
    public void getOrderList() {
        AbRequestParams params = new AbRequestParams();
        params.put("classid", getIntent().getStringExtra("classid"));
        mAbHttpUtil.postUrl(LCConstant.ROWCLASS_TASK_LIST, params,
                new AbStringHttpResponseListener() {
                    // 获取数据成功会调用这里
                    @Override
                    public void onSuccess(int statusCode, String content) {
                        L.e("=======4.5====statusCode==" + statusCode + "================" + loadConvert(content));
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(loadConvert(content));

                            if (jsonObject.optString("errorCode").equals("0")) {
                                JSONObject object = jsonObject.optJSONObject("data");
                                orderDetail = new BeanOrderDetail(object);
                            }
                            setValue();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // 开始执行前
                    @Override
                    public void onStart() {
                        LCUtils.startProgressDialog(context,"正在加载...");
                    }

                    // 失败，调用
                    @Override
                    public void onFailure(int statusCode, String content,
                                          Throwable error) {
                        L.e("=======4.5====statusCode==" + statusCode + "================" + content);
                    }

                    // 完成后调用，失败，成功
                    @Override
                    public void onFinish() {
                        LCUtils.stopProgressDialog(context);
                    }
                });
    }

//    /**
//     * 网络请求
//     */
//    public void saveOrder() {
//        AbRequestParams params = new AbRequestParams();
//        params.put("classid", getIntent().getStringExtra("classid"));
//        params.put("taskid", orderDetail.getCourselist().get(listPosition).getTaskid() + "");
////        params.put("taskid", 28+"");
//
//        mAbHttpUtil.postUrl(LCConstant.SAVE_USER_CLASS, params,
////        mAbHttpUtil.post("http://api.ledoedu.com/test/save-user-class", params,
//                new AbStringHttpResponseListener() {
//                    // 获取数据成功会调用这里
//                    @Override
//                    public void onSuccess(int statusCode, String content) {
//                        L.e("=======4.5====statusCode==" + statusCode + "================" + loadConvert(content));
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(loadConvert(content));
//                            if (jsonObject.optString("errorCode").equals("0")) {
//                                OrderDetail orderDetail = new OrderDetail(jsonObject.optJSONObject("data"));
//                                Intent intent = new Intent(context,LCOrderDetealActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("order", orderDetail);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            } else {
//                                showToast(jsonObject.optString("errorMessage"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    // 开始执行前
//                    @Override
//                    public void onStart() {
//                        LCUtils.startProgressDialog(context,"正在预约...");
//                    }
//
//                    // 失败，调用
//                    @Override
//                    public void onFailure(int statusCode, String content,
//                                          Throwable error) {
//                        L.e("=======4.5====statusCode==" + statusCode + "================" + content);
//                    }
//
//                    // 完成后调用，失败，成功
//                    @Override
//                    public void onFinish() {
//                        LCUtils.stopProgressDialog(context);
//                        order.setEnabled(true);
//                    }
//                });
//    }

    private void setValue() {
        title.setText(orderDetail.getCoursename() + "-" + orderDetail.getCentername() + "-" + orderDetail.getTeacher() + "老师");
        titleTime.setText(AbDateUtil.getStringByFormat(orderDetail.getSchooltime(), "yyyy-MM-dd") + " - " + AbDateUtil.getStringByFormat(orderDetail.getSchooltime(), "HH:mm") + "上课" + " - "+AbDateUtil.getStringByFormat(orderDetail.getEndtime(), "HH:mm") + "下课");
        for (int i = 0; i < orderDetail.getCourselist().size(); i++) {
            if (orderDetail.getCourselist().get(i).getNumber() < orderDetail.getCourselist().get(i).getMax_number()) {
                orderDetail.getCourselist().get(i).getStudentlist().add(new Student("可加入", LCConstant.HAND_URL));
            }
        }
        adapter = new LCOrderDetailAdapter(mContext, orderDetail.getCourselist());
        xListView.setAdapter(adapter);
    }

    public class LCOrderDetailAdapter extends BaseAdapter {

        Context context;
        private ArrayList<Courselist> lists;
        private int curp;


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
                holder.personOfAll.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                setStr(holder.personOfAll, orderDetail.getNumber() + "/" + orderDetail.getMax_number());
                holder.personOfAll.setTextColor(context.getResources().getColor(R.color.title_theme_text3));
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
                        if (position == orderDetail.getStudentlist().size() - 1 && orderDetail.getMax_number() > orderDetail.getNumber()) {
                            if (!isAdd) {
                                //设置自己刚刚添加的头像
                                orderDetail.getStudentlist().set(position, new Student("我", LCConstant.userinfo.getUserpic()));
                                /***
                                 *
                                 *  修改：BUG #253=>吴培涛 约课界面中，约课时加入任务，人数不变
                                 *
                                 *  添加人数
                                 */
                                orderDetail.setNumber(orderDetail.getNumber() + 1);
                                notifyDataSetChanged();
                                gridViewAdapter.notifyDataSetChanged();
                                listPosition = parentposition;
                                gridPosition = position;
                                isAdd = true;
                                order.setBackgroundResource(R.drawable.green);
//                    if(LCConstant.userinfo == null){
//                        holder.img.setImageResource(R.drawable.hand);
//                    }else if(TextUtils.isEmpty(LCConstant.userinfo.getUserpic())){
//                        holder.img.setImageResource(R.drawable.hand);
//                    }else {
//                        setmImage(holder.img, LCConstant.userinfo.getUserpic());
//                    }
                            } else {
                                //改变之前设置过的头像为空
                                lists.get(listPosition).getStudentlist().set(gridPosition, new Student("可加入", LCConstant.HAND_URL));
                                /***
                                 *
                                 *  修改：BUG #253=>吴培涛 约课界面中，约课时加入任务，人数不变
                                 *  减去上次选择的人数
                                 */
                                lists.get(listPosition).setNumber(lists.get(listPosition).getNumber()-1);
                                //设置自己刚刚添加的头像
                                orderDetail.getStudentlist().set(position, new Student("我", LCConstant.userinfo.getUserpic()));
                                orderDetail.setNumber(orderDetail.getNumber()+1);
                                notifyDataSetChanged();
                                listPosition = parentposition;
                                gridPosition = position;
                                notifyDataSetChanged();
                                gridViewAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            } else {
                holder.gridview.setVisibility(View.GONE);
            }
            return convertView;
            // TODO Auto-generated method stub
        }

        public void changeMe() {

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
}
