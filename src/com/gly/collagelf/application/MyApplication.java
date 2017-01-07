package com.gly.collagelf.application;

import java.util.HashMap;
import java.util.Map;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.gly.collagelf.bean.Person;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;
import android.app.Application;

/**
 * 缓存数据
 * 
 * 全局数据的初始化
 * 
 * @author 高留洋
 * 
 */
public class MyApplication extends Application {

	// 地图
	public static LocationClient mLocationClient = null;
	private static String MyAddress;
	private static double Longitude;
	private static double Latitude;

	public static String getAddress() {
		String str = MyAddress;
		return str;
	}

	public static double getLongitude() {
		double longitude = Longitude;
		return longitude;
	}

	public static double getLatitude() {
		double latitude = Latitude;
		return latitude;
	}

	// 维护一个静态的Person,用来存放用户名和密码
	public static Person person;

	// 维护一个map集合，用来存放数据
	static Map datas = new HashMap<String, Object>();

	/**
	 * 往全局中存放数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static Object putData(String key, Object value) {
		return datas.put(key, value);
	}

	/**
	 * 获取全局数据
	 * 
	 * @param idDelete
	 *            是否删除
	 * @param key
	 *            要查询的key
	 * @return
	 */
	public static Object getDate(boolean idDelete, String key) {
		if (idDelete) {
			return datas.remove(key);
		}
		return datas.get(key);
	}

	//微信分享
	private static final String APP_ID = "wxfdeafa10cb4c1bf2";
	public static IWXAPI api;

	public void registerToWx() {
		api = WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		//推送
		JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
		
		//分享
		registerToWx();
		
		// 第一：默认初始化
		Bmob.initialize(this, "9021a5c111cf4365a27bac7f5d8e0d5b");
		// SMS初始化
		BmobSMS.initialize(this, "9021a5c111cf4365a27bac7f5d8e0d5b");

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		SDKInitializer.initialize(getApplicationContext());
		// 第一步，初始化LocationClient类
		mLocationClient = new LocationClient(getApplicationContext()); //
		// 声明LocationClient类
		// 第二步:注册. 实现BDLocationListener接口
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				String address = location.getAddrStr();
				Longitude = location.getLongitude();
				Latitude = location.getLatitude();
				MyAddress = address;
				// Log.i("myTag", "address:" + address);
				// System.out.println("address=="+address);

			}
		});
		// 配置定位的参数
		initLocation();
		// 第三步:开启定位服务
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		mLocationClient.setLocOption(option);
	}
}
