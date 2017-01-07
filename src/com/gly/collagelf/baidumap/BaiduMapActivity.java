package com.gly.collagelf.baidumap;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.gly.collagelf.R;
import com.gly.collagelf.application.MyApplication;

public class BaiduMapActivity extends Activity {

	private MapView mapView;
	private EditText et;
	private Button btn;
	private BaiduMap baiduMap;
	private PoiSearch mPoiSearch;
	private LatLng xdlLocation;//拿到定位到的当前位置，设置为中心点
	private String btnMessage;
	private Double longitude;//经度
	private double latitude;//纬度
	private List<PoiInfo> pois;
	private PoiResult result2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidumap);
		mapView = (MapView) findViewById(R.id.bmapView);
		et = (EditText) findViewById(R.id.search_et);
		btn = (Button) findViewById(R.id.search_btn);
		
		
		//获取定位到的当前的地址以及经度和纬度
		btnMessage = MyApplication.getAddress();
		longitude = MyApplication.getLongitude();
		latitude = MyApplication.getLatitude();
		while (btnMessage == null) {
			btnMessage = MyApplication.getAddress();
			longitude = MyApplication.getLongitude();
			latitude = MyApplication.getLatitude();
		}
//		Toast.makeText(this, "address:"+btnMessage+longitude+".."+latitude, 0).show();
		xdlLocation = new LatLng(latitude, longitude);
		
		// 百度地图的控制器
		baiduMap = mapView.getMap();
		
		//给地图设置属性
		sheZhiShuXing();
		//添加覆盖物
		addMaker();
		// 第一步，创建POI检索实例
		mPoiSearch = PoiSearch.newInstance();

		// 第二步，创建POI检索监听者,设置POI检索监听者；
		mPoiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {


					// 获取POI检索结果
					@Override
					public void onGetPoiResult(PoiResult result) {
						if (result == null) {
							return;
						}

						result2 = result;
						pois = result.getAllPoi();
						baiduMap.clear();
						// 创建PoiOverlay
						PoiOverlay overlay = new MyPoiOverlay(baiduMap);
						// 设置overlay可以处理标注点击事件
						baiduMap.setOnMarkerClickListener(overlay);
						// 设置PoiOverlay数据
						overlay.setData(result);
						// 添加PoiOverlay到地图中
						overlay.addToMap();
						overlay.zoomToSpan();
						return;
					}

					@Override
					public void onGetPoiIndoorResult(PoiIndoorResult arg0) {

					}

					// 获取Place详情页检索结果
					@Override
					public void onGetPoiDetailResult(PoiDetailResult arg0) {

					}
				});

		// 第四步，释放POI检索实例；
//		mPoiSearch.destroy();
	}

	private void addMaker() {
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.map_localtion);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions option = new MarkerOptions().position(xdlLocation).icon(
				bitmap);
		// 在地图上添加Marker，并显示
		baiduMap.addOverlay(option);

	}

	private void sheZhiShuXing() {
		/**
		 * 设置地图显示的中心点
		 * 参数:坐标LatLng(纬度,经度)
		 */
		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
		.newLatLng(xdlLocation);
		// 更新地图数据(中心点)
		baiduMap.setMapStatus(mapStatusUpdate);
		MapStatusUpdate mapStatusUpdate1 = MapStatusUpdateFactory.zoomBy(4);
		// 更新地图数据(缩放级别)
		baiduMap.setMapStatus(mapStatusUpdate1);
		
	}

	public void searchClick(View v){
		// 第三步，发起检索请求；
		mPoiSearch.searchInCity((new PoiCitySearchOption()).city("北京")
				.keyword(et.getText().toString().trim()).pageNum(0));
	}
	
	// 第一步，构造自定义 PoiOverlay 类；
		private class MyPoiOverlay extends PoiOverlay {
			public MyPoiOverlay(BaiduMap baiduMap) {
				super(baiduMap);
			}

			/**
			 * 获取用户选择的地点,并回显到上个界面
			 */
			@Override
			public boolean onPoiClick(int index) {
				super.onPoiClick(index);
				
				String mapAddress= pois.get(index).address;
				final double actionLong = pois.get(index).location.longitude;
				final double actionLa = pois.get(index).location.latitude;
				final String mapName = pois.get(index).name;
				final String mapCity = pois.get(index).city;
				//创建InfoWindow展示的view  
				final View popView = getLayoutInflater().inflate(R.layout.baidumap_pop, null); 
				//获取弹出框中的控件,并设置内容
				TextView name = (TextView) popView.findViewById(R.id.map_pop_name_tv);
				name.setText(mapName);
				TextView desc = (TextView) popView.findViewById(R.id.map_pop_desc_tv);
				desc.setText(mapAddress);
				Button cancleBtn = (Button) popView.findViewById(R.id.map_pop_cancle_btn);
				//取消
				cancleBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						mBaiduMap.hideInfoWindow();
					}
				});
				Button selectBtn = (Button) popView.findViewById(R.id.map_pop_select_btn);
				//确定，点击完后把当前的地址给到上个页面，结束当前页面
				selectBtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra("name", mapName);
						intent.putExtra("city", mapCity);
						intent.putExtra("actionLong", actionLong);
						intent.putExtra("actionLa", actionLa);
						setResult(555, intent);
//						MyApplication.mLocationClient.stop();
						finish();
					}
				});
				InfoWindow mInfoWindow = new InfoWindow(popView, pois.get(index).location, -47);  
				//显示InfoWindow  
				mBaiduMap.showInfoWindow(mInfoWindow);
				return true;
			}
		}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapView.onPause();
	}
}
