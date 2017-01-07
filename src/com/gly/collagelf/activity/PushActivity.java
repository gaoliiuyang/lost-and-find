package com.gly.collagelf.activity;

import com.gly.collagelf.R;
import com.gly.collagelf.R.layout;
import com.gly.collagelf.R.menu;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 推送详情页面
 * @author 高留洋
 *
 */
public class PushActivity extends BaseActivity implements BaseInterface{

	private ImageButton push_back;
	private TextView push_title,push_message;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_push);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		push_back = (ImageButton) findViewById(R.id.act_push_back);
		push_title = (TextView) findViewById(R.id.act_push_title);
		push_message = (TextView) findViewById(R.id.act_push_message);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		String message = intent.getStringExtra("message");
		push_title.setText(title);
		push_message.setText(message);
	}

	@Override
	public void initOperat() {
		push_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(PushActivity.this, HomeActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		Intent intent = new Intent(PushActivity.this, HomeActivity.class);
		startActivity(intent);
	}

}
