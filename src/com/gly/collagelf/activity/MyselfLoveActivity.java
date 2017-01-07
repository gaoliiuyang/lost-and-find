package com.gly.collagelf.activity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;

import com.gly.collagelf.R;
import com.gly.collagelf.R.layout;
import com.gly.collagelf.R.menu;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.MessageInfo;
import com.gly.collagelf.bean.Person;
import com.gly.collagelf.utils.FindMessageInfoUtil;
import com.gly.collagelf.utils.FindMessageInfoUtil.FindMessageInfoListener;
import com.gly.collagelf.utils.FindPersonInfoUtil;
import com.gly.collagelf.utils.FindPersonInfoUtil.FindPersonInfoListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
/**
 * 我的收藏页面
 * @author 高留洋
 *
 */
public class MyselfLoveActivity extends BaseActivity implements BaseInterface{

	private ImageView myLoveBack;
	private ListView myLoveListView;
	
	private int screenWidth;// 屏幕宽度
	private List<MessageInfo> myLoveMessageInfos = new ArrayList<MessageInfo>();
	private HomeAdapter myLoveHomeAdapter;// 适配器
	
	private List<String> praiseMessageIds;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_myself_love);
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		myLoveBack = (ImageView) findViewById(R.id.act_myself_love_back);
		myLoveListView = (ListView) findViewById(R.id.act_myself_love_lv);
	}

	@Override
	public void initData() {
		//返回
		myLoveBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void initOperat() {
		FindPersonInfoUtil.findPersonInfo(MyApplication.person.getObjectId(), new FindPersonInfoListener() {
			
			@Override
			public void getPersonInfo(List<Person> person) {
				praiseMessageIds = new ArrayList<String>();
				praiseMessageIds = person.get(0).getPraiseAction();
				log("结果是："+praiseMessageIds);
//				toast(MyApplication.person.getObjectId()+"---------");
//				toast(praiseMessageIds+"---------");
				
				for (int i = 0; i < praiseMessageIds.size(); i++) {
					FindMessageInfoUtil.findMessageInfos(3, praiseMessageIds.get(i), 0, 0, new FindMessageInfoListener() {
						
						@Override
						public void getMessageInfo(List<MessageInfo> messageInfos, BmobException ex) {
							if (ex == null) {
//								log("messageInfos的结果是："+messageInfos.toString());
								myLoveMessageInfos.addAll(messageInfos);
//								myLoveMessageInfos = messageInfos;
								log("myLoveMessageInfos的结果是："+myLoveMessageInfos.toString());
								myLoveHomeAdapter = new HomeAdapter(MyselfLoveActivity.this, myLoveMessageInfos,screenWidth);
								myLoveListView.setAdapter(myLoveHomeAdapter);
							}else {
								log("收藏错误"+ex.getMessage());
							}
						}
					});
				}
			}
		});
		
	}

}
