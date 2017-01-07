package com.gly.collagelf.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

import com.gly.collagelf.R;
import com.gly.collagelf.activity.CurrItemMessageActivity;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.basefragment.BaseFragment;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.myview.XListView;
import com.gly.collagelf.myview.XListView.IXListViewListener;
import com.gly.collagelf.utils.FindMessageInfoUtil;
import com.gly.collagelf.utils.FindMessageInfoUtil.FindMessageInfoListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FindFragment extends BaseFragment {

	private XListView find_lv;
	private List<MessageInfo> findMessageInfos = new ArrayList<MessageInfo>();
	private int screenWidth;// 屏幕宽度
	private HomeAdapter myHomeAdapter;// 适配器

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_find, null);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		screenWidth = act.getWindowManager().getDefaultDisplay().getWidth();
		find_lv = (XListView) act.findViewById(R.id.find_lv);
		initData();
		initOperat();
	}

	/**
	 * 控件数据的初始化
	 */
	private void initData() {
		// ListView适配器
		myHomeAdapter = new HomeAdapter(act, findMessageInfos, screenWidth);
		find_lv.setAdapter(myHomeAdapter);
		
		find_lv.setOnItemClickListener(new OnItemClickListener() {

			//parent是父视图，view为当前视图，position是当前视图在adpter中位置，id是当前视图View的ID.
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(act, CurrItemMessageActivity.class);
				intent.putExtra("index", position);
				act.startActivity(intent);
			}
		});

		// XListView的下拉刷新和加载更多
		find_lv.setPullRefreshEnable(true);
		find_lv.setPullLoadEnable(true);
		find_lv.setXListViewListener(new IXListViewListener() {

			@Override
			public void onRefresh() {
				// 下拉刷新
				FindMessageInfoUtil.findMessageInfos(6, "招领", 0, 0,
						new FindMessageInfoListener() {

							@Override
							public void getMessageInfo(
									List<MessageInfo> messageInfos,
									BmobException ex) {
								// 关闭刷新
								find_lv.stopRefresh();
								// 跟新适配器的数据源
								myHomeAdapter.updateInfos(messageInfos);
							}
						});
			}

			@Override
			public void onLoadMore() {
				// 加载更多,忽略上次加载的n条数据
				FindMessageInfoUtil.findMessageInfos(7, "招领",
						findMessageInfos.size(), 0,
						new FindMessageInfoListener() {

							@Override
							public void getMessageInfo(
									List<MessageInfo> messageInfos,
									BmobException ex) {
								if (ex == null) {
									findMessageInfos.addAll(messageInfos);
									// 跟新适配器的数据源
									myHomeAdapter.updateInfos(findMessageInfos);
								} else {
									log("加载更多失败" + ex.getMessage());
								}
								// 关闭加载更多
								find_lv.stopLoadMore();
							}
						});
			}
		});
	}

	/**
	 * 控件的操作
	 */
	public void initOperat() {
		/**
		 * 查找所有的信息
		 */
		FindMessageInfoUtil.findMessageInfos(5, "招领", 0, 0,
				new FindMessageInfoListener() {

					@Override
					public void getMessageInfo(List<MessageInfo> messageInfos,
							BmobException ex) {
						if (ex == null) {
							// 把从服务器拿到的数据给全局信息集合
							findMessageInfos = messageInfos;
							// 跟新适配器的数据源
							myHomeAdapter.updateInfos(findMessageInfos);
						}
					}
				});

	}
	@Override
	public void onStart() {
		initOperat();
		super.onStart();
	}
}
