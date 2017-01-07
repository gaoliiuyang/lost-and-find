package com.gly.collagelf.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import cn.bmob.v3.exception.BmobException;

import com.gly.collagelf.R;
import com.gly.collagelf.activity.FindMessageInfosActivity;
import com.gly.collagelf.adapter.MyMoreGridViewAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.basefragment.BaseFragment;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.utils.FindMessageInfoUtil;
import com.gly.collagelf.utils.FindMessageInfoUtil.FindMessageInfoListener;
/**
 * 分类查找信息
 */
public class MoreFragment extends BaseFragment implements BaseInterface{

	private GridView moreGridView;
	// 数据源
	private String[] names = {"一卡通", "手机", "衣服" ,"饰品","钱包","书本","钥匙","宠物","其他"};
	private int[] imgs = { R.drawable.yikatong,R.drawable.shouji, R.drawable.yifu,R.drawable.shipin,R.drawable.qianbao,R.drawable.shu,R.drawable.yaoshi,R.drawable.chongwu,R.drawable.qita};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_more, null);
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		initOperat();
	}


	@Override
	public void initView() {
		moreGridView = (GridView) act.findViewById(R.id.frag_more_gridview);
	}

	@Override
	public void initData() {
		//GridView操作
		moreGridView.setAdapter(new MyMoreGridViewAdapter(act,names,imgs));
		moreGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View v, int position,
					long arg3) {
				final String messageGoodsType = names[position];
				FindMessageInfoUtil.findMessageInfos(4, messageGoodsType, 0, 0, new FindMessageInfoListener() {
					
					@Override
					public void getMessageInfo(List<MessageInfo> messageInfos, BmobException ex) {
						if (ex == null) {
							Intent intent = new Intent(act, FindMessageInfosActivity.class);
							MyApplication.putData("messageInfos", messageInfos);
							MyApplication.putData("messageGoodsType", messageGoodsType);
							startActivity(intent);
						}
					}
				});
			}
		});
	}

	@Override
	public void initOperat() {
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
}
