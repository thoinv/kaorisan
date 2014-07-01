package com.kaorisan.common;

import com.kaorisan.dataLayer.CacheData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	public static int NETWORK_STATE = -1;
	public static int TYPE_WIFI = 1;
	public static int TYPE_MOBILE = 2;
	public static int TYPE_NOT_CONNECTED = 0;

	public static int getConnectivityStatus(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (null != activeNetwork) {
			if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
				return TYPE_WIFI;

			if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
				return TYPE_MOBILE;
		}
		return TYPE_NOT_CONNECTED;
	}

	public static String getConnectivityStatusString(Context context) {
		int conn = NetworkUtil.getConnectivityStatus(context);
		String status = null;
		if (conn == NetworkUtil.TYPE_WIFI) {
			NETWORK_STATE = NetworkUtil.TYPE_WIFI;
			status = "Wifi enabled";
		} else if (conn == NetworkUtil.TYPE_MOBILE) {
			NETWORK_STATE = NetworkUtil.TYPE_MOBILE;
			status = "Mobile data enabled";
		} else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
			NETWORK_STATE = NetworkUtil.TYPE_NOT_CONNECTED;
			if (CacheData.getInstant().getCurrentProgressDialog() != null) {
				CacheData.getInstant().getCurrentProgressDialog().dismiss();
				CacheData.getInstant().setCurrentProgressDialog(null);
			}
			status = "Not connected to Internet";
		}
		return status;
	}
}