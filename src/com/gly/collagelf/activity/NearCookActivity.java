package com.gly.collagelf.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import cn.bmob.v3.exception.BmobException;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.NearCookAdapter;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.Cook;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.myview.XListView;
import com.gly.collagelf.myview.XListView.IXListViewListener;
import com.gly.collagelf.utils.FindCookInfoUtil;
import com.gly.collagelf.utils.FindMessageInfoUtil;
import com.gly.collagelf.utils.FindCookInfoUtil.FindCookInfoListener;
import com.gly.collagelf.utils.FindMessageInfoUtil.FindMessageInfoListener;
/**
 * 附近美食
 * @author 高留洋
 *
 */
public class NearCookActivity extends BaseActivity implements BaseInterface{

	private ImageView cBack;
	private XListView cLv;
	
	private List<Cook> myCookInfos = new ArrayList<Cook>();
	private NearCookAdapter cookAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_near_cook);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		cBack = (ImageView) findViewById(R.id.cook_back);
		cLv = (XListView) findViewById(R.id.cook_lv);
	}

	@Override
	public void initData() {
		cookAdapter = new NearCookAdapter(this,myCookInfos);
		cLv.setAdapter(cookAdapter);
		//查询所有信息
		FindCookInfoUtil.findCookInfo(1, null, 0, 0, new FindCookInfoListener() {
			
			@Override
			public void getCookInfo(List<Cook> cookInfos, BmobException ex) {
				if (ex == null) {
					//把从服务器拿到的数据给全局信息集合
					myCookInfos = cookInfos;
					//跟新适配器的数据源
					cookAdapter.updateInfos(myCookInfos);
				}else {
					log("查找附近美食结果错误"+ex.getMessage());
				}
			}
		});
		
		//XListView的下拉刷新和加载更多
		cLv.setPullRefreshEnable(true);
		cLv.setPullLoadEnable(true);
		cLv.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				//下拉刷新
				FindCookInfoUtil.findCookInfo(1, null, 0, 10, new FindCookInfoListener() {
					
					@Override
					public void getCookInfo(List<Cook> cookInfos, BmobException ex) {
						if (ex == null) {
							//跟新适配器的数据源
							cookAdapter.updateInfos(cookInfos);
							toast("刷新成功："+cookInfos.size());
						}else {
							log("刷新失败"+ex.getMessage());
						}
						//关闭刷新
						cLv.stopRefresh();
						
					}
				});
			}
			
			@Override
			public void onLoadMore() {
				//加载更多,忽略上次加载的n条数据
				FindCookInfoUtil.findCookInfo(2, null, myCookInfos.size(), 10, new FindCookInfoListener() {
					
					@Override
					public void getCookInfo(List<Cook> cookInfos, BmobException ex) {
						if (ex == null) {
							myCookInfos.addAll(cookInfos);
							//跟新适配器的数据源
							cookAdapter.updateInfos(myCookInfos);
						}else {
							log("加载更多失败"+ex.getMessage());
						}
						//关闭加载更多
						cLv.stopLoadMore();
						
					}
				});
			}
		});
	}

	@Override
	public void initOperat() {
		cBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
