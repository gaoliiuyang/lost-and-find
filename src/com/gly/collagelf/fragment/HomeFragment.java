package com.gly.collagelf.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import cn.bmob.v3.exception.BmobException;

import com.gly.collagelf.R;
import com.gly.collagelf.activity.AddMessageActivity;
import com.gly.collagelf.activity.CurrItemMessageActivity;
import com.gly.collagelf.activity.SearchMessageActivity;
import com.gly.collagelf.adapter.FragmentHomeViewPagerAdapter;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baidumap.StationMapActivity;
import com.gly.collagelf.basefragment.BaseFragment;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.myview.XListView;
import com.gly.collagelf.myview.XListView.IXListViewListener;
import com.gly.collagelf.utils.FindMessageInfoUtil;
import com.gly.collagelf.utils.FindMessageInfoUtil.FindMessageInfoListener;
/**
 * 首页对应的Fragment
 * @author 高留洋
 *
 */
public class HomeFragment extends BaseFragment implements OnClickListener {

	private LinearLayout anim_ll, xun_ll, ling_ll, xiansuo_ll;
	
	// ViewPage,滚动图片资源
	private ViewPager fragViewPager;
	private int[] imags = { R.drawable.ping1, R.drawable.ping2,
			R.drawable.ping3 };
	private List<View> views;
	
	//XListView
	private XListView home_lv;
	private int screenWidth;//屏幕宽度
	
	//活动信息的集合，用于暂时存放从服务器获取到的数据
	private List<MessageInfo> homeMessageInfos = new ArrayList<MessageInfo>();
	private HomeAdapter myHomeAdapter;//适配器
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, null);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		screenWidth = act.getWindowManager().getDefaultDisplay().getWidth();
		initView();
		initOperat();
		initData();
	}

	// 控件的初始化操作
	private void initView() {
		// viewPager
		fragViewPager = (ViewPager) act
				.findViewById(R.id.fragment_home_viewpager);

		home_lv = (XListView) act.findViewById(R.id.home_lv);
		// 四个按钮
		anim_ll = (LinearLayout) act.findViewById(R.id.search_ll);
		xun_ll = (LinearLayout) act.findViewById(R.id.xun_ll);
		ling_ll = (LinearLayout) act.findViewById(R.id.ling_ll);
		xiansuo_ll = (LinearLayout) act.findViewById(R.id.station_ll);
		anim_ll.setOnClickListener(this);
		xun_ll.setOnClickListener(this);
		ling_ll.setOnClickListener(this);
		xiansuo_ll.setOnClickListener(this);
	}
	
	// 数据的初始化
	private void initData() {
		// ListView适配器
		myHomeAdapter = new HomeAdapter(act,homeMessageInfos,screenWidth);
		home_lv.setAdapter(myHomeAdapter);
		
		home_lv.setOnItemClickListener(new OnItemClickListener() {

			//parent是父视图，view为当前视图，position是当前视图在adpter中位置，id是当前视图View的ID.
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(act, CurrItemMessageActivity.class);
				intent.putExtra("index", position);
//				toast("mYPositon=="+position);
				act.startActivity(intent);
			}
		});

		//XListView的下拉刷新和加载更多
		home_lv.setPullRefreshEnable(true);
		home_lv.setPullLoadEnable(true);
		home_lv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				//下拉刷新
				FindMessageInfoUtil.findMessageInfos(1, null, 0, 10, new FindMessageInfoListener() {
					
					@Override
					public void getMessageInfo(List<MessageInfo> messageInfos, BmobException ex) {
						if (ex == null) {
							//跟新适配器的数据源
							myHomeAdapter.updateInfos(messageInfos);
							toast("刷新成功："+messageInfos.size());
							log("刷新成功："+messageInfos);
						}else {
							log("刷新失败"+ex.getMessage());
						}
						//关闭刷新
						home_lv.stopRefresh();
					}
				});
			}
			
			@Override
			public void onLoadMore() {
				//加载更多,忽略上次加载的n条数据
				FindMessageInfoUtil.findMessageInfos(2, null, homeMessageInfos.size(), 0, new FindMessageInfoListener() {
					
					@Override
					public void getMessageInfo(List<MessageInfo> messageInfos, BmobException ex) {
						if (ex == null) {
							homeMessageInfos.addAll(messageInfos);
							//跟新适配器的数据源
							myHomeAdapter.updateInfos(homeMessageInfos);
						}else {
							log("加载更多失败"+ex.getMessage());
						}
						//关闭加载更多
						home_lv.stopLoadMore();
					}
				});
			}
		});
		
		// viewPager的适配器
		views = new ArrayList<View>();
	      LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
	            LinearLayout.LayoutParams.WRAP_CONTENT);

	      for (int i = 0; i < imags.length; i++) {
	         ImageView iv = new ImageView(act);
	         iv.setLayoutParams(mParams);
	         iv.setImageResource(imags[i]);
	         iv.setScaleType(ScaleType.FIT_XY);
	         views.add(iv);
	      }
	      fragViewPager.setAdapter(new FragmentHomeViewPagerAdapter(views));
		  picScrool();
	}
	/**
	 * 控件的操作
	 */
	public void initOperat() {
		/**
		 * 查找所有的信息
		 */
		FindMessageInfoUtil.findMessageInfos(1, null, 0, 0, new FindMessageInfoListener() {
			
			@Override
			public void getMessageInfo(List<MessageInfo> messageInfos, BmobException ex) {
				if (ex == null) {
					//把从服务器拿到的数据给全局信息集合
					homeMessageInfos = messageInfos;
					//跟新适配器的数据源
					myHomeAdapter.updateInfos(messageInfos);
				}else {
					log("查找所有结果错误"+ex.getMessage());
				}
			}
		});
	
	}


	@Override
	// 按钮点击事件
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_ll:
			startActivity(new Intent(act, SearchMessageActivity.class));
			break;

		case R.id.xun_ll:

			startActivity(new Intent(act, AddMessageActivity.class));
			MyApplication.putData("messageType", "失物");
			break;
		case R.id.ling_ll:

			startActivity(new Intent(act, AddMessageActivity.class));
			MyApplication.putData("messageType", "招领");
			break;
		case R.id.station_ll:
			startActivity(new Intent(act, StationMapActivity.class));
			break;
		}
	}
	
	/**
	 * 3秒切换一次图片
	 */
	private void picScrool() {
		AsyncTask<Void, Integer, Void> task = new AsyncTask<Void, Integer, Void>(){
			
			@Override
			protected Void doInBackground(Void... params) {
				for (int i = 0; i < Integer.MAX_VALUE; i++) {
					try {
						
						Thread.sleep(3000);
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					publishProgress(i);
				}
				
				return null;
			}
			
			
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				fragViewPager.setCurrentItem(values[0]);
			}
		};
		task.execute();
	}
	@Override
	public void onStart() {
		initOperat();
		super.onStart();
	}
}
