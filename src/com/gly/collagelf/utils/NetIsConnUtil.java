package com.gly.collagelf.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断当前网络是否连接
 * @author 高留洋
 *
 */
public class NetIsConnUtil {

	/**
	 * 判断当前网络是否连接
	 * @param context  上下文
	 * @return  true为连接，false为未连接
	 */
	public static boolean netIsConn(Context context){
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		//当NetworkInfo为空时，网络未连接，不为空时可能未连接
		if (info == null) {
			return false;
		}
		return info.isConnected();
	}
}
