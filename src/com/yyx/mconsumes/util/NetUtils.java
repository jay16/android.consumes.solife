package com.yyx.mconsumes.util;

import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	/**
	 * 与服务器发送请求，得到数据
	 * 
	 * @param url
	 * @return
	 */
	public static Object post(URL url) {
		return new Object();
	}
/**
 * 判断是否有网络
 * @param context
 * @return
 */
	public static boolean hasNetWork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			return false;
		}
		return true;
	}
}
