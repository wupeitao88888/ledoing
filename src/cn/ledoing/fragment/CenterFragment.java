package cn.ledoing.fragment;

import android.view.View.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import cn.ledoing.activity.R;
import cn.ledoing.adapter.HomeCenterAdapter;
import cn.ledoing.bean.Institution;
import cn.ledoing.bean.InstitutionHomeNews;
import cn.ledoing.bean.InstitutionTeacher;
import cn.ledoing.global.AbAppConfig;
import cn.ledoing.global.LCConstant;
import cn.ledoing.http.AbHttpUtil;
import cn.ledoing.http.AbRequestParams;
import cn.ledoing.http.AbStringHttpResponseListener;
import cn.ledoing.utils.L;
import cn.ledoing.utils.LCUtils;
import de.greenrobot.event.EventBus;

public class CenterFragment extends Fragment implements OnClickListener{

	private ArrayList<InstitutionTeacher.Datas> list;
	private RelativeLayout broadcast_rl;
	private GridView gridView;
	private TextView introduction_content;// 中心介绍
	private TextView class_time;
	private TextView phone_content;
	private TextView traffic_content;
	private TextView class_time_buttom;
	private InstitutionTeacher ins_teacher;
	private InstitutionHomeNews ins_home_news;
	private AbHttpUtil mAbHttpUtil;
	private Institution institution;
	private HomeCenterAdapter gridAdapter;
	private ImageView center_img;
	private ImageView center_img_two;
	private ImageView center_img_three;
	private ImageView center_img_four;
	private TextView center_num;
	private RelativeLayout center_dynamic;
	private TextView broadcast_content;

	public static CenterFragment instantiation(int position,Institution institution) {
		CenterFragment fragment = new CenterFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		args.putSerializable("institution",institution);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mAbHttpUtil = AbHttpUtil.getInstance(getActivity());
		mAbHttpUtil.setTimeout(5000);
		return inflater.inflate(R.layout.fragment_test, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		gridView = (GridView) view.findViewById(R.id.gridView);
		broadcast_rl = (RelativeLayout) view.findViewById(R.id.broadcast_rl);
		introduction_content = (TextView) view.findViewById(R.id.introduction_content);
		class_time = (TextView) view.findViewById(R.id.class_time);
		class_time_buttom = (TextView) view.findViewById(R.id.class_time_buttom);
		phone_content = (TextView) view.findViewById(R.id.phone_content);
		traffic_content = (TextView) view.findViewById(R.id.traffic_content);
		center_num = (TextView) view.findViewById(R.id.center_num);
		broadcast_content = (TextView) view.findViewById(R.id.broadcast_content);
		center_img = (ImageView) view.findViewById(R.id.center_img);
		center_img_two = (ImageView) view.findViewById(R.id.center_img_two);
		center_img_three = (ImageView) view.findViewById(R.id.center_img_three);
		center_img_four = (ImageView) view.findViewById(R.id.center_img_four);
		center_dynamic = (RelativeLayout) view.findViewById(R.id.center_dynamic);
		center_dynamic.setOnClickListener(this);
		gridView.setFocusable(false);
		institution = (Institution) getArguments().getSerializable("institution");
		if(null==institution.getData()){
			return;
		}
		if(!TextUtils.isEmpty(institution.getData().getReport_text())){
			L.e("+++广播+++++"+institution.getData().getReport_text());
			broadcast_rl.setVisibility(View.VISIBLE);
			broadcast_content.setText(institution.getData().getReport_text());
		}
		introduction_content.setText(institution.getData().getIns_desc());
		class_time.setText(institution.getData().getBusiness_hours());
		phone_content.setText(institution.getData().getContact_info());
		traffic_content.setText(institution.getData().getTraffic_info());
		list = new ArrayList<InstitutionTeacher.Datas>();
		gridAdapter = new HomeCenterAdapter(getActivity(),list,institution.getData().getIns_id());
		gridView.setAdapter(gridAdapter);
		getInstitutionTeacher();
		getInstitutionHome();
		// 传值
//		getArguments().getInt("position", 1);
	}

	private void getInstitutionTeacher(){
		AbRequestParams params = new AbRequestParams();
		params.put("insid", institution.getData().getIns_id());
		mAbHttpUtil.post(LCConstant.GET_INSTITUTION_TEACHER, params,
				new AbStringHttpResponseListener() {

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(int statusCode, String content, Throwable error) {
						//                        showToast("网络连接失败");
//						isNoNet();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						ins_teacher = JSON.parseObject(content, InstitutionTeacher.class);

						if( null != ins_teacher ){
							try {
								list.addAll(ins_teacher.getData());
								gridAdapter.notifyDataSetChanged();
							} catch (NumberFormatException e) {
								Toast.makeText(getActivity(),ins_teacher.getErrorMessage()+"",Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
						} else {
							Toast.makeText(getActivity(),content+"",Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void getInstitutionHome(){
		AbRequestParams params = new AbRequestParams();
		params.put("insid", institution.getData().getIns_id());
		mAbHttpUtil.post(LCConstant.GET_INSTITUTION_HOME_NEWS, params,
				new AbStringHttpResponseListener() {

					@Override
					public void onStart() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onFailure(int statusCode, String content, Throwable error) {
						//                        showToast("网络连接失败");
//						isNoNet();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						ins_home_news = JSON.parseObject(content, InstitutionHomeNews.class);
						if( null != ins_home_news ){
							try {
								center_num.setText(ins_home_news.getData().getTotal_count());
								if( ins_home_news.getData().getList().size() > 0 ){
									for(int i=0;i<ins_home_news.getData().getList().size();i++){
										if(i==0){
											Glide.with(getActivity()).load(ins_home_news.getData().getList().get(i).getNews_pic()).into(center_img);
										}
										if(i==1){
											Glide.with(getActivity()).load(ins_home_news.getData().getList().get(i).getNews_pic()).into(center_img_two);
											center_img_two.setVisibility(View.VISIBLE);
										}
										if(i==2){
											Glide.with(getActivity()).load(ins_home_news.getData().getList().get(i).getNews_pic()).into(center_img_three);
											center_img_three.setVisibility(View.VISIBLE);
										}
										if(i==3){
											Glide.with(getActivity()).load(ins_home_news.getData().getList().get(i).getNews_pic()).into(center_img_four);
											center_img_four.setVisibility(View.VISIBLE);
										}
									}
								}
							} catch (NumberFormatException e) {
								Toast.makeText(getActivity(),ins_teacher.getErrorMessage()+"",Toast.LENGTH_SHORT).show();
								e.printStackTrace();
							}
						} else {
							Toast.makeText(getActivity(),content+"",Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		super.setMenuVisibility(menuVisible);
		if (this.getView() != null){
			this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
			if(menuVisible){
				getInstitutionHome();
			}
		}
	}

	@Override
	public void onClick(View v) {
		EventBus.getDefault().post(AbAppConfig.CENTER_FRAGMENT_POST);
	}
}
