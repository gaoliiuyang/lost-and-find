package com.gly.collagelf.activity;

import java.util.List;

import com.gly.collagelf.R;
import com.gly.collagelf.adapter.HomeAdapter;
import com.gly.collagelf.application.MyApplication;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;
import com.gly.collagelf.bean.MessageInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 查询活动的Activity
 * @author 高留洋
 *
 */
public class FindMessageInfosActivity extends BaseActivity implements BaseInterface{

	private TextView findInfoTv;
	private ListView findInfoLv;
	private ImageView findBack;
	
	private LinearLayout beishangLL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_find_action_infos);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		findInfoTv = (TextView) findViewById(R.id.act_find_infos_tv);
		findInfoLv = (ListView) findViewById(R.id.act_find_infos_lv);
		findBack = (ImageView) findViewById(R.id.act_find_infos_back_btn);
		
		beishangLL = (LinearLayout) findViewById(R.id.act_find_beishang_ll);
	}

	@Override
	public void initData() {
		
		//从全局获取数据
		String messageGoodsType = (String) MyApplication.getDate(true, "messageGoodsType");
		findInfoTv.setText("查询"+messageGoodsType+"的结果");
		List<MessageInfo> infos = (List<MessageInfo>) MyApplication.getDate(true, "messageInfos");
		if (infos.size()==0) {
			beishangLL.setVisibility(View.VISIBLE);
			findInfoLv.setVisibility(View.GONE);
		}else {
			findInfoLv.setAdapter(new HomeAdapter(this, infos, getWindowManager().getDefaultDisplay().getWidth()));
			findInfoLv.setFocusable(true);
			findInfoLv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					Intent intent = new Intent(FindMessageInfosActivity.this, CurrItemMessageActivity.class);
					intent.putExtra("index", position+1);
					startActivity(intent);
				}
			});
		}
		
	}

	@Override
	public void initOperat() {
		//点击返回键，返回上个页面
		findBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
