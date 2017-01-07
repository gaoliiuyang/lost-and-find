package com.gly.collagelf.baidumap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.gly.collagelf.R;
import com.gly.collagelf.baseactivity.BaseActivity;
import com.gly.collagelf.baseinterface.BaseInterface;

/**
 * 失物招领站点位置
 * 
 * @author 高留洋
 * 
 */
public class StationMapActivity extends BaseActivity implements BaseInterface {

	private ImageView stationBack;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_station_map);
		initView();
		initData();
		initOperat();
	}

	@Override
	public void initView() {
		stationBack = (ImageView) findViewById(R.id.act_station_back_iv);
	}

	@Override
	public void initData() {
		stationBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void initOperat() {

	}
}
